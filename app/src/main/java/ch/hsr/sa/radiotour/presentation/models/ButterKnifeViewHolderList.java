package ch.hsr.sa.radiotour.presentation.models;

import android.view.View;

import net.gotev.recycleradapter.RecyclerAdapterNotifier;
import net.gotev.recycleradapter.RecyclerAdapterViewHolder;

import butterknife.ButterKnife;

/**
 * Created by uforrer on 23.02.2018.
 */

public abstract class ButterKnifeViewHolderList extends RecyclerAdapterViewHolder {
    public ButterKnifeViewHolderList(View itemView, RecyclerAdapterNotifier adapter) {
        super(itemView, adapter);
        ButterKnife.bind(this, itemView);
    }
}