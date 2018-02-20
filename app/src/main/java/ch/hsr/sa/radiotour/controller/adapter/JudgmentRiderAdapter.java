package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Reward;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
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
        Rider r = getRiderByRank(position + 1);
        holder.itemRank.setText(String.valueOf(position + 1) + ".");
        if (r == null) {
            holder.itemRiderInfo.setText(context.getResources().getString(R.string.placeholder_rider_judgment));
            holder.itemRiderStartNr.setText("");
        } else {
            holder.itemRiderInfo.setText(r.getName() + r.getTeamShortName());
            holder.itemRiderStartNr.setText(Integer.toString(r.getStartNr()));
            holder.imgCountry.setImageResource(UIUtilitis.getCountryFlag(r.getCountry()));
        }

    }

    private Rider getRiderByRank(int rank) {
        for (JudgmentRiderConnection jRC : judgmentRiderConnections) {
            if (jRC.getRank() == rank) {
                return jRC.getRider().first();
            }
        }
        return null;
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
        private TextView itemRiderInfo;
        private ImageView imgCountry;
        private TextView itemRiderStartNr;

        public JudgmentRiderViewHolder(View itemView) {
            super(itemView);
            itemRank = (TextView) itemView.findViewById(R.id.judgment_rank);
            itemRiderInfo = (TextView) itemView.findViewById(R.id.judgment_rider_info);
            imgCountry = (ImageView) itemView.findViewById(R.id.imgCountry);
            itemRiderStartNr = (TextView) itemView.findViewById(R.id.judgment_rider_startnr);

        }
    }

}