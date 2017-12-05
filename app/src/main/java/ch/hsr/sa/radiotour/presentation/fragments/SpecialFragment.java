package ch.hsr.sa.radiotour.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import ch.hsr.sa.radiotour.controller.adapter.JudgementAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import io.realm.RealmList;

public class SpecialFragment extends Fragment implements OnJudgmentClickListener {
    private RealmList<Judgement> judgements;
    private JudgementAdapter judgementAdapter;
    private RecyclerView rvJudgement;
    private Context mContext;
    private int actualOffset = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "SpecialFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_special, container, false);
        initComponents(root);
        return root;
    }

    public void initComponents(View root) {
        JudgmentPresenter.getInstance().addView(this);
        RewardPresenter.getInstance().addView(this);
        JudgmentRiderConnectionPresenter.getInstance().addView(this);
        this.judgements = new RealmList<>();
        this.judgementAdapter = new JudgementAdapter(judgements, mContext, this);
        rvJudgement = (RecyclerView) root.findViewById(R.id.rvJudgements);
        rvJudgement.setAdapter(judgementAdapter);
        rvJudgement.setLayoutManager(new LinearLayoutManager(mContext));
        rvJudgement.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
               actualOffset = rvJudgement.computeVerticalScrollOffset();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        JudgmentPresenter.getInstance().getAllJudgments();
    }

    public void showJudgments(RealmList<Judgement> judgementRealmList) {
        this.judgements.clear();
        this.judgements.addAll(judgementRealmList);
        rvJudgement.swapAdapter(new JudgementAdapter(judgements, mContext, this), true);
        rvJudgement.scrollBy(0, actualOffset);
        this.judgementAdapter.notifyDataSetChanged();
    }


    @Override
    public void onJudgmentClicked(Judgement judgement) {
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
}
