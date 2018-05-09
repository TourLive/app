package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import ch.hsr.sa.radiotour.BuildConfig;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.Parser;
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

public class ImportFragment extends Fragment implements View.OnClickListener {
    private static final String PARAMETER = "parameter";
    private static final String SUCCESS_MESSAGE = "success";
    private static final String DEMOREALM = "demorealm.realm";
    private static final String DEMODATA = "DemoRealm.realm";
    private static final String REALREAM = "radiotour.realm";
    private static int updateTime = 5000;
    private static int delayTime = 10000;
    private Button btnImport;
    private Button btnDemo;
    private Button security;
    private TextView gpsView;
    private TextView serverView;
    private TextView raceIdView;
    private TextView stageIdView;
    private TextView statusView;
    private ProgressDialog progressBar;
    private int progressBarStatus;
    private Handler progressBarHandler = new Handler();
    private Handler uiHandler = new Handler();
    private Timer timerForUpdate;
    private TimerTask timerTaskForUpdate;
    private boolean demoMode;
    private SharedPreferences sharedPreferences;
    private EditText username;
    private EditText password;
    private String shUsername;
    private String shPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "ImportFragment onCreateView");
        demoMode = false;
        View root = inflater.inflate(R.layout.fragment_import, container, false);

        btnImport = root.findViewById(R.id.btn_Import);
        btnImport.setOnClickListener(this);

        btnDemo = root.findViewById(R.id.btn_ImportDemodata);
        btnDemo.setOnClickListener(this);

        security = root.findViewById(R.id.btn_SaveSecurity);
        security.setOnClickListener(this);

        username = root.findViewById(R.id.txt_UserNameInput);
        password = root.findViewById(R.id.txt_PasswordInput);
        sharedPreferences = this.getActivity().getSharedPreferences("security", Context.MODE_PRIVATE);

        shUsername = sharedPreferences.getString("username","");
        shPassword = sharedPreferences.getString("password", "");
        if (shUsername != null && shPassword != null) {
            username.setText(shUsername);
            password.setText(shPassword);
        }
        System.out.println(shPassword);
        System.out.println(shUsername);

        gpsView = root.findViewById(R.id.circleGPS);
        serverView = root.findViewById(R.id.circleServer);
        raceIdView = root.findViewById(R.id.txtActualRaceIdValue);
        stageIdView = root.findViewById(R.id.txtActualStageIdValue);
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

        statusView = root.findViewById(R.id.txtStatus);
        statusView.setText(getString(R.string.import_status)  + getString(R.string.import_version, BuildConfig.VERSION_NAME));
        return root;
    }

    private void updateViews() {
        try{
            LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            Boolean active = manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? true : false;
            updateDrawable(gpsView, active);

            JSONObject settings = APIClient.getStatusFromAPI(UrlLink.STATUSPAGE, null);
            if (settings == null) {
                updateDrawable(serverView, false);
            } else {
                updateDrawable(serverView, true);
            }
        } catch (NullPointerException ex){
            // App went to background ignore update
        }
    }

    private void updateDrawable(View view, Boolean active) {
        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        int color = active ? ContextCompat.getColor(getContext(), R.color.green) : ContextCompat.getColor(getContext(), R.color.colorRed);
        uiHandler.post(() -> drawable.setColor(color));
    }

    @Override
    public void onClick(View v) {
        if (v == btnImport) {
            if (isNetworkAvailable()) {
                shUsername = sharedPreferences.getString("username","");
                shPassword = sharedPreferences.getString("password", "");
                if (shPassword == "" || shUsername == "" || shUsername == null || shPassword == null) {
                    Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.import_security), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                    toast.show();
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.import_start_import)
                            .setMessage(R.string.import_start_info)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok, (DialogInterface dialog, int which) -> startImport())
                            .create()
                            .show();
                }
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.import_no_internet_title)
                        .setMessage(R.string.import_no_internet_connection)
                        .setPositiveButton(android.R.string.ok, null)
                        .create()
                        .show();
            }
        }
        if (v == btnDemo) {
            switchDemoMode();
        }
        if (v == security) {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            sharedPreferences.edit().putString("username", user).apply();
            sharedPreferences.edit().putString("password", pass).apply();
            APIClient.setCredentials(pass, user);
            Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.import_success_security), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    }

    private void startImport() {
        try {
            progressBar.setCancelable(false);
            progressBar.setMessage("Import starting ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(900, 30,30, 0);
            progressBar.addContentView(new ProgressBar(getContext()), layoutParams);
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
        } catch (Exception ex) {
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
            updateActualStage(StagePresenter.getInstance().getStage());
            RiderPresenter.getInstance().getAllRiders();
            RaceGroupPresenter.getInstance().getAllRaceGroups();
            RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
            JudgmentPresenter.getInstance().getAllJudgments();
            MaillotPresenter.getInstance().getAllMaillots();
            StagePresenter.getInstance().getStageWithCallback();
            Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.import_success), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        });
    }

    private int importData() {
        while (progressBarStatus < 100) {
            if (progressBarStatus < 20) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_delete_data)));
                APIClient.deleteData();
                String message = APIClient.getSettings();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 20;
            } else if (progressBarStatus < 30) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver)));
                String message = APIClient.getRiders();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 30;
            } else if (progressBarStatus < 40) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByOfficalGap();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 40;
            } else if (progressBarStatus < 45) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByVirtualGap();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 45;
            } else if (progressBarStatus < 50) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByMountainPoints();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 50;
            } else if (progressBarStatus < 55) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByPoints();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 55;
            } else if (progressBarStatus < 60) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByBestSwiss();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 60;
            } else if (progressBarStatus < 65) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByMoney();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 65;
            } else if (progressBarStatus < 68) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByTimeInLead();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 68;
            } else if (progressBarStatus < 69) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_driver_update)));
                String message = "";
                try{ message = Parser.updateRiderConnectionRankByDistanceInLead();}
                catch (Exception ex){ message = getResources().getText(R.string.import_error_driver_update).toString();}
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 69;
            }
            else if (progressBarStatus < 70) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_maillot)));
                String message = APIClient.getMaillots();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 70;
            } else if (progressBarStatus < 75) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_stage)));
                String message = APIClient.getStages();
                String message2 = APIClient.getRace();
                if (!message.equals(SUCCESS_MESSAGE) || !message2.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 75;
            } else if (progressBarStatus < 80) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_racegroups)));
                String message = APIClient.getRacegroups();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 80;
            } else if (progressBarStatus < 90) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_reward)));
                String message = APIClient.getJudgments();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 90;
            } else if (progressBarStatus < 95) {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_jrc)));
                String message = APIClient.getJudgementRiderConnections();
                if (!message.equals(SUCCESS_MESSAGE)) {
                    setErrorDialog(message);
                    break;
                }
                return 95;
            } else {
                progressBarHandler.post(() -> progressBar.setMessage(getResources().getText(R.string.import_judgment)));
                String message = APIClient.getRewards();
                if (!message.equals(SUCCESS_MESSAGE)) {
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

    @Override
    public void onResume() {
        super.onResume();
        shUsername = sharedPreferences.getString("username","");
        shPassword = sharedPreferences.getString("password", "");
        if (shUsername != null && shPassword != null) {
            username.setText(shUsername);
            password.setText(shPassword);
            APIClient.setCredentials(shPassword, shUsername);
        }
    }

    private void switchDemoMode() {
        try {
            progressBar.setCancelable(false);
            progressBar.setMessage(getResources().getString(R.string.import_demo_start));
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(900, 30,30, 0);
            progressBar.addContentView(new ProgressBar(getContext()), layoutParams);
            progressBarStatus = 0;
            new Thread(() -> {
                while (progressBarStatus < 100) {
                    try {
                        progressBarStatus = loadDataForDemoMode();
                    } catch (IOException e) {
                        Log.d(ImportFragment.class.getSimpleName(), "APP - DEMOMODE - " + e.getMessage());
                    }
                    progressBarHandler.post(() -> progressBar.setProgress(progressBarStatus));
                }
                if (progressBarStatus >= 100) {
                    afterImport();
                    progressBarHandler.post(() -> {
                        if (demoMode) {
                            btnDemo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape));
                            MainActivity.getInstance().findViewById(R.id.txt_DemoMode).setVisibility(View.INVISIBLE);
                            btnDemo.setText(getResources().getText(R.string.import_demodata));
                            btnImport.setEnabled(true);
                            demoMode = false;
                            APIClient.setDemoMode(demoMode);
                            statusView.setText(getString(R.string.import_status)  + getString(R.string.import_version, BuildConfig.VERSION_NAME));

                        } else {
                            btnDemo.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape_green));
                            MainActivity.getInstance().findViewById(R.id.txt_DemoMode).setVisibility(View.VISIBLE);
                            btnDemo.setText(getResources().getString(R.string.import_demomode_active));
                            btnImport.setEnabled(false);
                            demoMode = true;
                            APIClient.setDemoMode(demoMode);
                            statusView.setText(getString(R.string.import_status)  + getString(R.string.import_demomode));
                        }
                        updateActualStage(StagePresenter.getInstance().getStage());
                        Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.import_success), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    });
                }
            }).start();
        } catch (Exception ex) {
            Log.d("error", ex.getMessage());
        }

    }

    private int loadDataForDemoMode() throws IOException {
        while (progressBarStatus < 100) {
            if (progressBarStatus < 50 && !demoMode) {
                File file = new File(getContext().getFilesDir(), DEMOREALM);
                if (!demoMode && !file.exists()) {
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_firstload_demodata));
                        try {
                            copyBundledRealmFile();
                        } catch (IOException e) {
                            Log.d(ImportFragment.class.getSimpleName(), "APP - DEMOMODE FILELOADING - " + e.getMessage());
                        }
                    });
                }
                return 50;
            } else {
                if (!demoMode) {
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_context_switch_demo));
                        try {
                            setDataToDemo();
                        } catch (IOException e) {
                            Log.d(ImportFragment.class.getSimpleName(), "APP - DEMOMODE - " + e.getMessage());
                        }
                    });
                } else {
                    progressBarHandler.post(() -> {
                        progressBar.setMessage(getResources().getText(R.string.import_context_switch_real));
                        try {
                            resetDataToImport();
                        } catch (IOException e) {
                            Log.d(ImportFragment.class.getSimpleName(), "APP - DEMOMODE - " + e.getMessage());
                        }
                    });
                }
                return 100;
            }
        }
        return 100;
    }

    private void setDataToDemo() throws IOException {
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
        if(stage == null){
            raceIdView.setText(getString(R.string.import_nodata_loaded));
            stageIdView.setText(getString(R.string.import_nodata_loaded));
        } else {
            raceIdView.setText(getString(R.string.import_race_infos, stage.getRaceName(), stage.getRaceId()));
            stageIdView.setText(getString(R.string.import_stage_infos, stage.getName(), stage.getFrom(), stage.getTo(), stage.getId()));
        }
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