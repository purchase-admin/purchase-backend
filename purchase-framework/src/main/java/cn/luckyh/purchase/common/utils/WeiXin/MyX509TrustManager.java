package cn.luckyh.purchase.common.utils.WeiXin;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author sjz
 */
public class MyX509TrustManager implements X509TrustManager {

     @Override
     public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

     @Override
     public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

     @Override
     public X509Certificate[] getAcceptedIssuers() {
         return null;
     }

}
