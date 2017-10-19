package ch.hsr.sa.radiotour.controller.adapter;
import android.content.ClipData;
import android.graphics.drawable.ColorDrawable;
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

public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderViewHolder>{

    private RealmList<Rider> riders;
    private RealmList<Rider> selectedRiders;
    private ArrayList<View> selectedViews;
    private ArrayList<RiderViewHolder> holders;
    private android.content.Context context;

    public RiderAdapter(RealmList<Rider> riders) {
        this.riders = riders;
        this.selectedRiders = new RealmList<>();
        this.selectedViews = new ArrayList<>();
        this.holders = new ArrayList<>();
    }

    @Override
    public RiderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider, parent, false);
        RiderViewHolder holder = new RiderViewHolder(view);
        context = parent.getContext();
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RiderViewHolder holder, int position) {
        holder.tvNummer.setText(String.valueOf(riders.get(position).getStartNr()));
        GradientDrawable drawable = (GradientDrawable) holder.tvNummer.getBackground();
        drawable.setColor(getColorFromState(RiderStateType.AKTIVE));
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }



    public RealmList<Rider> getSelectedRiders(){
        return this.selectedRiders;
    }

    public void resetSelectedRiders(){
        for(View view : selectedViews){
            view.setBackgroundResource(R.color.cardview_light_background);
        }
        selectedViews.clear();
        selectedRiders.clear();
    }

    public void updateRiderState(RiderStageConnection connection){
        GradientDrawable drawable = (GradientDrawable) holders.get(connection.getRiders().getStartNr()).tvNummer.getBackground();
        drawable.setColor(getColorFromState(connection.getType()));
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

    public class RiderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener{

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnDragListener(this);
        }

        @Override
        public void onClick(View v) {
            Rider rider = riders.get(getLayoutPosition());
            if(selectedRiders.contains(rider)){
                selectedRiders.remove(rider);
                selectedViews.remove(v);
                v.setBackgroundResource(R.color.cardview_light_background);
            } else {
                selectedRiders.add(rider);
                selectedViews.add(v);
                v.setBackgroundResource(R.color.colorAccent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            ClipData data = ClipData.newPlainText(" ", " ");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDragAndDrop(data, shadowBuilder, selectedRiders, 0);
            return true;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if(event.getAction() == DragEvent.ACTION_DRAG_ENDED){
                for(View view : selectedViews){
                    view.setBackgroundResource(R.color.cardview_light_background);
                }
                selectedViews.clear();
                selectedRiders.clear();
            }
            return true;
        }
    }

}
