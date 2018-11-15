package com.Mezda.Catastro.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.FileChooser;
import com.Mezda.Catastro.MyBase;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.activity.CalculoActivity;
import com.Mezda.Catastro.activity.GPSService;
import com.Mezda.Catastro.activity.MapActivity;
import com.Mezda.Catastro.activity.RegisterActivity;
import com.Mezda.Catastro.activity.RegisterActivityMod;
import com.Mezda.Catastro.adapter.CustomListAdapterDetails;
import com.Mezda.Catastro.model.CUsoPredio;
import com.Mezda.Catastro.model.DataSpinn;
import com.Mezda.Catastro.model.Item;
import com.Mezda.Catastro.model.ListaPredios;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;
import com.bumptech.glide.Glide;
import com.satsuware.usefulviews.LabelledSpinner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.Mezda.Catastro.util.UtilMethods.APP_MAP_MODE;
import static com.Mezda.Catastro.util.UtilMethods.browseUrl;
import static com.Mezda.Catastro.util.UtilMethods.isConnectedToInternet;
import static com.Mezda.Catastro.util.UtilMethods.isGpsEnable;
import static com.Mezda.Catastro.util.UtilMethods.showNoGpsDialog;
import static com.Mezda.Catastro.util.UtilMethods.showNoInternetDialog;


public class DetailViewFragment extends Fragment implements InternetConnectionListener {
    
    private static final int REQUEST_CAMARA_WRITE_PER = 111;
    private static final int REQUEST_GALLERY_WRITE_PER = 112;
    
    File image;
    OutputStream outputStream = null;
    public static Item itemDetails;
    private static AlertDialog dialog = null;
    private final int BROWSER_ACTION = 1;
    private final int MAP_ACTION = 2;
    private final int RATE_NOW_ACTION = 3;
    private InternetConnectionListener internetConnectionListener;
    private TextView
            Fr_DView_Croquis,
            Fr_DView_GPS,
            Fr_DView_Obs,
            Fr_DView_UsoPredio,
            Fr_DView_Calculo,
            Fr_DView_NuevoLev,
            Fr_DView_Guardar,
            Fr_DView_Refresh,
            Fr_DView_ClCatastral;
    private ImageView Fr_DView_TomarFoto, Fr_DView_Gallery;
    public static List<ListaPredios> movieList = new ArrayList<ListaPredios>();
    private ListView listView;
    private CustomListAdapterDetails adapter;
    public static ArrayList<String> rr;
    static MyBase bd;
    boolean ban = false, ban2=false;
    private ProgressDialog pDialog;
    String mCurrentPhotoPath;
    public static String Obs, Otrouso, IdTareaCampo, UsoPredio, Clv, IdPredio, Croquis, Fotos, Lat, Lng, LastLetter = "A";
    public static int IdUsoPredio = 1;
    public String lt [] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
    ImageView mImageView, Fr_DView_ImFoto,Fr_DView_ImCroquis;


    public DetailViewFragment() {

    }

    public static DetailViewFragment newInstance(Item item) {
        DetailViewFragment fragment = new DetailViewFragment();
        itemDetails = item;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_view, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Obs="";
        UsoPredio="";
        Fr_DView_ImFoto = (ImageView) view.findViewById(R.id.Fr_DView_ImFoto);
        Fr_DView_TomarFoto = (ImageView) view.findViewById(R.id.Fr_DView_TomarFoto); //FOTO
        Fr_DView_Gallery = (ImageView) view.findViewById(R.id.Fr_DView_Gallery);
        Fr_DView_ImCroquis = (ImageView) view.findViewById(R.id.Fr_DView_ImCroquis);
        Fr_DView_Croquis = (TextView) view.findViewById(R.id.Fr_DView_Croquis); //CROQUIS
        Fr_DView_GPS = (TextView) view.findViewById(R.id.Fr_DView_GPS); //GPS
        Fr_DView_Obs = (TextView) view.findViewById(R.id.Fr_DView_Obs); //OBSERVACION
        Fr_DView_UsoPredio = (TextView) view.findViewById(R.id.Fr_DView_UsoPredio); //USO PREDIO
        Fr_DView_Calculo = (TextView) view.findViewById(R.id.Fr_DView_Calculo);
        Fr_DView_NuevoLev = (TextView) view.findViewById(R.id.Fr_DView_NuevoLev); //NUEVO LEVANTAMIENTO
        Fr_DView_Guardar = (TextView) view.findViewById(R.id.Fr_DView_Guardar); //VERIFICADO
        Fr_DView_Refresh = (TextView) view.findViewById(R.id.Fr_DView_Refresh); //REFRESH
        Fr_DView_ClCatastral = (TextView) view.findViewById(R.id.Fr_DView_ClCatastral);
        rr = new ArrayList<>();
    
        listView = (ListView) view.findViewById(R.id.list);
        adapter = new CustomListAdapterDetails(this.getActivity(), movieList);
        listView.setAdapter(adapter);
    
        pDialog = new ProgressDialog(this.getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();
    
    
    
        bd = new MyBase(getContext());
    
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListaPredios d = movieList.get(position);
                //    Log.e("Vals",d.getClaveEstado() + " " + d.getClaveTermino() + " " + d.getClaveTipo());
                if(d.isDown()){
                
                    RegisterActivityMod.ctipo = d.getClaveTipo();
                    RegisterActivityMod.ctermino = d.getClaveTermino();
                    RegisterActivityMod.cestado = d.getClaveEstado();
                    RegisterActivityMod.antx = d.getAntiguedad();
                    RegisterActivityMod.isdown = d.isDown();
                    RegisterActivityMod.areax = d.getArea();
                    RegisterActivityMod.letrax = d.getLetra();
                
                    startActivity(new Intent(getActivity(), RegisterActivityMod.class));
                }else{
                    showKill(getActivity(),"Diferencia",d.getIdPredio(),d.getLetra());
                }
            
            }
        });
    
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    
        //        up = (TextView) rootView.findViewById(R.id.dsup);
    
        Fr_DView_ClCatastral.setText(Clv);
        //        up.setText(UsoPredio);
    
        getResults();
    
    
    
        Fr_DView_TomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                if (Fr_DView_ImFoto.getDrawable() == null) {
                    getcamara();
                } else {
                    final android.app.AlertDialog.Builder alerBuilder1 = new android.app.AlertDialog.Builder(getContext());
                    alerBuilder1.setMessage("Desea recapturar la imagen?")
                            .setTitle("Aviso")
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "Obteniendo Camara", Toast.LENGTH_SHORT).show();
                                    getcamara();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null).show();
                    alerBuilder1.create();
                }
            }
        });
        Fr_DView_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser fileChooser = new FileChooser(getActivity());
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
                            Fr_DView_ImFoto.setImageBitmap(bitmap);
                            UpdateDetails("Foto",ruta);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            Log.e("IOE: ",e.toString());
                            e.printStackTrace();
                        }
                    }
                }).showDialog();
            }
        });
        Fr_DView_Croquis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser d = new FileChooser(getActivity());
                d.setFileListener(new FileChooser.FileSelectedListener() {
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
                            Fr_DView_ImCroquis.setImageBitmap(bitmap);
                            UpdateDetails("Croquis",ruta);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            Log.e("IOE: ",e.toString());
                            e.printStackTrace();
                        }
                    }
                }).showDialog();
            }
        });
        
        Fr_DView_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // showMap();
                String address = "";
                GPSService mGPSService = new GPSService(getActivity());
                mGPSService.getLocation();
                if (mGPSService.isLocationAvailable == false) {
                    // Here you can ask the user to try again, using return; for that
                    Toast.makeText(getActivity(), "Tu localizacion no esta disponible, por favor intenta de nuevo.", Toast.LENGTH_SHORT).show();
                    return;
                    // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                    // address = "Location not available";
                } else {
                    // Getting location co-ordinates
                    double latitude = mGPSService.getLatitude();
                    double longitude = mGPSService.getLongitude();
                    ConfigMap.setLat(latitude);
                    ConfigMap.setLng(longitude);
                    UpdateGPS(latitude+"",longitude+"");
                    Toast.makeText(getActivity(), "Latitud:" + latitude + " | Longitud: " + longitude, Toast.LENGTH_LONG).show();
                    address = mGPSService.getLocationAddress();
                    // tvLocation.setText("Latitud: " + latitude + " \nLongitud: " + longitude);
                    //  tvAddress.setText("Direccion: " + address);
                }
                Toast.makeText(getActivity(), "Localizacion: " + address, Toast.LENGTH_SHORT).show();
                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
            }
        });
    
        Fr_DView_Obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                showNoteObs(getActivity(),"Observacion");
                
            }
        });
    
        Fr_DView_UsoPredio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteUsoP(getActivity(),"Uso Predio");
            }
        });
    
        Fr_DView_Calculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalculoActivity.class));
            }
        });
    
        Fr_DView_NuevoLev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
    
        Fr_DView_Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = null;
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View viewpop = inflater.inflate(R.layout.layout_dialog, null);
                ((TextView) viewpop.findViewById(R.id.headlineTV)).setText("Verificar");
                //TextView si = (TextView) view.findViewById(R.id.del_bt_si);
                //TextView no= (TextView) view.findViewById(R.id.del_bt_no);
                ((TextView) viewpop.findViewById(R.id.bodyTV)).setText("¿Desea marcar este predio como verificado?");
    
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateTareas();
                                dialog.dismiss();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
            
                        .setView(viewpop)
                        .setCancelable(false);
                dialog = builder.create();
                dialog.show();
            }
        });
    
        Fr_DView_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
                Refresh();
            }
        });
    
        getSearchResults();
    
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.findItem(R.id.action_filter).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
        }
    }
    
  
    private void Refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(DetailViewFragment.this).attach(DetailViewFragment.this).commit();
    }

    public static void showNoteObs(final Activity context, final String heading) {
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
                UpdateDetails("Observacion", Obs);
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

 


    public void showKill(final Activity context, final String heading, final String IdPred, final String Let) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_dialog, null);

        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);




        //TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        //TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        ((TextView) view.findViewById(R.id.bodyTV)).setText("¿Desea eliminar esta diferencia?");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //UpdateTareas();
                        DeleteDifLev(IdPred, Let);
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();



    }

    public static void showNoteUsoP(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.dialog_usop, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        final LabelledSpinner lsp1 = (LabelledSpinner) view.findViewById(R.id.spestconst);
        List<CUsoPredio> list = DataSpinn.UsoPredio();
        lsp1.setItemsArray(list);
        lsp1.getLabel().setTextSize(22);
        lsp1.getSpinner().setSelection(IdUsoPredio-1);

        TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        final EditText s = (EditText) view.findViewById(R.id.etComment);
        s.setText(Otrouso);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false);

        dialog = builder.create();
        dialog.show();

        final AlertDialog finalDialog = dialog;
        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Otrouso = s.getText().toString();
                CUsoPredio e1 = (CUsoPredio)lsp1.getSpinner().getSelectedItem();
                UpdateDetails("OtroUso", Otrouso);
                UpdateDetails("IdUsoPredio", e1.getId());
                IdUsoPredio = Integer.parseInt(e1.getId());
                finalDialog.dismiss();
                //Refresh();
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

            if (isConnectedToInternet(getActivity())) {

                    if (isGpsEnable(getActivity())) {
                        startActivity(new Intent(getActivity(), SetMapQR.class));
                    } else {
                        showNoGpsDialog(getActivity(), getResources().getString(R.string.no_gps),
                                getResources().getString(R.string.no_gps_message),
                                getResources().getString(R.string.no_gps_positive_text),
                                getResources().getString(R.string.no_gps_negative_text));
                    }

            } else {
                internetConnectionListener = (InternetConnectionListener) DetailViewFragment.this;
                showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
                        getResources().getString(R.string.no_internet_text),
                        getResources().getString(R.string.retry_string),
                        getResources().getString(R.string.cancel_string), MAP_ACTION);
            }


    }


    private void getResults() {

        String[] campos = new String[] {"IdTareaC","Croquis","Foto","Latitud","Longitud","IdUsoPredio","OtroUso","Notificacion","Observacion"};

        Cursor c = bd.getReadableDatabase().query("levcampo", campos, "IdTareaC="+IdTareaCampo, null, null, null, null);

         //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //System.out.println("DDDS: "+c.getString(1).toString()+ " " +c.getString(2).toString() + " "+c.getString(3).toString()+ " " +c.getString(6).toString() + " "+c.getString(8).toString());


                if(!c.getString(2).toString().equals("")){
                    Fr_DView_TomarFoto.setImageResource(R.drawable.camgreen);
                    Fr_DView_ImFoto.setImageBitmap(getImgFromRoute(c.getString(2)));
                    rr.add(c.getString(2));
                }

                if(!c.getString(1).toString().equals("")){
                    Fr_DView_Croquis.setCompoundDrawablesWithIntrinsicBounds(R.drawable.upload_216485green, 0, 0, 0);
                    Fr_DView_ImCroquis.setImageBitmap(getImgFromRoute(c.getString(1)));
                    rr.add(c.getString(1));
                }

                //System.out.println(c.getString(3)+" "+c.getString(4));

                if(!c.getString(3).toString().equals("") && !c.getString(4).toString().equals("")){
                    Fr_DView_GPS.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapgreen, 0, 0, 0);
                }
                if(!c.getString(6).toString().equals("")){
                    Fr_DView_UsoPredio.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_menu_arrow_botton_gren, 0, 0, 0);
                    Otrouso = c.getString(6);
                }else{
                    Otrouso = "";
                }
                if(!c.getString(8).toString().equals("")){
                    Fr_DView_Obs.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clipboard_pencil_write_trackgreen, 0, 0, 0);
                    Obs = c.getString(8);
                }else{

                }


            } while(c.moveToNext());
        }

    }
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Result: "," Entered Activity Result");
        if (requestCode == REQUEST_CAMARA_WRITE_PER && resultCode == RESULT_OK) {
            setPic(Fr_DView_ImFoto,mCurrentPhotoPath);
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            dispatchTakePictureIntent();
        }
    }
    
    
    private static void UpdateDetails(String Campo, String Valor) {
        SQLiteDatabase db = bd.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(""+Campo, Valor);
        db.update("levcampo", valores, "IdTareaC=" + IdTareaCampo, null);
        db.close();
    }

    private void DeleteDifLev(String IdPred, String Let) {
        SQLiteDatabase db = bd.getReadableDatabase();
        db.delete("levcons",  "Letra='" + Let + "' AND IdPredio="+IdPred, null);
        db.close();
        Refresh();
    }

    public static void UpdateGPS(String Lat, String Lng) {
        SQLiteDatabase db = bd.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("Latitud", Lat);
        valores.put("Longitud", Lng);
        db.update("levcampo", valores, "IdTareaC=" + IdTareaCampo, null);
        db.close();
    }

    private static void UpdateTareas() {
        SQLiteDatabase db = bd.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("Verif", "1");
        db.update("tareas", valores, "IdTareaP=" + IdTareaCampo, null);
        db.close();
    }
    
    
    @Override
    public void onResume() {
        super.onResume();

    }




    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

  

    private void getSearchResults() {
        movieList.clear();
        String[] campos = new String[] {"Letra","Area","Antiguedad","Valor","ClaveCons","ClaveEstadoCons","ClaveTerminoCons"};

        //Arriba
        Cursor c = bd.getReadableDatabase().query("predios", campos, "IdPredio=" + IdPredio, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Log.d(TAG, codigo);
                ListaPredios movie = new ListaPredios();

                movie.setLetra(c.getString(0));
                LastLetter = c.getString(0);
                ban = true;
                movie.setArea(c.getString(1));
                movie.setAntiguedad(c.getString(2));
                movie.setDown(true);
                movie.setValorC(c.getString(3));
                movie.setClaveTipo(c.getString(4));
                movie.setClaveEstado(c.getString(5));
                movie.setClaveTermino(c.getString(6));

                movieList.add(movie);
            } while(c.moveToNext());
        }


        //Abajo
        String[] campos0 = new String[] {"IdPredio","Letra","Area","Antiguedad","ValorCons","IdTipoCons","IdEstadoCons","IdTerminoCons"};
        Cursor c0 = bd.getReadableDatabase().query("levcons", campos0, "IdPredio=" + IdPredio, null, null, null, null);

        //Nos aseguramos de que existe al menos un registro
        if (c0.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Log.d(TAG, codigo);
                ListaPredios movie = new ListaPredios();

                movie.setIdPredio(c0.getString(0));
                movie.setLetra(c0.getString(1));
                ban = true;
                for(int i=0;i<lt.length;i++){
                    if(lt[i].equals(c0.getString(1))){
                        LastLetter = lt[i+1];
                        ban2=true;
                        break;
                    }
                }
                movie.setArea(c0.getString(2));
                movie.setAntiguedad(c0.getString(3));
                movie.setDown(false);
                movie.setValorC(c0.getString(4));
                movie.setClaveTipo(c0.getString(5));
                movie.setClaveEstado(c0.getString(6));
                movie.setClaveTermino(c0.getString(7));
                Log.e("SD---------->",c0.getString(5) + " " +c0.getString(6)+" "+ c0.getString(7));
                movieList.add(movie);
            } while(c0.moveToNext());
        }

        if(ban == true && ban2==false) {
            for (int i = 0; i < lt.length; i++) {
                if (lt[i].equals(LastLetter)) {
                    LastLetter = lt[i + 1];
                    break;
                }
            }
        }

        hidePDialog();
        adapter.notifyDataSetChanged();
    }
    
    @Override
    public void onConnectionEstablished(int code) {
        if (code == BROWSER_ACTION) {
            browseUrl(getActivity(), itemDetails.getWebUrl());
        }
        if (code == MAP_ACTION) {
            APP_MAP_MODE = false;
            startActivity(new Intent(getActivity(), MapActivity.class));
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
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
        
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    Log.e("IOE: ",e.toString());
                }
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(), getResources().getString(R.string.FileProviderString), photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    takePictureIntent.setClipData(ClipData.newRawUri("", photoURI));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    
                    startActivityForResult(takePictureIntent, REQUEST_CAMARA_WRITE_PER);
                }
        }
    }
    
    private File createImageFile() throws IOException {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = Clv + "_" + timestamp;
        File storageDir = null;
        storageDir = (getActivity()).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        
        
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
            UpdateDetails("Foto",Path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private Bitmap getImgFromRoute(String ruta){
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(Uri.fromFile(new File(ruta)));
            Bitmap maped = BitmapFactory.decodeStream(new BufferedInputStream(inputStream));
            return maped;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
}
