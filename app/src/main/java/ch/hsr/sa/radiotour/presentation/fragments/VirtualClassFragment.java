package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderExtendedAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.presentation.views.SortableVirtualClassementView;

public class VirtualClassFragment extends Fragment {
    private SortableVirtualClassementView sortableVirtualClassementView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
        initComponents(root);
        intiTable(root);
        return root;
    }

    public void initComponents(View root){
        RiderPresenter.getInstance().addView(this);
        context = getContext();
    }

    private void intiTable(View root) {
        List<RiderExtended> list = new ArrayList<>();
        sortableVirtualClassementView = (SortableVirtualClassementView) root.findViewById(R.id.tableView);
        if (sortableVirtualClassementView != null) {
            final RiderExtendedAdapter riderExtendedAdapter = new RiderExtendedAdapter(getContext(), list);
            sortableVirtualClassementView.setDataAdapter(riderExtendedAdapter);
        }
    }

    private List<RiderExtended> createRiders() {
        List<RiderExtended> createdRiders = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            RiderExtended rE = new RiderExtended();
            rE.setBonusPoint(i);
            rE.setBonusTime(i);
            rE.setCountry("CHF" + i);
            rE.setMoney(100 * i);
            rE.setName("NAME" + i);
            rE.setOfficialGap(new Date());
            rE.setOfficialTime(new Date());
            rE.setVirtualGap(new Date());
            rE.setRank(i);
            rE.setStartNr(1+i);
            rE.setTeamShortName("T" + i);
            createdRiders.add(rE);
        }
        return createdRiders;
    }

    @Override
    public void onStart() {
        super.onStart();
        RiderPresenter.getInstance().subscribeCallbacks();
        RiderPresenter.getInstance().getAllRiders();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RiderPresenter.getInstance().unSubscribeCallbacks();
    }

    @Override
    public void onStop(){
        super.onStop();
        RiderPresenter.getInstance().unSubscribeCallbacks();
        RiderPresenter.getInstance().removeView(this);
    }

    public void showRiders(List<RiderExtended> riders) {
        final RiderExtendedAdapter extendedAdapter = new RiderExtendedAdapter(getContext(), riders);
        sortableVirtualClassementView.setDataAdapter(extendedAdapter);
    }
}
