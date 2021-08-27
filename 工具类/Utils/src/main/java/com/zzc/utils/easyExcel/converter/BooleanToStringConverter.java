package com.zzc.utils.easyExcel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.util.BooleanUtils;

/**
 * 默认是返回 TRUE 和 FALSE  这边转换成中文"是、否"
 * @author Administrator
 */
public class BooleanToStringConverter implements Converter<Boolean> {

    @Override
    public Class supportJavaTypeKey() {
        return Boolean.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Boolean convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
                                     GlobalConfiguration globalConfiguration) {
        return BooleanUtils.valueOf(cellData.getStringValue());
    }

    @Override
    public CellData convertToExcelData(Boolean value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (null != value && value) {
            return new CellData("是");
        } else {
            return new CellData("否");
        }
    }
}
