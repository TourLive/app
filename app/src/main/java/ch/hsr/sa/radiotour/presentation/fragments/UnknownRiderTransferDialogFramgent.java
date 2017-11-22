package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.business.presenter.RaceGroupPresenter;
import ch.hsr.sa.radiotour.business.presenter.RiderPresenter;
import ch.hsr.sa.radiotour.controller.AdapterUtilitis;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;
import ch.hsr.sa.radiotour.dataaccess.models.Rider;
import io.realm.RealmList;

public class UnknownRiderTransferDialogFramgent extends DialogFragment {
    private Spinner spinner;
    private Rider selectedUnknownRider;

    public UnknownRiderTransferDialogFramgent() {
        // Empty Constructor needed to work as expected
    }

    public static UnknownRiderTransferDialogFramgent newInstance(Rider rider) {
        UnknownRiderTransferDialogFramgent frag = new UnknownRiderTransferDialogFramgent();
        frag.selectedUnknownRider = rider;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_unknowntransfer, null);
        alertDialogBuilder.setView(dialogView);
        spinner = (Spinner) dialogView.findViewById(R.id.riderSpinner);

        addItemsToSpinner();

        alertDialogBuilder.setTitle("Change unknown rider to an known rider");
        alertDialogBuilder.setMessage("Please select the rider to which you want transfer the unknown rider to.");
        alertDialogBuilder.setPositiveButton("Change the unknown rider", (DialogInterface dialog, int which) -> {
            String[] parts = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString().split("\\-");
            String startNr = parts[0];
            Log.d("", "" + selectedUnknownRider.toString());
            final int unknownRiderStartNr = selectedUnknownRider.getStartNr();
            new Thread(() -> {
                Rider unknownRider = RiderPresenter.getInstance().getRiderByStartNr(unknownRiderStartNr);
                RaceGroup raceGroup = unknownRider.getRaceGroups();
                RealmList<Rider> rider = new RealmList<>();
                rider.add(getRider(startNr));
                RaceGroupPresenter.getInstance().updateRaceGroupRiders(raceGroup, rider);
                RaceGroupPresenter.getInstance().deleteRiderInRaceGroup(raceGroup, unknownRider);
                RiderPresenter.getInstance().removeRiderWithoutCallback(unknownRider);
            }).start();
        });
        alertDialogBuilder.setNegativeButton("Dismiss", (DialogInterface dialog, int which) -> dialog.dismiss());

        return alertDialogBuilder.create();
    }

    private Rider getRider(String startNr) {
        for (Rider r : RiderPresenter.getInstance().getAllRidersReturned()) {
                if (r.getStartNr() ==  Integer.parseInt(startNr)){
                    return r;
                }
        }
        return null;
    }

    private void addItemsToSpinner() {
        List<String> list = AdapterUtilitis.listWithAllRidersForSpinner();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(dataAdapter);
    }
}
