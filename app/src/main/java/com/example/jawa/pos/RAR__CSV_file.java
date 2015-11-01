package com.example.jawa.pos;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;

import javax.mail.MessagingException;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by jawa on 10/22/2015.
 */
public class RAR__CSV_file extends Activity {
    private CustomersDbAdapter mDbHelper;
    private String gmailId,password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File dbFile = getDatabasePath("CustomerData");
        CustomersDbAdapter dbhelper = new CustomersDbAdapter(getApplicationContext());
        CustomersDbAdapter.DatabaseHelper dbhelper1 = new CustomersDbAdapter.DatabaseHelper(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "ColdStoreApp");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, "csvcreditcustomer.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), ',', ' ', "\n");
            SQLiteDatabase db = dbhelper1.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM CustomerInfo ", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4),
                        curCSV.getString(5),
                        curCSV.getString(6),
                        curCSV.getString(7),
                        curCSV.getString(8),
                        curCSV.getString(9)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("RAR_CSV_file", sqlEx.getMessage(), sqlEx);
        }
        Toast.makeText(RAR__CSV_file.this, "Registered Account Report has been saved successfully", Toast.LENGTH_SHORT).show();
        new Connections().execute();
    }

    private class Connections extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg0) {
            connects();
            return null;
        }
    }
    private void connects() {
        /// / TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mDbHelper = new CustomersDbAdapter(RAR__CSV_file.this);
                    mDbHelper.open();

                    Cursor c1 = mDbHelper.getgmail();
                    if(c1.moveToFirst()){
                        do{
                            gmailId = (c1.getString(2)).toString();
                            password =(c1.getString(3)).toString();
                        }while(c1.moveToNext());
                    }

                    GMailSender sender = new GMailSender(gmailId, password);
                    sender.sendMail("This is an email sent by ColdStoreApp from an Android device.",
                            "Registered Account Report",
                            "ColdStoreApp", gmailId,
                            "/sdcard/ColdStoreApp/csvcreditcustomer.csv","Please find the attachment of Registered account " +
                                    "Report below ");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(RAR__CSV_file.this,
                                    "Registered Account Report request is sent successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
        finish();
    }
}


