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
import ch.hsr.sa.radiotour.presentation.activites.MainActivity;
import io.realm.RealmList;

public class RaceFragment extends Fragment implements IPresenterFragments, OnStartDragListener, View.OnClickListener {

    private IRaceGroupPresenter raceGroupPresenter;
    private IRiderStageConnectionPresenter riderStageConnectionPresenter;
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;

    private RiderListAdapter adapter;
    private RaceGroupAdapter raceGroupAdapter;

    private ItemTouchHelper itemTouchHelper;

    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RaceFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_race, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root){
        RiderPresenter.getInstance().addView(this);
        raceGroupPresenter = new RaceGroupPresenter(this);
        riderStageConnectionPresenter = new RiderStageConnectionPresenter(this);
        addDefaultData();
        rvRider = (RecyclerView) root.findViewById(R.id.rvRider);
        rvRider.setOnClickListener(this);
        rvRider.setAdapter(new RiderListAdapter(new RealmList<Rider>()));
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvRaceGroup);
        button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(this);
        initRecyclerListener();
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




    public void showRiders(RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderListAdapter(riders);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8);
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
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList() {
        raceGroupPresenter.getAllRaceGroups();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    public void addDefaultData(){

        RiderPresenter.getInstance().clearAllRiders();
        RealmList<Rider> riders = new RealmList<>();
        for(int i = 0; i < 50; i++){
            Rider rider = new Rider();
            rider.setStartNr(i);
            rider.setCountry("swiss");
            rider.setName("rider" + i);
            rider.setTeamName("empty");
            rider.setShortTeamName("emtpy");
            RiderPresenter.getInstance().addRider(rider);
            riders.add(rider);
        }
        raceGroupPresenter.clearAllRaceGroups();
        RaceGroup raceGroup = new RaceGroup();
        for (int i = 0; i < 5; i++) {
            if (i == 1) {
                raceGroup.setType(RaceGroupType.FELD);
            } else {
                raceGroup.setType(RaceGroupType.LEAD);
            }
            raceGroup.setPosition(i);
            raceGroup.setHistoryGapTime(60+(long)i);
            raceGroup.setActualGapTime(i);
            RealmList<Rider> test = new RealmList<>();
            test.add(riders.get(i * 5));
            test.add(riders.get(i * 5 + 1));
            test.add(riders.get(i * 5 + 2));
            test.add(riders.get(i * 5 + 3));
            test.add(riders.get(i * 5 + 4));
            raceGroup.setRiders(test);
            raceGroupPresenter.addInitialRaceGroup(raceGroup);
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
            if (i == 4 || i == 9 || i == 14) {
                riderStageConnection.setType(RiderStateType.DOCTOR);
            } else if (i == 3 || i == 8 || i == 13) {
                riderStageConnection.setType(RiderStateType.DNC);
            } else {
                riderStageConnection.setType(RiderStateType.AKTIVE);
            }
            riderStageConnectionPresenter.addRiderStageConnection(riderStageConnection);
            RealmList<RiderStageConnection> test = new RealmList<>();
            test.add(riderStageConnectionPresenter.getRiderByRank(i+1));
            RiderPresenter.getInstance().updateRiderStageConnection(riders.get(i), test);
            test.clear();

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                MainActivity.viewPageAdapter.setDetail(true);
                MainActivity.viewPager.getAdapter().notifyDataSetChanged();
                break;
            default:
                break;
        }

    }
}
