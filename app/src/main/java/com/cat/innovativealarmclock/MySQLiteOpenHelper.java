package com.cat.innovativealarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static final String DatabaseFile = "schedule.db";
    static final int DatabaseVersion = 1;
    static final String ScheduleTable = "schedule";

    public MySQLiteOpenHelper(Context context){
        super(context, DatabaseFile, null, DatabaseVersion);
    }

    public void onCreate(SQLiteDatabase database){
        database.execSQL("create table " + ScheduleTable + " (id integer primary key autoincrement not null, schedule_title string not null, date integer not null)");
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        //DatabaseVersionの数値を変えた際に呼び出されるからまだ書きません。
    }
}
