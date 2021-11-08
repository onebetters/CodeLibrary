package com.zzc.easyExcel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.zzc.easyExcel.AnnotationType;
import com.zzc.easyExcel.CustomUtils;
import com.zzc.easyExcel.example.MarketTradeExcelExportDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
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
     * Map<尾行index，往上merge（合并）的行数>
     */
    private final Map<Integer, Long> mergeInfoMap = new HashMap<>();

    public MarketTradeMergeStrategy(@Nonnull final List<?> data, @Nonnull final Class<?> clazz) {
        List<MarketTradeExcelExportDTO> list = (List<MarketTradeExcelExportDTO>) data;
        if (list.isEmpty()) {
            return;
        }
        columnMergeMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.MERGER);
        if (columnMergeMap.size() == 0) {
            return;
        }
        Map<String, Long> tradeMap = list.stream().collect(Collectors.groupingBy(MarketTradeExcelExportDTO::getTradeId, Collectors.counting()));
        for (int rowIndex = 0; rowIndex < list.size(); rowIndex++) {
            String orderNo = list.get(rowIndex).getTradeId();
            if (StringUtils.isNotBlank(orderNo)) {
                Long orderNoCount = tradeMap.get(orderNo);
                if (orderNoCount > 1) {
                    mergeInfoMap.put(rowIndex + orderNoCount.intValue(), orderNoCount - 1);
                    rowIndex += orderNoCount - 1;
                }
            }
        }
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        CustomUtils.merge(sheet, cell, columnMergeMap, mergeInfoMap);
    }

}
