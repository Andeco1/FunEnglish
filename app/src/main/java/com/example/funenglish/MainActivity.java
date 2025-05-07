package com.example.funenglish;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.funenglish.fragments.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        nav.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_dashboard: fragment = new DashboardFragment(); break;
                case R.id.nav_lessons:   fragment = new LessonsFragment();  break;
                case R.id.nav_tests:     fragment = new TestsFragment();    break;
                case R.id.nav_reading:   fragment = new ReadingFragment();  break;
                case R.id.nav_vocab:     fragment = new VocabularyFragment();break;
                case R.id.nav_profile:   fragment = new ProfileFragment();   break;
                default:                 fragment = new DashboardFragment();break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        });
        if (savedInstanceState == null) nav.setSelectedItemId(R.id.nav_dashboard);
    }
}
