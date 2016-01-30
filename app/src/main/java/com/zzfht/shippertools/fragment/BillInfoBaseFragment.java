package com.zzfht.shippertools.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzfht.shippertools.R;

public class BillInfoBaseFragment extends Fragment {

    private View view;

    public BillInfoBaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bill_info_base, container, false);
        initView();
        return view;
    }

    private void initView() {

    }


}
