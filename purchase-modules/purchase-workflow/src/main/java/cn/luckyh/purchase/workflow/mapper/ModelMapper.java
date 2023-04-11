package cn.luckyh.purchase.workflow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.luckyh.purchase.workflow.vo.model.ModelRestVo;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/03/25 08:39
 */
@Mapper
public interface ModelMapper {

    List<ModelRestVo> list();

    List<ModelRestVo> listVersions(String key);

}
