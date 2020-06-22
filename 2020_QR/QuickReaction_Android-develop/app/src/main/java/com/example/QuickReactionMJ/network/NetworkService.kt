/*package com.example.user.alcohol_measurement


import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    //회원가입
@Multipart
@POST("/users/saveUser")
fun postSignUpResponse(
    @Header("Authorization") token : String,
    @Part("ID") ID : RequestBody,
    @Part("password") password : RequestBody,
    @Part("name") name : RequestBody,
    @Part("gender") gender : RequestBody,
    @Part("birth") birth : RequestBody,
    @Part("phone") phone : RequestBody,
    @Part("email") email : RequestBody,

    @Part voiceFile1 : MultipartBody.Part?,
    @Part voiceFile2 : MultipartBody.Part?,
    @Part voiceFile3 : MultipartBody.Part?,
    @Part imageFile : MultipartBody.Part?
) : Call<PostSignUpResponse>


    //로그인
    @POST("/users/login")
    fun postLoginResponse(
        @Header("Content-Type") content_type : String,
        @Body() body : JsonObject
    ) : Call<PostLogInResponse>


    //아이디 중복체크
     @GET("/users/chkID")
    fun getBoardListResponse(
        @Header("Content-Type") content_type : String,
        @Query("id") id : String
    ) : Call<GetBoardListResponse>

    //아이디 초기 체크
    @GET("/users/fileChk")
    fun getInitCheckResponse(
        @Header("Authorization") token : String
    ) : Call<GetInitCheckResponse>



    //측정하기 - 서버로 보내기
    @Multipart
    @POST("/users/save")
    fun postSendFileResponse(
        @Header("Authorization") token : String,
        @Part("email") gender : RequestBody,
        @Part("password") password : RequestBody,

        @Part imagefile : MultipartBody.Part?,
        @Part voicefile1 : MultipartBody.Part?,
        @Part voicefile2 : MultipartBody.Part?,
        @Part voicefile3 : MultipartBody.Part?

    ) : Call<PostSendFileResponse>


    //추가정보 입력
    @Multipart
    @POST("/users/saveInit")
    fun postAddInfoResponse(
        @Header("Authorization") token : String,

        @Part voiceFile1 : MultipartBody.Part?,
        @Part voiceFile2 : MultipartBody.Part?,
        @Part voiceFile3 : MultipartBody.Part?,
        @Part imageFile : MultipartBody.Part?

        ) : Call<PostAddInfoResponse>


    //본측정 입력
    @Multipart
    @POST("/users/saveAlcohol")
    fun postSaveAlcoholResponse(
        @Header("Authorization") token : String,

        @Part voiceFile1 : MultipartBody.Part?,
        @Part voiceFile2 : MultipartBody.Part?,
        @Part voiceFile3 : MultipartBody.Part?,
        @Part imageFile : MultipartBody.Part?

        ) : Call<PostSaveAlcoholResponse>


    //서버로 보내기
    @GET("/users/learnModel")
    fun postResultResponse(
        @Header("Authorization") token : String
        ) : Call<GetResultResponse>


    //전처리된 이미지 체크
    @Multipart
    @POST("/users/chkImage")
    fun postCheckImageFileResponse(
        @Header("Authorization") token : String,
        @Part preImageFile : MultipartBody.Part?

    ) : Call<PostCheckImageFileResopnse>

}*/