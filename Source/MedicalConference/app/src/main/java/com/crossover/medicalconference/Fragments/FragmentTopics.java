package com.crossover.medicalconference.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.Adapters.CustomListAdapterTopic;
import com.crossover.medicalconference.Helpers.SpinnerTrigger;
import com.crossover.medicalconference.R;

import java.lang.reflect.Field;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class FragmentTopics extends Fragment {

    private ListView mTopics;
    private EditText mCategory;
    private SpinnerTrigger mCategorySpinner;
    private boolean firstTimeSpinner = true;
    private String category;
    private String[] names, proposedBy, descriptions;
    private CustomListAdapterTopic adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopics = (ListView)view.findViewById(R.id.topic_list);

        mCategorySpinner = (SpinnerTrigger) view.findViewById(R.id.topic_category);
        ArrayAdapter<String> categoryAdapter =  new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, AccountManager.specialties);
        mCategorySpinner.setAdapter(categoryAdapter);
        mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(firstTimeSpinner) {
                    firstTimeSpinner = false;
                } else {
                    category = AccountManager.specialties[i];
                    mCategory.setText(category);
                    Cursor resultSet = null;
                    try {
                        resultSet = AccountManager.mydatabase.rawQuery("Select * from Topics where lower(Category)='"
                                + category.toLowerCase() + "'", null);
                    }catch (SQLiteException ex) {

                    }

                    if(resultSet != null) {
                        resultSet.moveToFirst();
                        //Log.d("Topic1",resultSet.getString(0)+"   "+ resultSet.getString(3));
                        names = new String[resultSet.getCount()];
                        proposedBy = new String[resultSet.getCount()];
                        descriptions = new String[resultSet.getCount()];
                        //iterate over rows
                        for (int k = 0; k < resultSet.getCount(); k++) {
                            names[k] = resultSet.getString(0);
                            descriptions[k] = resultSet.getString(2);
                            proposedBy[k] = resultSet.getString(3);
                            //move to the next row
                            resultSet.moveToNext();
                        }
                        //close the cursor
                        resultSet.close();
                        adapter = new CustomListAdapterTopic(getActivity(),names,proposedBy,descriptions);
                        mTopics.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        category = AccountManager.specialties[0];

        mCategory = (EditText)view.findViewById(R.id.topic_category_et);

        mCategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mCategorySpinner.performClick();
                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCategory.setShowSoftInputOnFocus(false);
        }

    }
}
