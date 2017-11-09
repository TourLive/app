package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import de.codecrafters.tableview.TableDataAdapter;

public class RiderExtendedAdapter extends TableDataAdapter<RiderExtended> {

    public RiderExtendedAdapter(final Context context, final List<RiderExtended> data) {
        super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final RiderExtended rider = getRowData(rowIndex);

        TextView textView = new TextView(getContext());
        textView.setPadding(8, 0, 8, 0);

        switch (columnIndex) {
            case 0:
                textView.setText(String.valueOf(rider.getStartNr()));
                break;
            case 1:
                textView.setText(String.valueOf(rider.getCountry()));
                break;
            case 2:
                textView.setText(String.valueOf(rider.getName()));
                break;
            case 3:
                textView.setText(String.valueOf(rider.getTeamShortName()));
                break;
            case 4:
                textView.setText(String.valueOf(rider.getBonusPoint()));
                break;
            case 5:
                textView.setText(String.valueOf(rider.getMoney()));
                break;
            case 6:
                textView.setText(String.valueOf(rider.getVirtualGap().toString()));
                break;
            case 7:
                textView.setText(String.valueOf(rider.getOfficialGap().toString()));
                break;
            case 8:
                textView.setText(String.valueOf(rider.getOfficialTime().toString()));
                break;
        }
        return textView;
    }
}
