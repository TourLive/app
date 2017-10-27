package ch.hsr.sa.radiotour.dataaccess.interfaces;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;

public interface IJudgmentRepository {

    void addJudgment(Judgement judgement);

    void clearAllJudgments();
}
