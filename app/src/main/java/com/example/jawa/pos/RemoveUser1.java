package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jawa on 10/7/2015.
 */
public class RemoveUser1 extends Activity implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private ListView mListView;
    private SearchView searchView;
    private CustomersDbAdapter mDbHelper;
    private TextView customerText;
    private TextView nameText;
    private TextView cityText;
    private TextView stateText;
    public TextView zipCodeText;
    public TextView amountText;
    ImageButton cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.removeraddeduser);

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
                    CustomersDbAdapter.KEY_CITY,
                    CustomersDbAdapter.KEY_STATE,
                    CustomersDbAdapter.KEY_ZIP};

            // Specify the Corresponding layout elements where we want the columns to go
            int[] to = new int[] {     R.id.rescustomer,
                    R.id.resname,
                    R.id.rescity,
                    R.id.resstate,
                    R.id.reszipCode
            };

            // Create a simple cursor adapter for the definitions and apply them to the ListView
            SimpleCursorAdapter customers = new SimpleCursorAdapter(this,R.layout.removeuser_listreslut, cursor, from, to);
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
                    String city = cursor.getString(cursor.getColumnIndexOrThrow("email_Id"));
                    String state = cursor.getString(cursor.getColumnIndexOrThrow("names"));
                    String zipCode = cursor.getString(cursor.getColumnIndexOrThrow("comment"));
                    String amount = cursor.getString(cursor.getColumnIndexOrThrow("amount"));

                    //Check if the Layout already exists
                    LinearLayout customerLayout = (LinearLayout)findViewById(R.id.customerLayout);
                    if(customerLayout == null){
                        //Inflate the Customer Information View
                        LinearLayout leftLayout = (LinearLayout)findViewById(R.id.rightLayout);
                        View customerInfo = getLayoutInflater().inflate(R.layout.remove_user_info, leftLayout, false);
                        leftLayout.addView(customerInfo);
                    }
                    //Get References to the TextViews
                    customerText = (TextView) findViewById(R.id.recustomer);
                    nameText = (TextView) findViewById(R.id.rename);
                    cityText = (TextView) findViewById(R.id.recity);
                    stateText = (TextView) findViewById(R.id.restate);
                    zipCodeText = (TextView) findViewById(R.id.rezipCode);
                    amountText = (TextView) findViewById(R.id.reamount1);

                    // Update the parent class's TextView
                    customerText.setText(customer);
                    nameText.setText(name);
                    cityText.setText(city);
                    stateText.setText(state);
                    zipCodeText.setText(zipCode);
                    amountText.setText(amount);
                    searchView.setQuery("",true);
                }
            });
        }
    }
    public void removeOn(View view) {

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setCancelable(false);
        dlgAlert.setTitle("Delete");
        dlgAlert.setMessage("Are you sure want to delete this customer?");
        // setup a dialog window
        dlgAlert.setCancelable(false)
                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TextView accNO = (TextView) findViewById(R.id.recustomer);
                        TextView namecust = (TextView) findViewById(R.id.restate);
                        String Textaccno = accNO.getText().toString();
                        String namecustomer = namecust.getText().toString();
                        mDbHelper.deleteUser(Textaccno);
                        Toast.makeText(RemoveUser1.this,
                                    "Deleted", Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
            }
           return super.onOptionsItemSelected(item);
    }
}



