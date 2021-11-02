package com.zzc.easyExcel.example;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.zzc.dateutil.DateTimeUtils;
import com.zzc.limitApi.LimitApi;
import com.zzc.easyExcel.ResponseBuilder;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RestController
public class Main {

    @RequestMapping("/exportTrade")
    @LimitApi(expire = 60)
    public void exportTrade(final HttpServletResponse response) throws IOException {

        ResponseBuilder.setExportResponseHeader(response, "市场销售单导出-" + DateTimeUtils.parse(new Date(), "yyyyMMddHHmmss"));
        @Cleanup("finish") ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();

        // WriteSheet groupBySellerSheet = EasyExcel.writerSheet(0, "按店铺汇总")
        //                                          .head(MarketTradeExcelGroupBySellerDTO.class)
        //                                          .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //                                          .registerWriteHandler(new FieldSumWriteHandler(MarketTradeExcelGroupBySellerDTO.class))
        //                                          .build();
        // WriteSheet groupByBuyerSheet  = EasyExcel.writerSheet(1, "按买家汇总")
        //                                          .head(MarketTradeExcelGroupByBuyerDTO.class)
        //                                          .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //                                          .registerWriteHandler(new FieldSumWriteHandler(MarketTradeExcelGroupByBuyerDTO.class))
        //                                          .build();
        // WriteSheet tradesSheet = EasyExcel.writerSheet(2, "订单信息")
        //                                   .head(MarketTradeExcelDTO.class)
        //                                   .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //                                   //.registerWriteHandler(new MarketTradeMergeStrategy(trades, MarketTradeExcelExportDTO.class))
        //                                   .build();
        // WriteSheet tradesMarketingSheet = EasyExcel.writerSheet(3, "订单营销信息")
        //                                            .head(MarketTradeMarketingExcelDTO.class)
        //                                            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //                                            .build();
        // WriteSheet tradeItemsSheet = EasyExcel.writerSheet(4, "订单商品信息")
        //                                       .head(MarketTradeItemExcelExportDTO.class)
        //                                       .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
        //                                       .build();
        // excelWriter.write(groupBySeller, groupBySellerSheet);
        // excelWriter.write(groupByBuyer, groupByBuyerSheet);
        // excelWriter.write(trades, tradesSheet);
        // excelWriter.write(trades.stream().map(MarketTradeExcelDTO::getTradeMarketing).collect(Collectors.toList()), tradesMarketingSheet);
        // excelWriter.write(tradeItemsDTO, tradeItemsSheet);
    }
}
