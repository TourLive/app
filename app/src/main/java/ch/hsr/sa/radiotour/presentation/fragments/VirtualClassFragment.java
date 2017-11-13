package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderExtendedAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.presentation.views.SortableVirtualClassementView;

public class VirtualClassFragment extends Fragment {
    private SortableVirtualClassementView sortableVirtualClassementView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
        intiTable(root);
        initComponents();
        return root;
    }

    public void initComponents(){
        RiderPresenter.getInstance().addView(this);
    }

    private void intiTable(View root) {
        List<RiderExtended> list = new ArrayList<>();
        sortableVirtualClassementView = (SortableVirtualClassementView) root.findViewById(R.id.tableView);
        if (sortableVirtualClassementView != null) {
            final RiderExtendedAdapter riderExtendedAdapter = new RiderExtendedAdapter(getContext(), list);
            sortableVirtualClassementView.setDataAdapter(riderExtendedAdapter);
        }
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
