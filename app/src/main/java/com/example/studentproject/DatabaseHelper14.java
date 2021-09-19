package com.example.studentproject;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper14 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="student.db";
    private static final String TABLE_NAME="Batch_14";
    private static final String C1="_id";
    private static final String C2="name";
    private static final String C3="mobile_no";
    private static final String C4="home_town";
    private static final String C5="batch";
    private static final String C6="blood";
    private static final String C7="image";
    private static final String C8="email";
    private static final String C9="facebook";

    private static final String CREATE_TABLE=" create table "+TABLE_NAME+" ( "+C1+ " varchar(10) primary key , "+C2+" varchar(60) , "
            +C3+" varchar(15) , "+C4+" varchar(30) , " +C5+" varchar(15) , "+C6+" varchar(10) , "+C7+" varchar(200) , "+C8+" varchar(70) ,"+C9+" varchar(150) " + " ) ;" ;
    private static final String DROP_TABLE="drop table if exists "+TABLE_NAME;
    private static final String TRUNCATE_TABLE="delete from "+TABLE_NAME;
    //private static final String RESET="delete from SQLITE_SEQUENCE  where name = " + " '" +TABLE_NAME + "' ";
    private static final String SORT_ALL_16="select * from "+TABLE_NAME+" where "+C1+" like 'CE16___' "+ " order by "+C1+" ASC;";
    private static final String SORT_ALL_15="select * from "+TABLE_NAME+" where "+C1+" like 'CE15___' "+ " order by "+C1+" ASC;";
    private static final String SORT_ALL_17="select * from "+TABLE_NAME+" where "+C1+" like 'CE17___' "+ " order by "+C1+" ASC;";
    private static final String OTHER="select * from "+TABLE_NAME+" where "+C1+" not like 'CE17___'  and "+ C1+" not like 'CE16___'  and " +C1+" not like 'CE15___';";
    private static final String SORT_ALL="select * from "+TABLE_NAME+" order by "+C1+" ASC;";
    private static final int VERSION_NUMBER=1;
    private Context context;

    public DatabaseHelper14(@Nullable Context context) {
        super(context, TABLE_NAME, null, VERSION_NUMBER);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        //Toast.makeText(context,"table created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        //Toast.makeText(context,"table dropped", Toast.LENGTH_LONG).show();
        onCreate(db);
    }

    public long insertData(String id, String name,String mobile, String hometown,String batch, String blood,String image,String email,String facebook){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(C1,id);
        contentValues.put(C2,name);
        contentValues.put(C3,mobile);
        contentValues.put(C4,hometown);
        contentValues.put(C5,batch);
        contentValues.put(C6,blood);
        contentValues.put(C7,image);
        contentValues.put(C8,email);
        contentValues.put(C9,facebook);
        long rowId=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowId;
    }
    public void truncateTable()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+TABLE_NAME);
        //sqLiteDatabase.rawQuery(TRUNCATE_TABLE,null);
    }

    public Cursor showAllData()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(SORT_ALL,null);
        return c;
    }
    public Cursor showAllData_s_16()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(SORT_ALL_16,null);
        return c;
    }
    public Cursor showAllData_s_15()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(SORT_ALL_15,null);
        return c;
    }
    public Cursor showAllData_s_17()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(SORT_ALL_17,null);
        return c;
    }
    public Cursor showOther()
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor c=sqLiteDatabase.rawQuery(OTHER,null);
        return c;
    }
}
