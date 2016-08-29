package com.cat.innovativealarmclock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

        String dateText;
        dateText = screenDate / 10000 + "年" + (screenDate / 100 - screenDate / 10000 * 100) + "月" + screenDate % 100 + "日";
        chooseDateButton.setText(dateText);

        items = new ArrayList<>();
        newsListData = new NewsListData();
        setNewsList();
    }

    public void setNewsList(){

        items.clear();

        search(screenDate, true);

        int amount = newsListData.amount;

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
                dateEditingDialog(position, false);
            }
        });
    }

    public void search(int resourceInteger, boolean searchByDate){

        Cursor cursor = null;

        String scheduleTitle = null;
        int date = 0;

        try{
            cursor = database.query(MySQLiteOpenHelper.ScheduleTable, new String[]{"schedule_title", "date"}, "date = ?", new String[]{String.valueOf(resourceInteger)}, null, null, null);

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

        if(scheduleTitle != null){
            newsListData.setNewsListData(scheduleTitle, date);
        }
    }

    public void save(String title){

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", screenDate);
        contentValues.put("schedule_title", title);
        database.insert(MySQLiteOpenHelper.ScheduleTable, null, contentValues);
    }

    public void update(String title){

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", screenDate);
        contentValues.put("schedule_title", title);
        database.update(MySQLiteOpenHelper.ScheduleTable, contentValues, null, null);
    }

    public void erase(String title){

        database.delete(MySQLiteOpenHelper.ScheduleTable, "date = " + screenDate + " and schedule_title = " + title, null);
    }

    public void dateEditingDialog(int position, final boolean isNew){

        final String firstText;

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.edit_schedule_layout, null);

        final EditText scheduleEditText = (EditText)layout.findViewById(R.id.scheduleEditText);
        search(position, false);
        if(newsListData.schedule != null){
            firstText = newsListData.schedule.get(0);
            scheduleEditText.setText(firstText);
        }else{
            firstText = null;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        View.OnClickListener listener = new View.OnClickListener(){
            public void onClick(View view){

                int id = view.getId();

                if(id == R.id.addButton){
                    String text;

                    SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder)scheduleEditText.getText();
                    if(spannableStringBuilder == null){
                        text = null;
                    }else{
                        text = spannableStringBuilder.toString();
                    }

                    if(isNew){
                        update(text);
                    }else{
                        save(text);
                    }
                    setNewsList();
                    alertDialog.dismiss();
                }else{
                    erase(firstText);
                }
            }
        };

        layout.findViewById(R.id.addButton).setOnClickListener(listener);
        layout.findViewById(R.id.eraseButton).setOnClickListener(listener);

        alertDialog.setView(layout);
        alertDialog.show();
    }

    public void addSchedule(View view){
        dateEditingDialog(0, true);
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
