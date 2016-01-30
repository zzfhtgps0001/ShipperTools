package com.zzfht.shippertools.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zzfht.shippertools.R;
import com.zzfht.shippertools.util.TitleBuilder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    private View view;
    private LinearLayout log_out;

    public MineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return view;
    }

    private void initView(){
        new TitleBuilder(view).setTitleText("个人中心").setRightImage(R.drawable.my_setting_unsel_2x);
        log_out = (LinearLayout) view.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDia();
            }
        });
    }

    private DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case Dialog.BUTTON_POSITIVE:

                    getActivity().finish();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };

    private void logoutDia(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确认", dialogOnclicListener);
        builder.setNegativeButton("取消", dialogOnclicListener);
        builder.create().show();
    }


}
