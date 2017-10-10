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
import ch.hsr.sa.radiotour.Presentation.fragments.SpecialFragment;
import ch.hsr.sa.radiotour.Presentation.fragments.VirtualClassFragment;
import ch.hsr.sa.radiotour.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        viewPageAdapter.addFragment(new RaceFragment(), getString(R.string.header_race));
        viewPageAdapter.addFragment(new SpecialFragment(), getString(R.string.header_special_class));
        viewPageAdapter.addFragment(new VirtualClassFragment(), getString(R.string.header_virtual_class));
        viewPageAdapter.addFragment(new MaillotsFragment(), getString(R.string.header_maillots));
        viewPageAdapter.addFragment(new ImportFragment(), getString(R.string.header_import_data));

        viewPager.setAdapter(viewPageAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
