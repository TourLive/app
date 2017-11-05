package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import io.realm.RealmList;

public class JudgmentRiderAdapter extends RecyclerView.Adapter<JudgmentRiderAdapter.JudgmentRiderViewHolder> {
    private Reward reward;
    private RealmList<JudgmentRiderConnection> judgmentRiderConnections;
    private Context context;

    public JudgmentRiderAdapter(Reward reward, RealmList<JudgmentRiderConnection> judgmentRiderConnections, Context context) {
        this.reward = reward;
        this.context = context;
        this.judgmentRiderConnections = judgmentRiderConnections;
    }

    @Override
    public JudgmentRiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider_judgment, parent, false);
        return new JudgmentRiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JudgmentRiderViewHolder holder, int position) {
        holder.itemRank.setText(String.valueOf(position + 1));
        holder.itemRiderName.setText(getRiderNameByRank(position + 1));
    }

    private String getRiderNameByRank(int rank) {
        for (JudgmentRiderConnection jRC : judgmentRiderConnections) {
            if (jRC.getRank() == rank) {
                return jRC.getRiders().getName();
            }
        }
        return context.getResources().getString(R.string.placeholder_rider_judgment);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (reward != null && reward instanceof Reward) {
            for (int i = 0; i < reward.getPoints().size(); i++) {
                if (reward.getPoints().get(i) != 0) {
                    size++;
                }
            }
        }
        return size;
    }

    public class JudgmentRiderViewHolder extends RecyclerView.ViewHolder {
        private TextView itemRank;
        private TextView itemRiderName;

        public JudgmentRiderViewHolder(View itemView) {
            super(itemView);
            itemRank = (TextView) itemView.findViewById(R.id.judgment_rank);
            itemRiderName = (TextView) itemView.findViewById(R.id.judgment_rider_name);
        }
    }

}