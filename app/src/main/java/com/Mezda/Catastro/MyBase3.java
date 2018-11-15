package com.Mezda.Catastro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Black Swan on 19/12/2017.
 */

public class MyBase3 extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
   // public static SQLiteDatabase SQ ;

    // Nombre de nuestro archivo de base de datos
    public static final String NOMBRE_BASEDATOS = "catprz33.db";


    private static final String TABLA_REZ = "CREATE TABLE reztemp" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ClaveCatastral TEXT, " +
            "TipoPredio TEXT, " +
            "IdBrigadista TEXT, " +
            "Lat TEXT, " +
            "Lng TEXT, " +
            "Fecha TEXT, " +
            "Hora TEXT, " +
            "Foto TEXT, " +
            "Observacion TEXT, " +
            "IdNot1 TEXT, " +
            "IdNot2 TEXT)";



    // CONSTRUCTOR de la clase
    public MyBase3(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_REZ);

        //SQ = getWritableDatabase();

        //IsEmpty.EstadoC(getWritableDatabase());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS ");
        onCreate(db);
    }




    public void InsertLCampo(String IdTareaC, String IdUsoPredio) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();

            valores.put("IdTareaC", IdTareaC);
            valores.put("Croquis", "");
            valores.put("Foto", "");
            valores.put("Latitud", "");
            valores.put("Longitud", "");
            valores.put("OtroUso", "");
            valores.put("Notificacion", "");
            valores.put("Observacion", "");
            valores.put("IdUsoPredio", IdUsoPredio);
            long d =db.insert("levcampo", null, valores);
            System.out.println(d);
            db.close();
        }
    }

    public void InsertUsoPredio(int id, String nom, int tlf, String email ) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("_id", id);
            valores.put("nombre", nom);
            valores.put("telefono", tlf);
            valores.put("email", email);
            db.insert("contactos", null, valores);
            db.close();
        }
    }

    public void InsertEstadoCons(int id, String nom, int tlf, String email ) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("_id", id);
            valores.put("nombre", nom);
            valores.put("telefono", tlf);
            valores.put("email", email);
            db.insert("contactos", null, valores);
            db.close();
        }
    }

    public void InsertTipoCons(int id, String nom, int tlf, String email) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("_id", id);
            valores.put("nombre", nom);
            valores.put("telefono", tlf);
            valores.put("email", email);
            db.insert("contactos", null, valores);
            db.close();
        }
    }

    public void InsertTermCons(int id, String nom, int tlf, String email) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            //
            ContentValues valores = new ContentValues();
            valores.put("_id", id);
            valores.put("nombre", nom);
            valores.put("telefono", tlf);
            valores.put("email", email);
            db.insert("contactos", null, valores);
            db.close();
        }
    }

    public  void InsertTareas(String idtp,
                                    String idpr,
                                    String clct,
                                    String idup,
                                    String up,
                                    String idper) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            //valores.put("_id", 0);
            valores.put("IdTareaP", idtp);
            valores.put("IdPredio", idpr);
            valores.put("ClaveCatast", clct);
            valores.put("IdUsoPred", idup);
            valores.put("UsoPredio", up);
            valores.put("Verif", "0");
            valores.put("IdPersona", idper);
            db.insert("tareas", null, valores);
            db.close();
        }
    }

    public  void InsertPredios( String idpre,
                                      String idper,
                                      String idconst,
                                      String letra,
                                      String area,
                                      String ant,
                                      String valor,
                                      String idtc,
                                      String cc,
                                      String idec,
                                      String ce,
                                      String idtec,
                                      String ctec) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            //valores.put("_id", 0);
            valores.put("IdPredio", idpre);
            valores.put("IdPersona", idper);
            valores.put("IdConstr", idconst);
            valores.put("Letra", letra);
            valores.put("Area", area);
            valores.put("Antiguedad", ant);
            valores.put("Valor", valor);
            valores.put("IdTipoCons", idtc);
            valores.put("ClaveCons", cc);
            valores.put("IdEstadoCons", idec);
            valores.put("ClaveEstadoCons", ce);
            valores.put("IdTerminoCons", idtec);
            valores.put("ClaveTerminoCons", ctec);
            db.insert("predios", null, valores);
            db.close();
        }
    }

    public  void InsertLevCons( String idpre,
                                String idtc,
                                String idec,
                                String idtec,
                                //String idlevcampo,
                                String area,
                                String ant,
                                String valor,
                                String letra) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("IdPredio", idpre);
            valores.put("IdTipoCons", idtc);
            valores.put("IdEstadoCons", idec);
            valores.put("IdTerminoCons", idtec);
           // valores.put("IdLevCampo", idlevcampo);
            valores.put("Area", area);
            valores.put("Antiguedad", ant);
            valores.put("ValorCons", valor);
            valores.put("Letra", letra);
            db.insert("levcons", null, valores);
            db.close();
        }
    }




    public List<Tareas> Tarea() {
        SQLiteDatabase db = getReadableDatabase();
        List<Tareas> lista_contactos = new ArrayList<Tareas>();
        String[] valores_recuperar = {"IdTareaP", "IdPredio", "ClaveCatast", "UsoPredio", "IdPersona"};
        Cursor c = db.query("tareas", valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        do {
            Tareas contactos = new Tareas(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            lista_contactos.add(contactos);
        } while (c.moveToNext());
        db.close();
        c.close();
        return lista_contactos;
    }
    public Boolean SearchAndDestroy(String _id){
        int success=0;
        SQLiteDatabase sqLiteDatabase  =getReadableDatabase();
        String[] valores_recuperar ={"_id", "ClaveCatastral", "TipoPredio", "IdBrigadista", "Lat", "Lng",
                "Fecha", "Hora", "Foto", "Observacion", "IdNot1", "IdNot2"};
        Cursor cursor = sqLiteDatabase.query("reztemp",valores_recuperar,null,null,null,null,null,null);
        cursor.moveToFirst();
        do {
            if (cursor.getString(0).equals(_id)){
                Log.e("SQLite-Trans", "Id deleted="+_id);
                success = sqLiteDatabase.delete("reztemp", "_id="+_id,null);
            }
            cursor.getString(0);
        }while (cursor.moveToNext());
        sqLiteDatabase.close();
        cursor.close();
        if (success>0){
            return true;
        }else{
            return false;
        }
    }
}
