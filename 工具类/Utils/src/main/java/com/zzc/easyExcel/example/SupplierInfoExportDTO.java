package com.zzc.easyExcel.example;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@ExcelIgnoreUnannotated
public class SupplierInfoExportDTO implements Serializable {

    private static final long serialVersionUID = -628931439892830891L;

    @ExcelProperty("客户关系编号")
    @ExcelFieldMerge
    private String userId;

    @ExcelProperty("店铺编号")
    @ExcelFieldMerge
    private String storeId;

    @ExcelProperty("商家店铺")
    @ExcelFieldMerge
    private String storeName;

    @ExcelProperty("店铺类型")
    @ExcelFieldMerge
    private String sceneBName;

    @ExcelIgnore
    private String ticketId;

    /**
     * 联系人姓名(目前只能为千米ID的昵称)
     */
    @ExcelProperty("联系人姓名")
    @ExcelFieldMerge
    private String nickName;

    @ExcelProperty("联系人手机")
    @ExcelFieldMerge
    private String cellphone;

    /**
     * 用户审核状态
     */
    @ExcelIgnore
    private String auditStatus;

    @ExcelIgnore
    private String sid;

    /**
     * 店铺状态(1:正常 0:已打烊)
     */
    @ExcelIgnore
    private String sceneStatus;

    /**
     * 主营类目
     */
    @ExcelProperty("行业")
    @ExcelFieldMerge
    private String jobType;

    /**
     * 签约状态(0:未签约（免费） 2:已签约（已购买）)
     */
    @ExcelProperty("签约状态")
    @ExcelFieldMerge
    private String signStatus;

    @ExcelProperty("店铺地址")
    @ExcelFieldMerge
    private String detailAddress;

    @ExcelProperty("店铺创建时间")
    @ExcelFieldMerge
    private Date createTime;

    @ExcelProperty("入驻市场时间")
    @ExcelFieldMerge
    private Date joinTime;

    //方便合并单元格，拆开
    //@ExcelProperty
    //@ApiModelProperty(value = "入驻的市场列表（成为供货商）")
    //private List<JoinMarketInfoDTO> joinMarketInfos;

    @ExcelIgnore
    private String shopId;

    @ExcelIgnore
    private String marketId;

    /**
     * 加入的市场名称
     */
    @ExcelProperty("所属市场")
    private String marketName;
}
