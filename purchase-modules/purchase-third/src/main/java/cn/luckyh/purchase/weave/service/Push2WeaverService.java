package cn.luckyh.purchase.weave.service;

import cn.luckyh.purchase.weave.entity.RestDelData;
import cn.luckyh.purchase.weave.entity.RestResponse;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/08 0008 15:59
 */
@Service
@Transactional
@SuppressWarnings("SpellCheckingInspection")
public class Push2WeaverService {

    private static final String SYS_CODE = "cgxt";

    private static final String DELETE_URL = "https://cjrboa.com/rest/ofs/deleteUserRequestInfoByJson";


    @RabbitListener(bindings = @QueueBinding(value = @Queue("create"), exchange = @Exchange(value = "weaver")))
    public void create(@Payload HashMap<String, String> data) {

    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue("delete"), exchange = @Exchange(value = "weaver")))
    public void delete(HashMap<String, String> data) {
        RestDelData restDelData = new RestDelData();
        restDelData.setSyscode(SYS_CODE);
        restDelData.setFlowid(data.get("flowid"));
        restDelData.setUserid(data.get("userid"));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<RestResponse> responseEntity = restTemplate.postForEntity("", restDelData, RestResponse.class);
        RestResponse body = responseEntity.getBody();
    }

}
