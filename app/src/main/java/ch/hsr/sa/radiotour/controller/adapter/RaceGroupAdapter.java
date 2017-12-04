package ch.hsr.sa.radiotour.controller.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.dataaccess.RadioTourApplication;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupComperator;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.Realm;
import io.realm.RealmList;

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private Context context;
    private Fragment fragment;
    private static final int NORMALITEM = 0;
    private static final int LASTITEM = 1;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context, Fragment fragment){
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComperator());
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public RaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == NORMALITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup, parent, false);
        } else if (viewType == LASTITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_racegroup_last, parent, false);
        } else {
            return null;
        }
        return new RaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RaceGroupViewHolder holder, int position) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
        holder.gaptimeActual.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getActualGapTime())));
        holder.gaptimeBefore.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getHistoryGapTime())));
        if (raceGroups.get(position).getType() != RaceGroupType.FELD) {
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(raceGroups.get(position).getRiders(), fragment);
            holder.racegroupRiders.setLayoutManager(layoutManager);
            holder.racegroupRiders.setAdapter(adapter);
            if (raceGroups.get(position).getType() == RaceGroupType.LEAD) {
                holder.gaptimeActual.setBackground(context.getDrawable(R.drawable.background_shape_racetime_before));
            }
        } else {
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(new RealmList<Rider>(), fragment);
            holder.racegroupRiders.setLayoutManager(layoutManager);
            holder.racegroupRiders.setAdapter(adapter);
        }
        holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
    }

    public String convertLongToTimeString(long time) {
        long resMinutes = time / 60;
        long resSeconds =  time - (resMinutes * 60);
        if (resMinutes < 10 && resSeconds < 10) {
            return "0" + Long.toString(resMinutes) + ":0" + Long.toString(resSeconds);
        } else if (resMinutes < 10 && resMinutes >= 10) {
            return "0" + Long.toString(resMinutes) + ":" + Long.toString(resSeconds);
        } else if (resMinutes > 10 && resMinutes >= 10) {
            return Long.toString(resMinutes) + ":0" + Long.toString(resSeconds);
        } else {
            return Long.toString(resMinutes) + ":" + Long.toString(resSeconds);
        }
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


    public class RaceGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView racegroupName;
        private TextView racegroupCount;
        private TextView gaptimeActual;
        private TextView gaptimeBefore;
        private RecyclerView racegroupRiders;
        private View layoutRacegroup;
        private View layoutAddButton;
        private ImageView icDragAndDrop;
        private RaceGroup actualRaceGroup;

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            layoutRacegroup = itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layoutAddButton = itemView.findViewById(R.id.constraintLayout_AddButton);
            icDragAndDrop = (ImageView) itemView.findViewById(R.id.icDragAndDrop);
            if (icDragAndDrop != null) {
                icDragAndDrop.setOnLongClickListener(this);
            }
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupName.setOnLongClickListener(this);
            racegroupRiders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
            racegroupCount.setOnLongClickListener(this);
            gaptimeActual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptimeBefore = (TextView) itemView.findViewById(R.id.gaptime_before);
            gaptimeActual.setOnClickListener(this);
            layoutRacegroup.setOnDragListener((View view, DragEvent dragEvent) -> {
                switch(dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DROP:
                        RaceGroup raceGroup = raceGroups.get(getAdapterPosition());
                        RealmList<Rider> newRiders = (RealmList<Rider>) dragEvent.getLocalState();
                        if (newRiders.equals(raceGroup.getRiders()))
                            return true;
                        RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, newRiders);
                        notifyItemChanged(getAdapterPosition());
                        return true;
                    default:
                        return true;
                }
            });
            if (layoutAddButton != null) {
                layoutAddButton.setOnDragListener((View view, DragEvent dragEvent) -> {
                    switch(dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            RealmList<Rider> newRiders = (RealmList<Rider>) dragEvent.getLocalState();
                            int raceGroupPos = raceGroups.get(getAdapterPosition()).getPosition();
                            RaceGroup beforeRaceGroup = raceGroups.get(getAdapterPosition());
                            RaceGroup raceGroup = new RaceGroup();
                            raceGroup.setPosition(raceGroupPos + 1);
                            raceGroup.setType(RaceGroupType.NORMAL);
                            raceGroup.setRiders(newRiders);
                            raceGroup.setActualGapTime(beforeRaceGroup.getActualGapTime() + 1);
                            RaceGroupPresenter.getInstance().addRaceGroup(raceGroup);
                            notifyDataSetChanged();
                            return true;
                        default:
                            return true;
                    }
                });
            }
        }

        @Override
        public boolean onLongClick(View view) {
            RaceGroup raceGroup = raceGroups.get(getLayoutPosition());
            if (raceGroup.getType() == RaceGroupType.FELD)
                return true;
            ClipData data = ClipData.newPlainText(" ", " ");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, raceGroup.getRiders(), 0);
            return true;
        }


        @Override
        public void onClick(View view) {
            if (raceGroups.get(getAdapterPosition()).getType() == RaceGroupType.LEAD) {
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.time_picker_dialog, null);
            builder.setView(dialogView);

            RecyclerView rvMinutes = (RecyclerView) dialogView.findViewById(R.id.rvNumberPadMinutes);
            RecyclerView rvSeconds = (RecyclerView) dialogView.findViewById(R.id.rvNumberPadSeconds);

            GridLayoutManager layoutManagerMinutes = new GridLayoutManager(context, 8, LinearLayoutManager.HORIZONTAL, false);
            GridLayoutManager layoutManagerSeconds = new GridLayoutManager(context, 8, LinearLayoutManager.HORIZONTAL, false);

            final TimeAdapter adapterMinutes;
            final TimeAdapter adapterSeconds;

            final int adapterPosition = getAdapterPosition();
            int pos = 0;
            if (getAdapterPosition() > 0) {
                pos = adapterPosition - 1;
            }
            RaceGroup raceGroup = raceGroups.get(pos);
            int actualMinutes =  (int) raceGroup.getActualGapTime() / 60;
            int actualSeconds = (int) raceGroup.getActualGapTime() - (actualMinutes * 60);

            adapterMinutes = new TimeAdapter(actualMinutes);
            adapterSeconds = new TimeAdapter(actualSeconds);

            rvMinutes.setLayoutManager(layoutManagerMinutes);
            rvMinutes.setAdapter(adapterMinutes);
            rvSeconds.setLayoutManager(layoutManagerSeconds);
            rvSeconds.setAdapter(adapterSeconds);

            builder.setTitle("Time Gap");
            builder.setMessage("Please enter the gap time relative to the leading group");
            builder.setPositiveButton("Change time", (DialogInterface dialogInterface, int i) -> {
                if (adapterMinutes.getSelectedNumber() != null && adapterSeconds.getSelectedNumber() != null) {
                    RaceGroupPresenter.getInstance().updateRaceGroupGapTime(raceGroups.get(getAdapterPosition()), adapterMinutes.getSelectedNumber(), adapterSeconds.getSelectedNumber());
                }
            });
            builder.setNegativeButton("Dismiss", (DialogInterface dialogInterface, int i) -> dialogInterface.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
}
