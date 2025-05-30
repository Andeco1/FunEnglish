package com.example.englishfun.ui.login;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.englishfun.MainActivity;
import com.example.englishfun.R;
import com.example.englishfun.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private static final String LOGIN_URL = "http://10.5.50.151:9090/api/users/login"; // Replace with your actual server URL
    private static final String REGISTER_URL = "http://10.5.50.151:9090/api/users/register"; // Replace with your actual server URL

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if we already have a token
        MainActivity mainActivity = (MainActivity) requireActivity();
        if (mainActivity.getAuthToken() != null) {
            // Token exists, navigate to home screen
            Navigation.findNavController(view)
                    .navigate(R.id.action_navigation_login_to_navigation_home);
            return;
        }

        binding.loginButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (validateInput(username, password)) {
                performLogin(username, password);
            }
        });

        binding.registerButton.setOnClickListener(v ->{
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (validateInput(username, password)) {
                performRegister(username, password);
            }
        });
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            binding.usernameLayout.setError("Username is required");
            return false;
        }
        binding.usernameLayout.setError(null);

        if (password.isEmpty()) {
            binding.passwordLayout.setError("Password is required");
            return false;
        }
        binding.passwordLayout.setError(null);

        return true;
    }
    private void performRegister(String username, String password){
        binding.registerButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                response -> {
                    binding.registerButton.setEnabled(true);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if(jsonResponse.getString("success").equals(false)){
                            showError("User already exists");
                        } else {
                            showError("Invalid server response");
                        }
                    } catch (JSONException e) {
                        showError("Error parsing server response");
                    }
                },
                error -> {
                    binding.registerButton.setEnabled(true);
                    showError("Register failed: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                Log.d("TESTTEST",auth);
                return headers;
            }
        };

        queue.add(stringRequest);
    }
    private void performLogin(String username, String password) {

        binding.loginButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    binding.loginButton.setEnabled(true);
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        if (jsonResponse.has("token")) {
                            // Save the token using MainActivity's method
                            String token = jsonResponse.getString("token");
                            MainActivity mainActivity = (MainActivity) requireActivity();
                            mainActivity.saveAuthToken(token);
                            
                            // Navigate to home screen
                            Navigation.findNavController(requireView())
                                    .navigate(R.id.action_navigation_login_to_navigation_home);
                        } else {
                            showError("Invalid server response");
                        }
                    } catch (JSONException e) {
                        showError("Error parsing server response");
                    }
                },
                error -> {
                    binding.loginButton.setEnabled(true);
                    showError("Login failed: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Authorization", auth);
                Log.d("TESTTEST",auth);
                return headers;
            }


        };

        queue.add(stringRequest);
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 