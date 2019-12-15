package com.avinabaray.oldsold;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class CommonMethods {

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

}
