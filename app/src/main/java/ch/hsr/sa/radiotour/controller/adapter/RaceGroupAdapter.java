package ch.hsr.sa.radiotour.controller.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
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

import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> implements ItemTouchHelperAdapter {
    private RealmList<RaceGroup> raceGroups;
    private Context context;
    private IRaceGroupPresenter raceGroupPresenter;

    private final static int ITEM = 0;
    private final static int ADDBUTTON = 1;
    private OnStartDragListener onStartDragListener;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context, IRaceGroupPresenter raceGroupPresenter, OnStartDragListener onStartDragListener){
        this.raceGroups = raceGroups;
        this.context = context;
        this.raceGroupPresenter = raceGroupPresenter;
        this.onStartDragListener = onStartDragListener;
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
            final RecyclerView.ViewHolder temp = (RecyclerView.ViewHolder) holder;
            holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
            holder.gaptimeActual.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getActualGapTime())));
            holder.gaptimeBefore.setText(String.valueOf(convertLongToTimeString(raceGroups.get(position).getHistoryGapTime())));
            holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(raceGroups.get(position).getRiders());
            holder.racegroupRiders.setLayoutManager(layoutManager);
            holder.racegroupRiders.setAdapter(adapter);
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        onStartDragListener.onStartDrag(temp);
                    }
                    return false;
                }
            });
        }
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

        public RaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroupName = (TextView) itemView.findViewById(R.id.racegroup_name);
            racegroupRiders = (RecyclerView) itemView.findViewById(R.id.racegroup_riders);
            racegroupCount = (TextView) itemView.findViewById(R.id.racegroup_count);
            gaptimeActual = (TextView) itemView.findViewById(R.id.gaptime_actual);
            gaptimeBefore = (TextView) itemView.findViewById(R.id.gaptime_before);
            gaptimeActual.setOnClickListener(this);
            itemView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View view, DragEvent dragEvent) {
                    switch(dragEvent.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            return true;
                        case DragEvent.ACTION_DROP:
                            RaceGroup raceGroup = raceGroups.get(getAdapterPosition());
                            RealmList<Rider> newRiders = (RealmList<Rider>) dragEvent.getLocalState();
                            System.out.print(newRiders.toString());
                            raceGroupPresenter.updateRaceGroupRiders(raceGroup, newRiders);
                            notifyItemChanged(getAdapterPosition());
                            return true;
                        default:
                            return true;
                    }
                }
            });
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

            final TimeAdapter adapterMinutes = new TimeAdapter(context);
            final TimeAdapter adapterSeconds = new TimeAdapter(context);
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
