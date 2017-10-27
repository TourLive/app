package ch.hsr.sa.radiotour.controller.adapter;
import android.content.ClipData;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RiderListAdapter extends RecyclerView.Adapter<RiderListAdapter.RiderViewHolder>{

    private RealmList<Rider> riders;
    private android.content.Context context;

    public RiderListAdapter(RealmList<Rider> riders) {
        this.riders = riders;
    }

    @Override
    public RiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider, parent, false);
        RiderViewHolder holder = new RiderViewHolder(view);
        context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(RiderViewHolder holder, int position) {
        holder.tvNummer.setText(String.valueOf(riders.get(position).getStartNr()));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    private int getColorFromState(RiderStateType stateType){
        int color;
        switch (stateType){
            case DOCTOR:
                color = context.getResources().getColor(R.color.colorRed, null);
                break;
            case DROP:
                color = context.getResources().getColor(R.color.colorYellow, null);
                break;
            case DEFECT:
                color = context.getResources().getColor(R.color.colorBlue, null);
                break;
            case QUIT:
                color = context.getResources().getColor(R.color.colorOrange, null);
                break;
            case DNC:
                color = context.getResources().getColor(R.color.colorOlive, null);
                break;
            default:
                color = context.getResources().getColor(R.color.cardview_light_background, null);
                break;
        }
        return color;
    }

    public class RiderViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
        }
    }

}
