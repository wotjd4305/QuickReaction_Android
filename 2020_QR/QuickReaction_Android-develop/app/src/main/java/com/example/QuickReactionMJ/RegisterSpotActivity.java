package com.example.QuickReactionMJ;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.QuickReactionMJ.db.SharedPreferenceController;
import com.example.QuickReactionMJ.domain.Address;
import com.example.QuickReactionMJ.domain.Spot;
import com.example.QuickReactionMJ.domain.addresses;
import com.example.QuickReactionMJ.get.GetNaverMapApiResult;
import com.example.QuickReactionMJ.listener.GetNaverMapApiListener;
import com.example.QuickReactionMJ.network.ApplicationController;
import com.example.QuickReactionMJ.network.ApplicationNaverApiController;
import com.example.QuickReactionMJ.network.NetworkService;
import com.example.QuickReactionMJ.post.PostSpotSaveResult;
import com.example.QuickReactionMJ.rest.Rest;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class RegisterSpotActivity extends AppCompatActivity implements GetNaverMapApiListener {

    private NetworkService networkService; //NetworkService 객체 생성
    private NetworkService networkService2; //NetworkService 객체 생성

    private AlertDialog dialog;
    private boolean validate = false;
    private String checkID = "";
    private String userName = "";
    private String userPhone = "";



    //스피너
    private ArrayAdapter<String> spinnerArrayAdapter;
    private List<addresses> list;

    private Spinner spinner;
    GetNaverMapApiResult getedAddress;
    EditText ed_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_spot);


        //리스너
        Rest rest = new Rest();
        rest.setGetNaverMapApiListener(this);


        //통신
        networkService = ApplicationNaverApiController.getInstance().getNetworkService();
        Call<GetNaverMapApiResult> call = networkService.GetNaverMapApiResult("", "","강남구 언주로22" );
        Rest.AdminGetNaverApiMethod(call);

        //통신(원래)
        networkService2 = ApplicationController.getInstance().getNetworkService();

        //텍스트 가져오기




        //스피너
        spinner = (Spinner) findViewById(R.id.user_spinner) ;




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0) i++;

                EditText ed_sido = (EditText)findViewById(R.id.spotSaveCity_edittxt);
                EditText ed_sigungu = (EditText)findViewById(R.id.spotSaveSiGungu_edittxt);
                EditText ed_roadName = (EditText)findViewById(R.id.spotSaveLoadName_edittxt);
                EditText ed_buildNum = (EditText)findViewById(R.id.spotSaveBuildNum_edittxt);
                EditText ed_buildName = (EditText)findViewById(R.id.spotSaveBuildName_edittxt);

                EditText ed_lat = (EditText)findViewById(R.id.spotSaveLat_edittxt);//위도, y
                EditText ed_lng = (EditText)findViewById(R.id.spotSaveLng_edittxt);//경도, x

                EditText ed_name = (EditText)findViewById(R.id.spotSaveName_edittxt);


                Log.i("선택된 아이템", spinner.getSelectedItem().toString());
                Log.i("선택된 아이템2 ", ""+(i-1));
                Log.i("선택된 아이템3", list.get(i-1).toString());
                Log.i("선택된 아이템4", list.get(i-1).getAddressElements().get(4).getLongName());


                String temp = spinner.getSelectedItem().toString();

                //0 - sido, 1 - sigungu, ,3 - roadName, 4- buildNum, 5- buildName
                if(list != null) {
                    ed_sido.setText(list.get(i-1).getAddressElements().get(0).getLongName());
                    ed_sigungu.setText(list.get(i-1).getAddressElements().get(1).getLongName());
                    ed_roadName.setText(list.get(i-1).getAddressElements().get(4).getLongName());
                    ed_buildNum.setText(list.get(i-1).getAddressElements().get(5).getLongName());
                    ed_buildName.setText(list.get(i-1).getAddressElements().get(6).getLongName());

                    ed_lat.setText(list.get(i-1).getY().toString());
                    ed_lng.setText(list.get(i-1).getX().toString());
                }


                Log.i("선택된 아이템", list.get(i-1).getJibunAddress() + ", " + i);
                //clickAfterDo(temp);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //검색하기 버튼
        ed_search = (EditText)findViewById(R.id.spotSaveCity_search);
        final Button buttonForSearch = (Button) findViewById(R.id.spotSave_buttonForSearch);

        buttonForSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<GetNaverMapApiResult> call = networkService.GetNaverMapApiResult("woy8xtf488", "p6PkPj3YgbGgLmVzHC6ngafhGOIAXW62nmkHCNGz",ed_search.getText().toString());
                Rest.AdminGetNaverApiMethod(call);

            }
        });

        //가입하기 버튼
        final Button register = (Button) findViewById(R.id.spotSave_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText ed_sido = (EditText)findViewById(R.id.spotSaveCity_edittxt);
                EditText ed_sigungu = (EditText)findViewById(R.id.spotSaveSiGungu_edittxt);
                EditText ed_roadName = (EditText)findViewById(R.id.spotSaveLoadName_edittxt);
                EditText ed_buildNum = (EditText)findViewById(R.id.spotSaveBuildNum_edittxt);
                EditText ed_buildName = (EditText)findViewById(R.id.spotSaveBuildName_edittxt);

                EditText ed_lat = (EditText)findViewById(R.id.spotSaveLat_edittxt);//위도, y
                EditText ed_lng = (EditText)findViewById(R.id.spotSaveLng_edittxt);//경도, x

                EditText ed_name = (EditText)findViewById(R.id.spotSaveName_edittxt);





                //순서대로
                String input_sido = ed_sido.getText().toString(); // sigugun | dongmyun -> 거제시 연초면
                String input_sigungu = ed_sigungu.getText().toString(); //7층 , 유저가 적음
                String input_roadName = ed_roadName.getText().toString(); // ri | rest -> 연사리, 93
                String input_buildNum = ed_buildNum.getText().toString(); // 이노프라자, 유저가적음
                String input_buildName =  ed_buildName.getText().toString();

                String input_lat = ed_lat.getText().toString(); // point.y, 위도
                String input_lng = ed_lng.getText().toString(); // point.x, 경도
                String input_name = ed_name.getText().toString(); // 유저가 적음  -> 코다차야

                //시티, 디테일, 군구, 집코드
                Address addressDto = new Address(input_sido,input_sigungu,input_roadName ,   input_buildName + " " +input_buildNum);
                Log.i("주소 입력",addressDto.toString());

                Spot dto = new Spot(addressDto,input_lat,input_lng,input_name);
                Log.i("스팟 입력",dto.toString());


                //
                String AdminId = "";
                if(!SharedPreferenceController.INSTANCE.getAuthorization(RegisterSpotActivity.this).isEmpty()){
                    AdminId = SharedPreferenceController.INSTANCE.getAuthorizationOfId(RegisterSpotActivity.this);
                }

                //연결
                //long 부분은 Admin_id
                if(!AdminId.equals("")) {
                    Log.i("스팟 입력 아이디 : ", AdminId);
                    Call<PostSpotSaveResult> spotSaveCall = networkService2.PostSpotSaveResponse(Long.parseLong(AdminId), dto);
                    Rest.SpotSaveMethod(spotSaveCall);
                }


                //finish();
            }
        });
    }

    private void clickAfterDo(addresses dto) {

    }

    //실패일떄만 false!
    @Override
    public void onSucOrFailEvent(boolean b) {
        if(b){
            Toast.makeText(getApplicationContext(),"서버에러 입니다.",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(RegisterSpotActivity.this, ManagerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void InitializeVisitData() {

    }

    @Override
    public void onVisitInfoReceiveEvent(GetNaverMapApiResult list) {
        //성공
        Toast.makeText(getApplicationContext(), "검색 완료", Toast.LENGTH_SHORT).show();


        Log.i("RegisterSpotActivity : ", list.toString());
        InitializeVisitData();

        getedAddress = list;

        ArrayList<String> outPutStr = new ArrayList<>();

        this.list = list.getAddresses();
        for(int i =0; i<this.list.size(); i++) {
            outPutStr.add(list.getAddresses().get(i).getRoadAddress());
        }
        spinnerArrayAdapter = new spinnerAdapter(RegisterSpotActivity.this, android.R.layout.simple_list_item_1);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArrayAdapter.addAll(outPutStr);
        spinnerArrayAdapter.add("주소를 선택해주세요");

        //
        spinner = (Spinner) findViewById(R.id.user_spinner) ;
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setSelection(spinnerArrayAdapter.getCount());
    }
}