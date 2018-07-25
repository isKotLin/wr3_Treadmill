package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.okttp.UrlConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * 完善信息
 */
public class CompleteInfoFragment extends BaseFragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    View view;
    RadioGroup info_rg_sex;
    RadioButton info_rt_male, info_rt_female;
    EditText info_et_name, info_et_height, info_et_weight, info_et_heart, info_et_age;
    ImageView info_iv_without, info_iv_arthritis, info_iv_cardiopathy, info_iv_hypertension, info_iv_hyperlipidemia, info_iv_hyperglycemia;
    Button complete_btn_commit;
    String msg = "1";
    LinearLayout info_tv_without, info_tv_arthritis, info_tv_cardiopathy, info_tv_hypertension, info_tv_hyperlipidemia, info_tv_hyperglycemia;

    private void enable() {//不可用
        complete_btn_commit.setSelected(true);
        complete_btn_commit.setClickable(false);
        complete_btn_commit.setText(R.string.load);
        info_et_name.setFocusableInTouchMode(false);
        info_et_height.setFocusableInTouchMode(false);
        info_et_weight.setFocusableInTouchMode(false);
        info_et_heart.setFocusableInTouchMode(false);
        info_et_age.setFocusableInTouchMode(false);
        info_tv_without.setClickable(false);
        info_tv_arthritis.setClickable(false);
        info_tv_cardiopathy.setClickable(false);
        info_tv_hypertension.setClickable(false);
        info_tv_hyperlipidemia.setClickable(false);
        info_tv_hyperglycemia.setClickable(false);
        info_rt_male.setEnabled(false);
        info_rt_female.setEnabled(false);
    }

    private void able() {//可用
        complete_btn_commit.setSelected(false);
        complete_btn_commit.setClickable(true);
        complete_btn_commit.setText(R.string.commit);
        info_et_name.setFocusableInTouchMode(true);
        info_et_height.setFocusableInTouchMode(true);
        info_et_weight.setFocusableInTouchMode(true);
        info_et_heart.setFocusableInTouchMode(true);
        info_et_age.setFocusableInTouchMode(true);
        info_tv_without.setClickable(true);
        info_tv_arthritis.setClickable(true);
        info_tv_cardiopathy.setClickable(true);
        info_tv_hypertension.setClickable(true);
        info_tv_hyperlipidemia.setClickable(true);
        info_tv_hyperglycemia.setClickable(true);
        info_rt_male.setEnabled(true);
        info_rt_female.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_complete_info, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        able();
        info_rg_sex.setOnCheckedChangeListener(this);
    }

    private void initView() {
        info_rg_sex = view.findViewById(R.id.info_rg_sex);
        info_rt_male = view.findViewById(R.id.info_rt_male);
        info_rt_female = view.findViewById(R.id.info_rt_female);
        info_et_name = view.findViewById(R.id.info_et_name);
        info_et_height = view.findViewById(R.id.info_et_height);
        info_et_weight = view.findViewById(R.id.info_et_weight);
        info_et_heart = view.findViewById(R.id.info_et_heart);
        info_et_age = view.findViewById(R.id.info_et_age);
        complete_btn_commit = view.findViewById(R.id.complete_btn_commit);
        setRegion(info_et_height, 90, 210);
        setRegion(info_et_weight, 30, 150);
        setRegion(info_et_heart, 84, 195);
        setRegion(info_et_age, 15, 80);
//        info_et_name.setFilters(new InputFilter[]{filter});
        info_et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private int selectionStart;
            private int selectionEnd;

            @Override
            public void afterTextChanged(Editable s) {
//                switchHint(s);

                selectionStart = info_et_name.getSelectionStart();
                selectionEnd = info_et_name.getSelectionEnd();
                info_et_name.removeTextChangedListener(this);//防止内存溢出，必须写。

                while (s.toString().trim().replaceAll("[^\\x00-\\xff]", "**").length() > 16) {//循环到20个字节为止
                    s.delete(selectionStart - 1, selectionEnd);
                    selectionStart--;
                    selectionEnd--;
                }
                info_et_name.setText(s);//重新设置字符串
                if (selectionStart <= 16) {
                    info_et_name.setSelection(selectionStart);//重新设置光标的位置
                }
                info_et_name.addTextChangedListener(this);//配合removeTextChangedListener
            }

        });
        info_iv_without = view.findViewById(R.id.info_iv_without);
        info_iv_arthritis = view.findViewById(R.id.info_iv_arthritis);
        info_iv_cardiopathy = view.findViewById(R.id.info_iv_cardiopathy);
        info_iv_hypertension = view.findViewById(R.id.info_iv_hypertension);
        info_iv_hyperlipidemia = view.findViewById(R.id.info_iv_hyperlipidemia);
        info_iv_hyperglycemia = view.findViewById(R.id.info_iv_hyperglycemia);

        info_tv_without = view.findViewById(R.id.info_tv_without);
        info_tv_arthritis = view.findViewById(R.id.info_tv_arthritis);
        info_tv_cardiopathy = view.findViewById(R.id.info_tv_cardiopathy);
        info_tv_hypertension = view.findViewById(R.id.info_tv_hypertension);
        info_tv_hyperlipidemia = view.findViewById(R.id.info_tv_hyperlipidemia);
        info_tv_hyperglycemia = view.findViewById(R.id.info_tv_hyperglycemia);

        info_iv_without.setSelected(true);
        info_tv_without.setOnClickListener(this);
        info_tv_arthritis.setOnClickListener(this);
        info_tv_cardiopathy.setOnClickListener(this);
        info_tv_hypertension.setOnClickListener(this);
        info_tv_hyperlipidemia.setOnClickListener(this);
        info_tv_hyperglycemia.setOnClickListener(this);
        complete_btn_commit.setOnClickListener(this);
        showInfo();
    }

    private void showInfo() {//编辑
        if (!User.getUserId().equals("-1")) {
            info_et_name.setText(User.getUserName());
            info_et_height.setText(User.getUserheight() == 0 ? "" : User.getUserheight() + "");
            info_et_weight.setText(User.getUserWeight() == 0 ? "" : User.getUserWeight() + "");
            info_et_heart.setText(User.getUserHeart() == 0 ? "" : User.getUserHeart() + "");
            info_et_age.setText(User.getUserAge() == 0 ? "" : User.getUserAge() + "");
            info_rg_sex.getChildAt(User.getSex());
            getNamss();
        }
    }

    private void getNamss() {
        if (User.getIll().equals("0")) {
            info_iv_without.setSelected(true);
        } else {
            info_iv_without.setSelected(false);
            if (User.getIll().contains("1")) info_iv_arthritis.setSelected(true);
            if (User.getIll().contains("2")) info_iv_cardiopathy.setSelected(true);
            if (User.getIll().contains("3")) info_iv_hypertension.setSelected(true);
            if (User.getIll().contains("4")) info_iv_hyperlipidemia.setSelected(true);
            if (User.getIll().contains("5")) info_iv_hyperglycemia.setSelected(true);
        }
    }

    private String seill() {
        StringBuffer buffer = new StringBuffer();
        if (info_iv_without.isSelected()) {
            buffer.append("0");
            return buffer.toString();
        } else {
            if (info_iv_arthritis.isSelected())
                buffer.append("1");
            if (info_iv_cardiopathy.isSelected())
                buffer.append("2");
            if (info_iv_hypertension.isSelected())
                buffer.append("3");
            if (info_iv_hyperlipidemia.isSelected())
                buffer.append("4");
            if (info_iv_hyperglycemia.isSelected())
                buffer.append("5");
            return buffer.toString();
        }
    }

    Runnable completeRunnable = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", User.getUserId());
            map.put("nickname", info_et_name.getText().toString());
            map.put("age", info_et_age.getText().toString());
            map.put("weight", info_et_weight.getText().toString());
            map.put("tall", info_et_height.getText().toString());
            map.put("xinlv", info_et_heart.getText().toString());
            map.put("sex", msg);
            map.put("illness", seill());
            Okhttp.get(false, UrlConstant.COMPELETE, map, new Okhttp.CallBac() {
                @Override
                public void onError(Call call, Exception e, String state, int id) {
                    showToast(getString(R.string.error));
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
                            User.setUserWeight(data.optInt("weight"));
                            User.setIll(data.optString("illness"));
                            User.setUserAge(data.optInt("age"));
                            User.setUserHeart(data.optInt("xinlv"));
                            User.setUserheight(data.optInt("tall"));
                            User.setUserName(data.optString("nickname"));
                            User.setUserPhone(data.optString("phone"));
                            User.setSex(data.optInt("sex"));
                            MyApplication.getInstance().setDianji(false);
                            ((MainActivity) getActivity()).cuss();
                            SportHomePageFragment.update();
                            Silently.getCustom("info1", "info2", "info3", "info4", "info5", "info6", "info7", "info8", "info9", "info10");
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.complete_btn_commit:
                commit();
                enable();
                break;
            case R.id.info_tv_without:
                if (info_iv_without.isSelected()) {//info_iv_without.setSelected(false);
                } else {
                    info_iv_without.setSelected(true);
                    info_iv_arthritis.setSelected(false);
                    info_iv_cardiopathy.setSelected(false);
                    info_iv_hypertension.setSelected(false);
                    info_iv_hyperlipidemia.setSelected(false);
                    info_iv_hyperglycemia.setSelected(false);
                }
                break;
            case R.id.info_tv_arthritis:
                if (info_iv_arthritis.isSelected())
                    info_iv_arthritis.setSelected(false);
                else
                    info_iv_arthritis.setSelected(true);
                break;
            case R.id.info_tv_cardiopathy:
                if (info_iv_cardiopathy.isSelected())
                    info_iv_cardiopathy.setSelected(false);
                else
                    info_iv_cardiopathy.setSelected(true);
                break;
            case R.id.info_tv_hypertension:
                if (info_iv_hypertension.isSelected())
                    info_iv_hypertension.setSelected(false);
                else
                    info_iv_hypertension.setSelected(true);
                break;
            case R.id.info_tv_hyperlipidemia:
                if (info_iv_hyperlipidemia.isSelected())
                    info_iv_hyperlipidemia.setSelected(false);
                else
                    info_iv_hyperlipidemia.setSelected(true);
                break;
            case R.id.info_tv_hyperglycemia:
                if (info_iv_hyperglycemia.isSelected())
                    info_iv_hyperglycemia.setSelected(false);
                else
                    info_iv_hyperglycemia.setSelected(true);
                break;
        }
        ill();
    }

    private void ill() {
        if (info_iv_arthritis.isSelected() ||
                info_iv_cardiopathy.isSelected() ||
                info_iv_hypertension.isSelected() ||
                info_iv_hyperlipidemia.isSelected() ||
                info_iv_hyperglycemia.isSelected())
            info_iv_without.setSelected(false);
        else info_iv_without.setSelected(true);
    }

    private void commit() {
        if (TextUtils.isEmpty(info_et_height.getText().toString()) ||
                TextUtils.isEmpty(info_et_weight.getText().toString()) ||
                TextUtils.isEmpty(info_et_name.getText().toString()) ||
                TextUtils.isEmpty(info_et_age.getText().toString()) ||
                TextUtils.isEmpty(info_et_heart.getText().toString())) {
            showToast(getString(R.string.no_kong));
        } else {
            ((MainActivity) getActivity()).handler.post(completeRunnable);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        if (info_rt_male.getId() == i) {
            msg = "1";
        } else if (info_rt_female.getId() == i) {
            msg = "0";
        }
    }

    private final int maxLen = 20;
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;
            while (count <= maxLen && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }
            if (count > maxLen) {
                return dest.subSequence(0, dindex - 1);
            }
            int sindex = 0;
            count = 0;
            while (count <= maxLen && sindex < source.length()) {
                char c = source.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }
            if (count > maxLen) {
                sindex--;
            }
            return source.subSequence(0, sindex);
        }
    };

    private void setRegion(final EditText et, final int MIN_MARK, final int MAX_MARK) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start > 1) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int num = Integer.parseInt(s.toString());
                        if (num > MAX_MARK) {
                            s = String.valueOf(MAX_MARK);
                            et.setText(s);
                        } else if (num < MIN_MARK)
                            s = String.valueOf(MIN_MARK);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && !s.equals("")) {
                    if (MIN_MARK != -1 && MAX_MARK != -1) {
                        int markVal = 0;
                        try {
                            markVal = Integer.parseInt(s.toString());
                        } catch (NumberFormatException e) {
                            markVal = 0;
                        }
                        if (markVal > MAX_MARK) {
                            showToast(getResources().getString(R.string.not_exceed) + MAX_MARK);
                            et.setText(String.valueOf(MAX_MARK));
                        }
                        return;
                    }
                }
            }
        });
    }
}
