package ch.hsr.sa.radiotour.controller.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import android.content.Context;

import java.util.Collections;

import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupComperator;
import io.realm.RealmList;

public class LittleRaceGroupAdapter extends RecyclerView.Adapter<LittleRaceGroupAdapter.LittleRaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private Context context;

    private static final int NORMALITEM = 0;
    private static final int FIRSTITEM = 1;

    public LittleRaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context){
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComperator());
        this.context = context;
    }

    @Override
    public LittleRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_little_racegroup, parent, false);
        } else if (viewType == FIRSTITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_little_racegroup_first, parent, false);
        } else {
            return null;
        }
        return new LittleRaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LittleRaceGroupViewHolder holder, int position) {
        holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
        holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FIRSTITEM;
        } else {
            return NORMALITEM;
        }

    }
    @Override
    public int getItemCount() {
        return raceGroups.size();
    }


    public class LittleRaceGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView racegroupName;
        private TextView racegroupCount;
        private View layout_racegroup;
        private View layout_addButton;

        public LittleRaceGroupViewHolder(View itemView) {
            super(itemView);
            layout_racegroup = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layout_addButton = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_AddButton);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
        }
    }
}
