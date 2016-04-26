package com.crossover.medicalconference.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.medicalconference.Activites.TopicActivity;
import com.crossover.medicalconference.R;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class CustomListAdapterTopic extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater inflater = null;
    private String[] topicNames, topicProposal, descriptions;


    public CustomListAdapterTopic(Context ctx, String[] topicNames, String[] topicProposal, String[] descriptions) {
        super(ctx, R.layout.topic_list_element, topicNames);

        this.topicNames = topicNames;
        this.topicProposal = topicProposal;
        this.descriptions = descriptions;
        context = ctx;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View rowView;
        rowView = inflater.inflate(R.layout.topic_list_element, null);
        TextView topicName = (TextView)rowView.findViewById(R.id.topic_name);
        TextView topicCategory = (TextView)rowView.findViewById(R.id.topic_category);
        topicName.setText(topicNames[position]);
        topicCategory.setText("Shared by:" + topicProposal[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TopicActivity.class);
                i.putExtra("Title", topicNames[position]);
                i.putExtra("Description", descriptions[position]);
                context.startActivity(i);
            }
        });
        return rowView;
    }

}