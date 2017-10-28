package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.UrlLink;

public class ImportFragment extends Fragment implements View.OnClickListener  {
    private Button btn_Import;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_import, container, false);
        btn_Import = (Button) root.findViewById(R.id.btn_Import);
        btn_Import.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == btn_Import){
            try{
                APIClient.importData();
            } catch (Exception ex){

            }
        }
    }
}