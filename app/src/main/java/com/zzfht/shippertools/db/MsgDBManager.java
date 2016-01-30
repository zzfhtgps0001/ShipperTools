package com.zzfht.shippertools.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zzfht.shippertools.entity.RemindMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2015-12-17.
 */
public class MsgDBManager {
    private MessageDBHelper helper;
    private SQLiteDatabase db;

    public MsgDBManager(Context context) {
        helper = new MessageDBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public void add(List<RemindMsg> list) {
        db.beginTransaction();
        try{
            for (RemindMsg msg : list) {
                db.execSQL("INSERT INTO message VALUES(null,?,?,?,?,'1',?,?)",
                        new Object[]{msg.getTittle(), msg.getContent(), msg.getPushDate(), msg.getPhone(),
                        msg.getType(),msg.getInfoId()});
            }
            db.setTransactionSuccessful();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }

    }

    public List<RemindMsg> query(String phone){
        if(phone==null||phone.equals("")){
            return null;
        }
        ArrayList<RemindMsg> msgs = new ArrayList<>();
        Cursor cursor = db.rawQuery(" SELECT * FROM message where phone = ? ORDER BY _id DESC", new String[]{phone});
        while (cursor.moveToNext()){
            RemindMsg msg = new RemindMsg();
            msg.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            msg.setTittle(cursor.getString(cursor.getColumnIndex("tittle")));
            msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
            msg.setPushDate(cursor.getString(cursor.getColumnIndex("pushDate")));
            msg.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            msgs.add(msg);
        }
        return msgs;
    }

    public void deleteById(String id){
        db.beginTransaction();
        try {
            Log.e("dbmanager", "deleteById");
            db.delete("message","_id = ?",new String[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }

    }

    public void updateRead(String id){
        db.beginTransaction();
        try{
            db.execSQL("UPDATE message SET isRead = 1 where _id = ?",new Object[]{id});
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

}
