package com.vigorchip.treadmill.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.adapter.AppAdapter;
import com.vigorchip.treadmill.adapter.AppViewPagerAdapter;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.utils.LogUtils;
import com.vigorchip.treadmill.view.AppViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 应用
 */
public class AppHomePageFragment extends BaseFragment implements AppAdapter.OnItemListenerOnClick, ViewPager.OnPageChangeListener
//        , SettingToApp
{
    View view;
    AppViewPager app_vvp;
    List<ResolveInfo> mApps;
    PackageManager packageManager;
    List<View> views;//页面集合
    LinearLayout app_ll_point;//圆点容器
    private List<ImageView> mDotView;//圆点集合
    int len;//页面数
//boolean isInto=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_home_page, container, false);
        return view;
    }
    AppAdapter adapter;
    @Override
    public void onResume() {
        super.onResume();
//        isInto=true;
        mo();
//        SettingHomePageFragment.setToApp(this);
    }

    private void mo() {
        app_vvp = view.findViewById(R.id.app_vvp);
        app_ll_point = view.findViewById(R.id.app_ll_point);
        init();
        AppSorting();
        getNumbers();
        app_vvp.addOnPageChangeListener(this);
    }

    private void init() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        if (getActivity()==null)
            return;
        packageManager = getActivity().getPackageManager();
        mApps = packageManager.queryIntentActivities(intent, 0);
        views = new ArrayList<>();
        mDotView = new ArrayList<>();
        Iterator iterator = mApps.iterator();//去除list中item
        while (iterator.hasNext()) {
            ResolveInfo resolveInfo = (ResolveInfo) iterator.next();
            if (resolveInfo.activityInfo.packageName.equals("com.vigorchip.treadmill.wr3") ||//本应用
                    resolveInfo.activityInfo.packageName.equals("com.android.settings") ||//设置
                    resolveInfo.activityInfo.packageName.equals("com.softwinner.update") || //升级
                    resolveInfo.activityInfo.packageName.equals("com.supercell.clashofclans.uc") || //部落冲突
                    resolveInfo.activityInfo.packageName.equals("com.qt.cleanmaster") //一键清理
                    ) {
                iterator.remove();
            }
        }
    }

    private void AppSorting() {//排序
//        Collections.sort(mApps, new Comparator<ResolveInfo>() {//按安装时间排序
//            @Override
//            public int compare(ResolveInfo a, ResolveInfo b) {
//                String packageNameA = a.activityInfo.applicationInfo.packageName;
//                String packageNameB = b.activityInfo.applicationInfo.packageName;
//                long firstInstallTimeA = 0, firstInstallTimeB = 0;
//                try {
//                    firstInstallTimeA = packageManager.getPackageInfo(packageNameA, 0).firstInstallTime;
//                    firstInstallTimeB = packageManager.getPackageInfo(packageNameB, 0).firstInstallTime;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return 0;
//                }
//                return firstInstallTimeA == firstInstallTimeB ? 0 : firstInstallTimeA > firstInstallTimeB ? 1 : -1;
//            }
//        });
        Collections.sort(mApps, new ResolveInfo.DisplayNameComparator(packageManager));//按包名排序
        for (int i = 0; i < mApps.size(); i++) {
            if (mApps.get(i).activityInfo.packageName.equals("com.vigorchip.WrMusic.wr2")) {
                mApps.add(0, mApps.remove(i));
            }
            if (mApps.get(i).activityInfo.packageName.equals("com.vigorchip.WrVideo.wr2")) {
                mApps.add(0, mApps.remove(i));
            }
        }
    }

    private void getNumbers() {//数据
        len = mApps.size() % 8 == 0 ? mApps.size() / 8 : mApps.size() / 8 + 1;
        LayoutInflater lf = getLayoutInflater(getArguments()).from(getContext());
        int j = 0;
        app_ll_point.removeAllViews();
        for (int i = 0; i < len; i++) {//有多少个页面 View linearLayout = lf.inflate(R.layout.fragment_app_content, null); GridView gridView = linearLayout.findViewById(R.id.app_gv_content);
            GridView gridView = (GridView) lf.inflate(R.layout.fragment_app_content, null);
            List<ResolveInfo> smallApps = new ArrayList<>();
            if (mDotView.size() == i) {
                ImageView imageView = new ImageView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            params.leftMargin = 10;//设置圆点相距距离     params.rightMargin = 10;
                params.setMargins((int) getResources().getDimension(R.dimen.my10dp),
                        (int) getResources().getDimension(R.dimen.my10dp),
                        (int) getResources().getDimension(R.dimen.my10dp),
                        (int) getResources().getDimension(R.dimen.my10dp));
                if (i == 0) {//初始化为实点
                    imageView.setBackgroundResource(R.mipmap.selectored);
                } else {
                    imageView.setBackgroundResource(R.mipmap.no_selector);
                }
                app_ll_point.addView(imageView, params);
                mDotView.add(imageView);
            }
            for (; j < mApps.size() && j < 8 + 8 * i; j++) {//一个页面放八个app
                smallApps.add(mApps.get(j));
            }
             adapter = new AppAdapter(getContext(), smallApps, i);
            gridView.setAdapter(adapter);
            views.add(gridView);
            adapter.setOnItemListenerOnClick(this);
        }
        app_vvp.setAdapter(new AppViewPagerAdapter(views));
    }

    @Override
    public void itemClick(int position) {
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(mApps.get(app_vvp.getCurrentItem() * 8 + position).activityInfo.packageName);
            startActivity(intent);
        } catch (Exception e) {
            LogUtils.log("跳转失败");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < len; i++) {
            if ((position % len) == i) {//如果是当前的位置就设置为实点
                mDotView.get(i).setBackgroundResource(R.mipmap.selectored);
            } else {
                mDotView.get(i).setBackgroundResource(R.mipmap.no_selector);
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

//    @Override
//    public void updataApp() {
//        mo();
//    }
}