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

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.adapter.LittleRaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.OnStartDragListener;
import ch.hsr.sa.radiotour.controller.adapter.RaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderEditAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderListAdapter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RiderRaceGroupFragment extends Fragment implements IPresenterFragments, View.OnClickListener {

    private IRaceGroupPresenter raceGroupPresenter;
    private IRiderStageConnectionPresenter riderStageConnectionPresenter;
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;

    private RiderEditAdapter adapter;
    private LittleRaceGroupAdapter raceGroupAdapter;

    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;

    private Button btnDoctor;
    private Button btnDNC;
    private Button btnDefect;
    private Button btnQuit;
    private Button btnDrop;
    private Button btnUnknownRiders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RiderRaceGroupFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_riderracegroup, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root){
        RiderPresenter.getInstance().addView(this);
        raceGroupPresenter = new RaceGroupPresenter(this);
        riderStageConnectionPresenter = new RiderStageConnectionPresenter(this);
        rvRider = (RecyclerView) root.findViewById(R.id.rvEditRider);
        rvRider.setAdapter(new RiderEditAdapter(new RealmList<Rider>()));
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvEditRaceGroup);
        initRecyclerListener();
        initButtons(root);
    }

    private void initButtons(View root) {
        btnDefect = (Button) root.findViewById(R.id.btn_Defect);
        btnDNC = (Button) root.findViewById(R.id.btn_DNC);
        btnDoctor = (Button) root.findViewById(R.id.btn_Doctor);
        btnQuit = (Button) root.findViewById(R.id.btn_Quit);
        btnDrop = (Button) root.findViewById(R.id.btn_Drop);
        btnUnknownRiders = (Button) root.findViewById(R.id.btn_UnkownRiders);
        btnDefect.setOnClickListener(this);
        btnDNC.setOnClickListener(this);
        btnDoctor.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        btnDrop.setOnClickListener(this);
        btnUnknownRiders.setOnClickListener(this);
    }

    private void initRecyclerListener() {
        rvRider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRider.setItemAnimator(new DefaultItemAnimator());
        rvRaceGroup.setHasFixedSize(true);
        rvRaceGroup.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRaceGroup.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        RiderPresenter.getInstance().subscribeCallbacks();
        RiderPresenter.getInstance().getAllRiders();
        raceGroupPresenter.subscribeCallbacks();
        raceGroupPresenter.getAllRaceGroups();
        riderStageConnectionPresenter.subscribeCallbacks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RiderPresenter.getInstance().unSubscribeCallbacks();
        raceGroupPresenter.unSubscribeCallbacks();
        riderStageConnectionPresenter.unSubscribeCallbacks();
    }




    public void showRiders(final RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderEditAdapter(riders);
        int rows = getFirstDigit(riders.get(riders.size() -1).getStartNr());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8, LinearLayoutManager.HORIZONTAL, false);
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.setAdapter(adapter);
    }

    @Override
    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        adapter.updateRiderStateOnGUI(connection);
        if(connection.getRiders().getRaceGroups() != null){
            raceGroupPresenter.deleteRiderInRaceGroup(connection.getRiders().getRaceGroups(), connection.getRiders());
        }
    }

    public int getFirstDigit(int number) {
        if (number/10 == 0) {
            return number;
        }
        return getFirstDigit(number/10);
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroups) {
        this.raceGroups = raceGroups;
        raceGroupAdapter = new LittleRaceGroupAdapter(raceGroups, getContext(), this);
        rvRaceGroup.setAdapter(raceGroupAdapter);
    }

    public void addRiderToList(){
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList() {
        raceGroupPresenter.getAllRaceGroups();
    }

    public void updateRiderStates(RiderStateType riderStateType) {
        for (Rider r : adapter.getSelectedRiders()) {
            riderStageConnectionPresenter.updateRiderState(riderStateType, r);
        }
        adapter.resetSelectRiders();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_Defect:
                updateRiderStates(RiderStateType.DEFECT);
                break;
            case R.id.btn_DNC:
                updateRiderStates(RiderStateType.DNC);
                break;
            case R.id.btn_Doctor:
                updateRiderStates(RiderStateType.DOCTOR);
                break;
            case R.id.btn_Drop:
                updateRiderStates(RiderStateType.DROP);
                break;
            case R.id.btn_Quit:
                updateRiderStates(RiderStateType.QUIT);
                break;
            case R.id.btn_UnkownRiders:
                break;
            default:
                break;
        }
    }

    public void onRaceGroupClicked(RaceGroup raceGroup, int position) {
        if (adapter.getSelectedRiders().size() != 0) {
            raceGroupPresenter.updateRaceGroupRiders(raceGroup, adapter.getSelectedRiders());
            raceGroupAdapter.notifyItemChanged(position);
            adapter.resetSelectRiders();
        }
    }

    public void onNewRaceGroupClicked(int position, RaceGroupType raceGroupType) {
        if (adapter.getSelectedRiders().size() != 0) {
            RaceGroup raceGroup = new RaceGroup();
            raceGroup.setPosition(position);
            raceGroup.setType(raceGroupType);
            raceGroup.setRiders(adapter.getSelectedRiders());
            raceGroupPresenter.addRaceGroup(raceGroup);
            raceGroupAdapter.notifyDataSetChanged();
            adapter.resetSelectRiders();
        }
    }
}
