package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentComperator;
import io.realm.RealmList;

public class JudgementAdapter extends RecyclerView.Adapter<JudgementAdapter.JudgementViewHolder> {
    private RealmList<Judgement> judgements;

    public JudgementAdapter(RealmList<Judgement> judgements) {
        this.judgements = judgements;
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
        holder.itemJudgementPlace.setText(String.valueOf(judgements.get(position).getDistance()));
        holder.itemJudgementKM.setText(String.valueOf(judgements.get(position).getDistance()));
    }

    @Override
    public int getItemCount() {
        return judgements.size();
    }

    public class JudgementViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTitleJudgement;
        private TextView itemJudgementPlace;
        private TextView itemJudgementKM;

        public JudgementViewHolder(View itemView) {
            super(itemView);
            itemTitleJudgement = (TextView) itemView.findViewById(R.id.itemTitleJudgement);
            itemJudgementPlace = (TextView) itemView.findViewById(R.id.itemJudgementPlace);
            itemJudgementKM = (TextView) itemView.findViewById(R.id.itemJudgementKM);
        }
    }

}
