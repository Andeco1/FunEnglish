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
    private static final String USER_INFO = "UserInfo";
    private static final String USER_ID = "UserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();

            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications,
                    R.id.navigation_profile,
                    R.id.navigation_login
            ).build();

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                boolean isMainScreen = destination.getId() == R.id.navigation_home ||
                                     destination.getId() == R.id.navigation_dashboard ||
                                     destination.getId() == R.id.navigation_notifications ||
                                     destination.getId() == R.id.navigation_profile;

                if (isMainScreen && (getAuthToken() == null || getAuthToken().isEmpty())) {
                    controller.navigate(R.id.navigation_login);
                    return;
                }
                
                binding.navView.setVisibility(isMainScreen ? View.VISIBLE : View.GONE);

                if (destination.getId() == R.id.navigation_login) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            });

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.navView, navController);

            String authToken = getAuthToken();
            if (authToken == null || authToken.isEmpty()) {

                navController.navigate(R.id.navigation_login);
            } else {
                binding.navView.setVisibility(View.VISIBLE);
                userInfo = UserInfo.getInstance(0L);
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

    public void saveUserInfo(Long userId){
        getSharedPreferences(USER_INFO,MODE_PRIVATE)
                .edit()
                .putLong(USER_ID,userId)
                .apply();
    }
    public Long getUserId(){
        return getSharedPreferences(USER_INFO, MODE_PRIVATE)
                .getLong(USER_ID,0L);
    }
    public void clearUserInfo() {
        getSharedPreferences(USER_INFO,MODE_PRIVATE)
                .edit()
                .remove(USER_ID)
                .apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (navController != null && navController.getCurrentDestination() != null) {
            if (navController.getCurrentDestination().getId() == R.id.navigation_login) {
                return false;
            }
        }
        return navController != null && navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (navController != null && navController.getCurrentDestination() != null) {
            if (navController.getCurrentDestination().getId() == R.id.navigation_login) {
                return;
            }
        }
        super.onBackPressed();
    }

    public void logout() {
        getSharedPreferences(AUTH_PREFS, MODE_PRIVATE).edit().clear().apply();
        getSharedPreferences(USER_INFO, MODE_PRIVATE).edit().clear().apply();

        deleteDatabase("english_fun_db");
        getApplicationContext().deleteDatabase("english_fun_db");

        if (userInfo != null) {
            userInfo.clearInstance();
        }

        if (navController != null) {
            navController.navigate(R.id.navigation_login);
        }
    }
}