package cn.luckyh.purchase.budget.vo;

import cn.luckyh.purchase.common.core.page.PageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/05/10 15:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BudgetSummaryQuery extends PageVo {

    private static final long serialVersionUID = 1L;

    public String title;
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
