package cn.luckyh.purchase;


import cn.hutool.core.codec.Base64;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/* JADX WARN: Classes with same name are omitted:
  Endecrypt.class
  Endecrypt.class
  Endecrypt.class
  project.zip:admin/WEB-INF/classes/kernel/util/Endecrypt.class
  project.zip:api/WEB-INF/classes/kernel/util/Endecrypt.class
 */
/* loaded from: project.zip:data/WEB-INF/classes/kernel/util/Endecrypt.class */
public class Endecrypt {

  private byte[] md5(String strSrc) {
    byte[] returnByte = null;
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      returnByte = md5.digest(strSrc.getBytes("GBK"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return returnByte;
  }

  private byte[] getEnKey(String spKey) {
    byte[] desKey = null;
    try {
      byte[] desKey1 = md5(spKey);
      desKey = new byte[24];
      int i = 0;
      while (i < desKey1.length && i < 24) {
        desKey[i] = desKey1[i];
        i++;
      }
      if (i < 24) {
        desKey[i] = 0;
        int i2 = i + 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return desKey;
  }

  public byte[] Encrypt(byte[] src, byte[] enKey) {
    byte[] encryptedData = null;
    try {
      DESedeKeySpec dks = new DESedeKeySpec(enKey);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
      SecretKey key = keyFactory.generateSecret(dks);
      Cipher cipher = Cipher.getInstance("DESede");
      cipher.init(1, key);
      encryptedData = cipher.doFinal(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return encryptedData;
  }

  public String getBase64Encode(byte[] src) {
    String requestValue = "";
    try {
      BASE64Encoder base64en = new BASE64Encoder();
      requestValue = base64en.encode(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestValue;
  }

  private String filter(String str) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < str.length(); i++) {
      int asc = str.charAt(i);
      if (asc != 10 && asc != 13) {
        sb.append(str.subSequence(i, i + 1));
      }
    }
    String output = new String(sb);
    return output;
  }

  public String getURLEncode(String src) {
    String requestValue = "";
    try {
      requestValue = URLEncoder.encode(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestValue;
  }

  public String get3DESEncrypt(String src, String spkey) {
    String requestValue = "";
    try {
      byte[] enKey = getEnKey(spkey);
      byte[] src2 = src.getBytes("UTF-16LE");
      byte[] encryptedData = Encrypt(src2, enKey);
      String base64String = getBase64Encode(encryptedData);
      String base64Encrypt = filter(base64String);
      requestValue = getURLEncode(base64Encrypt);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestValue;
  }

  public String getURLDecoderdecode(String src) {
    String requestValue = "";
    try {
      requestValue = URLDecoder.decode(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestValue;
  }

  public String deCrypt(byte[] debase64, String spKey) {
    String strDe;
    try {
      Cipher cipher = Cipher.getInstance("DESede");
      byte[] key = getEnKey(spKey);
      DESedeKeySpec dks = new DESedeKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
      SecretKey sKey = keyFactory.generateSecret(dks);
      cipher.init(2, sKey);
      byte[] ciphertext = cipher.doFinal(debase64);
      strDe = new String(ciphertext, "UTF-16LE");
    } catch (Exception ex) {
      strDe = "";
      ex.printStackTrace();
    }
    return strDe;
  }


  public String get3DESDecrypt(String src, String spkey) {
    String requestValue = "";
    try {
      String URLValue = getURLDecoderdecode(src);
      BASE64Decoder base64Decode = new BASE64Decoder();
      byte[] base64DValue = base64Decode.decodeBuffer(URLValue);
      requestValue = deCrypt(base64DValue, spkey);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return requestValue;
  }


  public static void newDecrypt(String src) {
    Endecrypt test = new Endecrypt();

    String SP_KEY = "Roj6#@08SDF87323FG00%jjsd";
    System.out.println(
        "src------" + test.deCrypt(
            Base64.decode(src), "Roj6#@08SDF87323FG00%jjsd"));

  }

  public static void main(String[] args) {
    Endecrypt test = new Endecrypt();

    String SP_KEY = "Roj6#@08SDF87323FG00%jjsd";

    System.out.println(
        "DB------" + test.get3DESEncrypt("FIrowp[qfl221;2[", SP_KEY));

    String src = "aAJXZjPjlojCmpZK803WtK/dTxUKop5R0C2QgzP8fRbXuBTmq4d4gA==";
    String src2 = "KuW05ZkswDp5/5///nP0cnTljlda/u0yLdoLdAxKM4AZe9kySZ1zHQ==\n";
    System.out.println(
        "DB------" + test.get3DESEncrypt(src, "Roj6#@08SDF87323FG00%jjsd"));

    newDecrypt(src);
    newDecrypt(src2);
  }
}