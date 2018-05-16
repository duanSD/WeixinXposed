package qi.chuangguo.weixinxposed;

import com.google.gson.Gson;

import org.json.JSONException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import qi.chuangguo.weixinxposed.util.AutoMessageBe;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
       // assertEquals(4, 2 + 2);
        //getAutoReplyContent();
        String ss = "wxid_b6xyhs9rcwek22".replace("_","");
        getAutoReplyContent("苏州天气",ss);

        //JsonSMS(json,url);
    }

    private static String url="http://openapi.tuling123.com/openapi/api/v2";

    public static void getAutoReplyContent() throws JSONException {
        OkHttpClient okHttpClient_post_kv = new OkHttpClient();

        AutoMessageBe autoMessageBe = new AutoMessageBe();
        AutoMessageBe.UserInfoBean userInfoBean = new AutoMessageBe.UserInfoBean();
        userInfoBean.setUserId("15555555fa5sf555f");
        userInfoBean.setApiKey("9abbd24245954f049acf2c7dfa63538d");
        autoMessageBe.setUserInfo(userInfoBean);
        autoMessageBe.setReqType(0);
        AutoMessageBe.PerceptionBean perceptionBean = new AutoMessageBe.PerceptionBean();
        AutoMessageBe.PerceptionBean.InputTextBean inputTextBean = new AutoMessageBe.PerceptionBean.InputTextBean();
        inputTextBean.setText("你好");
        perceptionBean.setInputText(inputTextBean);
        autoMessageBe.setPerception(perceptionBean);
        AutoMessageBe.PerceptionBean.SelfInfoBean selfInfoBean = new AutoMessageBe.PerceptionBean.SelfInfoBean();
        AutoMessageBe.PerceptionBean.SelfInfoBean.LocationBean locationBean = new AutoMessageBe.PerceptionBean.SelfInfoBean.LocationBean();
       // locationBean.setCity("上海");
        //locationBean.setProvince("上海");
        //locationBean.setStreet("松江");
        selfInfoBean.setLocation(locationBean);
        perceptionBean.setSelfInfo(selfInfoBean);
        autoMessageBe.setPerception(perceptionBean);


        Gson gson = new Gson();
       String json= gson.toJson(autoMessageBe);
       String ss= changeJsonToArguments(json);
        String str="";
        try {
             str = new String(json.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                  , json);

        Request request = new Request.Builder().post(requestBody).url(url).build();
        Response response = null;

        try {
            response = okHttpClient_post_kv.newCall(request).execute();
            System.out.println("response"+response.body().string());
            //Log.i("Utils", "getAutoReplyContent: "+response.body().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String changeJsonToArguments(String argument){
        String one = argument.toString();
        String two = "?" + one.substring(1, one.length() - 1).replace(",", "&").replace(":", "=").replace("\"", "");
        System.out.println("two"+two);
        return two;
    }

    public static String sendPost(String url, Map<String, String> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String value = paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                      new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("result:"+result);
        return result;

    }

    public static String JsonSMS(String postData, String postUrl) {
        try {
            //发送POST请求
            URL url = new URL(postUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Length", "" + postData.length());
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(postData);
            out.flush();
            out.close();
            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("connect failed!");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            System.out.println("result:"+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return "";
    }


    public static String getAutoReplyContent(String msg, String userID) {
        OkHttpClient okHttpClient_post_kv = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                  , StringToJson(msg, userID));
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Response response = null;
        try {
            response = okHttpClient_post_kv.newCall(request).execute();
            System.out.println("response.body().string()"+response.body().string());
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String StringToJson(String msg, String userID) {
        AutoMessageBe autoMessageBe = new AutoMessageBe();
        AutoMessageBe.UserInfoBean userInfoBean = new AutoMessageBe.UserInfoBean();
        userInfoBean.setUserId(userID);
        userInfoBean.setApiKey("9abbd24245954f049acf2c7dfa63538d");
        autoMessageBe.setUserInfo(userInfoBean);
        autoMessageBe.setReqType(0);
        AutoMessageBe.PerceptionBean perceptionBean = new AutoMessageBe.PerceptionBean();
        AutoMessageBe.PerceptionBean.InputTextBean inputTextBean = new AutoMessageBe.PerceptionBean.InputTextBean();
        inputTextBean.setText(msg);
        perceptionBean.setInputText(inputTextBean);
        autoMessageBe.setPerception(perceptionBean);
        AutoMessageBe.PerceptionBean.SelfInfoBean selfInfoBean = new AutoMessageBe.PerceptionBean.SelfInfoBean();
        //AutoMessageBe.PerceptionBean.SelfInfoBean.LocationBean locationBean = new AutoMessageBe.PerceptionBean.SelfInfoBean.LocationBean();
        //locationBean.setCity("上海");
        //locationBean.setProvince("上海");
        //locationBean.setStreet("松江");
        //selfInfoBean.setLocation(locationBean);
        perceptionBean.setSelfInfo(selfInfoBean);
        autoMessageBe.setPerception(perceptionBean);
        Gson gson = new Gson();
        String json = gson.toJson(autoMessageBe);
        return json;
    }


}