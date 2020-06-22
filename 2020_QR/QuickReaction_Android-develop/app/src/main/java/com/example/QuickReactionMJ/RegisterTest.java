package com.example.QuickReactionMJ;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.QuickReactionMJ.domain.SpotAdminJoinDto;
import com.example.QuickReactionMJ.domain.User;
import com.example.QuickReactionMJ.network.ApplicationController;
import com.example.QuickReactionMJ.network.NetworkService;
import com.example.QuickReactionMJ.post.PostAdminJoinResult;
import com.example.QuickReactionMJ.post.PostUserJoinResult;
import com.example.QuickReactionMJ.rest.Rest;

import retrofit2.Call;

public class RegisterTest extends AppCompatActivity {

    private AlertDialog dialog;
    private boolean validate = false;
    private boolean validateEmail = false;
    private boolean validateBN = false;


    private String checkID = "";

    //점주? 유저 체크
    boolean checked = false;

    //업주 비즈니스 중복체크 체크박스
    static CheckBox validateBNCB;



    private NetworkService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_practice);

        networkService = ApplicationController.getInstance().getNetworkService();

        final EditText userNameText = (EditText) findViewById(R.id.registerName);
        final EditText userContactText = (EditText) findViewById(R.id.registerContact);
        final EditText userEmailText = (EditText) findViewById(R.id.registerEmail);
        final EditText userPasswordText = (EditText) findViewById(R.id.registerPassWord);
        final EditText AdminBusinessNumber = (EditText) findViewById(R.id.registerBN);


        final Button register = (Button) findViewById(R.id.registerButton1);


        CheckBox validateEmailCB = (CheckBox) findViewById(R.id.validateCheck1);
        //이메레크
        validateEmailCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail) {
                    return;
                }
                if (checkID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTest.this);
                    dialog = builder.setMessage("이메일은 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
            }
        });

        validateBNCB = (CheckBox) findViewById(R.id.validateCheck3);
        //사업자번호체크
        validateBNCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateBN) {
                    return;
                }
                if (checkID.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTest.this);
                    dialog = builder.setMessage("사업자번호는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userContact = userContactText.getText().toString();
                String userEmail = userEmailText.getText().toString();
                String userName = userNameText.getText().toString();
                String userPassword = userPasswordText.getText().toString();
                String AdminBusinessNumber = userPasswordText.getText().toString();

                //업주
                if(checked) {
                    SpotAdminJoinDto dto = new SpotAdminJoinDto(AdminBusinessNumber,userContact, userEmail, userName, userPassword);
                    Log.i("업주 회원가입", dto.toString());
                    Call<PostAdminJoinResult> call = networkService.PostAdminJoinResponse(dto);
                    Rest.AdminJoinMethod(call);
                }else{
                    User dto = new User(userContact, userEmail, userName, userPassword);
                    Log.i("회원 회원가입", dto.toString());
                    Call<Long> call = networkService.PostUserJoinResult(dto);
                    Rest.UserJoinMethod(call);
                }

                Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void onCheckboxClicked(View v){

        LinearLayout layout_bn = findViewById(R.id.lay_bn);

        CheckBox checkBox = (CheckBox) v;

        if(checkBox.isChecked()){
            checked = true;
            Toast.makeText(getApplicationContext(), "업주선택", Toast.LENGTH_LONG).show();
            validateBNCB.setVisibility(View.VISIBLE);
            layout_bn.setVisibility(View.VISIBLE);


        }else{
            checked = false;
            Toast.makeText(getApplicationContext(), " 업주 미선택", Toast.LENGTH_SHORT).show();
            validateBNCB.setVisibility(View.GONE);
            layout_bn.setVisibility(View.GONE);
        }

    }

}

