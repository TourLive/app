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
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.interfaces.IRiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RaceFragment extends Fragment implements OnStartDragListener {

    private IRaceGroupPresenter raceGroupPresenter;
    private IRiderStageConnectionPresenter riderStageConnectionPresenter;
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;

    private RiderAdapter adapter;
    private RaceGroupAdapter raceGroupAdapter;

    private ItemTouchHelper itemTouchHelper;

    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RaceFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_race, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root){
        rvRider = (RecyclerView) root.findViewById(R.id.rvRider);
        rvRider.setAdapter(new RiderAdapter(new RealmList<Rider>()));
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvRaceGroup);
        RiderPresenter.getInstance().addView(this);
        raceGroupPresenter = new RaceGroupPresenter(this);
        riderStageConnectionPresenter = new RiderStageConnectionPresenter(this);
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
        adapter = new RiderAdapter(riders);
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
