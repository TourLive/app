package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONException;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.UrlLink;

public class ImportFragment extends Fragment implements View.OnClickListener  {
    private Button btn_Import;
    private ProgressDialog progressBar;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_import, container, false);
        btn_Import = (Button) root.findViewById(R.id.btn_Import);
        btn_Import.setOnClickListener(this);
        progressBar = new ProgressDialog(getActivity());
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == btn_Import){
            try{
                progressBar.setCancelable(true);
                progressBar.setMessage("Import starting ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBar.show();
                progressBarStatus = 0;
                new Thread(new Runnable() {
                    public void run() {
                        while (progressBarStatus < 100) {
                            try{
                                progressBarStatus = importData();
                            } catch (JSONException ex){
                                Log.d("error", ex.getMessage());
                            }
                            progressBarHandler.post(new Runnable() {
                                public void run() {
                                    progressBar.setProgress(progressBarStatus);
                                }
                            });
                        }
                        // performing operation if file is downloaded,
                        if (progressBarStatus >= 100) {
                        // sleeping for 1 second after operation completed
                        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
                        // close the progress bar dialog
                        progressBar.dismiss();
                    }
                }
                }).start();
            } catch (Exception ex){

            }
        }
    }

    private int importData() throws JSONException {
        while (progressBarStatus <= 100) {
            if (progressBarStatus < 20) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_delete_data));
                    }
                });
                APIClient.deleteData();
                return 20;
            } else if (progressBarStatus < 40) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_driver));
                    }
                });
                APIClient.getRiders();
                return 40;
            } else if (progressBarStatus < 60) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_judgment));
                    }
                });
                APIClient.getJudgments();
                return 60;
            } else if (progressBarStatus < 80) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_reward));
                    }
                });
                APIClient.getRewards();
                return 80;
            } else{
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_stage));
                    }
                });
                APIClient.getStages();
                return 100;
            }
        }//end of while
        return 100;
    }//end of doOperation
}