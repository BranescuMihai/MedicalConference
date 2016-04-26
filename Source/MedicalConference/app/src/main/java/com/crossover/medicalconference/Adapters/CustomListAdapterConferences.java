package com.crossover.medicalconference.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.crossover.medicalconference.Fragments.FragmentConferences;
import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.Activites.ConferenceActivityAdmin;
import com.crossover.medicalconference.R;

import java.util.ArrayList;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class CustomListAdapterConferences extends ArrayAdapter<String> {

    private Context context;
    private LayoutInflater inflater = null;
    private ArrayList<String> conferenceNames, conferenceProposal, conferenceDescriptions;
    private ArrayList<String> conferenceStart, conferenceEnd;

    public interface OnDeletedListener{
        void onDeleted();
    }

    private OnDeletedListener mListener;


    public CustomListAdapterConferences(Context ctx, ArrayList<String> conferenceNames,
                                        ArrayList<String> conferenceProposal,
                                        ArrayList<String> conferenceDescriptions,
                                        ArrayList<String> conferenceStart,
                                        ArrayList<String> conferenceEnd,
                                        OnDeletedListener del) {
        super(ctx, R.layout.topic_list_element, conferenceNames);

        this.conferenceNames = conferenceNames;
        this.conferenceProposal = conferenceProposal;
        this.conferenceDescriptions = conferenceDescriptions;
        this.conferenceStart = conferenceStart;
        this.conferenceEnd = conferenceEnd;

        mListener = del;
        context = ctx;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView;
        rowView = inflater.inflate(R.layout.conference_list_element, null);
        TextView conferenceName = (TextView)rowView.findViewById(R.id.invite_name);
        final TextView conferenceAuthor = (TextView)rowView.findViewById(R.id.invite_author);
        TextView conferenceDate = (TextView)rowView.findViewById(R.id.invite_date);

        conferenceName.setText(conferenceNames.get(position));
        conferenceAuthor.setText("Created by:" + conferenceProposal.get(position));
        conferenceDate.setText(conferenceStart.get(position) + " - " + conferenceEnd.get(position));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createConference = new Intent(context, ConferenceActivityAdmin.class);
                createConference.putExtra("Name", conferenceNames.get(position));
                createConference.putExtra("Author", conferenceProposal.get(position));
                createConference.putExtra("Description", conferenceDescriptions.get(position));
                createConference.putExtra("StartDate", conferenceStart.get(position));
                createConference.putExtra("EndDate", conferenceEnd.get(position));
                ((Activity)context).startActivityForResult(createConference, FragmentConferences.CONFERENCE_CREATED);
            }
        });
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Do you want to remove conference?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                AccountManager.mydatabase.execSQL("DELETE FROM Conferences where lower(Name)='"
                                        + conferenceNames.get(position).toLowerCase() + "' and lower(Author)='" +
                                        conferenceProposal.get(position).toLowerCase() + "';");
                                mListener.onDeleted();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                // show it
                alertDialog.show();
                return false;
            }
        });
        return rowView;
    }

}