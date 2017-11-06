package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v4.app.Fragment;
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

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> implements ItemTouchHelperAdapter {
    private RealmList<RaceGroup> raceGroups;
    private Context context;
    private IRaceGroupPresenter raceGroupPresenter;
    private Fragment fragment;

    private static final int NORMALITEM = 0;
    private static final int LASTITEM = 1;
    private OnStartDragListener onStartDragListener;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context, IRaceGroupPresenter raceGroupPresenter, OnStartDragListener onStartDragListener, Fragment fragment){
        this.raceGroups = raceGroups;
        Collections.sort(raceGroups, new RaceGroupComperator());
        this.context = context;
        this.raceGroupPresenter = raceGroupPresenter;
        this.onStartDragListener = onStartDragListener;
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
        final RecyclerView.ViewHolder temp = holder;
        holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
        holder.gaptimeActual.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getActualGapTime())));
        holder.gaptimeBefore.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getHistoryGapTime())));
        if (raceGroups.get(position).getType() != RaceGroupType.FELD) {
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(raceGroups.get(position).getRiders(), fragment);
            holder.racegroupRiders.setLayoutManager(layoutManager);
            holder.racegroupRiders.setAdapter(adapter);
        }
        holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
        holder.racegroupName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(temp);
                }
                return false;
            }
        });
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

    @Override
    public boolean onItemMove(int from, int to) {
        Log.v("", "FROM" + from + "TO" + to);
        if (from < raceGroups.size() && to < raceGroups.size()) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(raceGroups, i, i + 1);
                }
            } else {
                for (int i = from; i > to; i--) {
                    Collections.swap(raceGroups, i, i - 1);
                }
            }
            for (RaceGroup rG : raceGroups) {
                raceGroupPresenter.updateRaceGroupPosition(rG, raceGroups.indexOf(rG));
            }
            notifyItemMoved(from, to);
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        raceGroups.remove(position);
        notifyItemRemoved(position);
    }

    public class RaceGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
        private TextView racegroupName;
        private TextView racegroupCount;
        private TextView gaptimeActual;
        private TextView gaptimeBefore;
        private RecyclerView racegroupRiders;
        private View layout_racegroup;
        private View layout_addButton;

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            layout_racegroup = itemView.findViewById(R.id.constraintLayout_RaceGroup);
            layout_addButton = itemView.findViewById(R.id.constraintLayout_AddButton);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupRiders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
            gaptimeActual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptimeBefore = (TextView) itemView.findViewById(R.id.gaptime_before);
            gaptimeActual.setOnClickListener(this);
            layout_racegroup.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch(dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            RaceGroup raceGroup = raceGroups.get(getAdapterPosition());
                            RealmList<Rider> newRiders = (RealmList<Rider>) dragEvent.getLocalState();
                            raceGroupPresenter.updateRaceGroupRiders(raceGroup, newRiders);
                            notifyItemChanged(getAdapterPosition());
                            return true;
                        default:
                            return true;
                    }
                }
            });
            if (layout_addButton != null) {
                layout_addButton.setOnDragListener(new View.OnDragListener() {
                    @Override
                    public boolean onDrag(View view, DragEvent dragEvent) {
                        switch(dragEvent.getAction()) {
                            case DragEvent.ACTION_DRAG_STARTED:
                                return true;
                            case DragEvent.ACTION_DROP:
                                RealmList<Rider> newRiders = (RealmList<Rider>) dragEvent.getLocalState();
                                int raceGroup_pos = raceGroups.get(getAdapterPosition()).getPosition();
                                RaceGroup raceGroup = new RaceGroup();
                                raceGroup.setPosition(raceGroup_pos + 1);
                                raceGroup.setType(RaceGroupType.NORMAL);
                                raceGroup.setRiders(newRiders);
                                raceGroupPresenter.addRaceGroup(raceGroup);
                                notifyDataSetChanged();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
            }
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
            builder.setPositiveButton("Change time", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (adapterMinutes.getSelectedNumber() != null && adapterSeconds.getSelectedNumber() != null) {
                        raceGroupPresenter.updateRaceGroupGapTime(raceGroups.get(getAdapterPosition()), adapterMinutes.getSelectedNumber(), adapterSeconds.getSelectedNumber());
                    }
               }
            });
            builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Do nothing
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.BLUE);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.GREEN);
        }
    }
}
