package com.example.dhiren.passkeymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by DHIREN on 30-08-2016.
 */
public class Info {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "username";
    public static final String KEY_PASSWORD = "passkey";

    public static final String DATABASE_NAME = "DB1";
    public static final String DATABASE_TABLE = "InfoDB";
    public static final int DATABASE_VERSION = 1;

    private DbHelper ourHelper,ourHelper1;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;


    private static class DbHelper extends SQLiteOpenHelper
    {

        public DbHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NAME + " TEXT NOT NULL, " +
                    KEY_PASSWORD + " TEXT NOT NULL);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
    public Info(Context c)
    {
        ourContext = c;
    }

    public Info open() throws SQLException {
        ourHelper = new DbHelper(ourContext);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        ourHelper.close();
    }



    public Boolean loginCheck(String email,String password)
    {
        Boolean flag=false;
        String selectQuery = " SELECT  * FROM " + DATABASE_TABLE + " WHERE username= " + email +" AND passkey= "+ password;

        //ourHelper1 = new DbHelper(ourContext);
        //SQLiteDatabase db = ourHelper1.getReadableDatabase();
        Cursor cursor = ourDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() == 1){
            flag=true;
        }
        else
        {
            flag=false;
        }

        return flag;
    }


    public long createEntry(String name, String password)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_PASSWORD, password);
        return ourDatabase.insert(DATABASE_TABLE,null,cv);
    }



    public String getData()
    {
        String[] columns = new String[]{KEY_ROWID,KEY_NAME, KEY_PASSWORD};
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null, null, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iPassword = c.getColumnIndex(KEY_PASSWORD);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            result = result + c.getString(iRow) + " " + c.getString(iName) + " " + c.getString(iPassword) + "\n";
        }
        return result;
    }
}
