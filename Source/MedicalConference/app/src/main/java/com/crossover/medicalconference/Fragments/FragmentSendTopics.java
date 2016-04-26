package com.crossover.medicalconference.Fragments;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.Helpers.SpinnerTrigger;
import com.crossover.medicalconference.R;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class FragmentSendTopics extends Fragment {

    private EditText mName, mDescription, mCategory;
    private SpinnerTrigger mCategorySpinner;
    private String category;
    private boolean firstTimeSpinner = true;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_send_topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCategorySpinner = (SpinnerTrigger)view.findViewById(R.id.topic_category);
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
                }
                //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        category = AccountManager.specialties[0];

        Button sendTopic = (Button)view.findViewById(R.id.topic_send);
        sendTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySend();
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_place, new FragmentTopics());
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        mName = (EditText)view.findViewById(R.id.topic_name);
        mDescription = (EditText)view.findViewById(R.id.topic_description);
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

    private boolean isNameValid(String name) {
        return name.length() > 5;
    }

    private boolean isDescriptionValid(String description) {
        return description.length() > 20;
    }

    private void trySend() {
        mName.setError(null);
        mDescription.setError(null);

        String name = mName.getText().toString();
        String description = mDescription.getText().toString();

        if(!isNameValid(name))
            mName.setError("Name too short");
        else if(!isDescriptionValid(description))
            mDescription.setError("Description too short");
        else {
            AccountManager.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Topics(Name VARCHAR," +
                    "Category VARCHAR," + "Description VARCHAR," + "Proposed VARCHAR);");
            AccountManager.mydatabase.execSQL("INSERT INTO Topics VALUES('" + name + "','" + category + "','" +
                    description + "','" + AccountManager.firstName + " " + AccountManager.lastName + "');");

            mName.setText(null);
            mDescription.setText(null);
            mCategorySpinner.setSelection(0);
            mCategory.setText(null);

            Toast.makeText(getActivity(),"Topic successfully shared", Toast.LENGTH_SHORT).show();
        }
    }
}
