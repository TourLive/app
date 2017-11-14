package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import de.codecrafters.tableview.TableDataAdapter;

public class RiderExtendedAdapter extends TableDataAdapter<RiderExtended> {
    private Context context;

    public RiderExtendedAdapter(final Context context, final List<RiderExtended> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final RiderExtended rider = getRowData(rowIndex);
        View view = new View(context);
        DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");

        switch (columnIndex) {
            case 0:
                view = setTextToView(String.valueOf(rider.getStartNr()));
                break;
            case 1:
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(UIUtilitis.getCountryFlag(rider.getCountry()));
                LinearLayout.LayoutParams imgvwDimens =
                        new LinearLayout.LayoutParams(16, 16);
                imageView.setLayoutParams(imgvwDimens);
                imageView.setPadding(0,0,0,0);
                view = imageView;
                break;
            case 2:
                view = setTextToView(String.valueOf(rider.getCountry()));
                break;
            case 3:
                view = setTextToView(String.valueOf(rider.getName()));
                break;
            case 4:
                view = setTextToView(String.valueOf(rider.getTeamShortName()));
                break;
            case 5:
                view = setTextToView(String.valueOf(rider.getBonusPoint()));
                break;
            case 6:
                view = setTextToView(String.valueOf(rider.getMoney()));
                break;
            case 7:
                view = setTextToView(df.format(rider.getVirtualGap()));
                break;
            case 8:
                view = setTextToView(df.format(rider.getOfficialGap()));
                break;
            case 9:
                view = setTextToView(df.format(rider.getOfficialTime()));
                break;
            default:
                break;
        }
        return view;
    }

    private TextView setTextToView(String txt) {
        TextView textView = new TextView(context);
        textView.setPadding(8, 0, 8, 0);
        textView.setTextSize(10);
        textView.setText(txt);
        return textView;
    }
}
