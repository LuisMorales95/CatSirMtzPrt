package com.Mezda.Catastro.model;

/**
 * @author Audacity IT Solutions Ltd.
 * @class District
 * @brief data structure class used by HomeActivity
 */

public class TerminoCons {

    private String id;
    private String clave;
    private String termino;
    private String coeficiente;
    private String anual;

    public TerminoCons(String id, String clave, String termino, String coeficiente, String anual) {
        this.id = id;
        this.clave = clave;
        this.termino = termino;
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

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
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
//        return " "+termino+" Coeficiente: "+coeficiente+ " Anual: "+anual;
        return " "+clave+".- "+termino;
    }
}
