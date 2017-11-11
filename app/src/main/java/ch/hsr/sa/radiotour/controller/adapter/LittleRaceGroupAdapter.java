package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupComperator;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import io.realm.RealmList;

public class LittleRaceGroupAdapter extends RecyclerView.Adapter<LittleRaceGroupAdapter.LittleRaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private RiderRaceGroupFragment fragment;

    private static final int NORMALITEM = 0;
    private static final int FIRSTITEM = 1;

    public LittleRaceGroupAdapter(RealmList<RaceGroup> raceGroups, RiderRaceGroupFragment fragment){
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComperator());
        this.fragment = fragment;
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


    public class LittleRaceGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView racegroupName;
        private TextView racegroupCount;
        private View layoutRacegroup;
        private View layoutAddButton;
        private View layoutAddButtonTop;

        public LittleRaceGroupViewHolder(View itemView) {
            super(itemView);
            layoutRacegroup = itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layoutAddButton = itemView.findViewById(R.id.constraintLayout_AddButton);
            layoutAddButtonTop = itemView.findViewById(R.id.constraintLayout_AddButtonTop);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
            layoutRacegroup.setOnClickListener(this);
            layoutAddButton.setOnClickListener(this);
            if (layoutAddButtonTop != null) {
                layoutAddButtonTop.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.constraintLayout_RaceGroup:
                    fragment.onRaceGroupClicked(raceGroups.get(getAdapterPosition()), getAdapterPosition());
                    break;
                case R.id.constraintLayout_AddButtonTop:
                    fragment.onNewRaceGroupClicked(0, RaceGroupType.LEAD);
                    break;
                case R.id.constraintLayout_AddButton:
                    int pos = raceGroups.get(getAdapterPosition()).getPosition() + 1;
                    fragment.onNewRaceGroupClicked(pos, RaceGroupType.NORMAL);
                    break;
                default:
                    break;
            }
        }
    }
}
