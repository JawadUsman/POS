package com.example.jawa.pos;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by jawa on 10/31/2015.
 */
public class Time_Picker extends Activity {
    TimePicker myTimePicker;
    Button buttonstartSetDialog,buttonCancelAlarm;
    ImageButton cancel;
    TextView textAlarmPrompt,time;
    private String format = "";

    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timepicker);
        onNewIntent(getIntent());

        myTimePicker = (TimePicker) findViewById(R.id.timePicker1);
        myTimePicker.clearFocus();


        textAlarmPrompt = (TextView)findViewById(R.id.alarmprompt);
        time = (TextView)findViewById(R.id.time1);

        Calendar calendar = Calendar.getInstance();
        myTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        myTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));

        buttonstartSetDialog = (Button)findViewById(R.id.startSetDialog);
        buttonstartSetDialog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textAlarmPrompt.setText("");
                openTimePickerDialog(false);
            }
        });
        buttonCancelAlarm = (Button)findViewById(R.id.cancel);
        buttonCancelAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cancelAlarm();
            }
        });
        cancel = (ImageButton) findViewById(R.id.gobackdr);
        cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void openTimePickerDialog(boolean is24r){

        Calendar calNow = Calendar.getInstance();


        int hour = myTimePicker.getCurrentHour();
        int min = myTimePicker.getCurrentMinute();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,min);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        if(cal.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            cal.add(Calendar.DATE, 1);
        }
        setAlarm(cal);
    }
    private void setAlarm(Calendar targetCal){

        textAlarmPrompt.setText("\n\tDispatch time is set at" + targetCal.getTime());

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
    }
    private void cancelAlarm(){

        textAlarmPrompt.setText("\nAlarm Cancelled!");

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}

