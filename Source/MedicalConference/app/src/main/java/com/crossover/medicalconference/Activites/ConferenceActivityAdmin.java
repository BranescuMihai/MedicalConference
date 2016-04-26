package com.crossover.medicalconference.Activites;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

import java.util.Calendar;

public class ConferenceActivityAdmin extends AppCompatActivity {

    private EditText mName, mDescription, mAuthor, mStart, mEnd;
    private String name, description, author;
    private boolean isBeingCreated = true;
    private String startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_activity_admin);


        mName = (EditText) findViewById(R.id.conf_name);
        mDescription = (EditText) findViewById(R.id.conf_description);
        mAuthor = (EditText) findViewById(R.id.conf_author);
        mStart = (EditText) findViewById(R.id.start);
        mEnd = (EditText) findViewById(R.id.end);

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentDate = Calendar.getInstance();
                int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentDate.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(ConferenceActivityAdmin.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        startDate = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                        mStart.setText(startDate);
                    }
                },hour,minute, DateFormat.is24HourFormat(ConferenceActivityAdmin.this));


                mTimePicker.setTitle("Select date");
                mTimePicker.show();
            }
        });

        mEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentDate = Calendar.getInstance();
                int hour = mCurrentDate.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentDate.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(ConferenceActivityAdmin.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        endDate = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                        mEnd.setText(endDate);
                    }
                },hour,minute, DateFormat.is24HourFormat(ConferenceActivityAdmin.this));


                mTimePicker.setTitle("Select date");
                mTimePicker.show();
            }
        });

        mAuthor.setKeyListener(null);
       // mStart.setKeyListener(null);
       // mEnd.setKeyListener(null);

        Button mSave = (Button) findViewById(R.id.create_conference_button);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySave();
            }
        });

        name = getIntent().getStringExtra("Name");
        if(name != null) {
            mName.setText(name);
            description = getIntent().getStringExtra("Description");
            mDescription.setText(description);
            author = getIntent().getStringExtra("Author");
            mAuthor.setText(author);
            startDate = getIntent().getStringExtra("StartDate");
            mStart.setText(startDate);
            endDate = getIntent().getStringExtra("EndDate");
            mEnd.setText(endDate);
            mSave.setText("Save changes");
            isBeingCreated = false;
        }
        else {
            mAuthor.setText(AccountManager.firstName + " " + AccountManager.lastName);
        }

        Button mInvite = (Button) findViewById(R.id.invite_doctors_button);
        mInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryInvite();
            }
        });

        Button mListButton = (Button) findViewById(R.id.list_of_accepted_invites);
        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySeeAccepted();
            }
        });
    }

    private boolean isNameValid(String name) {
        return name.length() > 5;
    }

    private boolean isDescriptionValid(String description) {
        return description.length() > 20;
    }

    private void trySave() {

        mName.setError(null);
        mDescription.setError(null);
        mStart.setError(null);
        mEnd.setError(null);

        name = mName.getText().toString();
        author = AccountManager.firstName + " " + AccountManager.lastName;
        description = mDescription.getText().toString();
        startDate = mStart.getText().toString();
        endDate = mEnd.getText().toString();

        if(!isNameValid(name)) {
            mName.setError("Name too short");
        } else if (!isDescriptionValid(description)) {
            mDescription.setError("Description too short");
        } else if (startDate.equals("")) {
            mStart.setError("Start date is necessary");
        } else if (endDate.equals("")) {
            mEnd.setError("End date is necessary");
        } else{
            AccountManager.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Conferences(Name VARCHAR not null unique,Author VARCHAR," +
                    "Description VARCHAR,SDate VARCHAR,EDate VARCHAR);");

            String[] split = name.split(" ");
            name = "";
            for(int i = 0; i < split.length; i++) {
                name += split[i] + "_";
            }
            name = name.substring(0,name.length()-1);

            if(!isBeingCreated) {
                Toast.makeText(this, "Conference successfully saved", Toast.LENGTH_SHORT).show();
                AccountManager.mydatabase.execSQL("DELETE FROM Conferences where lower(Name)='"
                        + getIntent().getStringExtra("Name").toLowerCase() + "' and lower(Author)='" +
                        getIntent().getStringExtra("Author").toLowerCase() + "';");
                try {
                    AccountManager.mydatabase.execSQL("INSERT INTO Conferences VALUES('" + name + "','" + author + "','" +
                            description + "','" + startDate + "','" + endDate + "');");
                } catch(SQLException ex) {
                    Toast.makeText(this, "Conference already exists", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                try {
                    AccountManager.mydatabase.execSQL("INSERT INTO Conferences VALUES('" + name + "','" + author + "','" +
                            description  + "','" + startDate + "','" + endDate + "');");
                } catch(SQLException ex) {
                    Toast.makeText(this, "Conference already exists", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Conference successfully created", Toast.LENGTH_SHORT).show();

                AccountManager.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Invite" + name +"(Name VARCHAR not null unique,Email VARCHAR," +
                        "Accepted VARCHAR);");
            }

            finish();
        }
    }

    private void tryInvite() {
        Intent invite = new Intent(ConferenceActivityAdmin.this, InviteActivityAdmin.class);
        invite.putExtra("ConfName", name);
        invite.putExtra("ConfDescription", description);
        invite.putExtra("ConfStart", startDate);
        invite.putExtra("ConfEnd", endDate);
        startActivity(invite);
    }

    private void trySeeAccepted() {
        Intent accepted = new Intent(ConferenceActivityAdmin.this, AcceptedDoctorsActivity.class);
        accepted.putExtra("ConfName", name);
        startActivity(accepted);
    }

}
