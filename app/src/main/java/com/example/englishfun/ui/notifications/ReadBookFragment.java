package com.example.englishfun.ui.notifications;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishfun.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadBookFragment extends Fragment {
    private static final String ARG_FILE_PATH = "file_path";
    private static final String DICTIONARY_API_BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";
    private RequestQueue requestQueue;

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
                    contentView.setText("Ошибка чтения файла: " + e.getMessage());
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
        String url = DICTIONARY_API_BASE_URL + word.toLowerCase();

        JsonArrayRequest request = new JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            response -> {
                try {
                    if (response.length() > 0) {
                        JSONObject wordData = response.getJSONObject(0);
                        JSONArray meanings = wordData.getJSONArray("meanings");
                        StringBuilder definition = new StringBuilder();

                        for (int i = 0; i < meanings.length(); i++) {
                            JSONObject meaning = meanings.getJSONObject(i);
                            JSONArray definitions = meaning.getJSONArray("definitions");

                            for (int j = 0; j < definitions.length(); j++) {
                                JSONObject def = definitions.getJSONObject(j);
                                definition.append(def.getString("definition")).append("\n");
                            }
                        }

                        showDefinition(word, definition.toString().trim());
                    } else {
                        showDefinition(word, "Определение не найдено.");
                    }
                } catch (JSONException e) {
                    showDefinition(word, "Ошибка при разборе ответа словаря.");
                }
            },
            error -> showDefinition(word, "Ошибка при получении определения: " + error.getMessage())
        );

        request.setTag(this);
        requestQueue.add(request);
    }

    private void showDefinition(String word, String definition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(word);
        
        TextView definitionView = new TextView(requireContext());
        definitionView.setText(definition);
        definitionView.setPadding(50, 30, 50, 30);
        definitionView.setTextSize(16);
        
        android.widget.ScrollView scrollView = new android.widget.ScrollView(requireContext());
        scrollView.addView(definitionView);
        
        builder.setView(scrollView);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        
        AlertDialog dialog = builder.create();
        dialog.show();
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
    }
} 