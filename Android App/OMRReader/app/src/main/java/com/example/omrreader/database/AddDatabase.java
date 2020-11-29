package com.example.omrreader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.omrreader.Details.DetailsModel;

import java.util.ArrayList;
import java.util.List;

public class AddDatabase extends SQLiteOpenHelper {

    private static String DATABASE_NAME="Add.db";
    private static String TABLE_NAME="AddTable";
    private static String COL1="ID";
    private static String COL2="SETID";
    private static String COL3="NAME";
    private static String COL4="CORRECT";
    private static String COL5="WRONG";
    private static String COL6="NOOFQUESTIONS";

    public AddDatabase(@Nullable Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"SETID TEXT,NAME TEXT,CORRECT TEXT, WRONG TEXT,NOOFQUESTIONS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insert(DetailsModel detailsModel){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,detailsModel.getSetId());
        contentValues.put(COL3,detailsModel.getName());
        contentValues.put(COL4,detailsModel.getCorrect());
        contentValues.put(COL5,detailsModel.getWrong());
        contentValues.put(COL6,detailsModel.getQuestions());

        long res=db.insert(TABLE_NAME,null,contentValues);
        if(res==-1)
            return false;
        else
            return true;
    }

    public List<DetailsModel> getAllData(){
        List<DetailsModel> list=new ArrayList<>();
        //select query
        String query="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        if(cursor.moveToNext()){
            do{
                DetailsModel detailsModel=new DetailsModel();
                detailsModel.setSetId(cursor.getString(cursor.getColumnIndex(COL2)));
                detailsModel.setName(cursor.getString(cursor.getColumnIndex(COL3)));
                detailsModel.setCorrect(cursor.getString(cursor.getColumnIndex(COL4)));
                detailsModel.setWrong(cursor.getString(cursor.getColumnIndex(COL5)));
                detailsModel.setQuestions(cursor.getString(cursor.getColumnIndex(COL6)));
                list.add(detailsModel);
            }while (cursor.moveToNext());
        }
        return list;
    }

}
