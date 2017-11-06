package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
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
            if(isNetworkAvailable()) {
                new AlertDialog.Builder(getContext())
                    .setTitle(R.string.import_start_import)
                    .setMessage(R.string.import_start_info)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            startImport();
                        }
                    })
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
            new Thread(new Runnable() {
                public void run() {
                    while (progressBarStatus < 100) {
                        progressBarStatus = importData();
                        progressBarHandler.post(new Runnable() {
                            public void run() {
                                progressBar.setProgress(progressBarStatus);
                            }
                        });
                    }
                    if (progressBarStatus >= 100) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressBar.setProgress(0);
                        progressBar.dismiss();
                    }
                }
            }).start();
        } catch (Exception ex){
            Log.d("error", ex.getMessage());
        }
    }

    private int importData() {
        while (progressBarStatus <= 100) {
            if (progressBarStatus < 20) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_delete_data));
                    }
                });
                String message = APIClient.deleteData();
                if(!message.equals("success")){
                    ErrorDialog dialog = new ErrorDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    dialog.setArguments(bundle);
                    dialog.show(this.getFragmentManager(), "Error");
                    APIClient.clearDatabase();
                    break;
                }
                return 20;
            } else if (progressBarStatus < 40) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_driver));
                    }
                });
                String message = APIClient.getRiders();
                if(!message.equals("success")){
                    ErrorDialog dialog = new ErrorDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    dialog.setArguments(bundle);
                    dialog.show(this.getFragmentManager(), "Error");
                    APIClient.clearDatabase();
                    break;
                }
                return 40;
            } else if (progressBarStatus < 60) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_stage));
                    }
                });
                String message = APIClient.getStages();
                if(!message.equals("success")){
                    ErrorDialog dialog = new ErrorDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    dialog.setArguments(bundle);
                    dialog.show(this.getFragmentManager(), "Error");
                    APIClient.clearDatabase();
                    break;
                }
                return 60;
            } else if (progressBarStatus < 80) {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_reward));
                    }
                });
                String message = APIClient.getRewards();
                if(!message.equals("success")){
                    ErrorDialog dialog = new ErrorDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    dialog.setArguments(bundle);
                    dialog.show(this.getFragmentManager(), "Error");
                    APIClient.clearDatabase();
                    break;
                }
                return 80;
            } else {
                progressBarHandler.post(new Runnable() {
                    public void run() {
                        progressBar.setMessage(getResources().getText(R.string.import_judgment));
                    }
                });
                String message = APIClient.getJudgments();
                if(!message.equals("success")){
                    ErrorDialog dialog = new ErrorDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString("message", message);
                    dialog.setArguments(bundle);
                    dialog.show(this.getFragmentManager(), "Error");
                    APIClient.clearDatabase();
                    break;
                }
                return 100;
            }
        }
        return 100;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}