package ch.hsr.sa.radiotour.Presentation.activites;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.hsr.sa.radiotour.Controller.Adapter.ViewPageAdaper;
import ch.hsr.sa.radiotour.Presentation.fragments.ImportFragment;
import ch.hsr.sa.radiotour.Presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPageAdaper viewPageAdaper = new ViewPageAdaper(getSupportFragmentManager());

        viewPageAdaper.addFragment(new RaceFragment(), "RACE");
        viewPageAdaper.addFragment(new ImportFragment(), "IMPORT");

        viewPager.setAdapter(viewPageAdaper);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
