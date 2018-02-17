package com.thomas.mirakle.crm_mirakle;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarItemStyle;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarPredicate;


public class add_event_activity extends AppCompatActivity {
    TextView date_sample;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_activity);
        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

/* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);


        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        /*
        //unselected date config
        horizontalCalendar.getDefaultStyle()
                .setColorTopText(Color.parseColor("#FF0000"))
        .setColorMiddleText(Color.parseColor("#FFFF35"))
        .setColorBottomText(Color.parseColor("#0000FF"));

        //date underline config
        horizontalCalendar.getConfig()
                .setSelectorColor(Color.BLACK)
                .setSizeTopText(23);
        */

        /*Disable some dates
        HorizontalCalendar.Builder builder = new HorizontalCalendar.Builder();
        builder.disableDates(new HorizontalCalendarPredicate() {
            @Override
            public boolean test(Calendar date) {
                return false;    // return true if this date should be disabled, false otherwise.
            }

            @Override
            public CalendarItemStyle style() {
                return null;     // create and return a new Style for disabled dates, or null if no styling needed.
            }
        });
        */

        date_sample=findViewById(R.id.date_sample);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                date_sample.setText(date.get(Calendar.DATE)+"-"+date.get(Calendar.MONTH)+"-"+date.get(Calendar.YEAR));
            }
        });
    }
}
