package com.weqia.wq.component.utils.locate;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.data.MyLocData;
import com.weqia.wq.data.PosData;
import com.weqia.wq.data.global.WeqiaApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MX on 2014/6/19.
 */
public class GetMyLocation {
    // 定位相关
    public LocationClient mLocClient;
    private MyLocationData paramData = null;
    public static MyLocData myLocData;
    private MyLocationCallBack locationCallBack;
    private MyLocationPoiCallBack poiCallBack;
    private boolean isLose = false;
    private boolean onlyOne = true;  //只重新定位一次！

    public void initLocate(Context ctx, MyLocationCallBack locationCallBack,
                           MyLocationPoiCallBack poiCallBack) {
        initLocate(ctx, locationCallBack, poiCallBack, 0);
    }

    public void initLocate(Context ctx, MyLocationCallBack locationCallBack,
                           MyLocationPoiCallBack poiCallBack, int scanSpan) {
        try {
            if (ctx == null) {
                ctx = WeqiaApplication.ctx;
            }
            this.locationCallBack = locationCallBack;
            this.poiCallBack = poiCallBack;
            // 定位初始化
            mLocClient = new LocationClient(ctx);
            mLocClient.registerLocationListener(new MyLocationListenner());
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationMode.Hight_Accuracy);
            option.setAddrType("all");// 返回的定位结果包含地址信息
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setOpenGps(true);
            option.setScanSpan(scanSpan);
            option.disableCache(true);
            option.setIsNeedAddress(true);
            option.setProdName(ctx.getString(R.string.app_name));
            mLocClient.setLocOption(option);
            mLocClient.start();
        }catch (Exception e){
            L.toastShort("定位失败，请重试~");
        }
    }
    public void initLocate(Context ctx, MyLocationCallBack locationCallBack) {
        try {
            if (ctx == null) {
                ctx = WeqiaApplication.ctx;
            }
            this.locationCallBack = locationCallBack;
            // 定位初始化
            mLocClient = new LocationClient(ctx);
            mLocClient.registerLocationListener(new MyLocationListenner());
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationMode.Hight_Accuracy);
            option.setAddrType("all");// 返回的定位结果包含地址信息
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setOpenGps(true);

            option.disableCache(true);
            option.setIsNeedAddress(true);
            option.setProdName(ctx.getString(R.string.app_name));
            mLocClient.setLocOption(option);
            mLocClient.start();
        }catch (Exception e){
            L.toastShort("定位失败，请重试~");
        }
    }

    public void getMyAddr() {
        try {
            isLose = false;  //默认是定位失败的
            if (L.D) L.i("getMyAddr");

            if (mLocClient != null && mLocClient.isStarted()) {
                mLocClient.requestLocation();
                mLocClient.start();// requestPoi();
            }
        }catch (Exception e){
            L.toastShort("定位失败，请重试~");
        }
    }

    /**
     * 定位SDK监听函数
     */


//    定位返回错误码查询：
//            61 ： GPS定位结果，GPS定位成功。
//            62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
//            63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
//            65 ： 定位缓存的结果。
//            66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
//            67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
//            68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
//            161： 网络定位结果，网络定位定位成功。
//            162： 请求串密文解析失败，一般是由于客户端SO文件加载失败造成，请严格参照开发指南或demo开发，放入对应SO文件。
//            167： 服务端定位失败，请您检查是否禁用获取位置
// 信息权限，尝试重新请求定位。
//            502： key参数错误，请按照说明文档重新申请KEY。
//            505： key不存在或者非法，请按照说明文档重新申请KEY。
//            601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
//            602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
//            501～700：key验证失败，请按照说明文档重新申请KEY。
//    如果不能定位，请记住这个返回值，并到百度LBS开放平台论坛Andriod定位SDK版块中进行交流 http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=10 。若返回值是162~167，请将错误码、imei和定位时间反馈至loc-bugs@baidu.com，以便我们跟进追查问题。
    public class MyLocationListenner implements BDLocationListener {

        public MyLocationListenner() {
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null||location.getLatitude()==0.0||location.getLongitude()==0.0){
                if (!isLose) {  //定位失败
                    if (onlyOne) {
                        getMyAddr();
                        L.e("定位失败，重新定位一次");
                        onlyOne = false;
                    }
                }
                return;
            }
            isLose= true;  


            if (location.getLocType() != 61 && location.getLocType() != 161) {
                L.e("定位失败(返回值：" + location.getLocType() + ")");
                if (locationCallBack != null) {
                    locationCallBack.MyLocationCallBackDo(null, null);
                }
                return;
            }


            myLocData =
                    new MyLocData(location.getLatitude(), location.getLongitude(),
                            location.getProvince(), location.getCity(), location.getDistrict(),
                            location.getStreet(), location.getStreetNumber(), location.getRadius()
                            + "", "", location.getAddrStr(), location.getLocType() + "",
                            String.valueOf(System.currentTimeMillis()), null, false);
            paramData =
                    new MyLocationData.Builder().latitude(location.getLatitude())
                            .longitude(location.getLongitude()).accuracy(location.getRadius())
                            .direction(location.getDirection()).build();

            if (L.D) L.i("init MyLocationListenner ");
            if (L.D) L.i("定位信息"+myLocData.toString());
            if (locationCallBack != null) {
                locationCallBack.MyLocationCallBackDo(paramData, myLocData);
            }
            if (poiCallBack != null) {
                getPoiByGeoCoder(myLocData.getLatitude(), myLocData.getLongitude(),
                        myLocData.getAddrStr(), poiCallBack);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    // 反Geo搜索 附近点
    public static void getPoiByGeoCoder(Double lat, Double lng, final String addr, final MyLocationPoiCallBack poiCallBack) {
        GeoCoder mSearch = GeoCoder.newInstance();
        LatLng ll = new LatLng(lat, lng);
        if (poiCallBack == null) {
            return;
        }

        boolean bSuccess = mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
        if (bSuccess) {
            mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                    List<PosData> posDatas = new ArrayList<PosData>();
                    List<PoiInfo> poiInfo = reverseGeoCodeResult.getPoiList();
                    if (StrUtil.notEmptyOrNull(addr) && myLocData != null) {
                        posDatas.add(new PosData(addr, addr, myLocData.getLatitude(), "[位置]",
                                "[位置]", myLocData.getLongitude()));
                    }
                    L.i("poiInfo==>>"+poiInfo);
                    if (StrUtil.listNotNull(poiInfo)) {
                        for (PoiInfo info : poiInfo) {
                            posDatas.add(new PosData(info.address, info.address,
                                    info.location.latitude, info.name, info.name,
                                    info.location.longitude));
                        }
                    }
                    if (poiCallBack != null) {
                        poiCallBack.MyLocationPoiCallBackDo(posDatas);
                    }

                }
            });
        } else {
            if (poiCallBack != null) {
                poiCallBack.MyLocationPoiCallBackDo(new ArrayList<PosData>());
            }
        }

    }

    public interface MyLocationCallBack {
        public void MyLocationCallBackDo(MyLocationData locationData, MyLocData locationInfo);
    }

    public interface MyLocationPoiCallBack {
        public void MyLocationPoiCallBackDo(List<PosData> posDatas);
    }


    public void locationClientStop() {
        if (L.D) L.i("取消定位");
        if (mLocClient != null) mLocClient.stop();
    }

}
