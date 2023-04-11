package cn.luckyh.purchase.common.utils.WeiXin;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author sjz
 */
public class WeiXinUtil {

    private static Logger log = LoggerFactory.getLogger(WeiXinUtil.class);

    /**
     * 微信的请求url
     * 获取access_token的接口地址（GET） 限200（次/天）
     */
    //public final static String access_token_url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpId}&corpsecret={corpsecret}";
    //public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={corpId}&secret={corpsecret}";
    public final static String access_token_url = "https://api.weixin.qq.com/sns/jscode2session?appid={corpId}&secret={corpsecret}&js_code={code}&grant_type=authorization_code";

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @return
     */
    public static AccessToken getAccessToken(String appid, String appsecret) {
        AccessToken accessToken = null;

        String requestUrl = access_token_url.replace("{corpId}", appid).replace("{corpsecret}", appsecret);

        log.info("requestUrl {}", requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }

    /**
     * 获取access_token
     *
     * @param appid     凭证
     * @param appsecret 密钥
     * @param code
     * @return
     */
    public static AccessToken getAccessToken(String appid, String appsecret, String code) {
        AccessToken accessToken = null;

        String requestUrl = access_token_url.replace("{corpId}", appid).replace("{corpsecret}", appsecret).replace("{code}", code);

        log.info("requestUrl {}", requestUrl);

        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (null != jsonObject) {
            if(jsonObject.getString("errorCode")!=null){
                accessToken = new AccessToken();
                accessToken.setErrorCode(jsonObject.getString("errorCode"));
                return accessToken;
            }
            try {
                accessToken = new AccessToken();
                accessToken.setOpenId(jsonObject.getString("openid"));
                accessToken.setSessionKey(jsonObject.getString("session_key"));
            } catch (JSONException e) {
                accessToken = null;
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return accessToken;
    }


    /**
     * 1.发起https请求并获取结果
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 2.发起http请求获取返回结果
     *
     * @param requestUrl 请求地址
     * @return
     */
    public static String httpRequest(String requestUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            httpUrlConn.disconnect();

        } catch (Exception e) {
        }
        return buffer.toString();
    }


    /***************************************************************************************************


     public static String doGet(String url, Map<String, String> param) {

     // 创建Httpclient对象
     CloseableHttpClient httpclient = HttpClients.createDefault();

     String resultString = "";
     CloseableHttpResponse response = null;
     try {
     // 创建uri
     URIBuilder builder = new URIBuilder(url);
     if (param != null) {
     for (String key : param.keySet()) {
     builder.addParameter(key, param.get(key));
     }
     }
     URI uri = builder.build();

     // 创建http GET请求
     HttpGet httpGet = new HttpGet(uri);

     // 执行请求
     response = httpclient.execute(httpGet);
     // 判断返回状态是否为200
     if (response.getStatusLine().getStatusCode() == 200) {
     resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
     }
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     try {
     if (response != null) {
     response.close();
     }
     httpclient.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }
     return resultString;
     }

     public static String doGet(String url) {
     return doGet(url, null);
     }

     public static String doPost(String url, Map<String, String> param) {
     // 创建Httpclient对象
     CloseableHttpClient httpClient = HttpClients.createDefault();
     CloseableHttpResponse response = null;
     String resultString = "";
     try {
     // 创建Http Post请求
     HttpPost httpPost = new HttpPost(url);
     // 创建参数列表
     if (param != null) {
     List<NameValuePair> paramList = new ArrayList<>();
     for (String key : param.keySet()) {
     paramList.add(new BasicNameValuePair(key, param.get(key)));
     }
     // 模拟表单
     UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
     httpPost.setEntity(entity);
     }
     // 执行http请求
     response = httpClient.execute(httpPost);
     resultString = EntityUtils.toString(response.getEntity(), "utf-8");
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     try {
     response.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }

     return resultString;
     }

     public static String doPost(String url) {
     return doPost(url, null);
     }

     public static String doPostJson(String url, String json) {
     // 创建Httpclient对象
     CloseableHttpClient httpClient = HttpClients.createDefault();
     CloseableHttpResponse response = null;
     String resultString = "";
     try {
     // 创建Http Post请求
     HttpPost httpPost = new HttpPost(url);
     // 创建请求内容
     StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
     httpPost.setEntity(entity);
     // 执行http请求
     response = httpClient.execute(httpPost);
     resultString = EntityUtils.toString(response.getEntity(), "utf-8");
     } catch (Exception e) {
     e.printStackTrace();
     } finally {
     try {
     response.close();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }

     return resultString;
     }

     */
}
