package com.zzfht.shippertools.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zzfht.shippertools.R;
import com.zzfht.shippertools.fragment.BillListFragment;
import com.zzfht.shippertools.fragment.MineFragment;

import static android.widget.RadioGroup.OnCheckedChangeListener;

public class HomeActivity extends Activity implements OnCheckedChangeListener {

    private RadioGroup rg_tab;
    private Button main_btn_msg;

    private FragmentManager fragmentManager ;
    private FragmentTransaction transaction ;

    private BillListFragment billListFragment;
    private MineFragment mineFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();


        initView();

        initFragment();
    }

    private void initView(){

        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        rg_tab.setOnCheckedChangeListener(this);

    }

    public void initFragment(){
        if(billListFragment==null){
            billListFragment = new BillListFragment();
        }
        transaction.add(R.id.fl_content,billListFragment);
        transaction.commit();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
//                Toast.makeText(this,"首页",Toast.LENGTH_SHORT).show();
                if(billListFragment == null){
                    billListFragment = new BillListFragment();
                }
                if(!billListFragment.isAdded()){
                    transaction=fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_content,billListFragment);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
            case R.id.rb_meassage:
                break;
            case R.id.rb_mine:
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                }
                if(!mineFragment.isAdded()){
                    transaction=fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_content,mineFragment);
//                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
        }
    }
}
