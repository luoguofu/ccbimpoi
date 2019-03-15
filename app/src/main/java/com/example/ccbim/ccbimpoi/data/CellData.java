package com.example.ccbim.ccbimpoi.data;

import java.util.ArrayList;

/**
 * Created by lgf on 2019/3/14.
 */

public class CellData {
    private ArrayList<ChecDetailData> subCellList;
    private String cellName;          //单元格名称
    private String cellSelected;          //单元格选中与否
    private String cellWidth;          //单元格宽
    private String cellHeight;         //单元格高
    private String cellColor;           //单元格颜色
    private String cellFontColor;           //单元格字体颜色
    private String firtRow;               //单元格起始行
    private String firtColumn;           //单元格起始列
    private String lastRow;                //单元格结束行
    private String lastColumn;              //单元格结束列
    private ArrayList<String> picPath;        //图片路径

    public ArrayList<ChecDetailData> getSubCellList() {
        return subCellList;
    }

    public void setSubCellList(ArrayList<ChecDetailData> subCellList) {
        this.subCellList = subCellList;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getCellSelected() {
        return cellSelected;
    }

    public void setCellSelected(String cellSelected) {
        this.cellSelected = cellSelected;
    }

    public String getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(String cellWidth) {
        this.cellWidth = cellWidth;
    }

    public String getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(String cellHeight) {
        this.cellHeight = cellHeight;
    }

    public String getCellColor() {
        return cellColor;
    }

    public void setCellColor(String cellColor) {
        this.cellColor = cellColor;
    }

    public String getCellFontColor() {
        return cellFontColor;
    }

    public void setCellFontColor(String cellFontColor) {
        this.cellFontColor = cellFontColor;
    }

    public String getFirtRow() {
        return firtRow;
    }

    public void setFirtRow(String firtRow) {
        this.firtRow = firtRow;
    }

    public String getFirtColumn() {
        return firtColumn;
    }

    public void setFirtColumn(String firtColumn) {
        this.firtColumn = firtColumn;
    }

    public String getLastRow() {
        return lastRow;
    }

    public void setLastRow(String lastRow) {
        this.lastRow = lastRow;
    }

    public String getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(String lastColumn) {
        this.lastColumn = lastColumn;
    }

    public ArrayList<String> getPicPath() {
        return picPath;
    }

    public void setPicPath(ArrayList<String> picPath) {
        this.picPath = picPath;
    }
}
