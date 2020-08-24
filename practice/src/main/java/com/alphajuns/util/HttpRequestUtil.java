package com.alphajuns.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtil {

    private static Logger mailAndLyncFailLogger = Logger.getLogger("MailAndLyncFailLogger");

    /***
     * 向指定URL发送POST方法的请求
     * @param url	发送请求的URL
     * @return	URL所代表远程资源的响应
     */
    public static int sendGet(String url) {
        int resultCode = 0;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET"); //设置post方式连接
            // 设置通用的请求属性
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setUseCaches(false);
            // 建立实际的连接
            conn.connect();
            // 获取响应状态
            resultCode = conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return resultCode;
    }

    /***
     * 向指定URL发送GET方法的请求
     * @param url	发送请求的URL
     * @return	URL所代表远程资源的响应
     */
    public static String sendGetAndRetrieveContent(String url) {
        String result = null;
        StringBuffer resultBuffer = new StringBuffer();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET"); //设置post方式连接
            // 设置通用的请求属性
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setUseCaches(false);
            // 建立实际的连接
            conn.connect();
            // 获取响应状态
            InputStream in = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(in,"UTF-8");
            BufferedReader breader = new BufferedReader(reader);
            String str = null;
            while((str=breader.readLine())!=null){
                resultBuffer.append(str);
            }
            breader.close();
            reader.close();
            in.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result=resultBuffer.toString();
    }

    /***
     * 向指定URL发送POST方法的请求
     * @param url	发送请求的URL
     * @param param	请求参数，请求参数应该是name1=value1&name2=value2的形式
     * @return	URL所代表远程资源的响应
     */
    public static int sendPost(String url, String param) {
        PrintWriter out = null;
        int resultCode = 0;
        try {
            URL realUrl = new URL(url);
            mailAndLyncFailLogger.info("realUrl:"+realUrl);
            mailAndLyncFailLogger.info("param:"+param);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("POST"); //设置post方式连接
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            resultCode = conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            mailAndLyncFailLogger.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mailAndLyncFailLogger.error(ex.getMessage());
            }
        }
        return resultCode;
    }


    /***
     * 向指定URL发送POST方法的请求
     * @param url	发送请求的URL
     * @param param	请求参数，请求参数应该是name1=value1&name2=value2的形式
     * @param userName	新品系统发送数据账号名
     * @param passWord	新品系统发送数据账号密码
     * @return	URL所代表远程资源的响应
     */
    public static int sendPostExtend(String url, String param, String userName, String passWord) {
        PrintWriter out = null;
        int resultCode = -1;
        String base64Str = null;
        String userInfo =  null;
        try {
            URL realUrl = new URL(url);
            mailAndLyncFailLogger.info("realUrl:"+realUrl);
            mailAndLyncFailLogger.info("param:"+param+"; from userName="+userName);
            // 打开和URL之间的连接
            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
            //SSLContext sslcontext = SSLContext.getInstance("SSL","SunJSSE");
            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
            HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
                public boolean verify(String s, SSLSession sslsession) {
                    mailAndLyncFailLogger.info("WARNING: Hostname is not matched for cert.");
                    return true;
                }
            };

            conn.setHostnameVerifier(ignoreHostnameVerifier);
            conn.setSSLSocketFactory(sslcontext.getSocketFactory());
            conn.setRequestMethod("POST"); //设置post方式连接
			// 设置通用的请求属性
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");

            //柳汽通 需要包含指定头部，用于临时授权
            Base64 base64 = new Base64();
            userInfo = userName+":"+passWord;
            base64Str = base64.encodeToString(userInfo.getBytes("UTF-8"));
            conn.setRequestProperty("Authorization", "Basic "+base64Str);

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            //不校验
//			conn.setHostnameVerifier(DO_NOT_VERIFY);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            conn.getResponseCode();
            resultCode = conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
            mailAndLyncFailLogger.error(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                mailAndLyncFailLogger.error(ex.getMessage());
            }

        }
        return resultCode;
    }

}
