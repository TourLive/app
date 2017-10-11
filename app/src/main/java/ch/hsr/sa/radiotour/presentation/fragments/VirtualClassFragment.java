package ch.hsr.sa.radiotour.presentation.fragments;

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

public class VirtualClassFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
        return root;
    }
}
