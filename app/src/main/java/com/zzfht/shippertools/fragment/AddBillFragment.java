package com.zzfht.shippertools.fragment;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.zxing.common.StringUtils;
import com.zxing.activity.CaptureActivity;
import com.zzfht.shippertools.R;
import com.zzfht.shippertools.activity.HomeActivity;
import com.zzfht.shippertools.spinner.ClickControlledSpinner;
import com.zzfht.shippertools.util.GenerateSign;
import com.zzfht.shippertools.util.NormalPostRequest;
import com.zzfht.shippertools.util.TitleBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBillFragment extends ProvinceFragment {

    private ClickControlledSpinner driverSpinner, supercargoSpinner;
    private Handler mHandle;
    private Button add_bill_btn_scan, add_bill_btn_save;
    private EditText add_bill_vehicle, add_bill_company, add_bill_weight,add_bill_destination;
//    private TextView ;
    private List<String> driverList = new ArrayList<>();
    private List<String> srpercargoList = new ArrayList<>();
    private JSONArray driverJson = new JSONArray();
    private JSONArray cargoJson = new JSONArray();
    private OptionsPickerView pvOptions;
    private ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<ArrayList<ArrayList<String>>>();
    private View vMasker;
    private String mDriver, mCargo;
    private SharedPreferences preferences;


    private View view;

    public AddBillFragment( ) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_bill, container, false);

        preferences = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);

        initView();

        initProvince();

        return view;
    }

    private void initProvince() {
        pvOptions = new OptionsPickerView(getActivity());

        initProvinceDatas();

        //选项1
        for (int i = 0; i < mProvinceDatas.length; i++) {
            options1Items.add(i, mProvinceDatas[i]);
            String[] cityArray = mCitisDatasMap.get(mProvinceDatas[i]);
            ArrayList<String> cityList = new ArrayList<>();
            ArrayList<ArrayList<String>> districtList_01 = new ArrayList<>();

            for (int j = 0; j < cityArray.length; j++) {
                cityList.add(j, cityArray[j]);
                String[] districtArray = mDistrictDatasMap.get(cityArray[j]);
                ArrayList<String> districtList_01_01 = new ArrayList<>();

                for (int k = 0; k < districtArray.length; k++) {
                    districtList_01_01.add(k, districtArray[k]);
                }

                districtList_01.add(j, districtList_01_01);
            }

            //选项2
            options2Items.add(i, cityList);
            //选项3
            options3Items.add(i, districtList_01);
        }

        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle("选择城市");
        pvOptions.setCyclic(false, true, true);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(1, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1)
                        + options2Items.get(options1).get(option2)
                        + options3Items.get(options1).get(option2).get(options3);
                add_bill_destination.setText(tx);
                mCurrentProviceName = options1Items.get(options1);
                mCurrentCityName = options2Items.get(options1).get(option2);
                mCurrentDistrictName = options3Items.get(options1).get(option2).get(options3);
                vMasker.setVisibility(View.GONE);
            }
        });
        //点击弹出选项选择器
        view.findViewById(R.id.add_bill_destination).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });

    }

    private void initView() {
        new TitleBuilder(view).setTitleText("新增托运单");

        add_bill_vehicle = (EditText) view.findViewById(R.id.add_bill_vehicle);
        add_bill_destination = (EditText) view.findViewById(R.id.add_bill_destination);
        add_bill_company = (EditText) view.findViewById(R.id.add_bill_company);
        add_bill_weight = (EditText) view.findViewById(R.id.add_bill_et_weight);
        vMasker = view.findViewById(R.id.vMasker);


        //扫描二维码按钮
        add_bill_btn_scan = (Button) view.findViewById(R.id.add_bill_scan_btn);
        add_bill_btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
            }
        });

        //保存按钮
        add_bill_btn_save = (Button) view.findViewById(R.id.add_bill_btn_save);
        add_bill_btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBill();
            }
        });

        //选择驾驶员姓名
        driverSpinner = (ClickControlledSpinner) view.findViewById(R.id.spinner_driver);
        driverSpinner.setOnClickMyListener(new ClickControlledSpinner.OnClickMyListener() {
            @Override
            public void onClick() {
                getDrivers();
            }
        });
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDriver = driverList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //选择押运员姓名
        supercargoSpinner = (ClickControlledSpinner) view.findViewById(R.id.spinner_supercargo);
        supercargoSpinner.setOnClickMyListener(new ClickControlledSpinner.OnClickMyListener() {
            @Override
            public void onClick() {
                getCargos();
            }
        });
        supercargoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCargo = srpercargoList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        driverList.clear();
                        ArrayList<String> drivers = new ArrayList();
                        if (driverJson.length() != 0) {

                            for (int i = 0; i < driverJson.length(); i++) {
                                try {
                                    JSONObject json = (JSONObject) driverJson.get(i);
                                    drivers.add(i, json.getString("name"));
                                    driverList.add(i, json.getString("certificateId"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            driverSpinner.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.simple_spinner_item, drivers));
                            Log.e("driverSpinner", "onclick");
                            driverSpinner.performClick();
                        }
                        break;
                    case 2:
//                        Toast.makeText(getActivity(),"请输入车牌号",Toast.LENGTH_SHORT).show();
                        add_bill_vehicle.requestFocus();
                        add_bill_vehicle.setError("请输入车牌号");
                        break;
                    case 3:
                        add_bill_vehicle.setText(msg.getData().getString("carCode"));
                        add_bill_vehicle.setError(null);
                        break;
                    case 4:
                        srpercargoList.clear();
                        ArrayList<String> cargos = new ArrayList<>();
                        if (cargoJson.length() != 0) {

                            for (int i = 0; i < cargoJson.length(); i++) {
                                try {
                                    JSONObject json = (JSONObject) cargoJson.get(i);
                                    cargos.add(i, json.getString("name"));
                                    srpercargoList.add(i, json.getString("certificateId"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            supercargoSpinner.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.simple_spinner_item, cargos));
                            Log.e("supercargoSpinner", "onclick");
                            supercargoSpinner.performClick();
                        }
                        break;
                    case 5:
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void getDrivers() {
        Map<String, String> params = new HashMap<>();
        String plateNumber = add_bill_vehicle.getText().toString();
        Log.e("PlateNumber:", plateNumber);
        if (plateNumber == null || plateNumber.equals("")) {
            mHandle.sendEmptyMessage(2);
        } else {
            params.put("plateNumber", plateNumber);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            String url = getResources().getString(R.string.url) + "getDriversByPlateNumber.json";
            NormalPostRequest request = new NormalPostRequest(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("getDrivers", response.toString());
                            try {
                                if (response.get("errno").equals("0")) {
                                    driverJson = (JSONArray) response.get("data");
                                    mHandle.sendEmptyMessage(1);
                                } else {
                                    response.get("message");
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            Log.e("addbilldriver", volleyError.toString());
                        }
                    }, params);

            requestQueue.add(request);
        }
    }

    private void getCargos() {
        Map<String, String> params = new HashMap<>();
        String plateNumber = add_bill_vehicle.getText().toString();
        Log.e("PlateNumber:", plateNumber);
        if (plateNumber == null || plateNumber.equals("")) {
            mHandle.sendEmptyMessage(2);
        } else {
            params.put("plateNumber", plateNumber);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            //调用接口解码二维码信息，该接口数据需以GET方式发送
            String url = getResources().getString(R.string.url) + "getSupercargosByPlateNumber.json";
            NormalPostRequest request = new NormalPostRequest(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("getDrivers", response.toString());
                            try {
                                if (response.get("errno").equals("0")) {
                                    cargoJson = (JSONArray) response.get("data");
                                    mHandle.sendEmptyMessage(4);
                                } else {
                                    response.get("message");
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            Log.e("addbillcargo", volleyError.toString());
                        }
                    }, params);

            requestQueue.add(request);
        }
    }

    private void saveBill() {

        if (checkInput()) {
            String vehicle = add_bill_vehicle.getText().toString();
            String company = add_bill_company.getText().toString();
            String weight = add_bill_weight.getText().toString();
            Map<String, String> params = new HashMap<>();
            params.put("vehicleFlapper", vehicle);
            params.put("driverCertificate",mDriver);
            params.put("supercargoCertificate",mCargo);
            String account = preferences.getString("account","");
            Log.e("add",account+"__________________________________");
            params.put("account",account);
            params.put("placeCompanyName",company);
            params.put("placeProvince",mCurrentProviceName);
            params.put("placeDestination",mCurrentCityName);
            params.put("placepowiat",mCurrentDistrictName);
            params.put("actual",weight);

            Log.e("SAVEPARAM","vehicleFlapper:"+vehicle+" driverCertificate:"+mDriver+" supercargoCertificate"+mCargo);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

            String url = getResources().getString(R.string.url) + "saveEwayBillBase.json";
            NormalPostRequest request = new NormalPostRequest(url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("saveBill", response.toString());
                            try {
                                if (response.get("errno").equals("0")) {
                                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                                    HomeActivity activity = (HomeActivity) getActivity();
                                    FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fl_content,new BillListFragment());
                                    transaction.commit();
                                    Log.e("saveBill","done");
                                } else {
                                    response.get("message");
                                    Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                            Log.e("savebill", volleyError.toString());
                        }
                    }, params);

            requestQueue.add(request);

        }
    }

    private boolean checkInput() {
        String vehicle = add_bill_vehicle.getText().toString();
        String company = add_bill_company.getText().toString();
        String weight = add_bill_weight.getText().toString();
        String destination = add_bill_destination.getText().toString();
        if (vehicle.isEmpty()) {
            add_bill_vehicle.requestFocus();
            add_bill_vehicle.setError("请输入车牌号");
        } else if (mDriver == null || mDriver.isEmpty()) {
            Toast.makeText(getActivity(), "请选择驾驶员", Toast.LENGTH_SHORT).show();
        } else if (mCargo == null || mCargo.isEmpty()) {
            Toast.makeText(getActivity(), "请选择押运员", Toast.LENGTH_SHORT).show();
        } else if (destination.equals("点此选择目的地")) {
            Toast.makeText(getActivity(), "请选择目的地", Toast.LENGTH_SHORT).show();
        } else if (weight.isEmpty()) {
            add_bill_weight.requestFocus();
            add_bill_weight.setError("请输入实载量");
        } else if (company.isEmpty()) {
            add_bill_company.requestFocus();
            add_bill_company.setError("请输入收货方");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Log.e("scanResult:", scanResult);
            decode(scanResult);
        }
    }

    private void decode(String from) {
        Map<String, String> params = new HashMap<>();
        String sign = GenerateSign.generateSign("key=zzfht&qrcodeConent=" + from);
//        params.put("sign", sign);
        Log.e("sign:", sign);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //调用接口解码二维码信息，该接口数据需以GET方式发送
        String url = getResources().getString(R.string.qrcodeUrl) + "Service/WeiYun/DecodeQrcode?key=zzfht&qrcodeConent=" + from + "&sign=" + sign;
        NormalPostRequest request = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("qrcodeDecode", response.toString());
                        try {
                            if (response.get("statusCode").equals("200")) {
                                JSONObject data = (JSONObject) response.get("data");
                                Message msg = new Message();
                                msg.what = 3;
                                Bundle bundle = new Bundle();
                                bundle.putString("carCode", data.getString("carCode"));
                                msg.setData(bundle);
                                mHandle.sendMessage(msg);
                            } else {
                                response.get("message");
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MainActivity.this,response.get("msg").toString(),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                        Log.e("checkConsignor", volleyError.toString());
                    }
                }, params);

        requestQueue.add(request);
    }


}
