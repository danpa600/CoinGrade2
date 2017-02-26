package com.example.djp.coingrade2;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.djp.coingrade2.data.PetContract;


/**
 * Created by djp on 12/5/2016.
 * Cursor adaptor used to hydrate the coin selection view for a series.
 * The coin date, mint mark, and overall grade are shown in this view.
 */

public class SeriesCursorAdaptor extends CursorAdapter {

    public SeriesCursorAdaptor(Context context, Cursor c)  {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_series, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView issueTextView = (TextView) view.findViewById(R.id.issue);
        TextView gradeTextView = (TextView) view.findViewById(R.id.grade);

        // Find the columns of pet attributes that we're interested in
        int issueColumnIndex = cursor.getColumnIndex(PetContract.LincolnSeriesEntry.COLUMN_ISSUE);
        int gradeColumnIndex = cursor.getColumnIndex(PetContract.LincolnSeriesEntry.COLUMN_GRADE);

        // Read the pet attributes from the Cursor for the current issue
        String issue = cursor.getString(issueColumnIndex);
        int grade = cursor.getInt(gradeColumnIndex);

        if (cursor.moveToFirst()) {
            String displayGrade = "";
            switch (grade) {
                case PetContract.LincolnSeriesEntry.GRADE_ABOUT_GOOD:
                    displayGrade = context.getResources().getString(R.string.grade_about_good);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_GOOD:
                    displayGrade = context.getResources().getString(R.string.grade_good);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_VERY_GOOD:
                    displayGrade = context.getResources().getString(R.string.grade_very_good);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_FINE:
                    displayGrade = context.getResources().getString(R.string.grade_fine);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_VERY_FINE:
                    displayGrade = context.getResources().getString(R.string.grade_very_fine);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_CHOICE_VERY_FINE:
                    displayGrade = context.getResources().getString(R.string.grade_choice_very_fine);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_EXTREMELY_FINE:
                    displayGrade = context.getResources().getString(R.string.grade_extremely_fine);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_CHOICE_EXTREMELY_FINE:
                    displayGrade = context.getResources().getString(R.string.grade_choice_extremely_fine);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_ABOUT_UNCIRCULATED:
                    displayGrade = context.getResources().getString(R.string.grade_about_uncirculated);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_CHOICE_ABOUT_UNCIRCULATED:
                    displayGrade = context.getResources().getString(R.string.grade_choice_about_uncirculated);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_VERY_CHOICE_ABOUT_UNCIRCULATED:
                    displayGrade = context.getResources().getString(R.string.grade_very_choice_about_uncirculated);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_60:
                    displayGrade = context.getResources().getString(R.string.grade_ms_60);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_61:
                    displayGrade = context.getResources().getString(R.string.grade_ms_61);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_62:
                    displayGrade = context.getResources().getString(R.string.grade_ms_62);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_63:
                    displayGrade = context.getResources().getString(R.string.grade_ms_63);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_64:
                    displayGrade = context.getResources().getString(R.string.grade_ms_64);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_65:
                    displayGrade = context.getResources().getString(R.string.grade_ms_65);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_66:
                    displayGrade = context.getResources().getString(R.string.grade_ms_66);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_67:
                    displayGrade = context.getResources().getString(R.string.grade_ms_67);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_68:
                    displayGrade = context.getResources().getString(R.string.grade_ms_68);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_69:
                    displayGrade = context.getResources().getString(R.string.grade_ms_69);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_MS_70:
                    displayGrade = context.getResources().getString(R.string.grade_ms_70);
                    break;
                case PetContract.LincolnSeriesEntry.GRADE_PROOF:
                    displayGrade = context.getResources().getString(R.string.grade_proof);
                    break;
                default:
                    break;
            }

            // Update the TextViews with the attributes for the current issue
            issueTextView.setText(issue);
            gradeTextView.setText(displayGrade);
        }
    }
}
