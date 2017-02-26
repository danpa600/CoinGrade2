/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.djp.coingrade2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.camera2.params.StreamConfigurationMap;

import com.example.djp.coingrade2.data.PetContract.PetEntry;
import com.example.djp.coingrade2.data.PetContract.CoinSeriesEntry;
import com.example.djp.coingrade2.data.PetContract.LincolnSeriesEntry;
import com.example.djp.coingrade2.data.PetContract.FlyingEagleSeriesEntry;
import com.example.djp.coingrade2.data.PetContract.TwoCentSeriesEntry;

/**
 * Database helper for Pets app. Manages database creation and version management.
 */
public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "coin.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);

        String SQL_CREATE_LINCOLN_SERIES_TABLE =  "CREATE TABLE " + LincolnSeriesEntry.TABLE_NAME + " ("
                + CoinSeriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CoinSeriesEntry.COLUMN_ISSUE + " TEXT NOT NULL, "
                + CoinSeriesEntry.COLUMN_GRADE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_NOTES + " TEXT, "
                + CoinSeriesEntry.COLUMN_GRADE_OBVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_GRADE_REVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_IMAGE_OBVERSE + " TEXT, "
                + CoinSeriesEntry.COLUMN_IMAGE_REVERSE + " TEXT );";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_LINCOLN_SERIES_TABLE);
        addLincolnIssues(db);

        // Create the database tables for the flying eagle cents
        String SQL_CREATE_FLYING_EAGLE_SERIES_TABLE =  "CREATE TABLE " + FlyingEagleSeriesEntry.TABLE_NAME + " ("
                + CoinSeriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CoinSeriesEntry.COLUMN_ISSUE + " TEXT NOT NULL, "
                + CoinSeriesEntry.COLUMN_GRADE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_NOTES + " TEXT, "
                + CoinSeriesEntry.COLUMN_GRADE_OBVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_GRADE_REVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_IMAGE_OBVERSE + " TEXT, "
                + CoinSeriesEntry.COLUMN_IMAGE_REVERSE + " TEXT );";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_FLYING_EAGLE_SERIES_TABLE);
        addFlyingEagleIssues(db);

        // Create the database tables for the Two cent pieces
        String SQL_CREATE_TWO_CENT_SERIES_TABLE =  "CREATE TABLE " + TwoCentSeriesEntry.TABLE_NAME + " ("
                + CoinSeriesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CoinSeriesEntry.COLUMN_ISSUE + " TEXT NOT NULL, "
                + CoinSeriesEntry.COLUMN_GRADE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_NOTES + " TEXT, "
                + CoinSeriesEntry.COLUMN_GRADE_OBVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_GRADE_REVERSE + " INTEGER, "
                + CoinSeriesEntry.COLUMN_IMAGE_OBVERSE + " TEXT, "
                + CoinSeriesEntry.COLUMN_IMAGE_REVERSE + " TEXT );";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_TWO_CENT_SERIES_TABLE);
        addTwoCentIssues(db);
    }

    private void addLincolnIssues(SQLiteDatabase db) {
        //String SQL_LINCOLN = "INSERT INTO " + LincolnSeriesEntry.TABLE_NAME
        //        + " (" + LincolnSeriesEntry.COLUMN_ISSUE + ") VALUES (1909);";
        ContentValues values = new ContentValues();
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1909 V.D.B.");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1909S V.D.B.");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1909");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1909S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1909S over Horiz S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1910");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1910S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1911");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1911D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1911S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1912");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1912D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1912S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1913");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1913D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1913S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1914");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1914D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1914S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1915");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1915D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1915S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1916");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1916D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1916S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1917");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1917 DD Obverse");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1917D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1917S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1918");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1918D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1918S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1919");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1919D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1919S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1920");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1920D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1920S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1921");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1921S");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1922D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1922 No D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
        values.put(LincolnSeriesEntry.COLUMN_ISSUE, "1922 Weak D");
        db.insert(LincolnSeriesEntry.TABLE_NAME, LincolnSeriesEntry.COLUMN_ISSUE, values);
   }


    private void addFlyingEagleIssues(SQLiteDatabase db) {
        //String SQL_LINCOLN = "INSERT INTO " + LincolnSeriesEntry.TABLE_NAME
        //        + " (" + LincolnSeriesEntry.COLUMN_ISSUE + ") VALUES (1909);";
        ContentValues values = new ContentValues();
        values.put(FlyingEagleSeriesEntry.COLUMN_ISSUE, "1856");
        db.insert(FlyingEagleSeriesEntry.TABLE_NAME, FlyingEagleSeriesEntry.COLUMN_ISSUE, values);
        values.put(FlyingEagleSeriesEntry.COLUMN_ISSUE, "1857");
        db.insert(FlyingEagleSeriesEntry.TABLE_NAME, FlyingEagleSeriesEntry.COLUMN_ISSUE, values);
        values.put(FlyingEagleSeriesEntry.COLUMN_ISSUE, "1858, Lg Ltrs");
        db.insert(FlyingEagleSeriesEntry.TABLE_NAME, FlyingEagleSeriesEntry.COLUMN_ISSUE, values);
        values.put(FlyingEagleSeriesEntry.COLUMN_ISSUE, "1858, 8/7");
        db.insert(FlyingEagleSeriesEntry.TABLE_NAME, FlyingEagleSeriesEntry.COLUMN_ISSUE, values);
        values.put(FlyingEagleSeriesEntry.COLUMN_ISSUE, "1858, Sm Ltrs");
        db.insert(FlyingEagleSeriesEntry.TABLE_NAME, FlyingEagleSeriesEntry.COLUMN_ISSUE, values);
    }

    private void addTwoCentIssues(SQLiteDatabase db) {
        //String SQL_LINCOLN = "INSERT INTO " + LincolnSeriesEntry.TABLE_NAME
        //        + " (" + LincolnSeriesEntry.COLUMN_ISSUE + ") VALUES (1909);";
        ContentValues values = new ContentValues();
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1864 Small Motto");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1864 Large Motto");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1865");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1866");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1867");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1867 DblDie Obverse");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1868");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1869");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1870");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1871");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1872");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1873 Close 3");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
        values.put(TwoCentSeriesEntry.COLUMN_ISSUE, "1873 Open 3, Restrike");
        db.insert(TwoCentSeriesEntry.TABLE_NAME, TwoCentSeriesEntry.COLUMN_ISSUE, values);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_PETS_TABLE = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;
        String SQL_DELETE_LINCOLN_SERIES_TABLE = "DROP TABLE IF EXISTS " + LincolnSeriesEntry.TABLE_NAME;
        String SQL_DELETE_FLYING_EAGLE_SERIES_TABLE = "DROP TABLE IF EXISTS " + FlyingEagleSeriesEntry.TABLE_NAME;
        String SQL_DELETE_TWO_CENT_SERIES_TABLE = "DROP TABLE IF EXISTS " + TwoCentSeriesEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_PETS_TABLE);
        db.execSQL(SQL_DELETE_LINCOLN_SERIES_TABLE);
        db.execSQL(SQL_DELETE_FLYING_EAGLE_SERIES_TABLE);
        db.execSQL(SQL_DELETE_TWO_CENT_SERIES_TABLE);
        onCreate(db);
        // The database is still at version 1, so there's nothing to do be done here.
    }
}