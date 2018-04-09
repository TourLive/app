package ch.hsr.sa.radiotour.dataaccess.models;

import com.google.gson.annotations.Expose;

public class JudgmentRiderConnectionDTO {
    @Expose
    private String id;
    @Expose
    private int rank;
    @Expose
    private long riderId;
    @Expose
    private long judgementId;

    public JudgmentRiderConnectionDTO(JudgmentRiderConnection judgmentRiderConnection) {
        this.id = judgmentRiderConnection.getId();
        this.rank = judgmentRiderConnection.getRank();
        this.riderId = judgmentRiderConnection.getRider().get(0).getId();
        this.judgementId = judgmentRiderConnection.getJudgements().first().getId();
    }
}
