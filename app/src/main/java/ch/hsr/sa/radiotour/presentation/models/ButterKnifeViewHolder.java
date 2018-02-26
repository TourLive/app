package ch.hsr.sa.radiotour.presentation.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class ButterKnifeViewHolder extends RecyclerView.ViewHolder  {
    public ButterKnifeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}