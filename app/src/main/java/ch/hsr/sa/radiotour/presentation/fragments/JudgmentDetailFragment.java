package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import ch.hsr.sa.radiotour.dataaccess.models.RewardType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public class JudgmentDetailFragment extends Fragment implements View.OnClickListener, OnRiderJudgmentClickListener {
    private String judgementID;
    private Judgement judgement;
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
    private List<TextView> textViews = new ArrayList<>();
    private List<TextView> headers = new ArrayList<>();
    private Reward rewardM;
    private int rank = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_detail_judgment, container, false);
        judgementID = getArguments().getString("id");
        judgement = JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(judgementID);

        TextView title = (TextView) root.findViewById(R.id.titleJudgements2);
        title.setText("KM " + judgement.getDistance() + " | " + judgement.getName());


        RecyclerView rvRidersToSelect = (RecyclerView) root.findViewById(R.id.rvRidersToSelect);
        riderBasicAdapter = new RiderBasicAdapter(RiderPresenter.getInstance().getAllRidersReturned(), judgement, this);
        rvRidersToSelect.setAdapter(riderBasicAdapter);
        rvRidersToSelect.setLayoutManager(new GridLayoutManager(this.getContext(), 8, LinearLayoutManager.HORIZONTAL, false));
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
            int startNr = jRc.getRider().first().getStartNr();
            switch (jRc.getRank()) {
                case 1:
                    rankOne.setText(String.valueOf(startNr));
                    break;
                case 2:
                    rankTwo.setText(String.valueOf(startNr));
                    break;
                case 3:
                    rankThree.setText(String.valueOf(startNr));
                    break;
                case 4:
                    rankFour.setText(String.valueOf(startNr));
                    break;
                case 5:
                    rankFive.setText(String.valueOf(startNr));
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
       switch(view.getId()) {
            case R.id.RankOne:
                rank = 1;
                break;
            case R.id.RankTwo:
                rank = 2;
                break;
            case R.id.RankThree:
                rank = 3;
                break;
            case R.id.RankFour:
                rank = 4;
                break;
            case R.id.RankFive:
                rank = 5;
                break;
            default:
                break;
       }
    }

    public void saveJudgmnet() {
        Rider rider = riderBasicAdapter.getSelectedRider();
        if (rank != 0) {
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
            textViews.get(rank - 1).setText(String.valueOf(rider.getStartNr()));
        } else {
            Toast toast = Toast.makeText(getContext(), getResources().getString(R.string.judgment_text), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
            toast.show();
            Integer integer = rider.getRiderID();
            riderBasicAdapter.removeRiderFromList(integer);
        }
        riderBasicAdapter.resetSelectedRider();
        rank = 0;
    }

    private void updateRiderStateConnectionWithPerformance(Rider rider, int rank) {
        int r = rider.getStartNr();
        new Thread(() -> {
            Judgement judgement = JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(judgementID);
            rewardM = RewardPresenter.getInstance().getRewardReturnedByJudgment(judgement);
            RiderStageConnection riderStageConnection = new RiderStageConnection();
            riderStageConnection.setId(RiderPresenter.getInstance().getRiderByStartNr(r).getRiderStages().first().getId());
            if (rewardM.getType() == RewardType.TIME) {
                riderStageConnection.setBonusTime(RiderStageConnectionUtilities.getPointsAtPosition(rank, rewardM));
            } else if (rewardM.getType() == RewardType.POINTS) {
                int points = RiderStageConnectionUtilities.getPointsAtPosition(rank, rewardM);
                if (judgement.getName().toLowerCase().contains("sprint")) {
                    riderStageConnection.setSprintBonusPoints(points);
                } else if (judgement.getName().toLowerCase().contains("bergpreis")) {
                    riderStageConnection.setMountainBonusPoints(points);
                } else {
                    riderStageConnection.setBonusPoint(points);
                }
            } else {
                Log.d(JudgmentDetailFragment.class.getSimpleName(), "APP - CALCULATE - UNSUPPORTED TYPE");
            }
            riderStageConnection.setMoney(RiderStageConnectionUtilities.getMoneyAtPosition(rank, rewardM));
            RiderStageConnectionPresenter.getInstance().updateRiderStageConnectionReward(riderStageConnection);
        }).start();

    }
}
