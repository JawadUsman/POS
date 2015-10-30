package com.example.jawa.pos;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jawa on 10/6/2015.
 */
public class DailyPurchase extends Fragment implements View.OnClickListener {

    private CustomersDbAdapter dbHelper;
    ListView slistofcash,lsitofCredit;
    EditText cashcompanyname,cashamount,invoiceno,creditcompanyname,ccreditamount,dateofpayment;
    private EditText shoprent, houserent, maintanceexp, medicalexp,gafees,othrrefees;
    TextView inspectionDate;
    Button newt,credit,cash,expenses;
    private SimpleCursorAdapter dataAdapter,dataAdapter1;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialy_purchase, container, false);
        setHasOptionsMenu(true);

        newt = (Button) view.findViewById(R.id.newt);
        newt.setOnClickListener(this);
        expenses = (Button) view.findViewById(R.id.expenses);
        expenses.setOnClickListener(this);

        dbHelper = new CustomersDbAdapter(getActivity());
        dbHelper.open();

        slistofcash = (ListView) view.findViewById(R.id.listView21);
        lsitofCredit = (ListView) view.findViewById(R.id.listView22);
        inspectionDate = (TextView) view.findViewById(R.id.dialydate);

        displayDate();
        displayListViewofCash();
        displayListViewofCredit();

        return view;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newt:
                showInputDialog();
                break;
            case R.id.expenses:
                showExpensesDialog();
                break;

        }
    }

    private void showExpensesDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        final View promptView = layoutInflater.inflate(R.layout.expenses, null);

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setCancelable(false);
        dlgAlert.setView(promptView);
        dlgAlert.setTitle("Expenses");
        // setup a dialog window
        dlgAlert.setCancelable(false).setPositiveButton("Save" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    shoprent = (EditText) promptView.findViewById(R.id.shoprent);
                    houserent = (EditText) promptView.findViewById(R.id.houserent);
                    maintanceexp = (EditText) promptView.findViewById(R.id.maintain);
                    medicalexp = (EditText) promptView.findViewById(R.id.medical);
                    gafees = (EditText) promptView.findViewById(R.id.gaf);
                    othrrefees = (EditText) promptView.findViewById(R.id.otherbill);

                    int shopr = Integer.parseInt(shoprent.getText().toString());
                    int housere = Integer.parseInt(houserent.getText().toString());
                    int maintacebill = Integer.parseInt(maintanceexp.getText().toString());
                    int medicalre = Integer.parseInt(medicalexp.getText().toString());
                    int govafees = Integer.parseInt(gafees.getText().toString());
                    int otherbil = Integer.parseInt(othrrefees.getText().toString());
                    int totlebill = shopr + housere + maintacebill + medicalre + govafees + otherbil ;

                    if(shopr ==0 ){
                        dbHelper.createexpenses(0, housere, maintacebill, medicalre, govafees, otherbil, totlebill);
                    }else if(housere == 0){
                        dbHelper.createexpenses(shopr, 0, maintacebill, medicalre, govafees, otherbil, totlebill);
                    }else if(maintacebill == 0){
                        dbHelper.createexpenses(shopr, housere, 0, medicalre, govafees, otherbil, totlebill);
                    }else if(medicalre == 0){
                        dbHelper.createexpenses(shopr, housere, maintacebill, 0, govafees, otherbil, totlebill);
                    }else if(govafees == 0){
                        dbHelper.createexpenses(shopr, housere, maintacebill, medicalre, 0, otherbil, totlebill);
                    }else if(otherbil ==0){
                        dbHelper.createexpenses(shopr, housere, maintacebill, medicalre, govafees, 0, totlebill);
                    }else{
                        dbHelper.createexpenses(shopr, housere, maintacebill, medicalre, govafees, otherbil, totlebill);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(),"Expense report saved successfully",Toast.LENGTH_SHORT).show();
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create an alert dialog
        final AlertDialog alert = dlgAlert.create();
        alert.show();


    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayListViewofCash(){
        Cursor cursor = dbHelper.queueAll();
        // The desired columns to be bound
        String[] columns = new String[] {
                CustomersDbAdapter.CASH_PURCHASE_AMOUNT,
                CustomersDbAdapter.CASH_PURCHASE_COMPANY,
                CustomersDbAdapter.CASH_CREATED_AT};

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.sstate,
                R.id.scustomer,
                R.id.scity
        };
        dataAdapter = new SimpleCursorAdapter(getActivity(), R.layout.dailypurchase_listview, cursor, columns, to,0);
        slistofcash.setAdapter(dataAdapter);
        dataAdapter.notifyDataSetChanged();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayListViewofCredit() {
        Cursor cursor1 = dbHelper.queueAllCredit();
        // The desired columns to be bound
        String[] column = new String[]{
                CustomersDbAdapter.CREDIT_PURCHASE_INVOICENO,
                CustomersDbAdapter.CREDIT_PURCHASE_COMPANY,
                CustomersDbAdapter.CREDIT_PURCHASE_AMOUNT,
                CustomersDbAdapter.CREDIT_PURCHASE_PAYMENTDATE};

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.invoice,
                R.id.creditcompany,
                R.id.creditamount,
                R.id.duedate};
        dataAdapter1 = new SimpleCursorAdapter(getActivity(), R.layout.dailypuechase_creditlistview, cursor1, column, to, 0);
        lsitofCredit.setAdapter(dataAdapter1);
        lsitofCredit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String CompanyName = cursor.getString(cursor.getColumnIndexOrThrow("creditcompanyName"));
                Toast.makeText(getActivity(), CompanyName, Toast.LENGTH_SHORT).show();
            }
        });
        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchCountriesByName(constraint.toString());
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_refresh, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.search:
                startActivity(new Intent(getActivity(),Daily_purchase_Search.class));
                break;
            case R.id.refresh:
                refresh();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showInputDialog() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.choosepurchase, null);

        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setCancelable(false);
        dlgAlert.setView(promptView);
        dlgAlert.setTitle("Chose your Purchase Type");
        // setup a dialog window
        dlgAlert.setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create an alert dialog
        final AlertDialog alert = dlgAlert.create();
        alert.show();

        cash = (Button) promptView.findViewById(R.id.cash);
        credit = (Button) promptView.findViewById(R.id.credit);

        cash.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (alert != null) {
                    alert.dismiss();
                }

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View promptView = layoutInflater.inflate(R.layout.innercash, null);

                final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                dlgAlert.setCancelable(false);
                dlgAlert.setView(promptView);
                dlgAlert.setTitle("Cash Account");

//                boolean checked = ((CheckBox) view).isChecked();
//                switch (view.getId())
//                {
//                    case R.id.oldBill:
//                        if (checked) {
//                            selection.add("apple");
//                        } else {
//                            selection.remove("apple");
//                        }
//                        break;

                // setup a dialog window
                dlgAlert.setCancelable(false)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager immm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                immm.hideSoftInputFromWindow(promptView.getApplicationWindowToken(), 0);

                                dbHelper = new CustomersDbAdapter(getActivity());
                                dbHelper.open();

                                cashcompanyname = (EditText) promptView.findViewById(R.id.companyname);
                                cashamount = (EditText) promptView.findViewById(R.id.amount);
                                String companyn = cashcompanyname.getText().toString();
                                int companya = Integer.parseInt(cashamount.getText().toString());
                                if (companyn.isEmpty() && companya == 0) {
                                    Toast.makeText(getActivity(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Add some Customer data as a sample
                                    long cashid = dbHelper.dialycash(companyn, companya);
                                    if (cashid < 0) {
                                        Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                refresh();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(promptView.getApplicationWindowToken(), 0);
                                dialog.cancel();
                            }
                        });
                // create an alert dialog
                AlertDialog alert = dlgAlert.create();
                alert.show();
            }
        });

        credit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (alert != null) {
                    alert.dismiss();
                }
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View promptView = layoutInflater.inflate(R.layout.innercredit, null);


                final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
                dlgAlert.setCancelable(false);
                dlgAlert.setView(promptView);
                dlgAlert.setTitle("Credit Account");
                // setup a dialog window
                dlgAlert.setCancelable(false)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
                            public void onClick(DialogInterface dialog, int id) {

                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(promptView.getApplicationWindowToken(), 0);

                                dbHelper = new CustomersDbAdapter(getActivity());
                                dbHelper.open();

                                creditcompanyname = (EditText) promptView.findViewById(R.id.creditcompanyname);
                                ccreditamount = (EditText) promptView.findViewById(R.id.creditamount);
                                invoiceno = (EditText) promptView.findViewById(R.id.invoice);
                                dateofpayment = (EditText) promptView.findViewById(R.id.duedate);

                                String crcompanyn = creditcompanyname.getText().toString();
                                String crinvoice = invoiceno.getText().toString();
                                String duedate = dateofpayment.getText().toString();
                                int crcompanya = Integer.parseInt(ccreditamount.getText().toString());

                                if (crcompanyn.isEmpty() && crcompanya == 0) {
                                    Toast.makeText(getActivity(), "Please enter valid amount", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Add some Customer data as a sample
                                    long creditid = dbHelper.dialycredit(crinvoice, crcompanyn, crcompanya, duedate);
                                    if (creditid < 0) {
                                        Toast.makeText(getActivity(), "Unsuccessful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                refresh();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(promptView.getApplicationWindowToken(), 0);
                                dialog.cancel();
                            }
                        });
                // create an alert dialog
                AlertDialog alert = dlgAlert.create();
                alert.show();

            }
        });
    }
    public void refresh(){
        displayListViewofCash();
        displayListViewofCredit();
    }
    private void displayDate(){
        final Calendar c = Calendar.getInstance();
        inspectionDate.setText(new StringBuilder().append(c.get(Calendar.MONTH) + 1).append("/")
                .append(c.get(Calendar.DAY_OF_MONTH)).append("/")
                .append(c.get(Calendar.YEAR)).append(" "));
    }



}

