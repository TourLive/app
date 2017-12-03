package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import io.realm.RealmList;

public class MaillotsAdapter extends RecyclerView.Adapter<MaillotsAdapter.MaillotViewHolder> {
    private RealmList<Maillot> maillots;
    private Map<Maillot, MaillotViewHolder> maillotMaillotViewHolderMap;
    private Context context;

    public MaillotsAdapter(RealmList<Maillot> maillots, Context context) {
        this.maillots = maillots;
        this.context = context;
    }

    @Override
    public MaillotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_maillot_item, parent, false);
        return new MaillotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MaillotViewHolder holder, int position) {
        holder.name.setText(maillots.get(position).getDbIDd() + " | " + maillots.get(position).getName());
        holder.partner.setText(maillots.get(position).getPartner());
        getMaillotColor(maillots.get(position).getColor(), holder.trikot);
        getActualLeader(maillots.get(position).getType(), holder.leader);
        this.maillotMaillotViewHolderMap.put(maillots.get(position), holder);

    }

    private void getMaillotColor(String color, ImageView view) {
        int colorCode = ContextCompat.getColor(context, R.color.colorGrayDark);
        switch (color) {
            case "yellow":
                colorCode = ContextCompat.getColor(context, R.color.yellow);
                break;
            case "red":
                colorCode = ContextCompat.getColor(context, R.color.red);
                break;
            case "blue":
                colorCode = ContextCompat.getColor(context, R.color.blue);
                break;
            case "black":
                colorCode = ContextCompat.getColor(context, R.color.black);
                break;
            default:
                break;
        }
        view.setColorFilter(colorCode);
    }

    private void getActualLeader(String type, TextView view){
        Rider rider = null;
        List<RiderStageConnection> riderStageConnections = new ArrayList<>(RiderStageConnectionPresenter.getInstance().getAllRiderStateConnections());
        switch (type){
            case "leader":
                Collections.sort(riderStageConnections, new Comparator<RiderStageConnection>() {
                    @Override
                    public int compare(RiderStageConnection o1, RiderStageConnection o2) {
                        if(o1.getVirtualGap() >= o2.getVirtualGap()){
                            return 1;
                        }
                        return -1;
                    }
                });
                rider = riderStageConnections.get(0).getRiders();
                view.setText(String.format("%d, %s, %s, %s", rider.getStartNr(), "flag", rider.getName(), rider.getTeamName()));
                break;
            case "mountain":
                Collections.sort(riderStageConnections, new Comparator<RiderStageConnection>() {
                    @Override
                    public int compare(RiderStageConnection o1, RiderStageConnection o2) {
                        if(o1.getMountainBonusPoints() >= o2.getMountainBonusPoints()){
                            return 1;
                        }
                        return -1;
                    }
                });
                rider = riderStageConnections.get(0).getRiders();
                view.setText(rider.getName());
                break;
            case "points":
                Collections.sort(riderStageConnections, new Comparator<RiderStageConnection>() {
                    @Override
                    public int compare(RiderStageConnection o1, RiderStageConnection o2) {
                        if(o1.getBonusPoint() >= o2.getBonusPoint()){
                            return 1;
                        }
                        return -1;
                    }
                });
                rider = riderStageConnections.get(0).getRiders();
                view.setText(rider.getName());
                break;
            case "bestSwiss":
                Collections.sort(riderStageConnections, new Comparator<RiderStageConnection>() {
                    @Override
                    public int compare(RiderStageConnection o1, RiderStageConnection o2) {
                        if(o1.getVirtualGap() >= o2.getVirtualGap()){
                            return 1;
                        }
                        return -1;
                    }
                });
                for(RiderStageConnection connection : riderStageConnections){
                    if(connection.getRiders().getCountry().equals("SUI")){
                        rider = connection.getRiders();
                        break;
                    }
                }
                if(rider!=null)
                    view.setText(rider.getName());
                break;
            default:
                break;
        }
    }

    public void updateLeaders(){
        // needs to be calles from callback when ranking changed, get assoziated view
        for(Maillot maillot : this.maillots){
            getActualLeader(maillot.getType(), maillotMaillotViewHolderMap.get(maillot).leader);
        }
    }

    @Override
    public int getItemCount() {
        return maillots.size();
    }

    public class MaillotViewHolder extends RecyclerView.ViewHolder {

        private TextView partner;
        private TextView name;
        private ImageView trikot;
        private TextView leader;

        public MaillotViewHolder(View itemView) {
            super(itemView);
            partner = (TextView) itemView.findViewById(R.id.MaillotPartner);
            name = (TextView) itemView.findViewById(R.id.MaillotName);
            trikot = (ImageView) itemView.findViewById(R.id.imgTrikot);
            leader = (TextView) itemView.findViewById(R.id.leaderInfo);
        }
    }

}
