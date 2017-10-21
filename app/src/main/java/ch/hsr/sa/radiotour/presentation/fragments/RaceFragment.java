package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.adapter.EditItemTouchHelperCallback;
import ch.hsr.sa.radiotour.controller.adapter.OnStartDragListener;
import ch.hsr.sa.radiotour.controller.adapter.RaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderAdapter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RaceFragment extends Fragment implements View.OnClickListener, OnStartDragListener {
    private IRiderPresenter presenter;
    private IRaceGroupPresenter raceGroupPresenter;
    private IRiderStageConnectionPresenter riderStageConnectionPresenter;
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;

    private RiderAdapter adapter;
    private RaceGroupAdapter raceGroupAdapter;

    private ItemTouchHelper itemTouchHelper;

    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;
    private Button demoButton;
    private Button deleteButton;
    private Button doctorButton;
    private Button dropButton;
    private Button defectButton;
    private Button quitButton;
    private Button dncButton;
    private Button quitChoiceButton;
    private Button unkonownRiderButton;

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
        riderStageConnectionPresenter = new RiderStageConnectionPresenter(this);
        demoButton = (Button) root.findViewById(R.id.demoButton);
        demoButton.setOnClickListener(this);
        deleteButton = (Button) root.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);
        doctorButton = (Button) root.findViewById(R.id.btnDoctor);
        doctorButton.setOnClickListener(this);
        dropButton = (Button) root.findViewById(R.id.btnDrop);
        dropButton.setOnClickListener(this);
        defectButton = (Button) root.findViewById(R.id.btnDefect);
        defectButton.setOnClickListener(this);
        quitButton = (Button) root.findViewById(R.id.btnQuit);
        quitButton.setOnClickListener(this);
        dncButton = (Button) root.findViewById(R.id.btnDNC);
        dncButton.setOnClickListener(this);
        quitChoiceButton = (Button) root.findViewById(R.id.btnQuitChoice);
        quitChoiceButton.setOnClickListener(this);
        unkonownRiderButton = (Button) root.findViewById(R.id.btnUnknownRider);
        unkonownRiderButton.setOnClickListener(this);
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvRider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRider.setItemAnimator(new DefaultItemAnimator());
        rvRaceGroup.setHasFixedSize(true);
        rvRaceGroup.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        rvRaceGroup.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribeCallbacks();
        presenter.getAllRiders();
        raceGroupPresenter.subscribeCallbacks();
        raceGroupPresenter.getAllRaceGroups();
        riderStageConnectionPresenter.subscribeCallbacks();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unSubscribeCallbacks();
        raceGroupPresenter.unSubscribeCallbacks();
        riderStageConnectionPresenter.unSubscribeCallbacks();
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
                riderStageConnectionPresenter.clearAllRiderStageConnection();
                break;
            }
            case R.id.btnQuitChoice:{
                adapter.resetSelectedRiders();
            }
            case R.id.btnDoctor:{
                for(Rider r : adapter.getSelectedRiders()){
                    riderStageConnectionPresenter.updateRiderState(RiderStateType.DOCTOR, r);
                }
                adapter.resetSelectedRiders();
            }
            case R.id.btnDrop:{
                for(Rider r : adapter.getSelectedRiders()){
                    riderStageConnectionPresenter.updateRiderState(RiderStateType.DROP, r);
                }
                adapter.resetSelectedRiders();
            }
            case R.id.btnDefect:{
                for(Rider r : adapter.getSelectedRiders()){
                    riderStageConnectionPresenter.updateRiderState(RiderStateType.DEFECT, r);
                }
                adapter.resetSelectedRiders();
            }
            case R.id.btnQuit:{
                for(Rider r : adapter.getSelectedRiders()){
                    riderStageConnectionPresenter.updateRiderState(RiderStateType.QUIT, r);
                }
                adapter.resetSelectedRiders();
            }
            case R.id.btnDNC:{
                for(Rider r : adapter.getSelectedRiders()){
                    riderStageConnectionPresenter.updateRiderState(RiderStateType.DNC, r);
                }
                adapter.resetSelectedRiders();
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
            test.add(riders.get(i * 5 + 3));
            test.add(riders.get(i * 5 + 4));
            raceGroup.setRiders(test);
            raceGroupPresenter.addRaceGroup(raceGroup);
        }

        riderStageConnectionPresenter.clearAllRiderStageConnection();

        for(int i = 0; i < 50; i++){
            RiderStageConnection riderStageConnection = new RiderStageConnection();
            riderStageConnection.setRank(i+1);
            riderStageConnection.setOfficialTime(new Date(100));
            riderStageConnection.setOfficialGap(new Date(100));
            riderStageConnection.setVirtualGap(new Date(100));
            riderStageConnection.setBonusPoint(100);
            riderStageConnection.setBonusTime(100);
            riderStageConnection.setType(RiderStateType.AKTIVE);
            riderStageConnectionPresenter.addRiderStageConnection(riderStageConnection);
            RealmList<RiderStageConnection> test = new RealmList<>();
            test.add(riderStageConnectionPresenter.getRiderByRank(i+1));
            presenter.updateRiderStateConnection(riders.get(i), test);
            test.clear();

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
        raceGroupAdapter = new RaceGroupAdapter(raceGroups, getContext(), raceGroupPresenter, this);
        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(raceGroupAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvRaceGroup);
        rvRaceGroup.setAdapter(raceGroupAdapter);
    }

    public void addRiderToList(){
        presenter.getAllRiders();
    }

    public void addRaceGroupToList() {
        raceGroupPresenter.getAllRaceGroups();
    }

    public void updateRiderState(RiderStageConnection connection) {
        adapter.updateRiderState(connection);
        if(connection.getRiders().getRaceGroups() != null){
            raceGroupPresenter.deleteRiderInRaceGroup(connection.getRiders().getRaceGroups(), connection.getRiders());
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
