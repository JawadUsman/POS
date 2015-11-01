package com.example.jawa.pos;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jawa on 10/29/2015.
 */
public class App_holer_register extends Activity {

    TextView enbel;
    private EditText fname, lname, phoneNo, gmailId, password,companyname;
    private String fnameText, lnameText, phoneNoText, gmailIdText, passwordText, companyNameText, fullname;
    private CustomersDbAdapter mDbHelper;
    ImageButton cancel;
    Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appholderaccount);

        enbel = (TextView) findViewById(R.id.enabel);
        enbel.setMovementMethod(LinkMovementMethod.getInstance());

        mDbHelper = new CustomersDbAdapter(this);
        mDbHelper.open();

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        phoneNo = (EditText) findViewById(R.id.phoneNo);
        gmailId = (EditText) findViewById(R.id.gmailId);
        password = (EditText) findViewById(R.id.password);
        companyname = (EditText) findViewById(R.id.lcompanyname);


        cancel = (ImageButton) findViewById(R.id.backsingup);
        done = (Button) findViewById(R.id.button2);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnameText = fname.getText().toString();
                lnameText = lname.getText().toString();
                phoneNoText = phoneNo.getText().toString();
                gmailIdText = gmailId.getText().toString();
                passwordText = password.getText().toString();
                companyNameText = companyname.getText().toString();
                fullname = fnameText.concat(" ").concat(lnameText);

                if (fullname.isEmpty() && phoneNoText.isEmpty() &&
                        gmailIdText.isEmpty() && passwordText.isEmpty()) {

//                    View view1 = App_holer_register.this.getCurrentFocus();
//                    if (view1 != null) {
//                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
//                    }
                    Toast.makeText(App_holer_register.this, "Incomplete Registration", Toast.LENGTH_SHORT).show();
                } else {
                    //Add some Customer data as a sample
                    mDbHelper.deleteAlldailycashdayend("profiletable");
                    long id = mDbHelper.profileCustomer(fullname, phoneNoText, gmailIdText, passwordText, companyNameText);
                    if (id < 0) {
                        Toast.makeText(App_holer_register.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(App_holer_register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
            }
        });
    }
}
