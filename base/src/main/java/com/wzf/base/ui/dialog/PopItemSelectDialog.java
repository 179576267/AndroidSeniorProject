package com.wzf.base.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.demo.base.ui.adapter.RcyCommonAdapter;
import com.demo.base.ui.adapter.RcyViewHolder;
import com.demo.base.ui.base.BaseDialog;
import com.wzf.base.R;
import com.wzf.base.ui.utils.FullyLinearLayoutManager;

import java.util.List;

/**
 * Created by zhenfei.wang on 2016/7/15.
 * 视频的选择器，照片的选择器
 */
public abstract   class PopItemSelectDialog extends BaseDialog {
    private Context mContext;
    private List<PopItem> items;

    public PopItemSelectDialog(Context context, List<PopItem> list) {
        super(context);
        mContext = context;
        items = list;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setCanceledOnTouchOutside(true);
        initRcy(findViewById(R.id.rv));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_pop_select;
    }

    @Override
    protected float getWithRate() {
        return 1f;
    }


    @Override
    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected int getAnimationId() {
        return R.style.AnimBottom;
    }

    private void initRcy(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(mContext, 0));
        recyclerView.setAdapter(new RcyCommonAdapter<PopItem>(mContext, items, false, recyclerView) {
            @Override
            public void convert(RcyViewHolder holder, PopItem popItem) {
                TextView tv = holder.getView(R.id.tv);
                View underline = holder.getView(R.id.underline);
                if(mDatas.indexOf(popItem) == mDatas.size() - 1){
                    underline.setVisibility(View.GONE);
                }else {
                    underline.setVisibility(View.VISIBLE);
                }
                tv.setText(popItem.text);
            }

            @Override
            public int getLayoutId(int position) {
                return R.layout.dialog_gender_select;
            }

            @Override
            public void onItemClickListener(int position) {
                onItemClick(mDatas.get(position).id);
                dismiss();
            }
        });
    }

    public abstract void onItemClick(int id);

    public static class PopItem{
        public String text;
        public int id;
        public int color;

        public PopItem(String text, int id, int color) {
            this.text = text;
            this.id = id;
            this.color = (color == 0 ? 0xFF000000 : color);
        }
    }
}
