package com.vigorchip.treadmill.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.adapter.ProgramAdapter;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.SaveFixedDateUtils;
import com.vigorchip.treadmill.view.TwoLineCharView;

/**
 * 程序模式
 */
public class ProgramModeFragment extends BaseFragment implements ProgramAdapter.OnProgramListener, View.OnClickListener, View.OnTouchListener {
    View view;
    GridView program_gv_mode;

    public static int getIndexs() {
        return indexs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_program, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        program_gv_mode = view.findViewById(R.id.program_gv_mode);
        ProgramAdapter programAdapter = new ProgramAdapter(getContext());
        program_gv_mode.setAdapter(programAdapter);
        programAdapter.setOnProgramListener(this);
    }

    @Override
    public void ProgramListener(int position) {
        programDialog(getContext(), position);
    }

    TextView start_time; // ,start_tv_mode, start_tv_state,  start_running; ImageView start_time_plus, start_time_subtract;

    public static int getTime() {
        return time;
    }

    private static int time;
    private static int indexs;
    private static AlertDialog dialog;

    public void programDialog(Context context, int position) {
        if (dialog != null && dialog.isShowing())
            return;
        indexs = position;
        dialog = new AlertDialog.Builder(context, R.style.NoTitle).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_time_start);
        time = 15;
        TextView start_tv_mode = (TextView) dialog.findViewById(R.id.start_tv_mode);
        TextView start_tv_state = (TextView) dialog.findViewById(R.id.start_tv_state);
        TextView tv_slope = (TextView) dialog.findViewById(R.id.tv_slope);
        if (MyApplication.getInstance().SLOPES == 0)
            tv_slope.setVisibility(View.GONE);
        TwoLineCharView start_tcv_state = (TwoLineCharView) dialog.findViewById(R.id.start_tcv_state);
        start_tcv_state.updata(SaveFixedDateUtils.PROGRAM_TABLE.get(position)[0], SaveFixedDateUtils.PROGRAM_TABLE.get(position)[1]);
        start_time = (TextView) dialog.findViewById(R.id.start_time);
        TextView start_running = (TextView) dialog.findViewById(R.id.start_running);
        ImageView start_time_plus = (ImageView) dialog.findViewById(R.id.start_time_plus);
        ImageView start_time_subtract = (ImageView) dialog.findViewById(R.id.start_time_subtract);
        start_time.setText(time + "");
        start_tv_mode.setText(SaveFixedDateUtils.PROGRAM_NAME[position]);
        start_tv_state.setText(SaveFixedDateUtils.PROGRAM_STATE[position]);
        start_running.setOnClickListener(this);
        start_time_plus.setOnClickListener(this);
        start_time_subtract.setOnClickListener(this);
        start_time_plus.setOnTouchListener(this);
        start_time_subtract.setOnTouchListener(this);
    }

    public static void dis() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (R.id.start_running == view.getId()) {
            dis();
            ((MainActivity) getActivity()).startRun();
        }
    }

    private void click(View view) {
        switch (view.getId()) {
            case R.id.start_running:
                dis();
                ((MainActivity) getActivity()).startRun();
                break;
            case R.id.start_time_plus:
                if (time < 99)
                    time++;
                start_time.setText(time + "");
                break;
            case R.id.start_time_subtract:
                if (time > 5)
                    time--;
                start_time.setText(time + "");
                break;
        }
    }

    long lastTime;
    boolean isChang;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isChang = false;
                lastTime = System.currentTimeMillis();
                click(view);
                break;//TODO 长按
            case MotionEvent.ACTION_MOVE:
                if (System.currentTimeMillis() - lastTime>1000)
                    isChang=true;
                if (isChang && motionEvent.getY() > 0 && motionEvent.getY() <= view.getHeight() && motionEvent.getX() > 0 && motionEvent.getX() <= view.getWidth()) {
                    if (System.currentTimeMillis() - lastTime > 100) {
                        click(view);
                        lastTime = System.currentTimeMillis();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
}