package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.utils.HeartDate;

import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;
import wheel.OnItemSelectedListener;

/**
 * 心率模式
 */
public class HRCFragment extends BaseFragment implements View.OnClickListener {
    View view;
    private static LoopView age_ps, heart_ps, time_ps;
    TextView heart_tv_start;
    private static List<String> listAge, listTimes;

    public static String getHeartSet() {
        return HeartDate.getArray(heart_ps.getSelectedItem()).get(heart_ps.getSelectedItem());
    }

    public static String getTimeSet() {
        return listTimes.get(time_ps.getSelectedItem());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hrc, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        heart_tv_start = view.findViewById(R.id.heart_tv_start);
        heart_tv_start.setOnClickListener(this);
        initAge();
        initTime();
    }

    /**
     * 年龄,心率
     */
    private void initAge() {
        age_ps = view.findViewById(R.id.age_ps);
        heart_ps = view.findViewById(R.id.heart_ps);
        listAge = new ArrayList<>();
        for (int i = 15; i <= 80; i++) {
            listAge.add(i + "");
        }
        age_ps.setInitPosition(3);
        age_ps.setItems(listAge);
        age_ps.setTextSizes(45);
        heart_ps.setItems(HeartDate.getArray(3));
        heart_ps.setInitPosition(HeartDate.getIndex(0));
        heart_ps.setTextSizes(45);
//        heart_ps.setNotLoop();
        age_ps.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
//                heart_ps.setNotLoop();
                heart_ps.setItems(HeartDate.getArray(age_ps.getSelectedItem()));
                heart_ps.setInitPosition(HeartDate.getIndex(age_ps.getSelectedItem()));
            }
        });
    }

    /**
     * 时间
     */
    private void initTime() {
        time_ps = view.findViewById(R.id.time_ps);
        listTimes = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            listTimes.add(i < 10 ? "0" + i : i + "");
        }
        time_ps.setInitPosition(0);
        time_ps.setItems(listTimes);
        time_ps.setTextSizes(45);
    }

    private static AlertDialog dialog;

    public static void dismis() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (dialog != null && dialog.isShowing())
            return;
        dialog = new AlertDialog.Builder(getContext(), R.style.NoTitle).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_no_time_start);
//        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//        lp.width = DensityUtils.px2dip(getContext(), 602);//定义宽度
//        lp.height = DensityUtils.px2dip(getContext(), 360);//定义高度
//        dialog.getWindow().setAttributes(lp);
        RelativeLayout start_rl_run = (RelativeLayout) dialog.findViewById(R.id.start_rl_run);
        TextView start_tv_model = (TextView) dialog.findViewById(R.id.start_tv_model);
        TextView start_tv_statement = (TextView) dialog.findViewById(R.id.start_tv_statement);
        start_tv_model.setText(R.string.heart_rate_mode);
        start_tv_statement.setText(R.string.heart_state);
        start_rl_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    ((MainActivity) getActivity()).startRun();
                    dialog.dismiss();
                }
            }
        });
    }
}
