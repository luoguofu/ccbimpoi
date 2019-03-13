package com.weqia.wq.component.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.data.DialogData;
import com.weqia.utils.dialog.SharedCommonDialog;
import com.weqia.utils.dialog.SharedFullScreenDialog;
import com.weqia.utils.dialog.SharedSmallDialog;
import com.weqia.utils.exception.CheckedExceptionHandler;
import com.weqia.wq.R;
import com.weqia.wq.component.utils.autoupdate.SharedUpdateDialog;
import com.weqia.wq.component.utils.autoupdate.SharedUpdateProgressDialog;
import com.weqia.wq.component.view.FormRowEditView;
import com.weqia.wq.data.MenuEnumData;
import com.weqia.wq.data.base.CheckData;

import java.util.ArrayList;
import java.util.List;

import static com.weqia.wq.data.global.GlobalConstants.FORM_DIVIDE;

/**
 * 自定义对话框显示
 * @author Dminter
 */
public class DialogUtil {


    public static final int DLG_AVATAR = 1001;
    public static final int DLG_GENDER = 1002;
    public static final int DLG_WEQIANAME = 1003;
    public static final int DLG_VISABLE = 1004;
    public static final int DLG_ONLOADING = 1005;
    public static final int DLG_LOGOUT = 1006;
    public static final int DLG_EIXT_CO = 1007;
    public static final int DIALOG_BUTTON_ID_BEGIN = 100901;
    public static final int DIALOG_BUTTON_ID_QUIT = 10001;
    public static final int DIALOG_BUTTON_ID_PHOTO = 10002;
    public static final int DIALOG_BUTTON_ID_PICKER = 10003;
    public static final int DIALOG_BUTTON_ID_CALL = 10004;
    public static final int DIALOG_BUTTON_ID_SMS = 10005;
    public static final int DIALOG_BUTTON_ID_SHOW_VIEW = 10006;
    public static final int DIALOG_BUTTON_ID_SHOW_POSITION = 10007;
    public static final int DIALOG_BUTTON_ID_FILE_UP_CHOOSE = 10008;
    public static final int DIALOG_BUTTON_ID_FILE_UP_PICTURE = 10009;
    public static final int DLG_CONTACT = 10010;
    public static final int DLG_PISOTONTIME = 10011;
    public static final int ADD_DLG_DISCUSS_TAG = 10012;
    public static final int DEL_DLG_DISCUSS_TAG = 10013;
    public static final int DLG_DISCUSS_TAG = 10013;
    public static final int DIALOG_REPORT_REVIEW = 10014;

    /**
     * 选取照片
     * @return
     * @Description
     */
    public static Dialog initAvatarDialog(Context ctx, OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData dataPhoto =
                new DialogData(DIALOG_BUTTON_ID_PHOTO, ctx.getString(R.string.dialog_button_photo),
                        onClickListener, null);
        DialogData dataPicker =
                new DialogData(DIALOG_BUTTON_ID_PICKER,
                        ctx.getString(R.string.dialog_button_picker), onClickListener, null);
        List<DialogData> datas = new ArrayList<DialogData>();
        dataPhoto.setTitleColor(ctx.getResources().getColor(R.color.black));
        dataPicker.setTitleColor(ctx.getResources().getColor(R.color.black));
        datas.add(dataPhoto);
        datas.add(dataPicker);
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    // 打电话,发短信对话框
    public static Dialog initPhoneDialog(Context ctx, OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_CALL, ctx.getString(R.string.dialog_button_phone),
                        onClickListener, null);
        DialogData data2 =
                new DialogData(DIALOG_BUTTON_ID_SMS, ctx.getString(R.string.dialog_button_sms),
                        onClickListener, null);
        List<DialogData> datas = new ArrayList<DialogData>();
        data1.setTitleColor(ctx.getResources().getColor(R.color.black));
        data2.setTitleColor(ctx.getResources().getColor(R.color.black));
        datas.add(data1);
        datas.add(data2);
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    /**
     * 提示对话框自定义内容
     */
    public static Dialog commonShowDialog(Context context, String content) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(context);
        return builder
                .setTitle(context.getString(R.string.dialog_notice))
                .setTitleAttr(true, null)
                .showBar(false)
                .setPositiveButton(context.getString(R.string.dialog_confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setMessage(content).create();
    }


    public static Dialog commonShowWithLinksDialog(Context context, String content) {
        // LayoutInflater mInflater = LayoutInflater.from(context);
        // View view = mInflater.inflate(R.layout.view_dialog_links, null);
        // TextView tvShow = (TextView) view.findViewById(R.id.tvShow);
        // tvShow.setText(content);
        // tvShow.setMovementMethod(ScrollingMovementMethod.getInstance());
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(context);
        return builder
                .setTitle(context.getString(R.string.dialog_notice))
                .setTitleAttr(true, null)
                .setMessage(content)
                .showBar(false)
                .setPositiveButton(context.getString(R.string.dialog_confirm),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();

    }

    /**
     * 提示对话框自定义内容
     */
    public static Dialog commonShowDialog(Context context, String content, DialogInterface.OnClickListener listener) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(context);
        return builder
                .setTitle(context.getString(R.string.dialog_notice))
                .setTitleAttr(true, null)
                .showBar(false)
                .setPositiveButton("注册", listener)
                .setNegativeButton(context.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage(content).create();
    }

    /**
     * 等待对话框,带提示内容
     */
    public static Dialog commonLoadingDialog(Context context, String title) {
        View view = null;
        try {
            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.view_dialog_loading, null);
            TextView waitTV = (TextView) view.findViewById(R.id.loading_view_show);
            if (!StrUtil.isEmptyOrNull(title)) {
                waitTV.setText(title);
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

        if (view != null) {
            SharedSmallDialog.Builder builder = new SharedSmallDialog.Builder(context);
            return builder.setContentView(view).create();
            // .setBg(R.drawable.dialog_load_bg)
        } else {
            SharedSmallDialog.Builder builder = new SharedSmallDialog.Builder(context);
            return builder.create();
            // .setBg(R.drawable.dialog_load_bg)
        }
    }

    public static Dialog commonImgMsgDialog(Context context, int resId, String title) {
        View view = null;
        try {
            LayoutInflater mInflater = LayoutInflater.from(context);
            view = mInflater.inflate(R.layout.view_dialog_ivmsg, null);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            if (StrUtil.notEmptyOrNull(title)) {
                textView.setText(title);
            }
            if (resId != 0) {
                imageView.setImageResource(resId);
            }
        } catch (Exception e) {
            CheckedExceptionHandler.handleException(e);
        }

        if (view != null) {
            SharedSmallDialog.Builder builder = new SharedSmallDialog.Builder(context);
            return builder.setContentView(view).create();
            // .setBg(R.drawable.dialog_load_bg)
        } else {
            SharedSmallDialog.Builder builder = new SharedSmallDialog.Builder(context);
            return builder.create();
            // .setBg(R.drawable.dialog_load_bg)
        }
    }

    /**
     * 选择性别
     */
    public static Dialog initGenderDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          View view) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.setting_userinfo_gender));
        builder.showBar(true);
        builder.setTitleAttr(true, null);
        // builder.setBg(R.color.dlg_bg_white);
        // builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setContentView(view);
        return builder.create();
    }

    /**
     * 微会议标签
     */
    public static Dialog initDiscussTagDialog(Context ctx, DialogInterface.OnClickListener listener, View view, String title) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(title);
        builder.showBar(true);
        builder.setTitleAttr(true, ctx.getResources().getColor(R.color.blue));
        builder.setBg(R.color.dlg_bg_white);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), null);
        builder.setContentView(view);
        return builder.create();
    }
//

    /**
     * 位置上报时间
     */
    public static Dialog initPositionDialog(Context ctx, DialogInterface.OnClickListener listener,
                                            View view) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.setting_punchlocation_time));
        builder.showBar(true);
        builder.setTitleAttr(true, null);
        builder.setContentView(view);
        return builder.create();
    }

    public static Dialog initContactDialog(Context ctx, DialogInterface.OnClickListener listener, View view) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle("通讯录显示");
        builder.showBar(true);
        builder.setTitleAttr(true, null);
        builder.setContentView(view);
        return builder.create();
    }

    /**
     * 可见性对话框
     */
    public static Dialog initVisableDialog(Context ctx, DialogInterface.OnClickListener listener,
                                           View view) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle("可见性");
        builder.showBar(true);
        builder.setTitleAttr(true, null);
        builder.setContentView(view);

        return builder.create();
    }

    /**
     * 报告复核对话框
     */
    public static Dialog initReviewDialog(Context ctx, View view) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.review_report_result));
        builder.showBar(true);
        builder.setTitleAttr(true, null);
        builder.setContentView(view);
        return builder.create();
    }

    /**
     * 工作操作
     */
    public static Dialog initWorkOpDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          View view, String title) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        builder.setContentView(view);
        return builder.create();
    }

    /**
     * 列表项长按弹出菜单
     */
    public static Dialog initLongClickDialog(Context ctx, String title, String[] items,
                                             OnClickListener listener) {
        LinearLayout llChange = null;
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);
        }
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                View cellView = LayoutInflater.from(ctx).inflate(R.layout.view_reused_dialigtext, null);
                TextView textView = (TextView) cellView.findViewById(R.id.tv_dlg_title); // new
                textView.setText(items[i]);
                textView.setTag(i);
                textView.setTag(-1, items[i]);
                textView.setOnClickListener(listener);
                if (i == items.length - 1) {
                    View dvLine = cellView.findViewById(R.id.iv_dlg_dv);
                    ViewUtils.hideView(dvLine);
                }
                llChange.addView(cellView);
            }
        }
        builder.setContentView(view);
        return builder.create();
    }


    //选择,带有选中标志
    public static Dialog initLongCheckedClickDialog(Context ctx, String title, String[] items, String checkedStr,
                                                    OnClickListener listener) {
        if (StrUtil.isEmptyOrNull(checkedStr)) {
            return null;
        }
        LinearLayout llChange = null;
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);

        }
        for (int i = 0; i < items.length; i++) {
            View cellView = LayoutInflater.from(ctx).inflate(R.layout.view_menu_checked, null);
            TextView textView = (TextView) cellView.findViewById(R.id.tv_dlg_title);
            CheckBox checkBox = (CheckBox) cellView.findViewById(R.id.cbChecked);
            RelativeLayout rlBg = (RelativeLayout) cellView.findViewById(R.id.rlBg);
            textView.setText(items[i]);
            textView.setTag(i);
            rlBg.setOnClickListener(listener);
            if (i == items.length - 1) {
                View dvLine = cellView.findViewById(R.id.iv_dlg_dv);
                ViewUtils.hideView(dvLine);
            }

            if (checkedStr.equals(items[i])) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            llChange.addView(cellView);
        }
        builder.setContentView(view);
        return builder.create();
    }

    //选择,带有选中标志
    public static Dialog initLongRadioClickDialog(Context ctx, String title, String[] items, String checkedStr,
                                                  OnClickListener listener) {
/*        if (StrUtil.isEmptyOrNull(checkedStr)) {
            return null;
        }*/
        LinearLayout llChange = null;
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);

        }
        for (int i = 0; i < items.length; i++) {
            View cellView = LayoutInflater.from(ctx).inflate(R.layout.view_menu_checked, null);
            TextView textView = (TextView) cellView.findViewById(R.id.tv_dlg_title);
            CheckBox checkBox = (CheckBox) cellView.findViewById(R.id.cbChecked);
            RelativeLayout rlBg = (RelativeLayout) cellView.findViewById(R.id.rlBg);
            textView.setText(items[i]);
            textView.setTag(i);
            rlBg.setOnClickListener(listener);
            if (i == items.length - 1) {
                View dvLine = cellView.findViewById(R.id.iv_dlg_dv);
                ViewUtils.hideView(dvLine);
            }

/*            if (checkedId.equals(chechedIds[i])) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }*/
            if (StrUtil.notEmptyOrNull(checkedStr)) {
                if (checkedStr.equals(items[i])) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }
            } else {
                checkBox.setChecked(false);
            }

            llChange.addView(cellView);
        }
        builder.setContentView(view);
        return builder.create();
    }

    //多选,带有选中标志
    public static Dialog initLongCheckClickDialog(Context ctx, String title, String[] items, String checkedStr,
                                                  OnClickListener listener, DialogInterface.OnClickListener dialoglistener) {
        String[] checkArr = new String[0];
        if (StrUtil.isEmptyOrNull(checkedStr)) {
            checkArr = checkedStr.split(",");
        }
        LinearLayout llChange = null;
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);

        }
        final List<CheckData> list = new ArrayList<>();
        for (String str: items) {
            CheckData checkData = new CheckData();
            checkData.setTagValue(str);
            checkData.setCheck(false);
            list.add(checkData);
        }
        for (int i = 0; i < items.length; i++) {
            View cellView = LayoutInflater.from(ctx).inflate(R.layout.view_menu_checked, null);
            TextView textView = (TextView) cellView.findViewById(R.id.tv_dlg_title);
            final CheckBox checkBox = (CheckBox) cellView.findViewById(R.id.cbChecked);
            RelativeLayout rlBg = (RelativeLayout) cellView.findViewById(R.id.rlBg);
            textView.setText(items[i]);
            textView.setTag(i);
//            rlBg.setOnClickListener(listener);
            if (i == items.length - 1) {
                View dvLine = cellView.findViewById(R.id.iv_dlg_dv);
                ViewUtils.hideView(dvLine);
            }

/*            if (checkedId.equals(chechedIds[i])) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }*/
            if (StrUtil.notEmptyOrNull(checkedStr)) {
                for (int j = 0; j < checkArr.length; j ++) {
                    if (checkArr[j].equals(items[i])) {
                        checkBox.setChecked(true);
                        list.get(i).setCheck(true);
                    } else {
                        checkBox.setChecked(false);
                        list.get(i).setCheck(false);
                    }
                }
            }
            llChange.addView(cellView);
            rlBg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
//                        list.get(i).setCheck(false);
                    } else {
                        checkBox.setChecked(true);
//                        list.get(i).setCheck(true);
                    }
                }
            });

        }
        builder.setContentView(view);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                L.e(list.toString());
            }
        });
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), dialoglistener);
        return builder.create();
    }

    public static ArrayList<CheckData> checkList = new ArrayList<>();

    public static void showMultiChoiceDialog(final Context ctx, String title, final String[] items, String checkedStr, final TextView mFromSelectTv, final FormRowEditView rootView) {
        String[] checkArr = new String[0];
        if (StrUtil.notEmptyOrNull(checkedStr)) {
            checkArr = checkedStr.split(FORM_DIVIDE);
        }
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[] = new boolean[items.length];
        checkList.clear();
        for (int k = 0; k < items.length; k++) {
            CheckData checkData = new CheckData();
            checkData.setTagValue(items[k]);
            checkList.add(checkData);
            if (checkedStr != null) {
                for (int j = 0; j < checkArr.length; j++) {
                    if (items[k].equals(checkArr[j])) {
                        initChoiceSets[k] = true;
                        checkList.get(k).setCheck(true);
                        break;
                    } else {
                        initChoiceSets[k] = false;
                        checkList.get(k).setCheck(false);
                    }
                }
            } else {
                initChoiceSets[k] = false;
                checkList.get(k).setCheck(false);
            }

        }

        AlertDialog.Builder multiChoiceDialog =
                new AlertDialog.Builder(ctx);
        multiChoiceDialog.setTitle(title);
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
//                            yourChoices.add(which);
                            checkList.get(which).setCheck(true);
                        } else {
//                            yourChoices.remove(which);
                            checkList.get(which).setCheck(false);
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                int size = yourChoices.size();
//                String str = "";
//                for (int i = 0; i < size; i++) {
//                    str += items[yourChoices.get(i)] + " ";
//                }
                String str = "";
                for (CheckData checkData : checkList) {
                    if (checkData.isCheck()) {
                        if (StrUtil.isEmptyOrNull(str)) {
                            str += checkData.getTagValue();
                        } else {
                            str += FORM_DIVIDE + checkData.getTagValue();
                        }
                    }
                }
//                Toast.makeText(ctx,
//                        "你选中了" + str,
//                        Toast.LENGTH_SHORT).show();
                mFromSelectTv.setText(str.replace(FORM_DIVIDE, ","));
                rootView.setFieldVal(str);
            }

        });
        multiChoiceDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        multiChoiceDialog.show();
    }



    public static Dialog initLongClickDialog(Context ctx, String title, int[] itemKeys,
                                             OnClickListener listener) {
        LinearLayout llChange = null;
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        if (StrUtil.notEmptyOrNull(title)) {
            builder.setTitle(title);
            builder.showBar(true);
            builder.setTitleAttr(true, null);
        }
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.view_work_op, null);
        if (view != null) {
            llChange = (LinearLayout) view.findViewById(R.id.ll_work_change);
        }
        for (int i = 0; i < itemKeys.length; i++) {
            if (itemKeys[i] != MenuEnumData.TalkMenuEnum.NONE.value()) {
                View cellView = LayoutInflater.from(ctx).inflate(R.layout.view_reused_dialigtext, null);
                TextView textView = (TextView) cellView.findViewById(R.id.tv_dlg_title); // new
                textView.setText(MenuEnumData.TalkMenuEnum.valueOf(itemKeys[i]).strName());
                textView.setTag(itemKeys[i]);
                textView.setOnClickListener(listener);
                if (i == itemKeys.length - 1) {
                    View dvLine = cellView.findViewById(R.id.iv_dlg_dv);
                    ViewUtils.hideView(dvLine);
                }
                llChange.addView(cellView);
            }

        }
        builder.setContentView(view);
        return builder.create();
    }

    /**
     * 通用对话框
     */
    public static Dialog initOpenFileDialog(Context ctx, DialogInterface.OnClickListener listener,
                                            String tilte, View openFileView) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(tilte);
        builder.showBar(false);
        builder.setTitleAttr(false, null);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), listener);
        builder.setContentView(openFileView);
        return builder.create();
    }

    public static SharedUpdateProgressDialog initUpdateProgressDialog(Context ctx, boolean isCoercivenessUpgrade) {//isCoercivenessUpgrade
        SharedUpdateProgressDialog.Builder builder = new SharedUpdateProgressDialog.Builder(ctx);
        builder.setTitle("应用更新中...");
        SharedUpdateProgressDialog dialog = builder.build();
        dialog.setCancelable(!isCoercivenessUpgrade);
        return dialog;
    }


    public static Dialog initUpdateDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          String content, String versionName, String fileSize, boolean isCoercivenessUpgrade) {//isCoercivenessUpgrade
        SharedUpdateDialog.Builder builder = new SharedUpdateDialog.Builder(ctx);

        builder.setTitle("发现新版本")
                .showBar(false)
                .setTitleAttr(true, null)
                .setVersionNameText("最新版本号： " + versionName)
                .setFileSizeText("新版本大小： " + fileSize + "MB");
        if (!isCoercivenessUpgrade) {
            builder.setNegativeButton("以后再说", listener)
                    .setPositiveButton("立即更新", listener)
                    .setNaturalButton(null, null);
        } else {
            builder.setNegativeButton(null, null)
                    .setPositiveButton(null, null)
                    .setNaturalButton("立即更新", listener);
        }
        builder.setMessage(content);
        SharedUpdateDialog dialog = builder.build();
        dialog.setCancelable(!isCoercivenessUpgrade);
        return dialog;
    }

    /**
     * 通用对话框
     */
    public static Dialog initCommonDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          String str) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.dialog_notice));
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), listener);
        builder.setMessage(str);

        return builder.create();
    }

    public static Dialog initCommonDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          String str, String title, String positiveStr, String negativeStr) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);

        builder.setTitle(title);
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setPositiveButton(positiveStr, listener);
        builder.setNegativeButton(negativeStr, listener);
        builder.setMessage(str);

        return builder.create();
    }

    /**
     * 通用对话框
     */
    public static Dialog initCommonDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          String str, String tilte) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);

        builder.setTitle(tilte);
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setMessageLeft(true);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), listener);
        builder.setMessage(str);

        return builder.create();
    }
    /**
     * 通用对话框
     */
    public static Dialog initCommonDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          View view, String tilte) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(tilte);
        builder.setContentView(view);
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setPositiveButton(ctx.getString(R.string.dialog_confirm), listener);
        builder.setNegativeButton(ctx.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    public static Dialog initCommonDialog(Context ctx, DialogInterface.OnClickListener listener,
                                          String str, String btnSureStr, String btnCancelStr) {
        SharedCommonDialog.Builder builder = new SharedCommonDialog.Builder(ctx);
        builder.setTitle(ctx.getString(R.string.dialog_notice));
        builder.showBar(false);
        builder.setTitleAttr(true, null);
        builder.setPositiveButton(btnSureStr, listener);
        builder.setNegativeButton(btnCancelStr, listener);
        builder.setMessage(str);
        return builder.create();
    }

    /**
     * 找回密码
     * @param ctx
     * @param onClickListener
     * @return
     * @Description
     * @author Dminter
     */
    public static Dialog initFindPWDDialog(Context ctx,
                                           android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_BEGIN - 2, "通过手机找回密码", onClickListener, null);
        DialogData data2 =
                new DialogData(DIALOG_BUTTON_ID_BEGIN - 3, "通过邮箱找回密码", onClickListener, null);
        List<DialogData> datas = new ArrayList<DialogData>();
        data1.setTitleColor(ctx.getResources().getColor(R.color.black));
        data2.setTitleColor(ctx.getResources().getColor(R.color.black));
        datas.add(data1);
        datas.add(data2);
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    /**
     * 动态分享，回复，删除
     * @param ctx
     * @param onClickListener
     * @return
     * @Description
     * @author Dminter
     */
    public static Dialog initDynamicDialog(Context ctx,
                                           android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_BEGIN - 8, "删除", onClickListener,
                        R.drawable.util_com_alert_danger);
        DialogData data2 = new DialogData(DIALOG_BUTTON_ID_BEGIN - 9, "回复", onClickListener, null);
//        DialogData data3 = new DialogData(DIALOG_BUTTON_ID_BEGIN - 10, "分享", onClickListener, null);
        DialogData data4 = new DialogData(DIALOG_BUTTON_ID_BEGIN - 11, "复制", onClickListener, null);
        List<DialogData> datas = new ArrayList<DialogData>();

        // data1.setTitleColor(ctx.getResources().getColor(R.color.w));
        data2.setTitleColor(ctx.getResources().getColor(R.color.black));
//        data3.setTitleColor(ctx.getResources().getColor(R.color.black));
        data4.setTitleColor(ctx.getResources().getColor(R.color.black));
        datas.add(data1);
        datas.add(data2);
//        datas.add(data3);
        datas.add(data4);
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }


    /**
     * 清除聊天记录
     * @param ctx
     * @param onClickListener
     * @return
     */
    public static Dialog initClearRecordDialog(Context ctx,
                                               android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_BEGIN - 5, "清空本地聊天记录", onClickListener,
                        R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("将清空所有人的本地聊天记录");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    /**
     * 清除单人聊天记录
     * @param ctx
     * @param onClickListener
     * @return
     */
    public static Dialog initClearOneRecordDialog(Context ctx, android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 = new DialogData(DIALOG_BUTTON_ID_BEGIN - 6, "清空聊天记录", onClickListener, R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("清空聊天记录");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    public static Dialog commonClearDialog(Context ctx, android.view.View.OnClickListener onClickListener, String title, int index) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 = new DialogData(index, title, onClickListener, R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("清空聊天记录");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    public static Dialog initClearMsgCenterDialog(Context ctx,
                                                  android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_BEGIN - 7, "清空所有消息", onClickListener,
                        R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    public static Dialog initClearDbDialog(Context ctx, android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 = new DialogData(DIALOG_BUTTON_ID_BEGIN - 6, "清空缓存", onClickListener, R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("将清空所有缓存数据");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    // 退出企业
    public static Dialog exitCoDialog(Context ctx, android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DLG_EIXT_CO, "退出当前企业", onClickListener, R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle("退出后你将看不到该企业任何信息，确定退出当前企业吗？");
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    /**
     * 退出登录
     * @param ctx
     * @param onClickListener
     * @return
     */
    public static Dialog iniLogoutDialog(Context ctx, android.view.View.OnClickListener onClickListener) {
        SharedFullScreenDialog dialog = new SharedFullScreenDialog(ctx);
        DialogData data1 =
                new DialogData(DIALOG_BUTTON_ID_QUIT, ctx.getString(R.string.button_logout), onClickListener, R.drawable.util_com_alert_danger);
        List<DialogData> datas = new ArrayList<DialogData>();
        datas.add(data1);
        dialog.setDialogTitle(ctx.getString(R.string.dialog_quit_tip));
        dialog.setCancelable(true);
        dialog.setDialogButton(datas);
        return dialog;
    }

    interface MultiChoice {
        void getChekedStr(String chekedStr);
    }


}
