package com.cat.innovativealarmclock;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ScheduleItem> items;
    NewsListCustomAdapter customAdapter;

    ListView newsList;

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
    }

    public void setNewsList(){

    }

    public void searchDatabase(int dateNumber){

        Cursor cursor = null;

        try{
            cursor = database.query(MySQLiteOpenHelper.ScheduleTable, new String[]{"schedule_title", "date"}, "date = ?", new String[]{String.valueOf(dateNumber)}, null, null, null);

            int indexScheduleTitle = cursor.getColumnIndex("schedule_title");
            int indexDate = cursor.getColumnIndex("date");

            while(cursor.moveToNext()){
                String scheduleTitle = cursor.getString(indexScheduleTitle);
                int date = cursor.getInt(indexDate);
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
