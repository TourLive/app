package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class RiderBasicAdapter extends RecyclerView.Adapter<RiderBasicAdapter.RiderViewHolder> {
    private RealmList<Rider> riders;
    private Rider selectedRider = null;
    private View selectedView = null;
    private HashMap<Integer, RiderViewHolder> holderHashMap;
    private Context context;
    private Judgement judgement;

    public RiderBasicAdapter(RealmList<Rider> riders, Judgement judgement) {
        this.riders = AdapterUtilitis.removeUnknownRiders(riders);
        this.holderHashMap = new HashMap<>();
        this.judgement = judgement;
    }

    @Override
    public RiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider_edit, parent, false);
        this.context = parent.getContext();
        return new RiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderViewHolder holder, int position) {
        holder.tvNummer.setText(String.valueOf(riders.get(position).getStartNr()));
        holderHashMap.put(riders.get(position).getStartNr(), holder);
        for (JudgmentRiderConnection jRc : judgement.getJudgmentRiderConnection()) {
            if (jRc.getRider().contains(riders.get(position))) {
                setColorOnRider(riders.get(position).getStartNr());
            }
        }
    }

    public void setColorOnRider(int startNr) {
        RiderViewHolder holder = holderHashMap.get(startNr);
        holder.tvNummer.setTextColor(ContextCompat.getColor(context, R.color.colorGrayMiddle));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public void resetSelectedRider() {
        selectedView.setBackgroundColor(0);
        selectedRider = null;
    }

    public Rider getSelectedRider() {
        return this.selectedRider;
    }

    public class RiderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Rider rider = riders.get(getAdapterPosition());
            if (selectedView != null) {
                selectedView.setBackgroundResource(0);
            }
            selectedRider = rider;
            selectedView = view;
            view.setBackgroundResource(R.color.colorTeal);
        }
    }

}
