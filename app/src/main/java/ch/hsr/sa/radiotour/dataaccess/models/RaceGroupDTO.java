package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class RaceGroupDTO {
    @Expose
    private String id;
    @Expose
    private Long dbRaceGroupid;
    @Expose
    private RaceGroupType type;
    @Expose
    private long actualGapTime;
    @Expose
    private long historyGapTime;
    @Expose
    private int position;
    @Expose
    private List<Long> riders = new ArrayList<>();

    public RaceGroupDTO(RaceGroup raceGroup) {
        this.id = raceGroup.getId();
        this.dbRaceGroupid = raceGroup.getDbRaceGroupid();
        this.type = raceGroup.getType();
        this.actualGapTime = raceGroup.getActualGapTime();
        this.historyGapTime = raceGroup.getHistoryGapTime();
        this.position = raceGroup.getPosition();
        for (Rider r : raceGroup.getRiders()) {
            this.riders.add(r.getId());
        }

    }

    public String getId() {
        return id;
    }
}
