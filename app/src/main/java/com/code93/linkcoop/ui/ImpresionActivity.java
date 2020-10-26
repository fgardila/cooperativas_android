package com.code93.linkcoop.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.widget.ImageView;

import com.code93.linkcoop.MyApp;
import com.code93.linkcoop.R;
import com.code93.linkcoop.Tools;
import com.code93.linkcoop.Toolsss;
import com.code93.linkcoop.models.Cooperativa;
import com.code93.linkcoop.models.DataTransaccion;
import com.code93.linkcoop.models.Transaccion;
import com.squareup.picasso.Picasso;
import com.zcs.sdk.DriverManager;
import com.zcs.sdk.Printer;
import com.zcs.sdk.SdkResult;
import com.zcs.sdk.print.PrnStrFormat;
import com.zcs.sdk.print.PrnTextFont;
import com.zcs.sdk.print.PrnTextStyle;

import java.util.List;

public class ImpresionActivity extends AppCompatActivity {

    private Printer mPrinter;
    private DriverManager mDriverManager = MyApp.sDriverManager;

    Cooperativa cooperativa;
    Transaccion transaccion;
    List<DataTransaccion> elementos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impresion);

        if (Build.MODEL.equals("Z90")) {
            mDriverManager = MyApp.sDriverManager;
            mPrinter = mDriverManager.getPrinter();
        }

        ImageView imgLogoCoop = findViewById(R.id.imgLogoCoop);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cooperativa = (Cooperativa)getIntent().getSerializableExtra("Cooperativa");
            transaccion = (Transaccion)getIntent().getSerializableExtra("Transaccion");
            if (transaccion != null) {
                elementos = transaccion.getDataTrans();
            }
            if (cooperativa != null) {
                if (cooperativa.getUrlImgCoop() != null ) {
                    if (!cooperativa.getUrlImgCoop().trim().isEmpty()) {
                        Picasso.get().load(cooperativa.getUrlImgCoop()).into(imgLogoCoop);
                    } else {
                        if (cooperativa.getIdDrawable() != 0) {
                            imgLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                        }
                    }
                } else {
                    if (cooperativa.getIdDrawable() != 0) {
                        imgLogoCoop.setImageDrawable(getResources().getDrawable(cooperativa.getIdDrawable()));
                    }
                }
            }
        }

        if (Build.MODEL.equals("Z90")) {
            printMatrixText();
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                startActivity(new Intent(ImpresionActivity.this, FinishActivity.class));
            }, 2000);
        }

    }

    private void printMatrixText() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                int printStatus = mPrinter.getPrinterStatus();
                if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toolsss.showDialogError(ImpresionActivity.this, "No hay papel");

                        }
                    });
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
                    mPrinter.setPrintAppendString(" Negocio de Prueba ", format);
                    mPrinter.setPrintAppendString(" 1704142841001 ", format);
                    mPrinter.setPrintAppendString(" Avenidad Quito y Brasilia ", format);
                    mPrinter.setPrintAppendString(" Telefono: 2455555 ", format);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setTextSize(30);
                    format.setStyle(PrnTextStyle.BOLD);
                    mPrinter.setPrintAppendString(" QUITO ", format);
                    format.setTextSize(25);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    mPrinter.setPrintAppendString(" Numero de tarjeta", format);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setTextSize(30);
                    format.setStyle(PrnTextStyle.BOLD);
                    mPrinter.setPrintAppendString("**** **** **** **** 7816", format);
                    format.setAli(Layout.Alignment.ALIGN_CENTER);
                    format.setStyle(PrnTextStyle.NORMAL);
                    format.setTextSize(25);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" Valor de TOTAL:    $25.50 ", format);
                    mPrinter.setPrintAppendString("  ", format);
                    mPrinter.setPrintAppendString(" Test ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    mPrinter.setPrintAppendString(" ", format);
                    printStatus = mPrinter.setPrintStart();
                    if (printStatus == SdkResult.SDK_PRN_STATUS_PAPEROUT) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toolsss.showDialogError(ImpresionActivity.this, "No hay papel");
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(ImpresionActivity.this, FinishActivity.class));
                            }
                        });
                    }
                }
            }
        }).start();
    }
}