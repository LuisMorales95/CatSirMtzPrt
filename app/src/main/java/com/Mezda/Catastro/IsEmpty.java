package com.Mezda.Catastro;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Black Swan on 19/12/2017.
 */

public class IsEmpty {

    public static boolean UsoPredio(SQLiteDatabase sdb){
        SQLiteDatabase db = sdb;
        String count = "SELECT count(*) FROM usopredio";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean EstadoC(SQLiteDatabase sdb){
        SQLiteDatabase db = sdb;
        String count = "SELECT count(*) FROM estadocons";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean TipoC(SQLiteDatabase sdb){
        SQLiteDatabase db = sdb;
        String count = "SELECT count(*) FROM tipocons";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            return true;
        }else{
            return false;
        }
    }
    public static boolean TerminoC(SQLiteDatabase sdb){
        SQLiteDatabase db = sdb;
        String count = "SELECT count(*) FROM termcons";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if(icount==0){
            return true;
        }else{
            return false;
        }
    }
}
