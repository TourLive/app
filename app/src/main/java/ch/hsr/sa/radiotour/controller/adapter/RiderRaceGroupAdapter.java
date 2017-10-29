package ch.hsr.sa.radiotour.controller.adapter;

import android.content.ClipData;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.presentation.fragments.RiderRaceGroupFragment;
import ch.hsr.sa.radiotour.presentation.fragments.UnknownRiderDialogFragment;
import ch.hsr.sa.radiotour.presentation.fragments.UnknownRiderTransferDialogFramgent;
import io.realm.RealmList;

public class RiderRaceGroupAdapter extends  RecyclerView.Adapter<RiderRaceGroupAdapter.RiderRaceGroupViewHolder>{
    private RealmList<Rider> riders;
    private RealmList<Rider> selectedRider;
    private Fragment fragment;

    public RiderRaceGroupAdapter(RealmList<Rider> riders, Fragment fragment){
        this.riders = riders;
        this.selectedRider = new RealmList<>();
        this.fragment = fragment;
    }

    @Override
    public RiderRaceGroupAdapter.RiderRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider_in_racegroup, parent, false);
        return new RiderRaceGroupAdapter.RiderRaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderRaceGroupAdapter.RiderRaceGroupViewHolder holder, int position) {
        holder.racegroupRiderName.setText(String.valueOf(riders.get(position).getName()));
        holder.racegroupRiderStartNr.setText(String.valueOf(riders.get(position).getStartNr()));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public class RiderRaceGroupViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnDragListener, View.OnClickListener {
        private TextView racegroupRiderName;
        private TextView racegroupRiderStartNr;

        public RiderRaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroupRiderName = (TextView) itemView.findViewById(R.id.racegroup_rider_name);
            racegroupRiderStartNr = (TextView) itemView.findViewById(R.id.racegroup_rider_startnr);
            itemView.setOnLongClickListener(this);
            itemView.setOnDragListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            Rider r = riders.get(getLayoutPosition());
            selectedRider.add(r);
            ClipData data = ClipData.newPlainText(" ", " ");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, selectedRider, 0);
            return true;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            if (dragEvent.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                selectedRider.clear();
            }
            return true;
        }

        @Override
        public void onClick(View view) {
            FragmentManager fm = fragment.getFragmentManager();
            UnknownRiderTransferDialogFramgent alertDialog = UnknownRiderTransferDialogFramgent.newInstance();
            alertDialog.setTargetFragment(fragment, 300);
            alertDialog.show(fm, "");
        }
    }
}
