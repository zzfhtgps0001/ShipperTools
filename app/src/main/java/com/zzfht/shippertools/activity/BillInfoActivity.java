package com.zzfht.shippertools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzfht.shippertools.R;
import com.zzfht.shippertools.util.NormalPostRequest;
import com.zzfht.shippertools.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.view.View.*;
import static android.view.View.VISIBLE;

public class BillInfoActivity extends Activity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "billInfoActivity";

    private Button bill_info_btn_push;
    private EditText bill_info_medium, bill_info_appove, bill_info_actual, bill_info_receiver, bill_info_destination, bill_info_shipperDate, bill_info_shipper, bill_info_license, bill_info_tel, bill_info_vehicleFlapper, bill_info_vehicleTransportNo, bill_info_trailerFlapper, bill_info_trailerTransportNo, bill_info_driverName, bill_info_drivingnumber, bill_info_driverCertificate, bill_info_driverPhone, bill_info_supercargoName, bill_info_idnumber, bill_info_supercargoCertificate, bill_info_supercargoPhone;
    private TextView emerg_health_hazards, emerg_danger, emerg_reactivity, emerg_transport_security,
            emerg_first_aid, emerg_firefighting_method, emerg_release_measurces, emerg_special_warnings;
    private View baseView, safeView;
    private RadioGroup bill_info_rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_info);

        initView();

        getBaseData();

    }


    private void initView() {


        Log.e(TAG, "initView:extra=" + getIntent().getStringExtra("id"));

        new TitleBuilder(this).setTitleText("信息查看").setRightText("GPS").setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillInfoActivity.this, "GPS信息", Toast.LENGTH_SHORT).show();
            }
        });

        baseView = findViewById(R.id.bill_info_base_frag);
        safeView = findViewById(R.id.bill_info_safe_frag);
        baseView.setVisibility(VISIBLE);
        safeView.setVisibility(GONE);

        bill_info_btn_push = (Button) findViewById(R.id.bill_info_btn_push);

        bill_info_rg = (RadioGroup) findViewById(R.id.bill_info_rg);
        bill_info_rg.setOnCheckedChangeListener(this);

        Log.e(TAG, getIntent().getStringExtra("id"));

        bill_info_medium = (EditText) findViewById(R.id.bill_info_medium);//危险品名称
        bill_info_appove = (EditText) findViewById(R.id.bill_info_appove);//核载量
        bill_info_actual = (EditText) findViewById(R.id.bill_info_actual);//实载量
        bill_info_receiver = (EditText) findViewById(R.id.bill_info_receiver);//收货方
        bill_info_destination = (EditText) findViewById(R.id.bill_info_destination);//目的地
        bill_info_shipperDate = (EditText) findViewById(R.id.bill_info_shipperDate);//托运日期

        bill_info_shipper = (EditText) findViewById(R.id.bill_info_shipper);//承运方
        bill_info_license = (EditText) findViewById(R.id.bill_info_license);//经营许可证号
        bill_info_tel = (EditText) findViewById(R.id.bill_info_tel);//承运方电话

        bill_info_vehicleFlapper = (EditText) findViewById(R.id.bill_info_vehicleFlapper);//牵引车车牌号
        bill_info_vehicleTransportNo = (EditText) findViewById(R.id.bill_info_vehicleTransportNo);//牵引车道路运输证号
        bill_info_trailerFlapper = (EditText) findViewById(R.id.bill_info_trailerFlapper);//挂车车牌号
        bill_info_trailerTransportNo = (EditText) findViewById(R.id.bill_info_trailerTransportNo);//挂车道路运输证号
        bill_info_driverName = (EditText) findViewById(R.id.bill_info_driverName);//驾驶员姓名
        bill_info_drivingnumber = (EditText) findViewById(R.id.bill_info_drivingnumber);//驾驶证号
        bill_info_driverCertificate = (EditText) findViewById(R.id.bill_info_driverCertificate);//驾驶员从业资格证号
        bill_info_driverPhone = (EditText) findViewById(R.id.bill_info_driverPhone);//驾驶员电话
        bill_info_supercargoName = (EditText) findViewById(R.id.bill_info_supercargoName);//押运员姓名
        bill_info_idnumber = (EditText) findViewById(R.id.bill_info_idnumber);//押运员身份证号
        bill_info_supercargoCertificate = (EditText) findViewById(R.id.bill_info_supercargoCertificate);//押运员从业资格证号
        bill_info_supercargoPhone = (EditText) findViewById(R.id.bill_info_supercargoPhone);//押运员电话

        emerg_health_hazards = (TextView) findViewById(R.id.emerg_health_hazards);
        emerg_danger = (TextView) findViewById(R.id.emerg_danger);
        emerg_reactivity = (TextView) findViewById(R.id.emerg_reactivity);
        emerg_transport_security = (TextView) findViewById(R.id.emerg_transport_security);
        emerg_first_aid = (TextView) findViewById(R.id.emerg_first_aid);
        emerg_firefighting_method = (TextView) findViewById(R.id.emerg_firefighting_method);
        emerg_release_measurces = (TextView) findViewById(R.id.emerg_release_measurces);
        emerg_special_warnings = (TextView) findViewById(R.id.emerg_special_warnings);

    }


    private void getSafeData() {
        HashMap<String, String> params = new HashMap<>();
        // 获取前一个Intent中的传递过来的数据
        String medium = bill_info_medium.getText().toString();
        if (medium == null) {
            params.put("oneDangerousGoodsName", "");
        } else {
            params.put("oneDangerousGoodsName", medium);
        }
        // 数据请求地址
        String url = getResources().getString(R.string.url) + "/getEmergencyMeasuresByGoods.json";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        NormalPostRequest request = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("data", response.get("data").toString());
                            if (response.get("data") == null || response.get("data").toString().equals("null")) {
                                Toast.makeText(BillInfoActivity.this, "该档案还未建立", Toast.LENGTH_SHORT).show();
                            } else {
                                JSONObject data = (JSONObject) response.get("data");
                                setText(emerg_health_hazards, data, "healthHazards");
                                setText(emerg_danger, data, "danger");
                                setText(emerg_reactivity, data, "reactivity");
                                setText(emerg_transport_security, data, "transportationSecurity");
                                setText(emerg_first_aid, data, "firstaid");
                                setText(emerg_firefighting_method, data, "fireFightingMethods");
                                setText(emerg_release_measurces, data, "releaseMeasurces");
                                setText(emerg_special_warnings, data, "specialWarnings");
                                Log.e("emerg_info", data.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(BillInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.e("emerg_info_error", volleyError.toString());

                    }
                }
                , params);
        requestQueue.add(request);
    }

    private void getBaseData() {

        Map<String, String> params = new HashMap<>();
        params.put("id", getIntent().getStringExtra("id"));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //调用接口解码二维码信息，该接口数据需以GET方式发送
        String url = getResources().getString(R.string.url) + "getEwaybillById.json";
        NormalPostRequest request = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            if (response.get("errno").equals("0")) {
                                JSONObject data = response.getJSONObject("data");
                                setText(bill_info_medium, data, "oneDangerousGoodsName");
                                setText(bill_info_appove, data, "oneSumTonCubic");
                                setText(bill_info_actual, data, "actual");
                                setText(bill_info_receiver, data, "placeCompanyName");
                                bill_info_destination.setText(data.getString("placeProvince")
                                        + data.getString("placeDestination")
                                        + data.getString("placepowiat"));
                                setText(bill_info_shipperDate, data, "shipperDate");

                                setText(bill_info_shipper, data, "carrierCompanyName");
                                setText(bill_info_license, data, "carrierLicense");
                                setText(bill_info_tel, data, "carrierPhone");

                                setText(bill_info_vehicleFlapper, data, "vehicleFlapper");
                                setText(bill_info_vehicleTransportNo, data, "vehicleTransportNo");
                                setText(bill_info_trailerFlapper, data, "trailerFlapper");
                                setText(bill_info_trailerTransportNo, data, "trailerTransportNo");
                                setText(bill_info_driverName, data, "driverName");
                                setText(bill_info_drivingnumber, data, "drivingnumber");
                                setText(bill_info_driverCertificate, data, "driverCertificate");
                                setText(bill_info_driverPhone, data, "driverPhone");
                                setText(bill_info_supercargoName, data, "supercargoName");
                                setText(bill_info_idnumber, data, "idnumber");
                                setText(bill_info_supercargoCertificate, data, "supercargoCertificate");
                                setText(bill_info_supercargoPhone, data, "supercargoPhone");

                                int orderStatusType = data.getInt("orderStatusType");
                                if(orderStatusType==2){
                                    bill_info_btn_push.setText("已发车");
                                }else if (orderStatusType==3){
                                    bill_info_btn_push.setText("已到达");
                                }

                            } else {
                                response.get("message");
                                Toast.makeText(BillInfoActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(BillInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        Log.e("checkConsignor", volleyError.toString());
                    }
                }, params);

        requestQueue.add(request);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.bill_info_rb_left:
                baseView.setVisibility(VISIBLE);
                safeView.setVisibility(GONE);
                break;
            case R.id.bill_info_rb_right:
                baseView.setVisibility(GONE);
                safeView.setVisibility(VISIBLE);
                getSafeData();
                break;
        }

    }

    private void setText(TextView tv, JSONObject json, String sign) {
        try {
            String value = json.getString(sign);
            if (!value.equals("null")) {
                tv.setText(value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
