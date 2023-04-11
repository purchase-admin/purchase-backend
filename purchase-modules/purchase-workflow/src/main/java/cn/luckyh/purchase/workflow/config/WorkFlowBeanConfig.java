package cn.luckyh.purchase.workflow.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.rest.service.api.RestResponseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/15 17:23
 */
@Configuration
@ComponentScan(basePackages = {"org.flowable.rest", "org.flowable.form"})
public class WorkFlowBeanConfig {

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    public RestResponseFactory restResponseFactory() {
        return new RestResponseFactory(objectMapper);
    }

//    @Bean
//    public FormRestResponseFactory formRestResponseFactory() {
//        return new FormRestResponseFactory();
//    }


}
