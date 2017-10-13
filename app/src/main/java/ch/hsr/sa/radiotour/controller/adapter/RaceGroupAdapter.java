package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import io.realm.RealmList;

/**
 * Created by Urs Forrer on 13.10.2017.
 */

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups){
        this.raceGroups = raceGroups;
    }

    @Override
    public RaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup, parent, false);
        return new RaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RaceGroupViewHolder holder, int position) {
        holder.racegroup_name.setText(String.valueOf(raceGroups.get(position).getName()));
        holder.gaptime_actual.setText(String.valueOf(raceGroups.get(position).getActualGapTime()));
        holder.gaptime_before.setText(String.valueOf(raceGroups.get(position).getHistoryGapTime()));
        holder.racegroup_count.setText(String.valueOf(raceGroups.get(position).getRidersCount()));

    }

    @Override
    public int getItemCount() {
        return raceGroups.size();
    }

    public class RaceGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView racegroup_name;
        private TextView racegroup_count;
        private TextView gaptime_actual;
        private TextView gaptime_before;

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroup_name = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroup_count = (TextView) itemView.findViewById(R.id.racegroup_count);
            gaptime_actual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptime_before = (TextView) itemView.findViewById(R.id.gaptime_before);
        }
    }
}
