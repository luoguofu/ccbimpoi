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

}
