package sdi.com.currencywizard.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sdi.com.currencywizard.model.BasketList;
import sdi.com.currencywizard.model.StoresList;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "currency_wizard.db";

    //Basket list Table Name
    private static final String TABLE_BASKET_LIST="basket_list";
    //Basket list Table Name
    private static final String TABLE_STORE_LIST="store_list";

    //Basket list Table Columns names

    private static final String BASKET_LIST_ID = "id";
    private static final String BASKET_LIST_TITLE = "title";
    private static final String BASKET_LIST_DATE_TIME = "title_date_time";

   //Store list Table Columns names

    private static final String STORE_LIST_ID = "id";
    private static final String STORE_LIST_BASKET_LIST_ID = "basket_id";
    private static final String STORE_LIST_BASKET_LIST_NOTE = "note";
    private static final String STORE_LIST_BASKET_LIST_FROM_AMT = "from_amt";
    private static final String STORE_LIST_BASKET_LIST_FROM_CODE = "from_code";
    private static final String STORE_LIST_BASKET_LIST_FROM_SYM = "from_sym";
    private static final String STORE_LIST_BASKET_LIST_TO_AMT = "to_amt";
    private static final String STORE_LIST_BASKET_LIST_TO_CODE = "to_code";
    private static final String STORE_LIST_BASKET_LIST_TO_SYM = "to_sym";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_TABLE_BASKET_LIST = "CREATE TABLE " + TABLE_BASKET_LIST + "(" + BASKET_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + BASKET_LIST_TITLE + " TEXT," + BASKET_LIST_DATE_TIME + " TEXT )";

        String CREATE_TABLE_STORE_LIST = "CREATE TABLE " + TABLE_STORE_LIST + "(" + STORE_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + STORE_LIST_BASKET_LIST_ID + " TEXT,"+ STORE_LIST_BASKET_LIST_NOTE + " TEXT,"+ STORE_LIST_BASKET_LIST_FROM_AMT + " TEXT,"+ STORE_LIST_BASKET_LIST_FROM_CODE + " TEXT,"+STORE_LIST_BASKET_LIST_FROM_SYM + " TEXT,"+ STORE_LIST_BASKET_LIST_TO_AMT + " TEXT," + STORE_LIST_BASKET_LIST_TO_CODE + " TEXT,"+ STORE_LIST_BASKET_LIST_TO_SYM + " TEXT )";


        db.execSQL(CREATE_TABLE_BASKET_LIST);
        db.execSQL(CREATE_TABLE_STORE_LIST);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        Log.w(DatabaseHandler.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        // Drop older table if existed
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASKET_LIST);
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_LIST);
    // Create tables again
      onCreate(db);
    }

    public int addBasket(BasketList add) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BASKET_LIST_TITLE, add.getBasket_title());
        values.put(BASKET_LIST_DATE_TIME,add.getCreate_date_time());

        long insertId = db.insert(TABLE_BASKET_LIST, null, values);
        db.close();
        return (int)insertId;
    }


    public void addStore(StoresList add) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STORE_LIST_BASKET_LIST_ID, add.getBasket_id());
        values.put(STORE_LIST_BASKET_LIST_NOTE,add.getNote());
        values.put(STORE_LIST_BASKET_LIST_FROM_AMT,add.getFrom_amt());
        values.put(STORE_LIST_BASKET_LIST_FROM_CODE,add.getFrom_code());
        values.put(STORE_LIST_BASKET_LIST_FROM_SYM,add.getFrom_sym());
        values.put(STORE_LIST_BASKET_LIST_TO_AMT,add.getTo_amt());
        values.put(STORE_LIST_BASKET_LIST_TO_CODE,add.getTo_code());
        values.put(STORE_LIST_BASKET_LIST_TO_SYM,add.getTo_sym());

        db.insert(TABLE_STORE_LIST, null, values);
        db.close();
    }

    public int getBasketCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BASKET_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();
        return cnt;
    }
    public List<BasketList> getAllBasket() {
        List<BasketList> basketList = new ArrayList<BasketList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BASKET_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BasketList a = new BasketList();
                a.setId(cursor.getInt(0));
                a.setBasket_title(cursor.getString(1));
                a.setCreate_date_time(cursor.getString(2));

                basketList.add(a);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        // return getAllBasket list
        return basketList;
    }

    public List<StoresList> getStore(String basket_id) {
        List<StoresList> StoreList = new ArrayList<StoresList>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_STORE_LIST +" where basket_id='"+ basket_id+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StoresList a = new StoresList();
                a.setId(cursor.getInt(0));
                a.setBasket_id(cursor.getString(1));
                a.setNote(cursor.getString(2));
                a.setFrom_amt(cursor.getString(3));
                a.setFrom_code(cursor.getString(4));
                a.setFrom_sym(cursor.getString(5));
                a.setTo_amt(cursor.getString(6));
                a.setTo_code(cursor.getString(7));
                a.setTo_sym(cursor.getString(8));

                StoreList.add(a);
            } while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        // return Store list
        return StoreList;
    }

    // Deleting basket id
    public void deleteBasket(BasketList basket) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BASKET_LIST, BASKET_LIST_ID + " = ?",
                new String[] { String.valueOf(basket.getId()) });
        db.close();
    }

    // Deleting store id
    public void deleteStore(StoresList store) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORE_LIST, STORE_LIST_BASKET_LIST_ID + " = ?",
                new String[] { String.valueOf(store.getId()) });
        db.close();
    }
// Deleting store id
    public void deleteStore1(StoresList store) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORE_LIST, STORE_LIST_ID + " = ?",
                new String[] { String.valueOf(store.getId()) });
        db.close();
    }

    //update basket

    public int updateBasket(BasketList basket) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BASKET_LIST_TITLE,basket.getBasket_title());

        // updating row
        return db.update(TABLE_BASKET_LIST, values, BASKET_LIST_ID + " = ?",
                new String[] { String.valueOf(basket.getId()) });

    }

}