package ch.hsr.sa.radiotour.presentation.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderBasicAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class JudgmentDetailFragment extends Fragment implements View.OnClickListener {
    private String id;
    private RecyclerView rvRidersToSelect;
    private RiderBasicAdapter riderBasicAdapter;
    private TextView rankOne;
    private TextView rankTwo;
    private TextView rankThree;
    private TextView rankFour;
    private TextView rankFive;
    private List<TextView> textViews = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_detail_judgment, container, false);
        id = getArguments().getString("id");
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

        initListener();

        textViews.add(rankOne);
        textViews.add(rankTwo);
        textViews.add(rankThree);
        textViews.add(rankFour);
        textViews.add(rankFive);

        List<Boolean> work = getWorkProcesses();
        for (int o = 1; o < work.size(); o++) {
            if (!work.get(o)) {
                textViews.get(o).setVisibility(View.GONE);
            }
        }

        initTextViewsWithData();
    }

    private void initListener() {
        rankOne.setOnClickListener(this);
        rankTwo.setOnClickListener(this);
        rankThree.setOnClickListener(this);
        rankFour.setOnClickListener(this);
        rankFive.setOnClickListener(this);
    }

    private void initTextViewsWithData() {

        for (JudgmentRiderConnection jRc : JudgmentRiderConnectionPresenter.getInstance().getJudgmentRiderConnectionsReturnedByJudgment(JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(id))){
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
        Judgement judgement = JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(id);
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
        JudgmentRiderConnection judgmentRiderConnection = new JudgmentRiderConnection();
        judgmentRiderConnection.setRank(rank);
        RealmList<Rider> riderToAdd = new RealmList<>();
        riderToAdd.add(rider);
        judgmentRiderConnection.setRider(riderToAdd);
        RealmList<Judgement> judgementToAdd = new RealmList<>();
        judgementToAdd.add(JudgmentPresenter.getInstance().getJudgmentByObjectIdReturned(id));
        judgmentRiderConnection.setJudgements(judgementToAdd);
        JudgmentRiderConnectionPresenter.getInstance().addJudgmentRiderConnection(judgmentRiderConnection);
    }
}
