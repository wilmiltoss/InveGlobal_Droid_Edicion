package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.LecturasActivity;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity  {

    private static final String TAG = null;
    private Button btnCapturar,btnEnviar;
    private TextView  locacionSca, reciboDcamara;
    private EditText codigobarra;
    private ZXingScannerView vistaEscaner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        
        locacionSca = (TextView) findViewById(R.id.tvprueba);
        codigobarra = (EditText) findViewById(R.id.txtCodigoBarra);
        btnCapturar = (Button)findViewById(R.id.btnCapturar);
        btnEnviar = (Button)findViewById(R.id.btnEnviar);
        reciboDcamara = (TextView) findViewById(R.id.txRecibDCamara);

        cargarPreferencias();

    }

    private void cargarPreferencias() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String codigo=prefs.getString("codigo","No hay carga");
        locacionSca.setText(codigo);//el evento proceso
    }

    private void guardarPreferncia(){
        SharedPreferences prefs = getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        String locaSca =codigobarra.getText().toString();//almaceno en la variable
        SharedPreferences.Editor editor = prefs.edit();//lee la informacion
        editor.putString("scaner",locaSca);//dato a guardar
        locacionSca.setText(locaSca);//el evento proceso, muestra en la tx

        editor.commit();
    }



    public void Scanner (View view){
        switch (view.getId()) {
            case R.id.btnCapturar:capturaCodigo();
                break;
            case R.id.btnEnviar:guardarPreferncia();
                break;
        }
    }

    public void capturaCodigo(){

            vistaEscaner = new ZXingScannerView(this);
            vistaEscaner.setResultHandler(new escanearAqui());
            setContentView(vistaEscaner);
            vistaEscaner.startCamera();

    }

    //BOTON SCANNER -- llama a la camara
    class escanearAqui implements ZXingScannerView.ResultHandler{

            @Override
            public void handleResult(Result rawResult) {
                String dato = rawResult.getText();
                setContentView(R.layout.activity_scanner);
                vistaEscaner.stopCamera();
                //enviar el resultado
                codigobarra = (EditText) findViewById(R.id.txtCodigoBarra);
                codigobarra.setText(dato);//muestra en la tx

                Intent miIntent = null;
                String msjCamaraScanning = codigobarra.getText().toString();
                miIntent = new Intent(ScannerActivity.this, LecturasActivity.class);
                //envio de los mensajes a la otra pantalla
                Bundle miBundle = new Bundle();
                miBundle.putString("msjCamaraScanning", codigobarra.getText().toString());
                miIntent.putExtras(miBundle);
                startActivity(miIntent);
                sonidoScanner();
            }
    }

    public void recibirScannerCamara() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String camaraScanning = miBundle.getString("msjCamaraScanning");
            reciboDcamara.setText(camaraScanning);
        }
    }

    public void sonidoScanner () {

        try {
            Uri notificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notificacion);
            r.play();

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

/*
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnlistar:
                Intent intent = new Intent(this, SimpleScannerActivity.class);
                startActivity(intent);
        }
    }*/
    }


}
