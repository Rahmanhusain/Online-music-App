package com.example.musicapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.musicapp.Models.Recmodel;
import java.util.ArrayList;

public class dbhandler extends SQLiteOpenHelper {
    public  static  final String DB_NAME="musicdb";
    public  static  final String SONG_TABLE="Song_table";
    public  static  final String SONG_URL="Song_url";
    public  static  final String SONG_IMG="Song_img";
    public  static  final String SONG_NAME="Song_name";
    public static final String SONG_ARTIST="Song_artist";
    public dbhandler(Context context) {
        super(context,DB_NAME,null,5);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + SONG_TABLE+ " ("
                + SONG_IMG + " TEXT,"
                + SONG_NAME+ " TEXT,"
                + SONG_URL + " TEXT,"
                +SONG_ARTIST+" TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1>i) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SONG_TABLE);
        }
onCreate(sqLiteDatabase);
    }
    public void Adddata(String pic,String name,String url,String artist){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        ContentValues  v=new ContentValues();
        v.put(SONG_IMG,pic);
        v.put(SONG_NAME,name);
        v.put(SONG_URL,url);
        v.put(SONG_ARTIST,artist);
        sqLiteDatabase.insert(SONG_TABLE,null,v);
        sqLiteDatabase.close();
    }
    public ArrayList<Recmodel> readdata() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + SONG_TABLE, null);

        // on below line we are creating a new array list.
        ArrayList<Recmodel> datalist = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                datalist.add(new Recmodel(cursorCourses.getString(0),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3)
                        ));
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorCourses.close();
        return datalist;
    }
    public boolean itemcheck(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + SONG_TABLE, null);
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                if (name.equals(cursorCourses.getString(1))){
                    return true;
                }
            } while (cursorCourses.moveToNext());
            // moving our cursor to next.
        }

        return false;
    }
    public  void delete(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(SONG_TABLE,SONG_NAME +"=?",new String[]{name});
        db.close();
    }
}
