package com.ishanvadwala.nanodegree01.DialogFragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by ishanvadwala on 11/24/16.
 */
public class AboutDialogFragment extends DialogFragment{

    public static AboutDialogFragment newInstance() {

        Bundle args = new Bundle();

        AboutDialogFragment fragment = new AboutDialogFragment();
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("About");
        builder.setMessage("The movies in these app" +
                " are provided by TheMovieDatabase's API");
        builder.setNeutralButton("Okay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();
    }
}
