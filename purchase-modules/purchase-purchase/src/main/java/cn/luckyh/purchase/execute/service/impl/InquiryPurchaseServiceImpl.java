package cn.luckyh.purchase.execute.service.impl;

import cn.luckyh.purchase.execute.domain.InquiryPurchase;
import cn.luckyh.purchase.execute.mapper.InquiryPurchaseMapper;
import cn.luckyh.purchase.execute.service.IInquiryPurchaseService;
import cn.luckyh.purchase.execute.vo.ChooseSupplierDto;
import cn.luckyh.purchase.execute.vo.InquiryPurchaseVo;
import cn.luckyh.purchase.execute.vo.SimpleFileVo;
import cn.luckyh.purchase.system.domain.SysFileStorage;
import cn.luckyh.purchase.system.service.SysFileStorageService;
import cn.luckyh.purchase.task.domain.PurchaseTask;
import cn.luckyh.purchase.task.service.impl.PurchaseTaskServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 询价采购 Service业务层处理
 *
 * @author purchase
 * @date 2021-03-24
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class InquiryPurchaseServiceImpl extends ServiceImpl<InquiryPurchaseMapper, InquiryPurchase> implements IInquiryPurchaseService {
    @Resource
    private InquiryPurchaseMapper inquiryPurchaseMapper;

    @Autowired
    private PurchaseTaskServiceImpl purchaseTaskService;

    @Autowired
    private SysFileStorageService sysFileStorageService;

    /**
     * 查询询价采购
     *
     * @param id 询价采购 ID
     * @return 询价采购
     */
    @Override
    public InquiryPurchaseVo selectInquiryPurchaseById(String id) {
        return inquiryPurchaseMapper.selectInquiryPurchaseById(id);
    }

    /**
     * 查询询价采购 列表
     *
     * @param inquiryPurchase 询价采购
     * @return 询价采购
     */
    @Override
    public List<InquiryPurchaseVo> selectInquiryPurchaseList(InquiryPurchase inquiryPurchase) {
        List<InquiryPurchaseVo> list = inquiryPurchaseMapper.selectInquiryPurchaseList(inquiryPurchase);
        //若任务中的截止日期还没到，则将报价隐藏，设置为默认值0
        convertQuoatation(list);
        return list;
    }

    /**
     * 若任务中的截止日期还没到，则将报价隐藏，设置为默认值0
     *
     * @param list
     */
    private void convertQuoatation(List<InquiryPurchaseVo> list) {
        if (list.isEmpty()) {
            return;
        }
        String taskId = list.get(0).getTaskId();
        if (StringUtils.hasText(taskId)) {
            PurchaseTask byId = purchaseTaskService.getById(taskId);
            if (byId != null && byId.getEndTime() != null &&
                    byId.getEndTime().getTime() > new Date().getTime()) {
                list.forEach(inquiry -> inquiry.setQuotation(new BigDecimal("0")));
            }
        }
        //附件处理
        list.forEach(inquiry -> {
            String file1 = inquiry.getFile1();
            List<String> list1 = StrUtil.splitTrim(file1, StrUtil.COMMA);

            if (!list1.isEmpty()) {
                //查询文件信息
                LambdaQueryWrapper<SysFileStorage> lambdaQuery = Wrappers.lambdaQuery(SysFileStorage.class);
                lambdaQuery.in(SysFileStorage::getId, list1);
                //构建vo
                List<SimpleFileVo> simpleFileVoList = sysFileStorageService.list(lambdaQuery)
                        .stream()
                        .filter(Objects::nonNull)
                        .map(SimpleFileVo::new)
                        .collect(Collectors.toList());
                //填充返回体
                inquiry.setFileList(simpleFileVoList);
            }

            convertOtherFileName(inquiry);
        });

    }

    private void convertOtherFileName(InquiryPurchaseVo inquiry) {
        List<String> ids = new ArrayList<>();
        ids.add(inquiry.getFile2());
        ids.add(inquiry.getFile3());
        ids.add(inquiry.getFile4());
        ids.add(inquiry.getFile5());
        Map<String, String> map = sysFileStorageService.selectIdNameToMap(ids);
        inquiry.setFile2Name(map.getOrDefault(inquiry.getFile2(), ""));
        inquiry.setFile3Name(map.getOrDefault(inquiry.getFile3(), ""));
        inquiry.setFile4Name(map.getOrDefault(inquiry.getFile4(), ""));
        inquiry.setFile5Name(map.getOrDefault(inquiry.getFile5(), ""));
    }

    /**
     * 新增询价采购
     *
     * @param inquiryPurchase 询价采购
     * @return 结果
     */
    @Override
    public int insertInquiryPurchase(InquiryPurchase inquiryPurchase) {
        deleteInquiryPurchaseByTaskId(inquiryPurchase.getTaskId());//先清除相关的询价信息再新增
        PurchaseTask task = new PurchaseTask();
        task.setId(inquiryPurchase.getTaskId());
        task.setIsOpen(inquiryPurchase.getIsOpen());
        task.setEndTime(inquiryPurchase.getEndTime());
        purchaseTaskService.updatePurchaseTask(task);
        if (StringUtil.isNotEmpty(inquiryPurchase.getSupplier())) {
            //如果是邀请的多供应商，则保存多条
            if (inquiryPurchase.getSupplier().indexOf(",") != -1) {
                List<InquiryPurchase> inquiryPurchases = new ArrayList<>();
                String[] supplierIds = inquiryPurchase.getSupplier().split(",");
                for (String supplier : supplierIds) {
                    InquiryPurchase inq = new InquiryPurchase();
                    inq.setTaskId(inquiryPurchase.getTaskId());
                    inq.setSupplier(supplier);
                    inquiryPurchases.add(inq);
                }
                return saveBatch(inquiryPurchases) ? 1 : 0;
            }
        }
        return save(inquiryPurchase) ? 1 : 0;
    }

    /**
     * 修改询价采购
     *
     * @param inquiryPurchase 询价采购
     * @return 结果
     */
    @Override
    public int updateInquiryPurchase(InquiryPurchase inquiryPurchase) {
        return inquiryPurchaseMapper.updateById(inquiryPurchase);
    }

    /**
     * 批量删除询价采购
     *
     * @param ids 需要删除的询价采购 ID
     * @return 结果
     */
    @Override
    public int deleteInquiryPurchaseByIds(String[] ids) {
        return inquiryPurchaseMapper.deleteInquiryPurchaseByIds(ids);
    }

    /**
     * 删除询价采购 信息
     *
     * @param id 询价采购 ID
     * @return 结果
     */
    @Override
    public int deleteInquiryPurchaseById(String id) {
        return inquiryPurchaseMapper.deleteInquiryPurchaseById(id);
    }

    @Override
    public int chooseSupplier(InquiryPurchase inquiryPurchase) {
        if (StringUtil.isEmpty(inquiryPurchase.getSupplier()) || StringUtil.isEmpty(inquiryPurchase.getTaskId())) {
            return 0;
        }
        InquiryPurchase query = new InquiryPurchase();
        query.setTaskId(inquiryPurchase.getTaskId());
        List<InquiryPurchaseVo> inquiryPurchases = inquiryPurchaseMapper.selectInquiryPurchaseList(query);//先查出此次任务邀请的所有供应商
        // List<InquiryPurchase> inquiryPurchases = inquiryPurchaseMapper.selectInquiryPurchaseByTaskId(inquiryPurchase.getTaskId());//先查出此次任务邀请的所有供应商

        String isCheck = inquiryPurchase.getIsCheck();
        if ("1".equals(isCheck)) {
            //执行指定中标操作
            for (InquiryPurchaseVo inqu : inquiryPurchases) {
                if (inqu.getSupplier().equals(inquiryPurchase.getSupplier())) {//指定的那一条供应商设置为中标状态
                    inqu.setIsCheck("1");
                    inqu.setFile1(inquiryPurchase.getFile1());
                    inqu.setFile2(inquiryPurchase.getFile2());
                    inqu.setFile3(inquiryPurchase.getFile3());
                    inqu.setFile4(inquiryPurchase.getFile4());
                    inqu.setFile5(inquiryPurchase.getFile5());
                } else {
                    inqu.setIsCheck("0");
                    inqu.setFile1("");
                    inqu.setFile2("");
                    inqu.setFile3("");
                    inqu.setFile4("");
                    inqu.setFile5("");
                }
                //Fixme: 重写确定供应商    --Add By heng.wang 2021/10/12 0012 10:34
                // if (updateInquiryPurchase(inqu) == 0) {
                //     return 0;
                // }
            }
        } else {
            //执行取消中标操作
            for (InquiryPurchaseVo inqu : inquiryPurchases) {
                if (inqu.getSupplier().equals(inquiryPurchase.getSupplier())) {
                    inqu.setIsCheck("0");
                    inqu.setFile1("");
                    inqu.setFile2("");
                    inqu.setFile3("");
                    inqu.setFile4("");
                    inqu.setFile5("");
                    //Fixme: 重写确定供应商    --Add By heng.wang 2021/10/12 0012 10:34
                    // if (updateInquiryPurchase(inqu) == 0) {
                    //     return 0;
                    // } else {
                    //     return 1;
                    // }
                }
            }
        }
        return 1;
    }

    @Override
    public int chooseSupplier(ChooseSupplierDto chooseSupplierDto) {

        if (StringUtil.isEmpty(chooseSupplierDto.getSupplier()) || StringUtil.isEmpty(chooseSupplierDto.getTaskId())) {
            return 0;
        }

        // query.setTaskId(inquiryPurchase.getTaskId());
        // List<InquiryPurchaseVo> inquiryPurchases = inquiryPurchaseMapper.selectInquiryPurchaseList(query);//先查出此次任务邀请的所有供应商
        // List<InquiryPurchase> inquiryPurchases = inquiryPurchaseMapper.selectInquiryPurchaseByTaskId(inquiryPurchase.getTaskId());//先查出此次任务邀请的所有供应商
        // 中标操作
        InquiryPurchase inquiryPurchase = inquiryPurchaseMapper.selectInquiryPurchaseTaskIdAndSupplierId(chooseSupplierDto.getTaskId(), chooseSupplierDto.getSupplier());
        inquiryPurchase.setIsCheck("1");
        List<String> file1 = chooseSupplierDto.getFile1();
        String collect = "";
        if (Objects.nonNull(file1) && !file1.isEmpty()) {
            collect = String.join(StrUtil.COMMA, file1);
        }
        inquiryPurchase.setFile1(collect);
        inquiryPurchase.setFile2(chooseSupplierDto.getFile2());
        inquiryPurchase.setFile3(chooseSupplierDto.getFile3());
        inquiryPurchase.setFile4(chooseSupplierDto.getFile4());
        inquiryPurchase.setFile5(chooseSupplierDto.getFile5());
        updateById(inquiryPurchase);

        //其他取消中标,根据TaskId 供应商ID过滤
        LambdaUpdateWrapper<InquiryPurchase> updateWrapper = Wrappers.lambdaUpdate(InquiryPurchase.class);
        updateWrapper.set(InquiryPurchase::getIsCheck, "0");
        updateWrapper.eq(InquiryPurchase::getTaskId, chooseSupplierDto.getTaskId());
        updateWrapper.ne(InquiryPurchase::getSupplier, chooseSupplierDto.getSupplier());
        update(updateWrapper);
        return 1;
    }

    @Override
    public List<Map<String, Object>> selectNoticeList(String supplierId) {
        return inquiryPurchaseMapper.selectNoticeList(supplierId);
    }

    @Override
    public List<Map<String, Object>> selectOpenNoticeList(String supplierId) {
        if(StrUtil.isEmpty(supplierId)){
            return inquiryPurchaseMapper.selectOpenNoticeListWithoutLogin();
        }
        List<Map<String, Object>> mapList = inquiryPurchaseMapper.selectOpenNoticeList(supplierId);
        return filterData(mapList,supplierId);
    }

    private List<Map<String, Object>> filterData(List<Map<String, Object>> mapList,String supplierId) {
        List<String> taskIdList = mapList.stream().map(map -> map.get("taskId") == null ? "" : map.get("taskId").toString()).collect(Collectors.toList());
        List<String> needRemoveTaskIds = inquiryPurchaseMapper.selectIsJoinTaskId(taskIdList,supplierId);
        List<Map<String, Object>> returnList = new ArrayList<>();
        for(Map<String, Object> map : mapList){
            if(needRemoveTaskIds.size()==0){ return mapList;}
            for(String rmTaskId : needRemoveTaskIds){
                if(!rmTaskId.equals(map.get("taskId"))){
                    returnList.add(map);
                }
            }
        }
        return returnList;
    }

    public InquiryPurchase getOnlyOne(String taskId, String supplierId) {
        return inquiryPurchaseMapper.getOnlyOne(taskId, supplierId);
    }

    @Override
    public boolean checkEndTime(InquiryPurchase inquiryPurchase) {
        Date endTime;
        try {
            endTime = purchaseTaskService.selectPurchaseTaskById(inquiryPurchase.getTaskId()).getEndTime();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("询价截止日期校验异常");
            return false;
        }
        return new Date().getTime() > endTime.getTime();
    }

    @Override
    public boolean checkEndTime(String taskId) {
        Date endTime;
        try {
            endTime = purchaseTaskService.selectPurchaseTaskById(taskId).getEndTime();
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("询价截止日期校验异常");
            return false;
        }
        return new Date().getTime() > endTime.getTime();
    }


    @Override
    public int deleteInquiryPurchaseByTaskId(String taskId) {
        purchaseTaskService.cancelPurchaseTask(taskId);
        LambdaQueryWrapper<InquiryPurchase> wrapper = new LambdaQueryWrapper();
        wrapper.eq(InquiryPurchase::getTaskId, taskId);
        return inquiryPurchaseMapper.delete(wrapper);
    }

    @Override
    public void cancelSupplier(String taskId, String supplier) {
        InquiryPurchase purchase = inquiryPurchaseMapper.selectInquiryPurchaseTaskIdAndSupplierId(taskId, supplier);
        purchase.setIsCheck("0");
        updateById(purchase);
    }
}
