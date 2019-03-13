package com.weqia.wq.component.utils.locate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.weqia.utils.L;
import com.weqia.utils.StrUtil;
import com.weqia.utils.TimeUtils;
import com.weqia.utils.ViewUtils;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;
import com.weqia.wq.component.utils.DialogUtil;
import com.weqia.wq.component.utils.request.ResultEx;
import com.weqia.wq.component.utils.request.ServiceParams;
import com.weqia.wq.component.utils.request.ServiceRequester;
import com.weqia.wq.component.utils.request.UserService;
import com.weqia.wq.data.ComponentReqEnum;
import com.weqia.wq.data.MyLocData;
import com.weqia.wq.data.PosData;
import com.weqia.wq.data.SearchEnum;
import com.weqia.wq.data.global.GlobalConstants;

import java.io.File;
import java.util.List;

@SuppressLint("SdCardPath")
public class LocationActivity extends SharedDetailTitleActivity implements OnItemClickListener {
    private MapView mMapView = null; // 地图View
    private BaiduMap mMapController = null;
    private BitmapDescriptor bdLocationPic;
    // UI相关
    private boolean isRequest = false;// 是否手动触发请求定位
    private boolean isFirstLoc = true;// 是否首次定位
    private ListView llPos;
    private LocateListAdapter adapter;
    private MyLocData myLocData;
    private MyLocData positionLocData;
    // private LocationOverlay pointOverlay;
    private List<PosData> posDatas;
    // private MyLocationData locData = null;
    private GetMyLocation myLocation;
    private MyLocData transferData;
    private String title = "位置信息";

    // /**
    // * 当前地点击点
    // */
    // private LatLng currentPt;

    @SuppressWarnings("unused")
    private Marker mMarkerA;
    private BitmapDescriptor bdA;
    private BitmapDescriptor bdB;
    @SuppressWarnings("unused")
    private boolean haveLocation = false;
    private RelativeLayout rlMap;

    // 是否可选择
    private boolean canSelct = false;
    // 是否需要位置
    private boolean wantMyPos = true;
    // 选中的位置
    private LatLng selectLatLng;
    private MiddleIcon miView;
    private static final int REQ_SER_LOC = 1090;
    private PosData searchedPos;

    boolean bShowRefresh = false;
    private TextView tvTime;
    private static Dialog mNaviDialog;
    MyLocData justLoction;
    private String locationTime = null;
    boolean bReLocation = false;
    boolean bSelectedShow = false;
    private String rightStr;
    private LocationActivity ctx;
    private int currentSelect = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationoverlay);
        ctx = this;
        initView();
        initData();
    }

    private void initData() {
        tvTime.setVisibility(View.GONE);
        boolean bJustMap = false;
        transferData = (MyLocData) getDataParam();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bShowRefresh = extras.getBoolean(GlobalConstants.KEY_BASE_BOOLEAN);
            title = extras.getString(GlobalConstants.KEY_TOP_BANNER_TITLE);
            if (StrUtil.isEmptyOrNull(title)) {
                title = "位置";
            }
            bSelectedShow = extras.getBoolean("bSelectedShow");
            rightStr = extras.getString("rightStr");
        }
        if (transferData != null) {
            if (bShowRefresh) {
                getSysTime();
                sharedTitleView.initTopBanner(title, "刷新");
            } else {
                sharedTitleView.initTopBanner(title);
            }
            haveLocation = true;
            LatLng llA = null;
            if (transferData.getLatitude() != null && transferData.getLongitude() != null) {
                llA = new LatLng(transferData.getLatitude(), transferData.getLongitude());
                OverlayOptions ooA =
                        new MarkerOptions().position(llA).icon(bdB).zIndex(9).draggable(true);
                mMarkerA = (Marker) (mMapController.addOverlay(ooA));
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);
                mMapController.animateMapStatus(msu);
                if (!bShowRefresh) {
                    setPopTips(transferData.getLongitude(), transferData.getLatitude(),
                            transferData.getAddrName(), transferData.getAddrStr());
                }
                if (StrUtil.notEmptyOrNull(title) && title.equals("客户的位置")) {
                    sharedTitleView.initTopBanner(title, "修改");
                }
            }
        } else {
            haveLocation = false;
            mMapController.setMyLocationEnabled(true);
        }

        if (transferData != null && transferData.isbJustMap()) {
            bJustMap = true;
        } else {
            bJustMap = false;
        }
        if (bJustMap) {
            canSelct = false;
            myLocData = (MyLocData) getDataParam();
            if (L.D) L.e("received mylocate" + myLocData.toString());
            addPoint(myLocData);
            llPos.setVisibility(View.GONE);
            ViewUtils.hideViews(this, R.id.button_loc);
        } else {
            ViewUtils.showViews(this, R.id.button_loc);
            if (transferData != null) {
                myLocData = transferData;
                if (bShowRefresh) {
                    sharedTitleView.initTopBanner(title, "刷新");
                } else {
                    if (StrUtil.isEmptyOrNull(rightStr)) {
                        sharedTitleView.initTopBanner("我的位置", "取消");
                    } else {
                        sharedTitleView.initTopBanner(title, rightStr);
                    }
                }
                GetMyLocation.getPoiByGeoCoder(transferData.getLatitude(),
                        transferData.getLongitude(), transferData.getAddrStr(),
                        myLocationPoiCallBack);
            } else {
                myLocation = new GetMyLocation();
                myLocation.initLocate(this, myLocationCallBack, myLocationPoiCallBack);
                // if (transferData != null && !transferData.isCanDelete()) {
                // sharedTitleView.initTopBanner("位置");
                // } else {

                if (bSelectedShow) {
                    if (StrUtil.isEmptyOrNull(rightStr)) {
                        sharedTitleView.initTopBanner(title, "发送");
                    } else {
                        sharedTitleView.initTopBanner(title, rightStr);
                    }

                } else {
                    sharedTitleView.initTopBanner("我的位置", "取消");
                }
                // }
                initLocate();
            }
        }
    }

    private void initView() {
        llPos = (ListView) findViewById(R.id.ll_pos);
        mMapView = (MapView) findViewById(R.id.bmapView);
        tvTime = (TextView) findViewById(R.id.tvTime);
        mMapController = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);  //地图状态更新类
        mMapController.setMapStatus(msu);
        bdLocationPic = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo_small); // 自动定位点
        bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo); // 自动定位点
        mMapController.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true,
                bdLocationPic)); //配置百度地图定位的一些属性，最后一个是地图上定位默认显示的样式，传null就是百度地图默认的样式！
        ViewUtils.bindClickListenerOnViews(this, this, R.id.button_loc);
        adapter = new LocateListAdapter(this);
        llPos.setAdapter(adapter);
        llPos.setOnItemClickListener(this);
        bdA = BitmapDescriptorFactory.fromResource(R.drawable.loc_middle);

        canSelct = getIntent().getBooleanExtra(GlobalConstants.KEY_CAN_SELECT, false);

        rlMap = (RelativeLayout) findViewById(R.id.rl_map);

        if (canSelct) {
            ViewUtils.showView(sharedTitleView.getIvSer());
            miView = new MiddleIcon(this);
            rlMap.addView(miView, new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            ViewUtils.hideView(miView);
            mMapController.setOnMapTouchListener(new OnMapTouchListener() {

                @Override
                public void onTouch(MotionEvent arg0) {
                    if (arg0.getAction() == MotionEvent.ACTION_MOVE) {
                        wantMyPos = false;
                        searchedPos = null;
                        if (miView != null) {
                            if (miView.getVisibility() != View.VISIBLE) {
                                ViewUtils.showView(miView);
                            }
                        }
                        // 手动触发移动位置
                        // 去掉之前的位置标识
                        mMapController.clear();
                        if (mMapController.getProjection() != null) {
                            selectLatLng =
                                    mMapController.getProjection().fromScreenLocation(
                                            new Point(MiddleIcon.w, MiddleIcon.h));
                        }
                        if (selectLatLng != null) {
                            GetMyLocation.getPoiByGeoCoder(selectLatLng.latitude,
                                    selectLatLng.longitude, null, myLocationPoiCallBack);
                        }
                    }
                }
            });
        }
    }


    public static void naviDlg(final Context ctx, final double lon, final double lat,
                               final String title, final String describle) {
        mNaviDialog =
                DialogUtil.initLongClickDialog(ctx, "", new String[]{"高德地图", "百度地图"},
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mNaviDialog.dismiss();
                                int which = (Integer) v.getTag();
                                switch (which) {
                                    case 0:
                                        openGaoDeMap(ctx, lon, lat, title, describle);
                                        break;
                                    case 1:
                                        openBaiduMap(ctx, lon, lat, title, describle);
                                        break;
                                }
                            }
                        });
        mNaviDialog.show();
    }


    /**
     * 设置悬浮气泡
     */
    public void setPopTips(final double lon, final double lat, final String title,
                           final String describle) {
        LatLng latLng = new LatLng(lat, lon);
        View popTips =
                LayoutInflater.from(getApplicationContext()).inflate(R.layout.map_pop_marker, null);

        String titleShow = "[位置]";
        if (StrUtil.notEmptyOrNull(title) && StrUtil.notEmptyOrNull(describle)) {
            if (!title.equals(describle)) {
                titleShow = title;
            }
        }
        ((TextView) popTips.findViewById(R.id.addrTitle)).setText(titleShow);
        ((TextView) popTips.findViewById(R.id.addrContent)).setText(describle);
        InfoWindow.OnInfoWindowClickListener listener = new InfoWindow.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick() {
                initNavi(LocationActivity.this, lon, lat, title, describle);

            }
        };
        InfoWindow mInfoWindow =
                new InfoWindow(BitmapDescriptorFactory.fromView(popTips), latLng,
                        -(BitmapFactory.decodeResource(getResources(), R.drawable.loc_middle)
                                .getHeight()), listener);
        mMapController.showInfoWindow(mInfoWindow);
    }

    public static void initNavi(Context ctx, double lon, double lat, String title, String describle) {
        // 高德的包 com.autonavi.minimap
        if (isInstallPackage("com.autonavi.minimap") && isInstallPackage("com.baidu.BaiduMap")) {
            naviDlg(ctx, lon, lat, title, describle);
        } else {
            if (isInstallPackage("com.autonavi.minimap")) {
                openGaoDeMap(ctx, lon, lat, title, describle);
            } else if (isInstallPackage("com.baidu.BaiduMap")) {
                openBaiduMap(ctx, lon, lat, title, describle);
            } else {
                L.toastLong("请先安装高德/百度地图客户端~");
            }
        }
    }

    public static boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @SuppressWarnings("deprecation")
    public static void openBaiduMap(Context ctx, double lon, double lat, String title,
                                    String describle) {
        try {
            StringBuilder loc = new StringBuilder();
            loc.append("intent://map/direction?origin=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append("我的位置");
            loc.append("&destination=latlng:");
            loc.append(lat);
            loc.append(",");
            loc.append(lon);
            loc.append("|name:");
            loc.append(describle);
            loc.append("&mode=driving");
            loc.append("&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            Intent intent = Intent.getIntent(loc.toString());
            if (isInstallPackage("com.baidu.BaiduMap")) {
                ctx.startActivity(intent); // 启动调用
                Log.e("GasStation", "百度地图客户端已经安装");
            } else {
                Log.e("GasStation", "没有安装百度地图客户端");
            }
        } catch (Exception e) {
            L.toastShort("该版本百度地图不支持~~");
            e.printStackTrace();
        }
    }


    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    @SuppressWarnings("deprecation")
    public static void openGaoDeMap(Context ctx, double lon, double lat, String title,
                                    String describle) {
        try {
            double[] gd_lat_lon = bdToGaoDe(lon, lat);
            StringBuilder loc = new StringBuilder();
            loc.append("androidamap://viewMap?sourceApplication=" + ctx.getString(R.string.weqia_str));
            loc.append("&poiname=");
            loc.append(describle);
            loc.append("&lat=");
            loc.append(gd_lat_lon[0]);
            loc.append("&lon=");
            loc.append(gd_lat_lon[1]);
            loc.append("&dev=0");
            Intent intent = Intent.getIntent(loc.toString());
            ctx.startActivity(intent);
        } catch (Exception e) {
            L.toastShort("该版本高德地图不支持~~");
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == sharedTitleView.getButtonStringRight()) {
            if (StrUtil.notEmptyOrNull(rightStr) && rightStr.equalsIgnoreCase("下一步")) {
                if (myLocation != null) {
//                    PunchPeopleUtil.memChoose(this, null);
                    myLocation.locationClientStop();
                }
            } else if (bShowRefresh) {
                getSysTime();
                reLocation();
            } else if (StrUtil.notEmptyOrNull(title) && title.equals("客户的位置")) {
                reLocation();
                // bSelectedShow
            } else if (bSelectedShow) {
                if (myLocData != null) {
                    if (currentSelect == -1) {
                        selectItem(0);
                    }
                    Intent newIntent = new Intent();
                    newIntent.putExtra(GlobalConstants.KEY_LOC_DATA, myLocData);
                    this.setResult(RESULT_OK, newIntent);
                    this.finish();
                }
                // bSelectedShow
            } else {
                Intent newIntent = new Intent();
                newIntent.putExtra(GlobalConstants.KEY_LOC_DATA, new MyLocData());
                this.setResult(RESULT_OK, newIntent);
                this.finish();
            }
        } else if (v.getId() == R.id.button_loc) {
            if (myLocData != null) {
                wantMyPos = true;
                searchedPos = null;
                ViewUtils.hideView(miView);
                LatLng llA = new LatLng(myLocData.getLatitude(), myLocData.getLongitude());
                selectLatLng = llA;
                GetMyLocation.getPoiByGeoCoder(selectLatLng.latitude, selectLatLng.longitude, null,
                        myLocationPoiCallBack);

                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);
                mMapController.animateMapStatus(msu);
            }
        } else if (v == sharedTitleView.getIvSer()) {
            PosData posData = new PosData();
            if (myLocData != null) {
                posData.setY(myLocData.getLatitude());
                posData.setX(myLocData.getLongitude());
            } else if (transferData != null) {
                posData.setY(transferData.getLatitude());
                posData.setX(transferData.getLongitude());
            }

            Intent newIntent = new Intent(this, LocationSerActivity.class);
            newIntent.putExtra(GlobalConstants.KEY_PARAM_DATA, posData);
            newIntent.putExtra(GlobalConstants.KEY_SEARCH_TYPE, SearchEnum.S_NET_POIINFO.value());
            startActivityForResult(newIntent, REQ_SER_LOC);
        } else if (v == sharedTitleView.getButtonLeft()) {
            backDo();
        }
    }

    private void reLocation() {
        if (bReLocation) {
            L.toastShort("重新定位中~请稍后...");
            return;
        }
        if (transferData != null) {
            bReLocation = true;
            // 重新定位
            if (myLocation == null) {
                myLocation = new GetMyLocation();
            }
            myLocation.initLocate(ctx, new GetMyLocation.MyLocationCallBack() {
                public void MyLocationCallBackDo(MyLocationData locationData, MyLocData locationInfo) {
                    try {
                        bReLocation = false;
                        if (locationInfo != null) {
                            if (StrUtil.isEmptyOrNull(locationInfo.getAddrStr())) {
                                locationInfo.setAddrStr("[位置]");
                            }
                            transferData = locationInfo;
                            if (StrUtil.notEmptyOrNull(locationTime)) {
                                tvTime.setVisibility(View.VISIBLE);
                                tvTime.setText(TimeUtils.getDateMDHM(locationTime));
                            }
                            if (transferData.getLatitude() != null
                                    && transferData.getLongitude() != null) {
                                mMapController.clear();
                                LatLng llA =
                                        new LatLng(transferData.getLatitude(), transferData
                                                .getLongitude());
                                OverlayOptions ooA =
                                        new MarkerOptions().position(llA).icon(bdA).zIndex(9)
                                                .draggable(true);
                                mMarkerA = (Marker) (mMapController.addOverlay(ooA));
                                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);
                                mMapController.animateMapStatus(msu);
                            }
                            if (StrUtil.notEmptyOrNull(title) && title.equals("客户的位置")) {
                                // mMapController.clear();
                                GetMyLocation.getPoiByGeoCoder(transferData.getLatitude(),
                                        transferData.getLongitude(), null, myLocationPoiCallBack);
                            }
                        } else {
                            L.toastShort("定位失败,请检查网络~");
                        }
                    } catch (Exception e) {
                        L.toastShort("定位失败,请检查网络~");
                    }


                }
            }, new GetMyLocation.MyLocationPoiCallBack() {
                @Override
                public void MyLocationPoiCallBackDo(List<PosData> posDatas) {
                }
            });
            myLocation.getMyAddr();
        }
    }


    @Override
    public void onBackPressed() {
        backDo();
    }

    private void backDo() {
        // if (transferData != null && bShowRefresh) {
        // Intent newIntent = new Intent();
        // newIntent.putExtra(GlobalConstants.KEY_LOC_DATA, transferData);
        // setResult(Activity.RESULT_OK, newIntent);
        // }
        finish();
    }

    GetMyLocation.MyLocationCallBack myLocationCallBack = new GetMyLocation.MyLocationCallBack() {
        @Override
        public void MyLocationCallBackDo(MyLocationData locationData, MyLocData locationInfo) {
            myLocData = locationInfo;
            positionLocData = locationInfo;
            mMapController.setMyLocationData(locationData);
            // 是手动触发请求或首次定位时，移动到定位点
            if (isRequest || isFirstLoc) {
                // 移动地图到定位点
                if (isFirstLoc) {
                    LatLng llA = new LatLng(myLocData.getLatitude(), myLocData.getLongitude());
                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);
                    mMapController.animateMapStatus(msu);
                }
                isRequest = false;
            }
            // 首次定位完成
            isFirstLoc = false;
        }
    };

    GetMyLocation.MyLocationPoiCallBack myLocationPoiCallBack =
            new GetMyLocation.MyLocationPoiCallBack() {
                @Override
                public void MyLocationPoiCallBackDo(List<PosData> posDatas) {
                    currentSelect = -1;
                    LocationActivity.this.posDatas = posDatas;
//                    boolean havSelect = false;
                    if (LocationActivity.this.posDatas != null) {
                        for (PosData posData : posDatas) {
                            if (transferData != null) {
                                if ((transferData.getLongitude() == posData.getX() && transferData
                                        .getLatitude() == posData.getY())
                                        || (StrUtil.notEmptyOrNull(transferData.getAddrName()) && transferData
                                        .getAddrName().equals(posData.getName()))) {
                                    // posData.setSelect(true);
                                    MyLocData tmpLocData =
                                            new MyLocData(posData.getY(), posData.getX(),
                                                    transferData.getProvinc(),
                                                    transferData.getCity(),
                                                    transferData.getDistrict(),
                                                    transferData.getStreet(),
                                                    transferData.getStrNum(),
                                                    transferData.getRadius(),
                                                    transferData.getPoi(), posData.getAddr(),
                                                    transferData.getLocType(),
                                                    transferData.getTime(), posData.getName(),
                                                    false);
                                    addPoint(tmpLocData);
//                                    havSelect = true;
                                }
                            }

                        }
                    }
                    // 如果需要自己的位置，则第一个
                    if (wantMyPos) {
                        // 如果没有其他位置信息则添加一个当前的位置
                        if (posDatas.size() == 0) {
                            if (positionLocData != null) {
                                PosData myPosData =
                                        new PosData(positionLocData.getAddrStr(), "0",
                                                positionLocData.getLatitude(), "[位置]", "",
                                                positionLocData.getLongitude());
                                posDatas.add(0, myPosData);
                            }
                        }
                    } else {
                        // 没有自己的位置，选中模式
                        if (searchedPos != null) {
                            // 如果有查询到的点
                            posDatas.add(0, searchedPos);
                        } else {
                            // 如果选中的点附近没有pos，则添加一点选中的那个点
                            if (posDatas.size() == 0) {
                                if (selectLatLng != null) {
                                    posDatas.add(new PosData("选中位置", "0", selectLatLng.latitude,
                                            "[位置]", "", selectLatLng.longitude));
                                }
                            }
                        }
                    }
                    if (bSelectedShow) {
                        adapter.setItems(posDatas, 0);
                    } else {
                        adapter.setItems(posDatas, -1);
                    }
                    llPos.setVisibility(View.VISIBLE);
                }
            };


    private void initLocate() {
        myLocation.getMyAddr();
    }

    private void addPoint(MyLocData locationData) {
        myLocData = locationData;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter != null) {
            selectItem(position);
            adapter.setItems(posDatas, position);
            if (!bSelectedShow) {
                Intent newIntent = new Intent();
                newIntent.putExtra(GlobalConstants.KEY_LOC_DATA, myLocData);
                this.setResult(RESULT_OK, newIntent);
                this.finish();
            }
        }
    }

    private int selectItem(int position) {
        currentSelect = position;
        PosData currentData = (PosData) adapter.getItem(position);
        if (currentData != null && myLocData != null) {
            MyLocData tmpLocData =
                    new MyLocData(currentData.getY(), currentData.getX(), myLocData.getProvinc(),
                            myLocData.getCity(), myLocData.getDistrict(), myLocData.getStreet(),
                            myLocData.getStrNum(), myLocData.getRadius(), myLocData.getPoi(),
                            currentData.getAddr(), myLocData.getLocType(), String.valueOf(System
                            .currentTimeMillis()), currentData.getName(), false);
            myLocData = tmpLocData;
            if (!bSelectedShow) {
                position = -1;
            }
        }
        return position;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        super.onDestroy();
        try {
            if (myLocation != null) {
                myLocation.locationClientStop();
            }
            mMapView.onDestroy();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_SER_LOC) {
                PosData posData = (PosData) data.getSerializableExtra("posData");
                if (posData != null) {
                    wantMyPos = false;
                    searchedPos = posData;
                    if (miView != null) {
                        if (miView.getVisibility() != View.VISIBLE) {
                            ViewUtils.showView(miView);
                        }
                    }

                    LatLng llA = new LatLng(posData.getY(), posData.getX());
                    selectLatLng = llA;
                    GetMyLocation.getPoiByGeoCoder(selectLatLng.latitude, selectLatLng.longitude,
                            null, myLocationPoiCallBack);

                    MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(llA);

                    mMapController.animateMapStatus(msu);


                    // init choose first
                    if (bSelectedShow) {
                        MyLocData tmpLocData =
                                new MyLocData(posData.getY(), posData.getX(),
                                        myLocData.getProvinc(), myLocData.getCity(),
                                        myLocData.getDistrict(), myLocData.getStreet(),
                                        myLocData.getStrNum(), myLocData.getRadius(),
                                        myLocData.getPoi(), posData.getAddr(),
                                        myLocData.getLocType(), String.valueOf(System
                                        .currentTimeMillis()), posData.getName(), false);
                        myLocData = tmpLocData;
                    }
                }
            } else if (requestCode == 300) {
//                Intent intent = new Intent(LocationActivity.this, PunchRuleDetailActivity.class);
//                intent.putExtra(GlobalConstants.KEY_TOP_BANNER_TITLE, "新建考勤规则");
//                intent.putExtra("locate", myLocData);
//                startActivity(intent);
//                this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void getSysTime() {
        ServiceParams params = new ServiceParams(ComponentReqEnum.GET_SYSTIME.order());
        UserService.getDataFromServer(params, new ServiceRequester(LocationActivity.this) {

            @Override
            public void onResult(ResultEx resultEx) {
                if (resultEx.isSuccess()) {
                    locationTime = resultEx.getObject();
                    if (StrUtil.notEmptyOrNull(locationTime)) {
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(TimeUtils.getDateMDHM(locationTime));
                    }
                }
            }

            @Override
            public void onError(Integer errCode) {
            }
        });
    }

}
