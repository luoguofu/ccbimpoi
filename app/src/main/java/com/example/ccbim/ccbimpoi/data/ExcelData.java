package com.example.ccbim.ccbimpoi.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

/**
 * Created by lgf on 2019/4/19.
 */
@Table(name = "excel_data")
public class ExcelData extends BaseData {
    @Id
    private Integer id;
    private String excelJson;
    private String path;
    private String excelName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExcelJson() {
        return excelJson;
    }

    public void setExcelJson(String excelJson) {
        this.excelJson = excelJson;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }
}
