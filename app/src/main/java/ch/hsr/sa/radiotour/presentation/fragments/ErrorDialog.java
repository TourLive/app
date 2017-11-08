package ch.hsr.sa.radiotour.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import ch.hsr.sa.radiotour.R;

public class ErrorDialog extends DialogFragment {

    public ErrorDialog(){}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.error)
                .setTitle(R.string.import_error)
                .setMessage(getArguments().getString("message"))
                .setPositiveButton("OK", (DialogInterface dialog, int which) -> {})
                .create();
    }
}
