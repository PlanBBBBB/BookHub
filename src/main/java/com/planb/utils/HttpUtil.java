package com.planb.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送get，post请求
 */
public class HttpUtil {
    private static final Logger mLogger = LoggerFactory.getLogger(HttpUtil.class);

    public static String doGet(String url, Map<String, String> paramMap) throws IOException {
        String resultStr = null;
        InputStream in = null;
        BufferedReader br = null;
        try {
            String reqUrl = "";
            if (paramMap != null && paramMap.size() > 0) {
                String paramStr = "?";
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    paramStr += entry.getKey() + "=" + entry.getValue() + "&";
                }
                reqUrl = url + paramStr.substring(0, paramStr.length() - 1);
            } else {
                reqUrl = url;
            }

            URL endPoint = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) endPoint.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("contentType", "application/json");
            connection.setConnectTimeout(6000);
            connection.setReadTimeout(6000);
            connection.connect();

            // 请求返回的状态
            if (connection.getResponseCode() == 200) {
                // 请求返回的数据
                in = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String temp;
                StringBuilder sb = new StringBuilder();
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                resultStr = sb.toString();
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return resultStr;
    }

    public static String doPost(boolean isSSl, String url, String jsonParam) throws Exception {
        String result = null;
        CloseableHttpClient client = null;
        if (!isSSl) {
            client = HttpClients.createDefault();
        } else {
            client = SslUtil.SslHttpClientBuild();
        }
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)//设置请求连接和传输超时时间
                .setSocketTimeout(5000)//服务端超时
                .setConnectTimeout(5000).build();
        post.setConfig(requestConfig);
        StringEntity paramEntity = null;
        if (StringUtils.isNotBlank(jsonParam)) {
            paramEntity = new StringEntity(jsonParam, "utf-8");
            paramEntity.setContentType("application/json");
        }
        post.setEntity(paramEntity);
        CloseableHttpResponse response = null;
        try {
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity);
            } else {
                mLogger.error("接口：{}，返回码：{}", url, response.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            throw new IOException("调用接口出错", e);
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }
        return result;
    }

    /**
     * http post请求
     * NameValuePair
     * UrlEncodedFormEntity 表单请求
     *
     * @param url
     * @param paramMap
     * @return
     * @throws Exception
     */
    public static String doPost(boolean isSSl, String url, Map<String, String> paramMap) throws Exception {
        String result = "";
        CloseableHttpClient httpClient = null;
        if (!isSSl) {
            httpClient = HttpClients.createDefault();
        } else {
            httpClient = SslUtil.SslHttpClientBuild();
        }
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)//设置请求连接和传输超时时间
                .setSocketTimeout(5000)//服务端超时
                .setConnectTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        List<NameValuePair> parameters = new ArrayList<>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
        httpPost.setEntity(formEntity);
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                entity = response.getEntity();
                result = EntityUtils.toString(entity);
            } else {
//                throw new Exception("接口返回失败，错误码："+response.getStatusLine().getStatusCode());
                mLogger.error("接口：{}，返回码：{}", url, response.getStatusLine().getStatusCode());
            }
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }
        return result;
    }

    public static String doPost(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(7000)//设置请求连接和传输超时时间
                .setSocketTimeout(7000)//服务端超时
                .setConnectTimeout(7000).build();
        httpPost.setConfig(requestConfig);
        CloseableHttpClient client = HttpClients.createDefault();
        //请求参数转JOSN字符串
        StringEntity entity = new StringEntity(json, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        try {
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                JSONObject result = JSON.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String jsonPost(String url, String jsonStr, Map<String, String> map) {

        //定义接收数据
        JSONObject result = new JSONObject();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        //请求参数转JSON字符串
        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        if (map != null) {
            for (String key : map.keySet()) {
                httpPost.addHeader(key, map.get(key));
            }
        }

        try {
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
                return result.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.put("error", "连接错误！");
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
//        String s = doPost(Constant.uroadRescueEventAdd, "{\"additionalDetails\":\"\",\"beginMilestone\":189350,\"carInfo\":\"2102001\",\"carType\":\"11801\",\"directionId\":35,\"eventPictures\":[],\"eventTypeId\":\"01\",\"jeeves\":\"444\",\"latitude\":\"120.796598\",\"longitude\":\"30.067448\",\"needRescue\":true,\"personalDisposal\":true,\"phone\":\"\",\"reportTime\":\"2022-06-28 09:29:12\",\"rescueId\":\"19113\",\"rescueNo\":\"ZJJY202206280001\",\"roadId\":33141,\"situationRemark\":\"\",\"subEventTypeId\":\"010200\",\"userId\":\"00f46acb-52cc-11ea-8482-548028494c36\"}");
//        System.out.println(s);
//       String str =  "该事件为施救app上报, 抛撒物类型为: 桶状类, 上报人为: 向堂银, 联系方式：null, 本人处置: 是, 处置描述: sdd";
//        int i = str.lastIndexOf("#");
//        if(-1!=i){
//
//        }
//        System.out.println(i);
//        String substring = str.substring(i-1);
//        System.out.println(substring);

//        String url = "https://dev.u-road.com/ZJAppServer/checkphoto/20220705/checkimg2022070516544657422.jpg";
//        int i = url.lastIndexOf("/");
//        String substring = url.substring(i + 1);
//        System.out.println(substring);
//
//        String photourl = "http://12.1.150.233/ZJAppServer/checkphoto/20220721/checkimg2022072116040252542.jpg";
//        String replace = photourl.replace("12", "34");
//        if (StringUtils.isNotBlank(photourl)) {
//            photourl.replace("12.1.150.233", "115.238.84.147:8182");
//        }
//        System.out.println(photourl);

        String url = "http://open-api.zeiet.net:8186/api/nest/dbapi/emergency/detail";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", "2ec6ee0580b1c5a6d67da40ea6be078f");
        paramMap.put("name", "危险货物");
        String s = doPost(false, url, paramMap);
        System.out.println(s);


        String url2 = "http://12.1.10.152:9080/apisix/plugin/jwt/sign";
        Map<String, String> paramMap2 = new HashMap<>();
        paramMap2.put("key", "user-key");
        String s1 = doGet(url2, paramMap2);
        System.out.println(s1);

        String jsonStr = "这里是请求体";
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + "token");
        String result = jsonPost(url, jsonStr, headers);
        System.out.println(result);

    }
}
