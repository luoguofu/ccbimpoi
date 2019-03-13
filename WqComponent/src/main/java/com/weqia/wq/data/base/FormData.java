package com.weqia.wq.data.base;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.wq.data.BaseData;

/**
 * Created by lgf on 2018/12/10.
 */

public class FormData extends BaseData {
    @Id
    private String fieldId;              //表单字段的id
    private String fieldName;
    private String fieldType;           //表单字段的类型
    private String displayName;            //表单字段显示的名称
    private String defaultValue;            //表单所有选项
    private String orderNum;
    private String fieldVal;                  //表单字段内容
    private String initOption;
    private String fieldDataId;       //编辑是需要添加的id
    public enum FormType{
        LABEL("label","问题类型"),
        INPUT("input","输入框"),
        TEXTAREA("textarea","文本域"),
        SELECT("select","下拉选择框"),
        RADIO("radio","单选按钮"),
        CHECK("check","多选框"),
        ;
        private String value;
        private String strName;
        FormType(String value, String strName) {
            this.value = value;
            this.strName = strName;
        }


        public String strName() {
            return strName;
        }

        public String order() {
            return value;
        }
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }

    public String getInitOption() {
        return initOption;
    }

    public void setInitOption(String initOption) {
        this.initOption = initOption;
    }

    public String getFieldDataId() {
        return fieldDataId;
    }

    public void setFieldDataId(String fieldDataId) {
        this.fieldDataId = fieldDataId;
    }
}
