package com.Mezda.Catastro;

/**
 * Created by Black Swan on 19/12/2017.
 */

public class Tareas {

    private String idtp;
    private String idpr;
    private String clct;
    private String up;
    private String idper;

    public Tareas(String idtp, String idpr, String clct, String up, String idper) {
        this.idtp = idtp;
        this.idpr = idpr;
        this.clct = clct;
        this.up = up;
        this.idper = idper;
    }

    public String getIdtp() {
        return idtp;
    }

    public void setIdtp(String idtp) {
        this.idtp = idtp;
    }

    public String getClct() {
        return clct;
    }

    public void setClct(String clct) {
        this.clct = clct;
    }

    public String getIdpr() {
        return idpr;
    }

    public void setIdpr(String idpr) {
        this.idpr = idpr;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getIdper() {
        return idper;
    }

    public void setIdper(String idper) {
        this.idper = idper;
    }
}