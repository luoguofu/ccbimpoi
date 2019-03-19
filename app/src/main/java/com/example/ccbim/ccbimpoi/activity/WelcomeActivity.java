package com.example.ccbim.ccbimpoi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.example.ccbim.ccbimpoi.MainNewActivity;
import com.example.ccbim.ccbimpoi.R;
import com.umeng.analytics.MobclickAgent;
import com.weqia.utils.L;
import com.weqia.utils.ViewUtils;
import com.weqia.utils.view.CommonImageView;
import com.weqia.wq.RouterUtil;
import com.weqia.wq.component.AttachService;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.eventbus.RefreshEvent;
import com.weqia.wq.data.global.WeqiaApplication;
import com.weqia.wq.data.newdemand.MemberProjectPower;
import com.weqia.wq.global.ComponentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * 欢迎界面
 * @author Dminter
 * @Description :
 */
public class WelcomeActivity extends FragmentActivity {

    private WelcomeActivity ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ViewUtils.setImageRes((CommonImageView) findViewById(R.id.iv_welcome), R.drawable.welcome);
        ctx = this;
        EventBus.getDefault().register(this);

//        ContactUtil.initWorkerMember();
//        ComponentUtil.initDbAndUser();  // TODO: 2019/3/14
//        XUtil.getCategoryDb();
//        XUtil.getNCategoryDb();
//        // 同步好友
//        WPf.getInstance().put(Hks.last_check_update_time, 0);

//        WPfMid.getInstance().remove("BimProjectListData");
//        ContactModule.removeCoDownloadContact();
//        if (WeqiaApplication.getInstance().getLoginUser().getCurrentProjectId() != null) {
//            getPermission(WeqiaApplication.getInstance().getLoginUser().getCurrentProjectId());
//        }
//        if (!ContactUtil.getFullContacts(ctx)) {
//            ContactUtil.syncMembers();
//            ContactUtil.syncProjectMembers();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    toApp();
                }
            }, 2000);
//        }
    }


    // 添加成功 刷新界面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final RefreshEvent event) {
        final int type = event.type;
//        if (type == ContactConstants.FULL_LOAD_COMPLETE) {
//            toApp();
//        }
    }

    //获取项目成员权限
    private void getPermission(String pjId) {
        ServiceParams serviceParams = new ServiceParams(ComponentReqEnum.GET_PROJECT_MEMBER_POWER.order());
        serviceParams.setPjId(pjId);
        UserService.getDataFromServer(serviceParams, new ServiceRequester() {
            @Override
            public void onResult(ResultEx resultEx) {
                MemberProjectPower memberProjectPower = resultEx.getDataObject(MemberProjectPower.class);
                WeqiaApplication.getInstance().setMemPower(memberProjectPower);
                L.e(resultEx.toString());
            }
        });
    }
    // 进入APP
    private void toApp() {
//        String msg = WPfMid.getInstance().get("BimProjectListData", String.class);
//        if (StrUtil.notEmptyOrNull(msg)) {
//        startService(new Intent(this, AttachService.class));
//        RouterUtil.routerActionSync(WelcomeActivity.this, "pvmain", "actodetail");
//        } else {
//            RouterUtil.routerActionSync(WelcomeActivity.this, "pvmain", "actomain");
//        }
        startActivity(new Intent(this, HomeActivity.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
