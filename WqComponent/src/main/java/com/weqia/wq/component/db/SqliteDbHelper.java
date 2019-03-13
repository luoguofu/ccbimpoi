package com.weqia.wq.component.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weqia.data.StatedPerference;
import com.weqia.utils.L;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.WPfMid;
import com.weqia.wq.data.global.Hks;

class SqliteDbHelper {

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.e("清空所有的数据信息...");
        // 初始化联系人下载
        StatedPerference.getInstance().remove(Hks.pr_co_down_contacts);//
        WPf.setInstance(null);
        WPfMid.setInstance(null);
        // setCoDownloadContact("");
        Cursor cursor =
                db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' ", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (!"sqlite_sequence".equals(cursor.getString(0))) {
                    db.execSQL("DROP TABLE " + cursor.getString(0));
                }
            }
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
}
