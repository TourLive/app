package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;

/**
 * Created by Urs Forrer on 28.10.2017.
 */

public class UnknownRiderDialogFragment extends DialogFragment {
    public UnknownRiderDialogFragment() {

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

        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.countUnknownRiders);
        final TextView textView = (TextView) dialogView.findViewById(R.id.txtDialogUnknownRiders);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText("" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        alertDialogBuilder.setTitle("Adding Unknown Riders");
        alertDialogBuilder.setMessage("Please select how many unknown riders you would add and select the racegroup.");
        alertDialogBuilder.setPositiveButton("Add unknown riders",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                UnknownUserAddListener unknownUserAddListener = (UnknownUserAddListener) getTargetFragment();
                RaceGroup raceGroup = null;
                int count = seekBar.getProgress();
                unknownUserAddListener.onFinishAddingUnknownUser(count);
            }
        });
        alertDialogBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        return alertDialogBuilder.create();
    }

}
