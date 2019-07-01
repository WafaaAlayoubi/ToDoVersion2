package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.database.model.Note;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String[] note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Note.COLUMN_NOTE, note[0]);
        values.put(Note.COLUMN_CATEGORY, note[1]);
        values.put(Note.COLUMN_DATE, note[4]);
        values.put(Note.COLUMN_TIMESTART, note[5]);
        values.put(Note.COLUMN_TIMEEND, note[6]);
        values.put(Note.COLUMN_DATEEND, note[7]);

        // insert row
        long id = db.insert(Note.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE,Note.COLUMN_CATEGORY,Note.COLUMN_ALERT,Note.COLUMN_FINISH,Note.COLUMN_DATE,Note.COLUMN_DATEEND, Note.COLUMN_TIMESTART,Note.COLUMN_TIMEEND, Note.COLUMN_TIMESTAMP},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_CATEGORY)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_ALERT)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_FINISH)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATEEND)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTART)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMEEND)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<Note> getAllNotes(String name,String datec) {

        List<Note> notes = new ArrayList<>();
        String selectQuery;

            // Select All Query
             selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                    Note.COLUMN_TIMESTAMP + " DESC";

         if (name == "today"){
             Date c = Calendar.getInstance().getTime();
             SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
             String formattedDate = df.format(c);
             String[] parts = formattedDate.split("-");
             int day = Integer.parseInt(parts[0]);
             int month = Integer.parseInt(parts[1]);
             int year23 = Integer.parseInt(parts[2]);
             String today = day + "-" + month + "-" + year23;

            // selectQuery = "SELECT  * FROM  '"+Note.TABLE_NAME+"' WHERE '"+Note.COLUMN_CATEGORY+"' = personal ";

             selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                     Note.COLUMN_DATE + " = " + "'"+today+"'";

        }else if (name == "calender"){

            String[] parts = datec.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year23 = Integer.parseInt(parts[2]);
            String today = day + "-" + month + "-" + year23;

            // selectQuery = "SELECT  * FROM  '"+Note.TABLE_NAME+"' WHERE '"+Note.COLUMN_CATEGORY+"' = personal ";

            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_DATE + " = " + "'"+today+"'";

        }else if (name == "tomorrow"){

             Calendar calendar = Calendar.getInstance();
             calendar.add(Calendar.DAY_OF_YEAR, 1);
             Date c = calendar.getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c);
            String[] parts = formattedDate.split("-");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year23 = Integer.parseInt(parts[2]);
            String today = day + "-" + month + "-" + year23;


            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_DATE + " = " + "'"+today+"'";

        }else if (name == "personal"){
             selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'personal'";
        }
        else if (name == "work"){
            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'work'";
        }
        else if (name == "meeting"){
            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'meeting'";
        }
        else if (name == "shopping"){
            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'shopping'";
        }
        else if (name == "party"){
            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'party'";
        }
       else if (name == "study"){
            selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " WHERE " +
                    Note.COLUMN_CATEGORY + " = " + "'study'";
        }


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                note.setCategory(cursor.getString(cursor.getColumnIndex(Note.COLUMN_CATEGORY)));
                note.setAlert(cursor.getString(cursor.getColumnIndex(Note.COLUMN_ALERT)));
                note.setFinish(cursor.getString(cursor.getColumnIndex(Note.COLUMN_FINISH)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATE)));
                note.setDateend(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATEEND)));
                note.setTimestart(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTART)));
                note.setTimeend(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMEEND)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(String[] note1,Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_NOTE, note1[0]);
        values.put(Note.COLUMN_CATEGORY, note1[1]);
        values.put(Note.COLUMN_ALERT, note1[2]);
        values.put(Note.COLUMN_FINISH, note1[3]);
        values.put(Note.COLUMN_DATE, note1[4]);
        values.put(Note.COLUMN_TIMESTART, note1[5]);
        values.put(Note.COLUMN_TIMEEND, note1[6]);
        values.put(Note.COLUMN_DATEEND, note1[7]);

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public int updateAlert(String note1,Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_ALERT, note1);

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }
    public int updateFinish(String note1,Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_FINISH, note1);

        // updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
        db.close();
    }
}