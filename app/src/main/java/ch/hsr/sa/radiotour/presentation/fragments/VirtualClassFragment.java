package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.gotev.recycleradapter.AdapterItem;
import net.gotev.recycleradapter.RecyclerAdapter;

import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.VirtualClassementComparators;
import ch.hsr.sa.radiotour.controller.api.PostHandler;
import ch.hsr.sa.radiotour.presentation.models.EmptyItem;
import ch.hsr.sa.radiotour.presentation.models.VirtualClassementRider;
import io.realm.RealmList;

public class VirtualClassFragment extends Fragment {
    private Handler handler;
    private Context mContext;
    private RecyclerAdapter recyclerAdapter;
    @BindView(R.id.rvVirtualClassement)
    RecyclerView recyclerView;
    @BindView(R.id.virtualClassementStartNrHeader)
    TextView virtualClassementStartNrHeader;
    @BindView(R.id.virtualClassementNameHeader)
    TextView virtualClassementNameHeader;
    @BindView(R.id.virtualClassementTeamHeader)
    TextView virtualClassementTeamHeader;
    @BindView(R.id.virtualClassementCountryHeader)
    TextView virtualClassementCountryHeader;
    @BindView(R.id.virtualClassementOfficialTimeHeader)
    TextView virtualClassementOfficialTimeHeader;
    @BindView(R.id.virtualClassementOfficalGapHeader)
    TextView virtualClassementOfficalGapHeader;
    @BindView(R.id.virtualClassementVirtualGapHeader)
    TextView virtualClassementVirtualGapHeader;
    @BindView(R.id.virtualClassementPointsHeader)
    TextView virtualClassementPointsHeader;
    @BindView(R.id.virtualClassementMountainPointsHeader)
    TextView virtualClassementMountainPointsHeader;
    @BindView(R.id.virtualClassementSprintPointsHeader)
    TextView virtualClassementSprintPointsHeader;
    @BindView(R.id.virtualClassementMoneyHeader)
    TextView virtualClassementMoneyHeader;
    @BindView(R.id.virtualClassementTimeInLeadHeader)
    TextView virtualClassementTimeInLeadHeader;
    private TextView selectedColumn;
    private View selectedRow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
        ButterKnife.bind(this, root);
        intiTable(root);
        initComponents();
        return root;
    }

    public void initComponents() {
        RiderPresenter.getInstance().addView(this);
        RiderStageConnectionPresenter.getInstance().addView(this);
        handler = new Handler();
    }

    private void intiTable(View root) {
        recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.setEmptyItem(new EmptyItem(getString(android.R.string.cancel)));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);
        for (Rider r : RiderPresenter.getInstance().getAllRidersReturned()) {
            recyclerAdapter.add(new VirtualClassementRider(this, mContext, r));
        }
    }

    public void resetAndSetSelectedRow(View v) {
        if (selectedRow != null) {
            selectedRow.setBackgroundColor(0);
        }
        selectedRow = v;
    }

    @OnClick({R.id.virtualClassementStartNrHeader, R.id.virtualClassementNameHeader, R.id.virtualClassementTeamHeader, R.id.virtualClassementCountryHeader, R.id.virtualClassementOfficialTimeHeader, R.id.virtualClassementOfficalGapHeader, R.id.virtualClassementVirtualGapHeader, R.id.virtualClassementPointsHeader, R.id.virtualClassementMountainPointsHeader, R.id.virtualClassementSprintPointsHeader, R.id.virtualClassementMoneyHeader, R.id.virtualClassementTimeInLeadHeader})
    public void onSortClicked(View view) {
        Comparator<AdapterItem> comp = null;
        if (selectedColumn != null) {
            selectedColumn.setBackgroundColor(0);
        }
        selectedColumn = (TextView) view;
        switch(view.getId()) {
            case R.id.virtualClassementStartNrHeader:
                comp = VirtualClassementComparators.getStartNrComparator();
                break;
            case R.id.virtualClassementNameHeader:
                comp = VirtualClassementComparators.getNameComparator();
                break;
            case R.id.virtualClassementTeamHeader:
                comp = VirtualClassementComparators.getTeamComparator();
                break;
            case R.id.virtualClassementCountryHeader:
                comp = VirtualClassementComparators.getCountryComparator();
                break;
            case R.id.virtualClassementOfficialTimeHeader:
                comp = VirtualClassementComparators.getOfficialTimeComparator();
                break;
            case R.id.virtualClassementOfficalGapHeader:
                comp = VirtualClassementComparators.getOffizialDeficitComparator();
                break;
            case R.id.virtualClassementVirtualGapHeader:
                comp = VirtualClassementComparators.getVirtualDeficitComparator();
                break;
            case R.id.virtualClassementPointsHeader:
                comp = VirtualClassementComparators.getPointComparator();
                break;
            case R.id.virtualClassementMountainPointsHeader:
                comp = VirtualClassementComparators.getMountainPointComparator();
                break;
            case R.id.virtualClassementSprintPointsHeader:
                comp = VirtualClassementComparators.getSprintPointComparator();
                break;
            case R.id.virtualClassementMoneyHeader:
                comp = VirtualClassementComparators.getMoneyComparator();
                break;
            case R.id.virtualClassementTimeInLeadHeader:
                comp = VirtualClassementComparators.getTimeInLeadGroupComparator();
                break;
            default:
                comp = VirtualClassementComparators.getStartNrComparator();
                break;
        }
        selectedColumn.setBackgroundColor(ContextCompat.getColor(mContext ,R.color.colorAccent));
        recyclerAdapter.sort(true, comp);
    }

    @Override
    public void onStop() {
        super.onStop();
        RiderPresenter.getInstance().removeView(this);
    }

    public void updateRiders(RealmList<Rider> riders) {
        recyclerAdapter.clear();
        for (Rider r : riders) {
            recyclerAdapter.add(new VirtualClassementRider(this, mContext, r));
        }
        recyclerAdapter.notifyDataSetChanged();
    }

    public void updateRiderStageConnection() {
        handler.post(() -> RiderPresenter.getInstance().getAllRiders());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
