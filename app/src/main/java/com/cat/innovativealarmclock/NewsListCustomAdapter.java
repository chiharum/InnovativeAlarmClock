package com.cat.innovativealarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class NewsListCustomAdapter extends ArrayAdapter<ScheduleItem> {

    LayoutInflater layoutInflater;

    public NewsListCustomAdapter (Context context, int resource, List<ScheduleItem> items){
        super(context, resource, items);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.news_list_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ScheduleItem item = getItem(position);

        if(item != null){
            viewHolder.newsTitle.setText(item.scheduleItem);
            viewHolder.dateText.setText(String.valueOf(item.date));
        }

        return convertView;
    }

    private class ViewHolder{
        TextView newsTitle;
        TextView dateText;

        public ViewHolder(View view){
            newsTitle = (TextView)view.findViewById(R.id.scheduleTitleText);
            dateText = (TextView)view.findViewById(R.id.dateText);
        }
    }
}
