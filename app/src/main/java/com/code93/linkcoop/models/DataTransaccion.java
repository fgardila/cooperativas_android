package com.code93.linkcoop.models;

import java.io.Serializable;

public class DataTransaccion implements Serializable {

    String name;
    int inputType;
    int maxLength;
    String subTitulo;
    int drawable;
    String value;

    public DataTransaccion() {
    }

    public DataTransaccion(String name, int inputType, int maxLength, String subTitulo, int drawable) {
        this.name = name;
        this.inputType = inputType;
        this.maxLength = maxLength;
        this.subTitulo = subTitulo;
        this.drawable = drawable;
    }

    public DataTransaccion (DataTransaccion dataTransaccion, String value) {
        this.name = dataTransaccion.name;
        this.inputType = dataTransaccion.inputType;
        this.maxLength = dataTransaccion.maxLength;
        this.subTitulo = dataTransaccion.subTitulo;
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
}
