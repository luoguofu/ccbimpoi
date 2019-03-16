package com.example.ccbim.ccbimpoi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;


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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_export_excel:
                SaveToExcelUtil util = new SaveToExcelUtil(this, getExcelDir() + File.separator + "demo.xls");
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
