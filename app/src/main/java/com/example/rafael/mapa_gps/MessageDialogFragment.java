package com.example.rafael.mapa_gps;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by rafael on 11/10/2016.
 */

public class MessageDialogFragment extends DialogFragment {
    private Dialog mDialog;

    public MessageDialogFragment() {
        super();
        mDialog = null;
        setRetainInstance(true);
    }

    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }

}
