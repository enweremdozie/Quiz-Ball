package com.quizball.android.quizball;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dozie on 2017-08-30.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "leagues.db";
    public static String FIRST_TABLE_NAME = "championsLeague_table";
    public static String SECOND_TABLE_NAME = "premierLeague_table";
    public static String THIRD_TABLE_NAME = "bundesliga_table";
    public static String FOURTH_TABLE_NAME = "laliga_table";
    public static String CHAMPS_LEAG_LEVEL = "champs_leag_level";
    public static String CHAMPS_LEAG_TRIES = "champs_leag_tries";
    public static String UNITED_KING_LEVEL = "united_king_level";
    public static String UNITED_KING_TRIES = "united_king_tries";
    public static String ESP_LEVEL = "bbva_level";
    public static String ESP_TRIES = "bbva_tries";
    public static String GER_LEVEL = "ger_level";
    public static String GER_TRIES = "ger_tries";
    public static String MUTE = "mute";

    //public static String TABLE2_NAME = "post_table";
    //public static String TABLE3_NAME = "pass_table";

    public static String COL_ID = "ID";
    public static String COL_IF_ANSWERED = "IF_ANSWERED";
    public static String COL_ANSWERED_STATE = "ANSWERED_STATE";
    public static String COL_CHAMPS_LEVEL = "CHAMP_LEVEL";
    public static String COL_CL_TRIES = "CHAMP_TRIES";
    public static String COL_UNITED_LEVEL = "UNITED_LEVEL";
    public static String COL_UNITED_TRIES = "UNITED_TRIES";
    public static String COL_ESP_LEVEL = "ESP_LEVEL";
    public static String COL_ESP_TRIES = "ESP_TRIES";
    public static String COL_GER_LEVEL = "GER_LEVEL";
    public static String COL_GER_TRIES = "GER_TRIES";
    public static String COL_MUTE = "MUTE";



    public static final int DATABASE_VERSION = 1;




    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + FIRST_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

        sqLiteDatabase.execSQL("create table " + SECOND_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

        sqLiteDatabase.execSQL("create table " + THIRD_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IF_ANSWERED INTEGER , ANSWERED_STATE INTEGER)");

        sqLiteDatabase.execSQL("create table " + FOURTH_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

        //CHAMPS LEAGUE TABLES
        sqLiteDatabase.execSQL("create table " + CHAMPS_LEAG_LEVEL + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CHAMP_LEVEL INTEGER)");

        sqLiteDatabase.execSQL("create table " + CHAMPS_LEAG_TRIES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CHAMP_TRIES INTEGER)");

        //UNITED KINGDOM TABLES
        sqLiteDatabase.execSQL("create table " + UNITED_KING_LEVEL + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UNITED_LEVEL INTEGER)");

        sqLiteDatabase.execSQL("create table " + UNITED_KING_TRIES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "UNITED_TRIES INTEGER)");

        //SPAIN TABLES
        sqLiteDatabase.execSQL("create table " + ESP_LEVEL + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ESP_LEVEL INTEGER)");

        sqLiteDatabase.execSQL("create table " + ESP_TRIES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ESP_TRIES INTEGER)");

        //GERMANY TABLES
        sqLiteDatabase.execSQL("create table " + GER_LEVEL + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "GER_LEVEL INTEGER)");

        sqLiteDatabase.execSQL("create table " + GER_TRIES + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "GER_TRIES INTEGER)");

        //MUTE STATE
        sqLiteDatabase.execSQL("create table " + MUTE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MUTE INTEGER)");






        /*String createTable = "CREATE TABLE " + TABLE_NAME + " ( " +
                DBStory.DBMain._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT NOT NULL, " +
                COL_GENRE + " TEXT NOT NULL);";

        String createTable2 = "CREATE TABLE " + TABLE2_NAME + " ( " +
                DBState.DBMain._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_POS + " TEXT NOT NULL);";

        String createTable3 = "CREATE TABLE " + TABLE3_NAME + " ( " +
                DBStoryState.DBMain._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PASS + " TEXT NOT NULL);";*/

        //onUpgrade(sqLiteDatabase, 1, DATABASE_VERSION);
    }

    /*public Integer deleteUser(String id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.delete(TABLE2_NAME, "ID = ?", new String[] {id});
    }*/


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FIRST_TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SECOND_TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FOURTH_TABLE_NAME);
        }

        onCreate(sqLiteDatabase);
    }


    //all Champions League tables start here
    public boolean CLInformation(int answerState, int userAns){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IF_ANSWERED, answerState);
        contentValues.put(COL_ANSWERED_STATE, userAns);

        long result = sqLiteDatabase.insert(FIRST_TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



    public Cursor getCLUnanswered(SQLiteDatabase db){
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] projections = {COL_ID, COL_IF_ANSWERED};
        Cursor cursor = db.query(FIRST_TABLE_NAME, projections,null, null, null, null, null);
        return cursor;
    }


    public Integer delete (String league, SQLiteDatabase db){
        Integer state = 0;
        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        if(league.equals("CL")) {

            db.execSQL("DROP TABLE IF EXISTS " + FIRST_TABLE_NAME);
            db.execSQL("create table " + FIRST_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

            state = 1;
        }

        else if(league.equals("UK")){
            db.execSQL("DROP TABLE IF EXISTS " + SECOND_TABLE_NAME);
            db.execSQL("create table " + SECOND_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

            state = 1;
        }

        else if(league.equals("ESP")){
            db.execSQL("DROP TABLE IF EXISTS " + THIRD_TABLE_NAME);
            db.execSQL("create table " + THIRD_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

            state = 1;
        }

        else if(league.equals("GER")){
            db.execSQL("DROP TABLE IF EXISTS " + FOURTH_TABLE_NAME);
            db.execSQL("create table " + FOURTH_TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "IF_ANSWERED INTEGER, ANSWERED_STATE INTEGER)");

            state = 1;
        }

        return state;
    }



    public boolean changeCLLevel(int level){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CHAMPS_LEVEL, level);

        long result = sqLiteDatabase.insert(CHAMPS_LEAG_LEVEL, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateCLLevel(int level,  String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_CHAMPS_LEVEL, level);

        long result = sqLiteDatabase.update(CHAMPS_LEAG_LEVEL,contentValues, "ID = ?", new String[]{id});

        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor getCLLevel(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_CHAMPS_LEVEL};
        Cursor cursor = db.query(CHAMPS_LEAG_LEVEL, projections,null, null, null, null, null);
        return cursor;
    }

    public boolean ChangeCLTries(int tries){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CL_TRIES, tries);

        long result = sqLiteDatabase.insert(CHAMPS_LEAG_TRIES, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getCLTries(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_CL_TRIES};
        Cursor cursor = db.query(CHAMPS_LEAG_TRIES, projections,null, null, null, null, null);
        return cursor;
    }

    //Champions League tables end here


    //United Kingdom tables start here

    public boolean UKInformation(int answerState, int userAns){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IF_ANSWERED, answerState);
        contentValues.put(COL_ANSWERED_STATE, userAns);

        long result = sqLiteDatabase.insert(SECOND_TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



    public Cursor getUKUnanswered(SQLiteDatabase db){
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] projections = {COL_ID, COL_IF_ANSWERED};
        Cursor cursor = db.query(SECOND_TABLE_NAME, projections,null, null, null, null, null);
        return cursor;
    }



    public boolean changeUKLevel(int level){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_UNITED_LEVEL, level);

        long result = sqLiteDatabase.insert(UNITED_KING_LEVEL, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateUKLevel(int level,  String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_UNITED_LEVEL, level);

        long result = sqLiteDatabase.update(UNITED_KING_LEVEL,contentValues, "ID = ?", new String[]{id});

        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor getUKLevel(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_UNITED_LEVEL};
        Cursor cursor = db.query(UNITED_KING_LEVEL, projections,null, null, null, null, null);
        return cursor;
    }

    public boolean ChangeUKTries(int tries){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_UNITED_TRIES, tries);

        long result = sqLiteDatabase.insert(UNITED_KING_TRIES, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getUKTries(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_UNITED_TRIES};
        Cursor cursor = db.query(UNITED_KING_TRIES, projections,null, null, null, null, null);
        return cursor;
    }

    //UNITED KINGDOM TABLES END HERE

    //ESP TABLES START HERE

    public boolean ESPInformation(int answerState, int userAns){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IF_ANSWERED, answerState);
        contentValues.put(COL_ANSWERED_STATE, userAns);

        long result = sqLiteDatabase.insert(THIRD_TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



    public Cursor getESPUnanswered(SQLiteDatabase db){
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] projections = {COL_ID, COL_IF_ANSWERED};
        Cursor cursor = db.query(THIRD_TABLE_NAME, projections,null, null, null, null, null);
        return cursor;
    }



    public boolean changeESPLevel(int level){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ESP_LEVEL, level);

        long result = sqLiteDatabase.insert(ESP_LEVEL, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateESPLevel(int level,  String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_ESP_LEVEL, level);

        long result = sqLiteDatabase.update(ESP_LEVEL,contentValues, "ID = ?", new String[]{id});

        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor getESPLevel(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_ESP_LEVEL};
        Cursor cursor = db.query(ESP_LEVEL, projections,null, null, null, null, null);
        return cursor;
    }

    public boolean ChangeESPTries(int tries){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ESP_TRIES, tries);

        long result = sqLiteDatabase.insert(ESP_TRIES, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getESPTries(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_ESP_TRIES};
        Cursor cursor = db.query(ESP_TRIES, projections,null, null, null, null, null);
        return cursor;
    }

    //SPAIN TABLES END

    //GERMANY TABLES START
    public boolean GERInformation(int answerState, int userAns){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_IF_ANSWERED, answerState);
        contentValues.put(COL_ANSWERED_STATE, userAns);

        long result = sqLiteDatabase.insert(FOURTH_TABLE_NAME, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



    public Cursor getGERUnanswered(SQLiteDatabase db){
        //SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String[] projections = {COL_ID, COL_IF_ANSWERED};
        Cursor cursor = db.query(FOURTH_TABLE_NAME, projections,null, null, null, null, null);
        return cursor;
    }



    public boolean changeGERLevel(int level){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GER_LEVEL, level);

        long result = sqLiteDatabase.insert(GER_LEVEL, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean updateGERLevel(int level,  String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID, id);
        contentValues.put(COL_GER_LEVEL, level);

        long result = sqLiteDatabase.update(GER_LEVEL,contentValues, "ID = ?", new String[]{id});

        if(result == -1)
            return false;
        else
            return true;
    }


    public Cursor getGERLevel(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_GER_LEVEL};
        Cursor cursor = db.query(GER_LEVEL, projections,null, null, null, null, null);
        return cursor;
    }

    public boolean ChangeGERTries(int tries){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GER_TRIES, tries);

        long result = sqLiteDatabase.insert(GER_TRIES, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getGERTries(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_GER_TRIES};
        Cursor cursor = db.query(GER_TRIES, projections,null, null, null, null, null);
        return cursor;
    }

    //END OF GERMANY

    public boolean muteAudio(int mute, SQLiteDatabase db){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        db.execSQL("DROP TABLE IF EXISTS " + MUTE);

        sqLiteDatabase.execSQL("create table " + MUTE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "MUTE INTEGER)");


        contentValues.put(COL_MUTE, mute);

        long result = sqLiteDatabase.insert(MUTE, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor muteState(SQLiteDatabase db){
        String[] projections = {COL_ID, COL_MUTE};
        Cursor cursor = db.query(MUTE, projections,null, null, null, null, null);
        return cursor;
    }

}



