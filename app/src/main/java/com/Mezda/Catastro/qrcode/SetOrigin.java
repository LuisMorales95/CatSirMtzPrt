package com.Mezda.Catastro.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.R;
import com.Mezda.Catastro.activity.ZeroQR;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class SetOrigin extends AppCompatActivity {


  private ImageView cross;
  private TextView qrb;
  private boolean isUserCanceled = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //This contains the MapView in XML and needs to be called after the account manager
    setContentView(R.layout.activity_set_origen);

    qrb = (TextView)  findViewById(R.id.btnGenDir);

    final Activity activity = this;
    qrb.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
      }
    });

    cross = (ImageView) findViewById(R.id.crossImgView);
    cross.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        isUserCanceled = true;
        onPause();
      }
    });

  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if(result != null){
      if(result.getContents()==null){
        Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();
      } else {

        String cad = result.getContents().toString();
        if(!cad.equals("")) {
          try{
            //Log.d("Arshh: ",cad);
          String[] cs = cad.split("/");
            ZeroQR.ClaveCat = cs[0];
            ZeroQR.TipoPredio = cs[1];
            ZeroQR.Idnot1 = cs[2];
            ZeroQR.Idnot2 = cs[3];
            startActivity(new Intent(SetOrigin.this, ZeroQR.class));

         Toast.makeText(this, cs[0] + " " + cs[1] + " " + cs[2] + " " + cs[3], Toast.LENGTH_LONG).show();
          }catch(Exception D){
            Toast.makeText(this, "Codigo QR Incorrecto SPLIT", Toast.LENGTH_LONG).show();
          }

        }else{
          Toast.makeText(this, "Codigo QR Incorrecto", Toast.LENGTH_LONG).show();
        }

      }
    } else {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }




  @Override
  public void onResume() {
    super.onResume();
  }

  @Override
  protected void onStart() {
    super.onStart();

  }

  @Override
  public void onPause() {
    super.onPause();
    if (isUserCanceled) {
      overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
      finish();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }



  @Override
  public void onLowMemory() {
    super.onLowMemory();
  }










}
