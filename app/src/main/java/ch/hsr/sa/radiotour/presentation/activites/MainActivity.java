package ch.hsr.sa.radiotour.presentation.activites;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.ViewPageAdapter;
import ch.hsr.sa.radiotour.presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.presentation.fragments.MaillotsFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;

public class MainActivity extends AppCompatActivity {

    private static ViewPageAdapter viewPageAdapter;
    private static ViewPager viewPager;
    private static FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());
        setTabMenu();
        viewPager.setAdapter(viewPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Has to be implemented, but not needed
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Has to be implemented, but not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getText().toString().equals(getString(R.string.header_race))){
                    viewPageAdapter.setDetail(false);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        });

        fragmentManager = getSupportFragmentManager();
    }

    public static void notifyAdapter() {
        viewPager.getAdapter().notifyDataSetChanged();
    }

    public static void changeFirstFragment(boolean bool) {
        viewPageAdapter.setDetail(bool);
    }

    private void setTabMenu() {
        viewPageAdapter.addFragment(new RaceFragment(), getString(R.string.header_race));
        viewPageAdapter.addFragment(new SpecialFragment(), getString(R.string.header_special_class));
        viewPageAdapter.addFragment(new VirtualClassFragment(), getString(R.string.header_virtual_class));
        viewPageAdapter.addFragment(new MaillotsFragment(), getString(R.string.header_maillots));
        viewPageAdapter.addFragment(new ImportFragment(), getString(R.string.header_import_data));
    }
}
