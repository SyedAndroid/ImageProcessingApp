package com.idt.syed.imageapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by syed on 2017-09-13.
 * I used contract provider to help store the images and provide a clean way to access them for other images as well if there is any need.
 * It provides a database of images that have been processed by the app
 */



public class ImageProvider extends ContentProvider {
    ImageDbProvider dbHelper;

    public static final int PATH_IMAGE= 100;

    private static final UriMatcher matcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        final String authority = ImageContract.CONTENT_AUTHORITY;

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(authority,ImageContract.PATH_IMAGE,PATH_IMAGE);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        Context context=getContext();
        dbHelper= new ImageDbProvider(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match = matcher.match(uri);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;
        switch (match) {
            case PATH_IMAGE:
                cursor = db.query(ImageContract.imageEntry.IMAGE_TABLE_NAME, null, null, null, null, null, null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = matcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        long row_inserted;
        switch (match) {
            case PATH_IMAGE:
                row_inserted = db.insert(ImageContract.imageEntry.IMAGE_TABLE_NAME, null, contentValues);
                if (row_inserted > 0) {
                    returnUri = ContentUris.withAppendedId(ImageContract.imageEntry.CONTENT_URI, row_inserted);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = matcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;
        int row_deleted;
        switch (match) {
            case PATH_IMAGE:
                row_deleted = db.delete(ImageContract.imageEntry.IMAGE_TABLE_NAME, null,strings);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(s == null || row_deleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return row_deleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
