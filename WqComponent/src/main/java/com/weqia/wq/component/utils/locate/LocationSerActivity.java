package com.weqia.wq.component.utils.locate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.component.activity.SharedSearchActivity;
import com.weqia.wq.component.activity.assist.SharedSearchAdapter;
import com.weqia.wq.data.PosData;

import java.util.ArrayList;
import java.util.List;

public class LocationSerActivity extends SharedSearchActivity<PoiInfo>
        implements
        OnGetSuggestionResultListener {
    private SuggestionSearch mSuggestionSearch = null;
    private PosData paramPosData;
    private LocateSerchAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        paramPosData = (PosData) getDataParam();
        //禁止自动搜索-点击搜索才搜索
        autoSearch = false;
    }

    @Override
    public SharedSearchAdapter<PoiInfo> getAdapter() {
        if (adapter == null) {
            adapter = new LocateSerchAdapter(LocationSerActivity.this);
        }
        return adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PoiInfo msgData = (PoiInfo) parent.getItemAtPosition(position);
        try {
            PosData posData = new PosData(msgData.address, "0", msgData.location.latitude, msgData.name, "", msgData.location.longitude);
            Intent newIntent = new Intent();
            newIntent.putExtra("posData", posData);
            this.setResult(RESULT_OK, newIntent);
            this.finish();
        } catch (Exception e) {
            L.toastShort("地址无效~");
        }
    }

    @Override
    protected void onDestroy() {
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    protected void searchDataFromNet(Integer prewId, Integer nextId) {
        loadComplete();
        if (paramPosData == null) {
            paramPosData = new PosData();
        }
        //搜索建议的点 全国范围内
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(lastText)
                .city("全国"));
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        try {
            List<SuggestionResult.SuggestionInfo> infos = suggestionResult.getAllSuggestions();
            getSelDatas().clear();
            List<PoiInfo> poiInfos = new ArrayList<PoiInfo>();
            if (StrUtil.listNotNull(infos)){  //修改单聊、微会议、考勤等位置搜索有时显示不对（之前没有做非空的处理）
                for (SuggestionResult.SuggestionInfo tmp : infos) {
                    if (tmp.pt != null) {
                        PoiInfo poi = new PoiInfo();
                        poi.city = tmp.city;
                        poi.name = tmp.key;
                        poi.address = tmp.city + tmp.district;
                        if (StrUtil.isEmptyOrNull(poi.address)) {
                            poi.address = lastText;
                        }
                        poi.location = tmp.pt;
                        poiInfos.add(poi);
                    }
                }
            }
            getSelDatas().addAll(poiInfos);
            getAdapter().setItems(getSelDatas());
        } catch (Exception e) {
        }
    }
}
