package com.crossover.medicalconference.Activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.crossover.medicalconference.R;

public class TopicActivity extends AppCompatActivity {

    private TextView mTitle, mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        String title = getIntent().getStringExtra("Title");
        String description = getIntent().getStringExtra("Description");
        mTitle = (TextView) findViewById(R.id.title);
        mDescription = (TextView) findViewById(R.id.description);
        mTitle.setText(title);
        mDescription.setText(description);
    }
}
