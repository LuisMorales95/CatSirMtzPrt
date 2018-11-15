package com.Mezda.Catastro.model;

/**
 * @author Audacity IT Solutions Ltd.
 * @class District
 * @brief data structure class used by HomeActivity
 */

public class EstadoCons {

    private String id;
    private String clave;
    private String estado;
    private String coeficiente;
    private String anual;

    public EstadoCons(String id, String clave, String estado, String coeficiente, String anual) {
        this.id = id;
        this.clave = clave;
        this.estado = estado;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
//        return " "+estado+" Coeficiente: "+coeficiente+ " Anual: "+anual;
        return " "+clave+".- "+estado;
    
    }
}
