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
 * Created by jawa on 10/28/2015.
 */
public class Monthly_Report_CSV extends Activity {

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
        File file = new File(exportDir, "csvmonthlycash.csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file), ',', ' ', "\n");
            SQLiteDatabase db = dbhelper1.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM TableDailyPurchase WHERE Cashcreatedatdate = date('now','-30 days')", null);
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
            Log.e("Monthly_Report_CSV", sqlEx.getMessage(), sqlEx);
        }
        Toast.makeText(Monthly_Report_CSV.this, "Monthly Report Report has been saved successfully", Toast.LENGTH_SHORT).show();
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
                    mDbHelper = new CustomersDbAdapter(Monthly_Report_CSV.this);
                    mDbHelper.open();

                    Cursor c1 = mDbHelper.getgmail();
                    if(c1.moveToFirst()){
                        do{
                            gmailId = (c1.getString(2)).toString();
                            password =(c1.getString(3)).toString();
                        }while(c1.moveToNext());
                    }

                    int Totalmonthlyexpense = 0,Totalmonthlyrevenues = 0,Totalmonthlycredit = 0, Totalmonthlycashaftercredit = 0;

                    Totalmonthlyexpense = mDbHelper.getSumOfAllAvgofexpensesofaMonth("expenses", "expenses_created_at");
                    Totalmonthlyrevenues = mDbHelper.getSumOfAllAvgofmonth("CashAmount", "CashInfo", "Cashcreatedatdate");
                    Totalmonthlycredit = mDbHelper.getSumOfAllAvgofcolumn("amount", "CustomerInfo");

                    Totalmonthlycashaftercredit = Totalmonthlycredit + Totalmonthlyrevenues;

                    GMailSender sender = new GMailSender(gmailId, password);
                    sender.sendMail("This is an email sent by ColdStoreApp from an Android device.",
                            "Month Report",
                            "ColdStoreApp", gmailId,
                            "/sdcard/ColdStoreApp/csvmonthlycash.csv", "\t\t\t\t\tMonthly Business Review" +
                                    "\n\t\t\t\t\tExecutive Summary" +
                                    "\n\n\tExpense Report" +
                                    "\n\n\tMonthly Expense : "+Totalmonthlyexpense +"" +
                                    "\n\n\tMonthly Revenues : "+Totalmonthlyrevenues +"" +
                                    "\n\n\tMonthly Revenues after credit cleared : "+Totalmonthlycashaftercredit +"" +
                                    "\n\n\n\n\nPlease find the attachment of Monthly Cash Purchase Report below ");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Monthly_Report_CSV.this,
                                    "Monthly report request is sent successfully", Toast.LENGTH_LONG).show();
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
