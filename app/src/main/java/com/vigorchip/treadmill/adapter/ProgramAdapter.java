package com.vigorchip.treadmill.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.module.SaveFixedDateUtils;
import com.vigorchip.treadmill.view.LineCharView;

/**
 * 程序模式适配器
 */
public class ProgramAdapter extends BaseAdapter {
    Context mContext;

    public ProgramAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return SaveFixedDateUtils.PROGRAM_TABLE == null ? 0 : SaveFixedDateUtils.PROGRAM_TABLE.size();
    }

    @Override
    public Object getItem(int position) {
        return SaveFixedDateUtils.PROGRAM_TABLE.get(position)[0];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ProgramViewHandle handle;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.adapter_program_item, null);
            handle = new ProgramViewHandle(convertView);
            convertView.setTag(handle);
        } else
            handle = (ProgramViewHandle) convertView.getTag();
        handle.program_tv_name.setText(SaveFixedDateUtils.PROGRAM_NAME[position]);
        handle.program_rl_bj.setBackgroundDrawable(ContextCompat.getDrawable(mContext, SaveFixedDateUtils.PROGRAM_BJ[position]));
        handle.program_lcv_show.updata(SaveFixedDateUtils.PROGRAM_TABLE.get(position)[0]);
        handle.program_rl_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProgramListeners.ProgramListener(position);
            }
        });
        return convertView;
    }

    private OnProgramListener onProgramListeners;

    public void setOnProgramListener(OnProgramListener onProgramListener) {
        onProgramListeners = onProgramListener;
    }

    public interface OnProgramListener {
        void ProgramListener(int position);
    }

    private class ProgramViewHandle {
        LineCharView program_lcv_show;
        TextView program_tv_name;
        RelativeLayout program_rl_bj;

        public ProgramViewHandle(View convertView) {
            program_lcv_show = convertView.findViewById(R.id.program_lcv_show);
            program_tv_name = convertView.findViewById(R.id.program_tv_name);
            program_rl_bj = convertView.findViewById(R.id.program_rl_bj);
        }
    }
}
