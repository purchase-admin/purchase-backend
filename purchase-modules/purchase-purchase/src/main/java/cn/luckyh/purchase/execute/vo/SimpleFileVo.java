package cn.luckyh.purchase.execute.vo;

import cn.luckyh.purchase.system.domain.SysFileStorage;
import cn.luckyh.purchase.system.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * simple file info vo.
 *
 * @author heng.wang
 * @since 2021/10/11 0011 18:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleFileVo implements FileVo, Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;


    /**
     * filled with constructor.
     *
     * @param storage .
     */
    public SimpleFileVo(SysFileStorage storage) {
        this.id = storage.getId();
        this.name = storage.getName();
    }
}
