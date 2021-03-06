package com.zzc.utils.easyExcel.strategy;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.zzc.utils.easyExcel.AnnotationType;
import com.zzc.utils.easyExcel.CustomUtils;
import com.zzc.utils.easyExcel.example.SupplierInfoExportDTO;
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
 * Gm客户列表合并单元格策略
 * @author Administrator
 */
public class GmSupplierMergeStrategy extends AbstractMergeStrategy {

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

    public GmSupplierMergeStrategy(@Nonnull final List<?> data, @Nonnull final Class<?> clazz) {
        List<SupplierInfoExportDTO> list       = (List<SupplierInfoExportDTO>) data;
        int                         totalRowNo = list.size() + 1;
        if (list.size() == 0) {
            return;
        }
        columnMergeMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.MERGER);
        if (columnMergeMap.size() == 0) {
            return;
        }
        Map<String, Long> supplierMap = list.stream().collect(Collectors.groupingBy(SupplierInfoExportDTO::getUserId, Collectors.counting()));
        String lastUserId = "";
        for (int i = 0; i < list.size(); i++) {
            String userId = list.get(i).getUserId();
            if (userId.equals(lastUserId)) {
                lastUserId = userId;
                continue;
            }
            Long userIdCount = supplierMap.get(userId);
            if (userIdCount > 1) {
                mergeInfoMap.put(totalRowNo - list.size() + i, userIdCount - 1);
                //修复EasyExcel合并单元格后求和重复计算Bug，记录要清除内容的行index
                for (int j = 1; j <= userIdCount - 1; j++) {
                    blankRowList.add(totalRowNo - list.size() + i + j);
                }
            }
            lastUserId = userId;
        }
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        CustomUtils.merge(sheet, cell, columnMergeMap, mergeInfoMap, blankRowList);
    }

}
