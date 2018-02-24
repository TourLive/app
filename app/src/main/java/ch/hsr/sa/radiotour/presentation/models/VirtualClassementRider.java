package ch.hsr.sa.radiotour.presentation.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.gotev.recycleradapter.AdapterItem;
import net.gotev.recycleradapter.RecyclerAdapterNotifier;

import butterknife.BindView;
import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import ch.hsr.sa.radiotour.dataaccess.models.RankingType;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;

public class VirtualClassementRider extends AdapterItem<VirtualClassementRider.Holder> {

    private Context context;
    private int riderStartNr;
    private String riderName;
    private String riderTeam;
    private String riderCountry;
    private long riderOfficalTime;
    private long riderOfficalGap;
    private long riderVirtualGap;
    private int riderPoints;
    private int riderMountainPoints;
    private int riderSprintPoints;
    private int riderMoney;
    private Maillot maillot;
    private RiderStageConnection riderStageConnection;

    public VirtualClassementRider(Context context, Rider rider) {
        riderStageConnection = rider.getRiderStages().first();
        this.context = context;
        this.riderStartNr = rider.getStartNr();
        this.riderName = rider.getName();
        this.riderTeam = rider.getTeamShortName();
        this.riderCountry = rider.getCountry();
        this.riderOfficalTime = riderStageConnection.getOfficialTime();
        this.riderOfficalGap = riderStageConnection.getOfficialGap();
        this.riderVirtualGap = riderStageConnection.getVirtualGap();
        this.riderPoints = riderStageConnection.getBonusPoint();
        this.riderMountainPoints = riderStageConnection.getMountainBonusPoints();
        this.riderSprintPoints = riderStageConnection.getSprintBonusPoints();
        this.riderMoney = riderStageConnection.getMoney();
    }

    public int getRiderStartNr() {
        return riderStartNr;
    }

    public String getRiderName() {
        return riderName;
    }

    public String getRiderTeam() {
        return riderTeam;
    }

    public String getRiderCountry() {
        return riderCountry;
    }

    public long getRiderOfficalTime() {
        return riderOfficalTime;
    }

    public long getRiderOfficalGap() {
        return riderOfficalGap;
    }

    public long getRiderVirtualGap() {
        return riderVirtualGap;
    }

    public int getRiderPoints() {
        return riderPoints;
    }

    public int getRiderMountainPoints() {
        return riderMountainPoints;
    }

    public int getRiderSprintPoints() {
        return riderSprintPoints;
    }

    public int getRiderMoney() {
        return riderMoney;
    }

    @Override
    public boolean onFilter(String searchTerm) {
        return riderName.contains(searchTerm);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_virtualclassement;
    }

    @Override
    protected void bind(VirtualClassementRider.Holder holder) {
        holder.virtualClassementStartNr.setText(Integer.toString(riderStartNr));
        holder.virtualClassementName.setText(riderName);
        holder.virtualClassementTeam.setText(riderTeam);
        holder.virtualClassementCountry.setText(riderCountry);
        holder.virtualClassementOfficialTime.setText(AdapterUtilitis.longTimeToString(riderOfficalTime));
        holder.virtualClassementOfficalGap.setText(AdapterUtilitis.longTimeToString(riderOfficalGap) + " (" + Integer.toString(riderStageConnection.getRiderRanking(RankingType.OFFICAL).getRank()) + ")");
        holder.virtualClassementVirtualGap.setText(AdapterUtilitis.longTimeToString(riderVirtualGap) + " (" + Integer.toString(riderStageConnection.getRiderRanking(RankingType.VIRTUAL).getRank()) + ")");
        holder.virtualClassementPoints.setText(Integer.toString(riderPoints));
        holder.virtualClassementMountainPoints.setText(Integer.toString(riderMountainPoints) + " (" + Integer.toString(riderStageConnection.getRiderRanking(RankingType.MOUNTAIN).getRank()) + ")");
        holder.virtualClassementSprintPoints.setText(Integer.toString(riderSprintPoints) + " (" + Integer.toString(riderStageConnection.getRiderRanking(RankingType.SPRINT).getRank()) + ")");
        holder.virtualClassementMoney.setText(Integer.toString(riderMoney));
        holder.virtualClassementFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(riderCountry)));

    }

    public static class Holder extends ButterKnifeViewHolder {

        @BindView(R.id.virtualClassementStartNr)
        TextView virtualClassementStartNr;
        @BindView(R.id.virtualClassementName)
        TextView virtualClassementName;
        @BindView(R.id.virtualClassementMaillot)
        ImageView virtualClassementMaillot;
        @BindView(R.id.virtualClassementFlag)
        ImageView virtualClassementFlag;
        @BindView(R.id.virtualClassementTeam)
        TextView virtualClassementTeam;
        @BindView(R.id.virtualClassementCountry)
        TextView virtualClassementCountry;
        @BindView(R.id.virtualClassementOfficialTime)
        TextView virtualClassementOfficialTime;
        @BindView(R.id.virtualClassementOfficalGap)
        TextView virtualClassementOfficalGap;
        @BindView(R.id.virtualClassementVirtualGap)
        TextView virtualClassementVirtualGap;
        @BindView(R.id.virtualClassementPoints)
        TextView virtualClassementPoints;
        @BindView(R.id.virtualClassementMountainPoints)
        TextView virtualClassementMountainPoints;
        @BindView(R.id.virtualClassementSprintPoints)
        TextView virtualClassementSprintPoints;
        @BindView(R.id.virtualClassementMoney)
        TextView virtualClassementMoney;


        public Holder(View itemView, RecyclerAdapterNotifier adapter) {
            super(itemView, adapter);
        }
    }
}
