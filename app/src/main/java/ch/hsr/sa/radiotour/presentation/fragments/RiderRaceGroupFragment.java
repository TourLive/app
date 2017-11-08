package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.adapter.LittleRaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderEditAdapter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RiderRaceGroupFragment extends Fragment implements View.OnClickListener, UnknownRiderDialogFragment.UnknownUserAddListener {

    private RealmList<RaceGroup> raceGroups = new RealmList<>();
    private RealmList<Rider> riders = new RealmList<>();
    private RealmList<Rider> unknownRiders = new RealmList<>();
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
    private TextView txtUnknownRiders;
    private static final Integer SLEEP_TIME = 10000;
    private Handler stateHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RiderRaceGroupFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_riderracegroup, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root){
        RiderPresenter.getInstance().addView(this);
        RaceGroupPresenter.getInstance().addView(this);
        RiderStageConnectionPresenter.getInstance().addView(this);
        rvRider = (RecyclerView) root.findViewById(R.id.rvEditRider);
        rvRider.setAdapter(new RiderEditAdapter(riders));
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
        txtUnknownRiders = (TextView) root.findViewById(R.id.txtUnknownRiders);
        btnDefect.setOnClickListener(this);
        btnDNC.setOnClickListener(this);
        btnDoctor.setOnClickListener(this);
        btnQuit.setOnClickListener(this);
        btnDrop.setOnClickListener(this);
        btnUnknownRiders.setOnClickListener(this);
        txtUnknownRiders.setOnClickListener(this);
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
        RaceGroupPresenter.getInstance().subscribeCallbacks();
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        RiderStageConnectionPresenter.getInstance().subscribeCallbacks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RiderPresenter.getInstance().unSubscribeCallbacks();
        RaceGroupPresenter.getInstance().unSubscribeCallbacks();
        RiderStageConnectionPresenter.getInstance().unSubscribeCallbacks();
    }




    public void showRiders(final RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderEditAdapter(riders);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8, LinearLayoutManager.HORIZONTAL, false);
        rvRider.setLayoutManager(mLayoutManager);
        rvRider.setAdapter(adapter);
    }

    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        adapter.updateRiderStateOnGUI(connection);
        if(connection.getRiders().getRaceGroups() != null){
            RaceGroupPresenter.getInstance().deleteRiderInRaceGroup(connection.getRiders().getRaceGroups(), connection.getRiders());
        }
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroups) {
        this.raceGroups = raceGroups;
        raceGroupAdapter = new LittleRaceGroupAdapter(this.raceGroups, this);
        rvRaceGroup.setAdapter(raceGroupAdapter);
    }

    public void addRiderToList(){
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList() {
        RaceGroupPresenter.getInstance().getAllRaceGroups();
    }

    public void updateRiderStates(RiderStateType riderStateType) {
        for (Rider r : adapter.getSelectedRiders()) {
            RiderStageConnectionPresenter.getInstance().updateRiderState(riderStateType, r);
        }
        adapter.resetSelectRiders();
    }

    @Override
    public void onClick(View view) {
        RealmList<Rider> selectedRiders = adapter.getSelectedRiders();
        switch(view.getId()) {
            case R.id.btn_Defect:
                resetStateAfterXSeconds(selectedRiders);
                updateRiderStates(RiderStateType.DEFECT);
                break;
            case R.id.btn_DNC:
                updateRiderStates(RiderStateType.DNC);
                break;
            case R.id.btn_Doctor:
                resetStateAfterXSeconds(selectedRiders);
                updateRiderStates(RiderStateType.DOCTOR);
                break;
            case R.id.btn_Drop:
                resetStateAfterXSeconds(selectedRiders);
                updateRiderStates(RiderStateType.DROP);
                break;
            case R.id.btn_Quit:
                updateRiderStates(RiderStateType.QUIT);
                break;
            case R.id.btn_UnkownRiders:
                FragmentManager fm = getFragmentManager();
                UnknownRiderDialogFragment alertDialog = UnknownRiderDialogFragment.newInstance();
                alertDialog.setTargetFragment(RiderRaceGroupFragment.this, 300);
                alertDialog.show(fm, "");
                break;
            case R.id.txtUnknownRiders:
                if (txtUnknownRiders.getBackground() == null) {
                    txtUnknownRiders.setBackgroundResource(R.drawable.backgroup_shape_racetime);
                } else {
                    removeUnknownRiders();
                }
                break;
            default:
                break;
        }
    }

    private void resetStateAfterXSeconds(RealmList<Rider> selectedRiders) {
        ArrayList<Integer> ridersStartNr = new ArrayList<>();
        for(Rider r : selectedRiders){
            ridersStartNr.add(r.getStartNr());
        }
        Runnable runnable = (() -> {
            try {
                Thread.sleep(SLEEP_TIME);
                for(Integer startNr : ridersStartNr){
                    stateHandler.post(() -> {
                        Rider rider = RiderPresenter.getInstance().getRiderByStartNr(startNr);
                        RiderStageConnectionPresenter.getInstance().updateRiderState(RiderStateType.AKTIVE, rider);
                    });
                }
            } catch (InterruptedException e) {
                Log.d(RiderRaceGroupFragment.class.getSimpleName(), "APP - OUTPUT - " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void onRaceGroupClicked(RaceGroup raceGroup, int position) {
        if (!adapter.getSelectedRiders().isEmpty()) {
            RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, adapter.getSelectedRiders());
            adapter.resetSelectRiders();
        } else if (!unknownRiders.isEmpty()) {
            RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, unknownRiders);
            removeUnknownRiders();
        }
        raceGroupAdapter.notifyItemChanged(position);
    }

    public void onNewRaceGroupClicked(int position, RaceGroupType raceGroupType) {
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setPosition(position);
        raceGroup.setType(raceGroupType);
        if (!adapter.getSelectedRiders().isEmpty()) {
            raceGroup.setRiders(adapter.getSelectedRiders());
            RaceGroupPresenter.getInstance().addRaceGroup(raceGroup);
            raceGroupAdapter.notifyDataSetChanged();
            adapter.resetSelectRiders();
        } else if (!unknownRiders.isEmpty()) {
            raceGroup.setRiders(unknownRiders);
            RaceGroupPresenter.getInstance().addRaceGroup(raceGroup);
            raceGroupAdapter.notifyDataSetChanged();
            removeUnknownRiders();
        }
    }

    @Override
    public void onFinishAddingUnknownUser(int count) {
        int start;
        RealmList<Rider> dbUnknownRiders = RiderPresenter.getInstance().getAllUnknownRidersReturned();
        if (dbUnknownRiders == null || dbUnknownRiders.isEmpty()) {
            start = 0;
        } else {
            start = dbUnknownRiders.get(dbUnknownRiders.size() - 1).getStartNr() - 900 + 1;
        }

        unknownRiders = new RealmList<>();
        for (int i = start; i < count + start; i++) {
            Rider rider = new Rider();
            rider.setUnknown(true);
            rider.setName("U" + i);
            rider.setCountry("U");
            rider.setTeamShortName("U");
            rider.setTeamName("UNKNOWN");
            rider.setStartNr(i + 900);
            RiderPresenter.getInstance().addRider(rider);
            unknownRiders.add(rider);
        }
        txtUnknownRiders.setText("" + Integer.toString(count) + " unknown Riders to add");
    }

    private void removeUnknownRiders() {
        txtUnknownRiders.setBackgroundResource(0);
        txtUnknownRiders.setText("EMPTY");
        unknownRiders.clear();
    }
}
