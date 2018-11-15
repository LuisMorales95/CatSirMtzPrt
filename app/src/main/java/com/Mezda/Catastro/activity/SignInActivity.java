package com.Mezda.Catastro.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.Mezda.Catastro.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Mezda.Catastro.Const;
import com.Mezda.Catastro.LinksURL;
import com.Mezda.Catastro.MyBaseL;
import com.Mezda.Catastro.VolleySingleton;
import com.Mezda.Catastro.util.FloatLabel;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.Mezda.Catastro.util.UtilMethods.getPreferenceString;
import static com.Mezda.Catastro.util.UtilMethods.hideSoftKeyboard;
import static com.Mezda.Catastro.util.UtilMethods.isConnectedToInternet;
import static com.Mezda.Catastro.util.UtilMethods.savePreference;
import static com.Mezda.Catastro.util.Validator.isInputted;
import static com.Mezda.Catastro.util.Validator.isPasswordValid;

/**
 * @author Audacity IT Solutions Ltd.
 * @class SignInActivity
 * @brief Responsible for making user logged in
 */

public class SignInActivity extends Activity implements View.OnClickListener, View.OnTouchListener, InternetConnectionListener {

    private static SignInCompleteListener signInCompleteListener;
    private final int SIGNED_IN_ACTION = 1;
    private FloatLabel SignInUserName;
    private FloatLabel SignInPassword;
    private boolean isUserCanceled = false;
    static MyBaseL bd;
    private InternetConnectionListener internetConnectionListener;
    //public static String URL = "http://192.168.1.70/Catastro/verify.php";
//    public static String URL = "http://201.161.94.229:7777/"+Const.Munish+"verify.php";
    private String tag_json_obj = "jobj_req";
    private ImageView SignIn_Mun_logo;

    public static void setListener(Context context) {
        signInCompleteListener = (SignInCompleteListener) context;
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (getPreferenceString(this,Const.SP_Links).isEmpty()){
            savePreference(this,Const.SP_Links,"0");
        }
        Spinner spinner_ciudades = (Spinner) findViewById(R.id.spinner_ciudades);
        spinner_ciudades.setAdapter(new ArrayAdapter(SignInActivity.this,android.R.layout.simple_list_item_1,Const.Ciudades()));
        int position = Integer.valueOf(getPreferenceString(this,Const.SP_Links));
        spinner_ciudades.setSelection(position);
        spinner_ciudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    savePreference(SignInActivity.this,Const.SP_Links,"0");
                    Glide.with(SignInActivity.this).load(R.drawable.aytomtzt).into(((ImageView)findViewById(R.id.SignIn_Mun_logo)));
    
                }else if (position==1){
                    savePreference(SignInActivity.this,Const.SP_Links,"1");
                    Glide.with(SignInActivity.this).load(R.drawable.docto_77).into(((ImageView)findViewById(R.id.SignIn_Mun_logo)));
    
                }
            }
    
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnNewUserTV).setOnClickListener(this);
        findViewById(R.id.showPasswordImg).setOnTouchListener(this);
        findViewById(R.id.btnForgotPasswordTV).setOnClickListener(this);
        SignInUserName = (FloatLabel) findViewById(R.id.SignInUserName);
        SignInUserName.getEditText().setTextSize(40);
        SignInUserName.getLabel().setTextSize(26);
        SignInUserName.getEditText().setTextColor(Color.BLACK);
    
        SignInPassword = (FloatLabel) findViewById(R.id.SignInPassword);
        SignInPassword.getEditText().setTextSize(40);
        SignInPassword.getLabel().setTextSize(26);
        SignInPassword.getEditText().setTextColor(Color.BLACK);
        SignInPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        if (getPreferenceString(this,Const.SP_Links).equals("0")){
            Glide.with(this).load(R.drawable.aytomtzt).into(((ImageView)findViewById(R.id.SignIn_Mun_logo)));
//            ((ImageView)findViewById(R.id.SignIn_Mun_logo)).setImageResource(R.drawable.aytomtzt);
        }else if (getPreferenceString(this,Const.SP_Links).equals("1")){
            Glide.with(this).load(R.drawable.docto_77).into(((ImageView)findViewById(R.id.SignIn_Mun_logo)));
//            ((ImageView)findViewById(R.id.SignIn_Mun_logo)).setImageResource(R.drawable.docto_77);
        }

        bd = new MyBaseL(this);
        if(!getPreferenceString(SignInActivity.this,Const.SP_token).equals("")){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isUserCanceled) {
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignIn:
                if (isInputValid()) {
                    if (isConnectedToInternet(SignInActivity.this)) {
                        doLoginRequest(SignInUserName.getEditText().getText().toString(),SignInPassword.getEditText().getText().toString());
                        //startActivity(new Intent(SignInActivity.this, HomeActivity.class));

                    } else {

                        internetConnectionListener = SignInActivity.this;
                        UtilMethods.showNoInternetDialog(SignInActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), SIGNED_IN_ACTION);
                    }

                }
                break;

            case R.id.btnNewUserTV:
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                isUserCanceled = true;
                onPause();
                break;

            case R.id.crossImgView:
                hideSoftKeyboard(this);
                isUserCanceled = true;
                onPause();
                break;

            case R.id.btnForgotPasswordTV:
                startActivity(new Intent(SignInActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    private void doLoginRequest(String username, String password) {
        
        //signInCompleteListener.onSignInComplete();
       /* if(username.equals("NessWayne") && password.equals("123456")){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }

        if(username.equals("Arthur") && password.equals("123456")){
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        }*/
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            makeJsonObjReq(username,  password);
        }else{
            Toast.makeText(getApplicationContext(), "BD Interna!", Toast.LENGTH_LONG).show();

            if(bd.Loginz(username,  password, SignInActivity.this)){
                startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Usuario/Contraseña Incorrectos!",
                        Toast.LENGTH_LONG).show();
            }
        }



        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ;
            }
        });*/
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!TextUtils.isEmpty(SignInPassword.getEditText().getText())) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    SignInPassword.getEditText().setTransformationMethod(null);
                    SignInPassword.getEditText().setSelection(SignInPassword.getEditText().getText().length());
                    break;

                case MotionEvent.ACTION_UP:
                    SignInPassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                    SignInPassword.getEditText().setSelection(SignInPassword.getEditText().getText().length());
                    break;
            }
        }

        return false;
    }

    private boolean isInputValid() {

        if (!isInputted(this, SignInUserName)) {
            return false;
        }

        /*if (!isMobileNumberValid(this, SignInUserName)) {
            return false;
        }*/

        if (!isInputted(this, SignInPassword)) {
            return false;
        }

        if (!isPasswordValid(this, SignInPassword)) {
            return false;
        }

        return true;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == SIGNED_IN_ACTION) {
            doLoginRequest(SignInUserName.getEditText().getText().toString(),
                    SignInPassword.getEditText().getText().toString());
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }

    public interface SignInCompleteListener {
        void onSignInComplete();
    }

    private void makeJsonObjReq(String user, final String password) {
        String URL="";
        if (getPreferenceString(this,Const.SP_Links).equals("0")){
            URL= LinksURL.URLVerify;
        }else if (getPreferenceString(this,Const.SP_Links).equals("1")){
            URL= LinksURL.URLVerify1;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("user", user);
        map.put("pass", password);

        JSONObject object = new JSONObject(map);

        Log.d("TG", object.toString());
        JsonObjectRequest Req = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        Toast.makeText(getApplicationContext(),"Response: "+response.toString(),).show();
                        Const.bitacora+=response.toString()+"\n";
                        try {


                            if(!response.get("idp").equals("0")){
//                                User user = new User();
//                                user.setId("1"); //dummy
//                                user.setUsername(username);
//                                user.setName("User"); // dummy value
//                                user.setEmail("user@email.com");
                                
                                 Const.IdPerson = response.get("idp").toString();
                                 Const.User = response.get("user").toString();
                                 Const.Token = response.get("token").toString();
                                 Const.IdBrig = response.get("idbrig").toString();
                                 
                                savePreference(SignInActivity.this, Const.SP_idpersona, Const.IdPerson);
                                savePreference(SignInActivity.this, Const.SP_user, Const.User);
                                savePreference(SignInActivity.this, Const.SP_token, Const.Token);
                                savePreference(SignInActivity.this, Const.SP_idbrigada, Const.IdBrig);
    
//                                if(bd.UserExist(Const.IdPerson)){
//                                     bd.UpdateLg(Const.IdPerson,Const.Token);
//                                }else{
//                                     bd.InsertLogin(Const.User,password,Const.IdBrig,Const.IdPerson,Const.Token);
//                                }
                                
//                                Toast.makeText(SignInActivity.this,"TOKEN"+Const.Token,Toast.LENGTH_SHORT).show();

                                 startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                                 finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Usuario/Contraseña Incorrectos!",
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Volley: " + error.getMessage());
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }

        };

        VolleySingleton.getInstance().addToRequestQueue(Req,tag_json_obj);


    }
}
