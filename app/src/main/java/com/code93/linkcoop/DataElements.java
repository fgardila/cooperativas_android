package com.code93.linkcoop;

public class DataElements {
    public String nameField;
    public String dataField;

    public DataElements(String nameField, String dataField) {
        this.nameField = nameField;
        this.dataField = dataField;
    }

    public String getNameField() {
        return nameField;
    }

    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }
}
