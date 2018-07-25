package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.SportData;

/**
 * 启动页
 */
public class StartupFragment extends BaseFragment implements View.OnClickListener {
    View view;
    Button startup_btn_login, startup_btn_register, startup_btn_visitant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_startup, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SportData.setStatus(SportData.App);
        startup_btn_login = view.findViewById(R.id.startup_btn_login);
        startup_btn_register = view.findViewById(R.id.startup_btn_register);
        startup_btn_visitant = view.findViewById(R.id.startup_btn_visitant);
        startup_btn_login.setOnClickListener(this);
        startup_btn_register.setOnClickListener(this);
        startup_btn_visitant.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startup_btn_login:
                ((MainActivity)getActivity()).setCurrentIndex(11);
                ((MainActivity)getActivity()).setMode(getString(R.string.login));
                break;
            case R.id.startup_btn_register:
                ((MainActivity)getActivity()).setCurrentIndex(13);
                ((MainActivity)getActivity()).setMode(getString(R.string.register));
                break;
            case R.id.startup_btn_visitant:
//                ((MainActivity)getActivity()).activity_vp_main.setCurrentItem(0);
//                ((MainActivity) getActivity()).showHomePager();
                MyApplication.getInstance().setDianji(false);
                ((MainActivity) getActivity()).cuss();
                SportHomePageFragment.update();
                break;
        }
    }
}
