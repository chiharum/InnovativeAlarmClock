package com.cat.innovativealarmclock;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    int screenDate;

    Button chooseDateButton;
    ListView scheduleList;

    NewsListData newsListData;

    List<ScheduleItem> items;
    ScheduleEditingListCustomAdapter customAdapter;

    MySQLiteOpenHelper mySQLiteOpenHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext());
        database = mySQLiteOpenHelper.getWritableDatabase();

        chooseDateButton = (Button)findViewById(R.id.chooseDateButton);
        scheduleList = (ListView)findViewById(R.id.scheduleEditingListView);

        screenDate = getIntent().getIntExtra("todayDate", 0);

        items = new ArrayList<>();
        newsListData = new NewsListData();
        setNewsList();
    }

    public void setNewsList(){

        items.clear();

        searchDatabase(screenDate);

        int amount = newsListData.number;

        if(amount == 0){
            ScheduleItem item = new ScheduleItem(getString(R.string.noSchedule), screenDate);
            items.add(item);
        }else{
            for(int i = 0; i < amount; i++){

                String scheduleTitle = newsListData.schedule.get(i);
                int date = newsListData.date.get(i);

                ScheduleItem item;
                item = new ScheduleItem(scheduleTitle, date);
                items.add(item);
            }
        }

        customAdapter = new ScheduleEditingListCustomAdapter(this, R.layout.schedule_editor_layout, items);
        scheduleList.setAdapter(customAdapter);
        scheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //編集するためのアラートダイアログを表示する
            }
        });
    }

    public void searchDatabase(int dateNumber){

        Cursor cursor = null;

        String scheduleTitle = null;
        int date = 0;

        try{
            cursor = database.query(MySQLiteOpenHelper.ScheduleTable, new String[]{"schedule_title", "date"}, "date = ?", new String[]{String.valueOf(dateNumber)}, null, null, null);

            int indexScheduleTitle = cursor.getColumnIndex("schedule_title");
            int indexDate = cursor.getColumnIndex("date");

            while(cursor.moveToNext()){
                scheduleTitle = cursor.getString(indexScheduleTitle);
                date = cursor.getInt(indexDate);
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }

        newsListData.NewsListData(scheduleTitle, date);
    }

    public void addSchedule(View view){

    }

    public void chooseDate(View view){

        int year = 0, month = 0, day = 0;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                screenDate = day + month * 100 + year * 10000;
                setNewsList();
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setSpinnersShown(false);
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        datePickerDialog.show();
    }

    public void increase(View view){

    }

    public void decrease(View view){

    }
}
