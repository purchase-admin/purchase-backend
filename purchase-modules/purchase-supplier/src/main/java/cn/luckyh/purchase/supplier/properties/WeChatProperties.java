package cn.luckyh.purchase.supplier.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/6/7 0007 14:13
 */
@Configuration
@ConfigurationProperties(prefix = "purchase.wechat")
public class WeChatProperties {
    private String corpId;
    private String corpSecret;

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }
}
