package com.example.ccbim.ccbimpoi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.alibaba.fastjson.JSON;
import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.ExcelEnum;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.example.ccbim.ccbimpoi.util.SaveToExcelUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainNewActivity extends Activity implements View.OnClickListener {
    private Button bt_export_excel, bt_read_excel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
//        System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
//        System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        bt_export_excel = findViewById(R.id.bt_export_excel);
        bt_read_excel = findViewById(R.id.bt_read_excel);
        bt_export_excel.setOnClickListener(this);
        bt_read_excel.setOnClickListener(this);
    }
    public static String getExcelDir() {
        // SD卡指定文件夹
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString();
        File dir = new File(sdcardPath + File.separator + "Excel"
                + File.separator + "Person");

        if (dir.exists()) {
            return dir.toString();

        } else {
            dir.mkdirs();
            Log.e("TAG", "保存路径不存在,");
            return dir.toString();
        }
    }
    public static String getPoiExcelDir() {
        // SD卡指定文件夹
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString();
        File dir = new File(sdcardPath + File.separator + "Excel"
                + File.separator + "ExcelFile");

        if (dir.exists()) {
            return dir.toString();

        } else {
            dir.mkdirs();
            Log.e("TAG", "保存路径不存在,");
            return dir.toString();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_export_excel:
                ProjectCheckData data = JSON.parseObject(ExcelEnum.TOILETCHECKEXCEL.getValue(), ProjectCheckData.class);
/*                ProjectCheckData data = new ProjectCheckData();
                String colWidth = "6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6";
                data.setColWidth(colWidth);
                String rowHeight = "1,1,1,1,1,1,1,1,1,4,3,2,2,3,3,3,3,3,3,3,4,2,2,2";
                data.setRowHeight(rowHeight);
                data.setColWidth(colWidth);
                String headRows = "1,3,4,5,6,7,8,9";
                data.setHeadRows(headRows);
                String footRows = "21,22,23";
                data.setFootRows(footRows);
                ArrayList<CellData> tabHead = new ArrayList<>();
                CellData nameCell = new CellData("表格样表", 1 + "", "1", "0", "7");
                nameCell.setCellColor("ffff00");
                tabHead.add(nameCell);
                tabHead.add(new CellData("卫生间防渗漏验收记录表",3 + "", "3", "0", "7"));
                tabHead.add(new CellData("单位工程名称","4", "4", "0", "1"));
                tabHead.add(new CellData("xxx","4", "4", "2", "7"));
                tabHead.add(new CellData("分部工程名称","5", "5", "0", "1"));
                tabHead.add(new CellData("卫生间防水","5", "5", "2", "3"));
                tabHead.add(new CellData("验收时间","5", "5", "4", "5"));
                tabHead.add(new CellData("","5", "5", "6", "7"));
                tabHead.add(new CellData("施工单位","6", "6", "0", "1"));
                tabHead.add(new CellData("xxx","6", "6", "2", "3"));
                tabHead.add(new CellData("验收部位","6", "6", "4", "5"));
                tabHead.add(new CellData("","6", "6", "6", "7"));
                tabHead.add(new CellData("验收项目","7", "8", "0", "0"));
                tabHead.add(new CellData("验收标准","7", "8", "1", "4"));
                tabHead.add(new CellData("验收结果","7", "7", "5", "6"));
                tabHead.add(new CellData("符合要求","8", "5"));
                tabHead.add(new CellData("不涉及","8", "6"));
                tabHead.add(new CellData("附图","7", "8", "7", "7"));
                data.setTabHead(tabHead);
                ArrayList<CellData> tabBody = new ArrayList<>();
                CellData buildWater = new CellData("结构闭水", "9", "12", "0", "0");
                ArrayList<CheckDetailData> subBuildWater = new ArrayList<>();
                CheckDetailData checkDetailData11 = new CheckDetailData();
                checkDetailData11.setCheckName(new CellData("排水管洞周围处理", "9", "1"));
                checkDetailData11.setCheckStandard(new CellData("必须分两次封堵，第一次封堵至2/3，第二次封堵完成，分两次蓄水试验，每次不低于48小时，蓄水深度无地暖的230mm，有地暖的330mm确保不渗漏", "9", "9", "2", "4"));
                checkDetailData11.setCheckPass(new CellData("口","9","5"));
                checkDetailData11.setCheckInvolve(new CellData("口","9","6"));
                checkDetailData11.setCheckPic(new CellData("", "9", "7"));
                subBuildWater.add(checkDetailData11);
                CheckDetailData checkDetailData12 = new CheckDetailData();
                checkDetailData12.setCheckName(new CellData("管井保护", "10", "1"));
                checkDetailData12.setCheckStandard(new CellData("管洞封堵确保不渗漏后，根部浇筑不少于300mm高的细石砼台，全部包裹所有的排水管，然后再在其上做管井", "10", "10", "2", "4"));
                checkDetailData12.setCheckPass(new CellData("口","10","5"));
                checkDetailData12.setCheckInvolve(new CellData("口","10","6"));
                checkDetailData12.setCheckPic(new CellData("", "10", "7"));
                subBuildWater.add(checkDetailData12);
                CheckDetailData checkDetailData13 = new CheckDetailData();
                checkDetailData13.setCheckName(new CellData("风井保护", "11", "1"));
                checkDetailData13.setCheckStandard(new CellData("风道安装后在其根部浇不少于300mm 高的细石砼保护坎", "11", "11", "2", "4"));
                checkDetailData13.setCheckPass(new CellData("口","11","5"));
                checkDetailData13.setCheckInvolve(new CellData("口","11","6"));
                checkDetailData13.setCheckPic(new CellData("", "11", "7"));
                subBuildWater.add(checkDetailData13);
                CheckDetailData checkDetailData14 = new CheckDetailData();
                checkDetailData14.setCheckName(new CellData("螺栓防水", "12", "1"));
                checkDetailData14.setCheckStandard(new CellData("螺栓周边范围 150mm 内刷防水附加层，厚度不少于 1.5mm", "12", "12", "2", "4"));
                checkDetailData14.setCheckPass(new CellData("口","12","5"));
                checkDetailData14.setCheckInvolve(new CellData("口","12","6"));
                checkDetailData14.setCheckPic(new CellData("", "12", "7"));
                subBuildWater.add(checkDetailData14);
                buildWater.setSubCellList(subBuildWater);
                tabBody.add(buildWater);

                CellData checkWater = new CellData("防水验收", "13", "18", "0", "0");
                ArrayList<CheckDetailData> subCheckWater = new ArrayList<>();
                CheckDetailData checkDetailData21 = new CheckDetailData();
                checkDetailData21.setCheckName(new CellData("基层清理", "13", "1"));
                checkDetailData21.setCheckStandard(new CellData("基层表面应平整，不得有空鼓、起砂、开裂等缺陷。基层含水率应符合防水材料的施工要求", "13", "13", "2", "4"));
                checkDetailData21.setCheckPass(new CellData("口","13","5"));
                checkDetailData21.setCheckInvolve(new CellData("口","13","6"));
                checkDetailData21.setCheckPic(new CellData("", "13", "7"));
                subCheckWater.add(checkDetailData21);
                CheckDetailData checkDetailData22 = new CheckDetailData();
                checkDetailData22.setCheckName(new CellData("防水找平阴阳角", "14", "1"));
                checkDetailData22.setCheckStandard(new CellData("防水水泥砂浆找平层与基础结合密实，无空鼓，表面平整光洁、无裂缝、起砂，阴阳角做成圆弧形", "14", "14", "2", "4"));
                checkDetailData22.setCheckPass(new CellData("口","14","5"));
                checkDetailData22.setCheckInvolve(new CellData("口","14","6"));
                checkDetailData22.setCheckPic(new CellData("", "14", "7"));
                subCheckWater.add(checkDetailData22);
                CheckDetailData checkDetailData23 = new CheckDetailData();
                checkDetailData23.setCheckName(new CellData("涂膜厚度", "15", "1"));
                checkDetailData23.setCheckStandard(new CellData("涂膜防水层涂刷均匀，厚度满足产品技术规定的要求，一般厚度不少于1.5mm不露底", "15", "15", "2", "4"));
                checkDetailData23.setCheckPass(new CellData("口","15","5"));
                checkDetailData23.setCheckInvolve(new CellData("口","15","6"));
                checkDetailData23.setCheckPic(new CellData("", "15", "7"));
                subCheckWater.add(checkDetailData23);
                CheckDetailData checkDetailData24 = new CheckDetailData();
                checkDetailData24.setCheckName(new CellData("防水层高度", "16", "1"));
                checkDetailData24.setCheckStandard(new CellData("防水层应从地面延伸到墙面，高出地面250mm。浴室墙面的防水度不得低于1800mm", "16", "16", "2", "4"));
                checkDetailData24.setCheckPass(new CellData("口","16","5"));
                checkDetailData24.setCheckInvolve(new CellData("口","16","6"));
                checkDetailData24.setCheckPic(new CellData("", "16", "7"));
                subCheckWater.add(checkDetailData24);
                CheckDetailData checkDetailData25 = new CheckDetailData();
                checkDetailData25.setCheckName(new CellData("搭接要求", "17", "1"));
                checkDetailData25.setCheckStandard(new CellData("使用施工接茬应顺流水方向搭接，搭接宽度不小于100mm，使用两层及以上上下搭接时应错开幅宽的二分之一", "17", "17", "2", "4"));
                checkDetailData25.setCheckPass(new CellData("口","17","5"));
                checkDetailData25.setCheckInvolve(new CellData("口","17","6"));
                checkDetailData25.setCheckPic(new CellData("", "17", "7"));
                subCheckWater.add(checkDetailData25);
                CheckDetailData checkDetailData26 = new CheckDetailData();
                checkDetailData26.setCheckName(new CellData("涂膜和各接缝处收头", "18", "1"));
                checkDetailData26.setCheckStandard(new CellData("涂膜表面不起泡、不流淌、平整无凹凸，与管件、洁具地脚、地漏、排水口接缝严密收头圆滑不渗漏", "18", "18", "2", "4"));
                checkDetailData26.setCheckPass(new CellData("口","18","5"));
                checkDetailData26.setCheckInvolve(new CellData("口","18","6"));
                checkDetailData26.setCheckPic(new CellData("", "18", "7"));
                subCheckWater.add(checkDetailData26);
                checkWater.setSubCellList(subCheckWater);
                tabBody.add(checkWater);

                CellData closeWater = new CellData("防水闭水", "19", "19", "0", "0");
                ArrayList<CheckDetailData> subCloseWater = new ArrayList<>();
                CheckDetailData checkDetailData31 = new CheckDetailData();
                checkDetailData31.setCheckName(new CellData("蓄水试验", "19", "1"));
                checkDetailData31.setCheckStandard(new CellData("防水材料铺设后，必须蓄水检验。蓄水深度应为20－30mm，24h内无渗漏为合格", "19", "19", "2", "4"));
                checkDetailData31.setCheckPass(new CellData("口","19","5"));
                checkDetailData31.setCheckInvolve(new CellData("口","19","6"));
                checkDetailData31.setCheckPic(new CellData("", "19", "7"));
                subCloseWater.add(checkDetailData31);
                closeWater.setSubCellList(subCloseWater);
                tabBody.add(closeWater);

                CellData protectWater = new CellData("保护层闭水", "20", "20", "0", "0");
                ArrayList<CheckDetailData> subProtectWater = new ArrayList<>();
                CheckDetailData checkDetailData41 = new CheckDetailData();
                checkDetailData41.setCheckName(new CellData("防水保护层", "20", "1"));
                checkDetailData41.setCheckStandard(new CellData("保护层水泥砂浆厚度、强度必须符合设计要求，操作时严禁破坏防水层，根据设计要求做好地面泛水坡度，排水要畅通、不得有积水倒坡现象", "20", "20", "2", "4"));
                checkDetailData41.setCheckPass(new CellData("口","20","5"));
                checkDetailData41.setCheckInvolve(new CellData("口","20","6"));
                checkDetailData41.setCheckPic(new CellData("", "20", "7"));
                subProtectWater.add(checkDetailData41);
                protectWater.setSubCellList(subProtectWater);
                tabBody.add(protectWater);

                data.setTabBody(tabBody);

                ArrayList<CellData> tabFoot = new ArrayList<>();
                tabFoot.add(new CellData("施工单位签字","21", "21", "0", "1"));
                tabFoot.add(new CellData("","21", "21", "2", "3"));
                tabFoot.add(new CellData("时间","21", "21", "4", "5"));
                tabFoot.add(new CellData("","21", "21", "6", "7"));
                tabFoot.add(new CellData("监理单位签字","22", "22", "0", "1"));
                tabFoot.add(new CellData("","22", "22", "2", "3"));
                tabFoot.add(new CellData("时间","22", "22", "4", "5"));
                tabFoot.add(new CellData("","22", "22", "6", "7"));
                tabFoot.add(new CellData("建设单位签字","23", "23", "0", "1"));
                tabFoot.add(new CellData("","23", "23", "2", "3"));
                tabFoot.add(new CellData("时间","23", "23", "4", "5"));
                tabFoot.add(new CellData("","23", "23", "6", "7"));

                data.setTabFoot(tabFoot);*/
//                SaveToExcelUtil util = new SaveToExcelUtil(this, getExcelDir() + File.separator + "demo.xls");
                SaveToExcelUtil.exportEccel(this, getExcelDir() + File.separator + "demo1.xls", data);
                break;
            case R.id.bt_read_excel:
/*                Excel_reader test= new Excel_reader();
                ArrayList<ArrayList<String>> arr= null;  //后面的参数代表需要输出哪些列，参数个数可以任意
                try {
*//*                    arr = test.xlsx_reader(Environment.getExternalStorageDirectory()
                            .toString() + File.separator + "Excel"
                            + File.separator + "Person" + File.separator + "123456.xlsx", 0, 1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15);*//*
                    arr = test.xlsx_reader(getResources().getAssets().open("123456.xlsx") , 0, 1, 2, 3, 4, 5,6,7,8,9,10,11,12,13,14,15);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i=0;i<arr.size();i++){
                    ArrayList<String> row=arr.get(i);
                    for(int j=0;j<row.size();j++){
//                        System.out.print(row.get(j)+" ");
                        Log.e("TAG", row.get(j) + " ");
                    }
//                    System.out.println("");
                    Log.e("TAG", " ");
                }
                break;*/
        }
    }
}
