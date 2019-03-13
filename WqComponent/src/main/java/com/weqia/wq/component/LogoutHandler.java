package com.weqia.wq.component;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;

import com.weqia.utils.L;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.wq.R;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.db.DBHelper;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.global.ModulesConstants;

public class LogoutHandler {

    private static Dialog logoutDialog;
    private static Dialog leaveDialog;
    private Activity ctx;
    private static LogoutHandler instance;
    private static boolean bCreatDig;//初始化退出对话框，（因为横屏的显示）width :1080 hight: 1920 dlgWidth: 1632 dlgHight :-2

    public LogoutHandler(Activity ctxa) {
        this.ctx = ctxa;
    }

    public Dialog getLogoutDialog(Activity ctx) {
        //每次都重新
        if (logoutDialog != null) {
            WindowManager m = (WindowManager) this.ctx.getSystemService(Context.WINDOW_SERVICE);
            Display d = m.getDefaultDisplay();
            int width = d.getWidth();
            int realWidth = (int) ((double) d.getWidth() * 0.8D);
            int hight = d.getHeight();
            int dlgWidth = logoutDialog.getWindow().getAttributes().width;
            int dlgHight = logoutDialog.getWindow().getAttributes().height;
            L.e("width :" + width + " hight: " + hight + " dlgWidth: " + dlgWidth + " dlgHight :" + dlgHight);
            if (dlgWidth >= realWidth) {
//                bCreatDig = true;
                //当对话框的宽度大于屏幕的宽度的时候，重新创建对话框
                logoutDialog.dismiss();
                logoutDialog = null;
            }
        }
        if (logoutDialog == null) {
            // 被顶掉退出登录
            SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
            builder.setTitle(ctx.getString(R.string.dialog_notice))
                    .setPositiveButton(ctx.getString(R.string.dialog_confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    logoutDialog.dismiss();
                                    WeqiaApplication.isLogout = false;
                                    logoutDo();
                                }
                            })
                    .showBar(false)
                    .setTitleAttr(true, null)
                    .setMessage(
                            ctx.getResources().getString(R.string.login_out_left)
                                    + TimeUtils.getTimeHS()
                                    + ctx.getResources().getString(R.string.login_out_right));
            logoutDialog = builder.create();
            logoutDialog.setCancelable(false);
        }
        return logoutDialog;
    }

    public Dialog getLeaveDialog(Activity ctx) {
        if (leaveDialog == null) {
            SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
            builder.setTitle(ctx.getString(R.string.dialog_notice))
                    .setPositiveButton(ctx.getString(R.string.dialog_confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    WeqiaApplication.isLev = false;
                                    dialog.dismiss();
                                    logoutReReg();
                                }
                            }).showBar(false).setTitleAttr(true, null)
                    .setMessage(ctx.getResources().getString(R.string.login_out_leave));
            leaveDialog = builder.create();
            leaveDialog.setCancelable(false);
        }
        return leaveDialog;
    }

    public void showLogoutDialog() {
        L.e("显示退出登陆框");
        if (getLogoutDialog(ctx).isShowing()) {
            if (L.D) L.d("已经在显示了，不需要再显示");
            return;
        } else {
            RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvremotemsg", "acmsgstop");
            try {
                L.e("显示对话框");
                if (ctx.isFinishing()) {
                    L.e("界面关闭");
                }
                ctx.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        getLogoutDialog(ctx).show();
                    }
                });
            } catch (BadTokenException e) {
            }
        }
    }

    public void showLeaveDialog() {
        if (getLeaveDialog(ctx).isShowing()) {
            if (L.D) L.d("已经在显示了，不需要再显示");
            return;
        } else {
            try {
                RouterUtil.routerActionSync(WeqiaApplication.ctx, "pvremotemsg", "acmsgstop");
                getLeaveDialog(ctx).show();
            } catch (BadTokenException e) {
            }
        }
    }

    private void logoutReReg() {
        WeqiaApplication.getInstance().resetAppData();
        DBHelper.clearCoTable(WeqiaApplication.getgMCoId());
        WeqiaApplication.isLev = false;
        RouterUtil.routerActionSync(ctx, "pvlogin", "aclogin");
    }

    private void logoutDo() {
        logoutDialog = null;
        leaveDialog = null;
        WeqiaApplication.getInstance().resetAppData();
        RouterUtil.routerActionSync(ctx, null, "pvlogin", "aclogin", ModulesConstants.ROUTER_PARAM, "false");
//        Intent intent = new Intent(ctx, LoginActivity.class);
//        intent.putExtra("canBack", false);
//        ctx.startActivity(intent);
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                if (BottomMenuActivity.getInstance() != null) {
//                    BottomMenuActivity.getInstance().finish();
//                }
//                ctx.finish();
//            }
//        }, 200);
    }
}
