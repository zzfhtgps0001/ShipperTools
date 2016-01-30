package com.zzfht.shippertools.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by think on 2015-12-17.
 */
public class MessageDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "message.db";
    private static final int DATABASE_VERSION = 1;

    public MessageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS  message" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "tittle TEXT , content TEXT , pushDate TEXT , " +
                "phone TEXT , isRead TEXT DEFAULT 1 ,type TEXT," +
                "infoId TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
