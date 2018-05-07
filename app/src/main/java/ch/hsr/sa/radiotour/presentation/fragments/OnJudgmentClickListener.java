package ch.hsr.sa.radiotour.presentation.fragments;

import ch.hsr.sa.radiotour.dataaccess.models.Judgement;

/**
 * Created by Urs Forrer on 11.11.2017.
 */

public interface OnJudgmentClickListener {
    void onJudgmentClicked(Judgement judgement, int position);
}
