package com.vigorchip.treadmill.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vigorchip.treadmill.R;
import com.vigorchip.treadmill.base.BaseFragment;
import com.vigorchip.treadmill.utils.PublicUtils;

import java.text.DecimalFormat;

/**
 * 操作指南
 */
public class OperatorFragment extends BaseFragment {
    View view;
    TextView emmc_flash, setting_tv_ddr,
            opear_tv_0, opear_tv_1, opear_tv_2, opear_tv_3, opear_tv_4, opear_tv_5, opear_tv_6, opear_tv_7, opear_tv_8, opear_tv_9, opear_tv_10, opear_tv_11, opear_tv_12, opear_tv_13, opear_tv_14, opear_tv_15,
            type_tv_0, type_tv_1, type_tv_2, type_tv_3, type_tv_4, type_tv_5, type_tv_6, type_tv_7, type_tv_8, type_tv_9, type_tv_10, type_tv_11, type_tv_12, type_tv_13, type_tv_14, type_tv_15,
            codes_tv_0, codes_tv_1, codes_tv_2, codes_tv_3, codes_tv_4, codes_tv_5, codes_tv_6, codes_tv_7, codes_tv_8, codes_tv_9, codes_tv_10, codes_tv_11, codes_tv_12, codes_tv_13, codes_tv_14, codes_tv_15;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_operator, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        initData();
        initFlash();
    }

    DecimalFormat format;
    float total;
    float available;
    float sum;

    private void initFlash() {
        available = (float) PublicUtils.getSDAvailableSize();
        total = (float) PublicUtils.getSDTotalSize();
        format = new DecimalFormat("0.0");
        sum = total - available;
        if (sum > 1000000000)
            emmc_flash.setText(getString(R.string.memory_space) + format.format(8.0 - (total / 1000000000)) +getString(R.string.useable_user) + format.format(total / 1000000000) + getString(R.string.been_used) + format.format(sum / 1000000000) + getString(R.string.remaining_GB) + format.format(available / 1000000000) + getString(R.string.GB));
        else
            emmc_flash.setText(getString(R.string.memory_space) + format.format(8.0 - (total / 1000000000)) + getString(R.string.useable_user) + format.format(total / 1000000000) + getString(R.string.been_used) + format.format(sum / 1000000) + getString(R.string.remaining_MB) + format.format(available / 1000000000) + getString(R.string.GB));
    }

    private void initData() {
        setting_tv_ddr.setText("DDR : " + PublicUtils.getTotalMemorySize(getContext()));
    }

    private void initView() {
        setting_tv_ddr =  view.findViewById(R.id.setting_tv_ddr);
        emmc_flash =  view.findViewById(R.id.emmc_flash);
        codes_tv_0 =  view.findViewById(R.id.codes_tv_0);
        codes_tv_1 =  view.findViewById(R.id.codes_tv_1);
        codes_tv_2 =  view.findViewById(R.id.codes_tv_2);
        codes_tv_3 =   view.findViewById(R.id.codes_tv_3);
        codes_tv_4 =   view.findViewById(R.id.codes_tv_4);
        codes_tv_5 =   view.findViewById(R.id.codes_tv_5);
        codes_tv_6 =   view.findViewById(R.id.codes_tv_6);
        codes_tv_7 =   view.findViewById(R.id.codes_tv_7);
        codes_tv_8 =   view.findViewById(R.id.codes_tv_8);
        codes_tv_9 =   view.findViewById(R.id.codes_tv_9);
        codes_tv_10 =   view.findViewById(R.id.codes_tv_10);
        codes_tv_11 =   view.findViewById(R.id.codes_tv_11);
        codes_tv_12 =   view.findViewById(R.id.codes_tv_12);
        codes_tv_13 =   view.findViewById(R.id.codes_tv_13);
        codes_tv_14 =   view.findViewById(R.id.codes_tv_14);
        codes_tv_15 =   view.findViewById(R.id.codes_tv_15);

        type_tv_0 =   view.findViewById(R.id.type_tv_0);
        type_tv_1 =   view.findViewById(R.id.type_tv_1);
        type_tv_2 =   view.findViewById(R.id.type_tv_2);
        type_tv_3 =   view.findViewById(R.id.type_tv_3);
        type_tv_4 =   view.findViewById(R.id.type_tv_4);
        type_tv_5 =   view.findViewById(R.id.type_tv_5);
        type_tv_6 =   view.findViewById(R.id.type_tv_6);
        type_tv_7 =   view.findViewById(R.id.type_tv_7);
        type_tv_8 =   view.findViewById(R.id.type_tv_8);
        type_tv_9 =   view.findViewById(R.id.type_tv_9);
        type_tv_10 =   view.findViewById(R.id.type_tv_10);
        type_tv_11 =   view.findViewById(R.id.type_tv_11);
        type_tv_12 =   view.findViewById(R.id.type_tv_12);
        type_tv_13 =   view.findViewById(R.id.type_tv_13);
        type_tv_14 =   view.findViewById(R.id.type_tv_14);
        type_tv_15 =   view.findViewById(R.id.type_tv_15);

        opear_tv_0 =   view.findViewById(R.id.opear_tv_0);
        opear_tv_1 =   view.findViewById(R.id.opear_tv_1);
        opear_tv_2 =   view.findViewById(R.id.opear_tv_2);
        opear_tv_3 =   view.findViewById(R.id.opear_tv_3);
        opear_tv_4 =   view.findViewById(R.id.opear_tv_4);
        opear_tv_5 =   view.findViewById(R.id.opear_tv_5);
        opear_tv_6 =   view.findViewById(R.id.opear_tv_6);
        opear_tv_7 =   view.findViewById(R.id.opear_tv_7);
        opear_tv_8 =   view.findViewById(R.id.opear_tv_8);
        opear_tv_9 =   view.findViewById(R.id.opear_tv_9);
        opear_tv_10 =   view.findViewById(R.id.opear_tv_10);
        opear_tv_11 =   view.findViewById(R.id.opear_tv_11);
        opear_tv_12 =   view.findViewById(R.id.opear_tv_12);
        opear_tv_13 =   view.findViewById(R.id.opear_tv_13);
        opear_tv_14 =   view.findViewById(R.id.opear_tv_14);
        opear_tv_15 =   view.findViewById(R.id.opear_tv_15);
    }

    /**
     * 检测该包名所对应的应用是否存在
     *
     * @param packageName
     * @return
     */
    public boolean checkPackage(String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            getActivity().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
