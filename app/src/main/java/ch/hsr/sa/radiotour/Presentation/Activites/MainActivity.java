package ch.hsr.sa.radiotour.Presentation.activites;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ch.hsr.sa.radiotour.Controller.Adapter.ViewPageAdapter;
import ch.hsr.sa.radiotour.Presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.Presentation.fragments.MaillotsFragment;
import ch.hsr.sa.radiotour.Presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("TAG", "TEST");
        super.onCreate(savedInstanceState);
        Log.v("TAG", "TEST");
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new RaceFragment(), getString(R.string.header_race));
        viewPageAdapter.addFragment(new MaillotsFragment(), getString(R.string.header_maillots));
        viewPageAdapter.addFragment(new ImportFragment(), getString(R.string.header_import_data));

        Log.d("TAG", "TEST");

        viewPager.setAdapter(viewPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
