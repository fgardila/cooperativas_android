package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import com.code93.linkcoop.DataField;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.adapters.MenuCoopAdapter;
import com.code93.linkcoop.models.Cooperativa;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    ArrayList<Cooperativa> cooperativas;
    RecyclerView rvCooperativas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cooperativas = new ArrayList<>();
        rvCooperativas = findViewById(R.id.rvCooperativas);
        cargarArrayList();
        MenuCoopAdapter adapterMenus = new MenuCoopAdapter(cooperativas, this);
        adapterMenus.setOnClickCoop((item, position, id) -> {
            Cooperativa selectCoop = cooperativas.get(position);
            //Toast.makeText(MainActivity.this, "Select Coop " + selectCoop.getNombreCoop() , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, TransaccionesActivity.class);
            intent.putExtra("Cooperativa", selectCoop);
            startActivity(intent);
        });
        rvCooperativas.setLayoutManager(new GridLayoutManager(this, 2));
        rvCooperativas.setAdapter(adapterMenus);
    }

    private void cargarArrayList() {
        cooperativas.add(new Cooperativa("Andalucia",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_andalucia.png?alt=media&token=1d368515-75b6-4853-960b-b33fcfa84c77"));
        cooperativas.add(new Cooperativa("La 29",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_la29.png?alt=media&token=3d8d4dff-3a35-477d-bc6d-ecde8ebf21da"));
        cooperativas.add(new Cooperativa("Alianza del Valle",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_alianza.png?alt=media&token=3ee5e42b-d7ba-46e1-92bc-7eff6c68d310"));
        cooperativas.add(new Cooperativa("Cooperativa Policia Nacional",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_cpn_black.png?alt=media&token=38acc228-35f1-4408-b286-d30bacba5d0f"));
        cooperativas.add(new Cooperativa("Cooprogrerso",
                "https://firebasestorage.googleapis.com/v0/b/connectcoop-ecuador.appspot.com/o/logosCoop%2Flogo_cooprogreso.png?alt=media&token=c0bb3ee0-892c-4859-a71b-478d70fa42ad"));

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void prueba(View view) {

        ArrayList<DataField> list = new ArrayList<>();
        list.add(new DataField(Tools.NameFields.bitmap.toString(), "C000010800A000C8"));

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("utf-8", true);
            serializer.startTag("", "request_logon");
            for (DataField dataField : list) {
                serializer.startTag("", dataField.getNameField());
                serializer.text(dataField.getDataField());
                serializer.endTag("", dataField.getNameField());
            }
            serializer.startTag("", "message_code");
            serializer.text("0800");
            serializer.endTag("", "message_code");
            serializer.startTag("", "transaction_code");
            serializer.text("930001");
            serializer.endTag("", "transaction_code");
            serializer.startTag("", "adquirer_date_time");
            serializer.text("2020-05-08 11:12:13");
            serializer.endTag("", "adquirer_date_time");
            serializer.startTag("", "adquirer_sequence");
            serializer.text("0");
            serializer.endTag("", "adquirer_sequence");
            serializer.endTag("", "request_logon");
            serializer.endDocument();
            String result = writer.toString();
            Log.e("XML DATA: ", result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}