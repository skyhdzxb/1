package com.helloar.utils;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;


/**
 * Created by 21046 on 2017/7/25.
 */
public class SmsUtils {
    private static String appId = "AoBM355wvbY4NHD7AAa22na3-gzGzoHsz";
    private static String appKey = "KRXnrouSc0Y5cYrgzwfaGEdL";

    public static String sendSms(String phoneNum) {
        LinkedHashMap<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-LC-Id", appId);
        headers.put("X-LC-Key", appKey);
        HttpPost httpPost = new HttpPost("https://api.leancloud.cn/1.1/requestSmsCode");
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }
        String data = "{\"mobilePhoneNumber\":"+"\""+phoneNum+"\""+"}";
        StringEntity stringEntity = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            stringEntity = new StringEntity(data);
            httpPost.setEntity(stringEntity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().toString().contains("200")){
                return "success";
            }
        } catch (UnsupportedEncodingException e) {
            return  "fail";
        } catch (IOException e) {
          return  "fail";
        }
        return "success";
    }
    /**
     * 校验验证码
     * @param phoneNumber ：用户手机号
     * @param verifySmsCode ：验证码
     * @return
     */
    public static String verifySmsCode(String phoneNumber,String verifySmsCode) {
        LinkedHashMap<String,String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-LC-Id", appId);
        headers.put("X-LC-Key", appKey);
        HttpPost httpPost = new HttpPost("https://api.leancloud.cn/1.1/verifySmsCode/"
                +verifySmsCode+"?mobilePhoneNumber="+phoneNumber);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        for (String key : headers.keySet()) {
            httpPost.setHeader(key, headers.get(key));
        }
        try {
            HttpResponse response =httpClient.execute(httpPost);
            String msg = EntityUtils.toString(response.getEntity());
            if (msg.contains("error")){
                return "验证码错误";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "服务器异常";
        }
        return "检验成功";
    }
}

