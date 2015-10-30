package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by jawa on 10/7/2015.
 */
public class AddNewUser extends Activity {
    private CustomersDbAdapter mDbHelper;
    private ListView mListView;
    ImageButton cancel;
    Button register;
    private EditText accountnumber, customername, email, phonenumber, comment, amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewuser);


        mDbHelper = new CustomersDbAdapter(this);
        mDbHelper.open();
        mListView = (ListView) findViewById(R.id.list);


        accountnumber = (EditText) findViewById(R.id.accountnumber);
        customername = (EditText) findViewById(R.id.customername);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        email = (EditText) findViewById(R.id.email);
        comment = (EditText) findViewById(R.id.comment);
        amount = (EditText) findViewById(R.id.camount);
        register = (Button) findViewById(R.id.register);


//        Intent showacitivity = getIntent();
//        String acitvity = (String) showacitivity.getExtras().get("from");
//        TextView textView = (TextView) findViewById(R.id.textView24);
////        textView.append(" " + acitvity);

        cancel = (ImageButton) findViewById(R.id.imageButton41);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void register(View view) {
        String customer = accountnumber.getText().toString();
        String city = phonenumber.getText().toString();
        String address1 = phonenumber.getText().toString().trim();
        String address2 = email.getText().toString();
        String name = customername.getText().toString();
        String state = customername.getText().toString();
        String zipCode = comment.getText().toString();
        String camount = amount.getText().toString();


            if(customer.isEmpty() && state.isEmpty() && address2.isEmpty() ||
                city.length() == 0 || city.equals("") && zipCode == null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            Toast.makeText(AddNewUser.this, "Incomplete Registration", Toast.LENGTH_SHORT).show();
        } else {
            //Add some Customer data as a sample
            long id = mDbHelper.createCustomer(customer, city, name, address1, address2, state, zipCode, camount);

            if (id < 0) {
                Toast.makeText(AddNewUser.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AddNewUser.this, "Registration Successful", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
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

