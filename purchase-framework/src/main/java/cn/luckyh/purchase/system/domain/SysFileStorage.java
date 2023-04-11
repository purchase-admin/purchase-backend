package cn.luckyh.purchase.system.domain;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统文件存储
 *
 * @TableName sys_file_storage
 */
@TableName(value = "sys_file_storage")
@Data
public class SysFileStorage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private String id = IdUtil.simpleUUID();

    /**
     * 文件名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 扩展名
     */
    private String extension;
    private String contentType;

    /**
     * 创建时间
     */
    private LocalDateTime create_time;

}