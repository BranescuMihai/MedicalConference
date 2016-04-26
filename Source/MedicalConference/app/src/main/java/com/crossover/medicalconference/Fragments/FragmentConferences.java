package com.crossover.medicalconference.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.Activites.ConferenceActivityAdmin;
import com.crossover.medicalconference.Adapters.CustomListAdapterConferences;
import com.crossover.medicalconference.R;

import java.util.ArrayList;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class FragmentConferences extends Fragment implements CustomListAdapterConferences.OnDeletedListener {

    private ArrayList<String> names, authors, descriptions, start, end;
    private CustomListAdapterConferences adapter;
    public static final int CONFERENCE_CREATED = 1;
    private ListView mConferences;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_conferences, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConferences = (ListView)view.findViewById(R.id.list_conferences);

        Button create = (Button) view.findViewById(R.id.create_conference);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createConference = new Intent(getActivity(), ConferenceActivityAdmin.class);
                startActivityForResult(createConference, CONFERENCE_CREATED);
            }
        });

        Cursor resultSet = null;
        try {
            resultSet = AccountManager.mydatabase.rawQuery("Select * from Conferences", null);
        }catch(SQLiteException e) {
            Toast.makeText(getActivity(), "No conferences available", Toast.LENGTH_SHORT).show();
        }

        if(resultSet != null) {
            resultSet.moveToFirst();
            names = new ArrayList<>();
            authors = new ArrayList<>();
            descriptions = new ArrayList<>();
            start = new ArrayList<>();
            end = new ArrayList<>();

            //iterate over rows
            for (int k = 0; k < resultSet.getCount(); k++) {
                names.add(k,resultSet.getString(0));
                authors.add(k,resultSet.getString(1));
                descriptions.add(k,resultSet.getString(2));
                start.add(k,resultSet.getString(3));
                end.add(k,resultSet.getString(4));
                //move to the next row
                resultSet.moveToNext();
            }
            //close the cursor
            resultSet.close();
            adapter = new CustomListAdapterConferences(getActivity(),names,authors,descriptions,start,end, this);
            mConferences.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == CONFERENCE_CREATED) {

            if(names != null) {
                names.clear();
                authors.clear();
                descriptions.clear();
                start.clear();
                end.clear();
            }

            Cursor resultSet = null;
            try {
                resultSet = AccountManager.mydatabase.rawQuery("Select * from Conferences", null);
            } catch (SQLiteException e) {
                Toast.makeText(getActivity(), "No conferences available", Toast.LENGTH_SHORT).show();
            }

            if (resultSet != null) {
                resultSet.moveToFirst();

                if(names == null) {
                    names = new ArrayList<>();
                    authors = new ArrayList<>();
                    descriptions = new ArrayList<>();
                    start = new ArrayList<>();
                    end = new ArrayList<>();

                    adapter = new CustomListAdapterConferences(getActivity(), names, authors, descriptions, start, end, this);
                    mConferences.setAdapter(adapter);
                }

                //iterate over rows
                for (int k = 0; k < resultSet.getCount(); k++) {
                    names.add(k, resultSet.getString(0));
                    authors.add(k, resultSet.getString(1));
                    descriptions.add(k, resultSet.getString(2));
                    start.add(k, resultSet.getString(3));
                    end.add(k, resultSet.getString(4));
                    //move to the next row
                    resultSet.moveToNext();
                }
                //close the cursor
                resultSet.close();
            }
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onDeleted() {
        names.clear();
        authors.clear();
        descriptions.clear();
        start.clear();
        end.clear();

        Cursor resultSet = null;
        try {
            resultSet = AccountManager.mydatabase.rawQuery("Select * from Conferences", null);
        }catch(SQLiteException e) {
            Toast.makeText(getActivity(), "No conferences available", Toast.LENGTH_SHORT).show();
        }

        if(resultSet != null) {
            resultSet.moveToFirst();
            //iterate over rows
            for (int k = 0; k < resultSet.getCount(); k++) {
                names.add(k, resultSet.getString(0));
                authors.add(k, resultSet.getString(1));
                descriptions.add(k, resultSet.getString(2));
                start.add(k, resultSet.getString(3));
                end.add(k, resultSet.getString(4));
                //move to the next row
                resultSet.moveToNext();
            }
            //close the cursor
            resultSet.close();
        }
        adapter.notifyDataSetChanged();
    }
}
