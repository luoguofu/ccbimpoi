package com.example.ccbim.ccbimpoi.util;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import com.example.ccbim.ccbimpoi.data.CellData;
import com.example.ccbim.ccbimpoi.data.CheckDetailData;
import com.example.ccbim.ccbimpoi.data.PicBean;
import com.example.ccbim.ccbimpoi.data.ProjectCheckData;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.annotation.sqlite.Id;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFCellUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.ccbim.ccbimpoi.MainNewActivity.getExcelDir;

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
            if (file.exists()) {
                deleteExcel(file.getAbsolutePath());
            }
            if (!file.exists()) {
                wwb = new HSSFWorkbook();

                ws = wwb.createSheet("sheet1");

                // 在指定单元格插入数据
                Row row1 = ws.createRow(0);
                Row row2 = ws.createRow(1);
                Row row3 = ws.createRow(2);

//                row1.createCell(0).setCellValue("姓名");
//                row1.createCell(1).setCellValue("性别");
//                row1.createCell(2).setCellValue("年龄");
                ws.createRow(0).createCell(0).setCellValue("姓名");
                ws.createRow(0).createCell(1).setCellValue("性别");
                ws.createRow(0).createCell(2).setCellValue("年龄");
                row2.createCell(0).setCellValue("小明");
                row2.createCell(0).setCellValue("男");
                row2.createCell(2).setCellValue("21");
                row3.createCell(0).setCellValue("小红");
                row3.createCell(1).setCellValue("女");
                row3.createCell(2).setCellValue("18");
/*                CellRangeAddress region = new CellRangeAddress(3, 3, 0, 1);
                ws.addMergedRegion(region);
                CellRangeAddress region2 = new CellRangeAddress(3, 3, 2, 3);
                ws.addMergedRegion(region2);*/
//                Row row4 = ws.createRow(3);
//                HSSFCellStyle style = wwb.createCellStyle();
//                HSSFCellStyle hssfCellStyle = new HSSFCellStyle();

//                style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//                style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//                style.setFillForegroundColor(HSSFColor.YELLOW.index);
//                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//                Cell cell4_1 = row4.createCell(0);
//                Cell cell4_2 = row4.createCell(2);
//                cell4_1.setCellValue("合并单元格");
//                cell4_2.setCellValue("合并单元格2");
                CellData cellData1 = new CellData("合并单元格", "3", "3", "0", "1");
                CellData cellData2 = new CellData("合并单元格2", "3", "3", "2", "3");
                ArrayList<CellData> list = new ArrayList<>();
                list.add(cellData1);
                list.add(cellData2);
//                list.add(new CellData("单位工程名称","4", "4", "0", "1"));
//                list.add(new CellData("xxx","4", "4", "2", "7"));
                int count = 0;
                Row row = ws.createRow(3);
                for (CellData cellData : list) {

                    if (count == 0) {
//                        Row row = ws.createRow(3);
                        Cell cell1 = row.createCell(0);
//                        setCell(cell1, wwb, cellData, ws);
                    } else if (count == 1){
//                        Row row = ws.createRow(3);
                        Cell cell2 = row.createCell(2);
//                        setCell(cell2, wwb, cellData, ws);
                    }
//                    Row row = ws.createRow(Integer.parseInt(cellData.getFirtRow()));
//                    Cell cell = row.createCell(Integer.parseInt(cellData.getFirtColumn()));
//                    setCell(cell, wwb, cellData, ws);

                    count++;
                }
//                setCell(cell4_1, wwb, cellData1, ws);
//                setCell(cell4_2, wwb, cellData2, ws);
//                cell4_1.setCellStyle(style);
                // 从内存中写入文件中
//                wwb.write();
//                wwb.close();


                try {
                    HSSFPatriarch patriarch = ws.createDrawingPatriarch();
                    //anchor主要用于设置图片的属性
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 5, 2, (short) 11, 12);
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

    public static void exportEccel(Activity activity, String excelPath, ProjectCheckData projectCheckData) {
        HSSFWorkbook hwb = null;
        HSSFSheet hs = null;
        hwb = new HSSFWorkbook();
        hs = hwb.createSheet("sheet1");
        hs.setDefaultRowHeightInPoints(15);
//        hs.setDefaultColumnWidth(256 * 5);
        if (projectCheckData != null) {
/*            if (StrUtil.notEmptyOrNull(projectCheckData.getRowHeight())) {
                ArrayList<String> rows = new ArrayList(Arrays.asList(projectCheckData.getRowHeight().split(",")));
                for (int i = 0; i < rows.size(); i++) {
                    String row = rows.get(i);
                    hs.createRow(i).setHeightInPoints(Integer.parseInt(row) * 30);
                }
            }*/
            if (StrUtil.notEmptyOrNull(projectCheckData.getColWidth())) {
                ArrayList<String> cols = new ArrayList(Arrays.asList(projectCheckData.getColWidth().split(",")));
                for (int i = 0; i < cols.size(); i++) {
                    String col = cols.get(i);
//                    hs.setColumnWidth(i, Integer.parseInt(col) * 256 * 2);
                    if (i <= 8) {
                        hs.setColumnWidth(i, 2579);
                    } else {
                        hs.setColumnWidth(i, 2400);
                    }

                }
            }

            int num = 1;
            //封装附图
            ArrayList<PicBean> picBeans = new ArrayList<>();
            for (CellData cellData : projectCheckData.getTabBody()) {
                for (CheckDetailData checkDetailData : cellData.getSubCellList()) {
                    if (StrUtil.notEmptyOrNull(checkDetailData.getPicPathsStr())) {
                        PicBean picBean = new PicBean();
                        picBean.setCheckPath(num + checkDetailData.getCheckPath());
                        String paths = checkDetailData.getPicPathsStr();
                        List<String> list = Arrays.asList(paths.split(","));
                        picBean.setCheckPic(list);
                        picBeans.add(picBean);
                        checkDetailData.getCheckPic().setCellName(num + "");
                        num++;
                    }
                }
            }
            ArrayList<CellData> filePic = new ArrayList<>();
            boolean isSingle = true;
            int singleFistRow = 3;
            int singleLastRow = 10;
            int notSingleFistRow = 3;
            int notSingleLastRow = 10;
            for (int i = 0; i < picBeans.size(); i++) {
                int count = 0;
                if (isSingle) {
                    for (String path : picBeans.get(i).getCheckPic()) {
                        if (count == 0) {
                            filePic.add(new CellData(picBeans.get(i).getCheckPath(), singleFistRow + "", singleFistRow + "", "9", "12"));
                            insertPic(hs, hwb, path, 9, singleFistRow, 12, singleLastRow,250);
                        } else {
                            insertPic(hs, hwb, path, 9, singleFistRow, 12, singleLastRow,0);
                        }
                        singleFistRow = singleLastRow + 1;
                        singleLastRow = singleLastRow + 5;
                        count++;
                    }

                } else {
                    for (String path : picBeans.get(i).getCheckPic()) {
                        if (count == 0) {
                            filePic.add(new CellData(picBeans.get(i).getCheckPath(), notSingleFistRow + "", notSingleFistRow + "", "13", "16"));
                            insertPic(hs, hwb, path, 13, notSingleFistRow, 16, notSingleLastRow, 250);
                        } else {
                            insertPic(hs, hwb, path, 13, notSingleFistRow, 16, notSingleLastRow,0);
                        }
                        notSingleFistRow = notSingleLastRow + 1;
                        notSingleLastRow = notSingleLastRow + 5;
                        count++;
                    }

                }
                isSingle = !isSingle;
            }

/*            boolean isSingle = true;
            int singleFistRow = 3;
            int singleLastRow = 8;
            int notSingleFistRow = 3;
            int notSingleLastRow = 8;
            for (int i = 0; i < picBeans.size(); i++) {
                if (isSingle) {
                    insertPic(hs, hwb, getExcelDir() + File.separator + projectCheckData.getCheckPartName() + "部位防水表单.xls", 9, singleFistRow, 12, singleLastRow);
                    singleFistRow = singleFistRow + 5;
                    singleLastRow = singleLastRow + 5;
                } else {
                    insertPic(hs, hwb, getExcelDir() + File.separator + projectCheckData.getCheckPartName() + "部位防水表单.xls", 13, notSingleFistRow, 16, notSingleLastRow);
                    notSingleFistRow = notSingleFistRow + 5;
                    notSingleFistRow = notSingleFistRow + 5;
                }
            }*/


//            projectCheckData.getTabHead().add(new CellData("结构闭水-排水管洞周围处理", "3", "3", "9", "12"));
            if (StrUtil.listNotNull(projectCheckData.getTabHead())) {
                ArrayList<String> rows = new ArrayList(Arrays.asList(projectCheckData.getHeadRows().split(",")));
                for (String rowStr : rows) {
                    Row row = hs.getRow(Integer.parseInt(rowStr));
                    if (row == null) {
                        row = hs.createRow(Integer.parseInt(rowStr));
                    }
                    row.setHeightInPoints(13);
                    for (CellData cellData : projectCheckData.getTabHead()) {
                        setCell(row, hwb, cellData, hs);
                    }
                    for (CellData picdata : filePic) {
                        setCell(row, hwb, picdata, hs);
                    }
                }
            }
            if (StrUtil.listNotNull(projectCheckData.getTabBody())) {
                for (CellData cellData : projectCheckData.getTabBody()) {
                    if (StrUtil.listNotNull(cellData.getSubCellList())) {
                        for (int i = Integer.parseInt(cellData.getFirtRow()); i <= Integer.parseInt(cellData.getLastRow()); i++) {
                            Row row = hs.getRow(i);
                            if (row == null) {
                                row = hs.createRow(i);
                            }
                            if (StrUtil.notEmptyOrNull(projectCheckData.getRowHeight())) {
                                ArrayList<String> rows = new ArrayList(Arrays.asList(projectCheckData.getRowHeight().split(",")));
                                for (int j = 0; j < rows.size(); j++) {
                                    String rowHeight = rows.get(i);
                                    if (row.getRowNum() == j) {
                                        row.setHeightInPoints(Integer.parseInt(rowHeight) * 13);
                                    }
                                }
                            }
                            setCell(row, hwb, cellData, hs);
                            for (CheckDetailData checkDetailData : cellData.getSubCellList()) {
                                setCell(row, hwb, checkDetailData.getCheckName(), hs);
                                setCell(row, hwb, checkDetailData.getCheckStandard(), hs);
                                setCell(row, hwb, checkDetailData.getCheckPass(), hs);
                                setCell(row, hwb, checkDetailData.getCheckInvolve(), hs);
                                setCell(row, hwb, checkDetailData.getCheckPic(), hs);
                            }
                            for (CellData picdata : filePic) {
                                setCell(row, hwb, picdata, hs);
                            }
                        }

                    }
                }
            }

            int footLastRow = 0;
            if (StrUtil.listNotNull(projectCheckData.getTabFoot())) {
                ArrayList<String> rows = new ArrayList(Arrays.asList(projectCheckData.getFootRows().split(",")));
                for (String rowStr : rows) {
//                    Row row = hs.createRow(Integer.parseInt(rowStr));
                    Row row = hs.getRow(Integer.parseInt(rowStr));
                    if (row == null) {
                        row = hs.createRow(Integer.parseInt(rowStr));
                    }
                    row.setHeightInPoints(15);
                    for (CellData cellData : projectCheckData.getTabFoot()) {
                        setCell(row, hwb, cellData, hs);
                    }
                    for (CellData picdata : filePic) {
                        setCell(row, hwb, picdata, hs);
                    }
                    footLastRow = Integer.parseInt(rowStr);
                }
            }
            if (footLastRow != 0) {
                for (CellData picdata : filePic) {
                    if (Integer.parseInt(picdata.getFirtRow()) > footLastRow) {
                        Row row = hs.getRow(Integer.parseInt(picdata.getFirtRow()));
                        if (row == null) {
                            row = hs.createRow(Integer.parseInt(picdata.getFirtRow()));
                        }
                        setCell(row, hwb, picdata, hs);
                    }
                }
            }


/*            ArrayList<CellData> filePic = new ArrayList<>();
            boolean isSingle = true;
            int singleFistRow = 3;
            int singleLastRow = 10;
            int notSingleFistRow = 3;
            int notSingleLastRow = 10;
            for (int i = 0; i < picBeans.size(); i++) {
                int count = 0;
                if (isSingle) {
                    for (String path : picBeans.get(i).getCheckPic()) {
                        if (count == 0) {
                            filePic.add(new CellData(picBeans.get(i).getCheckPath(), singleFistRow + "", singleFistRow + "", "9", "12"));
                            insertPic(hs, hwb, path, 9, singleFistRow, 12, singleLastRow,200);
                        } else {
                            insertPic(hs, hwb, path, 9, singleFistRow, 12, singleLastRow,0);
                        }
                        singleFistRow = singleLastRow + 1;
                        singleLastRow = singleLastRow + 5;
                        count++;
                    }

                } else {
                    for (String path : picBeans.get(i).getCheckPic()) {
                        if (count == 0) {
                            filePic.add(new CellData(picBeans.get(i).getCheckPath(), notSingleFistRow + "", notSingleFistRow + "", "13", "16"));
                            insertPic(hs, hwb, path, 13, notSingleFistRow, 16, notSingleLastRow, 200);
                        } else {
                            insertPic(hs, hwb, path, 13, notSingleFistRow, 16, notSingleLastRow,0);
                        }
                        notSingleFistRow = notSingleLastRow + 1;
                        notSingleLastRow = notSingleLastRow + 5;
                        count++;
                    }

                }
                isSingle = !isSingle;
            }*/
            File file = new File(excelPath);
            if (file.exists()) {
                deleteExcel(excelPath);
            }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                hwb.write(fos);
                fos.close();
                Toast.makeText(activity,"导出成功，请在 文件管理器/Excel/ExcelFile中查看", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            L.toastLong("数据为空");
        }
    }

    //当它需要添加附图文字的时候需要加上偏移量
    public static void insertPic(HSSFSheet hs, HSSFWorkbook hwb, String path, int fistCol, int fistRow, int LastCol, int LastRow, int y) {
        HSSFPatriarch patriarch = hs.createDrawingPatriarch();
        //anchor主要用于设置图片的属性
        HSSFClientAnchor anchor = new HSSFClientAnchor(0, y, 1023, 255, (short) fistCol, fistRow, (short) LastCol, LastRow);
//                    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
        //插入图片
        byte[] asd = new byte[0];
        try {
            asd = readStream(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        patriarch.createPicture(anchor, hwb.addPicture(asd, HSSFWorkbook.PICTURE_TYPE_JPEG));
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

    public static void setCell(Row row, HSSFWorkbook wb, CellData cellData,HSSFSheet sheet) {
        if (row.getRowNum() == Integer.parseInt(cellData.getFirtRow())) {
            Cell cell = row.getCell(Integer.parseInt(cellData.getFirtColumn()));
            if (cell == null) {
                cell = row.createCell(Integer.parseInt(cellData.getFirtColumn()));
            }
            cell.setCellValue(cellData.getCellName());
            HSSFCellStyle hssfCellStyle = wb.createCellStyle();
            if (cellData.getCellLayout() == 1 || (Integer) cellData.getCellLayout() == null) {
                hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                hssfCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);      //居中
            }


            if (StrUtil.notEmptyOrNull(cellData.getCellColor())) {
                setColor(hssfCellStyle, cellData.getCellColor(), wb);
            }

            hssfCellStyle.setBorderBottom(CellStyle.BORDER_THIN); //下边框
            hssfCellStyle.setBorderLeft(CellStyle.BORDER_THIN);//左边框
            hssfCellStyle.setBorderTop(CellStyle.BORDER_THIN);//上边框
            hssfCellStyle.setBorderRight(CellStyle.BORDER_THIN);//右边框

            if (StrUtil.notEmptyOrNull(cellData.getLastColumn()) && StrUtil.notEmptyOrNull(cellData.getLastRow())) {
                CellRangeAddress region = new CellRangeAddress(Integer.parseInt(cellData.getFirtRow()), Integer.parseInt(cellData.getLastRow()),
                        Integer.parseInt(cellData.getFirtColumn()), Integer.parseInt(cellData.getLastColumn()));
//                Region region1 = new Region(Integer.parseInt(cellData.getFirtRow()), (short) Integer.parseInt(cellData.getFirtColumn()), Integer.parseInt(cellData.getLastRow()), (short)Integer.parseInt(cellData.getLastColumn()));
                sheet.addMergedRegion(region);
                RegionUtil.setBorderBottom(1, region, sheet, wb); // 下边框

                RegionUtil.setBorderLeft(1, region, sheet, wb); // 左边框

                RegionUtil.setBorderRight(1, region, sheet, wb); // 有边框

                RegionUtil.setBorderTop(1, region, sheet, wb); // 上边框
                setRegionStyle(sheet, region, hssfCellStyle);

            } else {

            }

            hssfCellStyle.setWrapText(true);
            setFontAndBorder(hssfCellStyle, "宋体", (short) 9, wb);
            cell.setCellStyle(hssfCellStyle);
        }


//        cell.setCellValue(cellData.getCellName());
    }
    /**
     * 解决合并单元格边框消失问题，不仅需要调用此方法，单元格自身也需要正常设置上下左右的边框
     *
     * @param sheet
     * @param region
     * @param cs
     */
/*    public static void setRegionStyle(HSSFSheet sheet, Region region,
                                      HSSFCellStyle cs) {
        for (int i = region.getRowFrom(); i <= region.getRowTo(); i++) {
            HSSFRow row = HSSFCellUtil.getRow(i, sheet);
            for (int j = region.getColumnFrom(); j <= region.getColumnTo(); j++) {
                HSSFCell cell = HSSFCellUtil.getCell(row, (short) j);
                cell.setCellStyle(cs);

            }
        }
    }*/
    public static void setRegionStyle(HSSFSheet sheet, CellRangeAddress region,
                                      HSSFCellStyle cs) {
//        cs.setBorderBottom(CellStyle.BORDER_THIN); //下边框
//        cs.setBorderLeft(CellStyle.BORDER_THIN);//左边框
//        cs.setBorderTop(CellStyle.BORDER_THIN);//上边框
//        cs.setBorderRight(CellStyle.BORDER_THIN);//右边框
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {

            HSSFRow row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    cell = row.createCell(j);
                    cell.setCellValue("");
                }
                cell.setCellStyle(cs);
//                cell.setCellValue(new HSSFRichTextString(""));

            }
        }
    }





    /**
     * 将16进制的颜色代码写入样式中来设置颜色
     *
     * @param style 保证style统一
     * @param color 颜色：66FFDD
     *              //     * @param index 索引 8-64 使用时不可重复
     * @return
     */
    public static HSSFCellStyle setColor(HSSFCellStyle style, String color, HSSFWorkbook wb) {

        if (color != "" && color != null) {
            //转为RGB码
            int r = Integer.parseInt((color.substring(0, 2)), 16);   //转为16进制
            int g = Integer.parseInt((color.substring(2, 4)), 16);
            int b = Integer.parseInt((color.substring(4, 6)), 16);
            //自定义cell颜色
            HSSFPalette palette = wb.getCustomPalette();
//            palette.setColorAtIndex((short) index, (byte) r, (byte) g, (byte) b);
            HSSFColor hssfColor = palette.findSimilarColor(r, g, b);
            style.setFillForegroundColor(hssfColor.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        }
        return style;
    }


    /**
     * 设置字体并加外边框
     *
     * @param style 样式
     * @param style 字体名
     * @param style 大小
     * @return
     */
    public static HSSFCellStyle setFontAndBorder(HSSFCellStyle style, String fontName, short size, HSSFWorkbook wb) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontName);
//        font.setBold(true);
        style.setFont(font);
//        style.setBorderBottom(CellStyle.BORDER_THIN); //下边框
//        style.setBorderLeft(CellStyle.BORDER_THIN);//左边框
//        style.setBorderTop(CellStyle.BORDER_THIN);//上边框
//        style.setBorderRight(CellStyle.BORDER_THIN);//右边框
        return style;
    }

    /**
     * 删除文件
     * @param  path
     * @return
     */
    public static boolean deleteExcel(String path){
        boolean flag = false;
        File file = new File(path);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }


}