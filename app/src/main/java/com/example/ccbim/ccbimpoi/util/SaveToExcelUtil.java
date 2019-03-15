package com.example.ccbim.ccbimpoi.util;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by lgf on 2019/2/28.
 */

public class SaveToExcelUtil {
    private HSSFWorkbook wwb;
    private String excelPath;
    private File excelFile;
    private Activity activity;

    public SaveToExcelUtil(Activity activity, String excelPath) {
        this.excelPath = excelPath;
        this.activity = activity;
        excelFile = new File(excelPath);
        createExcel(excelFile);

    }

    // 创建excel表.
    public void createExcel(File file) {
        HSSFSheet ws = null;
        try {
            if (!file.exists()) {
                wwb = new HSSFWorkbook();

                ws = wwb.createSheet("sheet1");

                // 在指定单元格插入数据
                Row row1 = ws.createRow(0);
                Row row2 = ws.createRow(1);
                Row row3 = ws.createRow(2);

                row1.createCell(0).setCellValue("姓名");
                row1.createCell(1).setCellValue("性别");
                row1.createCell(2).setCellValue("年龄");
                row2.createCell(0).setCellValue("小明");
                row2.createCell(1).setCellValue("男");
                row2.createCell(2).setCellValue("21");
                row3.createCell(0).setCellValue("小红");
                row3.createCell(1).setCellValue("女");
                row3.createCell(2).setCellValue("18");

                // 从内存中写入文件中
//                wwb.write();
//                wwb.close();


                try {
                    HSSFPatriarch patriarch = ws.createDrawingPatriarch();
                    //anchor主要用于设置图片的属性
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 5, 5, (short) 11, 12);
//                    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                    //插入图片
                    byte[] asd = readStream(Environment.getExternalStorageDirectory() + File.separator + "qwe.jpg");
                    patriarch.createPicture(anchor, wwb.addPicture(asd, HSSFWorkbook.PICTURE_TYPE_JPEG));


                    FileOutputStream fos = new FileOutputStream(file);
                    wwb.write(fos);
                    fos.close();
                    Toast.makeText(activity,"导出成功", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

/*    //将数据存入到Excel表中
    public void writeToExcel(Object... args) {

        try {
            Workbook oldWwb = Workbook.getWorkbook(excelFile);
            wwb = Workbook.createWorkbook(excelFile, oldWwb);
            WritableSheet ws = wwb.getSheet(0);
            // 当前行数
            int row = ws.getRows();
            Label lab1 = new Label(0, row, args[0] + "");
            Label lab2 = new Label(1, row, args[1] + "");
            Label lab3 = new Label(2, row, args[2] + "");
            Label lab4 = new Label(3, row, args[3] + "");
            ws.addCell(lab1);
            ws.addCell(lab2);
            ws.addCell(lab3);
            ws.addCell(lab4);

            // 从内存中写入文件中,只能刷一次.
            wwb.write();
            wwb.close();
            Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}