package ch.hsr.sa.radiotour.controller.adapter;

/**
 * Created by Urs Forrer on 21.10.2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int from, int to);
    void onItemDismiss(int position);
}
