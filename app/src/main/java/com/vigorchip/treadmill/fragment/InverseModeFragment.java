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

import java.util.ArrayList;
import java.util.List;

import wheel.LoopView;

/**
 * 倒数模式
 */
public class InverseModeFragment extends BaseFragment implements View.OnClickListener {
    View view;
    private static LoopView inverse_time_mode, inverse_mileage_mode, inverse_heat_mode;
    private static List<String> listTime, listMileage, listHeat;
    TextView inverse_tv_start;
    public static String getTimeSet() {
        return listTime.get(inverse_time_mode.getSelectedItem());
    }

    public static String getMileageSet() {
        return listMileage.get(inverse_mileage_mode.getSelectedItem());
    }

    public static String getHeatSet() {
        return listHeat.get(inverse_heat_mode.getSelectedItem());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inverse, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        inverse_tv_start = view.findViewById(R.id.inverse_tv_start);
        inverse_tv_start.setOnClickListener(this);
        initTimeData();
        initMileageData();
        initHeatData();
    }

    /**
     * 时间倒数
     */
    private void initTimeData() {
        inverse_time_mode = view.findViewById(R.id.inverse_time_mode);
        listTime = new ArrayList<>();
        for (int i = 5; i < 100; i++) {
            listTime.add(i < 10 ? "0" + i : i + "");
        }
        inverse_time_mode.setInitPosition(0);
        inverse_time_mode.setItems(listTime);
        inverse_time_mode.setTextSizes(45);
    }

    /**
     * 路程倒数
     */
    private void initMileageData() {
        inverse_mileage_mode = view.findViewById(R.id.inverse_mileage_mode);
        listMileage = new ArrayList<>();
        for (int i = 1; i <100; i++) {
            listMileage.add(i < 10 ? "0" + i : i + "");
        }
        inverse_mileage_mode.setInitPosition(0);
        inverse_mileage_mode.setItems(listMileage);
        inverse_mileage_mode.setTextSizes(45);
    }

    /**
     * 卡路里倒数
     */
    private void initHeatData() {
        inverse_heat_mode = view.findViewById(R.id.inverse_heat_mode);
        listHeat = new ArrayList<>();
        for (int i = 10; i < 991; i +=  10) {
            listHeat.add(i < 100 ? "0" + i : i + "");
        }
        listHeat.add(999 + "");
        inverse_heat_mode.setInitPosition(0);
        inverse_heat_mode.setItems(listHeat);
        inverse_heat_mode.setTextSizes(45);
    }

    private static AlertDialog dialog;

    public static void dises() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
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
        start_tv_model.setText(R.string.inverted_mode);
        start_tv_statement.setText(R.string.inverse_state);
        start_rl_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null && dialog.isShowing()) {
                    ((MainActivity) getActivity()).startRun();
                    dialog.dismiss();
                }
            }
        });
    }
}
