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
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import ch.hsr.sa.radiotour.BuildConfig;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.StagePresenter;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.UrlLink;
import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.RealmModul;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.presentation.activites.MainActivity;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.StageRealmProxy;

public class ImportFragment extends Fragment implements View.OnClickListener  {
    private Button btnImport;
    private Button btnDemo;
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

    private static final String DEMOREALM = "demorealm.realm";
    private static final String DEMODATA = "DemoRealm.realm";
    private static final String REALREAM = "radiotour.realm";
    private boolean demoMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","ImportFragment onCreateView");
        demoMode = false;
        View root = inflater.inflate(R.layout.fragment_import, container, false);

        btnImport = (Button) root.findViewById(R.id.btn_Import);
        btnImport.setOnClickListener(this);

        btnDemo = (Button) root.findViewById(R.id.btn_ImportDemodata);
        btnDemo.setOnClickListener(this);

        gpsView = (TextView) root.findViewById(R.id.circleGPS);
        serverView = (TextView) root.findViewById(R.id.circleServer);
        raceIdView = (TextView) root.findViewById(R.id.txtActualRaceIdValue);
        stageIdView = (TextView) root.findViewById(R.id.txtActualStageIdValue);
        StagePresenter.getInstance().addView(this);
        Stage stage = StagePresenter.getInstance().getStage();
        if (stage != null) {
            updateActualStage(stage);
        }

        progressBar = new ProgressDialog(getActivity());

        timerForUpdate = new Timer();
        timerTaskForUpdate = new TimerTask() {
            @Override
            public void run() {
                updateViews();
            }
        };
        timerForUpdate.schedule(timerTaskForUpdate, delayTime, updateTime);

        TextView versionView = (TextView) root.findViewById(R.id.txtVersion);
        versionView.setText(getString(R.string.import_version, BuildConfig.VERSION_NAME));

        return root;
    }

    private void updateViews(){
        LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Boolean active = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
        updateDrawable(gpsView, active);

        JSONArray settings = APIClient.getDataFromAPI(UrlLink.GLOBALSETTINGS, null);
        if(settings == null) {
            updateDrawable(serverView, false);
        } else {
            updateDrawable(serverView, true);
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
        if(v == btnDemo){
            switchDemoMode();
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
           StagePresenter.getInstance().getStageWithCallback();
        });
    }

    private int importData() {
        while (progressBarStatus < 100) {
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

    private void switchDemoMode(){
        try{
            progressBar.setCancelable(false);
            progressBar.setMessage(getResources().getString(R.string.import_demo_start));
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            progressBarStatus = 0;
            new Thread(() -> {
                while (progressBarStatus < 100) {
                    try {
                        progressBarStatus = loadDataForDemoMode();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                }
                if (progressBarStatus >= 100) {
                    afterImport();
                    progressBarHandler.post(() -> {
                        if(demoMode){
                            btnDemo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape));
                            MainActivity.getInstance().findViewById(R.id.txt_DemoMode).setVisibility(View.INVISIBLE);
                            btnDemo.setText(getResources().getText(R.string.import_demodata));
                            btnImport.setEnabled(true);
                            demoMode = false;
                            APIClient.setDemoMode(demoMode);
                        } else {
                            btnDemo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape_green));
                            MainActivity.getInstance().findViewById(R.id.txt_DemoMode).setVisibility(View.VISIBLE);
                            btnDemo.setText(getResources().getString(R.string.import_demomode_active));
                            btnImport.setEnabled(false);
                            demoMode = true;
                            APIClient.setDemoMode(demoMode);
                        }
                    });
                }
            }).start();
        } catch (Exception ex){
            Log.d("error", ex.getMessage());
        }

    }

    private int loadDataForDemoMode() throws IOException {
        while (progressBarStatus < 100) {
            if (progressBarStatus < 50 && !demoMode) {
                File file = new File(getContext().getFilesDir(), DEMOREALM);
                if(!demoMode && !file.exists()) {
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_firstload_demodata));
                        try {
                            copyBundledRealmFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                return 50;
            } else  {
                if(!demoMode){
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_context_switch_demo));
                        try {
                            setDataToDemo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_context_switch_real));
                        try {
                            resetDataToImport();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                return 100;
            }
        }
        return 100;
    }

    private void setDataToDemo() throws  IOException{
        RealmConfiguration config = new RealmConfiguration.Builder().
                name(DEMOREALM).
                deleteRealmIfMigrationNeeded().
                modules(new RealmModul()).
                build();
        RadioTourApplication.setInstance(config);
    }

    private void resetDataToImport() throws IOException {
        RealmConfiguration config = new RealmConfiguration.Builder().
                name(REALREAM).
                deleteRealmIfMigrationNeeded().
                modules(new RealmModul()).
                build();
        RadioTourApplication.setInstance(config);
    }

    public void updateActualStage(Stage stage) {
        raceIdView.setText(getString(R.string.import_race_infos, stage.getRaceName(), stage.getRaceId()));
        stageIdView.setText(getString(R.string.import_stage_infos, stage.getName(), stage.getFrom(), stage.getTo(), stage.getStageId()));
    }

    private String copyBundledRealmFile() throws IOException {
        FileOutputStream outputStream = null;
        InputStream is = null;
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.removeAllChangeListeners();
            realm.close();

            File file = new File(getContext().getFilesDir(), DEMOREALM);

            outputStream = new FileOutputStream(file);

            is = getContext().getAssets().open(DEMODATA);

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.d(ImportFragment.class.getSimpleName(), "APP - FILECOPY - " + e.getMessage());
        } finally {
            outputStream.close();
            is.close();
        }
        return null;
    }
}