package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;

public class JudgmentComperator implements Comparator<Judgement> {
    @Override
    public int compare(Judgement judgement, Judgement t1) {
        double left = judgement.getDistance();
        double right = t1.getDistance();

        return Double.compare(left, right);
    }
}