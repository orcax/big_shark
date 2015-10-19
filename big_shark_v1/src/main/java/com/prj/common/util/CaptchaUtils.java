package com.prj.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.type.TypeReference;

public class CaptchaUtils {

    private static final String APIKEY   = "924b91cae300188f5d6883598f7bb9e3";

    private static final String TPL      = "【投资之家】您的验证码为%s";

    private static final String URL      = "http://yunpian.com/v1/sms/send.json";

    private static final String ENCODING = "UTF-8";

    public static Boolean send(String mobile, String code) {
        List<NameValuePair> mapList = new ArrayList<NameValuePair>();
        mapList.add(new BasicNameValuePair("apikey", APIKEY));
        mapList.add(new BasicNameValuePair("text", String.format(TPL, code)));
        mapList.add(new BasicNameValuePair("mobile", mobile));
        return send(mapList);
    }

    @SuppressWarnings("unchecked")
    private static Boolean send(List<NameValuePair> mapList) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URL);
        CloseableHttpResponse response = null;
        try {
            RequestConfig config = RequestConfig.DEFAULT;
            UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(mapList, ENCODING);
            post.setEntity(reqEntity);
            post.setConfig(config);
            response = client.execute(post);
            HttpEntity resEntity = response.getEntity();
            String jsonText = EntityUtils.toString(resEntity, ENCODING);
            Map<String, Object> map = (Map<String, Object>) JsonUtils.deserialize(jsonText,
                new TypeReference<Map<String, Object>>() {
                });
            System.out.println(jsonText);
            return (map.get("code").equals(0) && map.get("msg").equals("OK"));
        } catch (Exception e) {
            return false;
        } finally {
            post.releaseConnection();
            try {
                response.close();
            } catch (IOException e) {
                System.err.println("response close failed");
                e.printStackTrace();
            }
        }
    }

    public static String getRandomStr() {
        Random random = new Random();
        int codelen = 6;
        String code = "";
        for (int i = 0; i < codelen; i++) {
            code += random.nextInt(10);
        }
        return code;
    }

    public static void main(String[] args) {
        //        System.out.println(String.format(TPL, "123456"));
        //        send("13916390426", "123456");
        //        String json = "{\"code\":0,\"msg\":\"OK\",\"result\":{\"count\":1,\"fee\":1,\"sid\":2302375401}}";
        //        Map map = new HashMap();
        //        map = (Map) JsonUtils.deserialize(json, new TypeReference<Map>() {
        //        });
        //        int code = (Integer) map.get("code");
        //        String msg = (String) map.get("msg");
        //        System.err.println(map.get("code").equals(0));
        //        System.err.println(map.get("msg").equals("OK"));
        //        System.err.println(code == 0 && msg.equals("OK"));
    }

}
