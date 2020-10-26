package com.code93.linkcoop;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ToolsXML extends Activity {

    static XmlSerializer serializer;
    static FileOutputStream fileos = null;
    public static String gReverse,gSettle,gUpdate;

    public static void createXML(String name) {

        serializer = Xml.newSerializer();
        File newxmlfile;
        if(Build.MODEL.equals("NEW9220")) {
            newxmlfile = new File(Environment.getExternalStorageDirectory().getPath() + "/" + name + ".xml");
        }
        else {
            newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()  , name + ".xml");
        }

        try {
            newxmlfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            fileos = new FileOutputStream(newxmlfile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {

            serializer.setOutput(fileos, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void packXML(String reversal, String settle, String update) {

        createXML("ConfigDF");

        try {
            serializer.startTag(null, "Configuration");
            serializer.startTag(null, "MdmInstall");

            setTag("isReversal", reversal);
            setTag("isSettle", settle);
            setTag("initAuto", update);

            serializer.endTag(null, "MdmInstall");
            serializer.endTag(null, "Configuration");

            serializer.endDocument();
            serializer.flush();
            fileos.close();

            read();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void setTag(String tag, String value) {
        if (value != null) {
            try {
                serializer.startTag(null, tag);
                serializer.text(value);
                serializer.endTag(null, tag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void read() {

        File ruta_sd = Environment.getExternalStorageDirectory();
        String reverse = null,
                settle = null,
                udpate = null;

        File file = new File(ruta_sd.getAbsolutePath(), "/ConfigDF.xml");

        if (file.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {

                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                Element documentElement = document.getDocumentElement();
                NodeList sList = documentElement.getElementsByTagName("MdmInstall");

                if (sList != null && sList.getLength() > 0) {
                    for (int i = 0; i < sList.getLength(); i++) {
                        Node node = sList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {

                            node.getAttributes();
                            Element e = (Element) node;
                            NodeList listElements = e.getElementsByTagNameNS(node.getNamespaceURI(), node.getLocalName());

                            //lee cada elemento del state
                            for (int b = 0; b < listElements.getLength(); b++) {

                                String nameEl = listElements.item(b).getNodeName();
                                try {
                                    if (nameEl.equals("isReversal")) {
                                        reverse = listElements.item(b).getTextContent();
                                    }
                                    if (nameEl.equals("isSettle")) {
                                        settle = listElements.item(b).getTextContent();
                                    }
                                    if (nameEl.equals("initAuto")) {
                                        udpate = listElements.item(b).getTextContent();
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();

                                }

                            }
                            if (reverse != null && settle != null && udpate != null) {
                                gReverse=reverse;
                                gSettle=settle;
                                gUpdate = udpate;
                            } else {
                                packXML("0", "0","0");
                            }

                        }
                    }
                }
            } catch (Exception e) {
                createXML("ConfigDF");
            }
        } else {
            packXML("0", "0","0");
        }

    }
}
