package com.zzfht.shippertools.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.zzfht.shippertools.R;
import com.zzfht.shippertools.db.MsgDBManager;
import com.zzfht.shippertools.entity.RemindMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by think on 2015-12-01.
 */
public class ShipperPushMessageReceiver extends PushMessageReceiver {

    /** TAG to Log */
    public static final String TAG = ShipperPushMessageReceiver.class
            .getSimpleName();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static String channelId;

    private MsgDBManager manager ;

    @Override
    public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;

        Log.d(TAG, responseString);

        this.channelId=channelId;

    }

    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {

    }

    @Override
    public void onSetTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {

    }

    @Override
    public void onDelTags(Context context, int errorCode, List<String> sucessTags, List<String> failTags, String requestId) {

    }

    @Override
    public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {

    }

    @Override
    public void onMessage(Context context, String message, String customContentString) {
        Log.e(TAG, message + "111111111111");
        Log.e(TAG, customContentString + "2222222222222");
    }

    @Override
    public void onNotificationClicked(Context context, String tittle, String description, String customContentString) {

    }

    @Override
    public void onNotificationArrived(Context context, String tittle, String description, String customContentString) {

//        preferences = context.getSharedPreferences(context.getResources().getString(R.string.package_name), Context.MODE_PRIVATE);
//        editor = preferences.edit();
        manager = new MsgDBManager(context);

        Log.e(TAG, description + "44444444444444444444");
        Log.e(TAG, customContentString + "5555555555555555555555");
        Log.e(TAG, tittle + "3333333333333333333");

//        RemindMsg msg = new RemindMsg();
//        msg.setTittle(tittle);
//        msg.setContent(description);
//        String phone = null;
//        try {
//            phone = preferences.getString("phone",null);
//
//            msg.setPhone(phone);
//            if (!TextUtils.isEmpty(customContentString)) {
//                JSONObject customJson = null;
//                try {
//                    customJson = new JSONObject(customContentString);
//                    if (!customJson.isNull("type")) {
//                        msg.setType(customJson.getString("type"));
//                    }
//                    if (!customJson.isNull("infoId")) {
//                        msg.setInfoId(customJson.getString("infoId"));
//                    }
//                    if (!customJson.isNull("datetime")) {
//                        msg.setPushDate(customJson.getString("datetime"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        List<RemindMsg> list = new ArrayList<>();
//        list.add(msg);
//        manager.add(list);
//
//        List<RemindMsg> msgs = manager.query(phone);
//        Log.e("listcount+++", String.valueOf(msgs.size()));
//
    }
}
