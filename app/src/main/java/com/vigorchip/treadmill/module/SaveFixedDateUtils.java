package com.vigorchip.treadmill.module;

import com.vigorchip.treadmill.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存固定数据
 */
public class SaveFixedDateUtils {
    public static final String INIT_TIME = "20180228 00:00:00";//初始化时间
    public static final String VERSION_TIME = "2018-02-28";//日期时间
    public static final String TYPE = "T421";//版本
    public static List<int[][]> PROGRAM_TABLE = new ArrayList<>();//程序模式数据
    public static int[] PROGRAM_NAME = {R.string.aerobies_gradient, R.string.climb_gradient, R.string.preinstall_gradient, R.string.insanity_gradient, R.string.health_care_gradient, R.string.warm_up_gradient};
    public static int[] PROGRAM_BJ = {R.drawable.test0, R.drawable.test5, R.drawable.test6, R.drawable.test7, R.drawable.test8, R.drawable.test9};
    public static int[] PROGRAM_STATE = {R.string.aerobies_state, R.string.climb_state, R.string.preinstall_state, R.string.insanity_state, R.string.health_state, R.string.warm_state};

    static {
        PROGRAM_TABLE.add(new int[][]{{40, 70, 50, 70, 50, 70, 50, 70, 50, 70, 50, 70, 50, 70, 50, 30},
                {4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0}});
        PROGRAM_TABLE.add(new int[][]{{50, 70, 60, 70, 60, 70, 60, 70, 60, 70, 60, 70, 60, 70, 60, 50},
                {3, 4, 5, 4, 5, 4, 5, 4, 5, 4, 5, 4, 5, 4, 5, 2}});
        PROGRAM_TABLE.add(new int[][]{{80, 110, 90, 110, 90, 110, 90, 110, 90, 110, 90, 110, 90, 110, 90, 50},
                {3, 6, 3, 6, 3, 6, 3, 6, 3, 6, 3, 6, 3, 6, 3, 0}});
        PROGRAM_TABLE.add(new int[][]{{60, 80, 90, 80, 90, 80, 90, 80, 90, 80, 90, 80, 90, 80, 90, 30},
                {2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0}});
        PROGRAM_TABLE.add(new int[][]{{30, 60, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 60, 30},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}});
        PROGRAM_TABLE.add(new int[][]{{30, 40, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 30},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0}});
    }
}