package com.zzc.easyExcel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.zzc.easyExcel.AnnotationType;
import com.zzc.easyExcel.CustomUtils;
import com.zzc.easyExcel.example.SupplierInfoExportDTO;
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
 * Gm客户列表合并单元格策略
 * @author Administrator
 */
public class GmSupplierMergeStrategy extends AbstractMergeStrategy {

    /**
     * 需要合并的列数
     */
    private Map<Integer, Field> columnMergeMap;
    /**
     * Map<尾行index，往上merge（合并）的行数>
     */
    private final Map<Integer, Long> mergeInfoMap = new HashMap<>();

    public GmSupplierMergeStrategy(@Nonnull final List<?> data, @Nonnull final Class<?> clazz) {
        List<SupplierInfoExportDTO> list = (List<SupplierInfoExportDTO>) data;
        if (list.isEmpty()) {
            return;
        }
        columnMergeMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.MERGER);
        if (columnMergeMap.size() == 0) {
            return;
        }
        Map<String, Long> supplierMap = list.stream().collect(Collectors.groupingBy(SupplierInfoExportDTO::getUserId, Collectors.counting()));
        for (int rowIndex = 0; rowIndex < list.size(); rowIndex++) {
            String userId = list.get(rowIndex).getUserId();
            if (StringUtils.isNotBlank(userId)) {
                Long userIdCount = supplierMap.get(userId);
                if (userIdCount > 1) {
                    mergeInfoMap.put(rowIndex + userIdCount.intValue(), userIdCount - 1);
                    rowIndex += userIdCount - 1;
                }
            }
        }
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        CustomUtils.merge(sheet, cell, columnMergeMap, mergeInfoMap);
    }

}
