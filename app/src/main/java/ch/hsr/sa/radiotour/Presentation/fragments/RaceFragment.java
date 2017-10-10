package ch.hsr.sa.radiotour.Presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hsr.sa.radiotour.R;

/**
 * Created by Urs Forrer on 10.10.2017.
 */

public class RaceFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_race, container, false);
    }
}
