package com.example.ccbim.ccbimpoi.data;

import java.util.ArrayList;

/**
 * Created by lgf on 2019/3/14.
 */

public class ProjectCheckData {
    private ArrayList<CellData> tabHead;      //表格的头部
    private ArrayList<CellData> tabBody;      //表格的body，验收的项目
    private ArrayList<CellData> tabFoot;       //表格的底部
    private String rowHeight;       //行高
    private String colWidth;       //列宽
    private String headRows;         //头部的行
    private String footRows;         //底部的行
//    private ArrayList<CellData> tabPic;         //表格的附图样表


    public ArrayList<CellData> getTabHead() {
        return tabHead;
    }

    public void setTabHead(ArrayList<CellData> tabHead) {
        this.tabHead = tabHead;
    }

    public ArrayList<CellData> getTabBody() {
        return tabBody;
    }

    public void setTabBody(ArrayList<CellData> tabBody) {
        this.tabBody = tabBody;
    }

    public ArrayList<CellData> getTabFoot() {
        return tabFoot;
    }

    public void setTabFoot(ArrayList<CellData> tabFoot) {
        this.tabFoot = tabFoot;
    }

    public String getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(String rowHeight) {
        this.rowHeight = rowHeight;
    }

    public String getColWidth() {
        return colWidth;
    }

    public void setColWidth(String colWidth) {
        this.colWidth = colWidth;
    }

    public String getHeadRows() {
        return headRows;
    }

    public void setHeadRows(String headRows) {
        this.headRows = headRows;
    }

    public String getFootRows() {
        return footRows;
    }

    public void setFootRows(String footRows) {
        this.footRows = footRows;
    }
}
