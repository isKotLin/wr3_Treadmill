package com.vigorchip.treadmill.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.view.AddCustomView;

/**
 * 调整自定义
 */
public class AdjustCustomFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private View view, custom_view_speed, custom_view_sloped;
    private AddCustomView custom_acv_data;
    private int[] speed, sloped;
    private RadioGroup selector_rg_ascend;
    private RadioButton selector_rb_sped, selector_rb_slop;
    private Button add_btn_custom;
    private EditText custom_et_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_adjust_custom, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        custom_acv_data = view.findViewById(R.id.custom_acv_data);
        custom_view_speed = view.findViewById(R.id.custom_view_speed);
        custom_view_sloped = view.findViewById(R.id.custom_view_sloped);
        selector_rg_ascend = view.findViewById(R.id.selector_rg_ascend);
        selector_rb_sped = view.findViewById(R.id.selector_rb_sped);
        selector_rb_slop = view.findViewById(R.id.selector_rb_slop);
        add_btn_custom = view.findViewById(R.id.add_btn_custom);
        custom_et_name = view.findViewById(R.id.custom_et_name);
        custom_et_name.setText("");
        custom_et_name.setFilters(new InputFilter[]{filter});
        custom_et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void afterTextChanged(Editable s) {
//        switchHint(s);

                selectionStart = custom_et_name.getSelectionStart();
                selectionEnd = custom_et_name.getSelectionEnd();
                custom_et_name.removeTextChangedListener(this);//防止内存溢出，必须写。

                while (s.toString().trim().replaceAll("[^\\x00-\\xff]", "**").length() >20) {//循环到20个字节为止
                    s.delete(selectionStart - 1, selectionEnd);
                    selectionStart--;
                    selectionEnd--;
                }
                custom_et_name.setText(s);//重新设置字符串
                custom_et_name.setSelection(selectionStart);//重新设置光标的位置
                custom_et_name.addTextChangedListener(this);//配合removeTextChangedListener
            }

        });
        speed = new int[16];
        sloped = new int[16];
        for (int i = 0; i < speed.length; i++) {
            speed[i] = MyApplication.getInstance().MINSPEED;
            sloped[i] = 0;
        }
        setData();
        ((RadioButton) selector_rg_ascend.getChildAt(0)).setChecked(true);
        selector_rb_sped.setTextColor(getResources().getColor(R.color.heart_text));
        selector_rg_ascend.setOnCheckedChangeListener(this);
        add_btn_custom.setOnClickListener(this);
        custom_acv_data.upDataRec(speed, true);
        if (MyApplication.getInstance().SLOPES == 0) {
            selector_rb_slop.setVisibility(View.GONE);
            custom_view_sloped.setVisibility(View.VISIBLE);
        }
        custom_acv_data.setOnChartClickListener(new AddCustomView.OnChartClickListener() {
            @Override
            public void onClick(int num, float value) {
                if (selector_rg_ascend.getCheckedRadioButtonId() == R.id.selector_rb_sped) {
                    value = (value < MyApplication.getInstance().MINSPEED) ? (float) MyApplication.getInstance().MINSPEED : value;
                    value = (value > MyApplication.getInstance().MAXSPEED) ? (float) MyApplication.getInstance().MAXSPEED : value;
                    speed[num] = (int) value;
                    custom_acv_data.upDataRec(speed, true);
                } else {
                    value = (value > MyApplication.getInstance().SLOPES) ? (float) MyApplication.getInstance().SLOPES : value;
                    sloped[num] = (int) value;
                    custom_acv_data.upDataRec(sloped, false);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.selector_rb_sped:
                selector_rb_sped.setTextColor(getResources().getColor(R.color.heart_text));
                selector_rb_slop.setTextColor(Color.WHITE);
                custom_view_sloped.setVisibility(View.INVISIBLE);
                custom_view_speed.setVisibility(View.VISIBLE);
                custom_acv_data.upDataRec(speed, true);
                break;
            case R.id.selector_rb_slop:
                selector_rb_sped.setTextColor(Color.WHITE);
                selector_rb_slop.setTextColor(getResources().getColor(R.color.heart_text));
                custom_view_sloped.setVisibility(View.VISIBLE);
                custom_view_speed.setVisibility(View.INVISIBLE);
                custom_acv_data.upDataRec(sloped, false);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(custom_et_name.getText().toString())) {
            showToast(getString(R.string.login_kong));
        } else {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < speed.length; i++) {
                buffer.append(speed[i] + " ");
                buffer.append(sloped[i] + " ");
            }
            Silently.setCustom(buffer.toString() + custom_et_name.getText().toString(), speed, sloped, custom_et_name.getText().toString());//设置数据
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void setData() {
        if (Silently.isTiHuan) {
            Silently.isTiHuan = false;
            speed = Silently.CUSTOM_TABLE.get(Silently.getCustomType() - 1)[0];
            sloped = Silently.CUSTOM_TABLE.get(Silently.getCustomType() - 1)[1];
            custom_et_name.setText(Silently.CUSTOM_NAME.get(Silently.getCustomType() - 1));
        }
    }

    private final int maxLen = 16;
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//            LogUtils.log("source:"+source+"    start:"+start+"     end:"+end+"     dest:"+dest+"       dstart:"+dstart+"    dend:"+dend);
            if (source.equals(" ")) {
                    showToast("不能输入空格");
                return "";
            }
//            int dindex = 0;
//            int count = 0;
//            while (count <= maxLen && dindex < dest.length()) {
//                char c = dest.charAt(dindex++);
//                if (c < 128) {
//                    count = count + 1;
//                } else {
//                    count = count + 2;
//                }
//            }
//            if (count > maxLen) {
//                LogUtils.log("dest:"+dest.subSequence(0, dindex - 1));
//                return dest.subSequence(0, dindex - 1);
//            }
//            int sindex = 0;count = 0;
//            while (count <= maxLen && sindex < source.length()) {
//                char c = source.charAt(sindex++);
//                if (c < 128) {
//                    count = count + 1;
//                } else {
//                    count = count + 2;
//                }
//            }
//            if (count > maxLen) {
//                sindex--;
//            }
//            return source.subSequence(0, sindex);
            return source;
        }
    };

//    /**
//     * 禁止EditText输入特殊字符
//     * @param editText
//     */
//    public static void setEditTextInhibitInputSpeChat(EditText editText){
//
//        InputFilter filter=new InputFilter() {
//            @Override
//            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
//                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//                Pattern pattern = Pattern.compile(speChat);
//                Matcher matcher = pattern.matcher(source.toString());
//                if(matcher.find())return "";
//                else return null;
//            }
//        };
//        editText.setFilters(new InputFilter[]{filter});
//    }
}
