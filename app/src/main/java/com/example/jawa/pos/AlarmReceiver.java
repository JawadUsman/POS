package com.example.jawa.pos;

/**
 * Created by jawa on 10/31/2015.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        Toast.makeText(arg0, "Daily report will send now", Toast.LENGTH_LONG).show();

        Vibrator vibrator = (Vibrator) arg0.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        Intent i = new Intent(arg0, DailyReport_CSV_file.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        arg0.startActivity(i);
    }
}