package com.Mezda.Catastro.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Mezda.Catastro.Base64;
import com.Mezda.Catastro.Const;
import com.Mezda.Catastro.LinksURL;
import com.Mezda.Catastro.MyBase3;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.VolleySingleton;
import com.Mezda.Catastro.activity.HomeActivity;
import com.Mezda.Catastro.activity.ZeroQR;
import com.Mezda.Catastro.adapter.CustomListAdapterQR;
import com.Mezda.Catastro.adapter.ResultListAdapter;
import com.Mezda.Catastro.model.Item;
import com.Mezda.Catastro.model.RezagoQR;
import com.Mezda.Catastro.util.LocationChangeListener;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Mezda.Catastro.util.Constants.isHomeOpened;
import static com.Mezda.Catastro.util.Constants.isResultListFragmentOpened;
import static com.Mezda.Catastro.util.UtilMethods.getPreferenceString;
import static com.Mezda.Catastro.util.UtilMethods.showNoInternetDialog;


/**
 * @author Audacity IT Solutions Ltd.
 * @class ResultListFragment
 * @brief Fragment for showing the business list
 */

public class ResultListFragmentqr extends AppCompatActivity implements InternetConnectionListener,
        LocationChangeListener {

    public static String catId;
    public static String searchTerm;
    public static LocationChangeListener locationChangeListener;
    private final int RESULT_ACTION = 1;
    private final int RESULT_LIMIT = 100;
    private ListView resultListView;
    private ArrayList<Item> searchResultList;
    private ResultListCallbacks mCallbacks;
    private InternetConnectionListener internetConnectionListener;

    // Log tag
    private static final String TAG = HomeActivity.class.getSimpleName();

    // Movies json url

    private String tag_json_obj = "jobj_req";
    private static  ProgressDialog pDialog;
    public static int pos = 0;
    public static List<RezagoQR> movieList0 = new ArrayList<RezagoQR>();
    private ListView listView;
    private ImageView prevImgView;
    private ImageView nextImgView;
    static MyBase3 bd;
    String ba1;
    private static CustomListAdapterQR adapter;
    private ViewPager imagePager;

    public ResultListFragmentqr() {

    }

    public static ResultListFragmentqr newInstance(String id) {
        ResultListFragmentqr fragment = new ResultListFragmentqr();
        catId = id;
        searchTerm = "";
        locationChangeListener = fragment;
        return fragment;
    }

    public static ResultListFragmentqr newInstance(String id, String term) {
        ResultListFragmentqr fragment = new ResultListFragmentqr();
        catId = id;
        searchTerm = term;
        locationChangeListener = fragment;
        return fragment;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.fragment_result_listqr);
        final TextView btnGenDir2 = (TextView) findViewById(R.id.btnGenDir2);
        final Activity activity = this;
        bd = new MyBase3(this);
        btnGenDir2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Escanear QR");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        
        listView = (ListView) findViewById(R.id.resultListView);


        adapter = new CustomListAdapterQR(this, movieList0);
        listView.setAdapter(adapter);
        

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
                if (Conn()){
                    showUpload(ResultListFragmentqr.this,"Upload");
                }else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ResultListFragmentqr.this);
                    builder.setMessage("Para realizar esta operación es necesario que cuente con internet");
                    builder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(false);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(getApplicationContext(),ResultListFragmentqr.class));
            }
        });

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                pos = position;

                RezagoQR t = movieList0.get(pos);
                ZeroQR.ClaveCat = t.getClaveCatastral();
                ZeroQR.Obs = t.getObservacion();
//                Toast.makeText(ResultListFragmentqr.this,String.valueOf(position),Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultListFragmentqr.this);
                builder.setTitle("Borrar").setMessage("Este elemento sera borrado!!").setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (bd.SearchAndDestroy(movieList0.get(position).getId())){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResultListFragmentqr.this);
                            builder1.setMessage("Hecho ! ! !").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),ResultListFragmentqr.class));
                                }
                            });
                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();
                        }else{
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ResultListFragmentqr.this);
                            builder1.setMessage("Vuelva a intentar lo").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder1.create();
                            alertDialog.show();
                        }
            
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    
            }
        });

        getSearchResults();

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();
            } else {
                
                String cad = result.getContents().toString();
                if (!cad.equals("")) {
                    try {
                        //Log.d("Arshh: ",cad);
                        String[] cs = cad.split("/");
                        ZeroQR.ClaveCat = cs[0];
                        ZeroQR.TipoPredio = cs[1];
                        ZeroQR.Idnot1 = cs[2];
                        ZeroQR.Idnot2 = cs[3];
                        startActivity(new Intent(getApplicationContext(), ZeroQR.class));
                        
                        Toast.makeText(this, cs[0] + " " + cs[1] + " " + cs[2] + " " + cs[3], Toast.LENGTH_LONG).show();
                    } catch (Exception D) {
                        Toast.makeText(this, "Codigo QR Incorrecto SPLIT", Toast.LENGTH_LONG).show();
                    }
                    
                } else {
                    Toast.makeText(this, "Codigo QR Incorrecto", Toast.LENGTH_LONG).show();
                }
                
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    
    public void showUpload(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        ((TextView) view.findViewById(R.id.bodyTV)).setText("¿Desea subir la informacion de los predios verificados?");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        UploadInfo();
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

    public void UploadInfo() {

        String[] campos = new String[] {"_id", "ClaveCatastral","TipoPredio","IdBrigadista","Lat","Lng","Fecha","Hora","Foto","Observacion","IdNot1","IdNot2"};
        Cursor c = bd.getReadableDatabase().query("reztemp", campos, null, null, null, null, null);

             //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
Toast.makeText(getBaseContext(),c.getString(3),Toast.LENGTH_LONG).show();
                             JsonUploadInfo(c.getString(0),
                                     c.getString(1),
                                     c.getString(2),
                                     c.getString(3),
                                     c.getString(4),
                                     c.getString(5),
                                     c.getString(6),
                                     c.getString(7),
                                     c.getString(8),
                                     c.getString(9),
                                     c.getString(10),
                                     c.getString(11)
                                     );

                             DelList(c.getString(0));


            } while(c.moveToNext());
        }

        getSearchResults();
        hidePDialog();
    }

    public void DelList(String id) {
        SQLiteDatabase db = bd.getReadableDatabase();
        db.delete("reztemp", "_id=" + id, null);
        db.close();
    }
    
    
    private void initResultList() {


    }





    private String SelectImg(String ruta) {
        Bitmap bm = BitmapFactory.decodeFile(ruta);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeBytes(ba);
    }

    private void JsonUploadInfo(String Id,
                                String ClaveCatastral,
                                String TipoPredio,
                                String IdBrigadista,
                                String Lat,
                                String Lng,
                                String Fecha,
                                String Hora,
                                String RFoto,
                                String Observ,
                                String IdNot1,
                                String IdNot2
                                ) {

        HashMap<String, String> map = new HashMap<>();
        map.put("token", Const.Token);
        map.put("cc",ClaveCatastral);
        map.put("tp",TipoPredio);
        map.put("idb", IdBrigadista);
        map.put("lt", Lat);
        map.put("lng", Lng);
        map.put("fc", Fecha);
        map.put("hr", Hora);
        map.put("obs", Observ);
        map.put("not1", IdNot1);
        map.put("not2", IdNot2);
        Log.d("RFoto", RFoto);


        if(!RFoto.equals("")){
        map.put("ImageFoto", ClaveCatastral + "_" + TipoPredio  + ".jpg");
        map.put("base641", SelectImg(RFoto));
        }else{
            map.put("ImageFoto", "");
        }

        JSONObject object = new JSONObject(map);

        Log.d("TG", object.toString());
        String URL=null;
        if (getPreferenceString(this,Const.SP_Links).equals("0")){
            URL= LinksURL.URLFragmentQR;
        }else if (getPreferenceString(this,Const.SP_Links).equals("1")){
            URL= LinksURL.URLFragmentQR1;
        }
        JsonObjectRequest Req = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String r = response.get("resp").toString();
                            Log.d("TAG", "Response Server ID: " + r);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("TAG", "Response Server: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("TAG", "Error Volley: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Log.d("TAG", "1");
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                            Log.d("TAG", "2");
                        } else if (error instanceof ServerError) {
                            //TODO
                            Log.d("TAG", "3");
                        } else if (error instanceof NetworkError) {
                            //TODO
                            Log.d("TAG", "4");
                        } else if (error instanceof ParseError) {
                            //TODO
                            Log.d("TAG", "5");
                        }
                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }

        };

        Req.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().addToRequestQueue(Req,tag_json_obj);

    }




    private static void getSearchResults() {

        movieList0.clear();
        String[] campos = new String[] {"_id", "ClaveCatastral","TipoPredio","IdBrigadista","Lat","Lng","Fecha","Hora","Foto","Observacion", "IdNot1", "IdNot2"};

        Cursor c = bd.getReadableDatabase().query("reztemp", campos, null, null, null, null, null);

//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Log.d(TAG, codigo);
                RezagoQR rz = new RezagoQR();
                rz.setId(c.getString(0));
                rz.setClaveCatastral(c.getString(1));
                rz.setTipoPredio(c.getString(2));
                rz.setIdBrigadista(c.getString(3));
                rz.setLat(c.getString(4));
                rz.setLng(c.getString(5));
                rz.setFecha(c.getString(6));
                rz.setHora(c.getString(7));
                rz.setFoto(c.getString(8));
                rz.setObservacion(c.getString(9));
                rz.setIdNot1(c.getString(10));
                rz.setIdNot2(c.getString(11));
                movieList0.add(rz);
            } while(c.moveToNext());
        }

        adapter.notifyDataSetChanged();
        hidePDialog();
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == RESULT_ACTION) {

        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == RESULT_ACTION) {
            finish();
        }
    }

    //! detect the location change by user from search filter on actionbar
    @Override
    public void onLocationChange() {
        if (UtilMethods.isConnectedToInternet(this)) {
            //getSearchResults(searchTerm);// call for live use
            //!calling for demo updates
            Collections.shuffle(searchResultList);
            ((ResultListAdapter) resultListView.getAdapter()).notifyDataSetChanged();

        } else {
            internetConnectionListener = (InternetConnectionListener) ResultListFragmentqr.this;
            showNoInternetDialog(this, internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), RESULT_ACTION);
        }
    }


    private static void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    // callback interface listen by HomeActivity to detect user click on business
    public interface ResultListCallbacks {
        void onResultItemSelected(Item itemDetails);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        isHomeOpened = false;
        isResultListFragmentOpened = true;
     /*   if (UtilMethods.isConnectedToInternet(this)) {

               // initResultList();

        } else {

            internetConnectionListener = (InternetConnectionListener) ResultListFragmentqr.this;
            showNoInternetDialog(this, internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), RESULT_ACTION);
        }*/
        
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
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
    
    public boolean Conn() {
        
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        
        if (mWifi.isConnected()) {
            return true;
        } else {
            return false;
        }
        
    }
}
