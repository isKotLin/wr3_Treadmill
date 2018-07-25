package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.dialog.Silently;
import com.vigorchip.treadmill.module.User;

/**
 * 个人信息
 */
public class PersonalInfoFragment extends BaseFragment implements View.OnClickListener {
    View view;
    ImageView personal_iv_icon;
    TextView personal_tv_name, personal_tv_time, personal_tv_height, personal_tv_weight, personal_tv_heart, personal_tv_ill;
    LinearLayout user_ll_edit
//            ,user_ll_cancellation
                    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        personal_iv_icon = view.findViewById(R.id.personal_iv_icon);
        personal_tv_name = view.findViewById(R.id.personal_tv_name);
        personal_tv_time = view.findViewById(R.id.personal_tv_time);
        personal_tv_height = view.findViewById(R.id.personal_tv_height);
        personal_tv_weight = view.findViewById(R.id.personal_tv_weight);
        personal_tv_heart = view.findViewById(R.id.personal_tv_heart);
        personal_tv_ill = view.findViewById(R.id.personal_tv_ill);
        user_ll_edit = view.findViewById(R.id.user_ll_edit);
//        user_ll_cancellation = view.findViewById(R.id.user_ll_cancellation);

        personal_iv_icon.setImageResource(User.getSex() == 1 ? R.mipmap.male : R.mipmap.female);
        personal_tv_name.setText(User.getUserName());
        personal_tv_time.setText(Silently.getHistoryLists().size() + " " + getString(R.string.ci));
        personal_tv_height.setText(User.getUserheight() + " " + getString(R.string.cm));
        personal_tv_weight.setText(User.getUserWeight() + " " + getString(R.string.kg));
        personal_tv_heart.setText(User.getUserHeart() + " " + getString(R.string.bpm));
        personal_tv_ill.setText(getNam());
        user_ll_edit.setOnClickListener(this);
//        user_ll_cancellation.setOnClickListener(this);
    }

    private String getNam() {
        StringBuffer buffer = new StringBuffer();
        if (User.getIll().equals("0")) {
            buffer.append(getString(R.string.without));
        } else {
            if (User.getIll().contains("1")) buffer.append(getString(R.string.arthritis) + "、");
            if (User.getIll().contains("2")) buffer.append(getString(R.string.cardiopathy) + "、");
            if (User.getIll().contains("3")) buffer.append(getString(R.string.hypertension) + "、");
            if (User.getIll().contains("4"))
                buffer.append(getString(R.string.hyperlipidemia) + "、");
            if (User.getIll().contains("5")) buffer.append(getString(R.string.hyperglycemia) + "、");
            buffer.delete(buffer.length() - 1, buffer.length());
        }
        return buffer.toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_ll_edit://编辑
                ((MainActivity) getActivity()).setCurrentIndex(10);
                ((MainActivity) getActivity()).setMode(getString(R.string.pre_info));
                break;
//            case user_ll_cancellation://退出
//                User.setUserId("-1");
//                User.setUserWeight(0);
//                MyApplication.getInstance().setDianji(true);
//                ((MainActivity) getActivity()).setCurrentIndex(14);
//                break;
        }
    }
}
