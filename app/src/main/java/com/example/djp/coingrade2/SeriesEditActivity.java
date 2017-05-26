package com.example.djp.coingrade2;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.djp.coingrade2.data.PetContract;
import com.example.djp.coingrade2.data.PetProvider;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by djp on 12/17/2016.
 */

public class SeriesEditActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Uri mCurrentPetUri;
    /** Identifier for the data loader */
    private static final int EXISTING_SERIES_LOADER = 0;
    private static final int OBVERSE = 0;
    private static final int REVERSE = 1;

    /** EditText field to enter the pet's name */
    private EditText mNotesEditText;
    private Spinner mGradeSpinner;
    private Spinner mObverseGradeSpinner;
    private Spinner mReverseGradeSpinner;
    private ImageView mObverseImage;
    private ImageView mReverseImage;
    private String mObverseImagePath;
    private String mReverseImagePath;

    // contains the type (obverse / reverse) of image being edited
    private int mImageInEdit = OBVERSE;

    private int mGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
    private int mObverseGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
    private int mReverseGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;

    /** track if the coin information has been changed **/
    private boolean mCoinHasChanged = false;

    private String mCurrentPhotoPath;

    static final int REQUEST_TAKE_OBVERSE_PHOTO = 1;
    static final int REQUEST_TAKE_REVERSE_PHOTO = 2;
    private Uri photoURI;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_editor);

        // Use getIntent() and getData() to ge the associated URI
        // Set title of EditorActivity on which situation we have
        // If opened using the ListView item, then we will have a uri
        // so change app bar to "Edit Pet"
        // Otherwise if this is a new pet, the uri is null so app bar says "Add a Pet"
        // Get the intent and determine what mode we should be in
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri == null) {
            // New pet - update title to state adding a pet
            //setTitle(getString(R.string.editor_activity_title_issue_details));
            //invalidate the options menu, so the Delete menu option can be hidden
            invalidateOptionsMenu();
        } else {
            // Edit existing Pet - update title
            //setTitle(getString(R.string.editor_activity_title_edit_details));
            // Initialize a loader to read the data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_SERIES_LOADER, null, this);
        }
        // Find all relevant views that we will need to read user input from
        mNotesEditText = (EditText) findViewById(R.id.edit_notes);
        mGradeSpinner = (Spinner) findViewById(R.id.spinner_grade);
        mObverseGradeSpinner = (Spinner) findViewById(R.id.spinner_obverse_grade);
        mReverseGradeSpinner = (Spinner) findViewById(R.id.spinner_reverse_grade);
        mObverseImage = (ImageView) findViewById(R.id.obverse_image);
        mReverseImage = (ImageView) findViewById(R.id.reverse_image);

        mNotesEditText.setOnTouchListener(mTouchListener);
        mGradeSpinner.setOnTouchListener(mTouchListener);
        mObverseGradeSpinner.setOnTouchListener(mTouchListener);
        mReverseGradeSpinner.setOnTouchListener(mTouchListener);
        mObverseImage.setOnTouchListener(mTouchListenerObverseImage);
        mReverseImage.setOnTouchListener(mTouchListenerReverseImage);

        setupGradeSpinner();
        setupObverseGradeSpinner();
        setupReverseGradeSpinner();
    }
        @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

            // Define a projection that specifies the columns from the table we care about.
            String[] projection = {
                    PetContract.CoinSeriesEntry._ID,
                    PetContract.CoinSeriesEntry.COLUMN_ISSUE,
                    PetContract.CoinSeriesEntry.COLUMN_GRADE,
                    PetContract.CoinSeriesEntry.COLUMN_NOTES,
                    PetContract.CoinSeriesEntry.COLUMN_GRADE_OBVERSE,
                    PetContract.CoinSeriesEntry.COLUMN_GRADE_REVERSE,
                    PetContract.CoinSeriesEntry.COLUMN_IMAGE_OBVERSE,
                    PetContract.CoinSeriesEntry.COLUMN_IMAGE_REVERSE
                    };
            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,  //Parent activity context
                    mCurrentPetUri,  // Provider content URI to query
                    projection,             // columns to include in the results cursor
                    null,                   //No selection clause
                    null,                   //No selection arguments
                    null);                  //default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int issueColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_ISSUE);
            int notesColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_NOTES);
            int gradeColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_GRADE);
            int gradeObverseColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_GRADE_OBVERSE);
            int gradeReverseColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_GRADE_REVERSE);
            int obverseImageColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_IMAGE_OBVERSE);
            int reverseImageColumnIndex = cursor.getColumnIndex(PetContract.CoinSeriesEntry.COLUMN_IMAGE_REVERSE);

            // Extract out the value from the Cursor for the given column index
            String issue = cursor.getString(issueColumnIndex);
            String notes = cursor.getString(notesColumnIndex);
            int grade = cursor.getInt(gradeColumnIndex);
            int gradeObverse = cursor.getInt(gradeObverseColumnIndex);
            int gradeReverse = cursor.getInt(gradeReverseColumnIndex);
            String obverseImagePath = cursor.getString(obverseImageColumnIndex);
            if (obverseImagePath != null) {
                mObverseImage.setImageURI(Uri.fromFile(new File(obverseImagePath)));
                mObverseImagePath = obverseImagePath;
            }
            String reverseImagePath = cursor.getString(reverseImageColumnIndex);
            if (reverseImagePath != null) {
                mReverseImage.setImageURI(Uri.fromFile(new File(reverseImagePath)));
                mReverseImagePath = reverseImagePath;
            }

            // Update the views on the screen with the values from the database
            //setTitle(issue.toString());
            Toolbar toolbar = (Toolbar) findViewById(R.id.edit_entry_toolbar);
            toolbar.setTitle(issue.toString());
            toolbar.setSubtitle(PetProvider.convertToString(mCurrentPetUri));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            mNotesEditText.setText(notes);

            setGradeSpinnerSelection(grade, mGradeSpinner);
            setGradeSpinnerSelection(gradeObverse, mObverseGradeSpinner);
            setGradeSpinnerSelection(gradeReverse, mReverseGradeSpinner);

        }
    }

    private void setGradeSpinnerSelection(int grade, Spinner spinner) {
        switch (grade) {
            case PetContract.CoinSeriesEntry.GRADE_ABOUT_GOOD:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_ABOUT_GOOD);
                break;
            case PetContract.CoinSeriesEntry.GRADE_GOOD:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_GOOD);
                break;
            case PetContract.CoinSeriesEntry.GRADE_VERY_GOOD:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_VERY_GOOD);
                break;
            case PetContract.CoinSeriesEntry.GRADE_FINE:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_FINE);
                break;
            case PetContract.CoinSeriesEntry.GRADE_VERY_FINE:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_VERY_FINE);
                break;
            case PetContract.CoinSeriesEntry.GRADE_CHOICE_VERY_FINE:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_CHOICE_VERY_FINE);
                break;
            case PetContract.CoinSeriesEntry.GRADE_EXTREMELY_FINE:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_EXTREMELY_FINE);
                break;
            case PetContract.CoinSeriesEntry.GRADE_CHOICE_EXTREMELY_FINE:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_CHOICE_EXTREMELY_FINE);
                break;
            case PetContract.CoinSeriesEntry.GRADE_ABOUT_UNCIRCULATED:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_ABOUT_UNCIRCULATED);
                break;
            case PetContract.CoinSeriesEntry.GRADE_CHOICE_ABOUT_UNCIRCULATED:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_CHOICE_ABOUT_UNCIRCULATED);
                break;
            case PetContract.CoinSeriesEntry.GRADE_VERY_CHOICE_ABOUT_UNCIRCULATED:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_VERY_CHOICE_ABOUT_UNCIRCULATED);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_60:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_60);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_61:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_61);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_62:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_62);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_63:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_63);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_64:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_64);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_65:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_65);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_66:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_66);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_67:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_67);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_68:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_68);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_69:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_69);
                break;
            case PetContract.CoinSeriesEntry.GRADE_MS_70:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_MS_70);
                break;
            case PetContract.CoinSeriesEntry.GRADE_PROOF:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_PROOF);
                break;
            default:
                spinner.setSelection(PetContract.CoinSeriesEntry.GRADE_UNKNOWN);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /** set up the OnTouchListener to track if edits have been made. **/
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mCoinHasChanged = true;
            return false;
        }
    };

    /** set up the OnTouchListener to track if edits have been made. **/
    private View.OnTouchListener mTouchListenerObverseImage = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (mObverseImagePath == null) {
                mCoinHasChanged = true;
                dispatchTakePictureIntent(REQUEST_TAKE_OBVERSE_PHOTO);
            }
            else { /*XXXXXXXXXXXXXXADDING CROP EDITINGXXXXXXXXXXXXXXXXXXX*/
                if (mObverseImagePath != null) {
                     photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "com.example.android.fileprovider",
                            new File(mObverseImagePath));
                    mImageInEdit = OBVERSE;
                    CropImage.activity(photoURI).start(SeriesEditActivity.this);
                }
            }
            return false;
        }
    };
    /** set up the OnTouchListener to track if edits have been made. **/
    private View.OnTouchListener mTouchListenerReverseImage = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (mReverseImagePath == null) {
                mCoinHasChanged = true;
                dispatchTakePictureIntent(REQUEST_TAKE_REVERSE_PHOTO);
            }
            else { /*XXXXXXXXXXXXXXADDING CROP EDITINGXXXXXXXXXXXXXXXXXXX*/
                if (mReverseImagePath != null) {
                    photoURI = FileProvider.getUriForFile(getApplicationContext(),
                            "com.example.android.fileprovider",
                            new File(mReverseImagePath));
                    mImageInEdit = REVERSE;
                    CropImage.activity(photoURI).start(SeriesEditActivity.this);
                }
            }
            return false;
        }
    };


    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //TODO add some error handling logic
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_OBVERSE_PHOTO && resultCode == RESULT_OK) {
            //try {
                mObverseImage.setImageURI(Uri.fromFile(new File(mCurrentPhotoPath)));
                mObverseImagePath = mCurrentPhotoPath;
        }
        if (requestCode == REQUEST_TAKE_REVERSE_PHOTO && resultCode == RESULT_OK) {
            //try {
            mReverseImage.setImageURI(Uri.fromFile(new File(mCurrentPhotoPath)));
            mReverseImagePath = mCurrentPhotoPath;
        }
        /*XXXXXXXXXXXXXXADDING CROP EDITINGXXXXXXXXXXXXXXXXXXX*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (mImageInEdit == OBVERSE) {
                    savefile(resultUri, mObverseImagePath);
                    mObverseImage.setImageURI(resultUri);
                }
                else if (mImageInEdit == REVERSE) {
                    savefile(resultUri, mReverseImagePath);
                    mReverseImage.setImageURI(resultUri);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /* used to save the cropped image */
    void savefile(Uri sourceuri, String file)
    {
        String sourceFilename= sourceuri.getPath();
        String destinationFilename = file;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Setup the dropdown spinner that allows the user to select a grade.
     */
    private void setupGradeSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_grade_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGradeSpinner.setAdapter(gradeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                mGrade = convertGradeSelectionToInt(selection);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
            }
        });
    }

    private int convertGradeSelectionToInt(String selection) {
        int grade = 0;
        if (!TextUtils.isEmpty(selection)) {
            if (selection.equals(getString(R.string.grade_about_good))) {
                grade = PetContract.CoinSeriesEntry.GRADE_ABOUT_GOOD;
            } else if (selection.equals(getString(R.string.grade_good))) {
                grade = PetContract.CoinSeriesEntry.GRADE_GOOD;
            } else if (selection.equals(getString(R.string.grade_very_good))) {
                grade = PetContract.CoinSeriesEntry.GRADE_VERY_GOOD;
            } else if (selection.equals(getString(R.string.grade_fine))) {
                grade = PetContract.CoinSeriesEntry.GRADE_FINE;
            } else if (selection.equals(getString(R.string.grade_very_fine))) {
                grade = PetContract.CoinSeriesEntry.GRADE_VERY_FINE;
            } else if (selection.equals(getString(R.string.grade_choice_very_fine))) {
                grade = PetContract.CoinSeriesEntry.GRADE_CHOICE_VERY_FINE;
            } else if (selection.equals(getString(R.string.grade_extremely_fine))) {
                grade = PetContract.CoinSeriesEntry.GRADE_EXTREMELY_FINE;
            } else if (selection.equals(getString(R.string.grade_choice_extremely_fine))) {
                grade = PetContract.CoinSeriesEntry.GRADE_CHOICE_EXTREMELY_FINE;
            } else if (selection.equals(getString(R.string.grade_about_uncirculated))) {
                grade = PetContract.CoinSeriesEntry.GRADE_ABOUT_UNCIRCULATED;
            } else if (selection.equals(getString(R.string.grade_choice_about_uncirculated))) {
                grade = PetContract.CoinSeriesEntry.GRADE_CHOICE_ABOUT_UNCIRCULATED;
            } else if (selection.equals(getString(R.string.grade_very_choice_about_uncirculated))) {
                grade = PetContract.CoinSeriesEntry.GRADE_VERY_CHOICE_ABOUT_UNCIRCULATED;
            } else if (selection.equals(getString(R.string.grade_ms_60))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_60;
            } else if (selection.equals(getString(R.string.grade_ms_61))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_61;
            } else if (selection.equals(getString(R.string.grade_ms_62))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_62;
            } else if (selection.equals(getString(R.string.grade_ms_63))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_63;
            } else if (selection.equals(getString(R.string.grade_ms_64))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_64;
            } else if (selection.equals(getString(R.string.grade_ms_65))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_65;
            } else if (selection.equals(getString(R.string.grade_ms_66))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_66;
            } else if (selection.equals(getString(R.string.grade_ms_67))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_67;
            } else if (selection.equals(getString(R.string.grade_ms_68))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_68;
            } else if (selection.equals(getString(R.string.grade_ms_69))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_69;
            } else if (selection.equals(getString(R.string.grade_ms_70))) {
                grade = PetContract.CoinSeriesEntry.GRADE_MS_70;
            } else if (selection.equals(getString(R.string.grade_proof))) {
                grade = PetContract.CoinSeriesEntry.GRADE_PROOF;
            } else {
                grade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
            }
        }
        return grade;
    }

    /**
     * Setup the dropdown spinner that allows the user to select a grade.
     */
    private void setupObverseGradeSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_grade_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mObverseGradeSpinner.setAdapter(gradeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mObverseGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                mObverseGrade = convertGradeSelectionToInt(selection);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mObverseGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select a grade.
     */
    private void setupReverseGradeSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_grade_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mReverseGradeSpinner.setAdapter(gradeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mReverseGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                mReverseGrade = convertGradeSelectionToInt(selection);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mReverseGrade = PetContract.CoinSeriesEntry.GRADE_UNKNOWN;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save coin to database
                saveCoin();
                // Exit activity
                finish();
                return true;
            case R.id.action_clear:
                // Remove coin's information
                clearCoinInformation();
                // Exit activity
                finish();
                return true;
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mCoinHasChanged) {
                    finish();
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(SeriesEditActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** set up dialog for confirming unsaved change exits **/
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        //builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveCoin() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String notesString = mNotesEditText.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(PetContract.CoinSeriesEntry.COLUMN_NOTES, notesString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE, mGrade);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE_OBVERSE, mObverseGrade);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE_REVERSE, mReverseGrade);
        values.put(PetContract.CoinSeriesEntry.COLUMN_IMAGE_OBVERSE, mObverseImagePath);
        values.put(PetContract.CoinSeriesEntry.COLUMN_IMAGE_REVERSE, mReverseImagePath);

        // Use getIntent() and getData() to get the associated URI
        // Get the intent and determine what mode we should be in
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri == null) {
            // Insert a new row for pet in the database, returning the ID of that new row.
            //Uri uri = getContentResolver().insert(PetContract.LincolnSeriesEntry.CONTENT_URI, values);
            Uri uri = getContentResolver().insert(mCurrentPetUri, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (uri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_coin_update_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.editor_coin_update_successful), Toast.LENGTH_SHORT).show();
            }
        } else {
            int row = getContentResolver().update(mCurrentPetUri, values, null, null);
            if (row == 0) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_coin_update_failed), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.editor_coin_update_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void clearCoinInformation() {
        // Null the fields
        String nullString = null;

        ContentValues values = new ContentValues();
        values.put(PetContract.CoinSeriesEntry.COLUMN_NOTES, nullString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE, nullString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE_OBVERSE, nullString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_GRADE_REVERSE, nullString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_IMAGE_OBVERSE, nullString);
        values.put(PetContract.CoinSeriesEntry.COLUMN_IMAGE_REVERSE, nullString);

        // Use getIntent() and getData() to get the associated URI
        // Get the intent and determine what mode we should be in
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();

        if (mCurrentPetUri == null) {
            // Insert a new row for pet in the database, returning the ID of that new row.
            //Uri uri = getContentResolver().insert(PetContract.LincolnSeriesEntry.CONTENT_URI, values);
            Uri uri = getContentResolver().insert(mCurrentPetUri, values);
            // Show a toast message depending on whether or not the insertion was successful
            if (uri == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_coin_update_failed), Toast.LENGTH_SHORT).show();
            } else {
                // clear was successful, now remove the images
                if (mObverseImagePath != null) {
                    File dfile = (new File(mObverseImagePath));
                    if (dfile.exists()) {
                        dfile.delete();
                    }
                }
                if (mReverseImagePath != null) {
                    File dfile = (new File(mReverseImagePath));
                    if (dfile.exists()) {
                        dfile.delete();
                    }
                }
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.editor_coin_update_successful), Toast.LENGTH_SHORT).show();
            }
        } else {
            int row = getContentResolver().update(mCurrentPetUri, values, null, null);
            if (row == 0) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_coin_update_failed), Toast.LENGTH_SHORT).show();
            } else {
                // clear was successful, now remove the images
                if (mObverseImagePath != null) {
                    File dfile = (new File(mObverseImagePath));
                    if (dfile.exists()) {
                        dfile.delete();
                    }
                }
                if (mReverseImagePath != null) {
                    File dfile = (new File(mReverseImagePath));
                    if (dfile.exists()) {
                        dfile.delete();
                    }
                }
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getString(R.string.editor_coin_update_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
