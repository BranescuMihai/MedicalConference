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
import android.widget.ListView;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.Adapters.CustomListAdapterInvites;
import com.crossover.medicalconference.R;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class FragmentInvites extends Fragment {

    private String[] names, invitedBy, descriptions, start, end;
    private ListView mConferences;
    private CustomListAdapterInvites adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_invites, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConferences = (ListView)view.findViewById(R.id.invites_list);
        Cursor resultSet = null;
        try {
            resultSet = AccountManager.mydatabase.rawQuery("Select * from PersonalInvite" +
                    AccountManager.firstName+AccountManager.lastName, null);
        }catch(SQLiteException e) {
            Toast.makeText(getActivity(), "No invites available", Toast.LENGTH_SHORT).show();
        }

        if(resultSet != null) {
            resultSet.moveToFirst();
            names = new String[resultSet.getCount()];
            invitedBy = new String[resultSet.getCount()];
            descriptions = new String[resultSet.getCount()];
            start = new String[resultSet.getCount()];
            end = new String[resultSet.getCount()];

            //iterate over rows
            for (int k = 0; k < resultSet.getCount(); k++) {
                names[k] = resultSet.getString(0);
                invitedBy[k] = resultSet.getString(1);
                descriptions[k] = resultSet.getString(2);
                start[k] = resultSet.getString(3);
                end[k] = resultSet.getString(4);
                //move to the next row
                resultSet.moveToNext();
            }
            //close the cursor
            resultSet.close();
            adapter = new CustomListAdapterInvites(getActivity(),names,invitedBy,descriptions, start, end);
            mConferences.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == CustomListAdapterInvites.INVITE_RESPONDED) {
//            Log.d("intra", "aic");
//            Cursor resultSet = null;
//            try {
//                resultSet = AccountManager.mydatabase.rawQuery("Select * from PersonalInvite" +
//                        AccountManager.firstName+AccountManager.lastName, null);
//            } catch (SQLiteException e) {
//                Toast.makeText(getActivity(), "No new invites available", Toast.LENGTH_SHORT).show();
//            }
//
//            if (resultSet != null) {
//                resultSet.moveToFirst();
//                Log.d("intra", resultSet.getCount()+"");
//                names = new String[resultSet.getCount()];
//                invitedBy = new String[resultSet.getCount()];
//                descriptions = new String[resultSet.getCount()];
//
//                //iterate over rows
//                for (int k = 0; k < resultSet.getCount(); k++) {
//                    names[k] = resultSet.getString(0);
//                    invitedBy[k] = resultSet.getString(1);
//                    descriptions[k] = resultSet.getString(2);
//                    Log.d("names", names[k] +" " + invitedBy[k]);
//                    //move to the next row
//                    resultSet.moveToNext();
//                }
//                //close the cursor
//                resultSet.close();
//            }
//            adapter.notifyDataSetChanged();
//        }
//    }
}
