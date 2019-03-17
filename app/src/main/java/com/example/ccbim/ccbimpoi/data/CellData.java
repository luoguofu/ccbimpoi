package com.example.ccbim.ccbimpoi.data;

import com.weqia.wq.data.BaseData;

import java.util.ArrayList;

/**
 * Created by lgf on 2019/3/14.
 */

public class CellData extends BaseData {
    private ArrayList<CheckDetailData> subCellList;
    private String cellName;          //单元格名称
    private String cellSelected;          //单元格选中与否
    private String cellWidth;          //单元格宽
    private String cellHeight;         //单元格高
    private String cellColor;           //单元格填充颜色
    private String cellFontColor;           //单元格字体颜色
    private String firtRow;               //单元格起始行
    private String firtColumn;           //单元格起始列
    private String lastRow;                //单元格结束行
    private String lastColumn;              //单元格结束列
    private ArrayList<String> picPath;        //图片路径
    private int cellLayout = 1;                   //单元格的布局（居中等，默认是居中）
    private String fontName;                       //字体名称
    private short fontSize;                       //字体大小

    public CellData() {

    }
    public CellData(String cellName, String firtRow, String firtColumn) {
        this.cellName = cellName;
        this.firtRow = firtRow;
        this.firtColumn = firtColumn;
    }

    public CellData(String cellName, String firtRow, String lastRow, String firtColumn, String lastColumn) {
        this.cellName = cellName;
        this.firtRow = firtRow;
        this.firtColumn = firtColumn;
        this.lastRow = lastRow;
        this.lastColumn = lastColumn;
    }

    public ArrayList<CheckDetailData> getSubCellList() {
        return subCellList;
    }

    public void setSubCellList(ArrayList<CheckDetailData> subCellList) {
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

    public int getCellLayout() {
        return cellLayout;
    }

    public void setCellLayout(int cellLayout) {
        this.cellLayout = cellLayout;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public short getFontSize() {
        return fontSize;
    }

    public void setFontSize(short fontSize) {
        this.fontSize = fontSize;
    }
}
