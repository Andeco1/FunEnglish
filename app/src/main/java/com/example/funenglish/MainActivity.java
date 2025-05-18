package com.example.funenglish;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.funenglish.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_lessons) {
                selectedFragment = new LessonsFragment();
            } else if (itemId == R.id.nav_tests) {
                selectedFragment = new TestsFragment();
            } else if (itemId == R.id.nav_reading) {
                selectedFragment = new ReadingFragment();
            } else if (itemId == R.id.nav_vocabulary) {
                selectedFragment = new VocabularyFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
                return true;
            }
            return false;
        });

        // Set default fragment
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
        }
    }
}