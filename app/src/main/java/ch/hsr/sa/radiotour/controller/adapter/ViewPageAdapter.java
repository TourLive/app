package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.presentation.fragments.RaceFragment;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();
    private boolean detail = false;

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setDetail(boolean in) {
        detail = in;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            if (!detail) {
                return new RaceFragment();
            } else {
                detail = false;
                return new RiderRaceGroupFragment();
            }
        } else {
            return fragments.get(position);
        }

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
