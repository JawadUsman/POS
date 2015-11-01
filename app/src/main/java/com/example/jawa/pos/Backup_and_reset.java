package com.example.jawa.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jawa on 11/1/2015.
 */
public class Backup_and_reset extends Activity {
    private CustomersDbAdapter dbHelper;
    ImageButton cancel;
    TextView t1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backupandrest);
        cancel = (ImageButton) findViewById(R.id.gobackb);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbHelper = new CustomersDbAdapter(this);
        dbHelper.open();
        t1 = (TextView)findViewById(R.id.textView28);
        Cursor c1 = dbHelper.getgmail();
        if(c1.moveToFirst()){
            do{
                String gmail = (c1.getString(2)).toString();
                t1.setText(gmail);
            }while(c1.moveToNext());
        }
        dbHelper.close();
    }
    public void reset(View view) {

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setTitle("Data reset");
        dlgAlert.setMessage("All data will be erased from device");
        // setup a dialog window
        dlgAlert.setCancelable(false)
                .setPositiveButton("Reset device",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dbHelper.clearAll();
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create an alert dialog
        AlertDialog alert = dlgAlert.create();
        alert.show();
    }
}
