package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.base.BaseFragment;

/**
 * 实景列表模式
 */

public class SceneListFragment extends BaseFragment implements View.OnClickListener {
    View view;
   LinearLayout scene_ll_first, scene_ll_second, scene_ll_third;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sence, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        scene_ll_first = view.findViewById(R.id.scene_ll_first);
        scene_ll_second = view.findViewById(R.id.scene_ll_second);
        scene_ll_third = view.findViewById(R.id.scene_ll_third);

        scene_ll_first.setOnClickListener(this);
        scene_ll_second.setOnClickListener(this);
        scene_ll_third.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scene_ll_first:
                ((MainActivity) getActivity()).setVideo_index(0);
                break;
            case R.id.scene_ll_second:
                ((MainActivity) getActivity()).setVideo_index(1);
                break;
            case R.id.scene_ll_third:
                ((MainActivity) getActivity()).setVideo_index(2);
                break;
        }
        ((MainActivity) getActivity()).setCurrentIndex(8);
    }
}
