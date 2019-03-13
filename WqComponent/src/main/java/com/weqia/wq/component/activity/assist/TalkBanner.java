package com.weqia.wq.component.activity.assist;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedTitleActivity;
import com.weqia.wq.component.imageselect.SelectMediaUtils;
import com.weqia.wq.component.talk.SoftKeyboardUtil;
import com.weqia.wq.component.view.face.ExpressionGridView;
import com.weqia.wq.data.WPf;
import com.weqia.wq.data.global.Hks;

import java.util.ArrayList;


public class TalkBanner {

    private BarAdapter barAdapter;
    private GridView gvTalk;


    private boolean bVoice = false;
    private ImageView ibVoice;// 语音
    private Button btnRecorder;// 录音

    private boolean bFace = false;
    private ImageView ibAdd;
    private ImageView ibFace;
    private boolean bAdd = false;
    private TextView btnSend;// 发送

    private EditText etInput;// 发送消息输入框
    private InputMethodManager imm;

    private LinearLayout llPic;
    private LinearLayout llHide;
    private ExpressionGridView expressionGridView;
    private LinearLayout llChat;

    // private Dialog fileDlg = null;

    private boolean bClickFirst = true;

    // 传入参数
    private SharedTitleActivity ctx;
    private TalkBarInterface talkBarInterface;
    private String businessId;

    private RecordVoiceView recordVoiceView;
    private boolean bHB;

    public enum AddMediaType {

        PICTURE(1, "图片", R.drawable.sel_img), //
        VIDEO(2, "视频", R.drawable.sel_video), //
        LOC(3, "位置", R.drawable.sel_pos), //
        FILE(4, "文件", R.drawable.sel_file), //
        NOTICE(5, "通告", R.drawable.icon_duyixia), //
        LINK(6, "链接", R.drawable.icon_lianjie), //
//        RED_PACKET(7, "红包", R.drawable.sel_hongbao), //
        BUSINESS_CARD(8, "名片", R.drawable.icon_mingpian), //
        SHIKOU(9, "视口", R.drawable.icon_qunliao_shikou), //
        ;

        private String strName;
        private int value;
        private int drawId;

        private AddMediaType(int value, String strName, int drawId) {
            this.value = value;
            this.strName = strName;
            this.drawId = drawId;
        }

        public String strName() {
            return strName;
        }

        public int value() {
            return value;
        }

        public int drawId() {
            return drawId;
        }
    }

    public TalkBanner(SharedTitleActivity ctx, String businessId, TalkBarInterface talkBarInterface) {
        this.ctx = ctx;
        this.businessId = businessId;
        this.talkBarInterface = talkBarInterface;
    }

    public void initView() {
        imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        llChat = (LinearLayout) ctx.findViewById(R.id.llChat);
        ibAdd = (ImageView) llChat.findViewById(R.id.ibAdd);
        ibVoice = (ImageView) llChat.findViewById(R.id.ibVoice);
        ibFace = (ImageView) llChat.findViewById(R.id.ibFace);  //表情按钮
        btnRecorder = (Button) llChat.findViewById(R.id.btnRecorder);  //语音
        etInput = (EditText) llChat.findViewById(R.id.etInput);
        etInput.addTextChangedListener(mTextWatcher);
        btnSend = (TextView) llChat.findViewById(R.id.btnSend);
        llHide = (LinearLayout) llChat.findViewById(R.id.llHide);
        llPic = (LinearLayout) llChat.findViewById(R.id.ll_media_content);  //功能布局
        gvTalk = (GridView) llPic.findViewById(R.id.app_panel_grid);  //显示功能控件
        // 表情
        expressionGridView = (ExpressionGridView) ctx.findViewById(R.id.gvTalk);  //表情布局
        expressionGridView.initEt(etInput);
        judgeText();

        SoftKeyboardUtil.observeSoftKeyboard(ctx, false, new SoftKeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
//                L.e(softKeybardHeight  + " ------ " + visible);
            }
        });

        //如果之前是语音，则下次进来的时候还是语音
        if (WPf.getInstance().get(Hks.voice_pre + "|" + businessId, Boolean.class, false)) {
            bVoice = true;
            hideKeyboard();
            ibVoice.setImageResource(R.drawable.selector_keyboard_icon);
            llHide.setVisibility(View.GONE);
            setEtVisual(View.GONE);
            btnRecorder.setVisibility(View.VISIBLE);
        }

        ibAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToSend();
                if (bAdd) {
                    bAdd = false;
                    llHide.setVisibility(View.GONE);
                } else {
                    bAdd = true;
                    etInput.clearFocus();
                    hideKeyboard();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llHide.setVisibility(View.VISIBLE);
                            llPic.setVisibility(View.VISIBLE);
                            expressionGridView.setVisibility(View.GONE);

                            bClickFirst = true;
                            setEtVisual(View.VISIBLE);
                            btnRecorder.setVisibility(View.GONE);
                            ibVoice.setImageResource(R.drawable.selector_voice_icon);
                            bVoice = false;
                        }
                    }, 300);
                }
            }
        });
        ibFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToSend();
                hideKeyboard();
                if (bFace) {
                    llHide.setVisibility(View.GONE);
                    expressionGridView.setVisibility(View.GONE);
                    llPic.setVisibility(View.GONE);
                    setEtVisual(View.VISIBLE);
                    btnRecorder.setVisibility(View.GONE);
                    bFace = false;
                } else {
                    llHide.setVisibility(View.VISIBLE);
                    expressionGridView.setVisibility(View.VISIBLE);
                    llPic.setVisibility(View.GONE);
                    setEtVisual(View.VISIBLE);
                    btnRecorder.setVisibility(View.GONE);
                    bFace = true;
                }

            }
        });

        ibVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToSend();
                if (bVoice) {
                    setEtVisual(View.VISIBLE);
                    btnRecorder.setVisibility(View.GONE);
                    ibVoice.setImageResource(R.drawable.selector_voice_icon);
                    llHide.setVisibility(View.GONE);
                    bVoice = false;
                } else {
                    hideKeyboard();
                    ibVoice.setImageResource(R.drawable.selector_keyboard_icon);
                    llHide.setVisibility(View.GONE);
                    setEtVisual(View.GONE);
                    btnRecorder.setVisibility(View.VISIBLE);
                    bVoice = true;
                }
            }
        });

        // 点击表情图片弹框收起
        etInput.setClickable(true);
        etInput.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollToSend();
                llHide.setVisibility(View.GONE);
                bAdd = false;
            }
        });

        // etInput.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (WPf.getInstance().get(Hks.msg_enterbtn_sendmsg, Boolean.class, false)) {
                    if (arg1 == EditorInfo.IME_ACTION_UNSPECIFIED) {
                        String content = etInput.getText().toString().trim();
                        if (StrUtil.notEmptyOrNull(content)) {
                            if (talkBarInterface != null) {
                                talkBarInterface.sendText(content);
                            }
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        etInput.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                scrollToSend();
                if (bClickFirst) {
                    llHide.setVisibility(View.GONE);
                    bAdd = false;
                    bClickFirst = false;
                }
            }
        });
        etInput.setOnKeyListener(keyListen);

        // 发消息文字
        btnSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scrollToSend();
                if (talkBarInterface != null) {
                    String text = etInput.getText().toString();
                    if (StrUtil.isEmptyOrNull(text)) {
                        return;
                    } else {
                        talkBarInterface.sendText(text);
                    }
                }
                // 显示加号+
                btnSend.setVisibility(View.GONE);
                ibAdd.setVisibility(View.VISIBLE);
            }
        });

        recordVoiceView = new RecordVoiceView(ctx, btnRecorder, talkBarInterface);
        recordVoiceView.initVoice();
        initGvTalk();
    }

    public boolean isbVoice() {
        return bVoice;
    }

    private void judgeText() {
        String tmp = etInput.getText().toString();
        if (StrUtil.notEmptyOrNull(tmp)) {
            btnSend.setVisibility(View.VISIBLE);
            ibAdd.setVisibility(View.GONE);
        } else {
            btnSend.setVisibility(View.GONE);
            ibAdd.setVisibility(View.VISIBLE);
        }
    }

    public boolean isDiscuss() {
        try {
            Integer.parseInt(this.businessId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 初始化图片选框
    private void initGvTalk() {
        ArrayList<TalkGridData> talkGrids = new ArrayList<TalkGridData>();
        talkGrids.add(new TalkGridData(AddMediaType.PICTURE));
        talkGrids.add(new TalkGridData(AddMediaType.VIDEO));
//        bHB = WPfMid.getInstance().get(Hks.key_hb_open, Boolean.class, false);
//        L.e("TalkBanner" + Hks.key_hb_open + "=" + bHB);
//        if (bHB) {
//            talkGrids.add(new TalkGridData(AddMediaType.RED_PACKET));
//        }
        talkGrids.add(new TalkGridData(AddMediaType.SHIKOU));
//        talkGrids.add(new TalkGridData(AddMediaType.LOC));
//        PlugConfig config = WeqiaApplication.getInstance().getPlugConfig();
//        if (config.isFile()) {
            talkGrids.add(new TalkGridData(AddMediaType.FILE));
//        }

//        if (isDiscuss()) {
            talkGrids.add(new TalkGridData(AddMediaType.NOTICE));
//        }
//        talkGrids.add(new TalkGridData(AddMediaType.LINK));
//        talkGrids.add(new TalkGridData(AddMediaType.BUSINESS_CARD));
        barAdapter = new BarAdapter(ctx, talkGrids);
        gvTalk.setAdapter(barAdapter);
        gvTalk.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TalkGridData gridData = (TalkGridData) parent.getItemAtPosition(position);
                if (gridData == null) {
                    return;
                }
                int type = gridData.getType();
                if (type == AddMediaType.PICTURE.value) {
                    SelectMediaUtils.addPic(ctx);
                } else if (type == AddMediaType.LOC.value()) {
                    SelectMediaUtils.addLoc(ctx);
                } else if (type == AddMediaType.VIDEO.value()) {
                    SelectMediaUtils.addVideo(ctx, 1);
                } else if (type == AddMediaType.FILE.value()) {
                    SelectMediaUtils.addLocalFile(ctx);
                }
//                else if (type == AddMediaType.NOTICE.value()) {
//                    addTnotice(ctx);
//                } else if (type == AddMediaType.LINK.value()) {
//                    addLink(ctx);
//                }else if (type == AddMediaType.BUSINESS_CARD.value()) {
//                    addBusinessCard(ctx);
//                }
            }
        });
    }

    public void onResume() {
        StrUtil.etResume(businessId, etInput);
        StrUtil.etSelectionLast(etInput);
        judgeText();
    }

    public void onPause() {
        StrUtil.etSave(businessId, etInput);
    }

    /**
     * 隐藏键盘等view
     */
    public void hideView() {
        hideKeyboard();
        llHide.setVisibility(View.GONE);
        bAdd = false;
    }

    public EditText getEtInput() {
        return etInput;
    }

    /**
     * 设置bar的可见性
     *
     * @param visual
     */
    public void setBarVisual(int visual) {
        llChat.setVisibility(visual);
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
        // initListView(plTalk);
    }

    private void scrollToSend() {
        if (talkBarInterface != null) {
            talkBarInterface.scrollToSend();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String str = s.toString();
            if (StrUtil.notEmptyOrNull(str)) {
                String input = str.substring(start, str.length());
                String pre = str.replace(input, "");
                if (input.equals("@")) {
                    if (StrUtil.isEmptyOrNull(pre) || pre.endsWith(" ")) {
                        if (talkBarInterface != null) {
                            talkBarInterface.artPeople();
                        }
                    }
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            int inputCount = temp.length();
            if (!WPf.getInstance().get(Hks.msg_enterbtn_sendmsg, Boolean.class, false)) {
                if (inputCount > 0) {
                    btnSend.setVisibility(View.VISIBLE);
                    ibAdd.setVisibility(View.GONE);
                } else {
                    btnSend.setVisibility(View.GONE);
                    ibAdd.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private void setEtVisual(int visual) {
        etInput.setVisibility(visual);
        ibFace.setVisibility(visual);
    }

    private OnKeyListener keyListen = new OnKeyListener() {
        public boolean onKey(View view, int i, KeyEvent keyevent) {
            if (i == KeyEvent.KEYCODE_DEL) {
                if (talkBarInterface != null) {
                    talkBarInterface.delChar(keyevent);
                }
            }
            return false;
        }
    };

    public GridView getGvTalk() {
        return gvTalk;
    }

    public void setGvTalk(GridView gvTalk) {
        this.gvTalk = gvTalk;
    }
}
