package com.globalformulae.shiguang.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.globalformulae.shiguang.model.NBAEvents;
import com.globalformulae.shiguang.model.ResponseBean;
import com.globalformulae.shiguang.model.User;
import com.globalformulae.shiguang.model.WeatherBean24h;
import com.globalformulae.shiguang.model.WeatherBean7D;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ZXG on 2017/2/27.
 */

public class OkHttpUtil {
    private final static int
            REQUEST_SUCC = 0, //连接网络成功，获得返回的数据或错误信息
            REQUEST_FAIL = 1; //连接网络失败

    /**
     * 服务器返回的数据
     * code:状态代码
     * message:状态信息
     * data:数据
     * 状态代码对应信息
     * 400：错误(传入参数、验证码等错误)
     * 401：未授权，无权限
     * 200: 成功
     * 503：错误(数据库错误)
     */
    private static final String NBAEVENTS_URL="http://op.juhe.cn/onebox/basketball/nba?key=15cf3c6dd41abb470c21f9161b93de54";
    private static final String SCHOOL_GENIMG="http://210.42.121.132/servlet/GenImg";
    private static final String SCHOOL_LOGINURL = "http://210.42.121.132/servlet/Login";
    public static final String BASEURL="http://121.42.140.71:8080/shiguangServer/";
    //public static final String BASEURL="http://192.168.191.5:8080/shiguangServer/";
    private static final String REGISTURL="regist";
    private static final String LOGINURL="login";
    public static final String GETFRIENDINFO="doGetFriendInfo";
    public static final String STEALPOWER="doStealPower";
    private static final String IDENTIFYCODEURL="sendIdentifyCode";
    public static final String GETRECORD="doGetRecord";
    private static OkHttpUtil okHttpUtil=new OkHttpUtil();
    private static OkHttpClient okHttpClient;
    private OkHttpUtil(){}
    private StringBuilder mResult=new StringBuilder();
    private NBAEvents nbaEvents;


    public static OkHttpUtil getInstance(){
        if(okHttpClient==null)
            okHttpClient=new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .cookieJar(new CookieJar() {
                        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();

                        @Override
                        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                            cookieStore.put(url.host(), cookies);
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl url) {
                            List<Cookie> cookies = cookieStore.get(url.host());
                            return cookies != null ? cookies : new ArrayList<Cookie>();
                        }
                    })
                    .build();
        return okHttpUtil;
    }




    public NBAEvents NbaEvents(){
                Request.Builder builder=new Request.Builder();  //发起请求
                Request request=builder.get().url(NBAEVENTS_URL).build(); //构造者模式
                Call call=okHttpClient.newCall(request);
                Response response= null;
                try {
                    response = call.execute();
                    Gson gson=new Gson();
                    String json=response.body().string();
                    nbaEvents=gson.fromJson(json, NBAEvents.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        return nbaEvents;
    }







    public StringBuilder getmResult() {
        return mResult;
    }

    public void setmResult(StringBuilder mResult) {
        this.mResult = mResult;
    }

    public void setNbaEvents(NBAEvents nbaEvents) {
        this.nbaEvents = nbaEvents;
    }

    public NBAEvents getNbaEvents() {
        return nbaEvents;
    }

    private static class MyHandler extends Handler {
        private WeakReference<NetServiceUser> mNetServiceUser;

        public MyHandler(NetServiceUser netServiceUser) {
            mNetServiceUser = new WeakReference<NetServiceUser>(netServiceUser);
        }

        @Override
        public void handleMessage(Message msg) {
            NetServiceUser netServiceUser = mNetServiceUser.get();
            if (netServiceUser == null) { //如果目标user已经被销毁，则不再传回数据
                Log.wtf("目标回调函数已被销毁","null");
                return;
            }
            switch (msg.arg1) {
                case REQUEST_SUCC:
                    ResponseBean responseData = (ResponseBean) msg.obj;
                    if (responseData.getCode().equals("200")) {
                        netServiceUser.connectedcallBack(
                                NetServiceUser.CONNECT_SUCC, msg.arg2, responseData.getData(), responseData.getMessage());
                    } else {
                        netServiceUser.connectedcallBack(
                                NetServiceUser.CONNECT_ERROR, msg.arg2, responseData.getData(), responseData.getMessage());
                    }
                    break;
                case REQUEST_FAIL:
                    netServiceUser.connectedcallBack(
                            NetServiceUser.CONNECT_FAIL, msg.arg2, "", (String) msg.obj);
                    break;
            }
        }
    }

    public void connectForPost(final int type, RequestBody requestBody, String url, final NetServiceUser netServiceuser) {
        final MyHandler myHandler = new MyHandler(netServiceuser);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .post(requestBody)
                .build();
        Log.wtf("POST来自：" + netServiceuser.getClass(), url);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.obj = "无法连接到网络,请检查网络链接";
                message.arg1 = REQUEST_FAIL;
                message.arg2 = type;
                myHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Message message = Message.obtain();
                Log.wtf("【Net】", call.request().url() + ":" + result);
                message.arg2 = type;
                if (response.code() >= 200 && response.code() < 300) {
                    try {
                        message.arg1 = REQUEST_SUCC;
                        ResponseBean rb = new Gson().fromJson(result, new TypeToken<ResponseBean>() {
                        }.getType());
                        message.obj = rb;
                    } catch (Exception e) {
                        e.printStackTrace();
                        message.arg1 = REQUEST_FAIL;
                        message.obj = "操作成功，但返回数据有错误，解析失败";
                    }
                } else {
                    message.arg1 = REQUEST_FAIL;
                    message.obj = "网络错误，错误代码：" + Integer.toString(response.code());
                }
                myHandler.sendMessage(message);
            }
        });
    }

    public void connectForGet(final int type, String url, final NetServiceUser netServiceuser) {
        final MyHandler myHandler = new MyHandler(netServiceuser);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .build();
        Log.wtf("GET来自：" + netServiceuser.getClass(), url);
//        Log.wtf("GETHeader：" + netServiceuser.getClass(), request.headers().toString());
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.obj = "无法连接到网络,请检查网络链接";
                message.arg1 = REQUEST_FAIL;
                message.arg2 = type;
                myHandler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Message message = Message.obtain();
                message.arg2 = type;
//                Log.wtf("GETheader",response.headers().toString());
                if (response.code() >= 200 && response.code() < 300) {
                    try {
                        message.arg1 = REQUEST_SUCC;
                        ResponseBean rb = new Gson().fromJson(result, new TypeToken<ResponseBean>() {
                        }.getType());
                        message.obj = rb;
                    } catch (Exception e) {
                        e.printStackTrace();
                        message.arg1 = REQUEST_FAIL;
                        message.obj = "操作成功，但返回数据有错误，解析失败";
                    }
                } else {
                    message.arg1 = REQUEST_FAIL;
                    message.obj = "网络错误，错误代码：" + Integer.toString(response.code());
                }
                myHandler.sendMessage(message);
            }
        });
    }

    public Response getGenImg(){
        Request.Builder builder=new Request.Builder();  //发起请求
        Request request=builder.get().url(SCHOOL_GENIMG).build(); //构造者模式
        Call call=okHttpClient.newCall(request);
        Response response= null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response loginSchool(String studentid,String password,String xdvfb,String cookie,String userAgent) throws UnsupportedEncodingException {
        FormBody.Builder requestBodyBuilder=new FormBody.Builder();
        FormBody formBody=requestBodyBuilder
                .add("id", URLEncoder.encode(studentid, "utf8"))
                .add("pwd",URLEncoder.encode(password, "utf8"))
                .add("xdvfb",URLEncoder.encode(xdvfb, "utf8"))
                .build();
        Request.Builder builder=new Request.Builder();
        Request request=builder.url(SCHOOL_LOGINURL)
                .addHeader("User-Agent",userAgent)
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .addHeader("Accept-Encoding", "deflate")
                .addHeader("Referer","http://210.42.121.132/")
                .addHeader("Cookie",cookie)
                .addHeader("Connection","keep-alive")
                .addHeader("Upgrade-Insecure-Requests","1")
                .post(formBody).build();
        Log.e("777",request.headers().toString());
        Call call=okHttpClient.newCall(request);
        Response response= null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * 通知服务器发送验证码，并返回是否发送成功
     * @param phone：手机号
     * @return
     */
    public void getIdentifyCode(String phone){
        FormBody.Builder builder=new FormBody.Builder();
        FormBody formBody=builder
                .add("phone",phone)
                .build();
        Request.Builder builder1=new Request.Builder();
        Request request=builder1
                .url(BASEURL+IDENTIFYCODEURL)
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseBean responseBean=new ResponseBean();
                responseBean.setCode(ResponseBean.GET_IDENTIFY_CODE_FAIL);
                EventBus.getDefault().post(responseBean);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBean responseBean=new ResponseBean();
                //数据库中已经有了帐号,或者是服务器那边发送失败了
                String result=response.body().string().trim();
                Log.e("ccc", result);
                if(result.equals("-1")||result.equals("0")){
                    responseBean.setCode(ResponseBean.GET_IDENTIFY_CODE_FAIL);
                    EventBus.getDefault().post(responseBean);
                }else{
                    responseBean.setCode(ResponseBean.GET_IDENTIFY_CODE_SUCC);
                    EventBus.getDefault().post(responseBean);
                }

            }
        });
    }

    /**
     * 注册帐号
     * @param phone
     * @param password
     * @param identifyCodeC
     * @param name
     */
    public void registShiguang(String phone,String password,String identifyCodeC,String name){
        FormBody formBody=new FormBody.Builder()
                .add("phone",phone)
                .add("password",password)
                .add("identifyCodeC",identifyCodeC)
                .add("name",name)
                .build();
        Request.Builder builder=new Request.Builder();
        Request request=builder
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                .url(BASEURL+REGISTURL)
                .post(formBody)
                .build();

        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("responseBean:","failure" );
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBean responseBean=new ResponseBean();
                    if(response.code()==200){
                        String result=response.body().string();
                        Log.e("result200:",result);
                        Gson gson=new Gson();

                        responseBean.setCode(ResponseBean.REGIST_SUCC);
                        User user=gson.fromJson(result,User.class);
                        responseBean.setUser(user);

                        Log.e("responseBean:",responseBean.toString() );
                    }else if(response.code()==201){
                        responseBean.setCode(ResponseBean.REGIST_FAIL);
                        responseBean.setMessage("201");//验证码错误
                        Log.e("responseBean:","201" );
                    }else if(response.code()==202){
                        responseBean.setCode(ResponseBean.REGIST_FAIL);
                        responseBean.setMessage("202");//手机号和接受短信的手机号不一致
                        Log.e("responseBean:","202" );
                    }else{
                        //服务器那边的错误
                        responseBean.setCode(ResponseBean.REGIST_FAIL);
                        responseBean.setMessage("203");//
                        Log.e("responseBean:","error" );
                    }
                    EventBus.getDefault().post(responseBean);
                }
            });




    }

    public void loginShiguang(String phone,String password){
        FormBody formBody=new FormBody.Builder()
                .add("phone",phone)
                .add("password",password)
                .build();
        Request request=new Request.Builder()
                .url(BASEURL+LOGINURL)
                .post(formBody)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBean responseBean=new ResponseBean();
                if(response.code()==200){
                    String result=response.body().string();
                    Log.e("result200:",result);
                    Gson gson=new Gson();

                    responseBean.setCode(ResponseBean.LOGIN_SUCC);
                    User user=gson.fromJson(result,User.class);
                    responseBean.setUser(user);
                    Log.e("responseBean:",responseBean.toString() );
                }else if(response.code()==201){
                    responseBean.setCode(ResponseBean.LOGIN_FAIL);
                    responseBean.setMessage("201");//密码错误
                    Log.e("responseBean:","201" );
                }else if(response.code()==202){
                    responseBean.setCode(ResponseBean.LOGIN_FAIL);
                    responseBean.setMessage("202");//帐号不存在
                    Log.e("responseBean:","202" );
                }else {
                    //服务器那边的错误
                    responseBean.setCode(ResponseBean.LOGIN_FAIL);
                    responseBean.setMessage("203");//
                    Log.e("responseBean:", "error");
                }
                EventBus.getDefault().post(responseBean);
            }
        });
    }

    public void getWeather(Request request){
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hhh", "zheli?");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Gson gson=new Gson();
                WeatherBean24h weatherBean24h=gson.fromJson(result, WeatherBean24h.class);
                if(weatherBean24h!=null)
                    EventBus.getDefault().post(weatherBean24h);
                else
                    Log.e("hhh", "onResponse: ");
            }
        });
    }

    public void getWeather7d(Request request){
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("hhh", "zheli?");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result=response.body().string();
                Gson gson=new Gson();
                WeatherBean7D weatherBean7d=gson.fromJson(result, WeatherBean7D.class);
                if(weatherBean7d!=null)
                    EventBus.getDefault().post(weatherBean7d);
                else
                    Log.e("hhh", "onResponse: ");
            }
        });
    }




}

