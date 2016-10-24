package com.cat.innovativealarmclock;

import android.util.Log;

import java.util.ArrayList;

public class NewsListData {

    ArrayList<String> schedule;
    ArrayList<Integer> date;
    int amount = 0;

    public void setNewsListData(String schedule, int date){

        if(amount == 0){
            setArrayList();
        }

        this.schedule.add(schedule);
        this.date.add(date);
        amount++;

        Log.i("DataFromData", "amount: " + amount + ", schedule: " + schedule + ", date: " + date);
    }

    public void setArrayList(){

        this.schedule = new ArrayList<String>();
        this.date = new ArrayList<Integer>();
    }

    public void clearData(){
        if(amount != 0){
            this.amount = 0;
            this.schedule.clear();
            this.date.clear();
        }
    }
}