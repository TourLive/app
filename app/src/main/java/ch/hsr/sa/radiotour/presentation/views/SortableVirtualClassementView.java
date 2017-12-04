package ch.hsr.sa.radiotour.presentation.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RiderExtended;
import ch.hsr.sa.radiotour.dataaccess.models.VirtualClassementComparators;
import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


public class SortableVirtualClassementView extends SortableTableView<RiderExtended> {
    public SortableVirtualClassementView(final Context context) {
        this(context, null);
    }

    public SortableVirtualClassementView(final Context context, final AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.listViewStyle);
    }

    public SortableVirtualClassementView(final Context context, final AttributeSet attributeSet, final int styleAttributes) {
        super(context, attributeSet, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, R.string.virtrank_startnr, R.string.virtrank_name, R.string.virtrank_team, R.string.virtrank_img, R.string.virtrank_country, R.string.virtrank_offical_time, R.string.virtrank_offical_deficit, R.string.virtrank_virt_deficit, R.string.virtrank_point_bonus, R.string.virtrank_point_mountain, R.string.virtrank_point_sprint, R.string.virtrank_price_money);
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.colorBackground));
        simpleTableHeaderAdapter.setTextSize(10);
        simpleTableHeaderAdapter.setPaddingTop(8);
        simpleTableHeaderAdapter.setPaddingBottom(8);
        simpleTableHeaderAdapter.setPaddingLeft(4);
        simpleTableHeaderAdapter.setPaddingRight(0);
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.colorBackground);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.colorBackground);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(12);
        tableColumnWeightModel.setColumnWeight(0, 3);
        tableColumnWeightModel.setColumnWeight(1, 8);
        tableColumnWeightModel.setColumnWeight(2, 3);
        tableColumnWeightModel.setColumnWeight(3, 1);
        tableColumnWeightModel.setColumnWeight(4, 3);
        tableColumnWeightModel.setColumnWeight(5, 5);
        tableColumnWeightModel.setColumnWeight(6, 5);
        tableColumnWeightModel.setColumnWeight(7, 5);
        tableColumnWeightModel.setColumnWeight(8, 3);
        tableColumnWeightModel.setColumnWeight(9, 3);
        tableColumnWeightModel.setColumnWeight(10, 3);
        tableColumnWeightModel.setColumnWeight(11, 3);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, VirtualClassementComparators.getStartNrComparator());
        setColumnComparator(1, VirtualClassementComparators.getNameComparator());
        setColumnComparator(2, VirtualClassementComparators.getTeamComparator());
        setColumnComparator(3, null);
        setColumnComparator(4, VirtualClassementComparators.getCountryComparator());
        setColumnComparator(5, VirtualClassementComparators.getOfficialTimeComparator());
        setColumnComparator(6, VirtualClassementComparators.getOffizialDeficitComparator());
        setColumnComparator(7, VirtualClassementComparators.getVirtualDeficitComparator());
        setColumnComparator(8, VirtualClassementComparators.getPointComparator());
        setColumnComparator(9, VirtualClassementComparators.getMountainPointComparator());
        setColumnComparator(10, VirtualClassementComparators.getSprintPointComparator());
        setColumnComparator(11, VirtualClassementComparators.getMoneyComparator());
    }
}
