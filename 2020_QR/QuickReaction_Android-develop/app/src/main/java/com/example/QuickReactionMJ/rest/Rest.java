package com.example.QuickReactionMJ.rest;

import android.util.Log;

import com.example.QuickReactionMJ.get.GetAdminLoginResult;
import com.example.QuickReactionMJ.get.GetNaverMapApiResult;
import com.example.QuickReactionMJ.get.GetVisitInfoResult;
import com.example.QuickReactionMJ.listener.GetAdminvalidListener;
import com.example.QuickReactionMJ.listener.GetNaverMapApiListener;
import com.example.QuickReactionMJ.listener.GetVisitInfoEventListener;
import com.example.QuickReactionMJ.listener.MyEventListener;
import com.example.QuickReactionMJ.post.PostAdminJoinResult;
import com.example.QuickReactionMJ.post.PostAdminLoginResult;
import com.example.QuickReactionMJ.post.PostScanQrResult;
import com.example.QuickReactionMJ.post.PostSpotSaveResult;
import com.example.QuickReactionMJ.post.PostUserJoinResult;
import com.example.QuickReactionMJ.post.PostUserLoginResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Rest {

    /** 이벤트를 전달 받을 인터페이스 */
    private static MyEventListener mListener;
    private static GetVisitInfoEventListener vListener;
    private static GetNaverMapApiListener nListener;
    private static GetAdminvalidListener aListener;


    public void setMyEventListener(MyEventListener listener) { mListener = listener; }
    public void setGetVisitInfoEventListener(GetVisitInfoEventListener listener) { vListener = listener; }
    public void setGetNaverMapApiListener(GetNaverMapApiListener listener) { nListener = listener; }
    public void setGetAdminvalidListener(GetAdminvalidListener listener) {aListener = listener;}




    //어드민 상세조회
    public static void AdminDetailMethod(Call<GetAdminLoginResult> callParam){

        final String error_str = "AdminLogin";

        callParam.enqueue(new Callback<GetAdminLoginResult>() {
            @Override
            public void onResponse(Call<GetAdminLoginResult> call, Response<GetAdminLoginResult> response) {
                if (response.isSuccessful()) {
                    Log.i(error_str + " : suc ", response.body().getBusinessNumber());
                    aListener.onMyEvent(true);
                } else {
                    aListener.onMyEvent(false);
                    if (response.code() == 500) ;
                    else if (response.code() == 503);
                    else if (response.code() == 401);
                    //요청 실패, 응답 코드 봐야 함
                }
            }

            @Override
            public void onFailure(Call<GetAdminLoginResult> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                aListener.onMyEvent(false);
            }
        });


    }


    //어드민 회원가입
    public static void AdminJoinMethod(Call<PostAdminJoinResult> callParam){

        callParam.enqueue(new Callback<PostAdminJoinResult>() {
            @Override
            public void onResponse(Call<PostAdminJoinResult> call, Response<PostAdminJoinResult> response) {
                if (response.isSuccessful()) {
                    Log.i("AdminJoin : suc ", response.body().getSpotAdmin_id());
                } else {
                    if (response.code() == 500);
                    else if (response.code() == 503);
                    else if (response.code() == 401);
                    //요청 실패, 응답 코드 봐야 함
                }
            }

            @Override
            public void onFailure(Call<PostAdminJoinResult> call, Throwable t) {
                Log.i("AdminJoin : fail ",  t.getMessage());

            }
        });
    }

    //Admin 전체조회
    public static void AdminFindAllMethod(Call<List<GetAdminLoginResult>> callParam){

        callParam.enqueue(new Callback<List<GetAdminLoginResult>> () {
            @Override
            public void onResponse(Call<List<GetAdminLoginResult>> call, Response<List<GetAdminLoginResult>> response) {
                if (response.isSuccessful()) {
                    Log.i("AdminFindAll : suc ", "" + response.body().size());
                } else {
                    if (response.code() == 500);
                    else if (response.code() == 503);
                    else if (response.code() == 401);
                    //요청 실패, 응답 코드 봐야 함
                }
            }

            @Override
            public void onFailure(Call<List<GetAdminLoginResult>> call, Throwable t) {
                Log.i("AdminFindAll : fail ",  t.getMessage());

            }
        });
    }

    //Spot 등록
    public static void SpotSaveMethod(Call<PostSpotSaveResult> callParam){

        callParam.enqueue(new Callback<PostSpotSaveResult> () {
            @Override
            public void onResponse(Call<PostSpotSaveResult> call, Response<PostSpotSaveResult> response) {
                if (response.isSuccessful()) {
                    Log.i("SpotSave : suc 1 ", "" + response.body().getName());
                    Log.i("SpotSave : suc 2 ", "" + response.body().toString());
                    nListener.onSucOrFailEvent(true);

                } else {
                    nListener.onSucOrFailEvent(false);
                    if (response.code() == 500)  Log.i("SpotSave : fail  ", "500");
                    else if (response.code() == 503)  Log.i("SpotSave : fail  ", "503");
                    else if (response.code() == 401)  Log.i("SpotSave : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                }
            }

            @Override
            public void onFailure(Call<PostSpotSaveResult> call, Throwable t) {
                nListener.onSucOrFailEvent(false);
                Log.i("SpotSave : fail ",  t.getMessage());

            }
        });
    }


    //유저 로그인
    public static void UserLoginMethod(Call<PostUserLoginResult> callParam){

        callParam.enqueue(new Callback<PostUserLoginResult> () {

            String error_str = "UserLogin";

            @Override
            public void onResponse(Call<PostUserLoginResult> call, Response<PostUserLoginResult> response) {
                if (response.isSuccessful()) {
                    Log.i(error_str + " : suc 1 ", "" + response.body().getAccessToken());
                    Log.i(error_str + " : suc 2 ", "" + response.body().getTokenType());
                    Log.i(error_str + " : suc 3 ", "" + response.body().getId());

                    mListener.onTokenReceiveEvent(true, response.body().getAccessToken(), "USER", response.body().getId());
                    mListener.onMyEvent(true);
                 } else {
                    Log.i(error_str + " : fail 1 ", "" + response.message());

                    if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str + " : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                    mListener.onMyEvent(false);
                }
            }

            @Override
            public void onFailure(Call<PostUserLoginResult> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                mListener.onMyEvent(false);
            }
        });
    }

    //User 등록
    public static void UserJoinMethod(Call<Long> callParam){

        final String error_str = "UserJoin";

        callParam.enqueue(new Callback<Long> () {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    Log.i( error_str + " : suc 1 ", "" );

                } else {

                    if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str +" : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.i( error_str + ": fail ",  t.getMessage());

            }
        });
    }

    //점주 로그인
    public static void AdminLoginMethod(Call<PostAdminLoginResult> callParam){

        callParam.enqueue(new Callback<PostAdminLoginResult> () {

            String error_str = "AdminLogin";

            @Override
            public void onResponse(Call<PostAdminLoginResult> call, Response<PostAdminLoginResult> response) {
                if (response.isSuccessful()) {
                    Log.i(error_str + " : suc 1 ", "" + response.body().getAccessToken());
                    Log.i(error_str + " : suc 2 ", "" + response.body().getId());
                    Log.i(error_str + " : suc 3 ", "" + response.body().getTokenType());
                    mListener.onTokenReceiveEvent(true, response.body().getAccessToken(), "ADMIN",response.body().getId());
                    mListener.onMyEvent(true);
                } else {

                    if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str + " : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                    mListener.onMyEvent(false);
                }
            }

            @Override
            public void onFailure(Call<PostAdminLoginResult> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                mListener.onMyEvent(false);

            }
        });
    }

    //QR 스캔
    public static void QrScanMethod(Call<PostScanQrResult> callParam){

        callParam.enqueue(new Callback<PostScanQrResult> () {

            String error_str = "QrScan";

            @Override
            public void onResponse(Call<PostScanQrResult> call, Response<PostScanQrResult> response) {
                if (response.isSuccessful()) {
                    Log.i(error_str + " : suc 1 ", "" + response.body().getVisitInfo_Id());
                    mListener.onMyEvent(true);
                } else {

                    if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str + " : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                    mListener.onMyEvent(false);
                }
            }

            @Override
            public void onFailure(Call<PostScanQrResult> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                mListener.onMyEvent(false);

            }
        });
    }

    //방문정보 가져오기
    public static void UserGetVisitInfoMethod(Call<List<GetVisitInfoResult>> callParam){

        callParam.enqueue(new Callback<List<GetVisitInfoResult>> () {

            String error_str = "UserVisitInfo";
            List<GetVisitInfoResult> dummy = new ArrayList<>();

            @Override
            public void onResponse(Call<List<GetVisitInfoResult>> call, Response<List<GetVisitInfoResult>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() != 0) {
                        Log.i(error_str + " : suc 1 ", "" + response.body().get(0).getSpot().toString());
                    }
                    vListener.onVisitInfoReceiveEvent(response.body());
                } else {

                    if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str + " : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                    vListener.onSucOrFailEvent(false);
                }
            }

            @Override
            public void onFailure(Call<List<GetVisitInfoResult>> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                vListener.onSucOrFailEvent(false);

            }
        });
    }


    //네이버 API 가져오기
    public static void AdminGetNaverApiMethod(Call<GetNaverMapApiResult> callParam){
        Log.d("fail",callParam.request().url().toString());
        callParam.enqueue(new Callback<GetNaverMapApiResult> () {

            String error_str = "AdminNaverMapApiResult";
            List<GetVisitInfoResult> dummy = new ArrayList<>();

            @Override
            public void onResponse(Call<GetNaverMapApiResult> call, Response<GetNaverMapApiResult> response) {
                if (response.isSuccessful()) {
                    Log.i(error_str + " : suc 1 ", "" + response.body().getAddresses().get(0).getAddressElements().get(0).getLongName());
                    Log.i(error_str + " : suc 2 ", "" + response.body().toString());
                    Log.i(error_str + " : suc 3 ", "" + response.body().getAddresses().toString());

                    nListener.onVisitInfoReceiveEvent(response.body());
                } else {
                    Log.i(error_str  + " : else - fail ", response.headers().toString() + " ??? ");

                    //이쪽을바
                    if (response.code()  == 300)  Log.i(error_str + " : fail  ", "300");
                    else if (response.code() == 500)  Log.i(error_str + " : fail  ", "500");
                    else if (response.code() == 503)  Log.i(error_str + " : fail  ", "503");
                    else if (response.code() == 401)  Log.i(error_str + " : fail  ", "401");
                    //요청 실패, 응답 코드 봐야 함
                    nListener.onSucOrFailEvent(false);
                }
            }

            @Override
            public void onFailure(Call<GetNaverMapApiResult> call, Throwable t) {
                Log.i(error_str + " : fail ",  t.getMessage());
                nListener.onSucOrFailEvent(false);

            }
        });
    }




}
