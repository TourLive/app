package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import ch.hsr.sa.radiotour.R;

public class UnknownRiderDialogFragment extends DialogFragment {
    public UnknownRiderDialogFragment() {
        // Empty Constructor needed to work as expected
    }

    public interface UnknownUserAddListener {
        void onFinishAddingUnknownUser(int count);
    }

    public static UnknownRiderDialogFragment newInstance() {
        return new UnknownRiderDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_unknownrider, null);
        alertDialogBuilder.setView(dialogView);

        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.nPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(true);

        alertDialogBuilder.setTitle("Adding Unknown Riders");
        alertDialogBuilder.setMessage("Please select how many unknown riders you would add and select the racegroup.");
        alertDialogBuilder.setPositiveButton("Add unknown riders", (DialogInterface dialog, int which) ->  {
           UnknownUserAddListener unknownUserAddListener = (UnknownUserAddListener) getTargetFragment();
           int count = numberPicker.getValue();
           unknownUserAddListener.onFinishAddingUnknownUser(count);
        });

        alertDialogBuilder.setNegativeButton("Dismiss", (DialogInterface dialog, int which) -> dialog.dismiss());

        return alertDialogBuilder.create();
    }

}
