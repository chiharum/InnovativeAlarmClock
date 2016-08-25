package com.cat.innovativealarmclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ScheduleEditingListCustomAdapter extends ArrayAdapter<ScheduleItem> {

    LayoutInflater layoutInflater;

    public ScheduleEditingListCustomAdapter (Context context, int resource, List<ScheduleItem> items){
        super(context, resource, items);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent){

        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.schedule_editor_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ScheduleItem item = getItem(position);

        if(item != null){
            viewHolder.scheduleTitleText.setText(item.scheduleItem);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView scheduleTitleText;

        public ViewHolder(View view){
            scheduleTitleText = (TextView)view.findViewById(R.id.scheduleTitleText);
        }
    }
}
