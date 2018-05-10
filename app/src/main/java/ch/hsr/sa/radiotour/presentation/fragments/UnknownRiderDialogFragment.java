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

    public static UnknownRiderDialogFragment newInstance() {
        return new UnknownRiderDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_unknownrider, null);
        alertDialogBuilder.setView(dialogView);

        final NumberPicker numberPickerH = dialogView.findViewById(R.id.nPickerZiffer1);
        final NumberPicker numberPickerZ = dialogView.findViewById(R.id.nPickerZiffer2);
        final NumberPicker numberPickerE = dialogView.findViewById(R.id.nPickerZiffer3);

        numberPickerH.setMinValue(0);
        numberPickerH.setMaxValue(9);
        numberPickerH.setWrapSelectorWheel(true);

        numberPickerZ.setMinValue(0);
        numberPickerZ.setMaxValue(9);
        numberPickerZ.setWrapSelectorWheel(true);

        numberPickerE.setMinValue(0);
        numberPickerE.setMaxValue(9);
        numberPickerE.setWrapSelectorWheel(true);

        alertDialogBuilder.setTitle(R.string.race_unknownrider_title);
        alertDialogBuilder.setMessage(R.string.race_unknownrider_description);
        alertDialogBuilder.setPositiveButton(R.string.race_unknownrider_button, (DialogInterface dialog, int which) -> {
            UnknownUserAddListener unknownUserAddListener = (UnknownUserAddListener) getTargetFragment();
            int count = numberPickerH.getValue() * 100 + numberPickerZ.getValue() * 10 + numberPickerE.getValue();
            unknownUserAddListener.onFinishAddingUnknownUser(count);
        });

        alertDialogBuilder.setNegativeButton(R.string.dismiss, (DialogInterface dialog, int which) -> dialog.dismiss());

        return alertDialogBuilder.create();
    }

    public interface UnknownUserAddListener {
        void onFinishAddingUnknownUser(int count);
    }

}
