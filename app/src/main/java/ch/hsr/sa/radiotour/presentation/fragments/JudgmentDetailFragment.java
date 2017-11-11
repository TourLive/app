package ch.hsr.sa.radiotour.presentation.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;

public class JudgmentDetailFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_maillots, container, false);
        Bundle arguments = getArguments();
        String desired_string = arguments.getString("id");
        Log.d("TAG","" + desired_string);
        return root;
    }
}
