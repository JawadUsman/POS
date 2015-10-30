package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * Created by jawa on 10/6/2015.
 */
public class Cash extends Fragment implements View.OnClickListener {

    private CustomersDbAdapter mDbHelper;
    ImageButton adduser, removeuser, cash1;
    Button addcash, clearcash;
    EditText cashamount, cashcomment;
    ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.cash, container, false);
        setHasOptionsMenu(true);

        mDbHelper = new CustomersDbAdapter(getActivity());
        mDbHelper.open();


        adduser = (ImageButton) rootview.findViewById(R.id.adduser);
        removeuser = (ImageButton) rootview.findViewById(R.id.removeuser1);
        cash1 = (ImageButton) rootview.findViewById(R.id.cash1);

        adduser.setOnClickListener(this);
        removeuser.setOnClickListener(this);


        cashamount = (EditText) rootview.findViewById(R.id.cashamount);
        cashcomment = (EditText) rootview.findViewById(R.id.cashcomment);
        addcash = (Button) rootview.findViewById(R.id.addcash);
        clearcash = (Button) rootview.findViewById(R.id.clearcash);

        addcash.setOnClickListener(this);
        clearcash.setOnClickListener(this);


        scrollView = (ScrollView) rootview.findViewById(R.id.myview);
        cash1.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.scrollBy(0, +700);
            }
        });

        return rootview;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adduser:
//                boolean isPressed = false;
//                if (isPressed)
//                    adduser.setImageResource(R.drawable.addnewuser);
//                else
//                    adduser.setImageResource(R.drawable.useradd);
//
//                isPressed = !isPressed;

                Intent passavalue = new Intent(getActivity(), AddNewUser.class);
                passavalue.putExtra("from", "Cash");
                final int reslut = 1;
                startActivityForResult(passavalue, reslut);
                break;

            case R.id.removeuser1:
                startActivity(new Intent(getActivity(), RemoveUser1.class));
                break;

            case R.id.addcash:

                String a = cashamount.getText().toString().trim();
                String scashcomment = cashcomment.getText().toString();

                if (a.isEmpty()  && scashcomment.isEmpty()) {
                    Toast.makeText(getActivity(), "Invalid Amount", Toast.LENGTH_SHORT).show();
                } else {
                    //Add some Customer data as a sample
                    int scashamount = Integer.parseInt(cashamount.getText().toString());
                    long id = mDbHelper.cashCustomer(scashamount, scashcomment);
                    cashamount.setText("");
                    cashcomment.setText("");

                    if (id < 0) {
                        Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "The amount has been successfully added", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.clearcash:
                cashamount.setText("");
                cashcomment.setText("");
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.exit_menu, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.exit:
                getActivity().finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}