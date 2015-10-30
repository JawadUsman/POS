package com.example.jawa.pos;

/**
 * Created by jawa on 10/13/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CustomersDbAdapter {


    public static final String DATABASE_NAME = "CustomerData";
    public static final int DATABASE_VERSION = 1;

    public static final String FTS_VIRTUAL_TABLE = "CustomerInfo";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_CUSTOMER = "account_No";
    public static final String KEY_NAME = "phone_No1";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ADDRESS1 = "name";
    public static final String KEY_ADDRESS2 = "phone_No2";
    public static final String KEY_CITY = "email_Id";
    public static final String KEY_STATE = "names";
    public static final String KEY_ZIP = "comment";
    public static final String KEY_AMOUNT= "amount";
    public static final String KEY_SEARCH = "searchData";

    public static final String TAG = "CustomersDbAdapter";
    public DatabaseHelper mDbHelper;
    public SQLiteDatabase mDb;

    public static final String CASH_TABLE = "CashInfo";
    public static final String CASH_AMOUNT = "CashAmount";
    public static final String CASH_COMMMENT = "Cashcomment";
    public static final String CASH_CREATED_AT = "Cashcreatedat";
    public static final String CASH_CREATED_AT_DATE = "Cashcreatedatdate";

    public static final String DIALY_PURCHASE_CASH_TABLE = "TableDailyPurchase";
    public static final String CASH_PURCHASE_COMPANY = "companyName";
    public static final String CASH_PURCHASE_AMOUNT = "cashinneramount";

    public static final String CREATE_DIALY_PURCHASE_CASH = "CREATE TABLE "+DIALY_PURCHASE_CASH_TABLE+"" +
            "("+KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            ""+CASH_PURCHASE_COMPANY+" VARCHAR2(255)," +
            ""+CASH_PURCHASE_AMOUNT+" INTEGER," +
            ""+CASH_CREATED_AT+"," +
            ""+CASH_CREATED_AT_DATE+"," +
            "UNIQUE (" + KEY_ROWID +"))";

    public static final String DIALY_PURCHASE_CREDIT_TABLE = "TableDailyPurchasecredit";
    public static final String CREDIT_PURCHASE_COMPANY = "creditcompanyName";
    public static final String CREDIT_PURCHASE_INVOICENO = "invoiceno";
    public static final String CREDIT_PURCHASE_AMOUNT = "creditinneramount";
    public static final String CREDIT_PURCHASE_PAYMENTDATE = "paymentdate";
    public static final String CREDIT_CREATED_AT = "Creditcreatedat";
    public static final String KEY_SEARCH_CREDIT = "searchDataCredit";

    public static final String CREATE_DIALY_PURCHASE_CREDIT = "CREATE VIRTUAL TABLE "+DIALY_PURCHASE_CREDIT_TABLE+" USING fts3" +
            "("+KEY_ROWID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            CREDIT_PURCHASE_INVOICENO+"," +
            CREDIT_PURCHASE_COMPANY+" VARCHAR2(255)," +
            CREDIT_PURCHASE_AMOUNT+" INTEGER," +
            CREDIT_CREATED_AT+" TIME DEFAULT CURRENT_DATE," +
            KEY_SEARCH_CREDIT + "," +
            CREDIT_PURCHASE_PAYMENTDATE+" INTEGER ," +
            "UNIQUE (" + CREDIT_PURCHASE_INVOICENO +"))";

    //Create a FTS3 Virtual Table for fast searches
    public static final String DATABASE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3(" +
                    KEY_ROWID +" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_CUSTOMER + "," +
                    KEY_NAME + "," +
                    KEY_ADDRESS1 + "," +
                    KEY_ADDRESS2 + "," +
                    KEY_CITY + "," +
                    KEY_STATE + "," +
                    KEY_ZIP + "," +
                    KEY_SEARCH + "," +
                    KEY_AMOUNT + "," +
                    " UNIQUE (" + KEY_CUSTOMER + "));";

    public static final String CREATE_CASE = "CREATE VIRTUAL TABLE "+CASH_TABLE+" USING fts3("+CASH_AMOUNT+" INTEGER PRIMARY KEY," +
            CASH_COMMMENT+"," +
            CASH_CREATED_AT+" DATETIME DEFAULT CURRENT_TIMESTAMP,"+
            CASH_CREATED_AT_DATE+","+
            " UNIQUE ( "+ CASH_CREATED_AT + "));";

    public static final String EXPENSES_TABLE = "expenses";
    public static final String SHOP_RENT = "shoprent";
    public static final String HOUSE_RENT = "houserent";
    public static final String MAINTAIN = "maintain";
    public static final String MEDICAL = "medical";
    public static final String GOV_APP_FEES = "government_applicable_fees";
    public static final String OTHER_BILL = "otherbill";
    public static final String TOTAL_BILL = "totalbill";
    public static final String EXPENSES_CREATED_AT = "expenses_created_at";
    public static final String CREATE_EXPENSES = "CREATE VIRTUAL TABLE "+EXPENSES_TABLE+" " +
            " USING fts3("+ EXPENSES_CREATED_AT+","+
            SHOP_RENT+","+
            HOUSE_RENT+","+
            MAINTAIN+","+
            MEDICAL+","+
            GOV_APP_FEES+","+
            OTHER_BILL+","+
            TOTAL_BILL+","+
            " UNIQUE (" + EXPENSES_CREATED_AT + "));";

    public final Context mCtx;
    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
            db.execSQL(CREATE_CASE);
            db.execSQL(CREATE_DIALY_PURCHASE_CASH);
            db.execSQL(CREATE_DIALY_PURCHASE_CREDIT);
            db.execSQL(CREATE_EXPENSES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CASH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DIALY_PURCHASE_CASH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DIALY_PURCHASE_CREDIT_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE);
            onCreate(db);
        }
    }

    public CustomersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CustomersDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    public long cashCustomer(int cashamount, String commenet) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        ContentValues initialValues = new ContentValues();
        initialValues.put(CASH_AMOUNT, cashamount);
        initialValues.put(CASH_COMMMENT, commenet);
        initialValues.put(CASH_CREATED_AT_DATE, date);
        long db1 = mDb.insert(CASH_TABLE, null, initialValues);
        return  db1;
    }

    public long createCustomer(String customer, String name, String address1,
                               String address2, String city,
                               String state, String zipCode, String amount) {

        ContentValues initialValues = new ContentValues();
        String searchValue =     customer + " " +
                name + " " +
                address1 + " " +
                city + " " +
                state + " " +
                zipCode + " " +
                amount;
        initialValues.put(KEY_CUSTOMER, customer);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_ADDRESS1, address1);
        initialValues.put(KEY_ADDRESS2, address2);
        initialValues.put(KEY_CITY, city);
        initialValues.put(KEY_STATE, state);
        initialValues.put(KEY_ZIP, zipCode);
        initialValues.put(KEY_AMOUNT, amount);
        initialValues.put(KEY_SEARCH, searchValue);

        return mDb.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }

    public long createexpenses(int shop, int house,int ma, int med, int gafee,int other, int totlebill) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        ContentValues initialValues = new ContentValues();
        initialValues.put(EXPENSES_CREATED_AT, date);
        initialValues.put(SHOP_RENT, shop);
        initialValues.put(HOUSE_RENT, house);
        initialValues.put(MAINTAIN, ma);
        initialValues.put(MEDICAL, med);
        initialValues.put(GOV_APP_FEES, gafee);
        initialValues.put(OTHER_BILL, other);
        initialValues.put(TOTAL_BILL, totlebill);
        long db2 = mDb.insert(EXPENSES_TABLE, null, initialValues);
        return  db2;
    }

    public long dialycash(String name, int amount ){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        String aTime = updateTime();
        ContentValues initialValues = new ContentValues();
        initialValues.put(CASH_PURCHASE_COMPANY, name);
        initialValues.put(CASH_PURCHASE_AMOUNT, amount);
        initialValues.put(CASH_CREATED_AT, aTime);
        initialValues.put(CASH_CREATED_AT_DATE, date);
        return mDb.insert(DIALY_PURCHASE_CASH_TABLE, null, initialValues);
    }

    public Cursor queueAll() {
        String[] col =  new String[] {KEY_ROWID,
                CASH_PURCHASE_AMOUNT, CASH_PURCHASE_COMPANY, CASH_CREATED_AT};
        Cursor mCursor = mDb.query(DIALY_PURCHASE_CASH_TABLE, col,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long dialycredit(String invoiceno, String companyname, int amount, String duedate ){

        ContentValues initialValues = new ContentValues();
        String searchValue =     invoiceno + " " +
                companyname + " " +
                amount + " " +
                duedate;
        initialValues.put(CREDIT_PURCHASE_INVOICENO, invoiceno);
        initialValues.put(CREDIT_PURCHASE_COMPANY, companyname);
        initialValues.put(CREDIT_PURCHASE_AMOUNT, amount);
        initialValues.put(CREDIT_PURCHASE_PAYMENTDATE, duedate);
        initialValues.put(KEY_SEARCH_CREDIT, searchValue);
        return mDb.insert(DIALY_PURCHASE_CREDIT_TABLE, null, initialValues);
    }
    public long dialycreditupdate(String invoiceno, int amount, String duedate ){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues initialValues = new ContentValues();
        initialValues.put(CREDIT_PURCHASE_AMOUNT, amount);
        initialValues.put(CREDIT_PURCHASE_PAYMENTDATE, duedate);
        int countcr = db.update(DIALY_PURCHASE_CREDIT_TABLE, initialValues, CREDIT_PURCHASE_INVOICENO
                + " =? ", new String[]{invoiceno});
        return countcr;
    }

    public Cursor queueAllCredit() {
        String[] col =  new String[] {KEY_ROWID,
                CREDIT_PURCHASE_INVOICENO, CREDIT_PURCHASE_COMPANY, CREDIT_PURCHASE_AMOUNT,CREDIT_CREATED_AT,
                CREDIT_PURCHASE_PAYMENTDATE};
        Cursor mCursor = mDb.query(DIALY_PURCHASE_CREDIT_TABLE, col,
                null, null, null, null, CREDIT_PURCHASE_PAYMENTDATE + " ASC");
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor queueAllCreditCustomer() {
        String[] col =  new String[] {KEY_ROWID,KEY_CUSTOMER,
                KEY_ADDRESS1,KEY_NAME,KEY_CITY,KEY_ZIP,KEY_AMOUNT};
        Cursor mCursor = mDb.query(FTS_VIRTUAL_TABLE, col,
                null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(DIALY_PURCHASE_CREDIT_TABLE, new String[] {KEY_ROWID,
                            CREDIT_PURCHASE_INVOICENO, CREDIT_PURCHASE_COMPANY,
                            CREDIT_PURCHASE_AMOUNT,CREDIT_CREATED_AT,
                            CREDIT_PURCHASE_PAYMENTDATE},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, DIALY_PURCHASE_CREDIT_TABLE, new String[] {KEY_ROWID,
                            CREDIT_PURCHASE_INVOICENO, CREDIT_PURCHASE_COMPANY,
                            CREDIT_PURCHASE_AMOUNT,CREDIT_CREATED_AT,
                            CREDIT_PURCHASE_PAYMENTDATE},
                    CREDIT_PURCHASE_COMPANY + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public  int updateComment(String accno, int amount, String newComment){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ZIP, newComment);
        contentValues.put(KEY_AMOUNT, amount);
        int count = db.update(FTS_VIRTUAL_TABLE, contentValues, KEY_CUSTOMER + " =? ", new String[]{accno});
        return count;
    }

    public Cursor searchCustomer(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT docid as _id," +
                KEY_CUSTOMER + "," +
                KEY_NAME + "," +
                "(" + KEY_ADDRESS1 + "||" +
                "(case when " + KEY_ADDRESS2 +  "> '' then '\n' || " + KEY_ADDRESS2 + " else '' end)) as " +  KEY_ADDRESS +"," +
                KEY_ADDRESS1 + "," +
                KEY_ADDRESS2 + "," +
                KEY_CITY + "," +
                KEY_STATE + "," +
                KEY_ZIP + "," +
                KEY_AMOUNT +
                " from " + FTS_VIRTUAL_TABLE +
                " where " +  KEY_SEARCH + " MATCH '" + inputText + "';";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor searchCreditPurchase(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        String query = "SELECT docid as _id," +
                KEY_ROWID + "," +
                CREDIT_PURCHASE_INVOICENO + "," +
                CREDIT_PURCHASE_COMPANY + "," +
                CREDIT_PURCHASE_PAYMENTDATE + "," +
                CREDIT_PURCHASE_AMOUNT +
                " from " + DIALY_PURCHASE_CREDIT_TABLE +
                " where " + KEY_SEARCH_CREDIT + " MATCH '" + inputText + "';";
        Log.w(TAG, query);
        Cursor mCursor = mDb.rawQuery(query, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public int getSumOfAllAvgofcolumn(String colname,String TableName) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM("+ colname +") FROM "+TableName+"";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }

    public int getSumOfAllAvgofprev(String colname,String TableName,String dateprev) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM("+ colname +") FROM "+TableName+" WHERE "+dateprev+" = date('now','-1 days')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }

    public int getSumOfAllAvgofmonth(String colname,String TableName,String dateprev) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM("+ colname +") FROM "+TableName+" WHERE "+dateprev+" = date('now','-30 days')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofyear(String colname,String TableName,String dateprev) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM("+ colname +") FROM "+TableName+" WHERE "+dateprev+" = date('now','-365 days')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofaday(String colname,String TableName,String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM("+ colname +") FROM "+TableName+" WHERE "+date+" = date('now')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofexpensesofaday(String tablename,String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT  SUM(totalbill)  FROM "+tablename+" WHERE "+date+" = date('now')";
            Cursor c = db.rawQuery(query, null);
            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofexpensesofaMonth(String TableName,String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM(totalbill) FROM "+TableName+" WHERE "+date+" = date('now','-30 days')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofexpensesofayear(String TableName,String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT SUM(totalbill) FROM "+TableName+" WHERE "+date+" = date('now','-365 days')";
            Cursor c = db.rawQuery(query, null);

            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    public int getSumOfAllAvgofexpensesofaprevday(String tablename,String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try {
            String query = "SELECT  SUM(totalbill)  FROM "+tablename+" WHERE "+date+" = date('now','-1 days')";
            Cursor c = db.rawQuery(query, null);
            //Add in the movetofirst etc here? see SO
            c.moveToFirst();
            int i=c.getInt(0);
            return i;
        } finally {
            db.close();
        }
    }
    private String updateTime() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int mint =c.get(Calendar.MINUTE);
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mint < 10)
            minutes = "0" + mint;
        else
            minutes = String.valueOf(mint);
        String aTime = new StringBuilder().append(hour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }

    public void deleteUser(String accNo)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        try
        {
            db.delete(FTS_VIRTUAL_TABLE, "account_No = ?", new String[] { accNo });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    public boolean deleteAlldailycashdayend() {

        int doneDelete = 0;
        doneDelete = mDb.delete(FTS_VIRTUAL_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }
    public boolean deletearow(String name){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(DIALY_PURCHASE_CASH_TABLE, CASH_PURCHASE_COMPANY + "=" + name, null) > 0;
    }

}

