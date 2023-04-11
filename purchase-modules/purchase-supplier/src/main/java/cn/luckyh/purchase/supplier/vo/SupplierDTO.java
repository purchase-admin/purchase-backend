package cn.luckyh.purchase.supplier.vo;

import cn.luckyh.purchase.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * .
 *
 * @author heng.wang
 * @since 2021/09/28 0028 18:45
 */
@Data
public class SupplierDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private String id;

    /**
     * 公司名称
     */
    @Excel(name = "公司名称")
    private String companyName;

    /**
     * 法人代表
     */
    @Excel(name = "法人代表")
    private String corporateRep;

    /**
     * 公司地址
     */
    @Excel(name = "公司地址")
    private String companyAddress;

    /**
     * 邮编
     */
    @Excel(name = "邮编")
    private String postCode;

    /**
     * 业务联系人
     */
    @Excel(name = "业务联系人")
    private String businessContact;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 网址
     */
    @Excel(name = "网址")
    private String website;

    /**
     * 成立日期
     */
    @Excel(name = "成立日期")
    private String establishmentDate;

    /**
     * 注册资本
     */
    @Excel(name = "注册资本")
    private String registeredCapital;

    /**
     * 企业性质 字典
     */
    @Excel(name = "企业性质 字典")
    private String nature;

    /**
     * 统一信用代码
     */
    @Excel(name = "统一信用代码")
    private String creditCode;

    /**
     * 开户行名称
     */
    @Excel(name = "开户行名称")
    private String bankName;

    /**
     * 银行基本账号
     */
    @Excel(name = "银行基本账号")
    private String bankAccount;

    /**
     * 年销售额
     */
    @Excel(name = "年销售额")
    private String annualSale;

    /**
     * 员工人数
     */
    @Excel(name = "员工人数")
    private String employeeNumber;

    /**
     * 专业资质
     */
    @Excel(name = "专业资质")
    private String prefessionalAptitude;

    /**
     * 体系论证
     */
    @Excel(name = "体系论证")
    private String setupProof;

    /**
     * 经营范围和主要产品及服务 字典
     */
    @Excel(name = "经营范围和主要产品及服务 字典")
    private String businessScope;

    /**
     * 供应产品
     */
    @Excel(name = "供应产品")
    private String provideProduct;

    /**
     * 主要生产设备
     */
    @Excel(name = "主要生产设备")
    private String productionEquipment;

    /**
     * 生产及服务能力（获得荣誉与重点项目）
     */
    private String pointProject;
    /**
     * 收费标准
     */
    @Excel(name = "收费标准")
    private String expensesStandard;

    /**
     * 结算方式
     */
    @Excel(name = "结算方式")
    private String settlementModes;

    /**
     * 其他事项
     */
    @Excel(name = "其他事项")
    private String other;

    /**
     * 供应商类型
     */
    @Excel(name = "供应商类型")
    private String supplierType;


    /**
     * 供应商关联的项目分类
     */
    @TableField(exist = false)
    private List<String> projectTypeIds;

    /**
     * 供应商关联账号登录名
     */
    private String loginName;

    @TableField(exist = false)
    private String nickName;

    /**
     * 供应商微信小程序openId
     */
    private String openId;

    /**
     * 流程实例ID
     */
    @TableField(exist = false)
    private String processInstanceId;

    /**
     * 是否通过审核 0未通过 1已通过
     */
    private String isApprove;

    /**
     * 是否为库内供应商 0否 1是
     */
    private String isInternal;
}
