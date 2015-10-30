package com.example.jawa.pos;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by jawa on 10/6/2015.
 */
public class Reports extends Fragment implements View.OnClickListener{
    private Drawable image;

    ImageButton dailyreport,rar,monthyrep,yearlyr;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.reports, container, false);
        dailyreport = (ImageButton) view.findViewById(R.id.dialyreport);
        rar = (ImageButton) view.findViewById(R.id.rar);
        monthyrep = (ImageButton) view.findViewById(R.id.monthyrep);
        yearlyr = (ImageButton) view.findViewById(R.id.yearlyr);

        dailyreport.setOnClickListener(this);
        rar.setOnClickListener(this);
        monthyrep.setOnClickListener(this);
        yearlyr.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialyreport:
                Intent passavalue = new Intent(getActivity(), DailyReport_CSV_file.class);
                passavalue.putExtra("from", "Cash");
                final int reslut = 1;
                startActivityForResult(passavalue, reslut);
                break;

            case R.id.rar:
                Intent passavalue1 = new Intent(getActivity(), RAR__CSV_file.class);
                passavalue1.putExtra("from", "Cash");
                final int reslut1 = 1;
                startActivityForResult(passavalue1, reslut1);
                break;

            case R.id.monthyrep:
                Intent passavalue2 = new Intent(getActivity(), Monthly_Report_CSV.class);
                passavalue2.putExtra("from", "Cash");
                final int reslut2 = 1;
                startActivityForResult(passavalue2, reslut2);
                break;

            case R.id.yearlyr:
                Intent passavalue3 = new Intent(getActivity(), Yearly_Report_CSV.class);
                passavalue3.putExtra("from", "Cash");
                final int reslut3 = 1;
                startActivityForResult(passavalue3, reslut3);
                break;
        }
    }
}
