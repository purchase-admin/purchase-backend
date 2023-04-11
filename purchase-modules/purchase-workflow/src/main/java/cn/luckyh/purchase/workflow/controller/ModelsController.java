package cn.luckyh.purchase.workflow.controller;

import cn.luckyh.purchase.common.core.controller.BaseController;
import cn.luckyh.purchase.common.core.domain.R;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.workflow.service.model.ModelsService;
import cn.luckyh.purchase.workflow.vo.model.ModelDto;
import cn.luckyh.purchase.workflow.vo.model.ModelRestVo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.impl.persistence.entity.ModelEntityImpl;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 流程模型管理.
 *
 * @author heng.wang
 * @since 2021/03/15 17:46
 */
@Slf4j
@RestController
@Api(tags = "流程模型管理")
public class ModelsController extends BaseController {

    private final ModelsService modelsService;

    public ModelsController(ModelsService modelsService) {
        this.modelsService = modelsService;
    }

    /**
     * GET /workflow/models/list -> 查询所有流程模型
     */
    //    //@PreAuthorize("@ss.hasPermi('workflow:models:query')")
    @GetMapping(value = "/workflow/models/list")
    @ApiOperation(value = "查询模型")
    public TableDataInfo list() {
        startPage();
        List<ModelRestVo> list = modelsService.list();
        return getDataTable(list);
    }

    /**
     * POST /workflow/models/save -> 保存或更新模型
     */
    //    //@PreAuthorize("@ss.hasPermi('workflow:models:add')")
    @PostMapping(value = "/workflow/models/save")
    @ApiOperation(value = "新建流程模型")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "保存成功返回模型信息", response = ModelEntityImpl.class)
    })
    public R addModel(@RequestBody @Valid ModelDto dto) {
        return R.success("保存成功", modelsService.saveModel(dto));
    }

    /**
     * POST  /workflow/models/deploy/{modelId}  -> 部署流程模型
     */
    @PostMapping(value = "/workflow/models/deploy/{modelId}")
    @ApiOperation(value = "部署流程模型")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "部署成功返回流程定义ID", response = String.class)
    })
    public R deployModel(@PathVariable @ApiParam("模型ID") String modelId) {
        return R.success("保存成功", modelsService.deployModel(modelId));
    }

    /**
     * GET  -> /workflow/models/preview/{id} 预览流程模型
     */
    //    //@PreAuthorize("@ss.hasPermi('workflow:models:preview')")
    @GetMapping(value = "/workflow/models/preview/{id}")
    @ApiOperation(value = "预览流程模型")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "模型数据", response = String.class)
    })
    public R preview(@PathVariable String id) {
        return R.success();
    }

    /**
     * GET /workflow/model/{modelId}/history -> 获取流程模型历史版本
     */
    @GetMapping("/workflow/model/{modelId}/history")
    @ApiOperation(value = "获取流程模型历史版本")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "流程模型历史集合", response = TableDataInfo.class)
    })
    public TableDataInfo getHistoryList(@PathVariable @ApiParam(value = "模型ID", name = "modelId") String modelId, Integer pageSize, Integer pageNum) {
        return modelsService.getHistoryList(modelId, pageSize, pageNum);
    }

}
