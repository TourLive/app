package ch.hsr.sa.radiotour.controller.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RiderStageConnectionPresenter;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.dataaccess.models.RiderStageConnection;
import ch.hsr.sa.radiotour.presentation.UIUtilitis;
import de.codecrafters.tableview.TableDataAdapter;
import io.realm.RealmList;

public class RiderExtendedAdapter extends TableDataAdapter<RiderExtended> {
    private Context context;
    private HashMap<Integer, Integer> virtualRank;

    public RiderExtendedAdapter(final Context context, final List<RiderExtended> data) {
        super(context, data);
        this.context = context;
        virtualRank = new HashMap<>();
        RealmList<RiderStageConnection> riderStageConnections = RiderStageConnectionPresenter.getInstance().getRiderStageConnectionsSortedByVirtualGap();
        for(int i = 0; i < riderStageConnections.size(); i++){
            if(riderStageConnections.get(i).getRiders() != null)
                virtualRank.put(riderStageConnections.get(i).getRiders().getStartNr(), i+1);
        }

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
                if(rider.getMaillot() != null){
                    view = getLayoutInflater().inflate(R.layout.table_cell_tricot, parentView, false);
                    final ImageView imageViewTricot = (ImageView) view.findViewById(R.id.imageViewTricot);
                    getMaillotColor(rider.getMaillot().getColor(), imageViewTricot);
                    imageViewTricot.setAdjustViewBounds(true);
                }
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
                view = setTextToView(AdapterUtilitis.longTimeToString(rider.getVirtualGap()) + " R:" + virtualRank.get(rider.getStartNr()));
                break;
            case 7:
                view = setTextToView(AdapterUtilitis.longTimeToString(rider.getOfficialGap()) + " R:" + rider.getRank());
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

    private void getMaillotColor(String color, ImageView view) {
        int colorCode = ContextCompat.getColor(context, R.color.colorGrayDark);
        switch (color) {
            case "yellow":
                colorCode = ContextCompat.getColor(context, R.color.yellow);
                break;
            case "red":
                colorCode = ContextCompat.getColor(context, R.color.red);
                break;
            case "blue":
                colorCode = ContextCompat.getColor(context, R.color.blue);
                break;
            case "black":
                colorCode = ContextCompat.getColor(context, R.color.black);
                break;
            default:
                break;
        }
        view.setColorFilter(colorCode);
    }
}
