package com.Mezda.Catastro.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mezda.Catastro.FileChooser;
import com.Mezda.Catastro.MyBase2;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.activity.CalculoActivity;
import com.Mezda.Catastro.activity.GPSService;
import com.Mezda.Catastro.activity.MapActivity;
import com.Mezda.Catastro.activity.RegisterActivity2;
import com.Mezda.Catastro.activity.RegisterActivityMod;
import com.Mezda.Catastro.adapter.CustomListAdapterDetails;
import com.Mezda.Catastro.adapter.ImagePagerAdapter;
import com.Mezda.Catastro.model.CUsoPredio;
import com.Mezda.Catastro.model.Comment;
import com.Mezda.Catastro.model.DataSpinn;
import com.Mezda.Catastro.model.Item;
import com.Mezda.Catastro.model.ListaPredios;
import com.Mezda.Catastro.util.CustomRatingBar;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;
import com.satsuware.usefulviews.LabelledSpinner;

import java.io.File;
import java.io.IOException;
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

/**
 * @author Audacity IT Solutions Ltd.
 * @class DetailViewFragment
 * @brief Fragment for showing business in detail view with user comments, rating and gallery view
 */
public class DetailViewFragment2 extends Fragment implements InternetConnectionListener {


    public static Item itemDetails;
    private static AlertDialog dialog = null;
    private final int BROWSER_ACTION = 1;
    private final int MAP_ACTION = 2;
    private final int RATE_NOW_ACTION = 3;
    SimpleDateFormat appViewFormat;
    SimpleDateFormat serverFormat;
    private ViewPager imagePager;
    private ImageView prevImgView;
    private ImageView nextImgView;
    private InternetConnectionListener internetConnectionListener;
    private TextView b1, b2, b3, b4, b5, b6, b7, b8, b9, clv, up;
    public static List<ListaPredios> movieList = new ArrayList<ListaPredios>();
    private ListView listView;
    private CustomListAdapterDetails adapter;
    private int googlePlayServiceStatus;
    private ArrayList<Comment> commentList;
    private TextView countRatingTV;
    private TextView allRatingTV;
    private LayoutInflater inflater;
    private LinearLayout commentLayout;
    private String phoneString = null;
    public static ArrayList<String> rr;
    static MyBase2 bd;
    private ProgressDialog pDialog;
    String mCurrentPhotoPath;
    boolean ban = false;
    public static String Obs="", Otrouso="", IdTareaCampo, UsoPredio, Clv, IdPredio, Croquis, Fotos, Lat, Lng,LastLetter = "A";
    public String lt [] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
    public static int IdUsoPredio = 1;
    ImageView mImageView;

    private View root;

    public DetailViewFragment2() {

    }

    public static DetailViewFragment2 newInstance(Item item) {
        DetailViewFragment2 fragment = new DetailViewFragment2();
        itemDetails = item;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_view, container, false);
            rr = new ArrayList<>();
            root= rootView;

          // rr.add("/storage/emulated/0/Pictures/JPEG_20171225_192623_-967992915.jpg");
        listView = (ListView) rootView.findViewById(R.id.list);
        imagePager = ((ViewPager) rootView.findViewById(R.id.detailHeadingImageViewPager));
        prevImgView = (ImageView) rootView.findViewById(R.id.prevImgView);
        nextImgView = (ImageView) rootView.findViewById(R.id.nextImgView);

        adapter = new CustomListAdapterDetails(this.getActivity(), movieList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this.getContext());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();


        //startActivityForResult(100, RESULT_OK);

        bd = new MyBase2(getContext());

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

        b1 = (TextView) rootView.findViewById(R.id.Fr_DView_TomarFoto); //FOTO
        b2 = (TextView) rootView.findViewById(R.id.Fr_DView_Croquis); //CROQUIS
        b3 = (TextView) rootView.findViewById(R.id.Fr_DView_GPS); //GPS
        b4 = (TextView) rootView.findViewById(R.id.Fr_DView_Obs); //OBSERVACION
        b5 = (TextView) rootView.findViewById(R.id.Fr_DView_UsoPredio); //USO PREDIO
        b6 = (TextView) rootView.findViewById(R.id.Fr_DView_Calculo);
        b7 = (TextView) rootView.findViewById(R.id.Fr_DView_NuevoLev); //NUEVO LEVANTAMIENTO
        b8 = (TextView) rootView.findViewById(R.id.Fr_DView_Guardar); //VERIFICADO
        b9 = (TextView) rootView.findViewById(R.id.Fr_DView_Refresh); //REFRESH
        clv = (TextView) rootView.findViewById(R.id.clvcct);
        up = (TextView) rootView.findViewById(R.id.dsup);

        clv.setText(Clv);
        up.setText(UsoPredio);

        getResults();

        Viewer();



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FileChooser d = new FileChooser(getActivity());
                d.setFileListener(new FileChooser.FileSelectedListener() {
                    @Override
                    public void fileSelected(File file) {
                        //System.out.println(file.getAbsoluteFile());
                        Croquis =file.getAbsoluteFile().toString();
                        UpdateDetails("Croquis", Croquis);
                        rr.add(Croquis);
                        Viewer();
                    }
                }).showDialog();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showMap();
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

                    //tvLocation.setText("Latitud: " + latitude + " \nLongitud: " + longitude);
                    //tvAddress.setText("Direccion: " + address);
                }

                Toast.makeText(getActivity(), "Direccion: " + address, Toast.LENGTH_SHORT).show();

                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();

            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteObs(getActivity(),"Observacion");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNoteUsoP(getActivity(),"Uso Predio");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CalculoActivity.class));
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterActivity2.class));
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUPredio(getActivity(),"Predio");

            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Refresh();
            }
        });

        getSearchResults();

        setHasOptionsMenu(true);
        return rootView;
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
        ft.detach(DetailViewFragment2.this).attach(DetailViewFragment2.this).commit();
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

    public static void showUPredio(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_dialog, null);

        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);




        //TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        //TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        ((TextView) view.findViewById(R.id.bodyTV)).setText("¿Desea marcar este predio como verificado?");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateTareas();
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

    private void DeleteDifLev(String IdPred, String Let) {
        SQLiteDatabase db = bd.getReadableDatabase();
        db.delete("levcons",  "Letra='" + Let + "' AND IdPredio="+IdPred, null);
        db.close();
        Refresh();
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
                internetConnectionListener = (InternetConnectionListener) DetailViewFragment2.this;
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
                    b1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.camgreen, 0, 0, 0);
                    rr.add(c.getString(2));
                }

                if(!c.getString(1).toString().equals("")){
                    b2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.upload_216485green, 0, 0, 0);
                    rr.add(c.getString(1));
                }

                //System.out.println(c.getString(3)+" "+c.getString(4));

                if(!c.getString(3).toString().equals("") && !c.getString(4).toString().equals("")){
                    b3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mapgreen, 0, 0, 0);
                }
                if(!c.getString(6).toString().equals("")){
                    b5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.down_menu_arrow_botton_gren, 0, 0, 0);
                    Otrouso = c.getString(6);
                }else{
                    Otrouso = "";
                }
                if(!c.getString(8).toString().equals("")){
                    b4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clipboard_pencil_write_trackgreen, 0, 0, 0);
                    Obs = "";
                }else{

                }


            } while(c.moveToNext());
        }

    }

    private static void UpdateDetails(String Campo, String Valor) {
        SQLiteDatabase db = bd.getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(""+Campo, Valor);
        db.update("levcampo", valores, "IdTareaC=" + IdTareaCampo, null);
        db.close();
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

    private void Viewer(){
        if (rr != null) {

            //! viewpager to show images with horizontal scrolling.
            imagePager.setAdapter(new ImagePagerAdapter(getActivity(), rr));

            //! hide previous and next arrow if adapter size is less then 2
            if (imagePager.getAdapter().getCount() <= 1) {
                prevImgView.setVisibility(View.INVISIBLE);
                nextImgView.setVisibility(View.INVISIBLE);
            }


            ((ViewPager) root.findViewById(R.id.detailHeadingImageViewPager)).addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (imagePager.getAdapter().getCount() > 1) {
                        if (position == 0) {
                            nextImgView.setVisibility(View.VISIBLE);
                            prevImgView.setVisibility(View.INVISIBLE);
                        } else if (position == imagePager.getAdapter().getCount() - 1) {
                            prevImgView.setVisibility(View.VISIBLE);
                            nextImgView.setVisibility(View.INVISIBLE);
                        } else {
                            prevImgView.setVisibility(View.VISIBLE);
                            nextImgView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        }
    }
    /**
     * @brief custom dialog for showing rating dialog
     * @param context application context
     * @param headline headline in String
     * @param positiveString positive text in String
     * @param negativeString negative text in String
     */
    private void showRatingDialog(final Context context, String headline,
                                  String positiveString, String negativeString) {
        final EditText etComment;
        final CustomRatingBar ratingBar;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_rating_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(headline);
        etComment = (EditText) view.findViewById(R.id.etComment);
        ratingBar = (CustomRatingBar) view.findViewById(R.id.ratingBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (ratingBar.getScore() <= 0) {
                            Toast.makeText(getActivity(), getResources().
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
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
             setPic();
             Viewer();
            // System.out.println("Success!!!!!");
        }
    }


    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mImageView.setImageBitmap(bitmap);
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

                movieList.add(movie);
            } while(c0.moveToNext());
        }

        if(ban == true) {
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Clv + "_" + IdTareaCampo +  "_" + timeStamp ;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Fotos = mCurrentPhotoPath;
        UpdateDetails("Foto", Fotos);
        rr.add(Fotos);

        Log.e("Getpath", "Cool" + mCurrentPhotoPath);
        Viewer();
        return image;
    }




    @Override
    public void onConnectionEstablished(int code) {
        if (code == BROWSER_ACTION) {
            browseUrl(getActivity(), itemDetails.getWebUrl());
        }
        if (code == MAP_ACTION) {
            APP_MAP_MODE = false;
            startActivity(new Intent(getActivity(), MapActivity.class));
        } else if (code == RATE_NOW_ACTION) {
            showRatingDialog(getActivity(), itemDetails.getTitle(),
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





    /**
     * @brief methods to add all comments to comment view
     * @param commentList collection of comment to make updates of comment view
     */

}
