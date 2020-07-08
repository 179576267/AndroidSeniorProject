package com.wzf.base.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

/**
 */
@Route(path = DegradeServiceImpl.PATH)
public class DegradeServiceImpl implements DegradeService {

    static final String PATH = "/service/DegradeServiceImpl";

    @Override
    public void onLost(Context context, Postcard postcard) {
        if (context != null && postcard.getGroup().equals("activity")) {
//            Intent intent = new Intent(context, WebViewActivity.class);
//            intent.putExtra(Constant.URL, Constant.GITHUB);
//            intent.putExtra(Constant.TITLE, "github地址");
//            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    public void init(Context context) {

    }
}
