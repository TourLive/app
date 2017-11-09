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
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;

public class VirtualClassFragment extends Fragment {
    private RecyclerView rvRiderKlassement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","VirtualClassFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_virtualclass, container, false);
        return root;
    }

    public void initComponents(View root){
        RiderPresenter.getInstance().addView(this);
        rvRiderKlassement = (RecyclerView) root.findViewById(R.id.rvRiderKlassement);
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvRiderKlassement.setHasFixedSize(true);
        rvRiderKlassement.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRiderKlassement.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        RiderPresenter.getInstance().subscribeCallbacks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        RiderPresenter.getInstance().unSubscribeCallbacks();
    }
}
