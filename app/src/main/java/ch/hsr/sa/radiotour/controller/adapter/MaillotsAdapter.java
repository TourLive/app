package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import io.realm.RealmList;

public class MaillotsAdapter extends RecyclerView.Adapter<MaillotsAdapter.MaillotViewHolder> {
    private RealmList<Maillot> maillots;
    private HashMap<Maillot, MaillotViewHolder> maillotMaillotViewHolderMap;
    private Context context;

    public MaillotsAdapter(RealmList<Maillot> maillots, Context context) {
        this.maillots = maillots;
        this.context = context;
        this.maillotMaillotViewHolderMap = new HashMap<>();
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
        getActualLeader(maillots.get(position).getType(), holder);
        getRealLeader(maillots.get(position), holder);
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

    private void getRealLeader(Maillot maillot, MaillotViewHolder holder) {
        if (maillot.getRider() != null) {
            holder.leaderRealStart.setText(String.valueOf(maillot.getRider().getStartNr()));
            holder.leaderRealFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(maillot.getRider().getCountry())));
            holder.leaderRealFlag.setAdjustViewBounds(true);
            holder.leaderRealInfo.setText(String.format("%s, %s, (%d)", maillot.getRider().getName(), maillot.getRider().getTeamName(), maillot.getRider().getRiderStages().first().getRank()));
        }
    }

    private void getActualLeader(String type, MaillotViewHolder holder) {
        Rider rider = null;
        RealmList<RiderStageConnection> riderStageConnections;
        switch (type) {
            case "leader":
                riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByVirtualGap();
                rider = riderStageConnections.get(0).getRiders();
                break;
            case "mountain":
                riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByMountain();
                rider = riderStageConnections.get(0).getRiders();
                break;
            case "points":
                riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByPoints();
                rider = riderStageConnections.get(0).getRiders();
                break;
            case "bestSwiss":
                riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByBestSwiss();
                rider = riderStageConnections.get(0).getRiders();
                break;
            default:
                break;
        }
        if (rider != null) {
            holder.leaderVirtStart.setText(String.valueOf(rider.getStartNr()));
            holder.leaderVirtFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(rider.getCountry())));
            holder.leaderVirtFlag.setAdjustViewBounds(true);
            holder.leaderVirtInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), rider.getRiderStages().first().getRiderRanking(RankingType.VIRTUAL)));
        }
    }

    public void updateLeaders() {
        // needs to be calles from callback when ranking changed, get assoziated view
        for (Maillot maillot : this.maillots) {
            getActualLeader(maillot.getType(), maillotMaillotViewHolderMap.get(maillot));
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
        private TextView leaderVirtInfo;
        private TextView leaderRealInfo;
        private TextView leaderRealStart;
        private TextView leaderVirtStart;
        private ImageView leaderRealFlag;
        private ImageView leaderVirtFlag;

        public MaillotViewHolder(View itemView) {
            super(itemView);
            partner = (TextView) itemView.findViewById(R.id.MaillotPartner);
            name = (TextView) itemView.findViewById(R.id.MaillotName);
            trikot = (ImageView) itemView.findViewById(R.id.imgTrikot);
            leaderVirtInfo = (TextView) itemView.findViewById(R.id.LeaderVirtInfo);
            leaderRealInfo = (TextView) itemView.findViewById(R.id.LeaderRealInfo);
            leaderRealStart = (TextView) itemView.findViewById(R.id.LeaderRealStart);
            leaderVirtStart = (TextView) itemView.findViewById(R.id.LeaderVirtStart);
            leaderRealFlag = (ImageView) itemView.findViewById(R.id.img_country_real);
            leaderVirtFlag = (ImageView) itemView.findViewById(R.id.img_country_virt);
        }
    }

}
