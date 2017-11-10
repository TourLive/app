package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.api.APIClient;

public class ImportFragment extends Fragment implements View.OnClickListener  {
    private Button btnImport;
    private ProgressDialog progressBar;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private static final String SUCCESS_MESSAGE = "success";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_import, container, false);
        btnImport = (Button) root.findViewById(R.id.btn_Import);
        btnImport.setOnClickListener(this);
        progressBar = new ProgressDialog(getActivity());
        return root;
    }

    @Override
    public void onClick(View v) {
        if(v == btnImport){
            if(isNetworkAvailable()) {
                new AlertDialog.Builder(getContext())
                    .setTitle(R.string.import_start_import)
                    .setMessage(R.string.import_start_info)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> startImport())
                    .create()
                    .show();
            } else {
                new AlertDialog.Builder(getContext())
                    .setTitle(R.string.import_no_internet_title)
                    .setMessage(R.string.import_no_internet_connection)
                    .setPositiveButton(android.R.string.ok,null)
                    .create()
                    .show();
            }
        }
    }

    private void startImport(){
        try{
            progressBar.setCancelable(false);
            progressBar.setMessage("Import starting ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            progressBarStatus = 0;
            new Thread(() -> {
                while (progressBarStatus < 100) {
                    progressBarStatus = importData();
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                }
                if (progressBarStatus >= 100) {
                    afterImport();
                }
            }).start();
        } catch (Exception ex){
            Log.d("error", ex.getMessage());
        }
    }

    private void afterImport() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Log.d(ImportFragment.class.getSimpleName(), "APP - IMPORT - " + e.getMessage());
            Thread.currentThread().interrupt();
        }
        progressBar.setProgress(0);
        progressBar.dismiss();
    }

    private int importData() {
        while (progressBarStatus <= 100) {
            if (progressBarStatus < 20) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_delete_data)));
                String message = APIClient.deleteData();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 20;
            } else if (progressBarStatus < 40) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver)));
                String message = APIClient.getRiders();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 40;
            }  else if (progressBarStatus < 50) {
                progressBarHandler.post(() -> {
                    progressBar.setMessage(getResources().getText(R.string.import_stage));
                });
                String message = APIClient.getStages();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 50;
            }   else if (progressBarStatus < 60) {
                progressBarHandler.post(() -> {
                    progressBar.setMessage(getResources().getText(R.string.import_stage));
                });
                String message = APIClient.getStages();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 60;
            } else if (progressBarStatus < 80) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_reward)));
                String message = APIClient.getJudgments();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 80;
            } else {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_judgment)));
                String message = APIClient.getRewards();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 100;
            }
        }
        return 100;
    }

    private void setErrorDialog(String message) {
        ErrorDialog dialog = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dialog.setArguments(bundle);
        dialog.show(this.getFragmentManager(), "Error");
        APIClient.clearDatabase();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}