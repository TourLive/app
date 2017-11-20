package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderBasicAdapter;
import ch.hsr.sa.radiotour.dataaccess.RiderStageConnectionUtilities;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public class JudgmentDetailFragment extends Fragment implements View.OnClickListener {
    private Judgement judgement;
    private RecyclerView rvRidersToSelect;
    private RiderBasicAdapter riderBasicAdapter;
    private TextView rankOne;
    private TextView rankTwo;
    private TextView rankThree;
    private TextView rankFour;
    private TextView rankFive;
    private TextView rankOneTxt;
    private TextView rankTwoTxt;
    private TextView rankThreeTxt;
    private TextView rankFourTxt;
    private TextView rankFiveTxt;
    private TextView title;
    private List<TextView> textViews = new ArrayList<>();
    private List<TextView> headers = new ArrayList<>();
    private Reward rewardM;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_detail_judgment, container, false);
        judgement = JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(getArguments().getString("id"));

        title = (TextView) root.findViewById(R.id.titleJudgements2);
        title.setText(judgement.getDistance() + " | " + judgement.getName());

        rvRidersToSelect = (RecyclerView) root.findViewById(R.id.rvRidersToSelect);
        riderBasicAdapter = new RiderBasicAdapter(RiderPresenter.getInstance().getAllRidersReturned());
        rvRidersToSelect.setAdapter(riderBasicAdapter);
        rvRidersToSelect.setLayoutManager(new GridLayoutManager(getContext(), 12));
        rvRidersToSelect.setHasFixedSize(true);
        initRankView(root);

        return root;
    }

    private void initRankView(View root) {
        rankOne = (TextView) root.findViewById(R.id.RankOne);
        rankTwo = (TextView) root.findViewById(R.id.RankTwo);
        rankThree = (TextView) root.findViewById(R.id.RankThree);
        rankFour = (TextView) root.findViewById(R.id.RankFour);
        rankFive = (TextView) root.findViewById(R.id.RankFive);
        rankOneTxt = (TextView) root.findViewById(R.id.txtRankOne);
        rankTwoTxt = (TextView) root.findViewById(R.id.txtRankTwo);
        rankThreeTxt = (TextView) root.findViewById(R.id.txtRankThree);
        rankFourTxt = (TextView) root.findViewById(R.id.txtRankFour);
        rankFiveTxt = (TextView) root.findViewById(R.id.txtRankFive);
        initListener();
        iniList();

        List<Boolean> work = getWorkProcesses();
        for (int o = 1; o < work.size(); o++) {
            if (!work.get(o)) {
                textViews.get(o).setVisibility(View.GONE);
                headers.get(o).setVisibility(View.GONE);
            }
        }

        initTextViewsWithData();
    }

    private void iniList() {
        textViews.add(rankOne);
        textViews.add(rankTwo);
        textViews.add(rankThree);
        textViews.add(rankFour);
        textViews.add(rankFive);
        headers.add(rankOneTxt);
        headers.add(rankTwoTxt);
        headers.add(rankThreeTxt);
        headers.add(rankFourTxt);
        headers.add(rankFiveTxt);
    }

    private void initListener() {
        rankOne.setOnClickListener(this);
        rankTwo.setOnClickListener(this);
        rankThree.setOnClickListener(this);
        rankFour.setOnClickListener(this);
        rankFive.setOnClickListener(this);
    }

    private void initTextViewsWithData() {

        for (JudgmentRiderConnection jRc : JudgmentRiderConnectionPresenter.getInstance().getJudgmentRiderConnectionsReturnedByJudgment(judgement)){
            switch (jRc.getRank()) {
                case 1:
                    rankOne.setText(String.valueOf(jRc.getRider().first().getStartNr()));
                    break;
                case 2:
                    rankTwo.setText(String.valueOf(jRc.getRider().first().getStartNr()));
                    break;
                case 3:
                    rankThree.setText(String.valueOf(jRc.getRider().first().getStartNr()));
                    break;
                case 4:
                    rankFour.setText(String.valueOf(jRc.getRider().first().getStartNr()));
                    break;
                case 5:
                    rankFive.setText(String.valueOf(jRc.getRider().first().getStartNr()));
                    break;
                default:
                    break;
            }
        }
    }

    private List<Boolean> getWorkProcesses() {
        List<Boolean> res = new ArrayList<>();
        for (int i : judgement.getRewards().getPoints()) {
            if (i != 0) {
                res.add(true);
            } else {
                res.add(false);
            }
        }
        return res;
    }

    @Override
    public void onClick(View view) {
        Rider rider = riderBasicAdapter.getSelectedRider();
        if (rider == null) {
            return;
        }
        switch(view.getId()) {
            case R.id.RankOne:
                rankOne.setText(String.valueOf(rider.getStartNr()));
                saveJudgmnet(1, rider);
                break;
            case R.id.RankTwo:
                rankTwo.setText(String.valueOf(rider.getStartNr()));
                saveJudgmnet(2, rider);
                break;
            case R.id.RankThree:
                rankThree.setText(String.valueOf(rider.getStartNr()));
                saveJudgmnet(3, rider);
                break;
            case R.id.RankFour:
                rankFour.setText(String.valueOf(rider.getStartNr()));
                saveJudgmnet(4, rider);
                break;
            case R.id.RankFive:
                rankFive.setText(String.valueOf(rider.getStartNr()));
                saveJudgmnet(5, rider);
                break;
            default:
                break;
        }
        riderBasicAdapter.resetSelectedRider();
    }

    private void saveJudgmnet(int rank, Rider rider) {
        if (rider != null) {
            JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
            judgmentRiderConnection.setRank(rank);
            RealmList<Rider> riderToAdd = new RealmList<>();
            riderToAdd.add(rider);
            judgmentRiderConnection.setRider(riderToAdd);
            RealmList<Judgement> judgementToAdd = new RealmList<>();
            judgementToAdd.add(judgement);
            judgmentRiderConnection.setJudgements(judgementToAdd);
            JudgmentRiderConnectionPresenter.getInstance().addJudgmentRiderConnection(judgmentRiderConnection);
            updateRiderStateConnectionWithPerformance(rider, rank);
        }
        rewardM = null;
    }

    private void updateRiderStateConnectionWithPerformance(Rider rider, int rank) {
        rewardM = RewardPresenter.getInstance().getRewardReturnedByJudgment(judgement);
        RiderStageConnection riderStageConnection = new RiderStageConnection();
        riderStageConnection.setId(RiderStageConnectionPresenter.getInstance().getRiderByRank(rider.getRiderStages().first().getRank()).getId());
        switch(rewardM.getType()) {
            case TIME:
                riderStageConnection.setBonusTime(RiderStageConnectionUtilities.getPointsAtPosition(rank, rewardM));
                break;
            case POINTS:
                riderStageConnection.setBonusPoint(RiderStageConnectionUtilities.getPointsAtPosition(rank, rewardM));
                break;
        }
        riderStageConnection.setMoney(RiderStageConnectionUtilities.getMoneyAtPosition(rank, rewardM));
        RiderStageConnectionPresenter.getInstance().updateRiderStageConnectionReward(riderStageConnection);
    }
}
