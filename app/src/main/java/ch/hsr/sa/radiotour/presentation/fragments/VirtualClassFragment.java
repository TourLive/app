package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.gotev.recycleradapter.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderExtendedAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.presentation.models.EmptyItem;
import ch.hsr.sa.radiotour.presentation.models.ExampleItem;
import ch.hsr.sa.radiotour.presentation.views.SortableVirtualClassementView;

public class VirtualClassFragment extends Fragment {
    private Handler handler;
    private Context mContext;
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
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
        recyclerView = (RecyclerView) root.findViewById(R.id.rvVirtualClassement);
        recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.setEmptyItem(new EmptyItem(getString(android.R.string.cancel)));
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(recyclerAdapter);
        for (Rider r : RiderPresenter.getInstance().getAllRidersReturned()) {
            recyclerAdapter.add(new ExampleItem(mContext, r.getName()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //RiderPresenter.getInstance().getAllRiders();
    }

    @Override
    public void onStop() {
        super.onStop();
        RiderPresenter.getInstance().removeView(this);
    }

    public void showRiders(List<RiderExtended> riders) {
        //final RiderExtendedAdapter extendedAdapter = new RiderExtendedAdapter(getContext(), riders);
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
