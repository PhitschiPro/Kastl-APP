package com.example.philipp.whatalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static com.example.philipp.whatalarm.R.id.status_text;

public class AlarmActivity extends AppCompatActivity {

    AlarmManager manager;
    TimePicker alarm_timepicker;

    TextView update_text;
    Context context;
    PendingIntent pending_intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context = this;

        //initialize AlarmManager
        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initialize TimePicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        alarm_timepicker.setIs24HourView(DateFormat.is24HourFormat(this));
        //alarm_timepicker.setIs24HourView(true);

        //initialize StatusBox
        update_text = (TextView) findViewById(R.id.status_text);

        //create a instance of calender
        final Calendar calendar = Calendar.getInstance();

        //create an Intent for alarm receiver class
        final Intent intent = new Intent(this.context, Alarm_Receiver.class);










        //initialize Buttons
        Button set_alarm = (Button) findViewById(R.id.btn_alarm_on);
        Button unset_alarm = (Button) findViewById(R.id.btn_alarm_off);

        //creat an OnClick Listener for alarm on
        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calendar instance, wich is picked on timepicker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //get int values from calendar instance
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //convert int to string
                String hour_S = String.valueOf(hour);
                String minute_S = String.valueOf(minute);

                show_toasttime(hour , minute);

                if(minute < 10) {
                    minute_S = "0" + String.valueOf(minute);
                }

                set_alarm_text("Alarm set to " + hour_S + ":" + minute_S);


                //create a pending intent that delays until specified calendar time
                pending_intent = PendingIntent.getBroadcast(AlarmActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set alarm manager
                manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

            }
        });


        //creat an OnClick Listener for alarm off
        unset_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_alarm_text("Alarm OFF!");

                //cancels the alarm
                manager.cancel(pending_intent);
            }
        });
    }

    private void show_toasttime(int hour ,int minute) {
        //Calculetes how long alarm needs to ring
        Calendar cal = Calendar.getInstance();
        int sys_minute = cal.get(Calendar.MINUTE);
        int sys_hour = cal.get(Calendar.HOUR_OF_DAY);
        String toast_time_h = "";
        String toast_time_min = "";
        String toast_time = "";

        int toast_h = hour - sys_hour;
        int toast_min = minute - sys_minute;

        if(minute < sys_minute) {
            toast_min = 60 - sys_minute + minute;
            toast_h--;
        }
        if(hour == 0){
            toast_h = (sys_hour -24)*(-1) ;
        }
        if( hour < sys_hour && sys_minute > minute){
            toast_h = hour - sys_hour +23;
        }

        toast_time_h = Integer.toString(toast_h);
        toast_time_min = Integer.toString(toast_min);


        if(toast_min < 10){
            toast_time_min = "0" + String.valueOf(toast_min);
        }

        toast_time = toast_time_h + ":" + toast_time_min;
        //Toast for how long it takes to ring
        Toast.makeText(getApplicationContext(),  "Der Wecker läutet in " + toast_time , Toast.LENGTH_SHORT).show();
    }


    private void set_alarm_text(String output) {
        update_text.setText(output);

    }

}
