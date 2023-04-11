package cn.luckyh.purchase.budget.vo;

import cn.luckyh.purchase.common.core.page.PageVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/25 14:25
 */
@Data
@ApiModel("部门预算查询 model")
public class BudgetBatchQuery extends PageVo {

    private static final long serialVersionUID = 1L;

    public String status;
    public String year;

    /**
     * 提取分页信息
     *
     * @return pageVo
     */
    @ApiModelProperty(hidden = true)
    public PageVo getPage() {
        return new PageVo()
                .setPageNumber(this.getPageNumber())
                .setPageSize(this.getPageSize())
                .setOrder(this.getOrder())
                .setSort(this.getSort());
    }
}
