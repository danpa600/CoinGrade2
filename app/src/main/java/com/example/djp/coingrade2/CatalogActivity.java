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
package com.example.djp.coingrade2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.LoaderManager;
import android.content.CursorLoader;
//import android.support.v4.content.CursorLoader;
import android.content.Loader;
//import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.djp.coingrade2.data.PetContract;
import com.example.djp.coingrade2.data.PetContract.PetEntry;
//import com.example.djp.coingrade2.data.PetDbHelper;
import com.example.djp.coingrade2.data.PetProvider;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdaptor;

    /** Database helper that will provide us access to the database */
    //private PetDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //mDbHelper = new PetDbHelper(this);

        // Find the ListView which will be populated with the pet data
        final ListView petListView = (ListView) findViewById(R.id.list_view_pet);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
        //displayDatabaseInfo();

        //Setup an Adapter to create a list item for each row of pet data in the Cursor
        // There is no pet data yet (until the loader finishes) so pass in null for the cursor.
        mCursorAdaptor = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdaptor);

        //Setup item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to EditorActivity
                Intent intent = new Intent(CatalogActivity.this, SeriesActivity.class);
                // Form the content URI
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentPetUri);

                String[] projection = {
                        PetEntry._ID,
                        PetEntry.COLUMN_PET_NAME,
                        PetEntry.COLUMN_PET_GENDER};
                // This loader will execute the ContentProvider's query method on a background thread
                Cursor cursor = getContentResolver().query(
                        PetEntry.CONTENT_URI,  // Provider content URI to query
                        projection,             // columns to include in the results cursor
                        null,                   //No selection clause
                        null,                   //No selection arguments
                        null);                  //default sort order
                // send the series name based on the selected item
                // send the series type based on the selected item
                if (cursor.moveToPosition(position)) {
                    int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
                    int seriesColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
                    String selectedSeries = cursor.getString(nameColumnIndex);
                    int selectedSeriesType = cursor.getInt(seriesColumnIndex);
                    Bundle b = new Bundle();
                    //TODO make variables for these hardcoded strings
                    b.putString("SelectedSeries", selectedSeries);
                    b.putInt("SeriesType", selectedSeriesType);
                    intent.putExtras(b);
                }
                // Launch the EditorActivity to display the data for the current pet
                startActivity(intent);
            }
        });
        //Kick off the loader
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    // TODO remove the menu options not used
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // does nothing
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_GENDER};
        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  //Parent activity context
                PetEntry.CONTENT_URI,  // Provider content URI to query
                projection,             // columns to include in the results cursor
                null,                   //No selection clause
                null,                   //No selection arguments
                null);                  //default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update PetCursorAdapter with this new cursor containing updated pet data
        mCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callback called when the data needs to be deleted.
        mCursorAdaptor.swapCursor(null);
    }
}
