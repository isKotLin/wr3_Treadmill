package com.vigorchip.treadmill.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.activity.MainActivity;
import com.vigorchip.treadmill.adapter.MyViewPagerAdapter;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.module.SportData;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.service.MyService;
import com.vigorchip.treadmill.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 运动模式
 */
public class SportHomePageFragment extends BaseFragment implements View.OnClickListener
//        , View.OnTouchListener
{
    View view;
    LinearLayout sport_ll_program, sport_ll_custom, sport_ll_inverted, sport_ll_heart_rate, sport_ll_manual;
    RelativeLayout sport_ll_scene, sport_rl_personal;
    private static TextView sport_tv_user_name, hello_tv;
    private static CircleImageView sport_civ_user_icon;
    private static ViewFlipper viewflipper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sport_home_page, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sport_civ_user_icon = view.findViewById(R.id.sport_civ_user_icon);
        sport_ll_program = view.findViewById(R.id.sport_ll_program);
        sport_ll_custom = view.findViewById(R.id.sport_ll_custom);
        sport_ll_inverted = view.findViewById(R.id.sport_ll_inverted);
        sport_ll_heart_rate = view.findViewById(R.id.sport_ll_heart_rate);
        sport_ll_manual = view.findViewById(R.id.sport_ll_manual);
        sport_ll_scene = view.findViewById(R.id.sport_ll_scene);
        sport_rl_personal = view.findViewById(R.id.sport_rl_personal);
        sport_tv_user_name = view.findViewById(R.id.sport_tv_user_name);
        viewflipper = view.findViewById(R.id.viewflipper);
        hello_tv = view.findViewById(R.id.hello_tv);
        sport_ll_program.setOnClickListener(this);
        sport_ll_custom.setOnClickListener(this);
        sport_ll_inverted.setOnClickListener(this);
        sport_ll_heart_rate.setOnClickListener(this);
        sport_ll_manual.setOnClickListener(this);
        sport_ll_scene.setOnClickListener(this);
        sport_rl_personal.setOnClickListener(this);
//        sport_ll_scene.setOnTouchListener(this);
        start();
        update();
    }

    public static void update() {
        if (!User.getUserId().equals("-1")) {
            SportData.setStatus(SportData.NoAppVisitor);
            if (sport_civ_user_icon != null) {
                sport_civ_user_icon.setVisibility(View.VISIBLE);
                sport_civ_user_icon.setImageResource(User.getSex() == 1 ? R.mipmap.male : R.mipmap.female);
            }
            if (sport_tv_user_name != null) {
                sport_tv_user_name.setVisibility(View.VISIBLE);
                sport_tv_user_name.setText(User.getUserName());
            }
            if (hello_tv != null)
                hello_tv.setVisibility(View.GONE);
        } else {
            SportData.setStatus(SportData.AppVisitor);
            if (hello_tv != null)
                hello_tv.setVisibility(View.VISIBLE);
            if (sport_tv_user_name != null)
                sport_tv_user_name.setVisibility(View.GONE);
            if (sport_civ_user_icon != null)
                sport_civ_user_icon.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sport_ll_program:
                ((MainActivity) getActivity()).setCurrentIndex(0);
                ((MainActivity) getActivity()).setMode(getString(R.string.program_mode));
                SportData.setStatus(SportData.PROGRAM_SETUP_STATUS);
                break;
            case R.id.sport_ll_custom:
                if (User.getUserId().equals("-1"))
                    showToast(getString(R.string.qing));
                else {
                    ((MainActivity) getActivity()).setCurrentIndex(1);
                    ((MainActivity) getActivity()).setMode(getString(R.string.custom_mode));
                    SportData.setStatus(SportData.USER_SETUP_STATUS);
                }
                break;
            case R.id.sport_ll_inverted:
                ((MainActivity) getActivity()).setCurrentIndex(2);
                ((MainActivity) getActivity()).setMode(getString(R.string.inverted_mode));
                SportData.setStatus(SportData.INVERSE_SETUP_STATUS);
                break;
            case R.id.sport_ll_heart_rate:
                ((MainActivity) getActivity()).setCurrentIndex(3);
                ((MainActivity) getActivity()).setMode(getString(R.string.heart_rate_mode));
                SportData.setStatus(SportData.HRC_SETUP_STATUS);
                break;
            case R.id.sport_ll_manual:
                showDialog();
                break;
            case R.id.sport_ll_scene:
                showSceneDialog();
                break;
            case R.id.sport_rl_personal:
                if (User.getUserId().equals("-1")) {//showToast(getString(R.string.qing));
                    MyApplication.getInstance().setDianji(true);
                    ((MainActivity) getActivity()).setCurrentIndex(14);
                } else
                    ((MainActivity) getActivity()).setCurrentIndex(12);
                break;
        }
    }

    private static AlertDialog dialog;

    public void showDialog() {
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
        start_tv_model.setText(R.string.manual_mode);
        start_tv_statement.setText(R.string.manual_state);
        start_rl_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog.isShowing()) {
                    ((MainActivity) getActivity()).startRun();
                    dialog.dismiss();
                }
            }
        });
    }

    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (sceneDialog != null && sceneDialog.isShowing()) {
            sceneDialog.dismiss();
        }

    }

    private static AlertDialog sceneDialog;
    View scene_layout1, scene_layout2, scene_layout3;
    List<View> viewList;

    public void showSceneDialog() {
        if (sceneDialog != null && sceneDialog.isShowing())
            return;
        sceneDialog = new AlertDialog.Builder(getContext(), R.style.NoTitle).create();
        sceneDialog.show();
        sceneDialog.getWindow().setContentView(R.layout.dialog_scene_start);
//        WindowManager.LayoutParams lp = sceneDialog.getWindow().getAttributes();
//        lp.width = DensityUtils.px2dip(getContext(), 602);//定义宽度
//        lp.height = DensityUtils.px2dip(getContext(), 360);//定义高度
//        sceneDialog.getWindow().setAttributes(lp);
        final ViewPager scene_vp_show = (ViewPager) sceneDialog.findViewById(R.id.scene_vp_show);
        LayoutInflater lf = getActivity().getLayoutInflater().from(getContext());
        scene_layout1 = lf.inflate(R.layout.scene_layout1, null);
        scene_layout2 = lf.inflate(R.layout.scene_layout2, null);
        scene_layout3 = lf.inflate(R.layout.scene_layout3, null);

        viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(scene_layout1);
        viewList.add(scene_layout2);
        viewList.add(scene_layout3);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(viewList);
        scene_vp_show.setAdapter(adapter);
        RelativeLayout start_scene_rl_run = (RelativeLayout) sceneDialog.findViewById(R.id.start_scene_rl_run);
        final ImageView last_iv_scene = (ImageView) sceneDialog.findViewById(R.id.last_iv_scene);
        final ImageView next_iv_scene = (ImageView) sceneDialog.findViewById(R.id.next_iv_scene);
        last_iv_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scene_vp_show.setCurrentItem(scene_vp_show.getCurrentItem() - 1);
            }
        });
        next_iv_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scene_vp_show.setCurrentItem(scene_vp_show.getCurrentItem() + 1);
            }
        });
        scene_vp_show.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        last_iv_scene.setVisibility(View.GONE);
                        next_iv_scene.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        last_iv_scene.setVisibility(View.VISIBLE);
                        next_iv_scene.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        last_iv_scene.setVisibility(View.VISIBLE);
                        next_iv_scene.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        start_scene_rl_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SportData.setStatus(SportData.SCENE_SETUP_STATUS);
                if (sceneDialog.isShowing()) {
                    ((MainActivity) getActivity()).setVideo_index(scene_vp_show.getCurrentItem());
                    ((MainActivity) getActivity()).startRun();
                    sceneDialog.dismiss();
                }
            }
        });

    }

    public static void stop() {
        if (viewflipper != null) {
            viewflipper.stopFlipping();
        }
    }

    public static void start() {
        MyService.updatess();
        if (viewflipper != null && !viewflipper.isFlipping()) {
            viewflipper.startFlipping();
        }
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                stop();
//                break;
//            case MotionEvent.ACTION_UP:
//               start();
//                break;
//        }
//        return false;
//    }
}