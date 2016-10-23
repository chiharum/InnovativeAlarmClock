package com.cat.innovativealarmclock;

import java.util.ArrayList;

public class NewsListData {

    ArrayList<String> schedule;
    ArrayList<Integer> date;
    int amount = 0;

    // ArrayListの初期化

    public void setNewsListData(String schedule, int date){

        this.schedule.add(schedule);
        this.date.add(date);

        if(schedule != null && date != 0){
            amount++;
        }
    }
}
