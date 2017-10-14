package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

/**
 * Created by Urs Forrer on 14.10.2017.
 */

public class RiderRaceGroupAdapter extends  RecyclerView.Adapter<RiderRaceGroupAdapter.RiderRaceGroupViewHolder>{
    private RealmList<Rider> riders;

    public RiderRaceGroupAdapter(RealmList<Rider> riders){
        this.riders = riders;
    }

    @Override
    public RiderRaceGroupAdapter.RiderRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider_in_racegroup, parent, false);
        return new RiderRaceGroupAdapter.RiderRaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderRaceGroupAdapter.RiderRaceGroupViewHolder holder, int position) {
        holder.racegroup_rider_name.setText(String.valueOf(riders.get(position).getName()));
        holder.racegroup_rider_startnr.setText(String.valueOf(riders.get(position).getStartNr()));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public class RiderRaceGroupViewHolder extends RecyclerView.ViewHolder {
        private TextView racegroup_rider_name;
        private TextView racegroup_rider_startnr;

        public RiderRaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroup_rider_name = (TextView) itemView.findViewById(R.id.racegroup_rider_name);
            racegroup_rider_startnr = (TextView) itemView.findViewById(R.id.racegroup_rider_startnr);

        }
    }
}
