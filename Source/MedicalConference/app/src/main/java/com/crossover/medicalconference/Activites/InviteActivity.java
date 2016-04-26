package com.crossover.medicalconference.Activites;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

import java.util.Calendar;

public class InviteActivity extends AppCompatActivity {

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference);
        title = getIntent().getStringExtra("Title");
        final String description = getIntent().getStringExtra("Description");
        TextView mTitle = (TextView) findViewById(R.id.title);
        TextView mDescription = (TextView) findViewById(R.id.description);

        if (mTitle != null) {
            mTitle.setText(title);
        }
        if (mDescription != null) {
            mDescription.setText(description);
        }

        Button accept = (Button)findViewById(R.id.accept);
        if (accept != null) {
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = AccountManager.firstName + " " + AccountManager.lastName;

                    AccountManager.mydatabase.execSQL("DELETE FROM Invite" + title + " where lower(Name)='"
                            + name.toLowerCase() + "';");
                    AccountManager.mydatabase.execSQL("INSERT INTO Invite" + title + " VALUES('" + name + "','" +
                            AccountManager.email + "','" + 1 + "');");
                    AccountManager.mydatabase.execSQL("DELETE FROM PersonalInvite" + AccountManager.firstName +
                            AccountManager.lastName + " where lower(ConfName)='" + title.toLowerCase() + "';");

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");

                    String start = getIntent().getStringExtra("StartDate");
                    String end = getIntent().getStringExtra("EndDate");
                    String[] splitStart = start.split(":");
                    String[] splitEnd = end.split(":");

                    Calendar beginCal = Calendar.getInstance();
                    Calendar endCal = Calendar.getInstance();

                    beginCal.set(beginCal.get(Calendar.YEAR), beginCal.get(Calendar.MONTH),
                            beginCal.get(Calendar.DAY_OF_MONTH), Integer.parseInt(splitStart[0]),
                            Integer.parseInt(splitStart[1]));
                    long starting = beginCal.getTimeInMillis();

                    endCal.set(beginCal.get(Calendar.YEAR), beginCal.get(Calendar.MONTH),
                            beginCal.get(Calendar.DAY_OF_MONTH), Integer.parseInt(splitEnd[0]),
                            Integer.parseInt(splitEnd[1]));
                    long ending = endCal.getTimeInMillis();


//                    long starting = Integer.parseInt(splitStart[0]) * 3600 * 1000 +
//                            Integer.parseInt(splitStart[1]) * 60 * 1000;
//
//                    long ending = Integer.parseInt(splitEnd[0]) * 3600 * 1000 +
//                            Integer.parseInt(splitEnd[1]) * 60 * 1000;

                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, starting);
                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, ending);
                    intent.putExtra(CalendarContract.Events.TITLE, title);
                    intent.putExtra(CalendarContract.Events.DESCRIPTION,  description);

                    startActivity(intent);
                    finish();
                }
            });
        }
        Button decline = (Button)findViewById(R.id.decline);
        if (decline != null) {
            decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = AccountManager.firstName + " " + AccountManager.lastName;

                    AccountManager.mydatabase.execSQL("DELETE FROM Invite" + title + " where lower(Name)='"
                            + name.toLowerCase() + "';");
                    AccountManager.mydatabase.execSQL("INSERT INTO Invite" + title + " VALUES('" + name + "','" +
                            AccountManager.email + "','" + 2 + "');");
                    AccountManager.mydatabase.execSQL("DELETE FROM PersonalInvite" + AccountManager.firstName +
                            AccountManager.lastName + " where lower(ConfName)='" + title.toLowerCase() + "';");
                    finish();
                }
            });
        }


    }
}
