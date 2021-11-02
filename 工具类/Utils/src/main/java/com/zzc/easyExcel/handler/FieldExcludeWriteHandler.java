package com.zzc.easyExcel.handler;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.zzc.easyExcel.AnnotationType;
import com.zzc.easyExcel.CustomUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：导出Excel时排除不需要的字段
 *
 * 使用：
 *      数据写入时，记得注册FieldExcludeWriteHandler。
 *      例如：
 *          WriteSheet writeSheet = EasyExcel.writerSheet().build();
 *          //判断满足自己条件后，注册FieldExcludeWriteHandler
 *          writeSheet.getCustomWriteHandlerList().add(new FieldExcludeWriteHandler(xxx.class));
 *
 * @author Administrator
 */
@Slf4j
public class FieldExcludeWriteHandler implements SheetWriteHandler {

    /**
     * 需排除的字段index
     */
    private final Map<Integer, Field> columnIndexMap;

    public FieldExcludeWriteHandler(@Nonnull final Class<?> clazz) {
        this.columnIndexMap = CustomUtils.getNeedColumnIndex(clazz, AnnotationType.EXCLUDE);
    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Map<Integer, Head> headMap   = writeSheetHolder.excelWriteHeadProperty().getHeadMap();
        Map<String, Field> ignoreMap = writeSheetHolder.excelWriteHeadProperty().getIgnoreMap();
        Map<Integer, ExcelContentProperty> contentPropertyMap = writeSheetHolder.excelWriteHeadProperty().getContentPropertyMap();
        if (null != columnIndexMap && columnIndexMap.size() > 0) {
            columnIndexMap.forEach((index, field) -> {
                headMap.remove(index);
                contentPropertyMap.remove(index);
                ignoreMap.put(field.getName(), field);
            });

            Map<Integer, Head>                 newHeadMap            = new HashMap<>(headMap.size());
            Map<Integer, ExcelContentProperty> newContentPropertyMap = new HashMap<>(headMap.size());
            //排除字段后要更新index，否则Excel中会出现空列
            int index = 0;
            for (int i = 0; i < headMap.size(); i++) {
                Head                 head;
                ExcelContentProperty contentProperty;

                do {
                    head = headMap.get(index);
                    contentProperty = contentPropertyMap.get(index);
                    index++;
                } while (null == head);
                head.setColumnIndex(i);
                contentProperty.setHead(head);

                newHeadMap.put(i, head);
                newContentPropertyMap.put(i, contentProperty);
            }
            writeSheetHolder.excelWriteHeadProperty().setHeadMap(newHeadMap);
            writeSheetHolder.excelWriteHeadProperty().setContentPropertyMap(newContentPropertyMap);
            writeSheetHolder.excelWriteHeadProperty().setIgnoreMap(ignoreMap);
        }
    }
}
