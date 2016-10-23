package com.cat.innovativealarmclock;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ScheduleItem> items;
    NewsListCustomAdapter customAdapter;

    ListView newsList;

    NewsListData newsListData;

    int todayDate;

    MySQLiteOpenHelper mySQLiteOpenHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySQLiteOpenHelper = new MySQLiteOpenHelper(getApplicationContext());
        database = mySQLiteOpenHelper.getWritableDatabase();

        newsList = (ListView)findViewById(R.id.newsList);
        items = new ArrayList<>();

        newsListData = new NewsListData();

        Calendar calendar = Calendar.getInstance();
        todayDate = calendar.get(Calendar.DATE) + (calendar.get(Calendar.MONTH) + 1) * 100 + calendar.get(Calendar.YEAR) * 10000;

        setNewsList();
    }

    public void setNewsList(){

        items.clear();

        searchDatabase(todayDate);

        int amount = newsListData.amount;

        if(amount == 0){
            ScheduleItem item = new ScheduleItem(getString(R.string.noSchedule), todayDate);
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

        customAdapter = new NewsListCustomAdapter(this, R.layout.news_list_layout, items);
        newsList.setAdapter(customAdapter);
    }

    public void searchDatabase(int dateNumber){

        Cursor cursor = null;

        String scheduleTitle;
        int date;

        try{
            cursor = database.query(MySQLiteOpenHelper.ScheduleTable, new String[]{"schedule_title", "date"}, "date = ?", new String[]{String.valueOf(dateNumber)}, null, null, null);

            int indexScheduleTitle = cursor.getColumnIndex("schedule_title");
            int indexDate = cursor.getColumnIndex("date");

            while(cursor.moveToNext()){
                scheduleTitle = cursor.getString(indexScheduleTitle);
                scheduleTitle = "強制的に代入しました";
                Log.i("schedule", scheduleTitle);
                date = cursor.getInt(indexDate);
                newsListData.setNewsListData(scheduleTitle, date);
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public void setting(View view){
        Log.i(getString(R.string.eventLog_button), "MainActivity_setting");
    }

    public void alarmSetting(View view){
        Log.i(getString(R.string.eventLog_button), "MainActivity_alarm");
    }

    public void editSchedule(View view){
        Log.i(getString(R.string.eventLog_button), "MainActivity_schedule");

        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ScheduleActivity.class);
        intent.putExtra("todayDate", todayDate);
        startActivity(intent);
    }
}
