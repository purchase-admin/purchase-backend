package cn.luckyh.purchase.system.controller;

import cn.luckyh.purchase.common.config.PurchaseConfig;
import cn.luckyh.purchase.common.constant.Constants;
import cn.luckyh.purchase.common.core.domain.Result;
import cn.luckyh.purchase.common.core.domain.ResultUtil;
import cn.luckyh.purchase.common.core.page.TableDataInfo;
import cn.luckyh.purchase.common.utils.StringUtils;
import cn.luckyh.purchase.common.utils.file.FileUtils;
import cn.luckyh.purchase.system.domain.SysFileStorage;
import cn.luckyh.purchase.system.service.SysFileStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件存储 controller.
 *
 * @author heng.wang
 * @since 2021/09/27 0027 17:25
 */
@Slf4j
@RestController
@Api(tags = "系统存储")
@RequestMapping(value = "/sys/storage")
public class SysFileStorageController {

    private final SysFileStorageService sysFileStorageService;

    public SysFileStorageController(SysFileStorageService sysFileStorageService) {
        this.sysFileStorageService = sysFileStorageService;
    }

    /**
     * GET /sys/storage/{resourceId}/resource -> 下载文件
     */
    @SneakyThrows
    @GetMapping("/{resourceId}/resource")
    public void getResource(@PathVariable String resourceId, HttpServletRequest request, HttpServletResponse response) {
        SysFileStorage fileInfo = sysFileStorageService.getById(resourceId);
        if (Objects.isNull(fileInfo)) {
            throw new RuntimeException("文件不存在");
        }
        try {
            // 本地资源路径
            String localPath = PurchaseConfig.getProfile();
            // 数据库资源地址
            String realFileName = fileInfo.getName();
            String filePath = PurchaseConfig.getUploadPath() + fileInfo.getPath();
            String downloadPath = localPath + StringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * POST  /sys/storage/batch/upload -> 批量上传文件
     */
    @PostMapping("/batch/upload")
    @ApiOperation(value = "批量上传文件")
    public TableDataInfo uploadFile(MultipartFile[] files) {
        List<SysFileStorage> list = Arrays.stream(files).filter(Objects::nonNull).map(sysFileStorageService::storeFile).collect(Collectors.toList());
        return TableDataInfo.success(list, list.size(), HttpStatus.OK);
    }

    /**
     * POST /sys/storage/single/upload ->
     */
    @PostMapping("/single/upload")
    @ApiOperation(value = "单个文件上传")
    public SysFileStorage uploadSingleFile(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw new RuntimeException("上传文件不能为空");
        }
        return sysFileStorageService.storeFile(file);
    }

    /**
     * DELETE /sys/storage/{id} -> 删除单个文件
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除文件")
    public Result<String> deleteFileById(@PathVariable String id) {
        sysFileStorageService.deleteFileById(id);
        return ResultUtil.success("删除成功");
    }
}
