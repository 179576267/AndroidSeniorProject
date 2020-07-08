package com.wzf.base.arouter.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;

import com.alibaba.android.arouter.launcher.ARouter;
import com.demo.base.utils.ActivityCollector;
import com.demo.base.utils.DebugLog;
import com.demo.base.utils.StringUtils;
import com.wzf.base.arouter.service.BaseServiceProvider;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class WsRouter {

//    路由规则（对应我是谜APP v2.0.0版本）
//
//    格式：
//            [https|http]://[host]/[path]?wsmtypespecial=[type]&hideshare=[value]&content=[key-value Base64]
//
//    eg: https://www.mvoicer.com/700001?wsmtypespecial=1&content=aWQ9NjQ1
//    本地页面（wsmtypespecial=1），path = 700001（广场详情页面），content=id=645
//
//    详细说明：
//            0.[host]可以后台任意配置，不做校验位，[path]是校验位，wsmtypespecial 是校验字段
//
//      1.wsmtypespecial=1代表本地页面，不传或者其他值代表是网页链接
//
//      2.hideshare=1代表隐藏网页分享，仅用于网页控制网页在app中是否可以分享到外网，hindeshare不传则默认显示分享
//
//      3.content为具体数据格式为[key=value&key=value],（没有参数可以不传）
//            []内容数据需要base64（ url 后面参数base64后要将+/换成-*，decode的时候需要先将-*换成+/，再进行decode。）
//
//            4.测试可以用banner里面配置相应的链接测试页面正确性

    //case ROUTER_USER_INFO_DETAIL:
    //mvoicerwsm://route/1?userGuid=1542960546321kszweermbzcu0lgqe9t
    static String TAG = "WsRouter";
    //用户详情
    public static final String ROUTER_USER_INFO_DETAIL = "1";
    public static final String ROUTER_USER_INFO_SQUARE_ITEM_DETAIL = "2";

    public static void parse(Context context, String url) {
        parse(context, url, true);
    }

    public static void parse(Context context, String url, boolean isDecode) {
        if (url == null || "".equals(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        if (uri == null) {
            return;
        }

        try {
            //私有路由
            if (url.toLowerCase().startsWith("mvoicerwsm://")) {
                parseSchemeNative(context, uri);
            } else {
                parseUri(context, uri, isDecode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String routerTopicUrlGenerator(long id){
//        https://www.mvoicer.com/700003?wsmtypespecial=1&content=squareHotTopicId=1002
        return encodeRouteUrl("700003","squareHotTopicId="+id);
    }
    public static String encodeRouteUrl(String param, String query) {
        StringBuilder sb = new StringBuilder();
        sb.append("https://www.mvoicer.com/");
        sb.append(param);
        sb.append("?wsmtypespecial=1");
        if (query != null) {
            String encode = new String(Base64.encode(query.getBytes(), Base64.DEFAULT));
            encode = encode.replace("+", "-");
            encode = encode.replace("/", "*");
            sb.append("&content=");
            sb.append(encode);
        }
        return sb.toString();
    }
    private static RouterMap getUrlParams(String query, boolean isDecode) {
        RouterMap routerMap = new RouterMap();
        if (query == null) {
            return routerMap;
        }
        try {
            //   标准base64后将+/换成-*，然后需要decode的时候，先将-*换成+/，再进行decode。

//            String oQuery = query;
            if (isDecode) {
                query = query.replace("-", "+");
                query = query.replace("*", "/");
                query = new String(Base64.decode(query.getBytes(), Base64.DEFAULT));
            }
            if (query.length() > 0) {
                final String[] pairs = query.split("&");
                for (String pair : pairs) {
                    final int idx = pair.indexOf("=");
                    //如果等号存在且不在字符串两端，取出key、value
                    if (idx > 0 && idx < pair.length() - 1) {
                        final String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                        final String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                        routerMap.put(key, value);
                    }
                }
            }

//            if (routerMap.isEmpty()){
//                if (oQuery.length() > 0) {
//                    final String[] pairs = oQuery.split("&");
//                    for (String pair : pairs) {
//                        final int idx = pair.indexOf("=");
//                        //如果等号存在且不在字符串两端，取出key、value
//                        if (idx > 0 && idx < pair.length() - 1) {
//                            final String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
//                            final String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
//                            routerMap.put(key, value);
//                        }
//                    }
//                }
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return routerMap;
    }

    private static void parseUri(Context context, Uri uri, boolean isDecode) {

        String type = uri.getQueryParameter("wsmtypespecial");
        String content = uri.getQueryParameter("content");
        //网页
        String hideshare = uri.getQueryParameter("hideshare");
        boolean hide = false;
        if ("1".equals(hideshare) || "true".equalsIgnoreCase(hideshare)) {
            hide = true;
        }


        String path = null;
        List<String> pathSegments = uri.getPathSegments();

        if (pathSegments != null && pathSegments.size() > 0) {

            path = pathSegments.get(pathSegments.size() - 1);
        }
        //本地页面
        if ("1".equals(type) && path != null) {

            //参数映射
            RouterMap routerMap;
            if (content != null) {
                routerMap = getUrlParams(content, isDecode);
            } else {
                routerMap = new RouterMap();
            }

            if (parseNative(context, path, routerMap,hide)) {
                return;
            }
        }
        String url = uri.toString();
        if (url != null && url.startsWith("http")) {
            WsRouterParser.intoWeb(context, url, hide);
        }
    }


    //广场动态详情
    public static final String ROUTER_SQUARE_DETAIL = "700001";
    public static final String ROUTER_SQUARE_TOPIC_DETAIL = "700003";
    //
    public static final String ROUTER_SQUARE_PUBLISH = "700002";
    public static final String ROUTER_PRIVATE_ROOM = "300001";
    public static final String ROUTER_TALK_ROOM = "310001";
    public static final String ROUTER_TALK_DOUBLE_ROOM = "320001";
    public static final String ROUTER_GAME_ROOM = "210003";
    //剧本详情页
    public static final String ROUTER_DRAMA_DETAIL = "200002";
    //剧本列表页（剧本库）
    public static final String ROUTER_DRAMA_HALL = "200001";
    //首页进入的活动页
    public static final String ROUTER_ACTIVE_LIST = "110105";
    //用户详情页
    public static final String ROUTER_USER_DETAIL = "190001";
    //排行榜
    public static final String ROUTER_RANK_LIST = "110101";
    //主页
    public static final String ROUTER_HOME_ = "100000";
    //谜团详情页
    public static final String ROUTER_MYSTERY_DETAIL_ = "183002";
    //房间大厅
    public static final String ROUTER_GAME_HALL = "200003";
    //cp页
    public static final String ROUTER_CP_SHOW = "181003";
    //月卡签到
    public static final String ROUTER_MALL_MONTH_SIGN = "181002";
    //通知消息
    public static final String ROUTER_NOTIFY_INFO = "900001";
    //狗头侦探
    public static final String ROUTER_NOTIFY_SYSTEM_INFO = "900006";
    //邀请消息
    public static final String ROUTER_NOTIFY_INVITE_INFO = "900003";
    //评论消息
    public static final String ROUTER_NOTIFY_SQUARE_INFO = "900002";
    //俱乐部详情
    public static final String ROUTER_CLUB_DETAIL = "183003";
    //每日签到
    public static final String ROUTER_DAY_SIGN = "181001";
    //新手引导
    public static final String ROUTER_NEWER_GUIDE = "181100";
    //小资料卡
    public static final String ROUTER_USER_INFO = "191900";
    //谁是卧底
    public static final String ROUTER_GAME_FIND_HIDER = "250000";
    //狼人杀
    public static final String ROUTER_GAME_WOLF = "260000";
    //你画我猜
    public static final String ROUTER_GAME_DRAW_PAINT = "270000";

    private static boolean parseNative(Context context, String path, RouterMap routerMap,boolean hide) {


        //新首页
        //100000:首页 100001：包房列表  100002:语聊 100003:双人房
        // 100004：谜团 100005：俱乐部 100006：广场 100007：商城 100008：消息
        if (path != null && path.length() == 6 && path.startsWith("10000")) {


//            String[] routers = routerMap.getStringArray("router");

            List<String> list = new ArrayList<>();
            if ("100000".equals(path)) {
                list.add("1");
            } else if ("100001".equals(path)) {
                list.add("2");
                list.add("1");
            } else if ("100002".equals(path)) {
                list.add("2");
                list.add("2");
                list.add("2");
            } else if ("100003".equals(path)) {
                list.add("2");
                list.add("2");
                list.add("1");
            } else if ("100004".equals(path)) {
                list.add("2");
                list.add("3");

            } else if ("100005".equals(path)) {
                list.add("2");
                list.add("4");
            } else if ("100006".equals(path)) {
                list.add("3");
            } else if ("100007".equals(path)) {
                list.add("4");
            } else if ("100008".equals(path)) {
                list.add("5");
            }

            int size = list.size();
            if (size > 0) {
                String[] routers = new String[size];
                for (int i = 0; i < size; i++) {
                    routers[i] = list.get(i);
                }
            } else {
            }
            return true;
        }

        if ("182002".equals(path)) {
            String type = routerMap.get("type");
//            WsRouterParser.intoWeb(context, UserContext.FQ_URL, true);
            return true;
        }
        if ("180000".equals(path)) {
            String urlStr = routerMap.get("urlStr");
            if (urlStr != null && !"".equals(urlStr)) {
                WsRouterParser.intoWeb(context,urlStr,hide);
                return true;
            }
        }

        //  广场详情
        //  https://www.mvoicer.com/700001?wsmtypespecial=1&content=[id=645]
        //  https://www.mvoicer.com/700001?wsmtypespecial=1&content=aWQ9NjQ1
        if (ROUTER_SQUARE_DETAIL.equals(path) && routerMap.hasValue()) {
            long square_base_id = routerMap.getLong("id", 0);
            if (square_base_id > 0) {
                return true;
            }
        }


        return false;
    }

    public static final String URL_NOTIFY_SYSTEM = "https://www.mvoicer.com/900006?wsmtypespecial=1";
    public static final String URL_NOTIFY_INFO = "https://www.mvoicer.com/900001?wsmtypespecial=1";
    public static final String URL_NOTIFY_INVITE_INFO = "https://www.mvoicer.com/900003?wsmtypespecial=1";
    public static final String URL_NOTIFY_SQUARE_INFO = "https://www.mvoicer.com/900002?wsmtypespecial=1";

    public static void parseScheme(Context context, Intent intent,boolean justCache) {
        try {

            if (intent == null) {
                return;
            }
            String intentAction = intent.getAction();
            if (Intent.ACTION_VIEW.equals(intentAction)) {
                Uri uri = intent.getData();
                if (uri == null) {
                    return;
                }

                Activity currentActivity = ActivityCollector.getCurrentActivity();
                //将路由缓存
                if (justCache || currentActivity == null
                        ) {

                    addRouterTask(uri.toString());
                    return;
                }
                parseSchemeNative(context, uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void parseSchemeNative(Context context, Uri uri) {

        if (uri == null) {
            return;
        }
        DebugLog.i("SCHEME", "initIntentData-scheme: " + uri.getScheme());
        DebugLog.i("SCHEME", "initIntentData-host: " + uri.getHost());

        String host = uri.getHost();
        if (host == null) {
            return;
        }
        if ("mystery".equals(host)) {
            String guildid = uri.getQueryParameter("guildid");

            DebugLog.i("SCHEME", "initIntentData-guildid: " + guildid);
            int id = getInt(guildid);

        }else {
            parseUri(context, uri, true);
        }


    }
    public static LinkedList<String> mRouterTask = new LinkedList<>();

    public static void addRouterTask(String router) {
        mRouterTask.addFirst(router);
    }


    public static synchronized void consumerRouterTasks(Context context) {
        while (!mRouterTask.isEmpty()) {
            String first = mRouterTask.removeFirst();
            if (first != null) {
                parse(context, first);
            }
        }
    }

    public static int getInt(String value) {


        if (value != null) {
            try {
                return Integer.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    public static BaseServiceProvider getService(String path){
        return (BaseServiceProvider) ARouter.getInstance()
                .build(path).navigation();
    }

}
