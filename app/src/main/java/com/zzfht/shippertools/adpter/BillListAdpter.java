package com.zzfht.shippertools.adpter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zzfht.shippertools.R;
import com.zzfht.shippertools.activity.BillInfoActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by think on 2015-11-17.
 */
public class BillListAdpter extends ArrayAdapter {

    private List<Map<String,Object>> list ;
    private LayoutInflater inflater;

    public BillListAdpter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.list = objects;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = null;

        if(view == null){

            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_bill_item,null);
            holder.plateNumber = (TextView) view.findViewById(R.id.emgbill_platenumber);
            holder.whereTo = (TextView) view.findViewById(R.id.emgbill_whereto);
            holder.medium = (TextView) view.findViewById(R.id.emgbill_medium);
            holder.date = (TextView) view.findViewById(R.id.emgbill_date);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.plateNumber.setText((String)list.get(position).get("vehicleFlapper"));
        holder.whereTo.setText(list.get(position).get("placeDestination") + "" + list.get(position).get("placepowiat"));
        holder.medium.setText((String)list.get(position).get("oneDangerousGoodsName"));
        holder.date.setText((String)list.get(position).get("dispatchDate"));

        return view;
    }



    public class ViewHolder{
        public TextView plateNumber;
        public TextView whereTo;
        public TextView medium;
        public TextView date;
    }

}
