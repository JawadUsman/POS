package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by jawa on 10/30/2015.
 */
public class Profile extends Activity {

    TextView t1,t2,t3,t4,t5;
    ImageButton cancel;
    private CustomersDbAdapter dbHelper;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        cancel = (ImageButton) findViewById(R.id.gobackp);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbHelper = new CustomersDbAdapter(this);
        dbHelper.open();

        t1 = (TextView)findViewById(R.id.proname);
        t2= (TextView)findViewById(R.id.prophoneNo);
        t3 = (TextView)findViewById(R.id.profgmail);
        t4 = (TextView)findViewById(R.id.profcreatedat);
        t5 = (TextView)findViewById(R.id.procompany);

        Cursor c1 = dbHelper.getgmail();
        if(c1.moveToFirst()){
            do{
                String name = (c1.getString(0)).toString();
                String phone = (c1.getString(1)).toString();
                String gmail = (c1.getString(2)).toString();
                String createdat = (c1.getString(5)).toString();
                String company =(c1.getString(4)).toString();
                t1.setText(name);
                t2.setText(phone);
                t3.setText(gmail);
                t4.setText(createdat);
                t5.setText(company);
            }while(c1.moveToNext());
        }
        dbHelper.close();
    }
}
