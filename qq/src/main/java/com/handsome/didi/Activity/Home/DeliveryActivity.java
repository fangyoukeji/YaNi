package com.handsome.didi.Activity.Home;

import android.Manifest;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handsome.didi.Base.BaseActivity;
import com.handsome.didi.Bean.Delivery;
import com.handsome.didi.Controller.DeliveryController;
import com.handsome.didi.R;

import java.util.List;

public class DeliveryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private DeliveryController deliveryController;

    private EditText et_no;
    private TextView tv_query;
    private ListView lv_delivery;
    private Spinner sp_delivery;

    //快递信息
    private List<Delivery.ResultBean.ListBean> listBean;

    private String[] company = {"EMS", "圆通", "顺丰", "天天", "申通", "中通", "韵达"};
    private String[] companyNum = {"ems", "yt", "sf", "tt", "sto", "zto", "yd"};
    private String com = companyNum[0];
    private String no;

    @Override
    public int getLayoutId() {
        return R.layout.activity_delivery;
    }

    @Override
    public void initViews() {
        et_no = findView(R.id.et_no);
        tv_query = findView(R.id.tv_query);
        lv_delivery = findView(R.id.lv_delivery);
        sp_delivery = findView(R.id.sp_delivery);
        //初始化权限
        requestPermissions(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void initListener() {
        setOnClick(tv_query);
        sp_delivery.setOnItemSelectedListener(this);
    }

    @Override
    public void initData() {
        setTitle("物流查询");
        setTitleCanBack();

        deliveryController = DeliveryController.getInstance();

        //初始化选项
        initSpring();
    }

    private void initSpring() {
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, company);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_delivery.setAdapter(adapter);
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.tv_query:
                no = et_no.getText().toString().trim();
                queryDeliveryAndInitData("zto", "432650181160");
                break;
        }
    }

    /**
     * 查询快递和初始化快递数据
     *
     * @param com
     * @param no
     */
    private void queryDeliveryAndInitData(String com, String no) {
        showToast("功能未开启");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        com = companyNum[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
