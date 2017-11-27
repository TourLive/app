package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
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

        switch (columnIndex) {
            case 0:
                view = setTextToView(String.valueOf(rider.getStartNr()));
                break;
            case 1:
                view = setTextToView(String.valueOf(rider.getRank()));
                break;
            case 4:
                view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
                final ImageView imageView = (ImageView) view.findViewById(R.id.imageViewCountry);
                imageView.setImageResource(UIUtilitis.getCountryFlag(String.valueOf(rider.getCountry())));
                imageView.setAdjustViewBounds(true);
                break;
            case 5:
                view = setTextToView(String.valueOf(rider.getCountry()));
                break;
            case 2:
                view = setTextToView(String.valueOf(rider.getName()));
                break;
            case 3:
                view = setTextToView(String.valueOf(rider.getTeamShortName()));
                break;
            case 9:
                view = setTextToView(String.valueOf(rider.getBonusPoint()));
                break;
            case 12:
                view = setTextToView(String.valueOf(rider.getMoney()));
                break;
            case 8:
                view = setTextToView(AdapterUtilitis.longTimeToString(rider.getVirtualGap()));
                break;
            case 7:
                view = setTextToView(AdapterUtilitis.longTimeToString(rider.getOfficialGap()));
                break;
            case 6:
                view = setTextToView(AdapterUtilitis.longTimeToString(rider.getOfficialTime()));
                break;
            case 10:
                view = setTextToView(String.valueOf(rider.getMountainBonusPoints()));
                break;
            case 11:
                view = setTextToView(String.valueOf(rider.getSprintBonusPoints()));
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
