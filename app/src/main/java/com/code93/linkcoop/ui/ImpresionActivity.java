package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.widget.TextView;

import com.code93.linkcoop.DialogCallback;
import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.models.LogTransacciones;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.print.PrnStrFormat;
import com.zcs.sdk.print.PrnTextFont;
import com.zcs.sdk.print.PrnTextStyle;

public class ImpresionActivity extends AppCompatActivity {

    private Printer mPrinter;
    private DriverManager mDriverManager = MyApp.sDriverManager;

    LogTransacciones logTransacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.MODEL.contains("Z90")) {
            setContentView(R.layout.activity_impresion_pos);
            mDriverManager = MyApp.sDriverManager;
            mPrinter = mDriverManager.getPrinter();
        } else {
            setContentView(R.layout.activity_impresion_telefono);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            logTransacciones = (LogTransacciones) getIntent().getParcelableExtra("logTransacciones");
            if (Build.MODEL.contains("Z90")) {
                printMatrixText(logTransacciones);
            } else {
                mostrarTicket(logTransacciones);
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    startActivity(new Intent(ImpresionActivity.this, FinishActivity.class));
                }, 10000);
            }
        } else {
            Tools.showDialogErrorCallback(this, "No llego informacion de impresion.", new DialogCallback() {
                @Override
                public void onDialogCallback(int value) {
                    startActivity(new Intent(ImpresionActivity.this, FinishActivity.class));
                }
            });
        }

    }

    private void mostrarTicket(LogTransacciones logTransacciones) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("Link COOP\n");
        ticket.append("\n");
        ticket.append(logTransacciones.getComercio().getNombre());
        ticket.append("\n");
        ticket.append(logTransacciones.getComercio().getRuc());
        ticket.append("\n");
        ticket.append(logTransacciones.getComercio().getDireccion());
        ticket.append("\n");
        ticket.append(logTransacciones.getFieldsTrxResponse().getSwitch_date_time());
        ticket.append("\n");
        ticket.append("Switch Sequence ").
                append(logTransacciones.getFieldsTrxResponse().getSwitch_sequence()).
                append(" Adquirer Sequence ").
                append(logTransacciones.getFieldsTrxResponse().getAdquirer_sequence());
        ticket.append("\n");
        ticket.append(logTransacciones.getCooperativa().get_namec().trim());
        ticket.append("\n");
        ticket.append("\n");
        ticket.append("\n");

        switch (logTransacciones.getTransaction().get_namet().trim()) {
            case "RETIRO AHORROS":
                ticket.append(impresionRetiroTelefono(logTransacciones));
                break;
            case "CONSULTA DE SALDOS":
            case "CONSULTA SALDOS CC":
            case "CONSULTA SALDOS AH":
                ticket.append(impresionSaldoTelefono(logTransacciones));
                break;
            case "DEPOSITO AHORROS":
                ticket.append(impresionDepositoTelefono(logTransacciones));
                break;
            default:
                Tools.showDialogError(ImpresionActivity.this, "Transaccion no disponible");
        }

        TextView textView = findViewById(R.id.textViewRecibo);
        textView.setText(ticket.toString());

    }

    private String impresionDepositoTelefono(LogTransacciones logTransacciones) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("\n");

        return ticket.toString();
    }

    private String impresionSaldoTelefono(LogTransacciones logTransacciones) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("Titular : ").
                append(logTransacciones.getFieldsTrxResponse().getTarget_names());
        ticket.append("\n");
        ticket.append("Saldo disponible :    $ ")
                .append(logTransacciones.getFieldsTrxResponse().getAvailable_balance());
        ticket.append("\n");
        ticket.append("Saldo contable :    $ ")
                .append(logTransacciones.getFieldsTrxResponse().getLedger_balance());

        return ticket.toString();
    }

    private String impresionRetiroTelefono(LogTransacciones logTransacciones) {
        StringBuilder ticket = new StringBuilder();
        ticket.append("\n");

        return ticket.toString();

    }

    private void printMatrixText(LogTransacciones logTransacciones) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    runOnUiThread(() ->
                            Tools.showDialogErrorCallback(ImpresionActivity.this, "No hay papel",
                                    value -> printMatrixText(logTransacciones)));
                } else {
                    PrnStrFormat format = new PrnStrFormat();
                    format.setTextSize(35);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setStyle(PrnTextStyle.BOLD);
                    format.setFont(PrnTextFont.DEFAULT);
                    mPrinter.setPrintAppendString("Link COOP", format);
                    format.setTextSize(25);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(logTransacciones.getComercio().getNombre(), format);
                    mPrinter.setPrintAppendString(logTransacciones.getComercio().getRuc(), format);
                    mPrinter.setPrintAppendString(logTransacciones.getComercio().getDireccion(), format);
                    mPrinter.setPrintAppendString(logTransacciones.getFieldsTrxResponse().getSwitch_date_time(), format);
                    mPrinter.setPrintAppendString("Switch Sequence " +
                            logTransacciones.getFieldsTrxResponse().getSwitch_sequence() +
                            " Adquirer Sequence " + logTransacciones.getFieldsTrxResponse().getAdquirer_sequence() , format);
                    format.setTextSize(30);
                    format.setStyle(PrnTextStyle.BOLD);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString(logTransacciones.getCooperativa().get_namec().trim(), format);
                    switch (logTransacciones.getTransaction().get_namet().trim()) {
                        case "RETIRO AHORROS":
                            impresionRetiro(logTransacciones, format);
                            break;
                        case "CONSULTA DE SALDOS":
                        case "CONSULTA SALDOS CC":
                        case "CONSULTA SALDOS AH":
                            impresionSaldo(logTransacciones, format);
                            break;
                        case "DEPOSITO AHORROS":
                            impresionDeposito(logTransacciones, format);
                            break;
                        default:
                            Tools.showDialogError(ImpresionActivity.this, "Transaccion no disponible");
                    }
                    mPrinter.setPrintLine(2);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tools.showDialogErrorCallback(ImpresionActivity.this, "No hay papel", new DialogCallback() {
                                    @Override
                                    public void onDialogCallback(int value) {
                                        printMatrixText(logTransacciones);
                                    }
                                });
                            }
                        });
                    } else {
                        runOnUiThread(() ->
                                startActivity(new Intent(ImpresionActivity.this, FinishActivity.class)));
                    }
                }
            }

            private void impresionDeposito(LogTransacciones logTransacciones, PrnStrFormat format) {
                format.setTextSize(28);
                mPrinter.setPrintAppendString(logTransacciones.getTransaction().get_namet().trim(), format);
                format.setAli(Layout.Alignment.ALIGN_CENTER);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setTextSize(25);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" Monto :    $ " + logTransacciones.getFieldsTrxSend().getTransaction_amount(), format);
                mPrinter.setPrintAppendString("  ", format);
                mPrinter.setPrintAppendString(" Comision :    $ " + logTransacciones.getFieldsTrxSend().getCommision_amount(), format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
            }

            private void impresionSaldo(LogTransacciones logTransacciones, PrnStrFormat format) {
                format.setTextSize(28);
                mPrinter.setPrintAppendString(logTransacciones.getTransaction().get_namet().trim(), format);
                format.setAli(Layout.Alignment.ALIGN_CENTER);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setTextSize(25);
                if (!logTransacciones.getFieldsTrxResponse().getTarget_names().isEmpty()) {
                    mPrinter.setPrintAppendString("Titular : " +
                            logTransacciones.getFieldsTrxResponse().getTarget_names(), format);
                }
                mPrinter.setPrintAppendString(" Saldo disponible :    $ " + logTransacciones.getFieldsTrxResponse().getAvailable_balance(), format);
                mPrinter.setPrintAppendString("  ", format);
                mPrinter.setPrintAppendString(" Saldo contable :    $ " + logTransacciones.getFieldsTrxResponse().getLedger_balance(), format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
            }

            private void impresionRetiro(LogTransacciones logTransacciones, PrnStrFormat format) {
                format.setTextSize(28);
                mPrinter.setPrintAppendString(logTransacciones.getTransaction().get_namet().trim(), format);
                format.setAli(Layout.Alignment.ALIGN_CENTER);
                format.setStyle(PrnTextStyle.NORMAL);
                format.setTextSize(25);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" Monto :    $ " + logTransacciones.getFieldsTrxSend().getTransaction_amount(), format);
                mPrinter.setPrintAppendString("  ", format);
                mPrinter.setPrintAppendString(" Comision :    $ " + logTransacciones.getFieldsTrxSend().getCommision_amount(), format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
                mPrinter.setPrintAppendString(" ", format);
            }
        }).start();
    }
}