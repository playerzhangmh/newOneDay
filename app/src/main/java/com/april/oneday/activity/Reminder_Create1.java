package com.april.oneday.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.andexert.calendarlistview.library.DayPickerView;
import com.andexert.calendarlistview.library.SimpleMonthAdapter;
import com.april.oneday.R;

public class Reminder_Create1 extends Activity implements com.andexert.calendarlistview.library.DatePickerController {
    private DayPickerView dayPickerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_create1);

        dayPickerView = (DayPickerView) findViewById(R.id.pickerView);
        dayPickerView.setController(this);

    }

    @Override
    public int getMaxYear()
    {
        return 2020;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {

        String monthString;
        String dayString ;
        if (month+1<10){
            monthString = "0"+(month+1);
        }else{
            monthString = ""+(month+1);
        }

        if (day<10){
            dayString = "0"+day;
        }else{
            dayString = ""+day;
        }

        String reminder_date=year+"-"+monthString+"-"+dayString;
        Log.e("日期",reminder_date);

        Intent intent = new Intent(this, Reminder_Create2.class);
        intent.putExtra("reminder_date",reminder_date);
        startActivity(intent);
        finish();

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

        Log.e("Date range selected", selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());
    }
}
