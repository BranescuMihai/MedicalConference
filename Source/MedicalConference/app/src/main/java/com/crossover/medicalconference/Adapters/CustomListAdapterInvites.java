package com.crossover.medicalconference.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.medicalconference.Activites.InviteActivity;
import com.crossover.medicalconference.R;


/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class CustomListAdapterInvites extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater inflater = null;
    private String[] conferenceNames, conferenceProposal, conferenceDescriptions;
    private String[] conferenceStart, conferenceEnd;
    public static final int INVITE_RESPONDED = 1;

    public CustomListAdapterInvites(Context ctx, String[] conferenceNames,
                                    String[] conferenceProposal, String[] conferenceDescriptions,
                                    String[] conferenceStart, String[] conferenceEnd) {
        super(ctx, R.layout.topic_list_element, conferenceNames);

        this.conferenceNames = conferenceNames;
        this.conferenceProposal = conferenceProposal;
        this.conferenceDescriptions = conferenceDescriptions;
        this.conferenceStart = conferenceStart;
        this.conferenceEnd = conferenceEnd;

        context = ctx;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inflater.inflate(R.layout.invite_list_element, null);
        TextView inviteName = (TextView)rowView.findViewById(R.id.invite_name);
        TextView inviteAuthor = (TextView)rowView.findViewById(R.id.invite_author);
        TextView inviteDate = (TextView)rowView.findViewById(R.id.invite_date);
        inviteName.setText(conferenceNames[position]);
        inviteAuthor.setText("Invited by:" + conferenceProposal[position]);
        inviteDate.setText(conferenceStart[position] + " - " + conferenceEnd[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InviteActivity.class);
                i.putExtra("Title", conferenceNames[position]);
                i.putExtra("Description", conferenceDescriptions[position]);
                i.putExtra("StartDate", conferenceStart[position]);
                i.putExtra("EndDate", conferenceEnd[position]);
                context.startActivity(i);
            }
        });
        return rowView;
    }

}