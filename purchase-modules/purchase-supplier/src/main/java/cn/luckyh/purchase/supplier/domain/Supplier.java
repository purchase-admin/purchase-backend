package cn.luckyh.purchase.supplier.domain;

import cn.luckyh.purchase.common.annotation.Excel;
import cn.luckyh.purchase.common.core.domain.BaseEntity;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 供应商信息 供应商数据对象 supplier
 *
 * @author purchase
 * @date 2021-03-23
 */
@Data
public class Supplier extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id = IdUtil.simpleUUID();

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
    @Excel(name = "成立日期",dateFormat = "yyyy-MM-dd HH:mm:ss")
    private String establishmentDate;

    /**
     * 注册资本
     */
    @Excel(name = "注册资本")
    private String registeredCapital;

    /**
     * 企业性质 字典
     */
    @Excel(name = "企业性质",readConverterExp = "1=国有企业,2=集体企业,3=外资企业,4=三资企业,5=民营企业,6=其他企业")
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
    @Excel(name = "经营范围和主要产品及服务")
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
    @Excel(name = "供应商类型",readConverterExp = "1=生产型,2=经营型")
    private String supplierType;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 供应商关联的项目分类
     */
    @TableField(exist = false)
    private List<String> projectTypeIds;

    /**
     * 供应商关联账号登录名
     */
    @Excel(name = "供应商账号")
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


    @ApiModelProperty(value = "营业执照")
    private String yyzz;

    @ApiModelProperty(value = "开户许可")
    private String khxk;

    @ApiModelProperty(value = "授权委托")
    private String sqwt;

    @ApiModelProperty(value = "体系证书")
    private String txzs;

    @ApiModelProperty(value = "产品代理")
    private String cpdl;


    @ApiModelProperty(value = "营业执照名称")
    @TableField(exist = false)
    private String yyzzName;

    @ApiModelProperty(value = "开户许可名称")
    @TableField(exist = false)
    private String khxkName;

    @ApiModelProperty(value = "授权委托名称")
    @TableField(exist = false)
    private String sqwtName;

    @ApiModelProperty(value = "体系证书名称")
    @TableField(exist = false)
    private String txzsName;

    @ApiModelProperty(value = "产品代理名称")
    @TableField(exist = false)
    private String cpdlName;

}
