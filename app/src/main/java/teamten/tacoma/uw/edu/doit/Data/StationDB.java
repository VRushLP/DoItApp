package teamten.tacoma.uw.edu.doit.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import teamten.tacoma.uw.edu.doit.model.DoItList;

/**
 * Created by heath_000 on 5/2/2016.
 */
public class StationDB {
    public static final int DB_VERSION = 5;
    public static final String DB_NAME = "_450atm10.db";
    private static final String TABLE_NAME = "lists";

    private StationDBHelper mStationDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    public StationDB(Context context) {
        mStationDBHelper = new StationDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mStationDBHelper.getWritableDatabase();
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }



    /**
     * Returns the list of courses from the local Course table.
     * @return list
     */
    public List<DoItList> getDoItLists() {

        String[] columns = {
                //"listID",
                "title",
                "isDeleted"
        };

        Cursor c = mSQLiteDatabase.query(
                TABLE_NAME,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<DoItList> list = new ArrayList<DoItList>();
        for (int i=0; i<c.getCount(); i++) {
            //int id = c.getInt(0);
            String title = c.getString(0);
            int isDeleted = c.getInt(1);
            DoItList theList = new DoItList(title, isDeleted);
            list.add(theList);
            c.moveToNext();
        }

        return list;
    }



    /**
     * Inserts the course into the local sqlite table. Returns true if successful, false otherwise.
     * @param
     * @param title
     * @return true or false
     */
    public boolean insertStation(String title, int isDeleted) {
        ContentValues contentValues = new ContentValues();
        //contentValues.put("listID", id);
        contentValues.put("title", title);
        contentValues.put("isDeleted", isDeleted);
        long rowId = mSQLiteDatabase.insert("lists", null, contentValues);
        return rowId != -1;
    }

    /**
     * Delete all the data from the TABLE
     */
    public void deleteStation() {
        mSQLiteDatabase.delete(TABLE_NAME, null, null);
    }


    class StationDBHelper extends SQLiteOpenHelper {

        private static final String CREATE_LIST_SQL =
                "CREATE TABLE IF NOT EXISTS lists "
//                        + "(listID INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, isDeleted INTEGER)";
                        + "(title TEXT, isDeleted INTEGER)";
//        private static final String CREATE_TASK_SQL =
//                "CREATE TABLE IF NOT EXISTS tasks "
//                        + "(taskID INTEGER PRIMARY KEY AUTOINCREMENT, textInput TEXT, isDeleted TINYINT)";

        private static final String DROP_COURSE_SQL =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public StationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_LIST_SQL);
//            sqLiteDatabase.execSQL(CREATE_TASK_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COURSE_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}