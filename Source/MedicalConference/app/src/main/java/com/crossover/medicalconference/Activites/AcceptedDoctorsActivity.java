package com.crossover.medicalconference.Activites;

import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

import java.util.ArrayList;

public class AcceptedDoctorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_doctors);

        ListView acceptedInvites = (ListView)findViewById(R.id.accepted_invites_list);
        ArrayList<String> names = new ArrayList<>();

        Cursor resultSet = null;
        try {
            resultSet = AccountManager.mydatabase.rawQuery("Select * from Invite" + getIntent().getStringExtra("ConfName"), null);
        } catch(SQLException ex) {
            Toast.makeText(this, "No doctors were invited", Toast.LENGTH_SHORT).show();
        }

        if(resultSet != null) {
        resultSet.moveToFirst();

        //iterate over rows
        for (int i = 0; i < resultSet.getCount(); i++) {
            String name = resultSet.getString(0);
            String accepted = resultSet.getString(2);
            if(accepted.equals("1"))
                names.add(name);
            //move to the next row
            resultSet.moveToNext();
        }
        //close the cursor
        resultSet.close();
        }

        if(names.size() == 0) {
            Toast.makeText(this, "No doctor accepted yet", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        acceptedInvites.setAdapter(adapter);
    }
}
