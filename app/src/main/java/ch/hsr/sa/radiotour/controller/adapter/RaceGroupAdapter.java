package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import android.content.Context;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import io.realm.RealmList;

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private Context context;

    private final int ITEM = 0;
    private final int ADDBUTTON = 1;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context){
        this.raceGroups = raceGroups;
        this.context = context;
    }

    @Override
    public RaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup, parent, false);
        } else if (viewType == ADDBUTTON) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup_add, parent, false);
        } else {
            return null;
        }
        return new RaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RaceGroupViewHolder holder, int position) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        if (position >= getItemCount()) {

        } else {
            holder.racegroup_name.setText(String.valueOf(raceGroups.get(position).getName()));
            holder.gaptime_actual.setText(String.valueOf(raceGroups.get(position).getActualGapTime()));
            holder.gaptime_before.setText("(" + String.valueOf(raceGroups.get(position).getHistoryGapTime()) + ")");
            holder.racegroup_count.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(raceGroups.get(position).getRiders());
            holder.racegroup_riders.setLayoutManager(layoutManager);
            holder.racegroup_riders.setAdapter(adapter);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount()) {
            return ITEM;
        } else {
            return ADDBUTTON;
        }
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
        private RecyclerView racegroup_riders;

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroup_name = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroup_riders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroup_count = (TextView) itemView.findViewById(R.id.racegroup_count);
            gaptime_actual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptime_before = (TextView) itemView.findViewById(R.id.gaptime_before);
        }
    }
}
