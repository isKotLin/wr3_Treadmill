package com.vigorchip.treadmill.module;


import android.os.Parcel;
import android.os.Parcelable;

public class Pickers implements Parcelable {
    private String showConetnt;

    protected Pickers(Parcel in) {
        showConetnt = in.readString();
    }

    public static final Creator<Pickers> CREATOR = new Creator<Pickers>() {
        @Override
        public Pickers createFromParcel(Parcel in) {
            return new Pickers(in);
        }

        @Override
        public Pickers[] newArray(int size) {
            return new Pickers[size];
        }
    };

    public String getShowConetnt() {
        return showConetnt;
    }

    public Pickers(String showConetnt) {
        super();
        this.showConetnt = showConetnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(showConetnt);
    }
}
/**
 * @author zengtao 2015年5月20日下午7:18:14
 */
//public class Pickers implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private String showConetnt;
//    private String showId;
//
//    public String getShowConetnt() {
//        return showConetnt;
//    }
//
//    public String getShowId() {
//        return showId;
//    }
//
//    public Pickers(String showConetnt, String showId) {
//        super();
//        this.showConetnt = showConetnt;
//        this.showId = showId;
//    }
//
//    public Pickers() {
//        super();
//    }
//}