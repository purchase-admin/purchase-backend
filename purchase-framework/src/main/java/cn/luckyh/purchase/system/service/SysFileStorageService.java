package cn.luckyh.purchase.system.service;

import cn.luckyh.purchase.system.domain.SysFileStorage;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface SysFileStorageService extends IService<SysFileStorage> {


    @SneakyThrows
    SysFileStorage storeFile(MultipartFile file);

    void deleteFileById(String id);


    /**
     * @param ids ids
     * @return HashMap<" id ", " name ">
     */
    Map<String, String> selectIdNameToMap(List<String> ids);

}
