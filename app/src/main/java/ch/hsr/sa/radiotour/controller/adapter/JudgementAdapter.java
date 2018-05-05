package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.JudgmentRiderConnectionPresenter;
import ch.hsr.sa.radiotour.business.presenter.RewardPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentComperator;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.presentation.fragments.OnJudgmentClickListener;
import ch.hsr.sa.radiotour.presentation.models.ButterKnifeViewHolder;
import io.realm.RealmList;

public class JudgementAdapter extends RecyclerView.Adapter<JudgementAdapter.JudgementViewHolder> {
    private RealmList<Judgement> judgements;
    private Context context;
    private OnJudgmentClickListener onJudgmentClickListener;
    private TextView activeJudgment = null;
    private int activePosition = 0;

    public JudgementAdapter(RealmList<Judgement> judgements, Context context, OnJudgmentClickListener onJudgmentClickListener) {
        this.judgements = judgements;
        this.context = context;
        this.onJudgmentClickListener = onJudgmentClickListener;
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
        holder.itemJudgementKM.setText("KM " + judgements.get(position).getDistance());
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

    public class JudgementViewHolder extends ButterKnifeViewHolder {
        @BindView(R.id.itemTitleJudgement)
        TextView itemTitleJudgement;
        @BindView(R.id.itemJudgementKM)
        TextView itemJudgementKM;
        @BindView(R.id.rvRiderJudgement)
        RecyclerView rvJudgmentRiders;

        public JudgementViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.itemJudgementKM)
        public void onClick() {
            if(activeJudgment != null){
                activeJudgment.setBackground(ContextCompat.getDrawable(context, R.drawable.background_shape_judgment));
            }
            activeJudgment = itemJudgementKM;
            activePosition = getAdapterPosition();
            onJudgmentClickListener.onJudgmentClicked(judgements.get(getAdapterPosition()));
            itemJudgementKM.setBackground(ContextCompat.getDrawable(context, R.drawable.background_shape_judgment_active));
        }
    }

}
