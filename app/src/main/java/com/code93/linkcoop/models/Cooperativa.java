package com.code93.linkcoop.models;

import java.io.Serializable;

public class Cooperativa implements Serializable {

    String nombreCoop;
    String urlImgCoop;
    int idDrawable;

    public Cooperativa() {
    }

    public Cooperativa(String nombreCoop, String urlImgCoop) {
        this.nombreCoop = nombreCoop;
        this.urlImgCoop = urlImgCoop;
    }

    public Cooperativa(String nombreCoop, int idDrawable) {
        this.nombreCoop = nombreCoop;
        this.idDrawable = idDrawable;
    }

    public Cooperativa(String nombreCoop, String urlImgCoop, int idDrawable) {
        this.nombreCoop = nombreCoop;
        this.urlImgCoop = urlImgCoop;
        this.idDrawable = idDrawable;
    }

    public String getNombreCoop() {
        return nombreCoop;
    }

    public void setNombreCoop(String nombreCoop) {
        this.nombreCoop = nombreCoop;
    }

    public String getUrlImgCoop() {
        return urlImgCoop;
    }

    public void setUrlImgCoop(String urlImgCoop) {
        this.urlImgCoop = urlImgCoop;
    }

    public int getIdDrawable() {
        return idDrawable;
    }

    public void setIdDrawable(int idDrawable) {
        this.idDrawable = idDrawable;
    }
}
