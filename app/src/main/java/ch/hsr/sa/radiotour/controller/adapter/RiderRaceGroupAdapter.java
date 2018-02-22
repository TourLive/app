package ch.hsr.sa.radiotour.controller.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderComparator;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import ch.hsr.sa.radiotour.presentation.fragments.UnknownRiderTransferDialogFramgent;
import io.realm.RealmList;

public class RiderRaceGroupAdapter extends RecyclerView.Adapter<RiderRaceGroupAdapter.RiderRaceGroupViewHolder> {
    private List<Rider> riders;
    private RealmList<Rider> selectedRider;
    private Fragment fragment;
    private Context context;
    HashMap<String, Integer> tricots;

    public RiderRaceGroupAdapter(RealmList<Rider> riders, Fragment fragment) {
        this.riders = new LinkedList<>(riders);
        Collections.sort(this.riders, new RiderComparator());
        this.selectedRider = new RealmList<>();
        this.fragment = fragment;
        tricots = new HashMap<>();
        initTricots();
    }

    private void initTricots() {
        tricots.put("leader", RiderStageConnectionPresenter.getInstance().getLeader().getRiders().getStartNr());
        tricots.put("sprint", RiderStageConnectionPresenter.getInstance().getSprintLeader().getRiders().getStartNr());
        tricots.put("mountain", RiderStageConnectionPresenter.getInstance().getMountainLeader().getRiders().getStartNr());
        tricots.put("swiss", RiderStageConnectionPresenter.getInstance().getSwissLeader().getRiders().getStartNr());
    }

    @Override
    public RiderRaceGroupAdapter.RiderRaceGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_rider_in_racegroup, parent, false);
        context = parent.getContext();
        return new RiderRaceGroupAdapter.RiderRaceGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RiderRaceGroupAdapter.RiderRaceGroupViewHolder holder, int position) {
        Rider rider = riders.get(position);
        holder.racegroupRiderName.setText(String.valueOf(rider.getName()));
        if (rider.getStartNr() < 900) {
            holder.racegroupRiderStartNr.setText(String.valueOf(rider.getStartNr()));
            holder.racegroupRiderTeam.setText(String.valueOf(rider.getTeamShortName()));
            /*RealmList<RiderStageConnection> riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByVirtualGap();
            for(int i = 0; i < riderStageConnections.size(); i++){
                if(riderStageConnections.get(i).getRiders().getStartNr() == rider.getStartNr()){
                    holder.racegroupRiderVirtualRank.setText("R: " + (i+1));
                    break;
                }
            }*/
            holder.racegroupRiderVirtualRank.setText(Integer.toString(rider.getRiderStages().first().getRiderRanking(RankingType.VIRTUAL).getRank()));
            setTrictos(rider, holder);
        } else {
            holder.racegroupRiderStartNr.setText(R.string.race_startnr_unknownrider);
            holder.racegroupRiderTeam.setText(R.string.race_startnr_unknownrider);
        }
        holder.racegroupRiderCountry.setImageResource(UIUtilitis.getCountryFlag(riders.get(position).getCountry()));
    }

    private void setTrictos(Rider rider, RiderRaceGroupViewHolder holder) {
        int ridernr = rider.getStartNr();
        for (String key : tricots.keySet()) {
            if (tricots.get(key).equals(ridernr)) {
                setMaillotColor(key, holder);
            }
        }
    }

    private void setMaillotColor(String color, RiderRaceGroupViewHolder holder) {
        holder.racegroupRiderTricot.setVisibility(View.VISIBLE);
        int colorCode = ContextCompat.getColor(context, R.color.colorGrayDark);
        switch (color) {
            case "leader":
                colorCode = ContextCompat.getColor(context, R.color.yellow);
                break;
            case "swiss":
                colorCode = ContextCompat.getColor(context, R.color.red);
                break;
            case "mountain":
                colorCode = ContextCompat.getColor(context, R.color.blue);
                break;
            case "sprint":
                colorCode = ContextCompat.getColor(context, R.color.black);
                break;
            default:
                break;
        }
        holder.racegroupRiderTricot.setColorFilter(colorCode);
    }

    @Override
    public int getItemCount() {
        return riders.size();
    }

    public class RiderRaceGroupViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnDragListener, View.OnClickListener {
        private TextView racegroupRiderName;
        private TextView racegroupRiderStartNr;
        private ImageView racegroupRiderCountry;
        private TextView racegroupRiderTeam;
        private ImageView racegroupRiderTricot;
        private TextView racegroupRiderVirtualRank;

        public RiderRaceGroupViewHolder(View itemView) {
            super(itemView);
            racegroupRiderName = (TextView) itemView.findViewById(R.id.racegroup_rider_name);
            racegroupRiderStartNr = (TextView) itemView.findViewById(R.id.racegroup_rider_startnr);
            racegroupRiderCountry = (ImageView) itemView.findViewById(R.id.racegroup_rider_country);
            racegroupRiderTeam = (TextView) itemView.findViewById(R.id.racegroup_rider_team);
            racegroupRiderTricot = (ImageView) itemView.findViewById(R.id.racegroup_rider_tricot);
            racegroupRiderVirtualRank = (TextView) itemView.findViewById(R.id.racegroup_rider_virtualRank);
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
            if (riders.get(getAdapterPosition()).isUnknown()) {
                FragmentManager fm = fragment.getFragmentManager();
                UnknownRiderTransferDialogFramgent alertDialog = UnknownRiderTransferDialogFramgent.newInstance(riders.get(getAdapterPosition()));
                alertDialog.setTargetFragment(fragment, 300);
                alertDialog.show(fm, "");
            }
        }
    }
}
