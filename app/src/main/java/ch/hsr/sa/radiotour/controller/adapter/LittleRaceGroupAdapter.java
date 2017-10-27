package ch.hsr.sa.radiotour.controller.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import android.content.Context;

import java.util.Collections;

import ch.hsr.sa.radiotour.business.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupComperator;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class LittleRaceGroupAdapter extends RecyclerView.Adapter<LittleRaceGroupAdapter.LittleRaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private Context context;
    private IRaceGroupPresenter raceGroupPresenter;

    private static final int NORMALITEM = 0;
    private static final int LASTITEM = 1;
    private OnStartDragListener onStartDragListener;

    public LittleRaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context, IRaceGroupPresenter raceGroupPresenter){
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComperator());
        this.context = context;
        this.raceGroupPresenter = raceGroupPresenter;
    }

    @Override
    public LittleRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup, parent, false);
        } else if (viewType == LASTITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup_last, parent, false);
        } else {
            return null;
        }
        return new LittleRaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LittleRaceGroupViewHolder holder, int position) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        final RecyclerView.ViewHolder temp = holder;
        holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
        holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount() - 1) {
            return NORMALITEM;
        } else {
            return LASTITEM;
        }
    }
    @Override
    public int getItemCount() {
        return raceGroups.size();
    }


    public class LittleRaceGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView racegroupName;
        private TextView racegroupCount;
        private TextView gaptimeActual;
        private TextView gaptimeBefore;
        private RecyclerView racegroupRiders;
        private View layout_racegroup;
        private View layout_addButton;

        public LittleRaceGroupViewHolder(View itemView) {
            super(itemView);
            layout_racegroup = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layout_addButton = (ConstraintLayout) itemView.findViewById(R.id.constraintLayout_AddButton);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupRiders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
            gaptimeActual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptimeBefore = (TextView) itemView.findViewById(R.id.gaptime_before);
        }
    }
}
