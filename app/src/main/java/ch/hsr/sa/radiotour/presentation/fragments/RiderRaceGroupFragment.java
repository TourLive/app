package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.LittleRaceGroupAdapter;
import ch.hsr.sa.radiotour.controller.adapter.RiderEditAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import io.realm.RealmList;

public class RiderRaceGroupFragment extends Fragment implements View.OnClickListener, UnknownRiderDialogFragment.UnknownUserAddListener {

    private static final int SPAN = 8;
    private static final int LASTNUMBER = 300;
    private RealmList<RaceGroup> raceGroups = new RealmList<>();
    private RealmList<Rider> riders = new RealmList<>();
    private RealmList<Rider> unknownRiders = new RealmList<>();
    private ArrayList<Integer> resetRiderAnimation = new ArrayList<>();
    private RiderEditAdapter adapter;
    private LittleRaceGroupAdapter raceGroupAdapter;
    private RecyclerView rvRider;
    private RecyclerView rvRaceGroup;
    private TextView txtUnknownRiders;
    private Context mContext;
    private Handler stateHandler = new Handler();
    private HashMap<Integer, Integer> number = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "RiderRaceGroupFragment - onCreateView");
        View root = inflater.inflate(R.layout.fragment_riderracegroup, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        RiderPresenter.getInstance().addView(this);
        RaceGroupPresenter.getInstance().addView(this);
        RiderStageConnectionPresenter.getInstance().addView(this);
        rvRider = root.findViewById(R.id.rvEditRider);
        rvRider.setAdapter(new RiderEditAdapter(riders, mContext));
        rvRaceGroup = root.findViewById(R.id.rvEditRaceGroup);
        initRecyclerListener();
        initButtons(root);
    }

    private void initButtons(View root) {
        Button btnDefect = root.findViewById(R.id.btn_Defect);
        Button btnDNC = root.findViewById(R.id.btn_DNC);
        Button btnDoctor = root.findViewById(R.id.btn_Doctor);
        Button btnQuit = root.findViewById(R.id.btn_Quit);
        Button btnDrop = root.findViewById(R.id.btn_Drop);
        Button btnUnknownRiders = root.findViewById(R.id.btn_UnkownRiders);
        txtUnknownRiders = root.findViewById(R.id.txtUnknownRiders);
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

    public void showRiders(final RealmList<Rider> riders) {
        this.riders = riders;
        adapter = new RiderEditAdapter(riders, mContext);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8, LinearLayoutManager.HORIZONTAL, false);
        number = UIUtilitis.getCountsPerLine(riders);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int startNumber = adapter.getItemStartNr(position);
                int nextStartNumber;
                if (adapter.getItemCount() > position + 1) {
                    nextStartNumber = adapter.getItemStartNr(position + 1);
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
        rvRider.setAdapter(adapter);
    }

    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        adapter.updateRiderStateOnGUI(connection);
        boolean stillInRace = connection.getRiders().getRiderStages().first().getType() != RiderStateType.QUIT
                && connection.getRiders().getRiderStages().first().getType() != RiderStateType.DNC;
        if (connection.getRiders().getRaceGroups() != null && !stillInRace) {
            RaceGroupPresenter.getInstance().deleteRiderInRaceGroup(connection.getRiders().getRaceGroups(), connection.getRiders());
        }
    }

    public void showRaceGroups(RealmList<RaceGroup> raceGroups) {
        this.raceGroups = raceGroups;
        raceGroupAdapter = new LittleRaceGroupAdapter(this.raceGroups, this);
        rvRaceGroup.setAdapter(raceGroupAdapter);
    }

    public void addRiderToList() {
        RiderPresenter.getInstance().getAllRiders();
    }

    public void addRaceGroupToList(String raceGroupId) {
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        adapter.updateAnimateRiderInGroup(raceGroupId);
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
        switch (view.getId()) {
            case R.id.btn_Defect:
                addRiderToResetAfterTabChange(selectedRiders);
                updateRiderStates(RiderStateType.DEFECT);
                break;
            case R.id.btn_DNC:
                updateRiderStates(RiderStateType.DNC);
                break;
            case R.id.btn_Doctor:
                addRiderToResetAfterTabChange(selectedRiders);
                updateRiderStates(RiderStateType.DOCTOR);
                break;
            case R.id.btn_Drop:
                addRiderToResetAfterTabChange(selectedRiders);
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
            default:
                break;
        }
    }

    private void addRiderToResetAfterTabChange(RealmList<Rider> riders) {
        for (Rider r : riders) {
            resetRiderAnimation.add(r.getStartNr());
        }
    }

    public void onRaceGroupClicked(RaceGroup raceGroup, int position) {
        if (!adapter.getSelectedRiders().isEmpty()) {
            RealmList<Rider> activeRider = filterNonActiveRiders(adapter.getSelectedRiders());
            if (activeRider.isEmpty()) {
                adapter.resetSelectRiders();
                return;
            }
            RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, activeRider);
            adapter.resetSelectRiders();
        } else if (!unknownRiders.isEmpty()) {
            RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, unknownRiders);
            removeUnknownRiders();
        }
        raceGroupAdapter.notifyItemChanged(position);
    }

    public void onNewRaceGroupClicked(RaceGroup beforeRaceGroup, RaceGroupType raceGroupType) {
        RaceGroup raceGroup = new RaceGroup();
        raceGroup.setPosition(beforeRaceGroup.getPosition() + 1);
        raceGroup.setActualGapTime(0);
        raceGroup.setType(raceGroupType);
        if (!adapter.getSelectedRiders().isEmpty()) {
            RealmList<Rider> activeRider = filterNonActiveRiders(adapter.getSelectedRiders());
            if (activeRider.isEmpty()) {
                adapter.resetSelectRiders();
                return;
            }
            raceGroup.setRiders(activeRider);
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
            RiderPresenter.getInstance().addRiderNone(rider);
            unknownRiders.add(RiderPresenter.getInstance().getRiderByStartNr(rider.getStartNr()));
        }
        txtUnknownRiders.setText(getString(R.string.race_unknownrider_value, Integer.toString(count)));
        txtUnknownRiders.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
    }

    private void removeUnknownRiders() {
        txtUnknownRiders.setBackgroundResource(0);
        txtUnknownRiders.setText(R.string.race_unknownrider_empty);
        unknownRiders.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
        RiderPresenter.getInstance().getAllRiders();
        RaceGroupPresenter.getInstance().getAllRaceGroups();
        RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void resetStates() {
        stateHandler.post(() -> {
            for (int startNr : resetRiderAnimation) {
                Rider rider = RiderPresenter.getInstance().getRiderByStartNr(startNr);
                RiderStageConnectionPresenter.getInstance().updateRiderState(RiderStateType.ACTIVE, rider);
            }
            resetRiderAnimation.clear();
        });
    }

    private RealmList<Rider> filterNonActiveRiders(RealmList<Rider> riders) {
        RealmList<Rider> activeRiders = new RealmList<>();
        for (Rider r : riders) {
            if (!(r.getRiderStages().first().getType() == RiderStateType.DNC ||
                    r.getRiderStages().first().getType() == RiderStateType.QUIT)) {
                activeRiders.add(r);
            }
        }
        return activeRiders;
    }
}
