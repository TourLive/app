package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.Maillot;
import io.realm.RealmList;

public class MaillotsAdapter extends RecyclerView.Adapter<MaillotsAdapter.MaillotViewHolder> {
    private RealmList<Maillot> maillots;
    private Context context;

    public MaillotsAdapter(RealmList<Maillot> maillots) {
        this.maillots = maillots;
    }

    @Override
    public MaillotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_maillot_item, parent, false);
        MaillotViewHolder holder = new MaillotViewHolder(view);
        this.context = parent.getContext();
        return holder;
    }

    @Override
    public void onBindViewHolder(MaillotViewHolder holder, int position) {
        holder.name.setText(maillots.get(position).getDbIDd() + " | " + maillots.get(position).getName());
        holder.partner.setText(maillots.get(position).getPartner());
        holder.type.setText(maillots.get(position).getPartner());
        holder.trikot.setColorFilter(context.getResources().getColor(R.color.colorBlue));
    }

    @Override
    public int getItemCount() {
        return maillots.size();
    }

    public class MaillotViewHolder extends RecyclerView.ViewHolder {

        private TextView partner;
        private TextView name;
        private TextView type;
        private ImageView trikot;

        public MaillotViewHolder(View itemView) {
            super(itemView);
            partner = (TextView) itemView.findViewById(R.id.MaillotPartner);
            name = (TextView) itemView.findViewById(R.id.MaillotName);
            type = (TextView) itemView.findViewById(R.id.MaillotType);
            trikot = (ImageView) itemView.findViewById(R.id.imgTrikot);
        }
    }

}
