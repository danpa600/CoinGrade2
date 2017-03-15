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

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.djp.coingrade2.App;
import com.example.djp.coingrade2.R;

/**
 * API Contract for the Pets app.
 */
public final class PetContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private PetContract() {}

    /**
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website.  A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * device.
     */
    public static final String CONTENT_AUTHORITY = "com.example.djp.coingrade2";
    /**
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    /**
     * Possible path (appended to base content URI for possible URI's)
     * For instance, content://com.example.android.pets/pets/ is a valid path for
     * looking at pet data. content://com.example.android.pets/staff/ will fail,
     * as the ContentProvider hasn't been given any information on what to do with "staff".
     */
    public static final String PATH_PETS = "pets";
    public static final String PATH_LINCOLN_SERIES = "lincoln_series";
    public static final String PATH_FLYING_EAGLE_SERIES = "flying_eagle_series";
    public static final String PATH_TWO_CENT_SERIES = "two_cent_series";
    public static final String PATH_THREE_CENT_SILVER_SERIES = "three_cent_silver_series";
    public static final String PATH_THREE_CENT_NICKEL_SERIES = "three_cent_nickel_series";

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    public static final class PetEntry implements BaseColumns {

        /** The content URI to access the pet data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a single pet.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /** Name of database table for pets */
        public final static String TABLE_NAME = "pets";

        /**
         * Unique ID number for the pet (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_NAME ="name";

        /**
         * Breed of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PET_BREED = "breed";

        /**
         * Gender of the pet.
         *
         * The only possible values are {@link #GENDER_UNKNOWN}, {@link #SERIES_FLYING_EAGLE_CENT},
         * or {@link #SERIES_TWO_CENT_PIECES}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_GENDER = "gender";

        /**
         * Weight of the pet.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the gender of the pet.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int SERIES_FLYING_EAGLE_CENT = 1;
        public static final int SERIES_TWO_CENT_PIECES = 2;
        public static final int SERIES_THREE_CENT_SILVER = 3;
        public static final int SERIES_THREE_CENT_NICKEL = 4;

        // Convert db int value to string for series
        public static String toString(int series) {
            String seriesString = "";
            switch (series) {
                case (SERIES_TWO_CENT_PIECES):
                    seriesString = App.getContext().getResources().getString(R.string.series_two_cent_pieces);
                    break;
                case (SERIES_FLYING_EAGLE_CENT):
                    seriesString = App.getContext().getResources().getString(R.string.series_flying_eagle_cent);
                    break;
                case (SERIES_THREE_CENT_SILVER):
                    seriesString = App.getContext().getResources().getString(R.string.series_three_cent_silver);
                    break;
                case (SERIES_THREE_CENT_NICKEL):
                    seriesString = App.getContext().getResources().getString(R.string.series_three_cent_nickel);
                    break;
                default:
                    seriesString = App.getContext().getResources().getString(R.string.gender_unknown);
                    break;
            }
            return seriesString;
        }

        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #SERIES_FLYING_EAGLE_CENT},
         * or {@link #SERIES_TWO_CENT_PIECES}.
         */
        public static boolean isValidSeries(int gender) {
            if (gender == GENDER_UNKNOWN || gender == SERIES_FLYING_EAGLE_CENT ||
                    gender == SERIES_TWO_CENT_PIECES || gender == SERIES_THREE_CENT_SILVER ||
                    gender == SERIES_THREE_CENT_NICKEL) {
                return true;
            }
            return false;
        }
    }
    public static final class LincolnSeriesEntry extends CoinSeriesEntry {

        /** The content URI to access the series data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LINCOLN_SERIES);
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of issues in a series.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINCOLN_SERIES;
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a coin series.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LINCOLN_SERIES;
        /** Name of database table for Lincoln Cents */
        public final static String TABLE_NAME = "lincolnSeries";
     }

    // Class for the flying eagle cent database entries
    public static final class FlyingEagleSeriesEntry extends CoinSeriesEntry {

        /** The content URI to access the series data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FLYING_EAGLE_SERIES);
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of issues in a series.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLYING_EAGLE_SERIES;
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a coin series.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FLYING_EAGLE_SERIES;

        /** Name of database table for Flying Eagle Cents */
        public final static String TABLE_NAME = "flyingEagleSeries";
    }
    // Class for the Two cent database entries
    public static final class TwoCentSeriesEntry extends CoinSeriesEntry {

        /** The content URI to access the series data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TWO_CENT_SERIES);
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of issues in a series.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TWO_CENT_SERIES;
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a coin series.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TWO_CENT_SERIES;

        /** Name of database table for Flying Eagle Cents */
        public final static String TABLE_NAME = "twoCentSeries";
    }

    public static final class ThreeCentSilverSeriesEntry extends CoinSeriesEntry {

        /** The content URI to access the series data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_THREE_CENT_SILVER_SERIES);
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of issues in a series.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THREE_CENT_SILVER_SERIES;
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a coin series.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THREE_CENT_SILVER_SERIES;

        /** Name of database table for Flying Eagle Cents */
        public final static String TABLE_NAME = "threeCentSilverSeries";
    }

    public static final class ThreeCentNickelSeriesEntry extends CoinSeriesEntry {

        /** The content URI to access the series data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_THREE_CENT_NICKEL_SERIES);
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a list of issues in a series.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THREE_CENT_NICKEL_SERIES;
        /**
         * The MIME type of the {@link #BASE_CONTENT_URI} for a coin series.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_THREE_CENT_NICKEL_SERIES;

        /** Name of database table for Flying Eagle Cents */
        public final static String TABLE_NAME = "threeCentNickelSeries";
    }

    // Base Class for the coin series
    public static class CoinSeriesEntry implements BaseColumns {

        public static final int GRADE_UNKNOWN = 0;
        public static final int GRADE_ABOUT_GOOD = 1;
        public static final int GRADE_GOOD = 2;
        public static final int GRADE_VERY_GOOD = 3;
        public static final int GRADE_FINE = 4;
        public static final int GRADE_VERY_FINE = 5;
        public static final int GRADE_CHOICE_VERY_FINE = 6;
        public static final int GRADE_EXTREMELY_FINE = 7;
        public static final int GRADE_CHOICE_EXTREMELY_FINE = 8;
        public static final int GRADE_ABOUT_UNCIRCULATED = 9;
        public static final int GRADE_CHOICE_ABOUT_UNCIRCULATED = 10;
        public static final int GRADE_VERY_CHOICE_ABOUT_UNCIRCULATED = 11;
        public static final int GRADE_MS_60 = 12;
        public static final int GRADE_MS_61 = 13;
        public static final int GRADE_MS_62 = 14;
        public static final int GRADE_MS_63 = 15;
        public static final int GRADE_MS_64 = 16;
        public static final int GRADE_MS_65 = 17;
        public static final int GRADE_MS_66 = 18;
        public static final int GRADE_MS_67 = 19;
        public static final int GRADE_MS_68 = 20;
        public static final int GRADE_MS_69 = 21;
        public static final int GRADE_MS_70 = 22;
        public static final int GRADE_PROOF = 23;

        /**
         * Unique ID number for the coin entry (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Date, mint mark and special notes of coin entry.
         *
         * Type: TEXT
         */
        public final static String COLUMN_ISSUE ="issue";
        /**
         * Grade of the coin.
         *
         * Type: TEXT
         */
        public final static String COLUMN_GRADE = "grade";
        /**
         * Notes about the coin.
         *
         * Type: TEXT
         */
        public final static String COLUMN_NOTES = "notes";
        /**
         * Obverse grade of coin
         */
        public final static String COLUMN_GRADE_OBVERSE = "obverseGrade";
        /**
         * Reverse grade of coin
         */
        public final static String COLUMN_GRADE_REVERSE = "reverseGrade";
        /**
         * image location of Obverse
         */
        public final static String COLUMN_IMAGE_OBVERSE = "obverseImage";
        /**
         * image location of Reverse
         */
        public final static String COLUMN_IMAGE_REVERSE = "reverseImage";
    }

}

