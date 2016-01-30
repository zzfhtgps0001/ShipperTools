package com.zzfht.shippertools.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.zzfht.shippertools.R;
import com.zzfht.shippertools.push.ShipperPushMessageReceiver;
import com.zzfht.shippertools.util.NormalPostRequest;
import com.zzfht.shippertools.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {

    private AutoCompleteTextView login_account;
    private AutoCompleteTextView login_password;
    private Button login_lgBtn;
    private static final String TAG = "login";
    private SharedPreferences preferences;
    private Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY, getResources().getString(R.string.baidu_push_key));

        preferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.package_name), Context.MODE_PRIVATE);
        editor = preferences.edit();

        initView();

    }

    private void initView(){
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.app_name));

        login_account = (AutoCompleteTextView) findViewById(R.id.login_account);
        login_password = (AutoCompleteTextView) findViewById(R.id.login_password);
        login_lgBtn = (Button) findViewById(R.id.login_lgBtn);

        login_lgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        String account = login_account.getText().toString();
        String password = login_password.getText().toString();

        startActivity(new Intent(MainActivity.this,HomeActivity.class));

        if(TextUtils.isEmpty(account)){
            login_account.requestFocus();
            login_account.setError("请输入账号");

        }else if (TextUtils.isEmpty(password)){
            login_password.requestFocus();
            login_password.setError("请输入密码");
        }else{

            Map<String,String> params = new HashMap<>();
            params.put("account",account);
            params.put("password",password);
            params.put("roleId","1111");
            params.put("channelId", ShipperPushMessageReceiver.channelId);
            params.put("channelType","3");

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            String url = getResources().getString(R.string.url)+"checkConsignor.json";
            NormalPostRequest request = new NormalPostRequest( url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                if(response.get("errno").equals("0")){
                                    JSONObject data = response.getJSONObject("data");
                                    String account = data.getString("account");
                                    String companyId = data.getString("companyId");
                                    editor.putString("companyId",companyId);
                                    editor.putString("account",account);
                                    editor.commit();
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                                } else if (response.get("errno").equals("1")){
                                    Toast.makeText(MainActivity.this,response.get("msg").toString(),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener(){

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                            Log.e("checkConsignor",volleyError.toString());
                        }
                    },params);

            requestQueue.add(request);
        }
    }

}
