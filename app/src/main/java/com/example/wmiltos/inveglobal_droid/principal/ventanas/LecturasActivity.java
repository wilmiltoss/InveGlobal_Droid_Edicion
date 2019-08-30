package com.example.wmiltos.inveglobal_droid.principal.ventanas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wmiltos.inveglobal_droid.PruebasBD.ScannerActivity;
import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.GeneraArchivoActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.LimpiarActivity;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.VisualizarRegistroActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;
import com.google.zxing.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LecturasActivity extends AppCompatActivity {

    private static final String TAG = null;
    TextView txConteo, txMetro, txNivel, txLocacion, txSoporte, txRconteo, txRnroLocacion, txRsoporte,
            txRnivel, txRmetro, txRnroSoporte, nroLecturasL, tvUsuario, tvScanning;
    EditText campoScanning,lecturaRapida;
    Button btnBuscar, btnConteoRapido;
    TextInputLayout panelNormal, panelRapido;
    //del scanner---------------
    ImageButton  btnScanner;
    private ZXingScannerView vistaEscaner;
    //----------------------
    private Cursor fila;
    ConexionSQLiteHelper conn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturas);
        navigationView();
        variables();
        recepcionDatosUbicacion();
        validacionCampoScanning();
        validacionCampoScanning2();

        campoScanning.requestFocus();
        panelRapido.setVisibility(View.GONE);

    }

    public void onClickConteoRapido(View view) {

        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LecturasActivity.this);
        dialogo.setMessage("Activar/Desactivar conteo Rapido?").setTitle("InveGlobal").setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(panelRapido.getVisibility() == View.VISIBLE){ //si es Visible lo pones Gone
                    panelRapido.setVisibility(View.GONE);
                    panelNormal.setVisibility(View.VISIBLE);
                }else{ // si no es Visible, lo pones
                    panelRapido.setVisibility(View.VISIBLE);
                    panelNormal.setVisibility(View.GONE);
                }
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    public void conteoRapido(){
        btnConteoRapido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(panelRapido.getVisibility() == View.VISIBLE){ //si es Visible lo pones Gone
                    panelRapido.setVisibility(View.GONE);
                    panelNormal.setVisibility(View.VISIBLE);
                }else{ // si no es Visible, lo pones
                    panelRapido.setVisibility(View.VISIBLE);
                    panelNormal.setVisibility(View.GONE);
                }
            }
        });
    }


    //NAVEGADORES
    public void navigationView (){
        //declaracion de navegadores *menu inferior
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //*menu scanner
        BottomNavigationView navigation2 = (BottomNavigationView) findViewById(R.id.navigation2);
        navigation2.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener2);

    }
    //1-comprueba el codigo cargado si es existe
    public void comprobarCodigo (){


        try {
            ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "InveStock.sqlite", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            String scanning = campoScanning.getText().toString();
            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_SCANNING + " FROM " + Utilidades.TABLA_MAESTRO +
                    " WHERE " + Utilidades.CAMPO_SCANNING + "='" +scanning+ "'", null);
            fila.moveToFirst();
            tvScanning.setText(fila.getString(0));//muestra los campos en la tx oculta

        }catch (Exception e){
            tvScanning.setText("");
        }
    }
    //2-salto automatico de ventana
    public void validacionCampoScanning (){
        campoScanning.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                sumaRegistrosSQL();
            }
            //Pasar a la sgte ventana automaticamente si el numero de caracteres son las sgtes
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                comprobarCodigo();
                //validacion de salto
                if(     (s.length() == 15) && (tvScanning.getText().length()!=0) ||//que tenga 15 dig y q el campo sea indistinto a 0
                        (s.length() == 14) && (tvScanning.getText().length()!=0) ||//que tenga 14 dig y q el campo sea indistinto a 0
                        (s.length() == 13) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 12) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 11) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 10) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 9)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 8)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 7)  && (tvScanning.getText().length()!=0))
                {
                    envioDatosLectura();//si esta correcto, envia los datos a Lectura

                }else if (s.length()==13){
                    envioDatosLectura();//si esta correcto, envia los datos a Lec
                }
            }
        });
    }

    //2-salto automatico de ventana
    public void validacionCampoScanning2 (){
        lecturaRapida.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                sumaRegistrosSQL();
            }
            //Pasar a la sgte ventana automaticamente si el numero de caracteres son las sgtes
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                comprobarCodigo();
                //validacion de salto
                if(     (s.length() == 15) && (tvScanning.getText().length()!=0) ||//que tenga 15 dig y q el campo sea indistinto a 0
                        (s.length() == 14) && (tvScanning.getText().length()!=0) ||//que tenga 14 dig y q el campo sea indistinto a 0
                        (s.length() == 13) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 12) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 11) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 10) && (tvScanning.getText().length()!=0) ||
                        (s.length() == 9)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 8)  && (tvScanning.getText().length()!=0) ||
                        (s.length() == 7)  && (tvScanning.getText().length()!=0))
                {
                    envioDatosLectura2();//si esta correcto, envia los datos a Lectura

                }else if (s.length()==13){
                    envioDatosLectura2();//si esta correcto, envia los datos a Lec
                }
            }
        });
    }

    private void recepcionDatosUbicacion() {
        //recibe los datos cargado en los campos de UbicacionActivity
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String conteo = miBundle.getString("msjConteo");
            txConteo.setText("Conteo:   " + conteo);

            String metro = miBundle.getString("msjMetro");
            txMetro.setText("Metro:   " + metro);

            String nivel = miBundle.getString("msjNivel");
            txNivel.setText("Nivel:   " + nivel);

            String locacionDescrip = miBundle.getString("msjLocacionDescrip");
            String locacionId = miBundle.getString("msjLocacionId");
            txLocacion.setText("Locacion:  " + locacionDescrip);

            String SoporteId = miBundle.getString("msjIdSoporte");

            String SoporteDescrip = miBundle.getString("msjDescripSoporte");
            String NroSoporte = miBundle.getString("msjNroSoporte");
            txSoporte.setText("Soporte: " + SoporteDescrip + " - " + NroSoporte);


            //captura de los datos recibidos para almacenarlos en la tx
            txRnroLocacion.setText(locacionId); //R1
            txRconteo.setText(conteo); //R2
            txRsoporte.setText(SoporteId); //R3
            txRnroSoporte.setText(NroSoporte); //R4
            //txRidLetraSoporte.setText(LetraSoporte); //R5
            txRnivel.setText(nivel); //R6
            txRmetro.setText(metro); //R7
            String Usuario = miBundle.getString("msjUsuario");
            tvUsuario.setText(Usuario);
        }
    }

    private void variables() {
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        btnBuscar = (Button) findViewById(R.id.btn_buscar);
        btnConteoRapido = (Button) findViewById(R.id.btn_ConteoRapido);
        txConteo = (TextView) findViewById(R.id.txtConteo);
        txMetro = (TextView) findViewById(R.id.txtMetro);
        txNivel = (TextView) findViewById(R.id.txtNivel);
        txLocacion = (TextView) findViewById(R.id.txtLocacion);
        txSoporte = (TextView) findViewById(R.id.txtTipoSoporte);
        nroLecturasL = (TextView) findViewById(R.id.tvNroLecturasL);
        tvUsuario = (TextView)findViewById(R.id.tv_Usuario);
        tvScanning = (TextView)findViewById(R.id.tv_scanning);//auxiliar
        panelNormal=(TextInputLayout) findViewById(R.id.et_panelNormal);
        panelRapido=(TextInputLayout) findViewById(R.id.et_panelRapido);
        //btnScanner=(ImageButton)findViewById(R.id.btn_Scanner);

        campoScanning = (EditText) findViewById(R.id.et_scanningLectura); // campoScanning.requestFocus();
        lecturaRapida = (EditText) findViewById(R.id.et_scanningLecturaRapida);

        //variables de transferencia a la ventana StockActivity
        txRnroLocacion = (TextView) findViewById(R.id.tvRnroLocacion);
        txRconteo = (TextView) findViewById(R.id.tvRconteo);
        txRsoporte = (TextView) findViewById(R.id.tvRsoporte);
        txRnivel = (TextView) findViewById(R.id.tvRnivel);
        txRmetro = (TextView) findViewById(R.id.tvRmetro);
        txRnroSoporte = (TextView) findViewById(R.id.tvRnroSoporte);
    }

    //envia las capturas de los datos ingresados a la ventana StockActivity
    public void onClickBuscar(View view) {
        if (campoScanning.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje
            switch (view.getId()) {
                case R.id.btn_buscar:
                    envioDatosLectura();
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_SHORT).show();
        }
    }

    //Navedor menu inferior
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                  dialogo();
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(LecturasActivity.this, VisualizarRegistroActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(LecturasActivity.this, LimpiarActivity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_scanning:
                    Intent intent4 = new Intent(LecturasActivity.this, ScannerActivity.class);
                    startActivity(intent4);
                    return true;
            }
            return false;
        }
    };

    //Boton Navegador del scanner Camara
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener2
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_scanning:capturaCodigo();
                    return true;
            }
                return false;

        }
    };
    //dialogo antes de salir
    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LecturasActivity.this);
        dialogo.setMessage("Desea salir de la Lectura?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Intent intent = new Intent(LecturasActivity.this, UbicacionActivity.class);
                //startActivity(intent);
                LecturasActivity.this.finish();//finaliza la ventana
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    //suma la cantidad ingresada con lo que ya existe
    public void sumaRegistrosSQL() {
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + Utilidades.TABLA_LECTURAS, null);
        nroLecturasL.setText("Nro.Lecturas:   " + c.getCount());
        db.close();
    }
        //Envio de mensajes de Lecturas al activityStock
        public void envioDatosLectura(){
            Intent miIntent = null;

            String msjScanning = campoScanning.getText().toString();
            String msjEnroLocacion = txRnroLocacion.getText().toString();//E1
            String msjEconteo = txRconteo.getText().toString();//E2
            String msjEsoporte = txRsoporte.getText().toString();//E3
            String msjEnroSoporte = txRnroSoporte.getText().toString();//E4
            String msjEnivel = txRnivel.getText().toString();//E6
            String msjEmetro = txRmetro.getText().toString();//E7
            String msjUsu = tvUsuario.getText().toString();//E7

            miIntent = new Intent(LecturasActivity.this, StockActivity.class);
            Bundle miBundle = new Bundle();
            miBundle.putString("msjScanning", campoScanning.getText().toString());

            miBundle.putString("msjEnroLocacion", txRnroLocacion.getText().toString());//E1
            miBundle.putString("msjEconteo", txRconteo.getText().toString());//E2
            miBundle.putString("msjEsoporte", txRsoporte.getText().toString());//E2
            miBundle.putString("msjEnroSoporte", txRnroSoporte.getText().toString());//E4
            miBundle.putString("msjEnivel", txRnivel.getText().toString());//E6
            miBundle.putString("msjEmetro", txRmetro.getText().toString());//E7
            miBundle.putString("msjUsu", tvUsuario.getText().toString());

            miIntent.putExtras(miBundle);
            startActivity(miIntent);
            campoScanning.setText("");//limpia el campo
            sumaRegistrosSQL();
        }

    //Envio de mensajes de Lecturas al activityStock automatico
    public void envioDatosLectura2(){
        Intent miIntent = null;

        String msjScanning = lecturaRapida.getText().toString();
        String msjEnroLocacion = txRnroLocacion.getText().toString();//E1
        String msjEconteo = txRconteo.getText().toString();//E2
        String msjEsoporte = txRsoporte.getText().toString();//E3
        String msjEnroSoporte = txRnroSoporte.getText().toString();//E4
        String msjEnivel = txRnivel.getText().toString();//E6
        String msjEmetro = txRmetro.getText().toString();//E7
        String msjUsu = tvUsuario.getText().toString();//E7

        miIntent = new Intent(LecturasActivity.this, StockAutomaticoActivity.class);
        Bundle miBundle = new Bundle();
        miBundle.putString("msjScanning", lecturaRapida.getText().toString());

        miBundle.putString("msjEnroLocacion", txRnroLocacion.getText().toString());//E1
        miBundle.putString("msjEconteo", txRconteo.getText().toString());//E2
        miBundle.putString("msjEsoporte", txRsoporte.getText().toString());//E2
        miBundle.putString("msjEnroSoporte", txRnroSoporte.getText().toString());//E4
        miBundle.putString("msjEnivel", txRnivel.getText().toString());//E6
        miBundle.putString("msjEmetro", txRmetro.getText().toString());//E7
        miBundle.putString("msjUsu", tvUsuario.getText().toString());

        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        lecturaRapida.setText("");//limpia el campo
        sumaRegistrosSQL();
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
            setContentView(R.layout.activity_lecturas);
            vistaEscaner.stopCamera();
            campoScanning = (EditText) findViewById(R.id.et_scanningLectura);
            campoScanning.setText(dato);//muestra en la tx

            tvScanning = (TextView) findViewById(R.id.tv_scanning);
            tvScanning.setText(dato);

            //llama nuevamente en esta subClase los metodos iniciales
            navigationView();
            variables();
            recepcionDatosUbicacion();
            validacionCampoScanning();
        }
    }



}

