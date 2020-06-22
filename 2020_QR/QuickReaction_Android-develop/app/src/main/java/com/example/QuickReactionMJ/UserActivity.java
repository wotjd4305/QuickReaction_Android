package com.example.QuickReactionMJ;

import android.content.ClipData;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.QuickReactionMJ.db.SharedPreferenceController;
import com.example.QuickReactionMJ.domain.User;
import com.example.QuickReactionMJ.get.GetVisitInfoResult;
import com.example.QuickReactionMJ.listener.GetVisitInfoEventListener;
import com.example.QuickReactionMJ.network.ApplicationController;
import com.example.QuickReactionMJ.network.NetworkService;
import com.example.QuickReactionMJ.post.PostScanQrResult;
import com.example.QuickReactionMJ.rest.Rest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;


import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapPolyline;
import net.daum.mf.map.api.MapView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.daum.mf.map.api.MapReverseGeoCoder;

public class UserActivity extends AppCompatActivity implements GetVisitInfoEventListener,
        MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "MainActivity";

    private MapView mMapView;
    private ArrayList<location> mapList;

    private MapPolyline polyline;

    private HashMap<Integer, location> mTagItemMap = new HashMap<>();

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    TextView spot;
    TextView time;

    IntentIntegrator integrator;
    ArrayList<VisitData> visitDataList;
    VisitDataAdapter visitDataAdapter;

    ListView listView;

    private static final String SETTINGS_PLAYER_JSON = "settings_item_json";
    private long startTime = 0;
    private Spinner spinner;
    private List<String> list;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private ArrayList<VisitData> tempList;

    private NetworkService networkService;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        networkService = ApplicationController.getInstance().getNetworkService();

        Bundle message = getIntent().getExtras();
        if (message != null) {
            Log.i("유저 액티비티에서 받은 메시지 값", message.get("message").toString());
            Log.i("유저 액티비티에서 받은 타이틀 값", message.get("title").toString());
            Log.i("유저 액티비티에서 받은 토큰 값", message.get("token").toString());
            token = message.get("token").toString();
            String title = message.get("title").toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
            builder.setTitle(title);
            builder.setMessage(message.get("message").toString());
            builder.setPositiveButton("확인", null);
            builder.create().show();
        }

        if (!SharedPreferenceController.INSTANCE.getAuthorizationOfId(UserActivity.this).equals("")) {
            Long id = Long.parseLong(SharedPreferenceController.INSTANCE.getAuthorizationOfId(UserActivity.this));
            Call<List<GetVisitInfoResult>> getVisitInfoResultCall = networkService.GetVisitInfoResult(id);
            Rest.UserGetVisitInfoMethod(getVisitInfoResultCall);
        } else {

        }

        this.InitializeVisitData();

        spinner = (Spinner) findViewById(R.id.user_spinner);

        list = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar today = Calendar.getInstance();
            list.add(sdf.format(today.getTime()));

            for (int i = 0; i < 14; i++) {
                today.add(Calendar.DAY_OF_MONTH, -1);
                list.add(sdf.format(today.getTime()));
            }
        } catch (Exception e) {
            e.getMessage();
        }

        spinnerArrayAdapter = new spinnerAdapter(UserActivity.this, android.R.layout.simple_list_item_1);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.addAll(list);
        spinnerArrayAdapter.add("날짜를 선택하세요");
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(spinnerArrayAdapter.getCount());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner.getSelectedItemId() != spinnerArrayAdapter.getCount()) {
                    Log.i("선택된 아이템", spinner.getSelectedItem().toString());
                    String temp = spinner.getSelectedItem().toString();
                    if (mapList.size() == 0) {

                    } else {
                        checkDayToSpinner(temp);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //리스너
        Rest rest = new Rest();
        rest.setGetVisitInfoEventListener(this);

        spot = (TextView) findViewById(R.id.QRresult);
        time = (TextView) findViewById(R.id.timeText);

        listView = (ListView) findViewById(R.id.visitInfo);

        //user 방문 리스트

        integrator = new IntentIntegrator(this);

        final Button QR = (Button) findViewById(R.id.QRbutton);
        QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrator.setPrompt("바코드를 사각형 안에 비춰주세요");
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.setCaptureActivity(CaptureActivity.class);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        //맵 구현
        mMapView = (MapView) findViewById(R.id.map_view2);
        mMapView.setDaumMapApiKey("55b506eec012c6de79bbd7eaf854f71f");
        mMapView.setCurrentLocationEventListener(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                long nowTime = System.currentTimeMillis();
                try {
                    Date date = new Date(nowTime);
                    SimpleDateFormat sdNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String formatDate = sdNow.format(date);

                    visitDataList.add(new VisitData(result.getContents(), formatDate));
                    visitDataAdapter.notifyDataSetChanged();

                    //서버통신
                    Long userId = Long.parseLong(SharedPreferenceController.INSTANCE.getAuthorizationOfId(UserActivity.this));
                    String spotIdArr[] = new String[2];
                    spotIdArr = (""+result.getContents()).split("=");
                    Long spotId = Long.parseLong(spotIdArr[1]);
                    Log.i("UserActivity QR후  : " , ""+spotId);

                    Call<PostScanQrResult> call = networkService.PostQrScanResult(userId, spotId);
                    Rest.QrScanMethod(call);

                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사 바꿈
                if (checkLocationServicesStatus() && mapList.size() == 0) {

                    Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                    checkRunTimePermission();
                    return;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - startTime >= 2000) {
            startTime = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료합니다!", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - startTime < 2000) {
            finish();
        }
    }

    public void InitializeVisitData() {
        visitDataList = new ArrayList<VisitData>();
    }

    public void InitializeTempData() {
        tempList = new ArrayList<VisitData>();
    }


    private void checkDayToSpinner(String selectedDay) {
        InitializeTempData();
        List<location> visitLocation = new ArrayList<>();
        for (int i = 0; i < visitDataList.size(); i++) {
            String visitTime = visitDataList.get(i).getTime().substring(0, 10);
            Log.i("비교 값 : ", visitTime);
            if (visitTime.equals(selectedDay)) {
                tempList.add(new VisitData(visitDataList.get(i).getVisit(), visitDataList.get(i).getTime()));
            }
        }
        visitDataAdapter = new VisitDataAdapter(this, tempList);
        listView.setAdapter(visitDataAdapter);
    }


    @Override
    public void onSucOrFailEvent(boolean b) {
        //실패
        if (!b) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onVisitInfoReceiveEvent(final List<GetVisitInfoResult> list) {
        mapList = new ArrayList<>();
        //성공
        Log.i("UserActivity : ", list.toString());
        InitializeVisitData();
        Log.i("리스트 사이즈", "" + visitDataList.size());
        for (int i = 0; i < list.size(); i++) {
            visitDataList.add(new VisitData(list.get(i).getSpot().getName()
                    , "" + list.get(i).getLocalDateTime()));
            double lat = Double.parseDouble(list.get(i).getSpot().getLat());
            double lng = Double.parseDouble(list.get(i).getSpot().getLng());
            mapList.add(new location(list.get(i).getSpot().getName(), list.get(i).getLocalDateTime(), lat, lng));
            if (mapList.size() != 0) {
                setPoint(mapList);
            }
        }

        visitDataAdapter = new VisitDataAdapter(this, visitDataList);
        listView.setAdapter(visitDataAdapter);

        Log.i("visitList size : ", "" + visitDataList.size());

        integrator = new IntentIntegrator(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
            mMapView.setShowCurrentLocationMarker(false);
        } catch (Exception e) {
            SharedPreferenceController.INSTANCE.clearSPC(getApplicationContext());
        }
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }


    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {
                Log.d("@@@", "start");
                //위치 값을 가져올 수 있음
                /*if(mapList.size() == 0) {
                    mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                }*/
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(UserActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(UserActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(UserActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음
            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
            // TrackingModeOnWithHeading 로 바꾸면 바라보고있는 방향에 따라 화면 움직임


        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(UserActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(UserActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(UserActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void addDrawLine(List<location> visitList) {
        Log.i("애드드로우", "Cooooooooooooooooooooooooool");
        polyline = new MapPolyline();
        polyline.setTag(1000);
        polyline.setLineColor(Color.argb(128, 255, 51, 0)); // Polyline 컬러 지정.

        mMapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(visitList.get(0).latitude, visitList.get(0).longitude), true);

        for (int i = 0; i < visitList.size(); i++) {
            double latitude = visitList.get(i).latitude;
            double longitude = visitList.get(i).longitude;
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude));
        }

// Polyline 지도에 올리기.
        mMapView.addPolyline(polyline);

// 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정.
        MapPointBounds mapPointBounds = new MapPointBounds(polyline.getMapPoints());
        int padding = 100; // px
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding));
    }

    private void setPoint(List<location> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();
        for (int i = 0; i < itemList.size(); i++) {
            location item = itemList.get(i);
            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.visit);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage); //마커 타입 지정
            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin); //마커의 이미지
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage); //마커가 선택되었을때 마커 타입 지정
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커가 선택되었을때 마커 이미지
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);
            mMapView.addPOIItem(poiItem); //지도에 마커 추가
            mTagItemMap.put(poiItem.getTag(), item);
        }
        Log.v("마커", "showResult 메소드 지나감, itemList.size() = " + itemList.size() + ", itemList = " + itemList); //디버깅용 로그 메시지
        mMapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));
        MapPOIItem[] poiItems = mMapView.getPOIItems();
        if (poiItems.length > 0) {
            mMapView.selectPOIItem(poiItems[0], false);
        }
    }
}

    class location {
        String time;
        String visit;
        double latitude;
        double longitude;

        public location(String visit, String time, double latitude, double longitude) {

            this.visit = visit;
            this.time = time;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
