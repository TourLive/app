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

import butterknife.BindView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderRankingPresenter;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderRanking;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import ch.hsr.sa.radiotour.presentation.models.ButterKnifeViewHolder;
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
        Rider rider = maillot.getRider();
        if (rider != null) {
            holder.leaderRealStart.setText(String.valueOf(rider.getStartNr()));
            holder.leaderRealFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(rider.getCountry())));
            holder.leaderRealFlag.setAdjustViewBounds(true);
            int rank = 0;
            switch (maillot.getType()) {
                case "leader":
                    rank = rider.getRiderStages().first().getRiderRanking(RankingType.VIRTUAL).getRank();
                    break;
                case "mountain":
                    rank = rider.getRiderStages().first().getRiderRanking(RankingType.MOUNTAIN).getRank();
                    break;
                case "points":
                    rank = rider.getRiderStages().first().getRiderRanking(RankingType.SPRINT).getRank();
                    break;
                case "bestSwiss":
                    rank = rider.getRiderStages().first().getRiderRanking(RankingType.SWISS).getRank();
                    break;
                default:
                    break;
            }
            holder.leaderRealInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), rank));
        }
    }

    private void getActualLeader(String type, MaillotViewHolder holder) {
        Rider rider = null;
        RiderRanking riderRanking = null;
        switch (type) {
            case "leader":
                riderRanking = RiderRankingPresenter.getInstance().getFirstInRanking(RankingType.VIRTUAL);
                rider = riderRanking.getRiderStageConnection().getRiders();

                holder.leaderVirtInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), riderRanking.getRank()));
                break;
            case "mountain":
                riderRanking = RiderRankingPresenter.getInstance().getFirstInRanking(RankingType.MOUNTAIN);
                rider = riderRanking.getRiderStageConnection().getRiders();

                holder.leaderVirtInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), riderRanking.getRank()));
                break;
            case "points":
                riderRanking = RiderRankingPresenter.getInstance().getFirstInRanking(RankingType.SPRINT);
                rider = riderRanking.getRiderStageConnection().getRiders();

                holder.leaderVirtInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), riderRanking.getRank()));
                break;
            case "bestSwiss":
                riderRanking = RiderRankingPresenter.getInstance().getFirstInRanking(RankingType.SWISS);
                rider = riderRanking.getRiderStageConnection().getRiders();

                holder.leaderVirtInfo.setText(String.format("%s, %s, (%d)", rider.getName(), rider.getTeamName(), riderRanking.getRank()));
                break;
            default:
                break;
        }
        if (rider != null) {
            holder.leaderVirtStart.setText(String.valueOf(rider.getStartNr()));
            holder.leaderVirtFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(rider.getCountry())));
            holder.leaderVirtFlag.setAdjustViewBounds(true);
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

    public class MaillotViewHolder extends ButterKnifeViewHolder {

        @BindView(R.id.MaillotPartner)
        TextView partner;
        @BindView(R.id.MaillotName)
        TextView name;
        @BindView(R.id.imgTrikot)
        ImageView trikot;
        @BindView(R.id.LeaderVirtInfo)
        TextView leaderVirtInfo;
        @BindView(R.id.LeaderRealInfo)
        TextView leaderRealInfo;
        @BindView(R.id.LeaderRealStart)
        TextView leaderRealStart;
        @BindView(R.id.LeaderVirtStart)
        TextView leaderVirtStart;
        @BindView(R.id.img_country_real)
        ImageView leaderRealFlag;
        @BindView(R.id.img_country_virt)
        ImageView leaderVirtFlag;

        public MaillotViewHolder(View itemView) {
            super(itemView);
        }
    }

}
