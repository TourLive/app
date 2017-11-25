package ch.hsr.sa.radiotour.controller.adapter;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import io.realm.RealmResults;

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

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            layoutRacegroup = itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layoutAddButton = itemView.findViewById(R.id.constraintLayout_AddButton);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupName.setOnLongClickListener(this);
            racegroupRiders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
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
                        if(raceGroup.getType() == RaceGroupType.FELD){
                            if(newRiders.size() == 1 && newRiders.first().getTeamName().equals("UNKNOWN")){
                                RiderPresenter.getInstance().removeRider(newRiders.first());
                                RaceGroupPresenter.getInstance().getAllRaceGroups();
                                return true;
                            }
                            List<Rider> iteratorCopy = Realm.getInstance(RadioTourApplication.getInstance()).copyFromRealm(newRiders);
                            for(Rider r : iteratorCopy){
                                if(r.getTeamName().equals("UNKNOWN")){
                                    RiderPresenter.getInstance().removeRider(r);
                                    Log.d("removed", "rider");
                                }
                            }
                        }
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
                            RaceGroup raceGroup = new RaceGroup();
                            raceGroup.setPosition(raceGroupPos + 1);
                            raceGroup.setType(RaceGroupType.NORMAL);
                            raceGroup.setRiders(newRiders);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.time_picker_dialog, null);
            builder.setView(dialogView);

            RecyclerView rvMinutes = (RecyclerView) dialogView.findViewById(R.id.rvNumberPadMinutes);
            RecyclerView rvSeconds = (RecyclerView) dialogView.findViewById(R.id.rvNumberPadSeconds);

            GridLayoutManager layoutManagerMinutes = new GridLayoutManager(context, 8);
            GridLayoutManager layoutManagerSeconds = new GridLayoutManager(context, 8);

            final TimeAdapter adapterMinutes = new TimeAdapter();
            final TimeAdapter adapterSeconds = new TimeAdapter();
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
