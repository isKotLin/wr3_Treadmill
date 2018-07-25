package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.adapter.CustomAdapter;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.impl.SilentToCustom;
import com.vigorchip.treadmill.view.TwoLineCharView;

/**
 * 自定义模式
 */
public class CustomModeFragment extends BaseFragment implements SilentToCustom, View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnTouchListener {
    View view;
    GridView custom_gv_mode;
    CustomAdapter customAdapter;
    private static int currentss;

    public static int getTimess() {
        return timess;
    }

    private static int timess;

    public static int getCurrentss() {
        return currentss;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_custom, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        custom_gv_mode = view.findViewById(R.id.custom_gv_mode);
        customAdapter = new CustomAdapter(getContext());
        custom_gv_mode.setAdapter(customAdapter);
        Silently.setSilentToCustiom(this);
        custom_gv_mode.setOnItemClickListener(this);
        custom_gv_mode.setOnItemLongClickListener(this);
    }

    @Override
    public void setDataUpdate() {
        ((MainActivity) getActivity()).setCurrentIndex(1);
        customAdapter.notifyDataSetChanged();
    }

    public static void dismisses() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_running) {
            dismisses();
            ((MainActivity) getActivity()).startRun();
        }
//        click(view);
    }

    private void click(View view) {
        switch (view.getId()) {
            case R.id.start_time_plus:
                if (timess < 99)
                    timess++;
                start_time.setText(timess + "");
                break;
            case R.id.start_time_subtract:
                if (timess > 5)
                    timess--;
                start_time.setText(timess + "");
                break;
        }
    }

    private boolean isChang;
    long lastTime;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isChang = false;
                lastTime = System.currentTimeMillis();
                click(view);
                break;//TODO 长按
            case MotionEvent.ACTION_MOVE:
                if (System.currentTimeMillis() - lastTime > 1000)
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

    private static AlertDialog dialog;
    TextView start_time;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
        if (dialog != null && dialog.isShowing())
            return;
        Silently.setCustomType(index + 1);
        if (Silently.CUSTOM_TABLE == null || Silently.CUSTOM_TABLE.size() == index) {
            ((MainActivity) getActivity()).setCurrentIndex(9);
        } else {
            currentss = index;
            dialog = new AlertDialog.Builder(getContext(), R.style.NoTitle).create();
            dialog.show();
            dialog.getWindow().setContentView(R.layout.dialog_time_start);
            timess = 15;
            TextView start_tv_mode = (TextView) dialog.findViewById(R.id.start_tv_mode);
            //TextView start_tv_state = (TextView) dialog.findViewById(R.id.start_tv_state);
            start_time = (TextView) dialog.findViewById(R.id.start_time);
            TextView start_running = (TextView) dialog.findViewById(R.id.start_running);
            ImageView start_time_plus = (ImageView) dialog.findViewById(R.id.start_time_plus);
            ImageView start_time_subtract = (ImageView) dialog.findViewById(R.id.start_time_subtract);
            TextView tv_slope = (TextView) dialog.findViewById(R.id.tv_slope);
            if (MyApplication.getInstance().SLOPES == 0)
                tv_slope.setVisibility(View.GONE);
            TwoLineCharView start_tcv_state = (TwoLineCharView) dialog.findViewById(R.id.start_tcv_state);
            start_tcv_state.updata(Silently.CUSTOM_TABLE.get(index)[0], Silently.CUSTOM_TABLE.get(index)[1]);
            start_time.setText(timess + "");
            start_running.setOnClickListener(this);
            start_time_plus.setOnClickListener(this);
            start_time_subtract.setOnClickListener(this);
            start_time_plus.setOnTouchListener(this);
            start_time_subtract.setOnTouchListener(this);
            start_tv_mode.setText(Silently.CUSTOM_NAME.get(index));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (dialog != null && dialog.isShowing())
            return false;
        if (i == Silently.CUSTOM_NAME.size())
            return false;
        Silently.setCustomType(i + 1);
        dialog = new AlertDialog.Builder(getContext()).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_edit_delete);
        dialog.findViewById(R.id.dialog_btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Silently.isTiHuan = true;
                ((MainActivity) getActivity()).setCurrentIndex(9);
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.dialog_btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Silently.setIsDelete(true);
                Silently.setCustom("", new int[16], new int[16], "");
                dialog.dismiss();
            }
        });
        return true;
    }
}
