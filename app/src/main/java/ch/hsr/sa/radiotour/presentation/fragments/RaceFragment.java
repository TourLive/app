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

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.EditItemTouchHelperCallback;
import ch.hsr.sa.radiotour.controller.adapter.OnStartDragListener;
import ch.hsr.sa.radiotour.controller.adapter.RaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderListAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public class RaceFragment extends Fragment implements OnStartDragListener {

    private RealmList<RaceGroup> raceGroups = new RealmList<>();
    private RealmList<Rider> riders;
    private RiderListAdapter adapter;
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
        RiderPresenter.getInstance().addView(this);
        RaceGroupPresenter.getInstance().addView(this);
        RiderStageConnectionPresenter.getInstance().addView(this);
        rvRider = (RecyclerView) root.findViewById(R.id.rvRider);
        rvRider.setAdapter(new RiderListAdapter(new RealmList<Rider>()));
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvRaceGroup);
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvRider.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRaceGroup.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    public void showRiders(RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderListAdapter(riders);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8);
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        adapter.updateRiderStateOnGUI(connection);
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroups) {
        this.raceGroups = raceGroups;
        this.raceGroupAdapter = new RaceGroupAdapter(this.raceGroups, getContext(), RaceGroupPresenter.getInstance(), this, RaceFragment.this);
        ItemTouchHelper.Callback callback = new EditItemTouchHelperCallback(raceGroupAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvRaceGroup);
        rvRaceGroup.setAdapter(this.raceGroupAdapter);
        adapter.notifyDataSetChanged();
    }

    public void addRiderToList(){
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList() {
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        adapter.updateAnimateRiderInGroup(riders);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
