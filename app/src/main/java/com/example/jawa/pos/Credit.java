package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Created by jawa on 10/6/2015.
 */


public class Credit extends Fragment{
    private CustomersDbAdapter dbHelper;
    ListView slistofcreditcustomer;
    private SimpleCursorAdapter dataAdaptercredit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.credit_main, container, false);
        setHasOptionsMenu(true);
        dbHelper = new CustomersDbAdapter(getActivity());
        dbHelper.open();

        slistofcreditcustomer = (ListView) view.findViewById(R.id.listcreditcustomer);
        displayListViewofCredit();
        return view;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void displayListViewofCredit() {
        Cursor cursor = dbHelper.queueAllCreditCustomer();
        // The desired columns to be bound
        String[] columns = new String[] {
                CustomersDbAdapter.KEY_CUSTOMER,
                CustomersDbAdapter.KEY_ADDRESS1,
                CustomersDbAdapter.KEY_NAME,
                CustomersDbAdapter.KEY_CITY,
                CustomersDbAdapter.KEY_ZIP,
                CustomersDbAdapter.KEY_AMOUNT};
        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.scustomermain,
                R.id.snamemain,
                R.id.sphonenomain,
                R.id.emailmain,
                R.id.scommentmain,
                R.id.creditamountmain};
        dataAdaptercredit = new SimpleCursorAdapter(getActivity(),
                R.layout.credit_customer_mainlist, cursor, columns, to,0);
        slistofcreditcustomer.setAdapter(dataAdaptercredit);
        dataAdaptercredit.notifyDataSetChanged();
        dataAdaptercredit.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_refresh_addnewuser, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.search:
                startActivity(new Intent(getActivity(), Credit_Search.class));
                break;
            case R.id.refresh:
                displayListViewofCredit();
                break;
            case R.id.addnewuser:
                Intent passavalue = new Intent(getActivity(), AddNewUser.class);
                passavalue.putExtra("from", "Cash");
                final int reslut = 1;
                startActivityForResult(passavalue, reslut);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}