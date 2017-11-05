package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentComperator;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import io.realm.RealmList;

public class JudgementAdapter extends RecyclerView.Adapter<JudgementAdapter.JudgementViewHolder> {
    private RealmList<Judgement> judgements;
    private Context context;

    public JudgementAdapter(RealmList<Judgement> judgements, Context context) {
        this.judgements = judgements;
        this.context = context;
        Collections.sort(judgements, new JudgmentComperator());
    }

    @Override
    public JudgementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_judgement, parent, false);
        return new JudgementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JudgementViewHolder holder, int position) {
        holder.itemTitleJudgement.setText(String.valueOf(judgements.get(position).getName()));
        holder.itemJudgementKM.setText(String.valueOf(judgements.get(position).getDistance()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        RealmList<JudgmentRiderConnection> judgmentRiderConnections = JudgmentRiderConnectionPresenter.getInstance().getJudgmentRiderConnectionsReturnedByJudgment(judgements.get(position));
        JudgmentRiderAdapter adapter = new JudgmentRiderAdapter(RewardPresenter.getInstance().getRewardReturnedByJudgment(judgements.get(position)), judgmentRiderConnections, context);
        holder.rvJudgmentRiders.setLayoutManager(layoutManager);
        holder.rvJudgmentRiders.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return judgements.size();
    }

    public class JudgementViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTitleJudgement;
        private TextView itemJudgementKM;
        private RecyclerView rvJudgmentRiders;

        public JudgementViewHolder(View itemView) {
            super(itemView);
            itemTitleJudgement = (TextView) itemView.findViewById(R.id.itemTitleJudgement);
            itemJudgementKM = (TextView) itemView.findViewById(R.id.itemJudgementKM);
            rvJudgmentRiders = (RecyclerView) itemView.findViewById(R.id.rvRiderJudgement);
        }
    }

}
