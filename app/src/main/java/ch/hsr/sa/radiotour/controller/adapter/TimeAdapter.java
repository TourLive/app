package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.NumberViewHolder> {

    private String[] numbers = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
    private String selectedNumber;
    private View selectedView;
    private int numbersToSet;
    private Context context;

    public TimeAdapter(int n) {
        this.numbersToSet = n;
    }

    @Override
    public TimeAdapter.NumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_number, parent, false);
        this.context = parent.getContext();
        return new TimeAdapter.NumberViewHolder(view);
    }

    public String getSelectedNumber() {
        return selectedNumber;
    }

    @Override
    public void onBindViewHolder(TimeAdapter.NumberViewHolder holder, int position) {
        holder.numberPad.setText(numbers[position]);
        if (position == numbersToSet) {
            selectedNumber = numbers[position];
            holder.itemView.setBackgroundResource(R.color.colorAccent);
            selectedView = holder.itemView;
        }
    }

    @Override
    public int getItemCount() {
        return numbers.length;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView numberPad;

        public NumberViewHolder(View itemView) {
            super(itemView);
            numberPad = itemView.findViewById(R.id.numberPad);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            selectedView.setBackgroundResource(R.color.cardview_light_background);
            selectedNumber = numbers[getLayoutPosition()];
            selectedView = view;
            view.setBackgroundResource(R.color.colorAccent);
        }
    }
}
