
package com.weqia.wq.component.modules;

import android.os.Bundle;
import android.view.View;

import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.modules.assist.ShowInfoFragement;

public class ShowInfoActivity extends SharedDetailTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft);
        sharedTitleView.getRlBanner().setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new ShowInfoFragement())
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

}
