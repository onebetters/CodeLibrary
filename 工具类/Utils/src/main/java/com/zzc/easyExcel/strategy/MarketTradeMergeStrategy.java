package com.zzc.easyExcel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.zzc.easyExcel.AnnotationType;
import com.zzc.easyExcel.CustomUtils;
import com.zzc.easyExcel.example.MarketTradeExcelExportDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 市场销售单合并单元格策略
 * 废弃原因：订单数据量上来后，自定义合并单元格会非常慢，等待官方除了合并单元格后再修复
 * @author Administrator
 */
public class MarketTradeMergeStrategy extends AbstractMergeStrategy {

    /**
     * 需要合并的列数
     */
    private Map<Integer, Field> columnMergeMap;
    /**
     * Map<行index，往下merge（合并）的行数>
     */
    private final Map<Integer, Long> mergeInfoMap = new HashMap<>();

    /**
     * 需要清除内容的行index
     */
    private final List<Integer> blankRowList = new ArrayList<>();

    public MarketTradeMergeStrategy(@Nonnull final List<?> data, @Nonnull final Class<?> clazz) {
        List<MarketTradeExcelExportDTO> list       = (List<MarketTradeExcelExportDTO>) data;
        int                             totalRowNo = list.size() + 1;
        if (list.size() == 0) {
            return;
        }
        columnMergeMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.MERGER);
        if (columnMergeMap.size() == 0) {
            return;
        }
        Map<String, Long> tradeMap = list.stream().collect(Collectors.groupingBy(MarketTradeExcelExportDTO::getTradeId, Collectors.counting()));
        String lastOrderNo = "";
        for (int i = 0; i < list.size(); i++) {
            String orderNo = list.get(i).getTradeId();
            if (orderNo.equals(lastOrderNo)) {
                lastOrderNo = orderNo;
                continue;
            }
            Long orderNoCount = tradeMap.get(orderNo);
            if (orderNoCount > 1) {
                mergeInfoMap.put(totalRowNo - list.size() + i, orderNoCount - 1);
                //修复EasyExcel合并单元格后求和重复计算Bug，记录要清除内容的行index
                for (int j = 1; j <= orderNoCount - 1; j++) {
                    blankRowList.add(totalRowNo - list.size() + i + j);
                }
            }
            lastOrderNo = orderNo;
        }
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        CustomUtils.merge(sheet, cell, columnMergeMap, mergeInfoMap, blankRowList);
    }

}
