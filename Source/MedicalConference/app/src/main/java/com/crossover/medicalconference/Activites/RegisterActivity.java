package com.crossover.medicalconference.Activites;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.crossover.medicalconference.Helpers.AccountManager;
import com.crossover.medicalconference.R;

public class RegisterActivity extends AppCompatActivity {

    private String firstName, lastName, password, retypePassword, email;
    private EditText mEmail, mFirst, mLast, mPass, mRetypePass;
    private Button mRegister;
    private CheckBox mCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEmail = (EditText) findViewById(R.id.email);
        mFirst = (EditText) findViewById(R.id.first_name);
        mLast = (EditText) findViewById(R.id.last_name);
        mPass = (EditText) findViewById(R.id.password);
        mRetypePass = (EditText) findViewById(R.id.retype_password);
        mCheck = (CheckBox) findViewById(R.id.check_admin);
        mRegister = (Button)findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryRegister();
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void tryRegister() {

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
            Toast.makeText(this,"Passwords don't match", Toast.LENGTH_SHORT).show();
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

            AccountManager.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Accounts(Email VARCHAR unique not null,Password VARCHAR," +
                    "FirstName VARCHAR,LastName VARCHAR, IsAdmin NUMBER);");
            if(mCheck.isChecked()) {
                try {
                    AccountManager.mydatabase.execSQL("INSERT INTO Accounts VALUES('" + email + "','" + password + "','" +
                            firstName + "','" + lastName + "'," + 1 + ");");
                } catch (SQLException ex) {
                    Toast.makeText(this, "User already existed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else {
                try {
                    AccountManager.mydatabase.execSQL("INSERT INTO Accounts VALUES('" + email + "','" + password + "','" +
                            firstName + "','" + lastName + "'," + 0 + ");");
                } catch (SQLException ex) {
                    Toast.makeText(this, "User already existed", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            AccountManager.mydatabase.execSQL("CREATE TABLE IF NOT EXISTS PersonalInvite" + firstName + lastName +
                    "(ConfName VARCHAR,InvBy VARCHAR,Description VARCHAR,StartDate VARCHAR, EndDate VARCHAR);");

            Toast.makeText(this,"User successfully created", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
