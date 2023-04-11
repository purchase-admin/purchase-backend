package cn.luckyh.purchase.workflow.mapper;

import cn.luckyh.purchase.workflow.vo.runtime.TaskQueryResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/6/3 0003 15:21
 */
@Mapper
public interface TasksMapper {
    List<TaskQueryResult> listTasks(@Param("assignee") String assignee, @Param("processDefinitionKey") String processDefinitionKey, @Param("processInstanceName") String processInstanceName);
}
