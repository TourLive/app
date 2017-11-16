package ch.hsr.sa.radiotour.presentation.activites;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

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

    public ViewPageAdapter viewPageAdapter;
    public ViewPager viewPager;
    private Timer timerForUpdate;
    private TimerTask timerTask;
    private Handler uiHandler;
    private TextView heightView;
    private static int UPDATE_TIME = 1000;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHandler = new Handler();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        heightView = (TextView) findViewById(R.id.txtHeightValue);

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
        timerForUpdate.schedule(timerTask, 10000, UPDATE_TIME);
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
                int height = getHeight();
                uiHandler.post(() -> {
                    heightView.setText(height + "m");
                });
            }
        });
        update.start();
        update.join();

    }

    private int getHeight() {
        JSONObject jsonObject = APIClient.getDataFromAPI(UrlLink.STATES, null);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){ return (int)location.getAltitude();}
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        return 500;
    }
}
