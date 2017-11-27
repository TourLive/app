package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Date;

public class RiderExtended {
    private int rank;
    private int bonusPoint;
    private int bonusTime;
    private Date officialTime;
    private Date officialGap;
    private Date virtualGap;
    private String typeState;
    private int money;
    private String id;

    private int mountainBonusPoints;
    private int sprintBonusPoints;

    private int startNr;
    private String name;
    private String country;
    private String teamName;
    private String teamShortName;
    private boolean isUnknown = false;

    public String getId() {
        return id;
    }

    public int getStartNr() {
        return startNr;
    }

    public void setStartNr(int startNr) { this.startNr = startNr; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamShortName() {
        return teamShortName;
    }

    public void setTeamShortName(String teamShortName) {
        this.teamShortName = teamShortName;
    }


    public boolean isUnknown() {
        return isUnknown;
    }

    public void setUnknown(boolean unknown) {
        isUnknown = unknown;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(int bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public int getBonusTime() {
        return bonusTime;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public void setBonusTime(int bonusTime) {
        this.bonusTime = bonusTime;
    }

    public Date getOfficialTime() {
        return officialTime;
    }

    public void setOfficialTime(Date officialTime) {
        this.officialTime = officialTime;
    }

    public Date getOfficialGap() {
        return officialGap;
    }

    public void setOfficialGap(Date officialGap) {
        this.officialGap = officialGap;
    }

    public Date getVirtualGap() {
        return virtualGap;
    }

    public void setVirtualGap(Date virtualGap) {
        this.virtualGap = virtualGap;
    }

    public RiderStateType getType() {
        return RiderStateType.valueOf(typeState);
    }

    public void setType(RiderStateType type) {
        this.typeState = type.toString();
    }

    public int getMountainBonusPoints() {
        return mountainBonusPoints;
    }

    public void setMountainBonusPoints(int mountainBonusPoints) {
        this.mountainBonusPoints = mountainBonusPoints;
    }

    public void appendMountainBonusPoints(int add) { this.mountainBonusPoints += add; }

    public void removeMountainBonusPoints(int sub) { this.mountainBonusPoints -= sub; }

    public int getSprintBonusPoints() {
        return sprintBonusPoints;
    }

    public void setSprintBonusPoints(int sprintBonusPoints) {
        this.sprintBonusPoints = sprintBonusPoints;
    }

    public void appendSprintBonusPoints(int add) { this.sprintBonusPoints += add; }

    public void removeSprintBonusPoints(int sub) { this.sprintBonusPoints -= sub; }
}
