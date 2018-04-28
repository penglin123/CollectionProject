package com.example.library.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.blankj.utilcode.util.ToastUtils;

public class LngAndLatUtil {

    public static void searchRoute(BNRoutePlanNode sNode, BNRoutePlanNode eNode) {

        BNRoutePlanNode bp1 = sNode;
        BNRoutePlanNode bp2 = eNode;

        //百度的搜索路线的类
        RoutePlanSearch search = RoutePlanSearch.newInstance();
        DrivingRoutePlanOption drivingRoutePlanOption = new DrivingRoutePlanOption();
        //起始坐标和终点坐标
        PlanNode startPlanNode = PlanNode.withLocation(new LatLng(bp1.getLatitude(), bp1.getLongitude()));
        PlanNode endPlanNode = PlanNode.withLocation(new LatLng(bp2.getLatitude(), bp2.getLongitude()));
        drivingRoutePlanOption.from(startPlanNode);
        drivingRoutePlanOption.to(endPlanNode);
        search.drivingSearch(drivingRoutePlanOption);

        //搜索完成的回调
        search.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override //步行路线
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                if (walkingRouteResult.getRouteLines() == null) {
                    ToastUtils.showShort("算路失败");
                    return;
                }
                int duration = walkingRouteResult.getRouteLines().get(0).getDuration();
                ToastUtils.showShort("walkingRouteResult",duration + "米");

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                int duration = transitRouteResult.getRouteLines().get(0).getDuration();
                ToastUtils.showShort("transitRouteResult",duration + "米");
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
              //  int duration = MassTransitRouteResult.getRouteLines().get(0).getDuration();
                ToastUtils.showShort("米1111111");
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {     //驾车路线
                if (drivingRouteResult.getRouteLines() == null) {
                    ToastUtils.showShort("算路失败");
                    return;
                }
                int duration = drivingRouteResult.getRouteLines().get(0).getDistance();
                ToastUtils.showShort("drivingRouteResult","距离是:" + duration + "米");
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
                int duration = indoorRouteResult.getRouteLines().get(0).getDuration();
                ToastUtils.showShort("indoorRouteResult",duration + "米");
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                int duration = bikingRouteResult.getRouteLines().get(0).getDuration();
                ToastUtils.showShort("bikingRouteResult",duration + "米");
            }
        });
    }
}
