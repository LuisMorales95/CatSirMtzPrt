package com.Mezda.Catastro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Black Swan on 19/12/2017.
 */

public class MyBase2 extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
   // public static SQLiteDatabase SQ ;

    // Nombre de nuestro archivo de base de datos
    public static final String NOMBRE_BASEDATOS = "catprz3.db";

    // Sentencia SQL para la creacion de una tabla
    private static final String TABLA_USOPREDIO = "CREATE TABLE usopredio" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "clusop INT, " +
            "usopredio TEXT)";

    private static final String TABLA_ESTADOCONS = "CREATE TABLE estadocons" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "claveestado INT, " +
            "estadoconstr TEXT, " +
            "coeficiente TEXT, " +
            "anual TEXT)";

    private static final String TABLA_TIPOCONS = "CREATE TABLE tipocons" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "clavetipocons INT, " +
            "tipoconstr TEXT, " +
            "valor TEXT, " +
            "anual TEXT)";

    private static final String TABLA_TERMINOCONS = "CREATE TABLE termcons" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "clavetermcons INT, " +
            "termconstr TEXT, " +
            "coeficiente TEXT, " +
            "anual TEXT)";

    private static final String TABLA_TAREAS = "CREATE TABLE tareas" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdTareaP INT, " +
            "IdPredio INT, " +
            "ClaveCatast TEXT, " +
            "IdUsoPred INT, "+
            "UsoPredio TEXT, " +
            "Verif INT, " +
            "IdPersona INT,"+
            "VD TEXT,"+
            "Prop TEXT,"+
            "DirPro TEXT)";

    private static final String TABLA_LIST = "CREATE TABLE predios" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdPredio INT, " +
            "IdPersona INT, " +
            "IdConstr INT, " +
            "Letra TEXT, " +
            "Area TEXT, " +
            "Antiguedad TEXT, " +
            "Valor TEXT, " +
            "IdTipoCons INT, " +
            "ClaveCons TEXT, " +
            "IdEstadoCons INT, " +
            "ClaveEstadoCons TEXT, " +
            "IdTerminoCons INT, " +
            "ClaveTerminoCons TEXT)";

    private static final String TABLA_LEVCAMPO = "CREATE TABLE levcampo" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdTareaC INT, " +
            "Croquis TEXT, " +
            "Foto TEXT, " +
            "Latitud TEXT, " +
            "Longitud TEXT, " +
            "Altitud TEXT, " +
            "IdUsoPredio TEXT, " +
            "OtroUso TEXT, " +
            "Notificacion TEXT, " +
            "Observacion TEXT)";

    private static final String TABLA_LEVCONS = "CREATE TABLE levcons" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "IdPredio INT, " +
            "IdEstadoCons INT, " +
            "IdTipoCons INT, " +
            "IdTerminoCons INT, " +
            //"IdLevCampo INT, " +
            "Area TEXT, " +
            "Antiguedad TEXT, " +
            "ValorCons TEXT, " +
            "Letra TEXT)";

    // CONSTRUCTOR de la clase
    public MyBase2(Context context) {
        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLA_USOPREDIO);
        db.execSQL(TABLA_ESTADOCONS);
        db.execSQL(TABLA_TIPOCONS);
        db.execSQL(TABLA_TERMINOCONS);
        db.execSQL(TABLA_TAREAS);
        db.execSQL(TABLA_LIST);
        db.execSQL(TABLA_LEVCAMPO);
        db.execSQL(TABLA_LEVCONS);
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
                                    String idper,
                                    String propietario,
                                    String dirpro) {
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
            valores.put("VD", "VISITADO");
            valores.put("Prop", propietario);
            valores.put("DirPro", dirpro);
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

/*
    public void modificarCONTACTO(int id, String nom, int tlf, String email){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("_id", id);
        valores.put("nombre", nom);
        valores.put("telefono", tlf);
        valores.put("email", email);
        db.update("contactos", valores, "_id=" + id, null);
        db.close();
    }

    public void borrarCONTACTO(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("contactos", "_id="+id, null);
        db.close();
    }

    public Tareas recuperarCONTACTO(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperar = {"_id", "nombre", "telefono", "email"};
        Cursor c = db.query("contactos", valores_recuperar, "_id=" + id,
                null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        Tareas contactos = new Tareas(c.getInt(0), c.getString(1),
                c.getInt(2), c.getString(3));
        db.close();
        c.close();
        return contactos;
    }



  */

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
}
