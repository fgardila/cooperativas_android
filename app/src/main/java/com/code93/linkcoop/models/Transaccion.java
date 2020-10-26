package com.code93.linkcoop.models;

import java.io.Serializable;
import java.util.List;

public class Transaccion implements Serializable {

    String nameTrans;
    List<DataTransaccion> dataTrans;

    public Transaccion() {
    }

    public Transaccion(String nameTrans) {
        this.nameTrans = nameTrans;
    }

    public Transaccion(String nameTrans, List<DataTransaccion> dataTrans) {
        this.nameTrans = nameTrans;
        this.dataTrans = dataTrans;
    }

    public String getNameTrans() {
        return nameTrans;
    }

    public void setNameTrans(String nameTrans) {
        this.nameTrans = nameTrans;
    }

    public List<DataTransaccion> getDataTrans() {
        return dataTrans;
    }

    public void setDataTrans(List<DataTransaccion> dataTrans) {
        this.dataTrans = dataTrans;
    }
}
