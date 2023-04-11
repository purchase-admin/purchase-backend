package cn.luckyh.purchase.common.utils.WeiXin;

import com.alibaba.fastjson.JSONObject;


/**
 * @author sjz
 */
public class Contacts_UserService {

    private  static  String getUser_url="https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";


    /**
     * 获取成员
     *
     * @param accessToken
     * @param userId
     */
    public void getUser(String accessToken,String userId) {

             //1.获取请求的url
              getUser_url=getUser_url.replace("ACCESS_TOKEN", accessToken)
                        .replace("USERID", userId);

               //2.调用接口，发送请求，获取成员
                 JSONObject jsonObject = WeiXinUtil.httpRequest(getUser_url, "GET", null);
                System.out.println("jsonObject:"+jsonObject.toString());

                //3.错误消息处理
                if (null != jsonObject) {
                     if (0 != jsonObject.getInteger("errcode")) {
                    }
                }
            }


}
