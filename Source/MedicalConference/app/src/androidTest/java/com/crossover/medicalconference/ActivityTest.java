package com.crossover.medicalconference;

import android.test.ActivityInstrumentationTestCase2;

import com.crossover.medicalconference.Activites.LoginActivity;
import com.robotium.solo.Solo;

/**
 * Created by Mihai Branescu on 4/24/2016.
 */
public class ActivityTest extends
        ActivityInstrumentationTestCase2<LoginActivity> {

    private Solo solo;

    public ActivityTest() {
        super(LoginActivity.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void test01TryWrongPass() throws Exception {
        solo.enterText(0, "a@g.com");
        solo.enterText(1, "wrong");
        solo.clickOnButton("Sign in");
        assertTrue(solo.searchText("Email or password incorrect"));
    }

    public void test02TryInvalidEmail() throws Exception {
        solo.enterText(0, "invalidEmail");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        assertTrue(solo.searchText("This email address is invalid"));
    }

    public void test03PasswordTooShort() throws Exception {
        solo.enterText(0, "a@g.com");
        solo.enterText(1, "q");
        solo.clickOnButton("Sign in");
        assertTrue(solo.searchText("This password is too short"));
    }

    public void test04Register() throws Exception {
        solo.clickOnText("Don't have an account?");
        solo.waitForActivity("com.crossover.medicalconference.Activites.RegisterActivity", 2000);
        assertTrue(solo.searchText("Admin account"));
        solo.goBack();
    }

    public void test05RegisterSmallPass() throws Exception {
        solo.clickOnText("Don't have an account?");
        solo.waitForActivity("com.crossover.medicalconference.Activites.RegisterActivity", 2000);
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "FirstName");
        solo.enterText(2, "LastName");
        solo.enterText(3, "");
        solo.clickOnButton("Register");
        assertTrue(solo.searchText("Password too short"));
        solo.goBack();
    }

    public void test06RegisterCorrect() throws Exception {
        solo.clickOnText("Don't have an account?");
        solo.waitForActivity("com.crossover.medicalconference.Activites.RegisterActivity", 2000);
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "FirstName");
        solo.enterText(2, "LastName");
        solo.enterText(3, "qwerty");
        solo.enterText(4, "qwerty");
        solo.clickOnButton("Register");
        assertTrue(solo.searchText("User successfully created"));
    }


    public void test07LoginSuccessful() throws Exception {
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        assertTrue(solo.searchText("New Topic"));
        solo.goBack();
        solo.goBack();
    }

    public void test08NewTopicShortName() throws Exception {
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("New Topic");
        solo.clickOnText("New Topic");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.enterText(0,"Too");
        solo.clickOnButton("Share");
        assertTrue(solo.searchText("Name too short"));
        solo.goBack();
    }

    public void test09NewTopic() throws Exception {
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("New Topic");
        solo.clickOnText("New Topic");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.enterText(0,"Topic1");
        solo.clickOnText("Topic Category");
        solo.clickOnText("Dermatology");
        solo.enterText(2,"This topic will include a lot of subjects");
        solo.clickOnButton("Share");
        assertTrue(solo.searchText("Topic successfully shared"));
        solo.goBack();
    }

    public void test10CheckTopic() throws Exception {
        solo.enterText(0, "name@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("Topic List");
        solo.clickOnText("Topic List");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.clickOnText("Topic Category");
        solo.clickOnText("Dermatology");
        assertTrue(solo.searchText("Topic1"));
        solo.goBack();
    }


    public void test11RegisterCorrectAdmin() throws Exception {
        solo.clickOnText("Don't have an account?");
        solo.waitForActivity("com.crossover.medicalconference.Activites.RegisterActivity", 2000);
        solo.enterText(0, "admin@email.com");
        solo.enterText(1, "FirstAdmin");
        solo.enterText(2, "LastAdmin");
        solo.enterText(3, "qwerty");
        solo.enterText(4, "qwerty");
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Register");
        assertTrue(solo.searchText("User successfully created"));
    }

    public void test12CheckTopicAdmin() throws Exception {
        solo.enterText(0, "admin@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("Topic List");
        solo.clickOnText("Topic List");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.clickOnText("Topic Category");
        solo.clickOnText("Dermatology");
        assertTrue(solo.searchText("Topic1"));
        solo.goBack();
    }

    public void test13CreateConf() throws Exception {
        solo.enterText(0, "admin@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("Conference List");
        solo.clickOnText("Conference List");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.clickOnText("Create Conference");
        solo.enterText(0, "Conference1");
        solo.enterText(2, "This conference will be held by a great professor");
        solo.enterText(3,"15:30");
        solo.goBack();
        solo.enterText(4,"19:35");
        solo.goBack();
        solo.clickOnButton("Create");
        assertTrue(solo.searchText("Conference successfully created"));
        solo.goBack();
    }

    public void test14EditConf() throws Exception {
        solo.enterText(0, "admin@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("Conference List");
        solo.clickOnText("Conference List");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.clickOnText("Conference1");
        solo.enterText(0, "23");
        solo.clickOnButton("Save");
        assertTrue(solo.searchText("Conference successfully saved"));
        solo.goBack();
    }

    public void test15DelConf() throws Exception {
        solo.enterText(0, "admin@email.com");
        solo.enterText(1, "qwerty");
        solo.clickOnButton("Sign in");
        solo.waitForActivity("com.crossover.medicalconference.Activites.DoctorMain", 3000);
        solo.clickOnActionBarHomeButton();
        solo.clickOnImageButton(0);
        solo.waitForText("Conference List");
        solo.clickOnText("Conference List");
        solo.waitForFragmentById(R.id.fragment_place);
        solo.clickLongOnText("Conference123");
        solo.clickOnText("Yes");
        assertFalse(solo.searchText("Conference123"));
        solo.goBack();
    }


}