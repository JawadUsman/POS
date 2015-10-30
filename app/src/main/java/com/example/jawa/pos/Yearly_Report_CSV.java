package com.example.jawa.pos;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * Created by jawa on 10/29/2015.
 */
public class Yearly_Report_CSV extends Activity {

    private CustomersDbAdapter mDbHelper;
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
        File file = new File(exportDir, "csvyearlycash.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), ',', ' ', "\n");
            SQLiteDatabase db = dbhelper1.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM TableDailyPurchase WHERE Cashcreatedatdate = date('now','-365 days')", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                //Which column you want to exprort
                String arrStr[] = {curCSV.getString(0),
                        curCSV.getString(1),
                        curCSV.getString(2),
                        curCSV.getString(3),
                        curCSV.getString(4)
                };
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
        } catch (Exception sqlEx) {
            Log.e("Yearly_Report_CSV", sqlEx.getMessage(), sqlEx);
        }
        Toast.makeText(Yearly_Report_CSV.this, "Yearly Report Report has been saved successfully", Toast.LENGTH_SHORT).show();
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
                    mDbHelper = new CustomersDbAdapter(Yearly_Report_CSV.this);
                    mDbHelper.open();

                    int Totalyearlyexpense = 0,Totalyearlyrevenues = 0,Totalyearlycredit = 0, Totalyearlycashaftercredit = 0;

                    Totalyearlyexpense = mDbHelper.getSumOfAllAvgofexpensesofayear("expenses", "expenses_created_at");
                    Totalyearlyrevenues = mDbHelper.getSumOfAllAvgofyear("CashAmount", "CashInfo", "Cashcreatedatdate");
                    Totalyearlycredit = mDbHelper.getSumOfAllAvgofcolumn("amount", "CustomerInfo");

                    Totalyearlycashaftercredit = Totalyearlycredit + Totalyearlyrevenues;

                    GMailSender sender = new GMailSender("jawafacer@gmail.com",
                            "17121992");
                    sender.sendMail("This is an email sent by ColdStoreApp from an Android device.",
                            "Year Report",
                            "ColdStoreApp", "jawafacer@gmail.com",
                            "/sdcard/ColdStoreApp/csvyearlycash.csv", "\t\t\t\t\tYearly Business Review" +
                                    "\n\t\t\t\t\tExecutive Summary" +
                                    "\n\n\tExpense Report" +
                                    "\n\n\tYearly Expense : "+Totalyearlyexpense +"" +
                                    "\n\n\tYearly Revenues : "+Totalyearlyrevenues +"" +
                                    "\n\n\tYearly Revenues after credit cleared : "+Totalyearlycashaftercredit +"" +
                                    "\n\n\n\n\nPlease find the attachment of yearly Cash Purchase Report below ");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Yearly_Report_CSV.this,
                                    "Yearly report request is sent successfully", Toast.LENGTH_LONG).show();
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
