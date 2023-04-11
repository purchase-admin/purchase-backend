package cn.luckyh.purchase.workflow.vo.runtime;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/6/9 0009 16:16
 */
@Data
public class MigrationRepresentation {

    @NotEmpty(message = "流程实例ID不能为空")
    private String processInstanceId;
    @NotNull(message = "迁移定义id不能为空")
    private String migrationDefinitionId;

}
