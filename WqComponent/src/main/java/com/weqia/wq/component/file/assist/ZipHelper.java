package com.weqia.wq.component.file.assist;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.datastorage.file.FileUtil;
import com.weqia.utils.datastorage.file.NativeFileUtil;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by 20161005 on 2017/6/30.
 */

public class ZipHelper {

    public interface ZipInterface {
        void unzipSuccess(String path);
    }

    /**
     * 直接解压到指定文件夹，不进行文件预览逻辑操作
     * @param file Zipe文件
     * @param dir  解压输出目录
     */
    public static void unZip(File file, String dir) throws IOException {
        ZipFile zipFile = new ZipFile(file, "GBK");//设置压缩文件的编码方式为GBK
        Enumeration<ZipEntry> entris = zipFile.getEntries();
        ZipEntry zipEntry = null;
        File tmpFile = null;
        BufferedOutputStream bos = null;
        InputStream is = null;
        byte[] buf = new byte[1024];
        int len = 0;
        while (entris.hasMoreElements()) {
            zipEntry = entris.nextElement();
            // 不进行文件夹的处理,些为特殊处理
            tmpFile = new File(dir + zipEntry.getName());
            if (zipEntry.isDirectory()) {//当前文件为目录
                if (!tmpFile.exists()) {
                    FileUtil.getFolder(tmpFile.getAbsolutePath());
                }
            } else {
                if (!tmpFile.exists()) {
                    FileUtil.createOrReadFile(tmpFile.getAbsolutePath());
                }
                is = zipFile.getInputStream(zipEntry);
                bos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                while ((len = is.read(buf)) > 0) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
                bos.close();
            }
        }
    }

    /**
     * @param zipPath Zip文件路径
     * @return 返回Zip文件里面的ZipEntry实体集合，用于文件预览
     */
    public static List<ZipFileData> getZipFileEntryList(String zipPath) {
        List<ZipFileData> dataList = new ArrayList<>();
        try {
            if (StrUtil.isEmptyOrNull(zipPath)) {
                return null;
            }
            File file = new File(zipPath);
            if (!file.exists()) {
                L.e("没找到对应的Zip文件");
                return null;
            }
            ZipFile zipFile = new ZipFile(zipPath, "GBK");//设置压缩文件的编码方式为GBK
            Enumeration<ZipEntry> entris = zipFile.getEntries();
            while (entris.hasMoreElements()) {
                ZipEntry zipEntry = entris.nextElement();
                dataList.add(new ZipFileData(zipEntry.getName(), zipEntry.getSize(), zipEntry));
            }
            return dataList;
        } catch (IOException e) {
            e.printStackTrace();
            L.e("获取Zip文件失败");
        }
        return null;
    }

    /**
     * 点击预览文件列表的文件项进行解压
     * @param zipEntry   Zip包里的文件类型实体
     * @param zipPath    Zip文件路径
     * @param targetPath 解压到指定目录
     */
    public static void resloveZipEntry(ZipEntry zipEntry, String zipPath, String targetPath, ZipInterface zipInterface) throws IOException{
        ZipFile zfile=new ZipFile(zipPath,"GBK");
        Enumeration zList=zfile.getEntries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        boolean unZipSuccess = false;
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            if (ze.getName().equalsIgnoreCase(zipEntry.getName())) {
                if(ze.isDirectory()){
                    String dirstr = targetPath + ze.getName();
                    FileUtil.getFolder(dirstr);
                    continue;
                }
                File tmpFile = getRealFileName(targetPath, ze.getName());
                try {
                    if (!tmpFile.exists()) {
                        FileUtil.createOrReadFile(tmpFile.getAbsolutePath());
                    }
                    OutputStream os=new BufferedOutputStream(new FileOutputStream(tmpFile));
                    InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
                    int readLen=0;
                    while ((readLen=is.read(buf, 0, 1024))!=-1) {
                        os.write(buf, 0, readLen);
                    }
                    is.close();
                    os.close();
                    unZipSuccess = true;
                    if (zipInterface != null) {
                        zipInterface.unzipSuccess(tmpFile.getPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    NativeFileUtil.delFile(tmpFile);
                    throw e;
                }
                break;
            }
        }

        if (!unZipSuccess)
            throw new IOException("没有找到对应解压文件");
        zfile.close();
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        String lastDir=baseDir;
        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                lastDir +=(dirs[i]+"/");
                File dir =new File(lastDir);

                if(!dir.exists()){
                    dir.mkdirs();
                }
            }

            File ret = new File(lastDir,dirs[dirs.length-1]);
            return ret;
        }
        else {
            return new File(baseDir,absFileName);

        }
    }

}