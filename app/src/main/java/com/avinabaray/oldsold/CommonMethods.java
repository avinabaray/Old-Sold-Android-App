package com.avinabaray.oldsold;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class CommonMethods {

    ProgressDialog pd;

    public void createAlert(AlertDialog.Builder alertDialogBuilder, String msg) {
        alertDialogBuilder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void loadingDialogStart(Context context) {
        pd = new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.show();
    }

    public void loadingDialogStop() {
        pd.dismiss();
    }

}
