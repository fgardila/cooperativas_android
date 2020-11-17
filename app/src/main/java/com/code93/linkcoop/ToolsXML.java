package com.code93.linkcoop;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ToolsXML extends Activity {

    public static String createXML(String startTag, List<DataElements> listElements) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("utf-8", null);
            serializer.startTag("", startTag);
            for (DataElements dataField : listElements) {
                serializer.startTag("", dataField.getNameField());
                serializer.text(dataField.getDataField());
                serializer.endTag("", dataField.getNameField());
            }
            serializer.endTag("", startTag);
            serializer.endDocument();
            Log.e("XML DATA: ", writer.toString());
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
