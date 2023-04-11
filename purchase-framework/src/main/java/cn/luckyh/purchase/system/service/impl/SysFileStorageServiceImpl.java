package cn.luckyh.purchase.system.service.impl;

import cn.luckyh.purchase.common.config.PurchaseConfig;
import cn.luckyh.purchase.common.utils.file.FileUploadUtils;
import cn.luckyh.purchase.system.domain.SysFileStorage;
import cn.luckyh.purchase.system.mapper.SysFileStorageMapper;
import cn.luckyh.purchase.system.service.SysFileStorageService;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * save file.
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SysFileStorageServiceImpl extends ServiceImpl<SysFileStorageMapper, SysFileStorage>
        implements SysFileStorageService {


    @SneakyThrows
    @Override
    public SysFileStorage storeFile(MultipartFile file) {
        log.debug("****************保存文件:{}************************", file.getOriginalFilename());
        SysFileStorage storage = new SysFileStorage();
        String path = FileUploadUtils.upload(file);
        String extension = FileUploadUtils.getExtension(file);
        storage.setPath(path);
        storage.setName(file.getOriginalFilename());
        storage.setExtension(extension);
        storage.setContentType(file.getContentType());
        storage.setCreate_time(LocalDateTime.now());
        save(storage);
        log.debug("保存文件:{}", storage);
        return storage;
    }

    @Override
    public void deleteFileById(String id) {
        SysFileStorage byId = getById(id);
        if (Objects.isNull(byId)) {
            throw new RuntimeException("文件不存在");
        }
        String path = byId.getPath();
        FileUtil.del(PurchaseConfig.getProfile() + path);
        removeById(id);
    }

    @Override
    public Map<String, String> selectIdNameToMap(List<String> ids) {
        LambdaQueryWrapper<SysFileStorage> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.in(SysFileStorage::getId, ids);
        return list(lambdaQuery)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(SysFileStorage::getId, SysFileStorage::getName, (a, b) -> b, HashMap::new));
    }

}




