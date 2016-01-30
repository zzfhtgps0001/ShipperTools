package com.zzfht.shippertools.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zzfht.shippertools.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillInfoSafeFragment extends Fragment {

    public BillInfoSafeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bill_info_safe, container, false);
    }


}
