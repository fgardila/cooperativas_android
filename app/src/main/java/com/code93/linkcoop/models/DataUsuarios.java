package com.code93.linkcoop.models;

import java.util.List;

public class DataUsuarios {

    String key = "";
    List<Usuario> usuarios;

    public DataUsuarios(String key, List<Usuario> usuarios) {
        this.key = key;
        this.usuarios = usuarios;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
