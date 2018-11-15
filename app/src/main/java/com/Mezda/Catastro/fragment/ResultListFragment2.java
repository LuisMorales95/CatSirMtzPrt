package com.Mezda.Catastro.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.Mezda.Catastro.Base64;
import com.Mezda.Catastro.Const;
import com.Mezda.Catastro.LinksURL;
import com.Mezda.Catastro.MyBase2;
import com.Mezda.Catastro.R;
import com.Mezda.Catastro.VolleySingleton;
import com.Mezda.Catastro.activity.HomeActivity;
import com.Mezda.Catastro.adapter.CustomListAdapter;
import com.Mezda.Catastro.adapter.ResultListAdapter;
import com.Mezda.Catastro.model.Item;
import com.Mezda.Catastro.model.Tareas;
import com.Mezda.Catastro.util.LocationChangeListener;
import com.Mezda.Catastro.util.UtilMethods;
import com.Mezda.Catastro.util.UtilMethods.InternetConnectionListener;

import org.json.JSONArray;
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

public class ResultListFragment2 extends Fragment implements InternetConnectionListener,
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
    //private static final String url = "http://192.168.1.70/JairWS/JSon-All.php";
    //public static String URL = "http://192.168.1.70/Catastro/sync.php?token="+ Const.Token;

    private String tag_json_obj = "jobj_req";
    private static  ProgressDialog pDialog;
    public static int pos = 0;
    public static List<Tareas> movieList0 = new ArrayList<Tareas>();
    private ListView listView;
    private ImageView prevImgView;
    private ImageView nextImgView;
    static MyBase2 bd;
    private static String idlc = "";
    String ba1;
    private static CustomListAdapter adapter;
    private ViewPager imagePager;

    public ResultListFragment2() {

    }

    public static ResultListFragment2 newInstance(String id) {
        ResultListFragment2 fragment = new ResultListFragment2();
        catId = id;
        searchTerm = "";
        locationChangeListener = fragment;
        return fragment;
    }

    public static ResultListFragment2 newInstance(String id, String term) {
        ResultListFragment2 fragment = new ResultListFragment2();
        catId = id;
        searchTerm = term;
        locationChangeListener = fragment;
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (ResultListCallbacks) activity;
        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement ResultListCallbacks.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result_list2, container, false);
        listView = (ListView) rootView.findViewById(R.id.resultListView);
        setHasOptionsMenu(true);

        adapter = new CustomListAdapter(this.getActivity(), movieList0);
        listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
             //           .setAction("Action", null).show();
                showUpload(getActivity(),"Upload");
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(ResultListFragment2.this).attach(ResultListFragment2.this).commit();
            }
        });

        pDialog = new ProgressDialog(this.getContext());

        bd = new MyBase2(getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
               // Toast.makeText(getContext(), pos +"", Toast.LENGTH_LONG).show();
                // Crea el nuevo fragmento y la transacción.

                Tareas t = movieList0.get(pos);
                DetailViewFragment2.Clv = t.getClaveCatast();
                DetailViewFragment2.IdTareaCampo = t.getIdTareaP();
                DetailViewFragment2.IdUsoPredio = Integer.parseInt(t.getIdUsoPred());
                DetailViewFragment2.UsoPredio = t.getUsoPredio();
                DetailViewFragment2.IdPredio = t.getIdPredio();

                Fragment nuevoFragmento = new DetailViewFragment2();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.container, nuevoFragmento);
                transaction.addToBackStack(null);

                // Commit a la transacción
                transaction.commit();

            }
        });


        if (Conn()) {
            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            makeJsonArryReq();
        }else{
            getSearchResults();
        }


        return rootView;
    }


    public boolean Conn() {

        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            return true;
        }else{
            return false;
        }

    }
    public void showUpload(final Activity context, final String heading) {
        AlertDialog dialog = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_dialog, null);
        ((TextView) view.findViewById(R.id.headlineTV)).setText(heading);

        //TextView si = (TextView) view.findViewById(R.id.del_bt_si);
        //TextView no= (TextView) view.findViewById(R.id.del_bt_no);
        ((TextView) view.findViewById(R.id.bodyTV)).setText("¿Desea subir la informacion de los predios verificados?");

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //UpdateTareas();
                        //pDialog.setMessage("Subiendo Informacion...");
                        //pDialog.show();
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


        String[] campos = new String[] {"IdTareaC","Foto","Croquis","Latitud","Longitud","IdUsoPredio","OtroUso","Observacion", "_id"};
        Cursor c = bd.getReadableDatabase().query("levcampo", campos, null, null, null, null, null);

        String[] campos2 = new String[] {"Verif","IdPredio","VD"};

        String[] campos3 = new String[] {"IdPredio","IdEstadoCons","IdTipoCons","IdTerminoCons","Area","Antiguedad","ValorCons","Letra"};

//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {

                Cursor c2 = bd.getReadableDatabase().query("tareas", campos2, "IdTareaP="+c.getString(0), null, null, null, null);

                if (c2.moveToFirst()){
                     do {
                         if(c2.getString(0).equals("1")){

                             JsonUploadInfo(c.getString(0),
                                     c.getString(1),
                                     c.getString(2),
                                     c.getString(3),
                                     c.getString(4),
                                     c.getString(5),
                                     c.getString(6),
                                     c.getString(7),
                                     c2.getString(1),
                                     c2.getString(2)
                                     );


                            /* Cursor c3 = bd.getReadableDatabase().query("levcons", campos3, "IdPredio="+c2.getString(1), null, null, null, null);
                             if (c3.moveToFirst()){
                                 do {

                                     JsonUploadInfoList(c3.getString(0),
                                             c3.getString(1),
                                             c3.getString(2),
                                             c3.getString(3),
                                             c3.getString(4),
                                             c3.getString(5),
                                             c3.getString(6),
                                             c3.getString(7),
                                             c.getString(0));
                                 } while(c3.moveToNext());
                             }*/

                             DelList(c.getString(0));
                         }

                     } while(c2.moveToNext());
                  }


            } while(c.moveToNext());
        }



        getSearchResults();
        hidePDialog();
    }

    public void DelList(String id) {
        SQLiteDatabase db = bd.getReadableDatabase();
        db.delete("tareas", "IdTareaP=" + id, null);
        db.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        isHomeOpened = false;
        isResultListFragmentOpened = true;
        if (UtilMethods.isConnectedToInternet(getActivity())) {

                initResultList();

        } else {

            internetConnectionListener = (InternetConnectionListener) ResultListFragment2.this;
            showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
                    getResources().getString(R.string.no_internet_text),
                    getResources().getString(R.string.retry_string),
                    getResources().getString(R.string.exit_string), RESULT_ACTION);
        }

    }

    private void initResultList() {

        /**
         * json is populating from text file. To make api call use ApiHandler class
         * pass parameter using ContentValues (values)
         *
         * <CODE> ApiHandler handler = new ApiHandler(this, URL_GET_RESULT_LIST_WITH_AD, values);</CODE> <BR>
         * <CODE> handler.doApiRequest(ApiHandler.REQUEST_POST);</CODE> <BR>
         *
         * You will get the response in onSuccessResponse(String tag, String jsonString) method
         * if successful api call has done.
         */

        //String jsonString = loadJSONFromAsset(getActivity(), "get_result_list");
        //parseJson(jsonString);
    }

    private void makeJsonArryReq() {
       // showProgressDialog();
        String URL=null;
        if (getPreferenceString(getActivity(),Const.SP_Links).equals("0")){
            URL= LinksURL.URLSync2;
        }else if (getPreferenceString(getActivity(),Const.SP_Links).equals("1")){
            URL= LinksURL.URLSync3;
        }
        JsonArrayRequest req = new JsonArrayRequest(LinksURL.URLSync2+ getPreferenceString(getContext(),Const.SP_token),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       // Log.d("JODER!!!!", response.toString());

                        try {
                            for(int i=0;i<response.length();i++){
                                Log.d("XXXX-> "+i, response.getJSONObject(i).toString());
                                JSONObject ob = response.getJSONObject(i);


                                bd.InsertTareas(ob.get("IdTareaCampo").toString(),
                                                     ob.get("IdPredio").toString(),
                                                     ob.get("ClaveCT").toString(),
                                                     ob.get("IdUsoPredio").toString(),
                                                     ob.get("UsoPredio").toString(),
                                                     ob.get("IdPersona").toString(),
                                                     ob.get("Propietario").toString(),
                                                     ob.get("DirPro").toString());

                                bd.InsertLCampo(ob.get("IdTareaCampo").toString(),ob.get("IdUsoPredio").toString());

                                if(ob.get("Lista").equals(null)){ continue;}

                                JSONArray arz = (JSONArray) ob.get("Lista");

                                for(int j=0;j<arz.length();j++){
                                    JSONObject op = arz.getJSONObject(j);

                                    bd.InsertPredios(ob.get("IdPredio").toString(),
                                                          ob.get("IdPersona").toString(),
                                                          op.get("IdConstruccion").toString(),
                                                          op.get("Letra").toString(),
                                                          op.get("Area").toString(),
                                                          op.get("Antiguedad").toString(),
                                                          op.get("ValorC").toString(),
                                                          op.get("IdTipoCons").toString(),
                                                          op.get("ClaveTipoCons").toString(),
                                                          op.get("IdEstadoCons").toString(),
                                                          op.get("ClaveEstadoCons").toString(),
                                                          op.get("IdTerminoCons").toString(),
                                                          op.get("ClaveTerminoCons").toString()
                                                         );
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hidePDialog();
                        getSearchResults();

                        //}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error);
               // hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public int getMethod(){
                return Method.GET;
            }

        };


        // Adding request to request queue
        VolleySingleton.getInstance().addToRequestQueue(req,tag_json_obj);


    }



    private String SelectImg(String ruta) {
        Bitmap bm = BitmapFactory.decodeFile(ruta);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeBytes(ba);
    }

    private void JsonUploadInfo(String IdTarea,
                                String RFoto,
                                String RCroquis,
                                String Lat,
                                String Lng,
                                String IdUPredio,
                                String OtroUso,
                                String Observ,
                                final String IdPredio,
                                String VD
                                ) {

        HashMap<String, String> map = new HashMap<>();
        map.put("token", Const.Token);
        map.put("idtc", IdTarea);
        map.put("lt", Lat);
        map.put("lng", Lng);
        map.put("alt", "0");
        map.put("idup", IdUPredio);
        map.put("otro", OtroUso);
        map.put("notif", "0");
        map.put("obs", Observ);
        map.put("VD", VD);

        Log.d("RFoto", RFoto);
        Log.d("RCroquis", RCroquis);

        String path = RFoto;
        String file = path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));

        if(!RCroquis.equals("")){
        map.put("ImageCroq", file + ".jpg");
        map.put("base640", SelectImg(RCroquis));
        }else{
            map.put("ImageCroq", "");
        }

        if(!RFoto.equals("")){
        map.put("ImageFoto", file + ".jpg");
        map.put("base641", SelectImg(RFoto));
        }else{
            map.put("ImageFoto", "");
        }

        JSONObject object = new JSONObject(map);

        Log.d("TG", object.toString());
        String URL=null;
        if (getPreferenceString(getActivity(),Const.SP_Links).equals("0")){
            URL= LinksURL.URLSavelevcamp2;
        }else if (getPreferenceString(getActivity(),Const.SP_Links).equals("1")){
            URL= LinksURL.URLSavelevcamp3;
        }
        JsonObjectRequest Req = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            idlc = response.get("idlc").toString();
                            Log.d("TAG", "Response Server ID: " + idlc);

                            String[] campos3 = new String[] {"IdPredio","IdEstadoCons","IdTipoCons","IdTerminoCons","Area","Antiguedad","ValorCons","Letra"};

                            Cursor c3 = bd.getReadableDatabase().query("levcons", campos3, "IdPredio="+IdPredio, null, null, null, null);
                            if (c3.moveToFirst()){
                                do {

                                    JsonUploadInfoList(c3.getString(0),
                                            c3.getString(1),
                                            c3.getString(2),
                                            c3.getString(3),
                                            c3.getString(4),
                                            c3.getString(5),
                                            c3.getString(6),
                                            c3.getString(7),
                                            idlc);
                                } while(c3.moveToNext());
                            }

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

    private void JsonUploadInfoList(String IdPredio,
                                    String IdEstadoCons,
                                    String IdTipoCons,
                                    String IdTerminoCons,
                                    String Area,
                                    String Antiguedad,
                                    String ValorCons,
                                    String Letra,
                                    String IdLC) {

        HashMap<String, String> map = new HashMap<>();
        map.put("token", Const.Token);
        map.put("idprd", IdPredio);
        map.put("idec",IdEstadoCons);
        map.put("idtic", IdTipoCons);
        map.put("idtec", IdTerminoCons);
        map.put("ar", Area);
        map.put("an", Antiguedad);
        map.put("vc", ValorCons);
        map.put("lt", Letra);
        map.put("idlc", IdLC);

        Log.d("TAGXX", "IDLC: " + idlc);

        JSONObject object = new JSONObject(map);

        Log.d("TG", object.toString());
        String URL=null;
        if (getPreferenceString(getActivity(),Const.SP_Links).equals("0")){
            URL= LinksURL.URLSavelevcons2;
        }else if (getPreferenceString(getActivity(),Const.SP_Links).equals("1")){
            URL= LinksURL.URLSavelevcons3;
        }
        JsonObjectRequest Req = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", "Response Server: " + response);
                        //idlc = "";
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
        String[] campos = new String[] {"IdTareaP","IdPredio","ClaveCatast","IdUsoPred","UsoPredio","Verif","Prop","DirPro"};

        Cursor c = bd.getReadableDatabase().query("Tareas", campos, null, null, null, null, null);

//Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                //Log.d(TAG, codigo);
                Tareas tarea = new Tareas();
                tarea.setClaveCatast(c.getString(2));
                tarea.setIdPredio(c.getString(1));
                tarea.setIdTareaP(c.getString(0));
                tarea.setIdUsoPred(c.getString(3));
                tarea.setUsoPredio(c.getString(4));
                tarea.setVerif(c.getString(5));
                tarea.setPropietario(c.getString(6));
                tarea.setDirPro(c.getString(7));
                movieList0.add(tarea);
            } while(c.moveToNext());
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConnectionEstablished(int code) {
        if (code == RESULT_ACTION) {

        }
    }

    @Override
    public void onUserCanceled(int code) {
        if (code == RESULT_ACTION) {
            getActivity().finish();
        }
    }

    //! detect the location change by user from search filter on actionbar
    @Override
    public void onLocationChange() {
        if (UtilMethods.isConnectedToInternet(getActivity())) {
            //getSearchResults(searchTerm);// call for live use
            //!calling for demo updates
            Collections.shuffle(searchResultList);
            ((ResultListAdapter) resultListView.getAdapter()).notifyDataSetChanged();

        } else {
            internetConnectionListener = (InternetConnectionListener) ResultListFragment2.this;
            showNoInternetDialog(getActivity(), internetConnectionListener, getResources().getString(R.string.no_internet),
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

}
