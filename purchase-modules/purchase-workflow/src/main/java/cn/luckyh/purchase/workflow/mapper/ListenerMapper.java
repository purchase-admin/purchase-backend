package cn.luckyh.purchase.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/04/07 11:47
 */
@Repository
public interface ListenerMapper {

    int updateBudgetStatus(@Param("processInstanceId") String processInstanceId, @Param("status") Integer status);
}
