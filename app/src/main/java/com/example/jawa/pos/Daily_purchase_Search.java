package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jawa on 10/25/2015.
 */
public class Daily_purchase_Search extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ListView crListView;
    private SearchView crsearchView;
    private CustomersDbAdapter mDbHelper;
    private TextView invoiceNoText ;
    private TextView companyNameText;
    private TextView paymentDateText;
    private TextView innerAmountText;
    EditText crnewdate, crnewam;
    private RadioGroup crradioGroup;
    RadioButton craddto;
    RadioButton crdetract;
    ImageButton cancel;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_purchase_crdit_serch_inner);

        crsearchView = (SearchView) findViewById(R.id.crsearch);
        crsearchView.setIconifiedByDefault(false);
        crsearchView.setOnQueryTextListener(this);
        crsearchView.setOnCloseListener(this);

        mDbHelper = new CustomersDbAdapter(this);
        mDbHelper.open();
        crListView = (ListView) findViewById(R.id.crlist);

        cancel = (ImageButton) findViewById(R.id.crgoback);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

         /* Initialize Radio Group and attach click handler */
        crradioGroup = (RadioGroup) findViewById(R.id.crradioGroup);
        crradioGroup.clearCheck();

        /* Attach CheckedChangeListener to radio group */
        crradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (checkedId == R.id.addto) {
                    Toast.makeText(getApplicationContext(), "Add to", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.detract) {
                    Toast.makeText(getApplicationContext(), "Detract", Toast.LENGTH_SHORT).show();
                }
            }
        });
        craddto = (RadioButton) findViewById(R.id.craddto);
        crdetract = (RadioButton) findViewById(R.id.crdetract);
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        showResults(newText + "*");
        return false;
    }

    public boolean onQueryTextSubmit(String query) {
        showResults(query + "*");
        return false;
    }

    public boolean onClose() {
        showResults("");
        return false;
    }

    private void showResults(String query) {

        Cursor cursor = mDbHelper.searchCreditPurchase((query != null ? query.toString() : "@@@@"));
//        Cursor cursor = mDbHelper.searchCreditPurchase(query);

        if (cursor == null) {
            //
        } else {
            // Specify the columns we want to display in the result
            String[] from = new String[] {
                    CustomersDbAdapter.CREDIT_PURCHASE_INVOICENO,
                    CustomersDbAdapter.CREDIT_PURCHASE_COMPANY,
                    CustomersDbAdapter.CREDIT_PURCHASE_PAYMENTDATE,
                    CustomersDbAdapter.CREDIT_PURCHASE_AMOUNT};

            // Specify the Corresponding layout elements where we want the columns to go
            int[] to = new int[] {     R.id.crscustomer,
                    R.id.crsstate,
                    R.id.crsname,
                    R.id.crscity};
            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter customers = new SimpleCursorAdapter(this,R.layout.daily_purchase_credit_reslutlistview,
                    cursor, from, to,0);
            crListView.setAdapter(customers);

            // Define the on-click listener for the list items
            crListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) crListView.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String invoiceNo = cursor.getString(cursor.getColumnIndexOrThrow("invoiceno"));
                    String companyName = cursor.getString(cursor.getColumnIndexOrThrow("creditcompanyName"));
                    String paymentDate = cursor.getString(cursor.getColumnIndexOrThrow("paymentdate"));
                    String innerAmount = cursor.getString(cursor.getColumnIndexOrThrow("creditinneramount"));

                    //Check if the Layout already exists
                    LinearLayout customerLayout = (LinearLayout) findViewById(R.id.customerLayout);
                    if (customerLayout == null) {
                        //Inflate the Customer Information View
                        LinearLayout leftLayout = (LinearLayout) findViewById(R.id.rightLayout);
                        View customerInfo = getLayoutInflater().inflate(R.layout.daily_purchase_credit_infolistview,
                                leftLayout, false);
                        leftLayout.addView(customerInfo);
                    }

                    //Get References to the TextViews
                    invoiceNoText = (TextView) findViewById(R.id.crinvoice);
                    companyNameText = (TextView) findViewById(R.id.crcompanyname);
                    paymentDateText = (TextView) findViewById(R.id.crpaymentdate);
                    innerAmountText = (TextView) findViewById(R.id.cramount);

                    // Update the parent class's TextView
                    invoiceNoText.setText(invoiceNo);
                    companyNameText.setText(companyName);
                    paymentDateText.setText(paymentDate);
                    innerAmountText.setText(innerAmount);
                    crsearchView.setQuery("", true);
                }
            });
        }
    }
    public void savechange(View view) {

        try {
            crnewdate = (EditText) findViewById(R.id.crewdate);
            crnewam = (EditText) findViewById(R.id.crnewamount);
            int  newamount1 = Integer.parseInt(crnewam.getText().toString());
            String newduedate1 = crnewdate.getText().toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(Daily_purchase_Search.this,"Invalid input",Toast.LENGTH_SHORT).show();
            finish();
        }
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setTitle("Save");
        dlgAlert.setMessage(" Do you want to save those changes?");

        // setup a dialog window
        dlgAlert.setCancelable(false)
                .setPositiveButton("Save",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        TextView tv =  tv = (TextView) findViewById(R.id.crcompanyname);
                        TextView tv2 =  tv2 = (TextView) findViewById(R.id.crinvoice);
                        TextView tvamount = tvamount = (TextView) findViewById(R.id.cramount);
                        crnewdate = (EditText) findViewById(R.id.crewdate);
                        crnewam = (EditText) findViewById(R.id.crnewamount);
                        String companyname = null;
                        String invoiceNo = null;
                        int amounttv = 0;
                        int newamount = 0;
                        String newduedate = null;
                        int xy = 0;
                        try {
                            companyname = tv.getText().toString();
                            invoiceNo = tv2.getText().toString();
                            amounttv = Integer.parseInt(tvamount.getText().toString());
                            newamount = Integer.parseInt(crnewam.getText().toString());
                            newduedate = crnewdate.getText().toString();
                            xy = newamount;

                            int selectedId = crradioGroup.getCheckedRadioButtonId(),i = 0;
                            // find which radioButton is checked by id
                            if(selectedId == craddto.getId()) {
                                i = amounttv + newamount;
                                mDbHelper.dialycreditupdate(invoiceNo, i, newduedate);
                            } else if(selectedId == crdetract.getId()) {
                                i = amounttv - newamount;
                                mDbHelper.dialycash(companyname, xy);
                                if(i <= amounttv) {
                                    mDbHelper.dialycreditupdate(invoiceNo, i, newduedate);
                                }else
                                {
                                    mDbHelper.dialycreditupdate(invoiceNo, 0, null);
                                }
                                Toast.makeText(Daily_purchase_Search.this, "Your changes have been saved",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(Daily_purchase_Search.this,"Invalid input",Toast.LENGTH_SHORT).show();
                        }
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
        try {
            alert.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
