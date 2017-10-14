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
import android.widget.Button;

import java.util.LinkedList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.adapter.RaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderAdapter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by Urs Forrer on 10.10.2017.
 */

public class RaceFragment extends Fragment implements View.OnClickListener {
    private IRiderPresenter presenter;
    private IRaceGroupPresenter raceGroupPresenter;
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;

    private RiderAdapter adapter;
    private RaceGroupAdapter raceGroupAdapter;

    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;
    private Button demoButton;
    private Button deleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RaceFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_race, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root){
        rvRider = (RecyclerView) root.findViewById(R.id.rvRider);
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvRaceGroup);
        presenter = new RiderPresenter(this);
        raceGroupPresenter = new RaceGroupPresenter(this);
        demoButton = (Button) root.findViewById(R.id.demoButton);
        demoButton.setOnClickListener(this);
        deleteButton = (Button) root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvRider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRider.setItemAnimator(new DefaultItemAnimator());
        rvRaceGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRaceGroup.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribeCallbacks();
        presenter.getAllRiders();
        raceGroupPresenter.subscribeCallbacks();
        raceGroupPresenter.getAllRaceGroups();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unSubscribeCallbacks();
        raceGroupPresenter.unSubscribeCallbacks();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demoButton: {
                addDefaultData();
                break;
            }
            case R.id.deleteButton:{
                presenter.clearAllRiders();
                presenter.getAllRiders();
                raceGroupPresenter.clearAllRaceGroups();
                raceGroupPresenter.getAllRaceGroups();
                break;
            }
            default:{

            }
        }
    }

    public void addDefaultData(){
        presenter.clearAllRiders();
        RealmList<Rider> riders = new RealmList<>();
        for(int i = 0; i < 50; i++){
            Rider rider = new Rider();
            rider.setStartNr(i);
            rider.setCountry("swiss");
            rider.setName("rider" + i);
            presenter.addRider(rider);
            riders.add(rider);
        }
        raceGroupPresenter.clearAllRaceGroups();
        RaceGroup raceGroup = new RaceGroup();
        for (int i = 0; i < 5; i++) {
            raceGroup.setType(RaceGroupType.FELD);
            raceGroup.setPosition(i);
            raceGroup.setHistoryGapTime(60+i);
            raceGroup.setActualGapTime(i);
            RealmList<Rider> test = new RealmList<>();
            test.add(riders.get(i * 5));
            test.add(riders.get(i * 5 + 1));
            test.add(riders.get(i * 5 + 2));
            raceGroup.setRiders(test);
            raceGroupPresenter.addRaceGroup(raceGroup);
        }
    }

    public void showRiders(RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderAdapter(riders);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 10);
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.setAdapter(adapter);
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroups) {
        this.raceGroups = raceGroups;
        raceGroupAdapter = new RaceGroupAdapter(raceGroups, getContext());
        rvRaceGroup.setAdapter(raceGroupAdapter);
    }

    public void addRiderToList(){
        presenter.getAllRiders();
    }

    public void addRaceGroupToList() {
        raceGroupPresenter.getAllRaceGroups();
    }

}
