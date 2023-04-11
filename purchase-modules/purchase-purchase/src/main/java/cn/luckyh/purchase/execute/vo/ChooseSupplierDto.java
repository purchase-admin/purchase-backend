package cn.luckyh.purchase.execute.vo;

import lombok.Data;

import java.util.List;

/**
 * 选择中标供应商 DTO.
 *
 * @author heng.wang
 * @since 2021/10/11 0011 11:09
 */
@Data
public class ChooseSupplierDto {

    private String taskId;
    private String supplier;
    private List<String> file1;
    private String file2;
    private String file3;
    private String file4;
    private String file5;
}
