package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.okttp.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 注册
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText register_et_name, register_et_password, re_register_et_password;
    private Button register_btn;

    private void enAble() {
        register_et_name.setFocusableInTouchMode(false);
        register_et_password.setFocusableInTouchMode(false);
        re_register_et_password.setFocusableInTouchMode(false);
        register_btn.setSelected(true);
        register_btn.setClickable(false);
        register_btn.setText(R.string.load);
    }

    private void able() {
        register_et_name.setFocusableInTouchMode(true);
        register_et_password.setFocusableInTouchMode(true);
        re_register_et_password.setFocusableInTouchMode(true);
        register_btn.setSelected(false);
        register_btn.setClickable(true);
        register_btn.setText(R.string.register);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SportData.setStatus(SportData.AppRegist);
        register_et_name = view.findViewById(R.id.register_et_name);
        register_et_password = view.findViewById(R.id.register_et_password);
        re_register_et_password = view.findViewById(R.id.re_register_et_password);
        register_btn = view.findViewById(R.id.register_btn);
        able();
        register_btn.setOnClickListener(this);
    }

    Runnable registerRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", register_et_name.getText().toString());
            map.put("password", register_et_password.getText().toString());
            Okhttp.get(false, UrlConstant.REGISTER, map, new Okhttp.CallBac() {
                @Override
                public void onError(Call call, Exception e, String state, int id) {
                    showToast(getString(R.string.kdj));
                    able();
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject login = new JSONObject(response);
                        boolean success = login.optBoolean("success");
                        if (success) {
                            JSONObject data = login.optJSONObject("data");
                            User.setUserId(data.optString("user_id"));
                            ((MainActivity) getActivity()).setCurrentIndex(10);
                            ((MainActivity) getActivity()).setIsRegister(true);
                            ((MainActivity) getActivity()).setMode(getString(R.string.pre_info));
                        } else {
                            showToast(getString(R.string.fail));
                        }
                        able();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNoNetwork(String state) {
                    showToast(getString(R.string.no_wifi));
                    able();
                }
            });
        }
    };

    @Override
    public void onClick(View view) {
        if (!TextUtils.isEmpty(register_et_name.getText().toString()) && !TextUtils.isEmpty(register_et_password.getText().toString()) && !TextUtils.isEmpty(re_register_et_password.getText().toString())) {
            if (register_et_password.getText().toString().equals(re_register_et_password.getText().toString())) {
                ((MainActivity) getActivity()).handler.post(registerRunnable);
                enAble();
            } else {
                showToast(getString(R.string.mima));
            }
        } else {
            showToast(getString(R.string.no_kong));
        }
    }
}
