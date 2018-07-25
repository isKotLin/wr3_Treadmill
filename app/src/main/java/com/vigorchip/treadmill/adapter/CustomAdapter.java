package com.vigorchip.treadmill.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.view.LineCharView;

/**
 * 自定义模式适配器
 */
public class CustomAdapter extends BaseAdapter {
    Context mContext;
    private int numss;
private int showNumber=7;//显示个数（需减一）
    public CustomAdapter(Context context) {mContext = context;    }
    @Override
    public int getCount() {
        if (Silently.CUSTOM_TABLE.size() > showNumber)
            numss = 0;
        else
            numss = 1;
        return Silently.CUSTOM_TABLE == null ? numss : Silently.CUSTOM_TABLE.size() + numss;
    }

    @Override
    public Object getItem(int position) {
        return Silently.CUSTOM_TABLE.get(position)[0];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CustomViewHandle handle;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_custom_item, null);
            handle = new CustomViewHandle(convertView);
            convertView.setTag(handle);
        } else
            handle = (CustomViewHandle) convertView.getTag();
        if (Silently.CUSTOM_TABLE.size()>showNumber) {
            handle.custom_tv_name.setText(Silently.CUSTOM_NAME.get(position));
            handle.custom_rl_bj.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.test0));
            handle.custom_lcv_show.updata(Silently.CUSTOM_TABLE.get(position)[0]);
            handle.custom_view.setBackgroundResource(R.color.program_bj);
        } else {
            if (position < Silently.CUSTOM_TABLE.size()) {
                handle.custom_tv_name.setText(Silently.CUSTOM_NAME.get(position));
                handle.custom_rl_bj.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.test0));
                handle.custom_lcv_show.updata(Silently.CUSTOM_TABLE.get(position)[0]);
                handle.custom_view.setBackgroundResource(R.color.program_bj);
            } else {
                handle.custom_rl_bj.setBackgroundResource(R.mipmap.clean_bj);
                handle.custom_iv_add.setImageResource(R.mipmap.custom_icon);
                handle.custom_tv_add.setText(R.string.add);
            }
        }
        return convertView;
    }

    private class CustomViewHandle {
        LineCharView custom_lcv_show;
        TextView custom_tv_name, custom_tv_add;
        RelativeLayout custom_rl_bj;
        View custom_view;
        ImageView custom_iv_add;

        public CustomViewHandle(View convertView) {
            custom_lcv_show = convertView.findViewById(R.id.custom_lcv_show);
            custom_tv_name = convertView.findViewById(R.id.custom_tv_name);
            custom_rl_bj = convertView.findViewById(R.id.custom_rl_bj);
            custom_view = convertView.findViewById(R.id.custom_view);
            custom_iv_add = convertView.findViewById(R.id.custom_iv_add);
            custom_tv_add = convertView.findViewById(R.id.custom_tv_add);
        }
    }
}
