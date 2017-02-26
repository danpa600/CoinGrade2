package com.example.djp.coingrade2;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.djp.coingrade2.data.PetContract;

public class SeriesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String mSeriesName = "";
    private int mSeries;
    private Intent mIntent;

    private static final int SERIES_LOADER = 0;
    SeriesCursorAdaptor mCursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntent = getIntent();
        setContentView(R.layout.activity_series);
        // set title of the series being displayed
        Bundle b = getIntent().getExtras();
        if (b != null) {
            //TODO make variables for these hardcoded strings
            mSeriesName = b.getString("SelectedSeries");
            mSeries = b.getInt("SeriesType");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSeriesName);
        toolbar.setSubtitle(PetContract.PetEntry.toString(mSeries));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        //mNameEditText.setOnTouchListener(mTouchListener);

        // Find the ListView which will be populated with the pet data
        ListView seriesListView = (ListView) findViewById(R.id.list_view_series);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        seriesListView.setEmptyView(emptyView);


        //Setup an Adapter to create a list item for each row of pet data in the Cursor
        // There is no pet data yet (until the loader finishes) so pass in null for the cursor.
        mCursorAdaptor = new SeriesCursorAdaptor(this, null);
        seriesListView.setAdapter(mCursorAdaptor);

        //Setup item click listener
        seriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to EditorActivity
                Intent intent = new Intent(SeriesActivity.this, SeriesEditActivity.class);
                // Form the content URI
                Uri currentSeriesUri;
                switch (mSeries){
                    case PetContract.PetEntry.GENDER_UNKNOWN:
                        currentSeriesUri = ContentUris.withAppendedId(PetContract.LincolnSeriesEntry.CONTENT_URI, id);
                        break;
                    case PetContract.PetEntry.GENDER_FEMALE:
                        currentSeriesUri = ContentUris.withAppendedId(PetContract.TwoCentSeriesEntry.CONTENT_URI, id);
                        break;
                    case PetContract.PetEntry.GENDER_MALE:
                        currentSeriesUri = ContentUris.withAppendedId(PetContract.FlyingEagleSeriesEntry.CONTENT_URI, id);
                        break;
                    default:
                        currentSeriesUri = ContentUris.withAppendedId(PetContract.LincolnSeriesEntry.CONTENT_URI, id);
                }

                // Set the URI on the data field of the intent
                intent.setData(currentSeriesUri);
                // Launch the EditorActivity to display the data for the current pet
                startActivity(intent);
            }
        });
            //Kick off the loader
        getLoaderManager().initLoader(SERIES_LOADER, null, this);

    }
    @Override
    // cause the option menu to be displayed
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_series_listing, menu);
        return true;
    }
    @Override
    // Act on menu option selections
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_details:
                // does nothing
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_edit:
                // Show edit activity
                launchCatalogItemEditActivity();
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchCatalogItemEditActivity() {
        // Create new intent to go to EditorActivity
        Intent intent = new Intent(SeriesActivity.this, EditorActivity.class);
        // Form the content URI
        Uri currentCatalogItemUri = mIntent.getData();
        // Set the URI on the data field of the intent
        intent.setData(currentCatalogItemUri);
        // Launch the EditorActivity to display the data for the current pet
        startActivity(intent);
    }

    /**
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("SelectedSeries", mSeriesName);
        savedInstanceState.putString("SeriesType", mSeries);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
     **/

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                PetContract.CoinSeriesEntry._ID,
                PetContract.CoinSeriesEntry.COLUMN_ISSUE,
                PetContract.CoinSeriesEntry.COLUMN_GRADE};
        // This loader will execute the ContentProvider's query method on a background thread
        Uri currentSeriesUri;
        switch (mSeries){
            case PetContract.PetEntry.GENDER_UNKNOWN:
                currentSeriesUri = PetContract.LincolnSeriesEntry.CONTENT_URI;
                break;
            case PetContract.PetEntry.GENDER_FEMALE:
                currentSeriesUri = PetContract.TwoCentSeriesEntry.CONTENT_URI;
                break;
            case PetContract.PetEntry.GENDER_MALE:
                currentSeriesUri = PetContract.FlyingEagleSeriesEntry.CONTENT_URI;
                break;
            default:
                currentSeriesUri = PetContract.LincolnSeriesEntry.CONTENT_URI;
        }
        return new CursorLoader(this,  //Parent activity context
                currentSeriesUri,  // Provider content URI to query
                projection,             // columns to include in the results cursor
                null,                   //No selection clause
                null,                   //No selection arguments
                null);                  //default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mCursorAdaptor.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdaptor.swapCursor(null);
    }
}
