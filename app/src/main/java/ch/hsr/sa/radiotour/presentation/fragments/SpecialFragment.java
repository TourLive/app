package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;

public class SpecialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","SpecialFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_special, container, false);
        return root;
    }
}
