package com.example.funenglish.fragments;

import android.os.Bundle;
import android.view.*;
import androidx.fragment.app.Fragment;

import com.example.funenglish.R;

public class DashboardFragment extends Fragment {
    @Override public View onCreateView(LayoutInflater inf,ViewGroup c,Bundle b){
        return inf.inflate(R.layout.fragment_dashboard,c,false);
    }
}