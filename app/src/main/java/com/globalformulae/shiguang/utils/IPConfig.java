package com.globalformulae.shiguang.utils;

import android.app.Activity;
import android.content.res.XmlResourceParser;

import com.globalformulae.shiguang.R;

import java.util.HashMap;

/**
 * Description:操作存储在/res/values/ipconfig下的服务器信息
 */
public class IPConfig {

    private static HashMap<String, String> hashMap = new HashMap<String, String>();

    /**
     * 描述: 获取ipconfig.xml中的数据,将其存放在hashMap
     */
    public static void getIPConfig(Activity activity) {
        XmlResourceParser xrp = activity.getResources().getXml(R.xml.ipconfig);
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    String tagname = xrp.getName();
                    if (tagname.equals("service")) {
                        hashMap.put(xrp.getAttributeName(0), xrp.getAttributeValue(0));
                    }
                }
                xrp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * @param methodName  要调用的服务器方法名
     * @return 返回该方法名的url地址
     */
    public static String getUrl(String methodName){
        return hashMap.get(methodName);
    }
}
