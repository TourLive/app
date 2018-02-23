package ch.hsr.sa.radiotour.presentation.models;

import android.view.View;
import android.widget.TextView;

import net.gotev.recycleradapter.AdapterItem;
import net.gotev.recycleradapter.RecyclerAdapterNotifier;

import butterknife.BindView;
import ch.hsr.sa.radiotour.R;

/**
 * Created by uforrer on 23.02.2018.
 */

public class EmptyItem extends AdapterItem<EmptyItem.Holder> {

    private String text;

    public EmptyItem(String text) {
        this.text = text;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_empty;
    }

    @Override
    protected void bind(EmptyItem.Holder holder) {
        holder.textView.setText(text);
    }

    public static class Holder extends ButterKnifeViewHolder {

        @BindView(R.id.test2)
        TextView textView;

        public Holder(View itemView, RecyclerAdapterNotifier adapter) {
            super(itemView, adapter);
        }
    }
}
