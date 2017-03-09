package com.peter.myapp.tv4mostpopular;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class MyCustomAdapter extends ArrayAdapter<Movie> {

    Context context;
    ArrayList<Movie> thingsToPrint;

    public MyCustomAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);

        //Save context for later
        this.context = context;
        thingsToPrint = (ArrayList) objects;

    }

    static class MyViewHolder {
        ImageView icon;         // Needs resizing
        TextView title;
        TextView description;
        TextView broadcastTime;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        // The variable that will be (re)used
        View itemView;

        if(convertView == null){
            itemView = layoutInflater.inflate(R.layout.activity_json_items, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder();

            myViewHolder.icon = (ImageView)itemView.findViewById(R.id.JsonImage);
            myViewHolder.title = (TextView)itemView.findViewById(R.id.JsonItemName);
            myViewHolder.description = (TextView)itemView.findViewById(R.id.JsonItemDesc);
            myViewHolder.broadcastTime = (TextView)itemView.findViewById(R.id.BroadcastDateTime);

            itemView.setTag(myViewHolder);
        } else {
            itemView = convertView;

        }

        MyViewHolder myViewHolder = new MyViewHolder();
        MyViewHolder returnedViewHolder = (MyViewHolder)itemView.getTag();
        //String movieName = thingsToPrint.get(position).setTitle();


        // The title
        returnedViewHolder.title.setText(thingsToPrint.get(position).getTitle());


        //The time
        String dateTime = thingsToPrint.get(position).getBroadcastTime();

        // Simple time formatter, inspiration from Stackoverflow.
        DateFormat inputFormatter1 = new SimpleDateFormat("yyy-MM-dd'T'HH:mm");
        Date date1 = null;
        try {
            date1 = inputFormatter1.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat outputFormatter = new SimpleDateFormat("yyy-MM-dd' 'HH:mm ");
        String output1 = outputFormatter.format(date1);

        // The broadcast date time
        returnedViewHolder.broadcastTime.setText("Broadcast date/time: " + output1);
        notifyDataSetChanged(); // mucho importante
                        // The full time-string from json below
                       //  returnedViewHolder.broadcastTime.setText(dateTime);
        //The icon
        // Resize the icon
        Drawable finalIcon = ResizeImage.reSize(thingsToPrint.get(position).getIcon());
        notifyDataSetChanged(); // mucho importante
        //Use the icon
        returnedViewHolder.icon.setImageDrawable(finalIcon);
        notifyDataSetChanged(); // mucho importante

        // The description
        returnedViewHolder.description.setText(thingsToPrint.get(position).getDescription());
        notifyDataSetChanged(); // mucho importante

        return itemView; // ...and a new view/item in the ListView is created


     }

}
