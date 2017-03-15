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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.djp.coingrade2.App;
import com.example.djp.coingrade2.R;
import com.example.djp.coingrade2.data.PetContract.PetEntry;

/**
 * {@link ContentProvider} for Pets app.
 */
public class PetProvider extends ContentProvider {

    /** Database helper object */
    private PetDbHelper mDbHelper;

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;
    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;
    /** URI matcher code for the content URI for the Lincoln cent table */
    private static final int LINCOLN_CENTS = 102;
    private static final int LINCOLN_CENTS_ID = 103;
    /** URI matcher code for the content URI for the Flying Eagle cent table */
    private static final int FLYING_EAGLE_CENTS = 104;
    private static final int FLYING_EAGLE_CENTS_ID = 105;
    /** URI matcher code for the content URI for the Flying Eagle cent table */
    private static final int TWO_CENTS = 106;
    private static final int TWO_CENTS_ID = 107;
    /** URI matcher code for the content URI for the Three cent silver table */
    private static final int THREE_CENT_SILVER = 108;
    private static final int THREE_CENT_SILVER_ID = 109;
    /** URI matcher code for the content URI for the Three cent nickel table */
    private static final int THREE_CENT_NICKEL = 110;
    private static final int THREE_CENT_NICKEL_ID = 111;
    /** Tag for the log messages */
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#",PET_ID);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_LINCOLN_SERIES,LINCOLN_CENTS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_LINCOLN_SERIES + "/#",LINCOLN_CENTS_ID);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_FLYING_EAGLE_SERIES,FLYING_EAGLE_CENTS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_FLYING_EAGLE_SERIES + "/#",FLYING_EAGLE_CENTS_ID);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_TWO_CENT_SERIES,TWO_CENTS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_TWO_CENT_SERIES + "/#",TWO_CENTS_ID);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_THREE_CENT_SILVER_SERIES,THREE_CENT_SILVER);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_THREE_CENT_SILVER_SERIES + "/#",THREE_CENT_SILVER_ID);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_THREE_CENT_NICKEL_SERIES,THREE_CENT_NICKEL);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_THREE_CENT_NICKEL_SERIES + "/#",THREE_CENT_NICKEL_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LINCOLN_CENTS:
                cursor = database.query(PetContract.LincolnSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case LINCOLN_CENTS_ID:
                selection = PetContract.LincolnSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.LincolnSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FLYING_EAGLE_CENTS:
                cursor = database.query(PetContract.FlyingEagleSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FLYING_EAGLE_CENTS_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(PetContract.FlyingEagleSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TWO_CENTS:
                cursor = database.query(PetContract.TwoCentSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case TWO_CENTS_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(PetContract.TwoCentSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case THREE_CENT_SILVER:
                cursor = database.query(PetContract.ThreeCentSilverSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case THREE_CENT_SILVER_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(PetContract.ThreeCentSilverSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case THREE_CENT_NICKEL:
                cursor = database.query(PetContract.ThreeCentNickelSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case THREE_CENT_NICKEL_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(PetContract.ThreeCentNickelSeriesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default:
                throw new IllegalArgumentException("PET Cannot query unknown URI " + uri);
        }

        // Set notification URI on the Cursor,
        // so we know what content URI the Cursor was created for.
        // If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

     /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertPet(Uri uri, ContentValues values) {
        // Check that the name is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Check that the gender is one of the three valid values
        int gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (!PetEntry.isValidSeries(gender)) {
            throw new IllegalArgumentException("Pet gender must be set to Unknown, Female or Male.");
        }
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(PetEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners taht the data has changed for the pet content URI
        // uri: contnet://com.example.android.pets/pets
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return deletePet(uri, selection, selectionArgs);
            case PET_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return deletePet(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("delete is not supported for " + uri);
        }
    }

    private int deletePet (Uri uri, String selection, String[] selectionArgs) {
        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted = database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

        if (rowsDeleted != 0) {
            // Notify all listeners taht the data has changed for the pet content URI
            // uri: contnet://com.example.android.pets/pets
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            case LINCOLN_CENTS_ID:
                selection = PetContract.LincolnSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoin(uri, contentValues, selection, selectionArgs);
            case FLYING_EAGLE_CENTS_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoin(uri, contentValues, selection, selectionArgs);
            case TWO_CENTS_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoin(uri, contentValues, selection, selectionArgs);
            case THREE_CENT_SILVER_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoin(uri, contentValues, selection, selectionArgs);
            case THREE_CENT_NICKEL_ID:
                selection = PetContract.CoinSeriesEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateCoin(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("PET Update is not supported for " + uri);
        }
    }


    private int updatePet(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        boolean containsGender = contentValues.containsKey (PetEntry.COLUMN_PET_GENDER);
        boolean containsName = contentValues.containsKey (PetEntry.COLUMN_PET_NAME);

        if (containsName || containsGender) {
            if (containsGender) {
                // Check that the gender is one of the three valid values
                int gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
                if (!PetEntry.isValidSeries(gender)) {
                    throw new IllegalArgumentException("Pet gender must be set to Unknown, Female or Male.(update)");
                }
            }
            if (containsName) {
                String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
                if (name == null) { //TODO this does not work!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    throw new IllegalArgumentException("Pet name can not be set to null");
                }
            }
            // Get writable database
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            int rowsUpdated = database.update(PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);

            if (rowsUpdated != 0) {
                // Notify all listeners taht the data has changed for the pet content URI
                // uri: contnet://com.example.android.pets/pets
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rowsUpdated;
        }
        return 0; // no rows updated as there is no values to update.
    }
    private int updateCoin(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        boolean containsNotes = contentValues.containsKey (PetContract.CoinSeriesEntry.COLUMN_NOTES);
        boolean containsGrade = contentValues.containsKey (PetContract.CoinSeriesEntry.COLUMN_GRADE);
        boolean containsObverseGrade = contentValues.containsKey (PetContract.CoinSeriesEntry.COLUMN_GRADE_OBVERSE);
        boolean containsReverseGrade = contentValues.containsKey (PetContract.CoinSeriesEntry.COLUMN_GRADE_REVERSE);

        if (containsGrade || containsObverseGrade || containsReverseGrade || containsNotes){
            // Add a safety check to ensure something huge does not get added into the notes field of the database
            // as this field is free text
            if (containsNotes) {
                String notes = contentValues.getAsString(PetContract.CoinSeriesEntry.COLUMN_NOTES);
                if (notes != null ) {
                    if (notes.length() > 250) {
                        throw new IllegalArgumentException("Notes on a coin cannot be greater than 250 characters.");
                    }
                }
            }
            // Get writable database
            SQLiteDatabase database = mDbHelper.getWritableDatabase();

            int rowsUpdated;
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case LINCOLN_CENTS_ID:
                    rowsUpdated = database.update(PetContract.LincolnSeriesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                    break;
                case FLYING_EAGLE_CENTS_ID:
                    rowsUpdated = database.update(PetContract.FlyingEagleSeriesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                    break;
                case TWO_CENTS_ID:
                    rowsUpdated = database.update(PetContract.TwoCentSeriesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                    break;
                case THREE_CENT_SILVER_ID:
                    rowsUpdated = database.update(PetContract.ThreeCentSilverSeriesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                    break;
                case THREE_CENT_NICKEL_ID:
                    rowsUpdated = database.update(PetContract.ThreeCentNickelSeriesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                    break;
                default:
                    rowsUpdated = 0;
            }

            if (rowsUpdated != 0) {
                // Notify all listeners taht the data has changed for the pet content URI
                // uri: contnet://com.example.android.pets/pets
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
        }
        return 0; // no rows updated as there is no values to update.
    }


    public static String convertToString(Uri uri) {
        final int match = sUriMatcher.match(uri);
        String uriString;
        switch (match) {
            case LINCOLN_CENTS_ID:
                uriString = App.getContext().getString(R.string.gender_unknown);
                break;
            case FLYING_EAGLE_CENTS_ID:
                uriString = App.getContext().getString(R.string.series_flying_eagle_cent);
                break;
            case TWO_CENTS_ID:
                uriString = App.getContext().getString(R.string.series_two_cent_pieces);
                break;
            case THREE_CENT_SILVER_ID:
                uriString = App.getContext().getString(R.string.series_three_cent_silver);
                break;
            case THREE_CENT_NICKEL_ID:
                uriString = App.getContext().getString(R.string.series_three_cent_nickel);
                break;
            default:
                uriString = "";
        }
        return uriString;
    }

}