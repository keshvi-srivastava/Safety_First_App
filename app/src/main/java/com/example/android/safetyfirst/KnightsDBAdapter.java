package com.example.android.safetyfirst;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.safetyfirst.Finding;

import java.util.ArrayList;

public class KnightsDBAdapter extends ContentProvider {

    private static final String DB_NAME= "KnightsDB.sqlite";
    private static final int DB_VERSION=1;
    private static final String TABLE_FINDINGS="findings";
    private static final String COL_ID="id";
    public static final String COL_HEIGHT="height";
    public static final String COL_AGE="age";
    public static final String COL_ADDRESS="address";
    public static final String COL_COMPLEXION="complexion";
    private static final String CREATE_TABLE_FINDINGS=String.format("create table %s(%s integer not null primary key autoincrement,"+
            "%s integer not null,%s integer null,%s text not null,%s text null)",TABLE_FINDINGS,COL_ID,COL_HEIGHT,COL_AGE,COL_ADDRESS,COL_COMPLEXION);
    private  static final String ALTER_TABLE_FINDINGS_COMPLEXION=String.format("alter table %s add column %s text null",TABLE_FINDINGS,COL_COMPLEXION);
    private SQLiteDatabase db;
    private static UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    static{
        matcher.addURI("com.android.yo.yo.KnightsContentProvider","/Knightfindings",1);
    }
    public KnightsDBAdapter(Context context){
        KnightsDbConnetionHelper helper=new KnightsDbConnetionHelper(context);
        db=helper.getWritableDatabase();

    }
    public KnightsDBAdapter() //compulsary for a content provider
    {
    }
    @Override
    public boolean onCreate() {
        KnightsDbConnetionHelper helper=new KnightsDbConnetionHelper(getContext());
        db=helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code=matcher.match(uri);
        switch(code)
        {
            case 1:return db.query(TABLE_FINDINGS,projection,selection,selectionArgs,null,null,sortOrder);
            default:return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int code =matcher.match(uri);
        switch(code)
        {
            case 1://he is right and wants to insert data into findings table
                long id=db.insert(TABLE_FINDINGS,null,values);
                Uri.withAppendedPath(uri,id+"");
                break;
            default://returns wrong values
                return null;
        }
        return uri;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int code =matcher.match(uri);
        switch(code)
        {
            case 1://he is right and wants to insert data into findings table
                long id=db.delete(TABLE_FINDINGS,null,null);
                Uri.withAppendedPath(uri,id+"");
                break;
            default://returns wrong values
                return 0;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    class KnightsDbConnetionHelper extends SQLiteOpenHelper {

        public KnightsDbConnetionHelper(Context context) {
            super(context,DB_NAME,null,DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_FINDINGS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(ALTER_TABLE_FINDINGS_COMPLEXION);
        }
    }
    public void updateDate(String address,String date)
    {
        db.execSQL("UPDATE findings SET COMPLEXION = '"+date+"' WHERE ADDRESS = '"+address+"';");

    }
    public void updateFindings(int height,String address)
    {
        db.execSQL("UPDATE findings SET HEIGHT = '"+height+"' WHERE ADDRESS = '"+address+"';");
    }
    public void insertfindingsmanually()
    {
        ContentValues values=new ContentValues();
        values.put(COL_HEIGHT,1);
        values.put(COL_AGE,0);
        values.put(COL_ADDRESS,"Go To The Washroom And Read A Magazine");
        values.put(COL_COMPLEXION,"1-01-1996");
        long id=db.insert(TABLE_FINDINGS,null,values);
        values.put(COL_HEIGHT,1);
        values.put(COL_AGE,0);
        values.put(COL_ADDRESS,"10 minutes at work and i start using the \"F-Word\"Like A Comma");
        values.put(COL_COMPLEXION,"1-01-1996");
        db.insert(TABLE_FINDINGS,null,values);
        values.put(COL_HEIGHT,1);
        values.put(COL_AGE,0);
        values.put(COL_ADDRESS,"Work Tip:Stand,Stretch,Go to the airport,Get On The Plane And Never Return");
        values.put(COL_COMPLEXION,"1-01-1996");
        db.insert(TABLE_FINDINGS,null,values);
        values.put(COL_HEIGHT,1);
        values.put(COL_AGE,0);
        values.put(COL_ADDRESS,"Dont Mistake This Fake Smile and the Professional Body Language, I'd Punch You Right In The Throat If I Knew I wouldn't Lose My Job");
        values.put(COL_COMPLEXION,"1-01-1996");
        db.insert(TABLE_FINDINGS,null,values);
        values.put(COL_HEIGHT,1);
        values.put(COL_AGE,0);
        values.put(COL_ADDRESS,"Sometimes I Cant Figure Out If I'm At School Or Pre-School,Oh Wait: I'm At Work");
        values.put(COL_COMPLEXION,"1-01-1996");
        db.insert(TABLE_FINDINGS,null,values);

    }
    public boolean insertFindings(float height,int age,String address,String complexion)
    {
        ContentValues values=new ContentValues();
        values.put(COL_HEIGHT,height);
        values.put(COL_AGE,age);
        values.put(COL_ADDRESS,address);
        values.put(COL_COMPLEXION,complexion);
        long id=db.insert(TABLE_FINDINGS,null,values);
        if(id==-1)
            return false;
        return true;
    }
    public void deleteUser(String userName)
    {
        db.execSQL("DELETE FROM findings WHERE ADDRESS = '"+userName+"'");
    }
    public ArrayList<Finding> getFindings(String date){
        ArrayList<Finding> findings=new ArrayList<Finding>();
        Cursor cursor=db.rawQuery("Select * FROM findings WHERE HEIGHT = '"+0+"'",null);;
        while(cursor.moveToNext()){
            findings.add(new Finding(cursor.getLong(cursor.getColumnIndex(COL_ID)),
                    cursor.getFloat(cursor.getColumnIndex(COL_HEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(COL_AGE)),
                    cursor.getString((cursor.getColumnIndex(COL_ADDRESS))),
                    cursor.getString((cursor.getColumnIndex(COL_COMPLEXION)))));
        }
        cursor.close();
        return findings;
    }
    public ArrayList<Finding> getFindings(){
        ArrayList<Finding> findings=new ArrayList<Finding>();
        Cursor cursor=db.query(TABLE_FINDINGS,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            findings.add(new Finding(cursor.getLong(cursor.getColumnIndex(COL_ID)),
                    cursor.getFloat(cursor.getColumnIndex(COL_HEIGHT)),
                    cursor.getInt(cursor.getColumnIndex(COL_AGE)),
                    cursor.getString((cursor.getColumnIndex(COL_ADDRESS))),
                    cursor.getString((cursor.getColumnIndex(COL_COMPLEXION)))));
        }
        cursor.close();
        return findings;
    }
}
