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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.adapter.RiderBasicAdapter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;

public class JudgmentDetailFragment extends Fragment {
    private String id;
    private RecyclerView rvRidersToSelect;
    private TextView RankOne;
    private TextView RankTwo;
    private TextView RankThree;
    private TextView RankFour;
    private TextView RankFive;
    private List<TextView> textViews = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TAG","JudgmentDetailFragment onCreateView");
        View root = inflater.inflate(R.layout.fragment_detail_judgment, container, false);
        id = getArguments().getString("id");
        rvRidersToSelect = (RecyclerView) root.findViewById(R.id.rvRidersToSelect);
        RiderBasicAdapter riderBasicAdapter = new RiderBasicAdapter(RiderPresenter.getInstance().getAllRidersReturned());
        rvRidersToSelect.setAdapter(riderBasicAdapter);
        rvRidersToSelect.setLayoutManager(new GridLayoutManager(getContext(), 12));
        rvRidersToSelect.setHasFixedSize(true);
        initRankView(root);
        return root;
    }

    private void initRankView(View root) {
        RankOne = (TextView) root.findViewById(R.id.RankOne);
        RankTwo = (TextView) root.findViewById(R.id.RankTwo);
        RankThree = (TextView) root.findViewById(R.id.RankThree);
        RankFour = (TextView) root.findViewById(R.id.RankFour);
        RankFive = (TextView) root.findViewById(R.id.RankFive);
        textViews.add(RankOne);
        textViews.add(RankTwo);
        textViews.add(RankThree);
        textViews.add(RankFour);
        textViews.add(RankFive);

        List<Boolean> work = getWorkProcesses();
        for (int o = 1; o < work.size(); o++) {
            if (!work.get(o)) {
                textViews.get(o).setVisibility(View.GONE);
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
}
