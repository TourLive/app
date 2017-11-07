package ch.hsr.sa.radiotour.controller.adapter;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.presentation.activites.MainActivity;
import io.realm.RealmList;

public class RiderListAdapter extends RecyclerView.Adapter<RiderListAdapter.RiderViewHolder>{

    private RealmList<Rider> riders;
    private android.content.Context context;

    public RiderListAdapter(RealmList<Rider> riders) {
        this.riders = AdapterUtilitis.removeUnknownRiders(riders);
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
        GradientDrawable drawable = (GradientDrawable) holder.tvNummer.getBackground();
        drawable.setColor(getColorFromState(getRiderStateType(position)));

    }

    public RiderStateType getRiderStateType(int position){
        return riders.get(position).getRiderStages().first().getType();
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    private int getColorFromState(RiderStateType stateType){
        int color;
        switch (stateType){
            case DOCTOR:
                color = ContextCompat.getColor(context, R.color.colorBlue);
                break;
            case DROP:
                color = ContextCompat.getColor(context, R.color.colorYellow);
                break;
            case DEFECT:
                color = ContextCompat.getColor(context, R.color.colorRed);
                break;
            case QUIT:
                color = ContextCompat.getColor(context, R.color.colorOlive);
                break;
            case DNC:
                color = ContextCompat.getColor(context, R.color.colorOrange);
                break;
            default:
                color = 0;
                break;
        }
        return color;
    }

    public class RiderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
        }


        @Override
        public void onClick(View view) {
            MainActivity.changeFirstFragment(true);
            MainActivity.notifyAdapter();
        }
    }

}
