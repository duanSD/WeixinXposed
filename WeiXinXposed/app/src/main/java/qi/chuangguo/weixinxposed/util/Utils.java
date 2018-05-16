package qi.chuangguo.weixinxposed.util;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chuangguo.qi on 2018/5/14.
 */

public class Utils {
    private static String url = "http://openapi.tuling123.com/openapi/api/v2";

    public static String getAutoReplyContent(String msg, String userID,String autoReplyStr) {
        OkHttpClient okHttpClient_post_kv = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                  , StringToJson(msg, userID,autoReplyStr));
        Request request = new Request.Builder().post(requestBody).url(url).build();
        Response response = null;
        try {
            response = okHttpClient_post_kv.newCall(request).execute();
            Gson gson = new Gson();
            String msgJson = response.body().string();
            if (!TextUtils.isEmpty(msgJson)){
                ResultsBe resultsBe = gson.fromJson(msgJson, ResultsBe.class);
                if (resultsBe!=null){
                    List<ResultsBe.ResultsBean> results = resultsBe.getResults();
                    if (results!=null && results.size()>0){
                        StringBuffer stringBuffer = new StringBuffer();
                        for (int i = 0; i <results.size() ; i++) {
                            ResultsBe.ResultsBean resultsBean = results.get(i);
                                if (resultsBean != null) {
                                    ResultsBe.ResultsBean.ValuesBean values = resultsBean.getValues();
                                    if (values != null) {
                                        if (resultsBean.getResultType().equals("url")){
                                            stringBuffer.append(values.getUrl());
                                        }else if (resultsBean.getResultType().equals("text")){
                                            stringBuffer.append(values.getText());
                                        }

                                    }
                                }
                        }
                        return stringBuffer.toString();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String StringToJson(String msg, String userID,String autoReplyStr) {
        AutoMessageBe autoMessageBe = new AutoMessageBe();
        AutoMessageBe.UserInfoBean userInfoBean = new AutoMessageBe.UserInfoBean();
        userInfoBean.setUserId(userID);
        userInfoBean.setApiKey(autoReplyStr);
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
