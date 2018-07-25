package com.vigorchip.treadmill.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.module.HistoryList;

import java.util.List;

/**
 * 跑步记录
 */
public class HistoryAdapter extends BaseAdapter {
    Context mContext;
    List<HistoryList> mList;

    public HistoryAdapter(Context context, List<HistoryList> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public HistoryList getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pro, View view, ViewGroup viewGroup) {
        HistoryViewHandle handle;
        if (view == null) {
            view = View.inflate(mContext, R.layout.adapter_history_item, null);
            handle = new HistoryViewHandle(view);
            view.setTag(handle);
        }
        handle = (HistoryViewHandle) view.getTag();// long currentTime = System.currentTimeMillis(); SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒"); SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); Date date = new Date(mList.get(i).getStartTime()); System.out.println(formatter.format(date)); handle.history_tv_data.setText(tter.format(date));
        try {
            handle.history_tv_data.setText(mList.get(pro).getStartTime().substring(0, 4) + "." + mList.get(pro).getStartTime().substring(4, 6) + "." + mList.get(pro).getStartTime().substring(6));
        } catch (Exception e) {
        }
        int timeShows = mList.get(pro).getTotletime();
        StringBuffer strTimes = new StringBuffer();
        strTimes.append(timeShows / 3600 > 0 ? timeShows / 3600 + ":" : "00:");//时
        strTimes.append((timeShows % 3600 / 60 > 9 ? timeShows % 3600 / 60 : "0" + timeShows % 3600 / 60) + ":");//分
        strTimes.append(timeShows % 3600 % 60 > 9 ? timeShows % 3600 % 60 : "0" + timeShows % 3600 % 60);//秒
        handle.history_tv_time.setText(strTimes.toString()//+ mContext.getResources().getString(R.string.min)
        );
        handle.history_tv_pace.setText(mList.get(pro).getPace()//+ mContext.getResources().getString(R.string.seconds)
        );
        handle.history_tv_distance.setText(mList.get(pro).getDistance() //+ mContext.getResources().getString(R.string.km)
        );
        handle.history_tv_kaluli.setText(mList.get(pro).getKaluli() //+ mContext.getResources().getString(R.string.kilocalorie)
        );
        handle.history_tv_heart.setText(mList.get(pro).getXinlv() //+ mContext.getResources().getString(R.string.bpm)
        );
        if (pro == mList.size() - 1) {
            handle.line_history.setVisibility(View.GONE);
        }
        return view;
    }

    private class HistoryViewHandle {
        TextView history_tv_data, history_tv_time, history_tv_pace, history_tv_distance, history_tv_kaluli, history_tv_heart;
        View line_history;
//        LinearLayout history_ll_list;

        public HistoryViewHandle(View view) {
//            history_ll_list = view.findViewById(R.id.history_ll_list);
            history_tv_data = view.findViewById(R.id.history_tv_data);
            history_tv_time = view.findViewById(R.id.history_tv_time);
            history_tv_pace = view.findViewById(R.id.history_tv_pace);
            history_tv_distance = view.findViewById(R.id.history_tv_distance);
            history_tv_kaluli = view.findViewById(R.id.history_tv_kaluli);
            history_tv_heart = view.findViewById(R.id.history_tv_heart);
            line_history = view.findViewById(R.id.line_history);
        }
    }
}