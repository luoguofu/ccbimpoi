package com.weqia.wq;

import com.spinytech.macore.multiprocess.BaseApplicationLogic;
import com.weqia.utils.L;
import com.weqia.wq.component.db.DBOpEnum;
import com.weqia.wq.component.db.WeqiaDbUtil;
import com.weqia.wq.data.BucketFileData;
import com.weqia.wq.data.global.WeqiaApplication;

/**
 * Created by berwin on 2017/9/11.
 */

public class ComponentApplicationLogic extends BaseApplicationLogic {

    @Override
    public void onDbOp(String type, String param) {
        DBOpEnum opEnum = DBOpEnum.valueOfOp(type);
        if (opEnum == null) {
            L.e("数据库操作错误---");
            return;
        }
        if (opEnum == DBOpEnum.CRATETABLE) {
            createTable();
        } else if(opEnum == DBOpEnum.CLEARTABLE) {
            clearTable();
        } else if(opEnum == DBOpEnum.CLEARBYCO) {
            L.e("数据库操作参数为" + param);
            clearCoTable(param);
        }
    }

    protected void createTable(){
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            L.e("dbUtil为空，---");
            return;
        }
        dbUtil.CreatTable(BucketFileData.class);
    }

    protected void clearTable(){
        WeqiaDbUtil dbUtil = WeqiaApplication.getInstance().getDbUtil();
        if (dbUtil == null) {
            L.e("dbUtil为空，---");
            return;
        }
        dbUtil.clear(BucketFileData.class);
    }

    protected void clearCoTable(String coId){

    }
}
