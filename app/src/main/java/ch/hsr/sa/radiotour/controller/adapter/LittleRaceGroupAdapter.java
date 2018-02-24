package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupComparator;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.models.ButterKnifeViewHolder;
import io.realm.RealmList;

public class LittleRaceGroupAdapter extends RecyclerView.Adapter<LittleRaceGroupAdapter.LittleRaceGroupViewHolder> {
    private static final int NORMALITEM = 0;
    private static final int FIRSTITEM = 1;
    private RealmList<RaceGroup> raceGroups;
    private RiderRaceGroupFragment fragment;
    private Context context;

    public LittleRaceGroupAdapter(RealmList<RaceGroup> raceGroups, RiderRaceGroupFragment fragment) {
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComparator());
        this.fragment = fragment;
    }

    @Override
    public LittleRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
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
        if (raceGroups.get(position).getType() != RaceGroupType.FELD) {
            int color = ContextCompat.getColor(context, R.color.colorGrayLight);
            holder.layoutRacegroup.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        } else {

            int color = ContextCompat.getColor(context, R.color.colorGrayMiddle);
            holder.layoutRacegroup.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
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


    public class LittleRaceGroupViewHolder extends ButterKnifeViewHolder {
        @BindView(R.id.racegroup_name)
        TextView racegroupName;
        @BindView(R.id.racegroup_count)
        TextView racegroupCount;
        @BindView(R.id.constraintLayout_RaceGroup)
        View layoutRacegroup;
        @BindView(R.id.constraintLayout_AddButton)
        View layoutAddButton;
        @Nullable
        @BindView(R.id.constraintLayout_AddButtonTop)
        View layoutAddButtonTop;

        public LittleRaceGroupViewHolder(View itemView) {
            super(itemView);

        }

        @Optional @OnClick({R.id.constraintLayout_RaceGroup, R.id.constraintLayout_AddButtonTop, R.id.constraintLayout_AddButton})
        public void onLittleRaceGroupClick(View view) {
            switch (view.getId()) {
                case R.id.constraintLayout_RaceGroup:
                    fragment.onRaceGroupClicked(raceGroups.get(getAdapterPosition()), getAdapterPosition());
                    break;
                case R.id.constraintLayout_AddButtonTop:
                    RaceGroup beforeRaceGroup = new RaceGroup();
                    beforeRaceGroup.setPosition(-1);
                    fragment.onNewRaceGroupClicked(beforeRaceGroup, RaceGroupType.LEAD);
                    break;
                case R.id.constraintLayout_AddButton:
                    fragment.onNewRaceGroupClicked(raceGroups.get(getAdapterPosition()), RaceGroupType.NORMAL);
                    break;
                default:
                    break;
            }
        }
    }
}
