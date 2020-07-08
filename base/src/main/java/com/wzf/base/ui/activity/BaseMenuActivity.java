package com.wzf.base.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.demo.base.ui.adapter.CommonAdapter;
import com.demo.base.ui.adapter.ViewHolder;
import com.demo.base.utils.ScreenUtils;
import com.wzf.base.R;
import com.wzf.base.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: wangzhenfei
 * @date: 2017-11-30 16:06
 */

public  class BaseMenuActivity extends BaseActivity {
    protected FrameLayout flContainer;
    protected Spinner spinner;
    private CommonAdapter<SpinnerItem> adapter;
    private List<SpinnerItem> itemList = new ArrayList<SpinnerItem>(){{
//        add(new SpinnerItem("请选择", null, null));
    }};


    @Override
    public int getLayoutId() {
        return R.layout.activity_base_menu;
    }

    @Override
    protected void initForSave(Bundle savedInstanceState) {
        flContainer = findViewById(R.id.fl_container);
        spinner = findViewById(R.id.spinner);
        if(getContainerLayoutResId() != 0){
            LayoutInflater.from(this).inflate(getContainerLayoutResId(), flContainer, true);
        }
        initSpinner();
    }

    private void initSpinner() {

        adapter = new CommonAdapter<SpinnerItem>(itemList, this, R.layout.item_spinner){
            @Override
            public void convert(ViewHolder holder, SpinnerItem item, int position) {
                super.convert(holder, item, position);
                TextView tv = holder.getView(R.id.text1);
                tv.setText(item.name);
                tv.setOnClickListener((v)->{
                    if(item.listener != null){
                        item.listener.onClick(v);
                    }else if(item.t != null){
                        startActivity(new Intent(BaseMenuActivity.this, item.t));
                    }
                    spinner.setSelection(position);
                });
            }
        };
        spinner.setAdapter(adapter);
    }

    public  int getContainerLayoutResId(){
        return 0;
    }



    protected void addChild(String text, final Class t) {
        itemList.add(new SpinnerItem(text, t, null));
        adapter.notifyDataSetChanged();
    }


    protected void addChild(String text, View.OnClickListener onClickListener) {
        itemList.add(new SpinnerItem(text, null, onClickListener));
        adapter.notifyDataSetChanged();

    }

    class SpinnerItem{
        String name;
        Class t;
        View.OnClickListener listener;

        public SpinnerItem(String name, Class t, View.OnClickListener listener) {
            this.name = name;
            this.t = t;
            this.listener = listener;
        }
    }
}
