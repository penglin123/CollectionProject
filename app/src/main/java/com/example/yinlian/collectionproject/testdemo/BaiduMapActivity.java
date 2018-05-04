package com.example.yinlian.collectionproject.testdemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.library.baidumap.BaiduMapUtil;
import com.example.library.baidumap.OverlayManager;
import com.example.library.utils.ToastUtils;
import com.example.library.baidumap.TransitRouteOverlay;
import com.example.library.baidumap.WalkingRouteOverlay;
import com.example.yinlian.collectionproject.R;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

public class BaiduMapActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    // 定位相关
    private LocationClient mLocClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;//传感器
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;//维度
    private double mCurrentLon = 0.0;//经度
    private float mCurrentAccracy;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    Button requestLocButton;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    private Button button2;//签到功能
    private Button planning_on_foot;//步行导航
    private Button public_transport_planning;//公交车导航
    private GeoCoder geoCoder;//定位

    private LatLng startll;//定位起点

    private RoutePlanSearch routePlanSearch;//路径规划


    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    WalkingRouteResult nowResultwalk = null;
    boolean hasShownDialogue = false;
    private PlanNode startNode;
    private PlanNode endNode;

    private int tag = 0;

    RouteLine route = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BarUtils.setStatusBarAlpha(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_main4);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mMapView = findViewById(R.id.bmapView);
        requestLocButton = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        planning_on_foot = (Button) findViewById(R.id.planning_on_foot);
        public_transport_planning = (Button) findViewById(R.id.public_transport_planning);
    }

    private void initData() {
        requestLocButton.setText("普通");

        //获取传感器管理服务
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        // 开启定位图层
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListenner());
        mLocClient.setLocOption(BaiduMapUtil.initLocation());
        mLocClient.start();

        // 搜索某个地址相关，注册事件监听
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(onGetGeoCoderResultListener);
        //路径规划相关
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(onGetRoutePlanResultListener);

    }

    private void initListener() {
        button2.setOnClickListener(this);
        planning_on_foot.setOnClickListener(this);
        public_transport_planning.setOnClickListener(this);
        requestLocButton.setOnClickListener(this);
    }

    /**
     * 传感器
     *
     * @param sensorEvent
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View v) {
        //  startNode = PlanNode.withCityNameAndPlaceName("上海", "银联数据大楼");
        //  endNode = PlanNode.withCityNameAndPlaceName("上海", "中科路2500弄");


        switch (v.getId()) {
            case R.id.button1:
                switch (mCurrentMode) {
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder1 = new MapStatus.Builder();
                        builder1.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                        break;
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        MapStatus.Builder builder = new MapStatus.Builder();
                        builder.overlook(0);
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        break;

                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.button2:
                // Geo搜索
                tag = 1;
                geoCoder.geocode(new GeoCodeOption().city(
                        "上海").address("中科路2530号"));

                break;
            case R.id.planning_on_foot:
                // 步行规划
                tag = 2;
                geoCoder.geocode(new GeoCodeOption().city(
                        "上海").address("顾唐路1699"));

                break;

            case R.id.public_transport_planning:
                // 公交规划
                tag = 3;
                geoCoder.geocode(new GeoCodeOption().city(
                        "上海").address("中科路2530号"));


                break;
            default:
                break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            LogUtils.i(new Gson().toJson(location.getAddress()), location.getLocType());

            startll = new LatLng(mCurrentLat, mCurrentLon);

            startNode = PlanNode.withLocation(startll);

            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(15.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {

        }
    }

    /**
     * 搜索注册事件监听
     */
    OnGetGeoCoderResultListener onGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {

        @Override//获取地理编码结果
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
                ToastUtils.showShort("没有检索到结果");
                return;
            }
            mBaiduMap.clear();

            LatLng endll = new LatLng(result.getLocation().latitude, result.getLocation().longitude);
            endNode = PlanNode.withLocation(endll);

            if (tag == 1) {
                mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marka)));
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                        .getLocation()));


                LogUtils.i("纬度：" + result.getLocation().latitude,
                        " 经度：" + result.getLocation().longitude);

                //签到（计算两点距离）
                double distance = DistanceUtil.getDistance(startll, endll);

                LogUtils.i(distance);
                ToastUtils.showShort(distance + "米");

            } else if (tag == 2) {

                routePlanSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(startNode)
                        .to(endNode));

            } else if (tag == 3) {

                routePlanSearch.transitSearch((new TransitRoutePlanOption())
                        .from(startNode)
                        .city("上海")
                        .to(endNode));
            }


        }

        @Override//获取反向地理编码结果
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有找到检索结果
            }

        }
    };

    /**
     * 路线规划搜索回调
     */
    OnGetRoutePlanResultListener onGetRoutePlanResultListener = new OnGetRoutePlanResultListener() {

        //获取步行线路规划结果
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {

            LogUtils.i(result.error);
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtils.showShort("抱歉，未找到结果");
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();
                JSONArray jsonArray = new JSONArray(result.getSuggestAddrInfo().getSuggestEndNode());

                LogUtils.i(result.getSuggestAddrInfo().getSuggestEndCity(),
                        jsonArray.toString(),
                        result.getSuggestAddrInfo().getSuggestStartCity(),
                        result.getSuggestAddrInfo().getSuggestStartNode(),
                        result.getSuggestAddrInfo().getSuggestWpNode());

                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                nodeIndex = -1;
                LogUtils.i("步行导航线路数:" + result.getRouteLines().size());
                if (result.getRouteLines().size() == 1) {
                    // 直接显示
                    RouteLine route = null;
                    route = result.getRouteLines().get(0);
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    OverlayManager routeOverlay = overlay;
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();
                    ToastUtils.showShort(result.getRouteLines().get(0).getDistance() + "米");

                } else {
                    Log.d("route result", "结果数<0");
                    return;
                }

            }
        }

        //公交导航路线
        @Override
        public void onGetTransitRouteResult(final TransitRouteResult result) {
            //公交导航路线
            LogUtils.i(result.error);
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
                ToastUtils.showShort("抱歉，未找到结果");
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                result.getSuggestAddrInfo();

                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                LogUtils.i("公交导航线路数:" + result.getRouteLines().size());


                if (result.getRouteLines().size() > 1) {
//                    nowResultransit = result;
                    if (!hasShownDialogue) {
                        MyTransitDlg myTransitDlg = new MyTransitDlg(BaiduMapActivity.this,
                                result.getRouteLines(),
                                RouteLineAdapter.Type.TRANSIT_ROUTE);
                        myTransitDlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                hasShownDialogue = false;
                            }
                        });
                        myTransitDlg.setOnItemInDlgClickLinster(new OnItemInDlgClickListener() {
                            @Override
                            public void onItemClick(int position) {

                                route = result.getRouteLines().get(position);

                                LogUtils.i(new Gson().toJson(route.getAllStep()));

                                for (int i = 0; i < route.getAllStep().size(); i++) {
                                }
                                TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                                mBaiduMap.setOnMarkerClickListener(overlay);
                                // routeOverlay = overlay;
                                overlay.setData(result.getRouteLines().get(position));
                                overlay.addToMap();
                                overlay.zoomToSpan();
                            }

                        });
                        myTransitDlg.show();
                        hasShownDialogue = true;
                    }
                } else if (result.getRouteLines().size() == 1) {
                    // 直接显示
                    route = result.getRouteLines().get(0);
                    TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(overlay);
                    //routeOverlay = overlay;
                    overlay.setData(result.getRouteLines().get(0));
                    overlay.addToMap();
                    overlay.zoomToSpan();

                } else {
                    Log.d("route result", "结果数<0");
                    return;
                }

            }
        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

        }

    };

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        geoCoder.destroy();
        routePlanSearch.destroy();
        mMapView = null;
        super.onDestroy();
    }


    // 响应DLg中的List item 点击
    interface OnItemInDlgClickListener {
        void onItemClick(int position);
    }

    // 供路线选择的Dialog
    class MyTransitDlg extends Dialog {

        private List<? extends RouteLine> mtransitRouteLines;
        private ListView transitRouteList;
        private RouteLineAdapter mTransitAdapter;

        OnItemInDlgClickListener onItemInDlgClickListener;

        public MyTransitDlg(Context context, int theme) {
            super(context, theme);
        }

        public MyTransitDlg(Context context, List<? extends RouteLine> transitRouteLines, RouteLineAdapter.Type
                type) {
            this(context, 0);
            mtransitRouteLines = transitRouteLines;
            mTransitAdapter = new RouteLineAdapter(context, mtransitRouteLines, type);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        @Override
        public void setOnDismissListener(OnDismissListener listener) {
            super.setOnDismissListener(listener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_transit_dialog);

            transitRouteList = (ListView) findViewById(R.id.transitList);
            transitRouteList.setAdapter(mTransitAdapter);

            transitRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemInDlgClickListener.onItemClick(position);
//                    mBtnPre.setVisibility(View.VISIBLE);
//                    mBtnNext.setVisibility(View.VISIBLE);
                    dismiss();
                    hasShownDialogue = false;
                }
            });
        }

        public void setOnItemInDlgClickLinster(OnItemInDlgClickListener itemListener) {
            onItemInDlgClickListener = itemListener;
        }

    }
}
