package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.Judgement;
import ch.hsr.sa.radiotour.dataaccess.models.JudgmentRiderConnection;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import ch.hsr.sa.radiotour.presentation.fragments.OnRiderJudgmentClickListener;
import io.realm.RealmList;

public class RiderBasicAdapter extends RecyclerView.Adapter<RiderBasicAdapter.RiderViewHolder> {
    private RealmList<Rider> riders;
    private Rider selectedRider = null;
    private HashMap<Integer, RiderViewHolder> holderHashMap;
    private Context context;
    private Judgement judgement;
    private OnRiderJudgmentClickListener onRiderJudgmentClickListener;
    private List<Integer> selectedRiders = new ArrayList<>();

    public RiderBasicAdapter(RealmList<Rider> riders, Judgement judgement, OnRiderJudgmentClickListener onRiderJudgmentClickListener) {
        this.riders = AdapterUtilitis.removeUnknownRiders(riders);
        this.holderHashMap = new HashMap<>();
        this.judgement = judgement;
        this.onRiderJudgmentClickListener = onRiderJudgmentClickListener;
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
        setRiderStateAnimation(holder.tvNummer, riders.get(position).getRiderStages().first().getType());
    }

    private void setRiderStateAnimation(TextView tvNumber, RiderStateType stateType) {
        GradientDrawable drawable = (GradientDrawable) tvNumber.getBackground();
        switch (stateType) {
            case QUIT:
                tvNumber.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                drawable.setColor(0);
                break;
            case DNC:
                tvNumber.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tvNumber.setTextColor(ContextCompat.getColor(context, R.color.colorGrayMiddle));
                drawable.setColor(0);
                break;
            case DOCTOR:
                drawable.setColor(ContextCompat.getColor(context, R.color.colorBlue));
                break;
            case DEFECT:
                drawable.setColor(ContextCompat.getColor(context, R.color.colorRed));
                break;
            case DROP:
                drawable.setColor(ContextCompat.getColor(context, R.color.colorOlive));
                break;
            default:
                drawable.setColor(0);
                break;
        }
    }

    public void setColorOnRider(int startNr) {
        RiderViewHolder holder = holderHashMap.get(startNr);
        holder.tvNummer.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public void resetSelectedRider() {
        selectedRider = null;
    }

    public Rider getSelectedRider() {
        return this.selectedRider;
    }

    public void removeRiderFromList(Integer i) {
        selectedRiders.remove(i);
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
            if (selectedRiders.contains(rider.getRiderID())) {
                Toast toast = Toast.makeText(context, context.getResources().getString(R.string.judgment_duplicated_rider), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            } else if (rider.getRiderStages().first().getType() == RiderStateType.DNC || rider.getRiderStages().first().getType() == RiderStateType.QUIT) {
                Toast toast = Toast.makeText(context, context.getResources().getString(R.string.judgment_only_active), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
                toast.show();
            } else {
                selectedRider = rider;
                selectedRiders.add(rider.getRiderID());
                setColorOnRider(rider.getStartNr());
                onRiderJudgmentClickListener.saveJudgmnet();
            }
        }
    }

}
