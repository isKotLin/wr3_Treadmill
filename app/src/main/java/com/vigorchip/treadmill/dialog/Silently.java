package com.vigorchip.treadmill.dialog;

import android.content.Context;
import android.text.TextUtils;

import com.vigorchip.treadmill.base.MyApplication;
import com.vigorchip.treadmill.impl.SilentToCustom;
import com.vigorchip.treadmill.impl.SilentlyToHand;
import com.vigorchip.treadmill.module.HistoryList;
import com.vigorchip.treadmill.module.User;
import com.vigorchip.treadmill.okttp.Okhttp;
import com.vigorchip.treadmill.okttp.UrlConstant;
import com.vigorchip.treadmill.utils.PublicUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 *
 */
public class Silently {
    public static void setCheck(int check) {
        Check = check;
    }

    private static int Check = 1;
    private static String downloadUrl;//app下载路径
    private static String version_content;//app更新内容

    public static void appUpdate(Context context) {
        HashMap<String, String> map = new HashMap<>();
        map.put("type", MyApplication.getInstance().APP_TYPE);
        map.put("version", PublicUtils.getVersionName(context));
        Okhttp.get(false, UrlConstant.IS_NEED_UPDATA_URL, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
                Check = 2;
            }

            @Override
            public void onResponse(String response, int id) {
                parseJSON(response);
                Check = 2;
            }

            @Override
            public void onNoNetwork(String state) {
                Check = 2;
            }
        });
    }

    private static void parseJSON(String response) {
        try {
            JSONObject respose_json = new JSONObject(response);
            if (respose_json.optBoolean("success") && !respose_json.optString("data").equals("null")) {
                JSONObject data_json = respose_json.optJSONObject("data");
                downloadUrl = data_json.optString("version_down_url");
                version_content = data_json.optString("version_content");
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getDoorStatus() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mac", getMacAddress());
        map.put("tag", MyApplication.getInstance().getCustom());
        Okhttp.get(false, UrlConstant.BACK_DOOR_URL, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                josnDoor(response);
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    public static String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    private static void josnDoor(String response) {
        try {
            JSONObject response_json = new JSONObject(response);
            if (response_json.optBoolean("success")) {
                JSONObject data_json = response_json.optJSONObject("data");
                int machine_status = data_json.optInt("machine_status");
                MyApplication.getInstance().setDoorStatus(machine_status);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String TAG = "TAG";

    public static void systemUpdate() {
        HashMap<String, String> map = new HashMap<>();
        try {
            if (PublicUtils.getProperty("custom.system.type", TAG).equals(TAG) || PublicUtils.getProperty("custom.system.version", TAG).equals(TAG))
                return;
            map.put("type", PublicUtils.getProperty("custom.system.type", TAG));
            map.put("version", PublicUtils.getProperty("custom.system.version", TAG));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Okhttp.get(false, UrlConstant.IS_SYSTEM_NEED_UPDATA, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                parseSystemJSON(response);
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    private static String downSystemUrl;//系统下载地址
    private static String version_system_content;//系统更新内容

    private static void parseSystemJSON(String response) {
        try {
            JSONObject respose_json = new JSONObject(response);
            if (respose_json.optBoolean("success") && !respose_json.optString("data").equals("null")) {
                JSONObject data_json = respose_json.optJSONObject("data");
                downSystemUrl = data_json.optString("version_down_url");
                version_system_content = data_json.optString("version_content");
            } else {
//                Logutil.e(respose_json.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDownloadUrl() {
        return downloadUrl;
    }

    public static String getVersion_content() {
        return version_content;
    }

    public static String getDownSystemUrl() {
        return downSystemUrl;
    }

    public static String getVersion_system_content() {
        return version_system_content;
    }

    public static int getCheck() {
        return Check;
    }

    private static SilentlyToHand silentlyToHand;

    public static void setSilentlyToHand(SilentlyToHand silentlyToHand) {
        Silently.silentlyToHand = silentlyToHand;
    }

    private static List<HistoryList> historyList = new ArrayList<>();

    public static void setHistoryList(HistoryList list) {
        historyList.add(0, list);
        getList();
    }

    public static List<HistoryList> getHistoryLists() {
        return historyList;
    }

    public static void getList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", User.getUserId());
        map.put("size", "10");
        map.put("page", "1");
        Okhttp.get(false, UrlConstant.GETRUNRECORD, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) { //保存跑步记录数据及运动次数
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (historyList == null)
                            historyList = new ArrayList<>();
                        if (null != array && array.length() != 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo1 = array.getJSONObject(i);
                                HistoryList history = new HistoryList();
                                history.setRecord_id(jo1.optString("id"));
                                history.setStartTime(jo1.optString("starttime"));
                                history.setTotletime(jo1.optInt("totletime"));
                                history.setKaluli(jo1.optString("kaluli"));
                                history.setDistance(jo1.optString("distance"));
                                history.setPace(jo1.optString("speed"));
                                history.setXinlv(jo1.optString("xinlv"));
                                historyList.set(i, history);
                            }
                        }
                    } else
//                        if (silentlyToHand != null)
                    {
//                        silentlyToHand.end();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    public static void getHistoryList(final int page) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", User.getUserId());
//        map.put("user_id", "25");
        map.put("size", "10");
        map.put("page", page + "");
        Okhttp.get(false, UrlConstant.GETRUNRECORD, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) { //如何保存跑步记录数据及运动次数
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        if (page == 1 && historyList != null)
                            historyList = new ArrayList<>();
                        if (null != array && array.length() != 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo1 = array.getJSONObject(i);
                                HistoryList history = new HistoryList();
                                history.setRecord_id(jo1.optString("id"));
                                history.setStartTime(jo1.optString("starttime"));
                                history.setTotletime(jo1.optInt("totletime"));
                                history.setKaluli(jo1.optString("kaluli"));
                                history.setDistance(jo1.optString("distance"));
                                history.setPace(jo1.optString("speed"));
                                history.setXinlv(jo1.optString("xinlv"));
                                historyList.add(history);
                            }
//                            if (silentlyToHand != null && page == 1) {
//                                silentlyToHand.setAdap();
//                            }
                            if (array.length() == 10) {
                                getHistoryList(page + 1);
                            }
//                            else if (silentlyToHand != null)
//                                silentlyToHand.end();
                        }
                    } else
//                        if (silentlyToHand != null)
                    {
//                        silentlyToHand.end();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    public static void deleteHistoryList(final int record_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("record_id", historyList.get(record_id).getRecord_id());
        Okhttp.get(false, UrlConstant.DELETERUNRECORD, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
//                        historyList.remove(record_id);
                        silentlyToHand.setAdap(record_id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    public static List<int[][]> CUSTOM_TABLE = new ArrayList<>();
    public static List<String> CUSTOM_NAME = new ArrayList<>();
    private static int[][] custom;

    public static void getCustom(String... pame) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", User.getUserId());
//        map.put("user_id", "25");
        for (String temp : pame) {
            map.put(temp, "1");
        }
        Okhttp.get(false, UrlConstant.GETCOSTOMINFO, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Silently.getHistoryList(1);
                    JSONObject object = new JSONObject(response);
                    CUSTOM_TABLE = new ArrayList<>();
                    CUSTOM_NAME = new ArrayList<>();
                    if (object.getBoolean("success")) {
                        JSONObject array = object.getJSONObject("data");
                        JSONArray jsonArray = array.getJSONArray("infos");
                        for (int i = 0; i < 10; i++) {//总数据
                            if (TextUtils.isEmpty(jsonArray.getString(i * 2 + 1)) || jsonArray.getString(i * 2 + 1).equals("去掉"))
                                return;
                            else {
                                custom = new int[2][16];
                                String[] customdate = jsonArray.getString(i * 2 + 1).split(" ");
                                for (int j = 0; j < customdate.length / 2; j++) {//一组数据
                                    custom[0][j] = Integer.parseInt(customdate[j * 2]);
                                    custom[1][j] = Integer.parseInt(customdate[2 * (j + 1) - 1]);
                                }
                                CUSTOM_TABLE.add(custom);
                                CUSTOM_NAME.add(customdate[customdate.length - 1]);
//                            for(int[][] tmp:CUSTOM_TABLE){
//                                for (int j = 0; j < tmp.length; j++) {
//                                    for (int k = 0; k < tmp[1].length; k++) {
//                                        LogUtils.log(tmp[j][k]);
//                                    }
//                                }
//                            }
                            }
                        }
                        if (silentToCustiom != null) {
                            silentToCustiom.setDataUpdate();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    private static SilentToCustom silentToCustiom;
    private static int customType = 1;

    public static void setIsDelete(boolean isDeletes) {
        isDelete = isDeletes;
    }

    public static boolean isTiHuan = false;
    private static boolean isDelete = false;

    public static void setCustomType(int indexsss) {
        customType = indexsss;
    }

    public static int getCustomType() {
        return customType;
    }

    private static int postion;

    public static void setCustom(final String customContent, final int[] speedsss, final int[] slopedsss, final String na) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", User.getUserId());
        map.put("info" + customType, customContent);
        Okhttp.get(false, UrlConstant.SETCOSTOMINFO, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {
//                        CUSTOM_TABLE.add(new int[][]{speedsss, slopedsss});
//                        CUSTOM_NAME.add(na);
                        if (customType == CUSTOM_NAME.size() + 1) {//添加
                            CUSTOM_TABLE.add(new int[][]{speedsss, slopedsss});
                            CUSTOM_NAME.add(na);
                        } else if (customType <= CUSTOM_NAME.size()) {
                            if (isDelete) {//删除
                                isDelete = false;
                                CUSTOM_TABLE.remove(customType - 1);
                                CUSTOM_NAME.remove(customType - 1);
                                postion = customType;
                                new Thread() {//服务器归位
                                    @Override
                                    public void run() {
                                        super.run();
                                        for (; postion <= CUSTOM_NAME.size(); postion++) {
                                            StringBuffer buffer = new StringBuffer();
                                            for (int j = 0; j < speedsss.length; j++) {
                                                buffer.append(CUSTOM_TABLE.get(postion - 1)[0][j] + " ");
                                                buffer.append(CUSTOM_TABLE.get(postion - 1)[1][j] + " ");
                                            }
                                            setCustom(postion, buffer.toString() + CUSTOM_NAME.get(postion - 1));
                                            try {
                                                Thread.sleep(500);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        setCustom(postion, "去掉");
                                    }
                                }.start();
//                                deleteCustom();
                            } else {//更改
                                CUSTOM_TABLE.set(customType - 1, new int[][]{speedsss, slopedsss});
                                CUSTOM_NAME.set(customType - 1, na);
                            }
                        }
                        if (silentToCustiom != null) {//更新
                            silentToCustiom.setDataUpdate();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }//private static void deleteCustom() {}

    private static void setCustom(int indexs, String customContent) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", User.getUserId());
        map.put("info" + indexs, customContent);
        Okhttp.get(false, UrlConstant.SETCOSTOMINFO, map, new Okhttp.CallBac() {
            @Override
            public void onError(Call call, Exception e, String state, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.optBoolean("success")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNoNetwork(String state) {
            }
        });
    }

    public static void setSilentToCustiom(SilentToCustom silentToCustiom) {
        Silently.silentToCustiom = silentToCustiom;
    }
}