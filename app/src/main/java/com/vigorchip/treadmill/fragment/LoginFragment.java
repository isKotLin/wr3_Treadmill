package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.okttp.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 登录
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private EditText login_et_name, login_et_password;
    private ImageView login_iv_remember;
    private Button login_btn;
    private TextView re_tv_registration, forget_tv_password;
    private String userName = "abini", remember = "dsuji", password = "lkndgfd";
    private boolean remem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        SportData.setStatus(SportData.AppLogin);
        login_et_name = view.findViewById(R.id.login_et_name);
        login_et_password = view.findViewById(R.id.login_et_password);
        login_btn = view.findViewById(R.id.login_btn);
        login_iv_remember = view.findViewById(R.id.login_iv_remember);
        re_tv_registration = view.findViewById(R.id.re_tv_registration);
        forget_tv_password = view.findViewById(R.id.forget_tv_password);
        able();
        remem = MyApplication.getInstance().getSP().getBoolean(remember, false);
        login_et_name.setText(MyApplication.getInstance().getSP().getString(userName, ""));
        login_et_password.setText("");
        if (remem) {
            login_iv_remember.setSelected(true);
            login_et_password.setText(MyApplication.getInstance().getSP().getString(password, ""));
        }
        login_btn.setOnClickListener(this);
        re_tv_registration.setOnClickListener(this);
        forget_tv_password.setOnClickListener(this);
        login_iv_remember.setOnClickListener(this);
    }

    Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put("phone", login_et_name.getText().toString());
            map.put("password", login_et_password.getText().toString());
            Okhttp.get(false, UrlConstant.LOGIN, map, new Okhttp.CallBac() {
                @Override
                public void onError(Call call, Exception e, String state, int id) {
                    showToast(getString(R.string.login_error));
                    able();
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject login = new JSONObject(response);
                        boolean success = login.optBoolean("success");
                        if (success) {
                            JSONObject data = login.optJSONObject("data");//TODO 登录时完善信息
                            String user_id = data.optString("user_id");
                            User.setUserId(user_id);
                            if (TextUtils.isEmpty(data.optString("nickname"))) {
                                ((MainActivity) getActivity()).setMode(getString(R.string.pre_info));
                                ((MainActivity) getActivity()).setIsRegister(true);
                                ((MainActivity) getActivity()).setCurrentIndex(10);
                            } else { Silently.getCustom("info1", "info2", "info3", "info4", "info5", "info6", "info7", "info8", "info9", "info10");
                                User.setUserWeight(data.optInt("weight"));
                                User.setIll(data.optString("illness"));
                                User.setUserAge(data.optInt("age"));
                                User.setUserHeart(data.optInt("xinlv"));
                                User.setUserheight(data.optInt("tall"));
                                User.setUserName(data.optString("nickname"));
                                User.setUserPhone(data.optString("phone"));
                                User.setSex(data.optInt("sex"));
                                MyApplication.getInstance().setDianji(false);
                                MyApplication.getInstance().getSP().edit().putString(userName, data.optString("phone")).commit();
                                if (remem) {
                                    MyApplication.getInstance().getSP().edit().putString(password, passwordss).commit();
                                    MyApplication.getInstance().getSP().edit().putBoolean(remember, remem).commit();
                                }
                                ((MainActivity) getActivity()).cuss();
                                SportHomePageFragment.update();
                            }
                        } else {
                            showToast(getString(R.string.login_error));
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
    String passwordss;

    private void enable() {//不可用
        login_btn.setSelected(true);
        login_btn.setClickable(false);
        login_btn.setText(R.string.load);
        login_et_password.setFocusableInTouchMode(false);
        login_et_name.setFocusableInTouchMode(false);
        login_iv_remember.setClickable(false);
        re_tv_registration.setClickable(false);
    }

    private void able() {
        login_btn.setSelected(false);
        login_btn.setClickable(true);
        login_btn.setText(R.string.login);
        login_et_password.setFocusableInTouchMode(true);
        login_et_name.setFocusableInTouchMode(true);
        login_iv_remember.setClickable(true);
        re_tv_registration.setClickable(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (!TextUtils.isEmpty(login_et_name.getText().toString()) && !TextUtils.isEmpty(login_et_password.getText().toString())) {
                    passwordss = login_et_password.getText().toString();
                    ((MainActivity) getActivity()).handler.post(loginRunnable);
                    enable();
                } else {
                    showToast(getString(R.string.login_kong));
                }
                break;
            case R.id.re_tv_registration:
                ((MainActivity) getActivity()).setCurrentIndex(13);
                ((MainActivity) getActivity()).setMode(getString(R.string.register));
                break;
            case R.id.forget_tv_password:
                break;
            case R.id.login_iv_remember:
                if (login_iv_remember.isSelected()) {
                    remem = false;
                } else remem = true;
                login_iv_remember.setSelected(remem);
                break;
        }
    }
}