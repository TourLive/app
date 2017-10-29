package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import ch.hsr.sa.radiotour.R;
import ch.hsr.sa.radiotour.dataaccess.models.RaceGroup;

/**
 * Created by Urs Forrer on 29.10.2017.
 */

public class UnknownRiderTransferDialogFramgent extends DialogFragment {
    public UnknownRiderTransferDialogFramgent() {

    }

    public interface UnknownUserAddListener {
        void onFinishAddingUnknownUser(int count);
    }

    public static UnknownRiderTransferDialogFramgent newInstance() {
        UnknownRiderTransferDialogFramgent frag = new UnknownRiderTransferDialogFramgent();
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_unknowntransfer, null);
        alertDialogBuilder.setView(dialogView);
        final TextView textView = (TextView) dialogView.findViewById(R.id.txtDialogUnknownRiders);

        alertDialogBuilder.setTitle("Change unknown rider to an known rider");
        alertDialogBuilder.setMessage("Please select the rider to which you want transfer the unknown rider to.");
        alertDialogBuilder.setPositiveButton("Change the unknown rider",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
