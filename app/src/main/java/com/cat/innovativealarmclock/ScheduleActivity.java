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
import android.widget.Toast;

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

        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_onCreate");

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
        setScheduleList();
    }

    public void setScheduleList(){

        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_setNewsList");

        items.clear();

        search(screenDate);

        int amount = newsListData.amount;

        Log.i("int_amount", String.valueOf(amount));

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

    public void search(int resourceDate){

        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_search");

        Cursor cursor = null;

        String scheduleTitle;
        int date;

        try{
            cursor = database.query(MySQLiteOpenHelper.ScheduleTable, new String[]{"schedule_title", "date"}, "date = ?", new String[]{String.valueOf(resourceDate)}, null, null, null);

            int indexScheduleTitle = cursor.getColumnIndex("schedule_title");
            int indexDate = cursor.getColumnIndex("date");

            while(cursor.moveToNext()){
                newsListData.amount++;
                scheduleTitle = cursor.getString(indexScheduleTitle);
                date = cursor.getInt(indexDate);
                newsListData.setNewsListData(scheduleTitle, date);
            }
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public void save(String title){
        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_save");

        Log.i("title", title);

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", screenDate);
        contentValues.put("schedule_title", title);
        database.insert(MySQLiteOpenHelper.ScheduleTable, null, contentValues);
    }

    public void update(String title){
        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_update");

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", screenDate);
        contentValues.put("schedule_title", title);
        database.update(MySQLiteOpenHelper.ScheduleTable, contentValues, null, null);
    }

    public void erase(String title){
        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_erase");

        database.delete(MySQLiteOpenHelper.ScheduleTable, "date = " + screenDate + " and schedule_title = " + title, null);
    }

    public void dateEditingDialog(int position, final boolean isNew){

        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_dateEditingDialog");

        final String firstText;

        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = layoutInflater.inflate(R.layout.edit_schedule_layout, null);

        final EditText scheduleEditText = (EditText)layout.findViewById(R.id.scheduleEditText);
        search(position);
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
                        Log.i("Text is ", "null");
                    }else{
                        text = spannableStringBuilder.toString();
                    }

                    if(isNew){
                        save(text);
                    }else{
                        update(text);
                    }
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    setScheduleList();
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
        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_addSchedule");
        dateEditingDialog(0, true);
    }

    public void chooseDate(View view){

        Log.i(getString(R.string.eventLog_activityAndClass), "ScheduleActivity_chooseDate");

        int year = 0, month = 0, day = 0;

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                screenDate = day + month * 100 + year * 10000;
                setScheduleList();
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setSpinnersShown(false);
        datePickerDialog.getDatePicker().setCalendarViewShown(true);
        datePickerDialog.show();
    }

    public void increase(View view){

        Log.i(getString(R.string.eventLog_button), "ScheduleActivity_increase");

    }

    public void decrease(View view){

        Log.i(getString(R.string.eventLog_button), "ScheduleActivity_decrease");

    }
}
