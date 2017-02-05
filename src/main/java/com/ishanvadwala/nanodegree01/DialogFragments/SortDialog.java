package com.ishanvadwala.nanodegree01.DialogFragments;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by ishanvadwala on 10/20/16.
 */
public class SortDialog extends DialogFragment {
    int optionSelected;

    public SortDialog(){}

    public interface SortDialogChangeListener{
         void optionSelected(int option);
    }
    public static SortDialog newInstance(String title) {
        
        Bundle args = new Bundle();
        args.putString("title",title);
        SortDialog fragment = new SortDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        CharSequence[] optionsArray = new CharSequence[]{"By popularity", "By Ratings"};
        alertDialogBuilder.setSingleChoiceItems(optionsArray, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               Log.d("Option selected is:",String.valueOf(which));
                sendBackResult(which);
            }
        });
        return alertDialogBuilder.create();
    }

    public void sendBackResult(int option) {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        SortDialogChangeListener listener = (SortDialogChangeListener) getTargetFragment();
        listener.optionSelected(option);
        dismiss();
    }


}
