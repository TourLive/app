package ch.hsr.sa.radiotour.presentation.activites;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.hsr.sa.radiotour.controller.adapter.ViewPageAdapter;
import ch.hsr.sa.radiotour.presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.presentation.fragments.MaillotsFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.SpecialFragment;
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;
import ch.hsr.sa.radiotour.R;

public class MainActivity extends AppCompatActivity {

    private static ViewPageAdapter viewPageAdapter;
    private static ViewPager viewPager;

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
