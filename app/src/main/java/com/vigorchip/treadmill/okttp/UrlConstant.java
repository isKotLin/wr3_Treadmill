package com.vigorchip.treadmill.okttp;

public class UrlConstant {
    public static String BaseUrl="http://39.108.225.155/vc_tp/Home/Api/";

    public static String IS_NEED_UPDATA_URL=BaseUrl+"isNeedUpdata";//app升级
    public static String BACK_DOOR_URL=BaseUrl+"getRunMechinaStatus";//后门
    public static String IS_SYSTEM_NEED_UPDATA=BaseUrl+"isSystemNeedUpdata";//系统升级
    public static String REGISTER=BaseUrl+"register";//注册
    public static String LOGIN=BaseUrl+"login";//登录
    public static String GETINFO=BaseUrl+"getInfo";//得到个人信息
    public static String COMPELETE=BaseUrl+"compelete";//完成个人信息
    public static String UPDATA_RUNRECORD=BaseUrl+"updataRunrecord";//上传跑步记录信息
    public static String GETRUNRECORD=BaseUrl+"getRunRecord";//获取跑步列表数据
    public static String GETCOSTOMINFO=BaseUrl+"getCostomInfo";//获取自定义数据
    public static String SETCOSTOMINFO=BaseUrl+"setCostomInfo";//上传数据
    public static String DELETERUNRECORD=BaseUrl+"deleteRunRecord";//上传数据
//    app升级接口
//            (/isNeedUpdata)
//    参数   type类型,区别不同机器和厂家     version版本好,类似于1.0.1
//
//
//    系统升级接口
//            (/isSystemNeedUpdata)
//    参数   type类型,区别不同机器和厂家     version版本好,类似于1.0.1
//
//
//    得到机型运行状态
//            (/getRunMechinaStatus)
//    参数   mac物理地址   tag类别,区别不同机器和厂家
//
//    注册
//            (/register)
//    参数   phone手机号(后台没做校验)  password密码
//
//
//    登录
//            (/login)
//    phone手机号(后台没做校验)  password密码
//
//    得到个人信息
//            (/getInfo)
//    参数 user_id
//
//    完成个人信息
//            (/compelete)
//    user_id用户id   age年龄 nickname 昵称  tall身高   weight体重  xinlv心率  sex性别   illness病信息数据(只做保存,不做修改)
//
//    上传自定义数据
//            (/setCostomInfo)
//    参数:user_id 用户id   info1 到info10   分别保存不同的字符串,由客户端自定义
//
//
//    下载自定义数据
//            (/getCostomInfo)
//    参数:user_id 用户id   要那个数据就传info1=1
//
//    上传跑步记录信息
//            (/updataRunrecord)
//    user_id用户id     starttime开始运行时间  totletime总共运行时间   speed速度   distance距离  kaluli卡路里  xinlv心律
//
//    获取跑步列表数据
//            (/getRunRecord)
//    user_id用户id   size请求条数   page第几页(从1开始)
//
//    删除跑步列表数据
//           (/deleteRunRecord)
//     record_id 条
//    本地服务器域名地址:http://localhost/vc_tp/Home/Api/    +接口url
//    正式服务器        :http://39.108.225.155/vc_tp/Home/Api/ +接口url
}
