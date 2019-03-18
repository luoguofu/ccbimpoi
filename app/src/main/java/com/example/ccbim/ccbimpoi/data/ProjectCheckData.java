package com.example.ccbim.ccbimpoi.data;

import com.weqia.utils.annotation.sqlite.Id;
import com.weqia.utils.annotation.sqlite.Table;
import com.weqia.wq.data.BaseData;

import java.util.ArrayList;

/**
 * Created by lgf on 2019/3/14.
 */
@Table(name = "project_check_data")
public class ProjectCheckData extends BaseData{
    @Id
    private int id;
    private ArrayList<CellData> tabHead;      //表格的头部
    private ArrayList<CellData> tabBody;      //表格的body，验收的项目
    private ArrayList<CellData> tabFoot;       //表格的底部
    private String tabHeadStr;      //表格的头部
    private String tabBodyStr;      //表格的body，验收的项目
    private String tabFootStr;       //表格的底部
    private String rowHeight;       //行高
    private String colWidth;       //列宽
    private String headRows;         //头部的行
    private String footRows;         //底部的行
    private String checkPartName;     //验收部位名称
    private int completeStatus = 0;         //0表示未完成，1表示已完成
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

    public String getCheckPartName() {
        return checkPartName;
    }

    public void setCheckPartName(String checkPartName) {
        this.checkPartName = checkPartName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTabHeadStr() {
        return tabHeadStr;
    }

    public void setTabHeadStr(String tabHeadStr) {
        this.tabHeadStr = tabHeadStr;
    }

    public String getTabBodyStr() {
        return tabBodyStr;
    }

    public void setTabBodyStr(String tabBodyStr) {
        this.tabBodyStr = tabBodyStr;
    }

    public String getTabFootStr() {
        return tabFootStr;
    }

    public void setTabFootStr(String tabFootStr) {
        this.tabFootStr = tabFootStr;
    }

    public int getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(int completeStatus) {
        this.completeStatus = completeStatus;
    }
}
