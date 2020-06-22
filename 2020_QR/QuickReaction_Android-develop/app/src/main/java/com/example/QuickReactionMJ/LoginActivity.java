package com.example.QuickReactionMJ;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.QuickReactionMJ.db.SharedPreferenceController;
import com.example.QuickReactionMJ.domain.SpotAdminLoginDto;
import com.example.QuickReactionMJ.domain.UserLoginDto;
import com.example.QuickReactionMJ.network.ApplicationController;
import com.example.QuickReactionMJ.listener.MyEventListener;
import com.example.QuickReactionMJ.network.NetworkService;
import com.example.QuickReactionMJ.post.PostAdminLoginResult;
import com.example.QuickReactionMJ.post.PostUserLoginResult;
import com.example.QuickReactionMJ.rest.Rest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements MyEventListener {

    private ProgressDialog loadingDialog;
    private AlertDialog dialog;
    private NetworkService networkService;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        networkService = ApplicationController.getInstance().getNetworkService();

        //리스너
        Rest rest = new Rest();
        rest.setMyEventListener(this);

        loadingDialog = new ProgressDialog(this);

        //텍스트값
        final EditText idEdit = (EditText) findViewById(R.id.id);
        final EditText passEdit = (EditText) findViewById(R.id.pass);

        Button loginAdmin = (Button) findViewById(R.id.loginAdmin);
        Button loginUser = (Button) findViewById(R.id.loginUser);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceID failed", task.getException());
                            return;
                        }
                        // 현재 토큰 검색!
                        token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토큰:" + token);
                    }
                });


        //SharedPreferenceController.INSTANCE.clearSPC(LoginActivity.this);
        Log.i("Login : Token exist1",""+SharedPreferenceController.INSTANCE.getAuthorization(LoginActivity.this));

        if(!SharedPreferenceController.INSTANCE.getAuthorization(LoginActivity.this).isEmpty()){

            loadingDialog.setMessage("Loading...");
            loadingDialog.setIndeterminate(true);
            loadingDialog.setCancelable(true);
            loadingDialog.show();

            //점주 토큰
            if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(LoginActivity.this).equals("ADMIN")) {
                Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                loadingDialog.cancel();
                startActivity(intent);
            }
            //유저 토큰
            if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(LoginActivity.this).equals("USER")) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                loadingDialog.cancel();
                startActivity(intent);
            }

            Log.i("Login : Token exist2",""+SharedPreferenceController.INSTANCE.getAuthorization(LoginActivity.this));
        }



        //유저 로그인 버튼
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "로그인 클릭", Toast.LENGTH_SHORT).show();

                loadingDialog.setMessage("Loading...");
                loadingDialog.setIndeterminate(true);
                loadingDialog.setCancelable(true);
                loadingDialog.show();

                String email =idEdit.getText().toString();
                String password = passEdit.getText().toString();

                UserLoginDto dto = new UserLoginDto(token, email, password);

                Call<PostUserLoginResult> userLoginCall = networkService.PostUserLoginResponse(dto);
                Rest.UserLoginMethod(userLoginCall);

            }
        });

        //점주 로그인 버튼
        loginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingDialog.setMessage("Loading...");
                loadingDialog.setIndeterminate(true);
                loadingDialog.setCancelable(true);
                loadingDialog.show();

                String email = idEdit.getText().toString();
                String password = passEdit.getText().toString();

                SpotAdminLoginDto dto = new SpotAdminLoginDto(token, email, password);

                Call<PostAdminLoginResult> call = networkService.PostAdminLoginResponse(dto);
                Rest.AdminLoginMethod(call);
            }
        });


        final CheckBox loginCheck = (CheckBox) findViewById(R.id.loginCheck);
        loginCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("자동 로그인으로 설정하시겠습니까?");
                builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginCheck.setChecked(false);
                    }
                });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        loginCheck.setChecked(true);
                    }
                });
                builder.create().show();
            }
        });

        //동의창
        final Button loginPractice = (Button) findViewById(R.id.go_register);
        loginPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AgreeActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public void onMyEvent(boolean res) {
        //Toast.makeText(this, "리스너! " + test , Toast.LENGTH_LONG).show();

        loadingDialog.cancel();

        if(res){
            Log.i("test", " hi - s");

            //점주 토큰
            if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(LoginActivity.this).equals("ADMIN")) {
                Intent intent = new Intent(LoginActivity.this, ManagerActivity.class);
                startActivity(intent);
            }
            //유저 토큰
            if(SharedPreferenceController.INSTANCE.getAuthorizationOfRole(LoginActivity.this).equals("USER")) {
                Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                startActivity(intent);
            }

        }
        if(!res){
            Log.i("test", " hi - f");
            Toast.makeText(this, "로그인 실패" , Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onTokenReceiveEvent(boolean b, String token, String role, Long id) {
        SharedPreferenceController.setAuthorization(LoginActivity.this, token, role,id);
        Log.i("onTokenReceiveEvent val", ":" + SharedPreferenceController.INSTANCE.getAuthorization(LoginActivity.this));
        Log.i("onTokenReceiveEvent", ":" + token);
    }
}