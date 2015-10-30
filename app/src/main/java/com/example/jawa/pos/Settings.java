package com.example.jawa.pos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by jawa on 10/6/2015.
 */
public class Settings  extends Fragment {
    ListView slist;
    String[] web = {
            "Help",
            "Profile",
            "Account",
            "Daily Report Dispatch Time",
            "Backup and Recovery"
    };
    Integer[] imageId = {
            R.drawable.help,
            R.drawable.profile,
            R.drawable.account,
            R.drawable.timer,
            R.drawable.backup,
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         final View view = inflater.inflate(R.layout.settings, container, false);
        slist = (ListView) view.findViewById(R.id.slist);
        com.example.jawa.pos.CustomList adapter = new
                com.example.jawa.pos.CustomList(getActivity(), web, imageId);
        slist.setAdapter(adapter);
        slist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (web[+position]){
                    case "Account":

                        Intent passavalue = new Intent(getActivity(), App_holer_register.class);
                        passavalue.putExtra("from", "Cash");
                        final int reslut = 1;
                        startActivityForResult(passavalue, reslut);
                        break;
                }
                Toast.makeText(getActivity(), "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }
}
