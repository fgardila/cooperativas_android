package com.code93.linkcoop.persistence.models;

import java.io.Serializable;

public class DataTransaccion implements Serializable {

    String name;
    int inputType;
    int maxLength;
    String subTitulo;
    int drawable;
    TipoDato tipo;
    String value;

    public DataTransaccion() {
    }

    public DataTransaccion(String name, int inputType, int maxLength, String subTitulo, int drawable, String value, TipoDato tipo) {
        this.name = name;
        this.inputType = inputType;
        this.maxLength = maxLength;
        this.subTitulo = subTitulo;
        this.drawable = drawable;
        this.value = value;
        this.tipo = tipo;
    }

    public DataTransaccion(String name, int inputType, int maxLength, String subTitulo, TipoDato tipo, int drawable) {
        this.name = name;
        this.inputType = inputType;
        this.maxLength = maxLength;
        this.subTitulo = subTitulo;
        this.drawable = drawable;
        this.tipo = tipo;
    }

    public DataTransaccion (DataTransaccion dataTransaccion, String value) {
        this.name = dataTransaccion.name;
        this.inputType = dataTransaccion.inputType;
        this.maxLength = dataTransaccion.maxLength;
        this.subTitulo = dataTransaccion.subTitulo;
        this.tipo = dataTransaccion.tipo;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getSubTitulo() {
        return subTitulo;
    }

    public void setSubTitulo(String subTitulo) {
        this.subTitulo = subTitulo;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }

    public enum TipoDato{
        CEDULA, MONTO, OTP, EMAIL, OTRO, IP, KEY
    }
}
