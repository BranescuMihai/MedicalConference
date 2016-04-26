package com.crossover.medicalconference;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Mihai Branescu on 4/24/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginTest {

    private EditText emailView, passwordView;
    private Button button;

//    @Before
//    public void setUp() {
//        Activity activity = Robolectric.setupActivity(LoginActivity.class);
//        button = (Button) activity.findViewById(R.id.email_sign_in_button);
//        emailView = (EditText) activity.findViewById(R.id.email);
//        passwordView = (EditText) activity.findViewById(R.id.password);
//    }
//
//    @Test
//    public void loginSuccess() {
//        emailView.setText("a@g.com");
//        passwordView.setText("qwerty");
//        button.performClick();
//
//        ShadowApplication application = shadowOf(RuntimeEnvironment.application);
//        assertThat("Next activity has started", application.getNextStartedActivity(), is(notNullValue()));
//    }


}