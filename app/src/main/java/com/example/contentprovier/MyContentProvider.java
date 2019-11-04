package com.example.contentprovier;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    static final String AUTHORITY="com.example.contentprovier";
    static  final String CONTENT_PATH="DATA";
    static  final  String URL="content://"+ AUTHORITY+"/"+ CONTENT_PATH;
    static  final Uri CONTENT_URI=Uri.parse(URL);
    static final String TABLE_NAME = "Book";

    static  final String ID = "id";
    static  final String TITLE = "title";
    static  final  String AUTHOR = "author";
    private  static HashMap<String,String> BOOKG_PROJECTION_MAP;
    static final int ALLITIEMS=1;
    static  final int ONEITEM=2;
    static UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ALLITIEMS);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH+"/#", ONEITEM);
    }
    // database
    private SQLiteDatabase db;
//    static final String DATABASE_NAME = "BookDatabase";
//    static final String TABLE_NAME = "Book";
//    static final  int DATABASE_VERSION = 1;
    //static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( \"id integer primary key" + "title text", \"+\" author integer);";

    private static class SachDBHeplper extends SQLiteOpenHelper
    {
        SachDBHeplper(Context context)
        {
            super(context, "Book", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table Book(id integer primary key,title text,author text)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Book");
            onCreate(sqLiteDatabase);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        SachDBHeplper dbHeplper = new SachDBHeplper(context);
        db = dbHeplper.getWritableDatabase();
        if(db == null)
            return false;
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(TABLE_NAME);
        switch (uriMatcher.match(uri)) {
            case ALLITIEMS:
                sqLiteQueryBuilder.setProjectionMap(BOOKG_PROJECTION_MAP);
                break;
            case ONEITEM:
                sqLiteQueryBuilder.appendWhere("id" + "" + uri.getPathSegments().get(1));
                break;
        }
        if (sortOrder == null || sortOrder == "") {
            sortOrder = "title";
        }
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        String ret = getContext().getContentResolver().getType(CONTENT_URI);
        return ret;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long number_row = db.insert(TABLE_NAME, "", contentValues);
        if(number_row>0)
        {
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, number_row);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;

        }
        throw new SQLException("Failed to add a record in to" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri))
        {
            case ALLITIEMS:
                count = db.delete(TABLE_NAME,s , strings);
                break;
            case ONEITEM:
                String id =  uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME, "id" + "=" + id +
                        (!TextUtils.isEmpty(s)?"AND ("+ s+')':""),strings);
                break;
            default:
                try {
                    throw  new IllegalAccessException("Unknown URI"+ uri);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return  count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri))
        {
            case ALLITIEMS:
                count = db.update(TABLE_NAME,contentValues, s, strings);
                break;
            case ONEITEM:
                count = db.update(TABLE_NAME,contentValues, "id" + "=" + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(s)?"AND ("+ s+')':""),strings);break;
            default:
                try {
                    throw  new IllegalAccessException("Unknown URI"+ uri);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return  count;
    }
}
