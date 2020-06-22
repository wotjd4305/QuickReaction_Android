package com.example.QuickReactionMJ.network;


import com.example.QuickReactionMJ.domain.Spot;
import com.example.QuickReactionMJ.domain.SpotAdminJoinDto;
import com.example.QuickReactionMJ.domain.SpotAdminLoginDto;
import com.example.QuickReactionMJ.domain.User;
import com.example.QuickReactionMJ.domain.UserLoginDto;
import com.example.QuickReactionMJ.get.GetAdminLoginResult;
import com.example.QuickReactionMJ.get.GetNaverMapApiResult;
import com.example.QuickReactionMJ.get.GetVisitInfoResult;
import com.example.QuickReactionMJ.post.PostAdminJoinResult;
import com.example.QuickReactionMJ.post.PostAdminLoginResult;
import com.example.QuickReactionMJ.post.PostScanQrResult;
import com.example.QuickReactionMJ.post.PostSpotSaveResult;
import com.example.QuickReactionMJ.post.PostUserJoinResult;
import com.example.QuickReactionMJ.post.PostUserLoginResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {
/*

    @HTTP(method = "GET", path = "/cfcqr/api/spotAdmins/", hasBody = true)
    Call<GetLoginResult> GetAdminLoginResponse(
            @Header("Content-Type") String content_type,
            @Body JsonObject body
    );*/

    //점주 로그인
    @POST("/cfcqr/api/login/spotAdmin")
    @Headers({"Name:Content-Type"})
    Call<PostAdminLoginResult> PostAdminLoginResponse(
            @Body SpotAdminLoginDto spotAdminLoginDto
    );

    //유저 로그인
    @POST("/cfcqr/api/login/user")
    @Headers({"Name:Content-Type"})
    Call<PostUserLoginResult> PostUserLoginResponse(
            @Body UserLoginDto userLoginDto
    );

    //점주 상세 조회.
    @GET("/cfcqr/api/spotAdmins/{spotAdminId}")
    @Headers({"Name:Content-Type"})
    Call<GetAdminLoginResult> GetAdminLoginResponse(
            @Path("spotAdminId") Long spotAdminId
    );

    //점주 전체조회
    @GET("/cfcqr/api/spotAdmins/")
    @Headers({"Name:Content-Type"})
    Call<List<GetAdminLoginResult>> GetAdminFindAllResponse(
    );

    //점주 회원가입
    @POST("/cfcqr/api/spotAdmins/")
    @Headers({"Name:Content-Type"})
    Call<PostAdminJoinResult> PostAdminJoinResponse(
            @Body SpotAdminJoinDto spotAdminJoinDto
            );

    //스팟 등록
    @POST("/cfcqr/api/spots/{spotAdminId}")
    @Headers({"Name:Content-Type"})
    Call<PostSpotSaveResult> PostSpotSaveResponse(
            @Path("spotAdminId") Long spotAdminId,
            @Body Spot spot
    );



    //유저 회원가입
    @POST("/cfcqr/api/users/")
    @Headers({"Name:Content-Type"})
    Call<Long> PostUserJoinResult(
            @Body User user
    );



    //QR 스캔
    @POST("/cfcqr/api/visitInfos/{userId}/{spotId}")
    @Headers({"Name:Content-Type"})
    Call<PostScanQrResult> PostQrScanResult(
            @Path("userId") Long userid,
            @Path("spotId") Long spotid
    );

    //방문 정보 가져오기
    @GET("/cfcqr/api/visitInfos/{userId}")
    @Headers({"Name:Content-Type"})
    Call<List<GetVisitInfoResult>> GetVisitInfoResult(
            @Path("userId") Long userid
    );

    //방문 정보 가져오기
    @GET("/map-geocode/v2/geocode")
    Call<GetNaverMapApiResult> GetNaverMapApiResult(
            @Header("X-NCP-APIGW-API-KEY-ID") String Id,
            @Header("X-NCP-APIGW-API-KEY") String pw,
            @Query("query") String query
    );


}
