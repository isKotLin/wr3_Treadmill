package com.vigorchip.treadmill.module;

/**
 * 用户
 */
public class User {
//    private static String userId = "-1";//用户id
//    private static int sex = 0;//用户性别
//    private static String ill = "12345";//用户病
//    private static String userName = "无名氏";//用户昵称
//    private static String userPhone = "00000000000";//手机号
//    private static int userAge = 15;//用户年龄
//    private static int userheight = 170;//用户身高
//    private static int userWeight = 60;//用户体重
//    private static int userHeart = 123;//用户心率

    private static String userId = "-1";//用户id
    private static int sex;//用户性别
    private static String ill = "0";//用户病
    private static String userName;//用户昵称
    private static String userPhone;//手机号
    private static int userAge;//用户年龄
    private static int userheight;//用户身高
    private static int userWeight;//用户体重
    private static int userHeart;//用户心率

    public static String getIll() {
        return ill;
    }

    public static void setIll(String ill) {
        User.ill = ill;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        User.userPhone = userPhone;
    }

    public static int getUserAge() {
        return userAge;
    }

    public static void setUserAge(int userAge) {
        User.userAge = userAge;
    }

    public static int getUserheight() {
        return userheight;
    }

    public static void setUserheight(int userheight) {
        User.userheight = userheight;
    }

    public static int getUserWeight() {
        return userWeight;
    }

    public static int getTimeUserWeight() {
        return userWeight <= 30 ? 60 : userWeight;
    }

    public static void setUserWeight(int userWeight) {
        User.userWeight = userWeight;
    }

    public static int getUserHeart() {
        return userHeart;
    }

    public static void setUserHeart(int userHeart) {
        User.userHeart = userHeart;
    }


    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        User.userId = userId;
    }

    public static int getSex() {
        return sex;
    }

    public static void setSex(int sex) {
        User.sex = sex;
    }
}
