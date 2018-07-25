package com.vigorchip.treadmill.module;

/**
 * 跑步记录
 */
public class HistoryList {
    private String startTime;//日期
    private int totletime;//跑步时长
    private String pace;//配速、速度
    private String record_id;
    private String distance;//路程
    private String kaluli;//卡路里
    private String xinlv;//心率

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getTotletime() {
        return totletime;
    }

    public void setTotletime(int totletime) {
        this.totletime = totletime;
    }

    public String getPace() {
        return pace;
    }

    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getKaluli() {
        return kaluli;
    }

    public void setKaluli(String kaluli) {
        this.kaluli = kaluli;
    }

    public String getXinlv() {
        return xinlv;
    }

    public void setXinlv(String xinlv) {
        this.xinlv = xinlv;
    }

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }
}
