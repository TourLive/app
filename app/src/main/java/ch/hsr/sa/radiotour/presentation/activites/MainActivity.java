package ch.hsr.sa.radiotour.presentation.activites;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.StagePresenter;
import ch.hsr.sa.radiotour.controller.adapter.ViewPageAdapter;
import ch.hsr.sa.radiotour.controller.api.APIClient;
import ch.hsr.sa.radiotour.controller.api.UrlLink;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Stage;
import ch.hsr.sa.radiotour.presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.presentation.fragments.MaillotsFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;

public class MainActivity extends AppCompatActivity {

    private static MainActivity activity;
    private ViewPageAdapter viewPageAdapter;
    private ViewPager viewPager;

    private Timer timerForUpdate;
    private TimerTask timerTaskForUpdate;
    private Timer timerForRace;
    private TimerTask timerTaskForRace;

    private Handler uiHandler;

    private TextView heightView;
    private TextView topFieldActualGapView;
    private TextView topFieldVirtualGapView;
    private TextView topRadioTourActualGapView;
    private TextView topRadioTourVirtualGapView;
    private TextView stageView;
    private TextView velocityView;
    private TextView raceKilometerView;
    private TextView raceTimeView;
    private TextView startStopView;

    private Boolean raceInProgress = false;
    private Time raceTime = new Time(0);
    private float distanceInMeter = 0;
    private float wholeDistanceInKm = 0;
    private float raceKilometerTop = 0;
    private float raceKilometerRadioTour = 0;
    private float raceKilometerField = 0;
    private float officialSpeedRadioTour = 0;
    private float officialSpeedField = 0;
    private float officalGapTopRadioTour = 0;
    private float officalGapTopField = 0;

    private Location actualLocation;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static int updateTime = 5000;
    private static int delayTime = 10000;
    private static int updateTimeForRace = 1000;
    private static int delayZero = 0;
    private static int minDistanceChange = 0;

    private static String SOURCES = "sources";
    private static String RACEKILOMETER = "rennkilometer";
    private static String SPEED = "speed";

    public static MainActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        initViewsAndHandlers();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(5);

        setTabMenu();
        viewPageAdapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Has to be implemented, but not needed
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.header_special_class))) {
                    closeDetailJudgmentFragment();
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals(getString(R.string.header_special_class))) {
                    closeDetailJudgmentFragment();
                }
            }
        });
    }

    private void initViewsAndHandlers() {
        uiHandler = new Handler();
        timerForRace = new Timer();

        heightView = (TextView) findViewById(R.id.txtHeightValue);
        topFieldActualGapView = (TextView) findViewById(R.id.txtTopFieldActualGap);
        topFieldVirtualGapView = (TextView) findViewById(R.id.txt_TopFieldVirtualGap);
        topRadioTourActualGapView = (TextView) findViewById(R.id.txtTopRadioTourActualGap);
        topRadioTourVirtualGapView = (TextView) findViewById(R.id.txt_TopRadioTourVirtualGap);
        stageView = (TextView) findViewById(R.id.txtStageValue);
        velocityView = (TextView) findViewById(R.id.txtVelocityValue);
        raceKilometerView = (TextView) findViewById(R.id.txtRacekilometerValue);
        raceTimeView = (TextView) findViewById(R.id.txtRacetimeValue);
        startStopView = (TextView) findViewById(R.id.btnStartStopRace);

        if(StagePresenter.getInstance().getStage() != null)
            updateStageInfo(StagePresenter.getInstance().getStage());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        startStopView.setOnClickListener(click -> {
            if(!raceInProgress){
                raceInProgress = true;
                startStopView.setBackgroundColor(getColor(R.color.colorOlive));
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    locationManager.getProvider(LocationManager.GPS_PROVIDER).supportsAltitude();
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            if(actualLocation != null) { distanceInMeter += actualLocation.distanceTo(location); }
                            actualLocation = location;
                            uiHandler.post(() -> {
                                heightView.setText(getString(R.string.header_prefix_m, actualLocation.getAltitude()));
                                raceKilometerView.setText(getString(R.string.header_prefix_km, distanceInMeter / 1000f, wholeDistanceInKm));
                                double seconds = TimeUnit.MILLISECONDS.toSeconds(raceTime.getTime());
                                double averageSpeed = (distanceInMeter / seconds) * 3.6;
                                velocityView.setText(getString(R.string.header_prefix_kmh, averageSpeed));
                            });
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            // Has to be implemented, but not needed
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            // Has to be implemented, but not needed
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            // Has to be implemented, but not needed
                        }
                    };

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, minDistanceChange, locationListener);

                    timerForRace = new Timer();
                    timerTaskForRace = new TimerTask() {
                        @Override
                        public void run() {
                            raceTime.setTime(raceTime.getTime() + updateTimeForRace);
                            uiHandler.post(() -> raceTimeView.setText(convertLongToTimeString(raceTime.getTime())));
                        }
                    };
                    timerForRace.schedule(timerTaskForRace, delayZero, updateTimeForRace);

                }
            } else{
                raceInProgress = false;
                locationManager.removeUpdates(locationListener);
                startStopView.setBackgroundColor(getColor(R.color.colorPrimaryUltraLight));
                timerForRace.cancel();
            }
        });

        startStopView.setOnLongClickListener(event -> {
            raceInProgress = false;
            raceTime.setTime(0);
            distanceInMeter = 0;
            locationManager.removeUpdates(locationListener);
            actualLocation = null;
            startStopView.setBackgroundColor(getColor(R.color.colorPrimaryUltraLight));
            raceTimeView.setText(convertLongToTimeString(raceTime.getTime()));
            timerForRace.cancel();
            return true;
        });


        timerForUpdate = new Timer();
        timerTaskForUpdate = new TimerTask() {
            @Override
            public void run() {
                try {
                    updateUIInfos();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(MainActivity.class.getSimpleName(), "APP - UI - " + e.getMessage());
                }
            }
        };
        timerForUpdate.schedule(timerTaskForUpdate, delayTime, updateTime);
    }

    private void closeDetailJudgmentFragment() {
        Fragment fragment = viewPageAdapter.getItem(2).getChildFragmentManager().findFragmentByTag("DETAILJ");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    private void initCallbacks() {
        MaillotPresenter.getInstance().subscribeCallbacks();
        JudgmentRiderConnectionPresenter.getInstance().subscribeCallbacks();
        JudgmentPresenter.getInstance().subscribeCallbacks();
        RaceGroupPresenter.getInstance().subscribeCallbacks();
        RewardPresenter.getInstance().subscribeCallbacks();
        RiderPresenter.getInstance().subscribeCallbacks();
        RiderStageConnectionPresenter.getInstance().subscribeCallbacks();
        StagePresenter.getInstance().subscribeCallbacks();
    }

    @Override
    public void onStart() {
        super.onStart();
        initCallbacks();
    }

    private void setTabMenu() {
        viewPageAdapter.addFragment(new RaceFragment(), getString(R.string.header_race));
        viewPageAdapter.addFragment(new RiderRaceGroupFragment(), getString(R.string.header_ridergroup));
        viewPageAdapter.addFragment(new SpecialFragment(), getString(R.string.header_special_class));
        viewPageAdapter.addFragment(new VirtualClassFragment(), getString(R.string.header_virtual_class));
        viewPageAdapter.addFragment(new MaillotsFragment(), getString(R.string.header_maillots));
        viewPageAdapter.addFragment(new ImportFragment(), getString(R.string.header_import_data));
    }

    private void updateUIInfos() throws InterruptedException {
        Thread update = new Thread(() -> {
            synchronized (this) {
                JSONObject temp = new JSONObject();
                JSONObject gpsData = APIClient.getDataFromAPI(UrlLink.STATES, null);
                try {
                    JSONArray gpsInfoArray = gpsData.getJSONArray(SOURCES);
                    temp = gpsInfoArray.getJSONObject(2);
                    raceKilometerTop = (float)temp.getDouble(RACEKILOMETER);
                    temp = gpsInfoArray.getJSONObject(3);
                    raceKilometerRadioTour = (float)temp.getDouble(RACEKILOMETER);
                    officialSpeedRadioTour = (float)temp.getDouble(SPEED);
                    temp = gpsInfoArray.getJSONObject(4);
                    raceKilometerField = (float)temp.getDouble(RACEKILOMETER);
                    officialSpeedField = (float)temp.getDouble(SPEED);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(officialSpeedRadioTour != 0) { officalGapTopRadioTour = (raceKilometerTop - raceKilometerRadioTour) / officialSpeedRadioTour; }
                if(officialSpeedField != 0){ officalGapTopField = (raceKilometerTop - raceKilometerField) / officialSpeedField; }

                uiHandler.post(() -> {
                    topRadioTourActualGapView.setText(convertLongToTimeShortString(TimeUnit.SECONDS.toMillis((long)officalGapTopRadioTour)));
                    topFieldActualGapView.setText(convertLongToTimeShortString(TimeUnit.SECONDS.toMillis((long)officalGapTopField)));
                });
            }
        });
        update.start();
        update.join();
    }

    public void updateStageInfo(Stage stage){
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(stage.getName());
        m.find();
        wholeDistanceInKm = stage.getDistance();
        uiHandler.post(() -> {
            stageView.setText(m.group(0));
            raceKilometerView.setText(getString(R.string.header_prefix_km, 0.0, wholeDistanceInKm));
        });
    }

    public void updateVirtualGap(RaceGroup group){
        if(group.getType() == RaceGroupType.FELD){
            topFieldVirtualGapView.setText(convertLongToTimeShortString(TimeUnit.SECONDS.toMillis(group.getActualGapTime())));
        }
    }

    public void setTab(int tab) {
        viewPager.setCurrentItem(tab);
    }

    private String convertLongToTimeString(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        return formatter.format(date);
    }

    private String convertLongToTimeShortString(long time) {
        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(date);
    }
}
