package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;

public class SpecialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","SpecialFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_special, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        initRecyclerListener();
    }

    private void initRecyclerListener() {
    }

    @Override
    public void onStart() {
        super.onStart();
        JudgmentPresenter.getInstance().subscribeCallbacks();
        RiderPresenter.getInstance().subscribeCallbacks();
        RewardPresenter.getInstance().subscribeCallbacks();
        RewardRiderConnectionPresenter.getInstance().subscribeCallbacks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        JudgmentPresenter.getInstance().unSubscribeCallbacks();
        RiderPresenter.getInstance().unSubscribeCallbacks();
        RewardPresenter.getInstance().unSubscribeCallbacks();
        RewardRiderConnectionPresenter.getInstance().unSubscribeCallbacks();
    }


}
