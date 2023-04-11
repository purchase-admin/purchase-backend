package cn.luckyh.purchase.workflow.service;

import cn.luckyh.purchase.common.core.domain.entity.SysUser;
import cn.luckyh.purchase.common.core.domain.model.UserRepresentation;
import cn.luckyh.purchase.system.service.ISysUserService;

import java.util.HashMap;
import java.util.Objects;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/6/3 0003 18:07
 */
public class BaseQueryService {
    HashMap<String, SysUser> userCache = new HashMap<>();
    private final ISysUserService userService;

    public BaseQueryService(ISysUserService userService) {
        this.userService = userService;
    }

    protected UserRepresentation getAssigneeRepresentation(String assignee) {
        SysUser cachedUser = userCache.get(assignee);
        UserRepresentation representation;
        if (Objects.nonNull(cachedUser)) {
            representation = new UserRepresentation(cachedUser);
        } else {
            SysUser user = userService.selectUserByUserName(assignee);
            if (Objects.nonNull(user)) {
                userCache.put(user.getUserName(), user);
                representation = new UserRepresentation(user);
            } else {
                representation = new UserRepresentation(assignee);
            }
        }
        return representation;
    }
}
