package com.vigorchip.treadmill.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;

import java.util.List;

/**
 * 应用适配器
 */
public class AppAdapter extends BaseAdapter {
    List<ResolveInfo> mApps;
    Context mContext;
    int mPager;
    private OnItemListenerOnClick onItemListenerOnClick;

    public AppAdapter(Context context, List<ResolveInfo> apps, int pager) {
        mContext = context;
        mApps = apps;
        mPager = pager;
    }

    @Override
    public int getCount() {
        return mApps == null ? 0 : mApps.size();
    }

    @Override
    public ResolveInfo getItem(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppViewHandle handle;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_app_item, null);
            handle = new AppViewHandle(convertView);
            convertView.setTag(handle);
        } else
            handle = (AppViewHandle) convertView.getTag();
        ResolveInfo resolveInfo = mApps.get(position);
        if (resolveInfo.activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2")) {
            handle.apps_iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.music_icon));
            handle.apps_tv.setText(mContext.getString(R.string.local_music));
        } else if (resolveInfo.activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2")) {
            handle.apps_iv.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.viedo_icon));
            handle.apps_tv.setText(mContext.getString(R.string.local_video));
        } else {
            handle.apps_iv.setImageDrawable(resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
            handle.apps_tv.setText(resolveInfo.loadLabel(mContext.getPackageManager()));
        }
        if ((mPager * 8 + position) % 6 == 0) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter1));
        } else if ((mPager * 8 + position) % 6 == 1) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter2));
        } else if ((mPager * 8 + position) % 6 == 2) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter3));
        } else if ((mPager * 8 + position) % 6 == 3) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter4));
        } else if ((mPager * 8 + position) % 6 == 4) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter5));
        } else if ((mPager * 8 + position) % 6 == 5) {
            handle.app_ll_item.setBackgroundDrawable(ContextCompat.getDrawable(mContext,R.drawable.adapter6));

        }
        handle.app_ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemListenerOnClick.itemClick(position);
            }
        });
        return convertView;
    }

    public void setOnItemListenerOnClick(OnItemListenerOnClick onItemListenerOnClick) {
        this.onItemListenerOnClick = onItemListenerOnClick;
    }

    public interface OnItemListenerOnClick {
        void itemClick(int position);
    }

    private class AppViewHandle {
        public ImageView apps_iv;
        public TextView apps_tv;
        public LinearLayout app_ll_item;

        public AppViewHandle(View view) {
            apps_iv = view.findViewById(R.id.apps_iv_icon);
            apps_tv = view.findViewById(R.id.apps_tv_name);
            app_ll_item = view.findViewById(R.id.app_ll_item);
        }
    }
}