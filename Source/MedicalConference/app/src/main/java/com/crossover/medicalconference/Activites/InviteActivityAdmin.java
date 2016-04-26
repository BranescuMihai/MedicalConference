package com.crossover.medicalconference.Activites;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

import java.util.ArrayList;

public class InviteActivityAdmin extends AppCompatActivity {

    private CustomAdapterInviteDoctors dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_doctors);

        //Generate list View from ArrayList
        displayListView();
        checkButtonClick();

    }

    private class Doctor {

        String name;
        String email;
        boolean isChecked;

        Doctor(String name, boolean isChecked, String email) {
            this.name = name;
            this.email = email;
            this.isChecked = isChecked;
        }
    }

    private void displayListView() {

        //Array list of countries
        ArrayList<Doctor> doctorsList = new ArrayList<>();
        ArrayList<String> dummy = new ArrayList<>();

        Cursor resultSet = AccountManager.mydatabase.rawQuery("Select * from Accounts",null);
        if(resultSet != null) {
            resultSet.moveToFirst();

            //iterate over rows
            for (int i = 0; i < resultSet.getCount(); i++) {
                //add only doctors, not admins
                if(resultSet.getInt(4) == 0) {
                    String name = resultSet.getString(2) + " " + resultSet.getString(3);
                    String email = resultSet.getString(0);
                    doctorsList.add(new Doctor(name, false, email));
                    dummy.add(name);
                }
                //move to the next row
                resultSet.moveToNext();
            }
            //close the cursor
            resultSet.close();
        }

        resultSet = AccountManager.mydatabase.rawQuery("Select * from Invite" + getIntent().getStringExtra("ConfName"),null);
        if(resultSet != null) {
            resultSet.moveToFirst();

            //iterate over rows
            for (int i = 0; i < resultSet.getCount(); i++) {
                String name = resultSet.getString(0);
                for(int j = 0; j < doctorsList.size(); j++)
                    if(doctorsList.get(j).name.equals(name)) {
                        doctorsList.remove(j);
                        dummy.remove(j);
                    }
                //move to the next row
                resultSet.moveToNext();
            }
            //close the cursor
            resultSet.close();
        }

        //create an ArrayAdapter from the String Array
        dataAdapter = new CustomAdapterInviteDoctors(this,
                R.layout.invite_doctors_list_element, dummy, doctorsList);
        ListView listView = (ListView) findViewById(R.id.list_of_invites);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
    }

    private class CustomAdapterInviteDoctors extends ArrayAdapter<String> {

        private ArrayList<Doctor> doctorsList;

        public CustomAdapterInviteDoctors(Context context, int textViewResourceId, ArrayList<String> dummy,
                                          ArrayList<Doctor> doctorsList) {
            super(context, textViewResourceId, dummy);
            this.doctorsList = doctorsList;
        }

        private class ViewHolder {
            TextView name;
            CheckBox check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.invite_doctors_list_element, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name_of_doctor);
                holder.check = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);

                holder.check.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Doctor doc = (Doctor) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        doc.isChecked = cb.isChecked();
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Doctor doc = doctorsList.get(position);
            holder.name.setText(doc.name);
            holder.check.setChecked(doc.isChecked);
            holder.check.setTag(doc);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.send_invites_button);
        if (myButton != null) {
            myButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    StringBuffer responseText = new StringBuffer();
                    responseText.append("The following were selected...\n");

                    ArrayList<Doctor> doctorsList = dataAdapter.doctorsList;
                    for(int i=0; i< doctorsList.size();i++){
                        Doctor doc = doctorsList.get(i);
                        if(doc.isChecked){
                            try {
                                String[] split = doc.name.split(" ");
                                String name = split[0]+split[1];
                                AccountManager.mydatabase.execSQL("INSERT INTO Invite" + getIntent().getStringExtra("ConfName")
                                        + " VALUES('" + doc.name + "','" + doc.email + "','" + 0 + "');");
                                AccountManager.mydatabase.execSQL("INSERT INTO PersonalInvite" + name
                                        + " VALUES('" + getIntent().getStringExtra("ConfName") + "','" +
                                        AccountManager.firstName + " " + AccountManager.lastName + "','" +
                                        getIntent().getStringExtra("ConfDescription") + "','" +
                                        getIntent().getStringExtra("ConfStart") + "','" +
                                        getIntent().getStringExtra("ConfEnd") + "');");
                            } catch(SQLException ex) {

                            }
                            responseText.append("\n" + doc.name);
                        }
                    }

                    Toast.makeText(getApplicationContext(),
                            responseText, Toast.LENGTH_LONG).show();

                    finish();
                }
            });
        }

    }

}
