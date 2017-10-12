package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.adapter.RiderAdapter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.IRiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.Rider;
import io.realm.RealmList;

/**
 * Created by Urs Forrer on 10.10.2017.
 */

public class RaceFragment extends Fragment {
    private IRiderPresenter presenter;
    private RealmList<Rider> riders;
    private RiderAdapter adapter;

    private RecyclerView rvRider;
    private int rider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RaceFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_race, container, false);
        rider = R.id.tabRider;
        rvRider = (RecyclerView) root.findViewById(R.id.rvRider);
        presenter = new RiderPresenter(this);
        initRecyclerListener();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribeCallbacks();
        presenter.getAllRiders();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unSubscribeCallbacks();
    }

    private void initRecyclerListener() {
        rvRider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRider.setItemAnimator(new DefaultItemAnimator());
    }

    public void showRiders(RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderAdapter(riders);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 2);
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.setAdapter(adapter);

    }

}
