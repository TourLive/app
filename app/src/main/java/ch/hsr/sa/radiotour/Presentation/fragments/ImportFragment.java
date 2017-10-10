package ch.hsr.sa.radiotour.Presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;

/**
 * Created by Urs Forrer on 10.10.2017.
 */

public class ImportFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_import, container, false);
        return root;
    }
}