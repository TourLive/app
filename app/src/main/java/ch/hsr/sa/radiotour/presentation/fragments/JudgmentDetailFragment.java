package ch.hsr.sa.radiotour.presentation.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderBasicAdapter;

public class JudgmentDetailFragment extends Fragment {
    private String id;
    private RecyclerView rvRidersToSelect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_detail_judgment, container, false);
        id = getArguments().getString("id");
        rvRidersToSelect = (RecyclerView) root.findViewById(R.id.rvRidersToSelect);
        rvRidersToSelect.setAdapter(new RiderBasicAdapter(RiderPresenter.getInstance().getAllRidersReturned()));
        GridLayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 8, LinearLayoutManager.VERTICAL, false);
        rvRidersToSelect.setLayoutManager(mLayoutManager);
        return root;
    }
}
