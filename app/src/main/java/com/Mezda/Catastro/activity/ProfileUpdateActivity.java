package com.Mezda.Catastro.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.model.User;
import com.Mezda.Catastro.util.FloatLabel;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;

import static com.Mezda.Catastro.util.Constants.SP_CONTACT_NUMBER;
import static com.Mezda.Catastro.util.Constants.SP_EMAIL;
import static com.Mezda.Catastro.util.Constants.SP_ID;
import static com.Mezda.Catastro.util.Constants.SP_NAME;
import static com.Mezda.Catastro.util.UtilMethods.getPreferenceString;
import static com.Mezda.Catastro.util.UtilMethods.hideSoftKeyboard;
import static com.Mezda.Catastro.util.UtilMethods.isConnectedToInternet;
import static com.Mezda.Catastro.util.UtilMethods.isUserSignedIn;
import static com.Mezda.Catastro.util.UtilMethods.savePreference;
import static com.Mezda.Catastro.util.Validator.isInputted;
import static com.Mezda.Catastro.util.Validator.isMobileNumberValid;
import static com.Mezda.Catastro.util.Validator.isValidEmail;
import static com.Mezda.Catastro.util.Validator.setPhoneCodeListener;

/**
 * @author Audacity IT Solutions Ltd.
 * @class ProfileUpdateActivity
 * @brief Activity of updating user information
 */
public class ProfileUpdateActivity extends Activity implements View.OnClickListener, InternetConnectionListener {

    private final int PROFILE_UPDATE_ACTION = 1;
    private FloatLabel etMobileNumber;
    private FloatLabel etFullName;
    private FloatLabel etEmail;
    private boolean isUserCanceled = false;
    private InternetConnectionListener internetConnectionListener;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        etMobileNumber = (FloatLabel) findViewById(R.id.SignInUserName);
        etFullName = (FloatLabel) findViewById(R.id.etFullName);
        etEmail = (FloatLabel) findViewById(R.id.etEmail);
        findViewById(R.id.crossImgView).setOnClickListener(this);
        findViewById(R.id.btnUpdate).setOnClickListener(this);
        etMobileNumber.getEditText().setOnFocusChangeListener(setPhoneCodeListener(this));

        if (isUserSignedIn(this)) {
            getUserInfo();
            etFullName.getEditText().setText(user.getName());
            etMobileNumber.getEditText().setText(user.getPhonenumber());
            etMobileNumber.getEditText().setClickable(false);
            etMobileNumber.getEditText().setFocusable(false);
            etMobileNumber.getEditText().setFocusableInTouchMode(false);
            etMobileNumber.getEditText().setCursorVisible(false);
            etEmail.getEditText().setText(user.getEmail());
        }
    }

    private void getUserInfo() {
        user = new User();
        user.setId(getPreferenceString(this, SP_ID));
        user.setPhonenumber(getPreferenceString(this, SP_CONTACT_NUMBER));
        user.setName(getPreferenceString(this, SP_NAME));
        user.setEmail(getPreferenceString(this, SP_EMAIL));
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
            case R.id.crossImgView:
                hideSoftKeyboard(this);
                isUserCanceled = true;
                onPause();
                break;

            case R.id.btnUpdate:
                if (inputValid()) {
                    if (isConnectedToInternet(ProfileUpdateActivity.this)) {
                        updateProfile(etFullName.getEditText().getText().toString(),
                                etMobileNumber.getEditText().getText().toString(),
                                etEmail.getEditText().getText().toString());
                    } else {
                        internetConnectionListener = ProfileUpdateActivity.this;
                        UtilMethods.showNoInternetDialog(ProfileUpdateActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), PROFILE_UPDATE_ACTION);
                    }

                }
                break;
        }
    }

    private void updateProfile(String name, String mobileNumber, String email) {
        User user = new User();
        user.setPhonenumber(mobileNumber);
        user.setName(name);
        user.setEmail(email);
        savePreference(ProfileUpdateActivity.this, SP_ID, user.getId());
        savePreference(ProfileUpdateActivity.this, SP_CONTACT_NUMBER, user.getPhonenumber());
        savePreference(ProfileUpdateActivity.this, SP_NAME, user.getName());
        savePreference(ProfileUpdateActivity.this, SP_EMAIL, user.getEmail());
        Toast.makeText(ProfileUpdateActivity.this, getResources().getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
        isUserCanceled = true;
        onPause();

    }

    private boolean inputValid() {

        if (!isInputted(this, etFullName)) {
            return false;
        }

        if (!isInputted(this, etMobileNumber)) {
            return false;
        }

        if (!isMobileNumberValid(this, etMobileNumber)) {
            return false;
        }

        if (!isInputted(this, etEmail)) {
            return false;
        }

        if (!isValidEmail(this, etEmail)) {
            return false;
        }

        return true;
    }


    @Override
    public void onConnectionEstablished(int code) {
        if (code == PROFILE_UPDATE_ACTION) {
            updateProfile(etFullName.getEditText().getText().toString(),
                    etMobileNumber.getEditText().getText().toString(),
                    etEmail.getEditText().getText().toString());
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }
}
