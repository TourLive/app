package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.JudgementAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public class SpecialFragment extends Fragment implements OnJudgmentClickListener {
    private RealmList<Judgement> judgements;
    private JudgementAdapter judgementAdapter;
    private RecyclerView rvJudgement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","SpecialFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_special, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        JudgmentPresenter.getInstance().addView(this);
        RewardPresenter.getInstance().addView(this);
        JudgmentRiderConnectionPresenter.getInstance().addView(this);
        rvJudgement = (RecyclerView) root.findViewById(R.id.rvJudgements);
        rvJudgement.setAdapter(new JudgementAdapter(new RealmList<Judgement>(), getContext(), this));
        initRecyclerListener();
    }

    private void initRecyclerListener() {
        rvJudgement.setLayoutManager(new LinearLayoutManager(getContext()));
        rvJudgement.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onStart() {
        super.onStart();
        JudgmentPresenter.getInstance().subscribeCallbacks();
        JudgmentPresenter.getInstance().getAllJudgments();
        RiderPresenter.getInstance().subscribeCallbacks();
        RewardPresenter.getInstance().subscribeCallbacks();
        JudgmentRiderConnectionPresenter.getInstance().subscribeCallbacks();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        JudgmentPresenter.getInstance().unSubscribeCallbacks();
        RiderPresenter.getInstance().unSubscribeCallbacks();
        RewardPresenter.getInstance().unSubscribeCallbacks();
        JudgmentRiderConnectionPresenter.getInstance().unSubscribeCallbacks();
    }

    public void showJudgments(RealmList<Judgement> judgements) {
        this.judgements = judgements;
        judgementAdapter = new JudgementAdapter(judgements, getContext(), this);
        rvJudgement.setAdapter(judgementAdapter);
    }


    @Override
    public void onJudgmentClicked(Judgement judgement) {
        Log.d("DA", "on Judgement clicked");
        Log.d("DA", "" + judgement.toString());
        openDetailJudgmentFragment(judgement.getId());
    }

    private void openDetailJudgmentFragment(String judgementId) {
        Bundle arguments = new Bundle();
        arguments.putString("id", judgementId);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        JudgmentDetailFragment fragment = new JudgmentDetailFragment();
        fragment.setArguments(arguments);
        transaction.replace(R.id.layoutDetailJudgment, fragment, "DETAILJ");
        transaction.commit();
    }

    public void updateList() {
        JudgmentPresenter.getInstance().getAllJudgments();
    }
}
