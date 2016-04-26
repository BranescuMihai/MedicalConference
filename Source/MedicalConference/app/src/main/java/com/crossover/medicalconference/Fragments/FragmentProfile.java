package com.crossover.medicalconference.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class FragmentProfile extends Fragment {

    private String firstName, lastName, password, retypePassword, email;
    private EditText mEmail, mFirst, mLast, mPass, mRetypePass;
    private Button mSave;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        return inflater.inflate(
                R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEmail = (EditText) view.findViewById(R.id.email);
        mFirst = (EditText) view.findViewById(R.id.first_name);
        mLast = (EditText) view.findViewById(R.id.last_name);
        mPass = (EditText) view.findViewById(R.id.password);
        mRetypePass = (EditText) view.findViewById(R.id.retype_password);

        mEmail.setText(AccountManager.email);
        mFirst.setText(AccountManager.firstName);
        mLast.setText((AccountManager.lastName));

        mSave = (Button)view.findViewById(R.id.save_button);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySave();
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void trySave() {

        mEmail.setError(null);
        mPass.setError(null);
        mFirst.setError(null);
        mLast.setError(null);
        mRetypePass.setError(null);

        email = mEmail.getText().toString();
        password = mPass.getText().toString();
        firstName = mFirst.getText().toString();
        lastName = mLast.getText().toString();
        retypePassword = mRetypePass.getText().toString();

        if(!password.equals(retypePassword)) {
            Toast.makeText(getActivity(),"Passwords don't match", Toast.LENGTH_SHORT).show();
            mPass.setText("");
            mRetypePass.setText("");
        } else if(!isPasswordValid(password)) {
            mPass.setError("Password too short");
            mPass.setText("");
            mRetypePass.setText("");
        } else if(!isEmailValid(email)) {
            mEmail.setError("Email not valid");
            mEmail.setText("");
        } else if(TextUtils.isEmpty(firstName)) {
            mFirst.setError("First name too short");
            mFirst.setText("");
        } else if(TextUtils.isEmpty(lastName)) {
            mLast.setError("Last name too short");
            mLast.setText("");
        } else {
            AccountManager.mydatabase.execSQL("DELETE FROM Accounts where lower(Email)='"
                    + email.toLowerCase() + "';");
            AccountManager.mydatabase.execSQL("INSERT INTO Accounts VALUES('" + email + "','" + password + "','" +
                    firstName + "','" + lastName + "'," + 0 + ");");

            Toast.makeText(getActivity(),"User successfuly saved", Toast.LENGTH_SHORT).show();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_place, new FragmentTopics());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}
