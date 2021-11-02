package com.zzc.easyExcel.example;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzc.easyExcel.annotation.ExcelFieldMerge;
import com.zzc.easyExcel.converter.BooleanToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@ExcelIgnoreUnannotated
public class MarketTradeExcelExportDTO implements Serializable {
    private static final long serialVersionUID = -5770399878659723217L;

    @ExcelIgnore
    private String                        sellerStoreId;
    @ExcelIgnore
    private String                        buyerStoreId;
    @ExcelProperty("订单编号")
    @ExcelFieldMerge
    private String                        tradeId;
    @ExcelProperty("订单总额")
    @ExcelFieldMerge
    private BigDecimal                    totalTradeFee;
    @ExcelProperty("应付金额")
    @ExcelFieldMerge
    private BigDecimal                    totalShouldPayFee;
    @ExcelProperty("实付金额")
    @ExcelFieldMerge
    private BigDecimal                    actuallyPaidFee;
    @ExcelProperty("配送费用")
    @ExcelFieldMerge
    private BigDecimal                    logisticsFee;
    @ExcelProperty("发票费用")
    @ExcelFieldMerge
    private BigDecimal                    invoiceFee;
    @ExcelProperty("支付方式")
    @ExcelFieldMerge
    private String                        payTypeName;
    @ExcelProperty("配送方式")
    @ExcelFieldMerge
    private String                        shipMethodName;
    @ExcelProperty("客户名称")
    @ExcelFieldMerge
    private String                        buyCompanyName;
    @ExcelProperty("店铺名称")
    @ExcelFieldMerge
    private String                        buyStoreName;
    @ExcelProperty("店铺编号")
    @ExcelFieldMerge
    private String                        buyStoreCode;
    @ExcelProperty("店铺类型")
    @ExcelFieldMerge
    private String                        buySceneName;
    @ExcelProperty("联系人姓名")
    @ExcelFieldMerge
    private String                        buyRealName;
    @ExcelProperty("联系人手机")
    @ExcelFieldMerge
    private String                        buyPhone;
    @ExcelProperty("客户登录名")
    @ExcelFieldMerge
    private String                        buyNickName;
    @ExcelProperty("下单时间")
    @ExcelFieldMerge
    private Date                          createTime;
    @ExcelProperty("发货时间")
    @ExcelFieldMerge
    private Date                          deliverTime;
    @ExcelProperty("付款状态")
    @ExcelFieldMerge
    private String                        payStatusName;
    @ExcelProperty("发货状态")
    @ExcelFieldMerge
    private String                        deliverStatusName;
    @ExcelProperty("订单状态")
    @ExcelFieldMerge
    private String                        completeStatusName;
    @ExcelProperty("核销状态")
    @ExcelFieldMerge
    private String                        verificationStatusName;
    @ExcelProperty(value = "是否开票", converter = BooleanToStringConverter.class)
    @ExcelFieldMerge
    private boolean                       invoiceFlag;
    @ExcelProperty("发票类型")
    @ExcelFieldMerge
    private String                        invoiceTypeName;
    @ExcelProperty("付款日期")
    @ExcelFieldMerge
    private Date                          paidTime;
    @ExcelProperty("代下单人名称")
    @ExcelFieldMerge
    private String                        addUserName;
    @ExcelProperty("订单附言")
    @ExcelFieldMerge
    private String                        buyerRemark;
    @ExcelProperty("卖家备注")
    @ExcelFieldMerge
    private String                        sellerRemark;
    @ExcelProperty("支付渠道")
    @ExcelFieldMerge
    private String                        paymentChannelName;
    @ExcelProperty("供货商店铺名称")
    @ExcelFieldMerge
    private String                        sellerStoreName;
    @ExcelIgnore
    private String                        marketSaleManId;
    @ExcelProperty("客户经理")
    @ExcelFieldMerge
    private String                        marketSaleManName;
    @ExcelIgnore
    private String                        sellerMobile;
    @ExcelProperty(value = "是否小程序下单", converter = BooleanToStringConverter.class)
    @ExcelFieldMerge
    private boolean                       fromB2bApp;
    @ExcelProperty("用户姓名")
    @ExcelFieldMerge
    private String                        buyerName;
    @ExcelProperty("用户电话")
    @ExcelFieldMerge
    private String                        buyerPhone;
    @ExcelProperty("用户手机")
    @ExcelFieldMerge
    private String                        buyerMobile;
    @ExcelProperty("用户地区")
    @ExcelFieldMerge
    private String                        buyerAddressStreet;
    @ExcelProperty("用户地址")
    @ExcelFieldMerge
    private String                        buyerAddressDetail;
    @ExcelProperty("用户邮编")
    @ExcelFieldMerge
    private String                        buyerZipCode;

    //@ExcelIgnore
    //private List<MarketTradeItemExcelDTO> items;
    ///**
    // * 下面是订单商品数据
    // * EasyExcel不支持List数据类型导出，这边放在一起，然后根据自定义合并策略实现类似List的操作
    // */
    //@ExcelProperty("SKU商家编码")
    //private String     businessId;
    //@ExcelProperty("商品名称")
    //private String     skuName;
    //@ExcelProperty("商品图片")
    //private String     image;
    //@ExcelProperty("购买数量")
    //private BigDecimal buyCount;
    //@ExcelProperty("规格型号")
    //private String     spec;
    //@ExcelProperty("单位")
    //private String     unit;
    //@ExcelProperty("原价")
    //private BigDecimal initItemPrice;
    //@ExcelProperty("销售价")
    //private BigDecimal price;
    //@ExcelProperty("单价")
    //private BigDecimal perValue;
    //@ExcelProperty("金额")
    //private BigDecimal totalPrice;
    //@ExcelProperty("条形码")
    //private String     barcode;
    //@ExcelProperty("重量")
    //private BigDecimal weight;
    //@ExcelProperty("商品备注")
    //private String     remark;
}
