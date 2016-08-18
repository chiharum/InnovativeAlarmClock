package com.cat.innovativealarmclock;

import java.util.ArrayList;

public class NewsListData {

    ArrayList<String> schedule;
    ArrayList<Integer> date;
    int number = 0;

    public void NewsListData(String schedule, int date){

        this.schedule.add(schedule);
        this.date.add(date);

        if(schedule != null && date != 0){
            number++;
        }
    }
}
