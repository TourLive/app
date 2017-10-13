package ch.hsr.sa.radiotour.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.Rider;
import io.realm.RealmList;

/**
 * Created by dgood on 10.10.2017.
 */

public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderViewHolder>{

    private RealmList<Rider> riders;

    public RiderAdapter(RealmList<Rider> riders) {
        this.riders = riders;
    }

    @Override
    public RiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider, parent, false);
        return new RiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderViewHolder holder, int position) {
        holder.tvNummer.setText(String.valueOf(riders.get(position).getStartNr()));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }


    public class RiderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
        }
    }

}
