package ch.hsr.sa.radiotour.dataaccess.models;

import java.util.Comparator;

public class JudgmentComperator implements Comparator<Judgement> {
    @Override
    public int compare(Judgement judgement, Judgement t1) {
        int left = judgement.getDistance();
        int right = t1.getDistance();

        return Integer.compare(left, right);
    }
}