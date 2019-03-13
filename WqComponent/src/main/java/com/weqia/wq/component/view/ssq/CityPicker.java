package com.weqia.wq.component.view.ssq;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.global.ComponentUtil;
import com.weqia.wq.data.db.AreaData;
import com.weqia.wq.data.db.CityData;
import com.weqia.wq.data.db.ProvinceData;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.ArrayList;
import java.util.List;

public class CityPicker extends LinearLayout {

    private WheelView mProvincePicker;
    private WheelView mCityPicker;
    private WheelView mAreaPicker;

    public Integer mProvinceIndex;
    public Integer mCityIndex;
    public Integer mAreaIndex;

    private SSQUtil mSSQUtil;
    private List<ProvinceData> mProvinceList = new ArrayList<ProvinceData>();
    private List<CityData> mCityList = new ArrayList<CityData>();
    private List<AreaData> mAreaList = new ArrayList<AreaData>();


    /**
     * 默认没选中市、区  第一行NULL
     */
    private boolean isFirstNull = true;


    public CityPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attribute = context.obtainStyledAttributes(attrs,
                R.styleable.WheelView);
        isFirstNull = attribute
                .getBoolean(R.styleable.WheelView_isFirstNull, false);
        attribute.recycle();
        getAreaInfo();
    }


    public CityPicker(Context context) {
        this(context, null);
    }

    private void getAreaInfo() {


        mSSQUtil = new SSQUtil();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.pop_ssq, this);

        mProvincePicker = (WheelView) findViewById(R.id.province);
        mCityPicker = (WheelView) findViewById(R.id.city);
        mAreaPicker = (WheelView) findViewById(R.id.area);

        mProvinceList = mSSQUtil.getProvinces();

        if (StrUtil.listNotNull(mProvinceList)) {
            ArrayList<String> items = new ArrayList<>();
            for (ProvinceData tmp : mProvinceList) {
                items.add(tmp.getProvince_n());
            }
            mProvincePicker.setData(items);
            mProvincePicker.setDefault(0);
        } else {
            mSSQUtil.resetDb();
            ComponentUtil.configWqDb(WeqiaApplication.ctx);
        }
        if (StrUtil.listNotNull(mProvinceList)) {
            mCityList = mSSQUtil.getCitysByProvince(mProvinceList.get(0).getProvince_i());
        }

        if (StrUtil.listNotNull(mCityList)) {
            if (isFirstNull) {
                mCityList.add(0, new CityData(null, ""));
            }
            ArrayList<String> items_city = new ArrayList<>();
            for (CityData tmp : mCityList) {
                items_city.add(tmp.getCity_name());
            }
            mCityPicker.setData(items_city);
            mCityPicker.setDefault(0);
        }

        if (isFirstNull){
            mAreaList = mSSQUtil.getAreasByCity(mCityList.get(1).getCity_id());
        }else {
            mAreaList = mSSQUtil.getAreasByCity(mCityList.get(0).getCity_id());
        }

        if (StrUtil.listNotNull(mAreaList)) {
            if (isFirstNull) {
                mAreaList.add(0, new AreaData(null, ""));
            }
            mAreaIndex = mAreaList.get(0).getArea_id();
            ArrayList<String> items_area = new ArrayList<>();
            if (StrUtil.listIsNull(mAreaList)) {
                return;
            }
            for (AreaData tmp : mAreaList) {
                items_area.add(tmp.getArea_name());
            }
            mAreaPicker.setData(items_area);
            mAreaPicker.setDefault(0);
        } else {
            mSSQUtil.resetDb();
            ComponentUtil.configWqDb(WeqiaApplication.ctx);
            L.toastShort("城市列表载入失败,请重试!");
        }

        mProvincePicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
//                if (isFirstNull) {
//
//                    if (id == -1) {
//                        return;
//                    }
//
//                }

                if (StrUtil.listNotNull(mProvinceList)) {
                    mProvinceIndex = mProvinceList.get(id).getProvince_i();
                }
                mCityList = mSSQUtil.getCitysByProvince(mProvinceList.get(id).getProvince_i());
                if (StrUtil.listNotNull(mCityList)) {
                    if (isFirstNull) {
                        mCityList.add(0, new CityData(null, ""));
                    }
                    ArrayList<String> items_city = new ArrayList<>();
                    for (CityData tmp : mCityList) {
                        items_city.add(tmp.getCity_name());
                    }
                    mCityPicker.setData(items_city);
                    mCityPicker.setDefault(0);

                    if (isFirstNull){
                        mAreaList = mSSQUtil.getAreasByCity(mCityList.get(1).getCity_id());
                    }else {
                        mAreaList = mSSQUtil.getAreasByCity(mCityList.get(0).getCity_id());
                    }
                    if (StrUtil.listNotNull(mAreaList)) {
                        if (isFirstNull) {
                            mAreaList.add(0, new AreaData(null, ""));
                        }
                        mAreaIndex = mAreaList.get(0).getArea_id();
                        ArrayList<String> items_area = new ArrayList<>();
                        for (AreaData tmp : mAreaList) {
                            items_area.add(tmp.getArea_name());
                        }
                        mAreaPicker.setData(items_area);
                        mAreaPicker.setDefault(0);
                    }

                }


            }

            @Override
            public void selecting(int id, String text) {
            }
        });

        mCityPicker.setOnSelectListener(new WheelView.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                if (StrUtil.listNotNull(mCityList)) {
                    mCityIndex = mCityList.get(id).getCity_id();
                }
                mAreaList = mSSQUtil.getAreasByCity(mCityList.get(id).getCity_id());
                if (StrUtil.listNotNull(mAreaList)) {
                    if (isFirstNull) {
                        mAreaList.add(0, new AreaData(null, ""));
                    }
                    mAreaIndex = mAreaList.get(0).getArea_id();
                    ArrayList<String> items_area = new ArrayList<>();
                    for (AreaData tmp : mAreaList) {
                        items_area.add(tmp.getArea_name());
                    }
                    mAreaPicker.setData(items_area);
                    mAreaPicker.setDefault(0);
                }

            }

            @Override
            public void selecting(int id, String text) {

            }
        });

        mAreaPicker.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (StrUtil.listNotNull(mAreaList)) {
                    mAreaIndex = mAreaList.get(id).getArea_id();
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = ComponentUtil.dip2px(200);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result =  ComponentUtil.dip2px(150);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
