package com.example.aprochakovskiy.batt_watcher;

/**
 * Created by a.prochakovskiy on 28.08.2015.
 */
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "batterystatus";

    // Contacts table name
    private static final String TABLE_CONTACTS = "batterydata";

    // Contacts Table Columns names
    private static final String BAT_ID = "battary_id";
    private static final String BAT_VOLT = "battary_voltage";
    private static final String BAT_DT = "battary_datetime";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //db = this.getWritableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + BAT_ID + " INTEGER PRIMARY KEY," + BAT_VOLT + " INTEGER,"
                + BAT_DT + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void replaceContact(Battery battery) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BAT_ID, battery.getID());
        values.put(BAT_VOLT, battery.getVoltage());
        values.put(BAT_DT, new Date().getTime());

        db.replace(TABLE_CONTACTS, null, values);
        //db.close();
    }

    // Getting All Contacts
    public ArrayList<Battery> getAllContactsOdd() {
        ArrayList<Battery> contactList = new ArrayList<Battery>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get Min Values
        int my_min_value=2700;
        int my_max_value=3600;

        /*
        // Select MIN Query
        if ( my_min_value ==0 ) {
            String selectminQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + "=900 ORDER BY " + BAT_ID;
            Cursor mincursor = db.rawQuery(selectminQuery, null);
            if (mincursor.moveToFirst()) {
                do {
                    my_min_value = Integer.parseInt(mincursor.getString(0));
                } while (mincursor.moveToNext());
            }
        };

        if ( my_min_value ==0 ) my_min_value=2600; //Default if no in DB


        // Select MAX Query
        if ( my_max_value ==0 ) {
            String selectmaxQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + "=901 ORDER BY " + BAT_ID;
            Cursor maxcursor = db.rawQuery(selectmaxQuery, null);
            if (maxcursor.moveToFirst()) {
                do {
                    my_max_value = Integer.parseInt(maxcursor.getString(0));
                } while (maxcursor.moveToNext());
            }
        }
        if ( my_max_value ==0 ) my_max_value=2600; //Default if no in DB
        */

        // Select All Query
        String selectQuery = "SELECT  "+BAT_ID+","+BAT_VOLT+","+BAT_DT+" FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + " % 2 = 1" + " ORDER BY " + BAT_ID;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                if (!cursor.getString(0).equals("900"))
                if (!cursor.getString(0).equals("901"))  {
                    Battery contact = new Battery();
                    contact.setID(Integer.parseInt(cursor.getString(0)));
                    contact.setVoltage(Integer.parseInt(cursor.getString(1)));
                    contact.setVoltageDatetime(new Date(cursor.getLong(2)));
                    contact.set_min_value(my_min_value);
                    contact.set_max_value(my_max_value);
                    // Adding contact to list
                    contactList.add(contact);
                }

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Getting All Contacts
    public ArrayList<Battery> getAllContactsEven() {
        ArrayList<Battery> contactList = new ArrayList<Battery>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Get Min Values
        int my_min_value=2700;
        int my_max_value=3600;

        /*
        // Select MIN Query
        if ( my_min_value ==0 ) {
            String selectminQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + "=900 ORDER BY " + BAT_ID;
            Cursor mincursor = db.rawQuery(selectminQuery, null);
            if (mincursor.moveToFirst()) {
                do {
                    my_min_value = Integer.parseInt(mincursor.getString(0));
                } while (mincursor.moveToNext());
            }
        };

        if ( my_min_value ==0 ) my_min_value=2600; //Default if no in DB


        // Select MAX Query
        if ( my_max_value ==0 ) {
            String selectmaxQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + "=901 ORDER BY " + BAT_ID;
            Cursor maxcursor = db.rawQuery(selectmaxQuery, null);
            if (maxcursor.moveToFirst()) {
                do {
                    my_max_value = Integer.parseInt(maxcursor.getString(0));
                } while (maxcursor.moveToNext());
            }
        }
        if ( my_max_value ==0 ) my_max_value=2600; //Default if no in DB
        */

        // Select All Query
        String selectQuery = "SELECT  "+BAT_ID+","+BAT_VOLT+","+BAT_DT+" FROM " + TABLE_CONTACTS + " WHERE " + BAT_ID + " % 2 = 0" + " ORDER BY " + BAT_ID;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                if (!cursor.getString(0).equals("900"))
                    if (!cursor.getString(0).equals("901"))  {
                        Battery contact = new Battery();
                        contact.setID(Integer.parseInt(cursor.getString(0)));
                        contact.setVoltage(Integer.parseInt(cursor.getString(1)));
                        contact.setVoltageDatetime(new Date(cursor.getLong(2)));
                        contact.set_min_value(my_min_value);
                        contact.set_max_value(my_max_value);
                        // Adding contact to list
                        contactList.add(contact);
                    }

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }






}
