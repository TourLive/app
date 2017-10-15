package ch.hsr.sa.radiotour.controller.adapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class RiderAdapter extends RecyclerView.Adapter<RiderAdapter.RiderViewHolder>{

    private RealmList<Rider> riders;
    private RealmList<Rider> selectedRiders;

    public RiderAdapter(RealmList<Rider> riders) {
        this.riders = riders;
        this.selectedRiders = new RealmList<>();
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

    public class RiderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnDragListener {

        private TextView tvNummer;

        public RiderViewHolder(View itemView) {
            super(itemView);
            tvNummer = (TextView) itemView.findViewById(R.id.tv_nummer);
            itemView.setOnClickListener(this);
            itemView.setOnDragListener(this);
        }

        @Override
        public void onClick(View v) {
            Rider rider = riders.get(getLayoutPosition());
            if(selectedRiders.contains(rider)){
                selectedRiders.remove(rider);
                v.setBackgroundResource(R.color.cardview_light_background);
            } else {
                selectedRiders.add(rider);
                v.setBackgroundResource(R.color.colorAccent);
            }
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("Drag Started", String.valueOf(selectedRiders.size()));
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("Drag Entered", String.valueOf(selectedRiders.size()));
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("Drag Extied", String.valueOf(selectedRiders.size()));
                    break;
                case DragEvent.ACTION_DROP:
                    Log.d("Drop", String.valueOf(selectedRiders.size()));
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("Drag Ended", String.valueOf(selectedRiders.size()));
                default:
                    break;
            }
            return true;
        }
    }

}
