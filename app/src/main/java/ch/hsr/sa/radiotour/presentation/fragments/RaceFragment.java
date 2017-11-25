package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderListAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import io.realm.RealmList;

public class RaceFragment extends Fragment {
    private RealmList<RaceGroup> raceGroups;
    private RealmList<Rider> riders;
    private RiderListAdapter riderAdapter;
    private RaceGroupAdapter raceGroupAdapter;
    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;
    private Context mContext;
    private HashMap<Integer, Integer> number = new HashMap<>();
    private static final int SPAN = 8;
    private static final int LASTNUMBER = 300;

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
        this.riders = new RealmList<>();
        this.raceGroups = new RealmList<>();
        this.riderAdapter = new RiderListAdapter(riders, mContext);
        this.raceGroupAdapter = new RaceGroupAdapter(raceGroups, mContext,  RaceFragment.this);
        rvRider.setAdapter(riderAdapter);
        rvRaceGroup = (RecyclerView) root.findViewById(R.id.rvRaceGroup);
        rvRaceGroup.setAdapter(raceGroupAdapter);
        rvRider.setLayoutManager(new LinearLayoutManager(mContext));
        rvRaceGroup.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void showRiders(RealmList<Rider> riderRealmList) {
        this.riders.clear();
        this.riders.addAll(riderRealmList);
        this.riderAdapter = new RiderListAdapter(this.riders, mContext);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mContext, 8);
        number = UIUtilitis.getCountsPerLine(riders);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int startNumber = riderAdapter.getItemStartNr(position);
                int nextStartNumber;
                if (riderAdapter.getItemCount() > position + 1) {
                    nextStartNumber = riderAdapter.getItemStartNr(position + 1);
                } else {
                    nextStartNumber = LASTNUMBER;
                }

                int firstStartNumber = UIUtilitis.getFirstDigit(startNumber);
                int firstNextStartNumber = UIUtilitis.getFirstDigit(nextStartNumber);
                int divFirst = firstNextStartNumber - firstStartNumber;
                int sizeSpan = number.get(firstStartNumber);
                if (sizeSpan < SPAN && divFirst > 0) {
                    return SPAN - UIUtilitis.getLastDigit(startNumber) + 1;
                }
                return 1;
            }
        });
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.swapAdapter(riderAdapter,true);
        rvRider.scrollBy(0,0);
        this.riderAdapter.notifyDataSetChanged();
    }

    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        riderAdapter.updateRiderStateOnGUI(connection);
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroupRealmList) {
        this.raceGroups.clear();
        this.raceGroups.addAll(raceGroupRealmList);
        rvRaceGroup.swapAdapter(new RaceGroupAdapter(raceGroups, mContext, RaceFragment.this),true);
        rvRaceGroup.scrollBy(0,0);
        this.raceGroupAdapter.notifyDataSetChanged();
    }

    public void addRiderToList(){
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList() {
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        riderAdapter.updateAnimateRiderInGroup();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        RiderPresenter.getInstance().getAllRiders();
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
    }
}
