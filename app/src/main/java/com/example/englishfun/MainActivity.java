package com.example.englishfun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.englishfun.databinding.ActivityMainBinding;
import com.example.englishfun.ui.login.UserInfo;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private UserInfo userInfo;
    private static final String AUTH_PREFS = "AuthPrefs";
    private static final String AUTH_TOKEN_KEY = "auth_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            // Set up the bottom navigation only for main app screens
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications
            ).build();

            // Only show bottom navigation for main app screens
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                boolean isMainScreen = destination.getId() == R.id.navigation_home ||
                                     destination.getId() == R.id.navigation_dashboard ||
                                     destination.getId() == R.id.navigation_notifications;
                
                binding.navView.setVisibility(isMainScreen ? View.VISIBLE : View.GONE);
            });

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            // Check for existing auth token
            String authToken = getAuthToken();
            if (authToken != null && !authToken.isEmpty()) {
                // Token exists, navigate to home screen
                binding.navView.setVisibility(View.VISIBLE);
                userInfo = UserInfo.getInstance(0L); // Initialize user info
            }
        }
    }

    public String getAuthToken() {
        return getSharedPreferences(AUTH_PREFS, MODE_PRIVATE)
                .getString(AUTH_TOKEN_KEY, null);
    }

    public void saveAuthToken(String token) {
        getSharedPreferences(AUTH_PREFS, MODE_PRIVATE)
                .edit()
                .putString(AUTH_TOKEN_KEY, token)
                .apply();
    }

    public void clearAuthToken() {
        getSharedPreferences(AUTH_PREFS, MODE_PRIVATE)
                .edit()
                .remove(AUTH_TOKEN_KEY)
                .apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }
}