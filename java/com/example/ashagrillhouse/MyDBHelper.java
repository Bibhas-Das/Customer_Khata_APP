package com.example.ashagrillhouse;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AshaGrillHouse";
    private static final int DATABASE_VERSION = 1;
    //Drakar nei
    private static final String TAG = "MyDBHelper";

    Context parent_context;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        parent_context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            //All tables create uder the database
            //sqLiteDatabase.execSQL("CREATE TABLE contact ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' TEXT, 'phone' TEXT)");
            sqLiteDatabase.execSQL("CREATE TABLE password ('number' INTEGER PRIMARY KEY AUTOINCREMENT, 'user' TEXT, 'password' TEXT, 'attempt' INTEGER)");
            Log.d(TAG, "Table created successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS contact");
            onCreate(sqLiteDatabase);
            Log.d(TAG, "Table upgraded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error upgrading table: " + e.getMessage());
        }
    }

    public void runQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(query);
            Log.d(TAG, "Query executed successfully: " + query);
        } catch (Exception e) {
            Log.e(TAG, "Error executing query: " + e.getMessage());
        } finally {
            db.close();
        }
    }


//////////////////////////
public List<String[]> selectTable(String query) {
    SQLiteDatabase db = this.getReadableDatabase();
    List<String[]> resultList = new ArrayList<>();

    try (Cursor cursor = db.rawQuery(query, null)) {
        if (cursor != null && cursor.moveToFirst()) {
            int columnCount = cursor.getColumnCount();
            do {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = cursor.getString(i);
                }
                resultList.add(row);
            } while (cursor.moveToNext());
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        db.close();
    }

    return resultList;
}

/////////////////////
}



/////////////// USES ////////////////////
/*
//Database connection and set default password
        try {
            MyDBHelper mdb = new MyDBHelper(this);

            // Database connection and set default password
            List<String[]> dataList = mdb.selectTable("SELECT * from password where user='Biggyan'");
            if (dataList.isEmpty())
            {
                mdb.runQuery("INSERT INTO password (user, password, attempt) VALUES ('Biggyan', '"+SHA256("9932")+"', 6)");
                //Toast.makeText(this, "Password is set in password table", Toast.LENGTH_LONG).show();
            }
            else
            {
                String[] firstRow = dataList.get(0); // Get the first row
                if (firstRow.length > 1)
                {
                    NO_ATTEMPT_LOGIN = Integer.parseInt(firstRow[3]);
                  //  Toast.makeText(this, "Get Attempt status from database", Toast.LENGTH_LONG).show();
                }
            }
            // Close the database after you're done with it
            mdb.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
 */
/////////////////////////////////////////