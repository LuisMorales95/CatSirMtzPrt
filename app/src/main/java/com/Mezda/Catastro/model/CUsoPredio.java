package com.Mezda.Catastro.model;

/**
 * Created by Black Swan on 06/01/2018.
 */

public class CUsoPredio {
    String Id;
    String Clave;
    String UsoPredio;

    public CUsoPredio(String id, String clave, String usoPredio) {
        Id = id;
        Clave = clave;
        UsoPredio = usoPredio;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getUsoPredio() {
        return UsoPredio;
    }

    public void setUsoPredio(String usoPredio) {
        UsoPredio = usoPredio;
    }

    public String toString(){
        return UsoPredio;
    }
}
