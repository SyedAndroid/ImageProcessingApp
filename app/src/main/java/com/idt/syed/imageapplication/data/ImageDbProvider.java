package com.idt.syed.imageapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by syed on 2017-09-13.
 */

public class ImageDbProvider extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "images.db";
    public static final int version = 1;

    public ImageDbProvider(Context context) {
        super(context, DATABASE_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_DBTABLE_MOVIE="CREATE TABLE "+ ImageContract.imageEntry.IMAGE_TABLE_NAME+"("
                +ImageContract.imageEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +ImageContract.imageEntry.url_picture+" BLOB NOT NULL); ";
        sqLiteDatabase.execSQL(CREATE_DBTABLE_MOVIE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImageContract.imageEntry.IMAGE_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}


