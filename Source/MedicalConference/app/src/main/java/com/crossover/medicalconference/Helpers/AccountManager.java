package com.crossover.medicalconference.Helpers;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mihai Branescu on 4/23/2016.
 */
public class AccountManager {
    public static String lastName, firstName, email;
    public static String isAdmin;
    public static String specialty;
    public static String[] specialties = {"Immunology", "Anesthesiology", "Dermatology",
            "Emergency Medicine", "Family Medicine", "Internal Medicine", "Plastic Surgery",
            "Psychiatry", "Neurology", "Thoracic Surgery", "Urology"};
    public static SQLiteDatabase mydatabase;
}
