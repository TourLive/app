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

        final SeekBar seekBar = (SeekBar) dialogView.findViewById(R.id.countUnknownRiders);
        final TextView textView = (TextView) dialogView.findViewById(R.id.txtDialogUnknownRiders);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText("" + Integer.toString(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing because method has to be implemented, but not needed.
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing because method has to be implemented, but not needed.
            }
        });

        alertDialogBuilder.setTitle("Adding Unknown Riders");
        alertDialogBuilder.setMessage("Please select how many unknown riders you would add and select the racegroup.");
        alertDialogBuilder.setPositiveButton("Add unknown riders", (DialogInterface dialog, int which) ->  {
           UnknownUserAddListener unknownUserAddListener = (UnknownUserAddListener) getTargetFragment();
           int count = seekBar.getProgress();
           unknownUserAddListener.onFinishAddingUnknownUser(count);
        });

        alertDialogBuilder.setNegativeButton("Dismiss", (DialogInterface dialog, int which) -> {
           dialog.dismiss();
        });

        return alertDialogBuilder.create();
    }

}
