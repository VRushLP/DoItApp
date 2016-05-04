package teamten.tacoma.uw.edu.doit.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Adds a new user based on user input for email and password
 * and will be stored to the database.
 */
public class RegistrationDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Registration.db";

    private RegistrationDBHelper mRegistrationDBHelper;
    private SQLiteDatabase mSQLiteDatabase;


    public RegistrationDB(Context context) {
        mRegistrationDBHelper = new RegistrationDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mRegistrationDBHelper.getWritableDatabase();
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    private static final String USER_TABLE = "users";

//
//     /**
//     * Returns the list of courses from the course table.
//     * @return list
//     */
//    public List<Course> getCourses() {
//
//        String[] columns = {
//                "id", "shortDesc", "longDesc", "prereqs"
//        };
//
//        Cursor c = mSQLiteDatabase.query(
//                COURSE_TABLE,  // The table to query
//                columns,                               // The columns to return
//                null,                                // The columns for the WHERE clause
//                null,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                null                                 // The sort order
//        );
//        c.moveToFirst();
//        List<Course> list = new ArrayList<Course>();
//        for (int i=0; i<c.getCount(); i++) {
//            String id = c.getString(0);
//            String shortDesc = c.getString(1);
//            String longDesc = c.getString(2);
//            String prereqs = c.getString(3);
//            Course course = new Course(id, shortDesc, longDesc, prereqs);
//            list.add(course);
//            c.moveToNext();
//        }
//
//        return list;
//    }



    /**
     * Inserts the user into the local sqlite table. Returns true if successful, false otherwise.
     * @param email
     * @param password
     * @return true or false
     */
    public boolean insertUser(String email, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);

        long rowId = mSQLiteDatabase.insert("user", null, contentValues);
        return rowId != -1;
    }

//  Could be used later to allow deletion of user accounts?
//    /**
//     * Delete all the data from the COURSE_TABLE
//     */
//    public void deleteLoginReg() {
//        mSQLiteDatabase.delete(USER_TABLE, null, null);
//    }


    class RegistrationDBHelper extends SQLiteOpenHelper {

        private static final String CREATE_USER_SQL =
                "CREATE TABLE IF NOT EXISTS Course "
                        + "(id TEXT PRIMARY KEY, shortDesc TEXT, longDesc TEXT, prereqs TEXT)";

        private static final String DROP_COURSE_SQL =
                "DROP TABLE IF EXISTS Course";

        public RegistrationDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_USER_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_COURSE_SQL);
            onCreate(sqLiteDatabase);
        }

    }
}
