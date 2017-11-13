package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.MaillotPresenter;
import ch.hsr.sa.radiotour.controller.adapter.JudgementAdapter;
import ch.hsr.sa.radiotour.controller.adapter.MaillotsAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public class MaillotsFragment extends Fragment {
    private RecyclerView rvMaillots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","MaillotsFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_maillots, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        MaillotPresenter.getInstance().addView(this);
        rvMaillots = (RecyclerView) root.findViewById(R.id.rvMaillot);
        rvMaillots.setAdapter(new MaillotsAdapter(MaillotPresenter.getInstance().getAllMaillots()));
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvMaillots.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMaillots.setItemAnimator(new DefaultItemAnimator());
    }
}
