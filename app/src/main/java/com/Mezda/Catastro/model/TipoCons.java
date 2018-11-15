package com.Mezda.Catastro.model;

/**
 * @author Audacity IT Solutions Ltd.
 * @class District
 * @brief data structure class used by HomeActivity
 */

public class TipoCons {

    private String id;
    private String clave;
    private String tipo;
    private String coeficiente;
    private String anual;

    public TipoCons(String id, String clave, String tipo, String coeficiente, String anual) {
        this.id = id;
        this.clave = clave;
        this.tipo = tipo;
        this.coeficiente = coeficiente;
        this.anual = anual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(String coeficiente) {
        this.coeficiente = coeficiente;
    }

    public String getAnual() {
        return anual;
    }

    public void setAnual(String anual) {
        this.anual = anual;
    }

    public String toString(){
//        return tipo + " Anual: " + anual;
        return " "+clave+".- "+ tipo;

    }
}
