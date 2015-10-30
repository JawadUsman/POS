package com.example.jawa.pos;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by jawa on 10/29/2015.
 */
public class App_holer_register extends Activity {

    TextView enbel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appholderaccount);

        enbel = (TextView) findViewById(R.id.enabel);
        enbel.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
