package com.zzfht.shippertools.fragment;

import android.app.Fragment;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.zzfht.shippertools.R;
import com.zzfht.shippertools.activity.BillInfoActivity;
import com.zzfht.shippertools.adpter.BillListAdpter;
import com.zzfht.shippertools.entity.ShipperBillInfo;
import com.zzfht.shippertools.util.JsonUtil;
import com.zzfht.shippertools.util.NormalPostRequest;
import com.zzfht.shippertools.util.TitleBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import medusa.theone.waterdroplistview.view.WaterDropListView;


public class BillListFragment extends Fragment implements WaterDropListView.IWaterDropListViewListener, AdapterView.OnItemClickListener {

    private View view;
    private AddBillFragment fragment;
    private WaterDropListView bill_list_wl;
    private Handler handler;
    private SharedPreferences preference;
    private BillListAdpter adpter;
    private static int page = 1;
    private List<Map<String, String>> bill_list = new ArrayList<>();

    public BillListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bill_list, container, false);
        initView(container);
        return view;
    }

    private void initView(final ViewGroup container) {

        Log.e("billListFragment", "initView");

        preference = getActivity().getApplicationContext().getSharedPreferences(getActivity().getResources().getString(R.string.package_name), Context.MODE_PRIVATE);

        //设置标题栏
        new TitleBuilder(view).setTitleText("托运单").setRightText("新增").setRightOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),"新增",Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
                if (fragment == null) {
                    fragment = new AddBillFragment();
                }
                transaction.replace(R.id.fl_content, fragment);
//                transaction.addToBackStack(null);
                transaction.commit();
                RadioGroup radioGroup = (RadioGroup) getActivity().findViewById(R.id.rg_tab);
                radioGroup.clearCheck();
            }
        });

        getListData();
        adpter = new BillListAdpter(getActivity(), R.layout.list_bill_item, bill_list);
        bill_list_wl = (WaterDropListView) view.findViewById(R.id.bill_list_wl);
        bill_list_wl.setAdapter(adpter);
        bill_list_wl.setWaterDropListViewListener(this);
        bill_list_wl.setOnItemClickListener(this);
        bill_list_wl.setPullLoadEnable(true);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case 3:
                        adpter.notifyDataSetChanged();
                        break;
                    case 1:
                        bill_list_wl.stopRefresh();
                        break;
                    case 2:
                        bill_list_wl.stopLoadMore();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onRefresh() {
        page = 1;
        getListData();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        getListData();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getListData() {
        List<ShipperBillInfo> list = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        String companyId = preference.getString("companyId","");
//        params.put("companyId", "company20151223154433851562713180127566");
        params.put("companyId", companyId);
        params.put("page", String.valueOf(page));

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //调用接口解码二维码信息，该接口数据需以GET方式发送
        String url = getResources().getString(R.string.url) + "getEwaybilllInfoByCompanyId.json";
        NormalPostRequest request = new NormalPostRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("getBillList", response.toString());
                        try {
                            if (response.get("data") != null) {

                                List<Map<String, String>> list = JsonUtil.getList(response.get("data").toString());
                                if (page == 1) {
                                    bill_list.clear();
                                }
                                bill_list = addList(list, bill_list);
                                Log.e("callback", bill_list.toString());
//                                handler.sendEmptyMessage(1);
                                adpter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "当前网络不可用", Toast.LENGTH_SHORT).show();
                        Log.e("getBillList", volleyError.toString());
                    }
                }, params);

        requestQueue.add(request);
    }

    private List addList(List list, List listToAdd) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (!listToAdd.contains(list.get(i))) {
                    listToAdd.add(list.get(i));
                }
            }
        }
        return listToAdd;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), BillInfoActivity.class);
        i.putExtra("id", bill_list.get(position-1).get("id"));
        getActivity().startActivity(i);
    }
}
