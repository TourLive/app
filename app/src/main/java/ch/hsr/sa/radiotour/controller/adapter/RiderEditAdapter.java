package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroupType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStateType;
import io.realm.RealmList;

public class RiderEditAdapter extends RecyclerView.Adapter<RiderEditAdapter.RiderViewHolder> {

    private RealmList<Rider> riders;
    private Context context;
    private RealmList<Rider> selectedRiders;
    private ArrayList<View> selectedViews;
    private HashMap<Integer, RiderViewHolder> holderHashMap;


    public RiderEditAdapter(RealmList<Rider> riders, Context context) {
        this.riders = AdapterUtilitis.removeUnknownRiders(riders);
        this.selectedRiders = new RealmList<>();
        this.selectedViews = new ArrayList<>();
        this.holderHashMap = new HashMap<>();
        this.context = context;
    }

    @Override
    public RiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_rider_edit, parent, false);
        return new RiderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderViewHolder holder, int position) {
        holder.tvNummer.setText(String.valueOf(riders.get(position).getStartNr()));
        setRiderStateAnimation(holder.tvNummer, getRiderStateType(position));
        animateRiderInGroup(holder.tvNummer, riders.get(position).getStartNr());
        holderHashMap.put(riders.get(position).getStartNr(), holder);
    }

    public RiderStateType getRiderStateType(int position) {
        return riders.get(position).getRiderStages().first().getType();
    }

    public int getItemStartNr(int pos) {
        return riders.get(pos).getStartNr();
    }

    public void animateRiderInGroup(TextView tvNumber, Integer startNr) {
        RaceGroup raceGroup = RiderPresenter.getInstance().getRiderByStartNr(startNr).getRaceGroups();
        if (raceGroup != null && context != null) {
            GradientDrawable drawable = (GradientDrawable) tvNumber.getBackground();
            if (raceGroup.getType() != RaceGroupType.FELD) {
                drawable.setColor(ContextCompat.getColor(context, R.color.colorGrayLight));
            } else {
                drawable.setColor(0);
            }
        }
    }

    public void updateAnimateRiderInGroup(String raceGroupId) {
        RealmList<Rider> ridersToUpdate = RaceGroupPresenter.getInstance().getRaceGroupById(raceGroupId).getRiders();
        if (!holderHashMap.isEmpty()) {
            for (Rider r : ridersToUpdate) {
                if (holderHashMap.get(r.getStartNr()) != null) {
                    TextView tvNumber = holderHashMap.get(r.getStartNr()).tvNummer;
                    animateRiderInGroup(tvNumber, r.getStartNr());
                }
            }
        }
    }

    public void updateRiderStateOnGUI(RiderStageConnection connection) {
        RiderStateType stateType = connection.getType();
        if (!holderHashMap.isEmpty()) {
            TextView tvNumber = holderHashMap.get(connection.getRiders().getStartNr()).tvNummer;
            setRiderStateAnimation(tvNumber, stateType);
            if (stateType == RiderStateType.ACTIVE) {
                animateRiderInGroup(tvNumber, Integer.valueOf(connection.getRiders().getStartNr()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return riders.size();
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
                tvNumber.setPaintFlags(0);
                tvNumber.setTextColor(ContextCompat.getColor(context, R.color.colorGrayDark));
                break;
        }
    }

    public void resetSelectRiders() {
        for (View view : selectedViews) {
            view.setBackgroundColor(0);
        }
        selectedRiders.clear();
        selectedViews.clear();
    }

    public RealmList<Rider> getSelectedRiders() {
        return this.selectedRiders;
    }

    public class RiderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = itemView.findViewById(R.id.tv_nummer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Rider rider = riders.get(getLayoutPosition());
            if (selectedRiders.contains(rider)) {
                selectedRiders.remove(rider);
                selectedViews.remove(view);
                itemView.setBackgroundColor(0);
            } else {
                selectedViews.add(view);
                selectedRiders.add(rider);
                itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTeal));
            }
        }
    }

}
