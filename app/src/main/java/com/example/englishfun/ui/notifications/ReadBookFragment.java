package com.example.englishfun.ui.notifications;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishfun.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadBookFragment extends Fragment {
    private static final String ARG_FILE_PATH = "file_path";
    private static final String DICTIONARY_API_BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private RequestQueue requestQueue;
    private AlertDialog currentDialog;
    private MediaPlayer mediaPlayer;

    public static ReadBookFragment newInstance(String filePath) {
        ReadBookFragment fragment = new ReadBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_read_book, container, false);
        TextView contentView = root.findViewById(R.id.book_content);

        if (getArguments() != null) {
            String filePath = getArguments().getString(ARG_FILE_PATH);
            if (filePath != null) {
                try {
                    String content = readFile(filePath);
                    makeTextClickable(contentView, content);
                } catch (IOException e) {
                    contentView.setText("Error reading file: " + e.getMessage());
                }
            }
        }

        return root;
    }

    private void makeTextClickable(TextView textView, String text) {
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile("\\b[a-zA-Z]+\\b");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            final String word = matcher.group();
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    lookupWord(word);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(android.R.color.black, requireContext().getTheme()));
                }
            }, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void lookupWord(String word) {
        showLoadingDialog();
        String url = DICTIONARY_API_BASE_URL + word.toLowerCase();

        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            response -> {
                try {
                    if (response.length() > 0) {
                        JSONObject wordData = response.getJSONObject(0);
                        String phonetic = wordData.optString("phonetic", "");
                        String audioUrl = null;
                        
                        // Get audio URL from phonetics array
                        JSONArray phonetics = wordData.getJSONArray("phonetics");
                        for (int i = 0; i < phonetics.length(); i++) {
                            JSONObject phoneticObj = phonetics.getJSONObject(i);
                            if (phoneticObj.has("audio") && !phoneticObj.isNull("audio")) {
                                audioUrl = phoneticObj.getString("audio");
                                break;
                            }
                        }

                        List<String> definitions = new ArrayList<>();
                        List<String> examples = new ArrayList<>();
                        
                        JSONArray meanings = wordData.getJSONArray("meanings");
                        for (int i = 0; i < meanings.length(); i++) {
                            JSONObject meaning = meanings.getJSONObject(i);
                            String partOfSpeech = meaning.getString("partOfSpeech");
                            JSONArray definitionsArray = meaning.getJSONArray("definitions");

                            for (int j = 0; j < definitionsArray.length(); j++) {
                                JSONObject def = definitionsArray.getJSONObject(j);
                                definitions.add(partOfSpeech + ": " + def.getString("definition"));
                                
                                if (def.has("example") && !def.isNull("example")) {
                                    examples.add(def.getString("example"));
                                }
                            }
                        }

                        showDefinitionDialog(word, phonetic, audioUrl, definitions, examples);
                    } else {
                        showErrorDialog(word, "Definition not found.");
                    }
                } catch (JSONException e) {
                    showErrorDialog(word, "Error parsing dictionary response.");
                }
            },
            error -> showErrorDialog(word, "Error fetching definition: " + error.getMessage())
        );

        request.setTag(this);
        requestQueue.add(request);
    }

    private void showLoadingDialog() {
        if (currentDialog != null) {
            currentDialog.dismiss();
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(R.layout.dialog_loading);
        builder.setCancelable(false);
        currentDialog = builder.create();
        currentDialog.show();
    }

    private void showDefinitionDialog(String word, String phonetic, String audioUrl, 
                                    List<String> definitions, List<String> examples) {
        if (currentDialog != null) {
            currentDialog.dismiss();
        }

        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_word_definition, null);

        TextView wordTitle = dialogView.findViewById(R.id.wordTitle);
        TextView phoneticText = dialogView.findViewById(R.id.phoneticText);
        MaterialButton playButton = dialogView.findViewById(R.id.playPronunciationButton);
        TextView definitionsText = dialogView.findViewById(R.id.definitionsText);
        TextView examplesText = dialogView.findViewById(R.id.examplesText);

        wordTitle.setText(word);
        phoneticText.setText(phonetic);
        
        if (audioUrl != null && !audioUrl.isEmpty()) {
            playButton.setVisibility(View.VISIBLE);
            playButton.setOnClickListener(v -> playPronunciation(audioUrl));
        } else {
            playButton.setVisibility(View.GONE);
        }

        StringBuilder definitionsBuilder = new StringBuilder();
        for (String def : definitions) {
            definitionsBuilder.append("• ").append(def).append("\n\n");
        }
        definitionsText.setText(definitionsBuilder.toString().trim());

        if (!examples.isEmpty()) {
            StringBuilder examplesBuilder = new StringBuilder();
            for (String example : examples) {
                examplesBuilder.append("• ").append(example).append("\n\n");
            }
            examplesText.setText(examplesBuilder.toString().trim());
        } else {
            dialogView.findViewById(R.id.examplesTitle).setVisibility(View.GONE);
            examplesText.setVisibility(View.GONE);
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setView(dialogView);
        builder.setPositiveButton("Close", (dialog, which) -> {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        currentDialog = builder.create();
        currentDialog.show();
    }

    private void showErrorDialog(String word, String message) {
        if (currentDialog != null) {
            currentDialog.dismiss();
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        builder.setTitle(word);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        currentDialog = builder.create();
        currentDialog.show();
    }

    private void playPronunciation(String audioUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Error playing pronunciation", Toast.LENGTH_SHORT).show();
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (currentDialog != null) {
            currentDialog.dismiss();
        }
    }
} 