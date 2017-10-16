package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import android.content.Context;

import ch.hsr.sa.radiotour.controller.adapter.presenter.interfaces.IRaceGroupPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class RaceGroupAdapter extends RecyclerView.Adapter<RaceGroupAdapter.RaceGroupViewHolder> {
    private RealmList<RaceGroup> raceGroups;
    private Context context;
    private IRaceGroupPresenter raceGroupPresenter;

    private final static int ITEM = 0;
    private final static int ADDBUTTON = 1;

    public RaceGroupAdapter(RealmList<RaceGroup> raceGroups, Context context, IRaceGroupPresenter raceGroupPresenter){
        this.raceGroups = raceGroups;
        this.context = context;
        this.raceGroupPresenter = raceGroupPresenter;
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
            holder.racegroupName.setText(String.valueOf(raceGroups.get(position).getName()));
            holder.gaptimeActual.setText(String.valueOf(raceGroups.get(position).getActualGapTime()));
            holder.gaptimeBefore.setText("(" + String.valueOf(raceGroups.get(position).getHistoryGapTime()) + ")");
            holder.racegroupCount.setText(String.valueOf(raceGroups.get(position).getRidersCount()));
            RiderRaceGroupAdapter adapter = new RiderRaceGroupAdapter(raceGroups.get(position).getRiders());
            holder.racegroupRiders.setLayoutManager(layoutManager);
            holder.racegroupRiders.setAdapter(adapter);
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
            itemView.setOnDragListener(new View.OnDragListener() {
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
        }


    }
}
