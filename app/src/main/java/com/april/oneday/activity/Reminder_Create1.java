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
        Log.e("Day Selected", day + " / " + month + " / " + year);
        String reminder_date = year+"-"+month+"-"+day;

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
