package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.UrlLink;

public class ImportFragment extends Fragment implements View.OnClickListener  {
    private Button btnImport;
    private TextView gpsView;
    private TextView serverView;
    private TextView raceIdView;
    private TextView stageIdView;
    private static final String PARAMETER = "parameter";
    private ProgressDialog progressBar;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private Handler uiHandler = new Handler();
    private static final String SUCCESS_MESSAGE = "success";

    private Timer timerForUpdate;
    private TimerTask timerTaskForUpdate;
    private static int updateTime = 5000;
    private static int delayTime = 10000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_import, container, false);

        btnImport = (Button) root.findViewById(R.id.btn_Import);
        btnImport.setOnClickListener(this);

        gpsView = (TextView) root.findViewById(R.id.circleGPS);
        serverView = (TextView) root.findViewById(R.id.circleServer);
        raceIdView = (TextView) root.findViewById(R.id.txtActualRaceIdValue);
        stageIdView = (TextView) root.findViewById(R.id.txtActualStageIdValue);

        progressBar = new ProgressDialog(getActivity());

        timerForUpdate = new Timer();
        timerTaskForUpdate = new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        };
        timerForUpdate.schedule(timerTaskForUpdate, delayTime, updateTime);
        return root;
    }

    private void updateViews(){
        LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Boolean active = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
        updateDrawable(gpsView, active);

        JSONArray settings = APIClient.getDataFromAPI(UrlLink.GLOBALSETTINGS, null);
        if(settings == null) {
            updateDrawable(serverView, false);
            setText(stageIdView, getResources().getString(R.string.import_actual_not_available));
            setText(raceIdView, getResources().getString(R.string.import_actual_not_available));
        } else {
            try {
                updateDrawable(serverView, true);
                JSONObject settingsJSONObject= settings.getJSONObject(0);
                setText(stageIdView, settingsJSONObject.getString(PARAMETER));
                settingsJSONObject = settings.getJSONObject(1);
                setText(raceIdView, settingsJSONObject.getString(PARAMETER));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDrawable(View view, Boolean active){
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        int color = active ? ContextCompat.getColor(getContext(), R.color.green) : ContextCompat.getColor(getContext(), R.color.colorRed);
        uiHandler.post(() -> drawable.setColor(color));
    }

    private void setText(TextView view, String text){
        uiHandler.post(() -> view.setText(text));
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
        progressBarHandler.post(() -> {
           RiderPresenter.getInstance().getAllRiders();
           RaceGroupPresenter.getInstance().getAllRaceGroups();
           RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
           JudgmentPresenter.getInstance().getAllJudgments();
           MaillotPresenter.getInstance().getAllMaillots();
        });
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
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_maillot)));
                String message = APIClient.getMaillots();
                if(!message.equals(SUCCESS_MESSAGE)){
                    setErrorDialog(message);
                    break;
                }
                return 50;
            }   else if (progressBarStatus < 60) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_stage)));
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