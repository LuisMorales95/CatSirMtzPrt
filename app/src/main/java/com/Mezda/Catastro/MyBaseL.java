package com.Mezda.Catastro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.Mezda.Catastro.util.UtilMethods.savePreference;

/**
 * Created by Black Swan on 19/12/2017.
 */

public class MyBaseL extends SQLiteOpenHelper {

    private static final int VERSION_BASEDATOS = 1;
   // public static SQLiteDatabase SQ ;

    // Nombre de nuestro archivo de base de datos
    public static final String NOMBRE_BASEDATOS = "catprzll.db";


    private static final String TABLA_REZ = "CREATE TABLE login" +
            "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "Usuario TEXT, " +
            "Pass TEXT, " +
            "IdBrig TEXT, " +
            "IdPerson TEXT, " +
            "Token TEXT)";


    // CONSTRUCTOR de la clase
    public MyBaseL(Context context) {
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




    public void InsertLogin(String Us, String Pass, String Brig, String Per, String Tk) {
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            ContentValues valores = new ContentValues();
            valores.put("Usuario", Us);
            valores.put("Pass", Pass);
            valores.put("IdBrig", Brig);
            valores.put("IdPerson", Per);
            valores.put("Token", Tk);
            long d = db.insert("login", null, valores);
            System.out.println(d);
            db.close();
        }
    }






    public boolean UserExist(String IdPerson){
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperar = {"Usuario", "Pass", "IdBrig", "IdPerson", "Token"};
        Cursor c = db.query("login", valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        do {
            //Tareas contactos = new Tareas(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            if (c.moveToFirst()){
                if(c.getString(3).equals(IdPerson)){
                    return true;
                }
            }

        } while (c.moveToNext());
        db.close();
        c.close();
        return false;
    }

    public void UpdateLg(String IdPerson, String Token) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("Token", Token);
        db.update("login", valores, "IdPerson=" +IdPerson , null);
        db.close();
    }

    public boolean Loginz(String User, String Pass, Context context){
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperar = {"Usuario", "Pass", "IdBrig", "IdPerson", "Token"};
        Cursor c = db.query("login", valores_recuperar, null, null, null, null, null, null);
        c.moveToFirst();
        do {
            //Tareas contactos = new Tareas(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4));
            if(c.getString(0).equals(User) && c.getString(1).equals(Pass)){
                savePreference(context, Const.SP_idpersona, Const.IdPerson = c.getString(3));
                savePreference(context, Const.SP_user, Const.User = c.getString(0));
                savePreference(context, Const.SP_token, Const.Token = c.getString(4));
                savePreference(context, Const.SP_idbrigada, Const.IdBrig = c.getString(2));
                /*Const.IdPerson = c.getString(3);
                Const.User = c.getString(0);
                Const.Token = c.getString(4);
                Const.IdBrig = c.getString(2);*/
                return true;
            }
        } while (c.moveToNext());
        db.close();
        c.close();
        return false;
    }
    
}
