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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
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
import ch.hsr.sa.radiotour.presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.presentation.fragments.MaillotsFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;

public class MainActivity extends AppCompatActivity {

    private static MainActivity activity;
    public ViewPageAdapter viewPageAdapter;
    public ViewPager viewPager;
    private Timer timerForUpdate;
    private TimerTask timerTask;
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
    private static int UPDATE_TIME = 5000;
    private static int DELAY_TIME = 10000;
    private static int MIN_DISTANCE_CHANGE = 10;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public static MainActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                Fragment fragment = viewPageAdapter.getItem(tab.getPosition());
                if (fragment != null) {
                    //fragment.onResume();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Has to be implemented, but not needed
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

        heightView = (TextView) findViewById(R.id.txtHeightValue);
        topFieldActualGapView = (TextView) findViewById(R.id.txtTopFieldActualGap);
        topFieldVirtualGapView = (TextView) findViewById(R.id.txt_TopFieldVirtualGap);
        topRadioTourActualGapView = (TextView) findViewById(R.id.txtTopRadioTourActualGap);
        topRadioTourVirtualGapView = (TextView) findViewById(R.id.txt_TopRadioTourVirtualGap);
        stageView = (TextView) findViewById(R.id.txtStageValue);
        velocityView = (TextView) findViewById(R.id.txtVelocityValue);
        raceKilometerView = (TextView) findViewById(R.id.txtRacekilometerValue);
        raceTimeView = (TextView) findViewById(R.id.txtRacetimeValue);

        if(StagePresenter.getInstance().getStage() != null)
            updateStageId(StagePresenter.getInstance().getStage().getName());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.GPS_PROVIDER).supportsAltitude();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                uiHandler.post(() -> {
                    heightView.setText(location.getAltitude() + "m");
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

        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_TIME, MIN_DISTANCE_CHANGE, locationListener);
        }

        timerForUpdate = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    updateUIInfos();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timerForUpdate.schedule(timerTask, DELAY_TIME, UPDATE_TIME);
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


                uiHandler.post(() -> {

                });
            }
        });
        update.start();
        update.join();

    }

    /*public void getHeight() {

    }*/

    public void updateStageId(String name){
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(name);
        m.find();
        uiHandler.post(() -> {
            stageView.setText(m.group(0));
        });
    }
}
