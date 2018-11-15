package com.Mezda.Catastro.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.MyBase;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.model.DataSpinn;
import com.Mezda.Catastro.model.EstadoCons;
import com.Mezda.Catastro.model.TerminoCons;
import com.Mezda.Catastro.model.TipoCons;
import com.Mezda.Catastro.util.FloatLabel;
import com.Mezda.Catastro.util.UtilMethods;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.List;

import static com.Mezda.Catastro.util.Validator.isInputted;
import static com.Mezda.Catastro.util.Validator.isMobileNumberValid;
import static com.Mezda.Catastro.util.Validator.isPasswordMatched;
import static com.Mezda.Catastro.util.Validator.isPasswordValid;
import static com.Mezda.Catastro.util.Validator.isValidEmail;

/**
 * @author Audacity IT Solutions Ltd.
 * @class SignUpActivity
 * @brief Responsible for creating new user
 */

public class CalculoActivity extends AppCompatActivity implements
        View.OnTouchListener, UtilMethods.InternetConnectionListener ,  LabelledSpinner.OnItemChosenListener{

    private final int SIGNED_UP_ACTION = 1;
    private FloatLabel area;
    private FloatLabel ant;
    private FloatLabel valor;
    private FloatLabel letra;
    private TextView bt;
    private FloatLabel etRetypePassword;
    private RadioGroup sexGroup;
    private boolean isUserCanceled = false;
    private UtilMethods.InternetConnectionListener internetConnectionListener;
    private Spinner spinner1, spinner2, spinner3;
    private LabelledSpinner lsp1, lsp2, lsp3;
    static MyBase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo);

        area = (FloatLabel) findViewById(R.id.etArea);
        area.getEditText().setTextSize(32);
        area.getLabel().setTextSize(22);
        area.getEditText().setTextColor(Color.BLACK);

        ant = (FloatLabel) findViewById(R.id.etAnt);
        ant.getEditText().setTextSize(32);
        ant.getLabel().setTextSize(22);
        ant.getEditText().setTextColor(Color.BLACK);

        valor = (FloatLabel) findViewById(R.id.etValor);
        valor.getEditText().setTextSize(32);
        valor.getLabel().setTextSize(22);
        valor.getEditText().setTextColor(Color.BLACK);

        letra = (FloatLabel) findViewById(R.id.etLetra);
        letra.getEditText().setTextSize(32);
        letra.getLabel().setTextSize(22);
        letra.getEditText().setTextColor(Color.BLACK);

        bt = (TextView) findViewById(R.id.btnSignUp);



        addItemsOnSpinner1();
        addItemsOnSpinner2();
        addItemsOnSpinner3();

        bd = new MyBase(this);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* EstadoCons e1 = (EstadoCons)lsp1.getSpinner().getSelectedItem();
                TipoCons ti1 = (TipoCons)lsp2.getSpinner().getSelectedItem();
                TerminoCons te1 = (TerminoCons)lsp3.getSpinner().getSelectedItem();

                bd.InsertLevCons(DetailViewFragment.IdPredio,
                                 ti1.getId(),
                                 e1.getId(),
                                 te1.getId(),
                                 area.getEditText().getText().toString(),
                                 ant.getEditText().getText().toString(),
                                 valor.getEditText().getText().toString(),
                                 letra.getEditText().getText().toString());

                        area.getEditText().setText("");
                        ant.getEditText().setText("");
                        valor.getEditText().setText("");
                        letra.getEditText().setText("");

                Toast.makeText(getApplication(), "Informacion Guardada!", Toast.LENGTH_LONG).show();*/
            }
        });

       /*
        area = (FloatLabel) findViewById(R.id.area);
        ant = (FloatLabel) findViewById(R.id.ant);
        ant.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        valor = (FloatLabel) findViewById(R.id.etFullEmail);
        letra = (FloatLabel) findViewById(R.id.letra);
        etRetypePassword = (FloatLabel) findViewById(R.id.etRetypePassword);
        letra.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
        sexGroup = (RadioGroup) findViewById(R.id.radioGroup);
        area.getEditText().setOnFocusChangeListener(setPhoneCodeListener(this));
        area.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (area.getEditText().getText().length() <=
                            getResources().getText(R.string.mobile_country_code).length()) {
                        return true;
                    }
                }
                return false;
            }
        });*/

      /*    letra.getEditText().setTextSize(40);
        letra.getLabel().setTextSize(26);
        letra.getEditText().setTextColor(Color.BLACK);*/
        /*
         if (isConnectedToInternet(RegisterActivity.this)) {
                        //TODO: network call
                        initRequest();
                    } else {
                        internetConnectionListener = RegisterActivity.this;
                        UtilMethods.showNoInternetDialog(RegisterActivity.this, internetConnectionListener, getResources().getString(R.string.no_internet),
                                getResources().getString(R.string.no_internet_text),
                                getResources().getString(R.string.retry_string),
                                getResources().getString(R.string.cancel_string), SIGNED_UP_ACTION);
                    }
        * */
    }

    public void addItemsOnSpinner1() {
        lsp1 = (LabelledSpinner) findViewById(R.id.spestconst);
        List<EstadoCons> list = DataSpinn.EstadoCons();
        //ArrayAdapter<EstadoCons> dataAdapter = new ArrayAdapter<EstadoCons>(this, android.R.layout.simple_spinner_item,list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lsp1.setItemsArray(list);


    }

    public void addItemsOnSpinner2() {
        lsp2 = (LabelledSpinner) findViewById(R.id.sptipoconst);
        List<TipoCons> list = DataSpinn.TipoCons();
        //ArrayAdapter<TipoCons> dataAdapter = new ArrayAdapter<TipoCons>(this, android.R.layout.simple_spinner_item,list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lsp2.setItemsArray(list);


    }

    public void addItemsOnSpinner3() {
        lsp3 = (LabelledSpinner) findViewById(R.id.sptermconst);
        List<TerminoCons> list = DataSpinn.TerminoCons();
        //ArrayAdapter<TerminoCons> dataAdapter = new ArrayAdapter<TerminoCons>(this, android.R.layout.simple_spinner_item,list);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lsp3.setItemsArray(list);


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
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.spestconst:
                Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.sptipoconst:
                Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                break;
            case R.id.sptermconst:
                Toast.makeText(this, "Selected: " + selectedText, Toast.LENGTH_SHORT).show();
                break;
            // If you have multiple LabelledSpinners, you can add more cases here
        }
    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {
        // Do something here
    }

    private void initRequest() {

    }

    private boolean isInputValid() {

        if (!isInputted(this, area)) {
            return false;
        }

        if (!isMobileNumberValid(this, area)) {
            return false;
        }

        if (!isInputted(this, ant)) {
            return false;
        }

        if (!isInputted(this, valor)) {
            return false;
        }

        if (!isValidEmail(this, valor)) {
            return false;
        }

        if (!isInputted(this, letra)) {
            return false;
        }

        if (!isPasswordValid(this, letra)) {
            return false;
        }

        if (!isPasswordMatched(this, letra, etRetypePassword)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.showPasswordImg) {
            if (!TextUtils.isEmpty(letra.getEditText().getText())) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    letra.getEditText().setTransformationMethod(null);
                    letra.getEditText().setSelection(letra.getEditText().getText().length());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    letra.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                    letra.getEditText().setSelection(letra.getEditText().getText().length());
                }
            }

        } else {
            if (!TextUtils.isEmpty(etRetypePassword.getEditText().getText())) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    etRetypePassword.getEditText().setTransformationMethod(null);
                    etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    etRetypePassword.getEditText().setTransformationMethod(new PasswordTransformationMethod());
                    etRetypePassword.getEditText().setSelection(etRetypePassword.getEditText().getText().length());
                }
            }
        }
        return false;
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == SIGNED_UP_ACTION) {
            initRequest();
        }
    }

    @Override
    public void onUserCanceled(int code) {

    }

}

