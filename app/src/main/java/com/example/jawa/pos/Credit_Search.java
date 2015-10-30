package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by jawa on 10/13/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Credit_Search extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    ImageButton cancel;
    private ListView mListView;
    private SearchView searchView;
    private CustomersDbAdapter mDbHelper;
    private TextView customerText;
    private TextView nameText;
    private TextView addressText;
    private TextView cityText;
    private TextView stateText;
    public TextView zipCodeText;
    public TextView amountText;
    EditText newcomment,newam;
    private RadioGroup radioGroup;
    RadioButton addto;
    RadioButton detract;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit);

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        mDbHelper = new CustomersDbAdapter(this);
        mDbHelper.open();
        mListView = (ListView) findViewById(R.id.list);

        cancel = (ImageButton) findViewById(R.id.goback);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Initialize Radio Group and attach click handler */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        addto = (RadioButton) findViewById(R.id.addto);
        detract = (RadioButton) findViewById(R.id.detract);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDbHelper  != null) {
            mDbHelper.close();
        }
    }

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

        Cursor cursor = mDbHelper.searchCustomer((query != null ? query.toString() : "@@@@"));

        if (cursor == null) {
            //
        } else {
            // Specify the columns we want to display in the result
            String[] from = new String[] {
                    CustomersDbAdapter.KEY_CUSTOMER,
                    CustomersDbAdapter.KEY_NAME,
//                    CustomersDbAdapter.KEY_ADDRESS,
                    CustomersDbAdapter.KEY_CITY,
                    CustomersDbAdapter.KEY_STATE,
                    CustomersDbAdapter.KEY_ZIP};

            // Specify the Corresponding layout elements where we want the columns to go
            int[] to = new int[] {     R.id.scustomer,
                    R.id.sname,
//                    R.id.saddress,
                    R.id.scity,
                    R.id.sstate,
                    R.id.szipCode
                    };

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter customers = new SimpleCursorAdapter(this,R.layout.customerresult, cursor, from, to);
            mListView.setAdapter(customers);

            // Define the on-click listener for the list items
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) mListView.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String customer = cursor.getString(cursor.getColumnIndexOrThrow("account_No"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("phone_No1"));
//                    String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("email_Id"));
                    String state = cursor.getString(cursor.getColumnIndexOrThrow("names"));
                    String zipCode = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
                    String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));

                    //Check if the Layout already exists
                    LinearLayout customerLayout = (LinearLayout)findViewById(R.id.customerLayout);
                    if(customerLayout == null){
                        //Inflate the Customer Information View
                        LinearLayout leftLayout = (LinearLayout)findViewById(R.id.rightLayout);
                        View customerInfo = getLayoutInflater().inflate(R.layout.customerinfo, leftLayout, false);
                        leftLayout.addView(customerInfo);
                    }

                    //Get References to the TextViews
                    customerText = (TextView) findViewById(R.id.customer);
                    nameText = (TextView) findViewById(R.id.name);
//                    addressText = (TextView) findViewById(R.id.address);
                    cityText = (TextView) findViewById(R.id.city);
                    stateText = (TextView) findViewById(R.id.state);
                    zipCodeText = (TextView) findViewById(R.id.zipCode);
                    amountText = (TextView) findViewById(R.id.amount1);

                    // Update the parent class's TextView
                    customerText.setText(customer);
                    nameText.setText(name);
//                    addressText.setText(address);
                    cityText.setText(city);
                    stateText.setText(state);
                    zipCodeText.setText(zipCode);
                    amountText.setText(amount);
                    searchView.setQuery("",true);
                }
            });
        }
    }

    public void savechange(View view) {
        try {
            newcomment = (EditText) findViewById(R.id.newcomment);
            String newc1 = newcomment.getText().toString();
            newam = (EditText) findViewById(R.id.newamount);
            int newamount1 = Integer.parseInt(newam.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(Credit_Search.this,"Invalid input",Toast.LENGTH_SHORT).show();
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
                        newcomment = (EditText) findViewById(R.id.newcomment);
                        TextView tvamount = (TextView) findViewById(R.id.amount1);
                        newam = (EditText) findViewById(R.id.newamount);
                        TextView tv = (TextView) findViewById(R.id.customer);

                        try {
                            int newamount = Integer.parseInt(newam.getText().toString());
                            String newc = newcomment.getText().toString();
                            int amounttv = Integer.parseInt(tvamount.getText().toString());
                            String acc = tv.getText().toString();
                            int selectedId = radioGroup.getCheckedRadioButtonId(),i = 0;
                            // find which radioButton is checked by id
                            if(selectedId == addto.getId()) {
                                i = amounttv + newamount ;
                            } else if(selectedId == detract.getId()) {
                                i = amounttv - newamount;
                                mDbHelper.cashCustomer(newamount,newc);
                            }
                            mDbHelper.updateComment(acc, i, newc);
                            Toast.makeText(Credit_Search.this, "Your changes have been saved",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Toast.makeText(Credit_Search.this,"Invalid input",Toast.LENGTH_SHORT).show();
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
        alert.show();

    }
}
