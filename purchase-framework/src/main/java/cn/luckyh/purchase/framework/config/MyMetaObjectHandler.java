package cn.luckyh.purchase.framework.config;

import java.util.Date;
import java.util.Objects;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import cn.luckyh.purchase.common.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/06 09:27
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public static final String CREATE_BY = "createBy";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_BY = "updateBy";
    public static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.info("start insert fill ....");
        }
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal) || Objects.isNull(principal)) {
            username = "anonymousUser";
            log.debug("insert --- 当前接口未获取到登录人");
        } else {
            username = SecurityUtils.getUsername();
        }
        //fill createBy
        this.strictInsertFill(metaObject, CREATE_BY, String.class, username);
        //fill createTime
        this.strictInsertFill(metaObject, CREATE_TIME, Date.class, new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.info("start update fill ....");
        }
        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal) || Objects.isNull(principal)) {
            username = "anonymousUser";
            log.debug("update --- 当前接口未获取到登录人");
        } else {
            username = SecurityUtils.getUsername();
        }
        this.strictUpdateFill(metaObject, UPDATE_BY, String.class, username);
        this.strictUpdateFill(metaObject, UPDATE_TIME, Date.class, new Date());
    }
}