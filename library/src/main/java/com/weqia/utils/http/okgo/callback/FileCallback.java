package com.weqia.utils.http.okgo.callback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

import com.weqia.utils.http.okgo.convert.FileConvert;

public abstract class FileCallback extends RequestCallBack<File> {

    private FileConvert convert;    //文件转换类

    public FileCallback() {
        this(null);
    }

    public FileCallback(String destFileName) {
        this(null, destFileName);
    }

    public FileCallback(String destFileDir, String destFileName) {
        convert = new FileConvert(destFileDir, destFileName);
        convert.setCallback(this);
    }

    @Override
    public File convertSuccess(Response response) throws Exception {
        File file = convert.convertSuccess(response);
        response.close();
        return file;
    }


    @Override
    public void onSuccessData(File file, Call call, Response response) {
//        L.e("下载成功啦, 地址" + file.getAbsolutePath());
//        onSuccess(file);
    }

    public abstract void onSuccess(File file);
}