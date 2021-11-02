package com.zzc.easyExcel.example;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoExportDTO implements Serializable {

    private static final long serialVersionUID = 545637510711355800L;

    @ExcelProperty("客户关系编号")
    @ExcelFieldMerge
    private String userId;

    @ExcelProperty("客户店铺")
    @ExcelFieldMerge
    private String storeName;

    @ExcelProperty("店铺编号")
    @ExcelFieldMerge
    private String storeId;

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

    @ExcelProperty("店铺地址")
    @ExcelFieldMerge
    private String detailAddress;

    /**
     * 主营类目
     */
    @ExcelProperty("行业")
    @ExcelFieldMerge
    private String jobType;

    @ExcelProperty("签约状态")
    @ExcelFieldMerge
    private String signStatus;

    @ExcelProperty("店铺创建时间")
    @ExcelFieldMerge
    private Date createTime;

    @ExcelProperty("加入市场时间")
    @ExcelFieldMerge
    private Date joinTime;

    //方便合并单元格，拆开
    //@ExcelProperty
    //@ApiModelProperty(value = "加入的市场列表（成为客户）")
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
