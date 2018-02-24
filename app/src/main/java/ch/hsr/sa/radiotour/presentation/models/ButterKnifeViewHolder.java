package ch.hsr.sa.radiotour.presentation.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.gotev.recycleradapter.RecyclerAdapterNotifier;
import net.gotev.recycleradapter.RecyclerAdapterViewHolder;

import butterknife.ButterKnife;

public abstract class ButterKnifeViewHolder extends RecyclerView.ViewHolder  {
    public ButterKnifeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}