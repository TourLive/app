package ch.hsr.sa.radiotour.presentation.models;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import ch.hsr.sa.radiotour.presentation.fragments.VirtualClassFragment;

public class VirtualClassementRider extends AdapterItem<VirtualClassementRider.Holder> implements View.OnClickListener {

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
    private VirtualClassFragment fragment;

    public VirtualClassementRider(VirtualClassFragment  fragment, Context context, Rider rider) {
        this.fragment = fragment;
        this.riderStageConnection = rider.getRiderStages().first();
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
        this.maillot = riderStageConnection.getMaillot();
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

    public RiderStageConnection getRiderStageConnection() { return riderStageConnection; }

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
        holder.virtualClassementMoney.setText(Integer.toString(riderMoney) + " (" + Integer.toString(riderStageConnection.getRiderRanking(RankingType.MONEY).getRank()) + ")");
        holder.virtualClassementFlag.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(riderCountry)));
        holder.itemView.setOnClickListener(this);
        if(maillot != null) {
            holder.virtualClassementMaillot.setColorFilter(getMaillotColor(maillot.getColor()));
            holder.virtualClassementMaillot.setVisibility(View.VISIBLE);
        } else {
            holder.virtualClassementMaillot.setVisibility(View.GONE);
        }

    }

    private int getMaillotColor(String color) {
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
        return colorCode;
    }

    @Override
    public void onClick(View v) {
        fragment.resetAndSetSelectedRow(v);
        v.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorAccent));
    }

    public boolean onEvent(int position, Bundle data) {
        return false;
    }


    public static class Holder extends ButterKnifeViewHolderList {

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
