package com.code93.linkcoop;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.Transaction;

import org.jetbrains.annotations.NotNull;
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

    public static String requestDeposit(Transaction transaction, Cooperativa cooperativa, String numeroCuenta, String monto, String documento) {
        TokenData tokenData = new TokenData();
        String tokenX4 = tokenData.setToken("X4", documento);

        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "E210010810A050C0"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0200"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), transaction.get_code().trim()));
        listFields.add(new DataElements(Tools.NameFields.reference.toString(), numeroCuenta));
        listFields.add(new DataElements(Tools.NameFields.transaction_amount.toString(), monto));
        listFields.add(new DataElements(Tools.NameFields.commision_amount.toString(), transaction.get_cost().trim()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), cooperativa.get_channel().trim()));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), cooperativa.get_service().trim()));
        listFields.add(new DataElements(Tools.NameFields.source_names.toString(), "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"));
        listFields.add(new DataElements(Tools.NameFields.phone_number.toString(), "0000000000"));
        listFields.add(new DataElements(Tools.NameFields.token_data.toString(), tokenX4));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), cooperativa.get_product().trim()));
        return ToolsXML.createXML("request_deposit", listFields);
    }

    public static String requestWithdrawal(Transaction transaction, Cooperativa cooperativa, String numeroCuenta, String monto, String otp, String documento) {
        TokenData tokenData = new TokenData();
        String tokenX4 = tokenData.setToken("X4", documento);
        String tokenB8 = tokenData.setToken("B8", StringTools.INSTANCE.padright(otp, 20, ' '));
        String token = tokenX4 + tokenB8;

        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "E210010810A050C0"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0200"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), transaction.get_code().trim()));
        listFields.add(new DataElements(Tools.NameFields.reference.toString(), numeroCuenta));
        listFields.add(new DataElements(Tools.NameFields.transaction_amount.toString(), monto));
        listFields.add(new DataElements(Tools.NameFields.commision_amount.toString(), transaction.get_cost().trim()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), cooperativa.get_channel().trim()));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), cooperativa.get_service().trim()));
        listFields.add(new DataElements(Tools.NameFields.source_names.toString(), "XXXXX XXXXX XXXXX"));
        listFields.add(new DataElements(Tools.NameFields.phone_number.toString(), "0000000000"));
        listFields.add(new DataElements(Tools.NameFields.token_data.toString(), token));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), cooperativa.get_product().trim()));
        return ToolsXML.createXML("request_withdrawal", listFields);
    }

    public static String requestGenerate(Transaction transaction, Cooperativa cooperativa, String reference) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "E000010810A00040"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0200"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), transaction.get_code().trim()));
        listFields.add(new DataElements(Tools.NameFields.reference.toString(), reference));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), cooperativa.get_channel().trim()));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), cooperativa.get_service().trim()));
        listFields.add(new DataElements(Tools.NameFields.authorization_code.toString(), "845583"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), cooperativa.get_product().trim()));
        return ToolsXML.createXML("request_generate", listFields);
    }

    public static String requestInquiry(Transaction transaction, Cooperativa cooperativa, String reference, String documento) {
        TokenData tokenData = new TokenData();
        String tokenX4 = tokenData.setToken("X4", documento);

        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "E200010810A050C0"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0200"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), transaction.get_code().trim()));
        listFields.add(new DataElements(Tools.NameFields.reference.toString(), reference));
        listFields.add(new DataElements(Tools.NameFields.transaction_amount.toString(), "0"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), cooperativa.get_channel().trim()));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), cooperativa.get_service().trim()));
        listFields.add(new DataElements(Tools.NameFields.source_names.toString(), "XXXXXX XXXXXXX"));
        listFields.add(new DataElements(Tools.NameFields.phone_number.toString(), "0000000000"));
        listFields.add(new DataElements(Tools.NameFields.token_data.toString(), tokenX4));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), cooperativa.get_product().trim()));
        return ToolsXML.createXML("request_inquiry", listFields);
    }

    public static String requestLogon(String user, String pwd) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010810A0004C"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0800"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930002"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "2"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012000"));
        listFields.add(new DataElements(Tools.NameFields.user_id.toString(), user));
        listFields.add(new DataElements(Tools.NameFields.password.toString(), pwd));
        return ToolsXML.createXML("request_logon", listFields);
    }

    public static String requestLogoff(String user) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010800A00048"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0800"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930003"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "2"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012000"));
        listFields.add(new DataElements(Tools.NameFields.user_id.toString(), user));
        return createXML("request_logoff", listFields);
    }

    public static String requestClose(@NotNull String jsonCierre) {
        ArrayList<DataElements> listFields = new ArrayList<>();
        listFields.add(new DataElements(Tools.NameFields.bitmap.toString(), "C000010810A00240"));
        listFields.add(new DataElements(Tools.NameFields.message_code.toString(), "0200"));
        listFields.add(new DataElements(Tools.NameFields.transaction_code.toString(), "930007"));
        listFields.add(new DataElements(Tools.NameFields.adquirer_date_time.toString(), Tools.getLocalDateTime()));
        listFields.add(new DataElements(Tools.NameFields.adquirer_sequence.toString(), MyApp.sp2.getTraceNo()));
        listFields.add(new DataElements(Tools.NameFields.terminal_id.toString(), "TPOS-1002-000070"));
        listFields.add(new DataElements(Tools.NameFields.channel_id.toString(), "0"));
        listFields.add(new DataElements(Tools.NameFields.service_code.toString(), "0030011001"));
        listFields.add(new DataElements(Tools.NameFields.buffer_data.toString(), jsonCierre));
        listFields.add(new DataElements(Tools.NameFields.product_id.toString(), "012001"));
        return createXML("request_close", listFields);
    }

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
            Log.d("XML DATA: ", writer.toString());
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
