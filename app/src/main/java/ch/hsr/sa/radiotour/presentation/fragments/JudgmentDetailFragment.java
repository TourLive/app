package ch.hsr.sa.radiotour.presentation.fragments;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
    private TextView rankSix;
    private TextView rankSeven;
    private TextView rankEight;
    private TextView rankNine;
    private TextView rankTen;
    private TextView rankOneTxt;
    private TextView rankTwoTxt;
    private TextView rankThreeTxt;
    private TextView rankFourTxt;
    private TextView rankFiveTxt;
    private TextView rankSixTxt;
    private TextView rankSevenTxt;
    private TextView rankEightTxt;
    private TextView rankNineTxt;
    private TextView rankTenTxt;
    private View selectedView;
    private List<TextView> textViews = new ArrayList<>();
    private List<TextView> headers = new ArrayList<>();
    private ConstraintLayout lineTwoHeader;
    private ConstraintLayout lineTwoButtons;
    private Reward rewardM;
    private int rank = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG", "JudgmentDetailFragment onCreateView");
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
        lineTwoHeader = (ConstraintLayout) root.findViewById(R.id.constraintLayoutTxtTwo);
        lineTwoButtons = (ConstraintLayout) root.findViewById(R.id.constraintLayoutTwo);
        initRankView(root);
        selectedView = null;

        return root;
    }

    private void initRankView(View root) {
        rankOne = (TextView) root.findViewById(R.id.RankOne);
        rankTwo = (TextView) root.findViewById(R.id.RankTwo);
        rankThree = (TextView) root.findViewById(R.id.RankThree);
        rankFour = (TextView) root.findViewById(R.id.RankFour);
        rankFive = (TextView) root.findViewById(R.id.RankFive);
        rankSix = (TextView) root.findViewById(R.id.RankSix);
        rankSeven = (TextView) root.findViewById(R.id.RankSeven);
        rankEight = (TextView) root.findViewById(R.id.RankEight);
        rankNine = (TextView) root.findViewById(R.id.RankNine);
        rankTen = (TextView) root.findViewById(R.id.RankTen);
        rankOneTxt = (TextView) root.findViewById(R.id.txtRankOne);
        rankTwoTxt = (TextView) root.findViewById(R.id.txtRankTwo);
        rankThreeTxt = (TextView) root.findViewById(R.id.txtRankThree);
        rankFourTxt = (TextView) root.findViewById(R.id.txtRankFour);
        rankFiveTxt = (TextView) root.findViewById(R.id.txtRankFive);
        rankSixTxt = (TextView) root.findViewById(R.id.txtRankSix);
        rankSevenTxt = (TextView) root.findViewById(R.id.txtRankSeven);
        rankEightTxt = (TextView) root.findViewById(R.id.txtRankEight);
        rankNineTxt = (TextView) root.findViewById(R.id.txtRankNine);
        rankTenTxt = (TextView) root.findViewById(R.id.txtRankTen);
        initListener();
        iniList();

        List<Boolean> work = getWorkProcesses();
        for (int o = 1; o < work.size(); o++) {
            if (!work.get(o)) {
                textViews.get(o).setVisibility(View.GONE);
                headers.get(o).setVisibility(View.GONE);
            }
        }
        if (work.size() < 6) {
            lineTwoButtons.setVisibility(View.GONE);
            lineTwoHeader.setVisibility(View.GONE);
        }
        initTextViewsWithData();
    }

    private void iniList() {
        textViews.add(rankOne);
        textViews.add(rankTwo);
        textViews.add(rankThree);
        textViews.add(rankFour);
        textViews.add(rankFive);
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
        headers.add(rankSixTxt);
        headers.add(rankSevenTxt);
        headers.add(rankEightTxt);
        headers.add(rankNineTxt);
        headers.add(rankTenTxt);
        Reward reward = judgement.getRewards();
        for(int i = 0; i < reward.getPoints().size(); i++){
            headers.get(i).setText(headers.get(i).getText() + getResources().getString(R.string.judgment_points) + reward.getPoints().get(i));
        }
    }

    private void initListener() {
        rankOne.setOnClickListener(this);
        rankTwo.setOnClickListener(this);
        rankThree.setOnClickListener(this);
        rankFour.setOnClickListener(this);
        rankFive.setOnClickListener(this);
        rankSix.setOnClickListener(this);
        rankSeven.setOnClickListener(this);
        rankEight.setOnClickListener(this);
        rankNine.setOnClickListener(this);
        rankTen.setOnClickListener(this);
    }

    private void initTextViewsWithData() {

        for (JudgmentRiderConnection jRc : JudgmentRiderConnectionPresenter.getInstance().getJudgmentRiderConnectionsReturnedByJudgment(judgement)) {
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
                case 6:
                    rankSix.setText(String.valueOf(startNr));
                    break;
                case 7:
                    rankSeven.setText(String.valueOf(startNr));
                    break;
                case 8:
                    rankEight.setText(String.valueOf(startNr));
                    break;
                case 9:
                    rankNine.setText(String.valueOf(startNr));
                    break;
                case 10:
                    rankTen.setText(String.valueOf(startNr));
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
        if(selectedView != null)
            selectedView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape));
        selectedView = view;
        selectedView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.background_shape_active));
        switch (view.getId()) {
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
            case R.id.RankSix:
                rank = 6;
                break;
            case R.id.RankSeven:
                rank = 7;
                break;
            case R.id.RankEight:
                rank = 8;
                break;
            case R.id.RankNine:
                rank = 9;
                break;
            case R.id.RankTen:
                rank = 10;
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
            riderBasicAdapter.setColorOnRider(rider.getStartNr());
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
