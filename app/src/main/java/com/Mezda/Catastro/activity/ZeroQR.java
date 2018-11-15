package com.Mezda.Catastro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.Const;
import com.Mezda.Catastro.FileChooser;
import com.Mezda.Catastro.MyBase3;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.fragment.ConfigMap;
import com.Mezda.Catastro.fragment.SetMapQR0;
import com.Mezda.Catastro.model.Item;
import com.Mezda.Catastro.util.CustomRatingBar;
import com.Mezda.Catastro.util.UtilMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.Mezda.Catastro.util.UtilMethods.APP_MAP_MODE;
import static com.Mezda.Catastro.util.UtilMethods.browseUrl;
import static com.Mezda.Catastro.util.UtilMethods.getPreferenceString;
import static com.Mezda.Catastro.util.UtilMethods.isConnectedToInternet;
import static com.Mezda.Catastro.util.UtilMethods.isGpsEnable;
import static com.Mezda.Catastro.util.UtilMethods.showNoGpsDialog;
import static com.Mezda.Catastro.util.UtilMethods.showNoInternetDialog;


public class ZeroQR extends AppCompatActivity implements UtilMethods.InternetConnectionListener {
    private static final int REQUEST_CAMARA_WRITE_PER = 111;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 112;
    File image;
    OutputStream outputStream = null;
    private TextView Fr_DVQR_GPS, Fr_DVQR_Observacion, Fr_DVQR_Guardar, Fr_DVQR_Cancelar, clv;
    public static String ClaveCat, IdPredio, TipoPredio,Idnot1, Idnot2;
    public static ArrayList<String> rr;
    private ListView listView;
    static MyBase3 bd;
    private ProgressDialog pDialog;
    private UtilMethods.InternetConnectionListener internetConnectionListener;
    String mCurrentPhotoPath;
    public static String Obs="",  UsoPredio,  Fotos ="";
    ImageView Fr_DVQR_TomarFoto, Fr_DVQR_ImFoto, Fr_DVQR_Gallery;
    public static String Lat = "", Lng = "";
    //private static AlertDialog dialog = null;
    private final int BROWSER_ACTION = 1;
    private final int MAP_ACTION = 2;
    private final int RATE_NOW_ACTION = 3;
    public static Item itemDetails;
    private ImageView Fr_DVQR_Salir;
    private boolean isUserCanceled = false;
    DateFormat hourFormat ;
    DateFormat dateFormat;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detail_viewqr);

        rr = new ArrayList<>();

        // rr.add("/storage/emulated/0/Pictures/JPEG_20171225_192623_-967992915.jpg");
        listView = (ListView) findViewById(R.id.list);

        date = new Date();
        hourFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");


        bd = new MyBase3(this);
    
        Fr_DVQR_ImFoto = (ImageView) findViewById(R.id.Fr_DVQR_ImFoto);
        Fr_DVQR_TomarFoto = (ImageView) findViewById(R.id.Fr_DVQR_TomarFoto); //FOTO
        Fr_DVQR_Gallery = (ImageView) findViewById(R.id.Fr_DVQR_Gallery);
//        b2 = (TextView) findViewById(R.id.Fr_DView_Croquis); //CROQUIS
        Fr_DVQR_GPS = (TextView) findViewById(R.id.Fr_DVQR_GPS); //GPS
        Fr_DVQR_Observacion = (TextView) findViewById(R.id.Fr_DVQR_Observacion); //OBSERVACION
        //b5 = (TextView) findViewById(R.id.btnSignUp5); //USO PREDIO
//        b6 = (TextView) findViewById(R.id.Fr_DView_Calculo);
//        b7 = (TextView) findViewById(R.id.Fr_DView_NuevoLev); //NUEVO LEVANTAMIENTO
//        b8 = (TextView) findViewById(R.id.Fr_DView_Guardar); //VERIFICADO
//        b9 = (TextView) findViewById(R.id.crossImgView); //REFRESH
        Fr_DVQR_Guardar = (TextView) findViewById(R.id.Fr_DVQR_Guardar); //Guardar
        Fr_DVQR_Cancelar = (TextView) findViewById(R.id.Fr_DVQR_Cancelar); //Cancel
        clv = (TextView) findViewById(R.id.clvcct);
        clv.setText(ClaveCat);
        //up.setText(UsoPredio);

        //getResults();

        // Viewer();
    
    
        Fr_DVQR_Salir = (ImageView) findViewById(R.id.Fr_DVQR_Salir);
        Fr_DVQR_Salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isUserCanceled = true;
                onPause();
            }
        });
    
        Fr_DVQR_TomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                if (Fr_DVQR_ImFoto.getDrawable() == null) {
                    getcamara();
                } else {
                    final android.app.AlertDialog.Builder alerBuilder1 = new android.app.AlertDialog.Builder(ZeroQR.this);
                    alerBuilder1.setMessage("Desea recapturar la imagen?")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "Obteniendo Camara", Toast.LENGTH_SHORT).show();
                                    getcamara();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null).show();
                    alerBuilder1.create();
                }
            }
        });
        Fr_DVQR_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ZeroQR.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ZeroQR.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_CAMARA_WRITE_PER);
                } else {
                    FileChooser fileChooser = new FileChooser(ZeroQR.this);
                    fileChooser.setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(File file) {
                            String ruta = file.getAbsolutePath().toString();
                            //TODO: Get the dimensions of the view;
                            int targetWidth = 550;
                            int targetHeight = 550;
                            //TODO: Get the dimensions of the bitmap
                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                            bmOptions.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(ruta, bmOptions);
                            int photoWidth = bmOptions.outWidth;
                            int photoheight = bmOptions.outHeight;
                            //TODO: Determine how much to scale down the image
                            int scaleFactor = Math.min(photoWidth / targetWidth, photoheight / targetHeight);
                            //TODO: Decode the image file into a Bitmap sized to fill the view
                            bmOptions.inJustDecodeBounds = false;
                            bmOptions.inSampleSize = scaleFactor;
                            bmOptions.inPurgeable = true;
                            try {
                                int m_compress = 50;
                                Bitmap bitmap = BitmapFactory.decodeFile(ruta, bmOptions);
                                outputStream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, m_compress, outputStream);
                                Fr_DVQR_ImFoto.setImageBitmap(bitmap);
                                Fotos= ruta;
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                Log.e("IOE: ",e.toString());
                                e.printStackTrace();
                            }
                        }
                    }).showDialog();
                }
            }
        });
        Fr_DVQR_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showMap();
                String address = "";
                GPSService mGPSService = new GPSService(ZeroQR.this);
                mGPSService.getLocation();
                if (mGPSService.isLocationAvailable == false) {
                    Toast.makeText(getApplication(), "Tu localizacion no esta disponible, por favor intenta de nuevo.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    double latitude = mGPSService.getLatitude();
                    double longitude = mGPSService.getLongitude();
                    ConfigMap.setLat(latitude);
                    ConfigMap.setLng(longitude);
                    Lat = latitude+"";
                    Lng = longitude+"";
                    //UpdateGPS(latitude+"",longitude+"");
                    Toast.makeText(getApplication(), "Latitud:" + latitude + " | Longitud: " + longitude, Toast.LENGTH_LONG).show();
                    address = mGPSService.getLocationAddress();
                    //tvLocation.setText("Latitud: " + latitude + " \nLongitud: " + longitude);
                    //tvAddress.setText("Direccion: " + address);
                }
                Toast.makeText(getApplication(), "Direccion: " + address, Toast.LENGTH_SHORT).show();
                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
            }
        });
    
        Fr_DVQR_Observacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteObs(ZeroQR.this,"Observacion");
            }
        });
    
    
        Fr_DVQR_Cancelar.setOnClickListener(new View.OnClickListener() { //CANCELAR
            @Override
            public void onClick(View view) {
                isUserCanceled = true;
                onPause();
            }
        });
    
        Fr_DVQR_Guardar.setOnClickListener(new View.OnClickListener() { //GUARDAR
            @Override
            public void onClick(View view) {
                InsertLCampo();
            }
        });

//        b9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = getIntent();
//                finish();
//                startActivity(intent);
//            }
//        });

    }



    public void showNoteObs(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View view = inflater.inflate(R.layout.dialog_note, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no = (TextView) view.findViewById(R.id.del_bt_no);
        final EditText s = (EditText) view.findViewById(R.id.etComment);
        s.setText(Obs);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Obs = s.getText().toString();
                //UpdateDetails("Observacion", Obs);
                finalDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });


    }


    private void showMap() {

        if (isConnectedToInternet(this)) {

            if (isGpsEnable(this)) {
                startActivity(new Intent(this, SetMapQR0.class));
            } else {
                showNoGpsDialog(this, getResources().getString(R.string.no_gps),
                        getResources().getString(R.string.no_gps_message),
                        getResources().getString(R.string.no_gps_positive_text),
                        getResources().getString(R.string.no_gps_negative_text));
            }

        } else {
            internetConnectionListener = (UtilMethods.InternetConnectionListener) ZeroQR.this;
            showNoInternetDialog(this, internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.cancel_string), MAP_ACTION);
        }


    }

    private void showRatingDialog(final Context context, String headline, String positiveString, String negativeString) {
        final EditText etComment;
        final CustomRatingBar ratingBar;
        AlertDialog dialog = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_rating_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(headline);
        etComment = (EditText) view.findViewById(R.id.etComment);
        ratingBar = (CustomRatingBar) view.findViewById(R.id.ratingBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ratingBar.getScore() <= 0) {
                            Toast.makeText(getApplicationContext(), getResources().
                                            getString(R.string.no_rating_error_string),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            dialog.dismiss();
                        }
                    }
                }).setNegativeButton(negativeString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(view)
                .setCancelable(true);

        dialog = builder.create();
        dialog.show();
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
    public void onResume() {
        super.onResume();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result: "," Entered Activity Result");
        if (requestCode == REQUEST_CAMARA_WRITE_PER && resultCode == RESULT_OK) {
            setPic(Fr_DVQR_ImFoto,mCurrentPhotoPath);
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            dispatchTakePictureIntent();
        }
    }

  

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    


    public void InsertLCampo() {

        if(Fotos.equals("")){
            Toast.makeText(this, "Agrega una foto!", Toast.LENGTH_LONG).show();
            return;
        }

        if(Lat.equals("") && Lng.equals("")){
            Toast.makeText(this, "Selecciona una ubicacion!", Toast.LENGTH_LONG).show();
            return;
        }

        SQLiteDatabase db  = bd.getReadableDatabase();
        if(db != null){
            //pDialog = new ProgressDialog(this);
            // Showing progress dialog before making http request
            //pDialog.setMessage("Guardando...");
            //pDialog.show();

            ContentValues valores = new ContentValues();
            valores.put("ClaveCatastral", ClaveCat);
            valores.put("TipoPredio", TipoPredio);
            valores.put("IdBrigadista", getPreferenceString(ZeroQR.this,Const.SP_idbrigada).toString());
            valores.put("Lat", Lat);
            valores.put("Lng", Lng);
            valores.put("Fecha", dateFormat.format(date));
            valores.put("Hora", hourFormat.format(date));
            valores.put("Foto", Fotos);
            valores.put("Observacion", Obs);
            valores.put("IdNot1", Idnot1);
            valores.put("IdNot2", Idnot2);
            long d = db.insert("reztemp", null, valores);
            System.out.println(d);

            new AlertDialog.Builder(this)
                    .setTitle("Rezagos")
                    .setMessage("Registro Guardado Correctamente!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                            isUserCanceled = true;
                            onPause();

                        }
                    }).show();
            //hidePDialog();



            db.close();
        }
    }


    @Override
    public void onConnectionEstablished(int code) {
        if (code == BROWSER_ACTION) {
            browseUrl(this, itemDetails.getWebUrl());
        }
        if (code == MAP_ACTION) {
            APP_MAP_MODE = false;
            startActivity(new Intent(this, MapActivity.class));
        } else if (code == RATE_NOW_ACTION) {
            showRatingDialog(this, itemDetails.getTitle(),
                    getResources().getString(R.string.submit_string),
                    getResources().getString(R.string.cancel_string));
        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == RATE_NOW_ACTION) {

        }
        if (code == MAP_ACTION) {
            APP_MAP_MODE = false;
        }
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++                              CAMARA PERMISSION
    
    private void getcamara() {
        if (ContextCompat.checkSelfPermission(ZeroQR.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_CAMARA_WRITE_PER);
        } else {
            dispatchTakePictureIntent();
        }
    }
    
    @SuppressLint("NewApi")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        if (takePictureIntent.resolveActivity(ZeroQR.this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e("IOE: ",e.toString());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ZeroQR.this, getResources().getString(R.string.FileProviderString), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                
                startActivityForResult(takePictureIntent, REQUEST_CAMARA_WRITE_PER);
            }
        }
    }
    
    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = ClaveCat + "_" + timestamp;
        File storageDir = null;
        storageDir = (this).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        
        
        image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    private void setPic(ImageView mImageView, String Path) {
        int targetWidth = 550;
        int targetHeight = 550;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        BitmapFactory.decodeFile(Path, bmOptions);
        int photoWidth = bmOptions.outWidth;
        int photoheight = bmOptions.outHeight;
        int scaleFactor = Math.min(photoWidth / targetWidth, photoheight / targetHeight);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(Path,bmOptions);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(Path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            mImageView.setImageBitmap(UtilMethods.rotateBitmap(bitmap,orientation));
            Fotos = Path;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
