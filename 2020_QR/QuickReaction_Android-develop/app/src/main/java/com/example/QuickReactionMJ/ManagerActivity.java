package com.example.QuickReactionMJ;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.QuickReactionMJ.db.SharedPreferenceController;
import com.example.QuickReactionMJ.get.GetAdminLoginResult;
import com.example.QuickReactionMJ.listener.GetAdminvalidListener;
import com.example.QuickReactionMJ.listener.MyEventListener;
import com.example.QuickReactionMJ.network.ApplicationController;
import com.example.QuickReactionMJ.network.NetworkService;
import com.example.QuickReactionMJ.rest.Rest;

import retrofit2.Call;

public class ManagerActivity extends AppCompatActivity implements GetAdminvalidListener {
    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manager_layout);

        Bundle message = getIntent().getExtras();
        if (message != null) {
            Log.i("유저 액티비티에서 받은 메시지 값", message.get("message").toString());
            Log.i("유저 액티비티에서 받은 타이틀 값", message.get("title").toString());
            Log.i("유저 액티비티에서 받은 토큰 값", message.get("token").toString());
            //token = message.get("token").toString();
            String title = message.get("title").toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
            builder.setTitle(title);
            builder.setMessage(message.get("message").toString());
            builder.setPositiveButton("확인", null);
            builder.create().show();
        }


        //리스너
        Rest rest = new Rest();
        rest.setGetAdminvalidListener(ManagerActivity.this);


        Button moveQRMaker = (Button) findViewById(R.id.makeButton);

        String adminId = SharedPreferenceController.INSTANCE.getAuthorizationOfId(getApplicationContext());
        Long adminLongId = Long.parseLong(adminId);

        networkService = ApplicationController.getInstance().getNetworkService();

        //통신
        Call<GetAdminLoginResult> call = networkService.GetAdminLoginResponse(adminLongId);
        Rest.AdminDetailMethod(call);

    }

    @Override
    public void onMyEvent(boolean b) {
        //성공
        if(b){

        }
        //실패
        else{
            Log.i("Manger onMyEvent " ,"Fail");
            Intent intent = new Intent(ManagerActivity.this, RegisterSpotActivity.class);
            startActivity(intent);
            finish();
        }
    }


}