package com.example.wmiltos.inveglobal_droid.principal.ventanas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Locacion;
import com.example.wmiltos.inveglobal_droid.entidades.tablas.Soportes;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.GeneraArchivoActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.LimpiarActivity;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.VisualizarRegistroActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UbicacionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextMessage;

    EditText  campoLocacionId, campoLocacionDescripcion, campoIdSoporte, campoDescripSoporte;

    TextView txLocacion, txDescripcion, txSoporte, txDescripcionSoporte, txnombreEquipo, txtUsuario,
            campoConteo,campoMetro, campoNivel, campoNroSoporte;
    Spinner comboLocacion;
    Spinner comboSoporte;
    ArrayList<String> listaLocacion;
    ArrayList<Locacion> locacionList;
    ArrayList<String> listaSoporte;
    ArrayList<Soportes> soporteList;
    ConexionSQLiteHelper conn;
    Button btnHecho, btnConteo,btnMetro,btnNivel, btnNroSoporte;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        //Icono en el action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        variables();
        consultarListaLocacion();
        consultarListaSoporte();
        recepcionDatosUsuario();

        //botones de NumberPicker
        btnMetro = (Button) findViewById(R.id.btn_Metro);
        btnMetro.setOnClickListener(this);

        btnConteo = (Button) findViewById(R.id.btn_conteo);
        btnConteo.setOnClickListener(this);

        btnNivel = (Button) findViewById(R.id.btn_nivel);
        btnNivel.setOnClickListener(this);

        btnNroSoporte = (Button) findViewById(R.id.btn_nroSoporte);
        btnNroSoporte.setOnClickListener(this);

        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter
                (this, android.R.layout.simple_spinner_item, listaLocacion);
        ArrayAdapter<CharSequence> adaptadorSp = new ArrayAdapter
                (this, android.R.layout.simple_spinner_item, listaSoporte);

        //adaptador de los spinner
        comboLocacion.setAdapter(adaptador);
        comboSoporte.setAdapter(adaptadorSp);

        //captura de datos del spinner
        comboLocacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //   if (position ==0){
                txLocacion.setText(locacionList.get(position).getId_locacion().toString());
                txDescripcion.setText(locacionList.get(position).getDescripcion().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        comboSoporte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                txSoporte.setText(soporteList.get(position).getId_soporte().toString());
                txDescripcionSoporte.setText(soporteList.get(position).getDescripcion().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void variables() {
        //conexion
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);

        btnHecho = (Button) findViewById(R.id.btn_hecho);

        campoConteo = (TextView) findViewById(R.id.tv_conteo);//1
        campoNivel = (TextView) findViewById(R.id.tv_nivel);//4
        campoMetro = (TextView) findViewById(R.id.tv_metro);//
        campoNroSoporte = (TextView) findViewById(R.id.tv_nro_soporte);


        comboLocacion = (Spinner) findViewById(R.id.sp_locacion);
        comboSoporte = (Spinner) findViewById(R.id.sp_tipoSoporte);
        txnombreEquipo = (TextView) findViewById(R.id.text_nombreEquipoL);
        txtUsuario = (TextView) findViewById(R.id.text_usuario);


        //variables de lecturas de los txt capturados de los Spinner
        txLocacion = (TextView) findViewById(R.id.txtIdlocacion);//2
        txDescripcion = (TextView) findViewById(R.id.txtDescripcionloca);
        txSoporte = (TextView) findViewById(R.id.txtIdSoporte);//3
        txDescripcionSoporte = (TextView) findViewById(R.id.txtDescripSoporte);


        //de los spinner
        campoLocacionId = (EditText) findViewById(R.id.txtIdlocacion);
        campoLocacionDescripcion = (EditText) findViewById(R.id.txtDescripcionloca);
        campoIdSoporte = (EditText) findViewById(R.id.txtIdSoporte);
        campoDescripSoporte = (EditText) findViewById(R.id.txtDescripSoporte);
    }

    //spinner Locacion
    private void consultarListaLocacion() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Locacion locales = null;
        locacionList = new ArrayList<Locacion>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_LOCACION, null);

        while (cursor.moveToNext()) {
            locales = new Locacion();
            locales.setId_locacion(cursor.getInt(0));
            locales.setDescripcion(cursor.getString(1));

            Log.i("id", locales.getId_locacion().toString());
            Log.i("nombre", locales.getDescripcion());

            locacionList.add(locales);
        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaLocacion = new ArrayList<String>();
        //listaLocacion.add("Seleccione");

        for (int i = 0; i < locacionList.size(); i++) {
            listaLocacion.add(locacionList.get(i).getId_locacion() + " - " + locacionList.get(i).getDescripcion());
        }
    }

    //spinner tipo soporte
    private void consultarListaSoporte() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Soportes soporte = null;
        soporteList = new ArrayList<Soportes>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_SOPORTE, null);

        while (cursor.moveToNext()) {
            soporte = new Soportes();
            soporte.setId_soporte(cursor.getInt(0));
            soporte.setDescripcion(cursor.getString(1));
            soporte.setSubdivisible(cursor.getInt(2));

            Log.i("id", soporte.getId_soporte().toString());
            Log.i("descripcion", soporte.getDescripcion());
            Log.i("subdivisible", soporte.getSubdivisible().toString());

            soporteList.add(soporte);

        }
        obtenerListaSoporte();
    }

    private void obtenerListaSoporte() {
        listaSoporte = new ArrayList<String>();

        for (int i = 0; i < soporteList.size(); i++) {
            listaSoporte.add(soporteList.get(i).getId_soporte() + " - " + soporteList.get(i).getDescripcion());
        }
    }

    //capturar los datos de los campos EditText para envio a LecturasActivity
    public void onClickHecho(View view) {
        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.btn_hecho:
                String msjConteo = campoConteo.getText().toString();
                String msjMetro = campoMetro.getText().toString();
                String msjNivel = campoNivel.getText().toString();
                String msjLocacionId = campoLocacionId.getText().toString();
                String msjLocacionDescrip = campoLocacionDescripcion.getText().toString();
                String msjIdSoporte = campoIdSoporte.getText().toString();
                String msjDescripSoporte = campoDescripSoporte.getText().toString();
                String msjNroSoporte = campoNroSoporte.getText().toString();
                String msjUsuario = txtUsuario.getText().toString();

                miIntent = new Intent(UbicacionActivity.this, LecturasActivity.class);
                Bundle miBundle = new Bundle();
                miBundle.putString("msjConteo", campoConteo.getText().toString());
                miBundle.putString("msjMetro", campoMetro.getText().toString());
                miBundle.putString("msjNivel", campoNivel.getText().toString());
                miBundle.putString("msjLocacionId", campoLocacionId.getText().toString());
                miBundle.putString("msjLocacionDescrip", campoLocacionDescripcion.getText().toString());
                miBundle.putString("msjIdSoporte", campoIdSoporte.getText().toString());
                miBundle.putString("msjDescripSoporte", campoDescripSoporte.getText().toString());
                miBundle.putString("msjNroSoporte", campoNroSoporte.getText().toString());
                miBundle.putString("msjUsuario", txtUsuario.getText().toString());

                miIntent.putExtras(miBundle);
                startActivity(miIntent);//abre la sgte ventana
               // UbicacionActivity.this.finish();//finaliza la ventana anterior
                break;
        }
    }

    private void registrarDatosUbicacionSQL() {
        //CONEXION

        //ABRIR LA BD PARA EDITARLO
        SQLiteDatabase db = conn.getWritableDatabase();
        //CON SENTENCIA SQL
        String insert = "INSERT INTO " + Utilidades.TABLA_LECTURAS + "("
                + Utilidades.CAMPO_NRO_CONTEO + ","    //1
                + Utilidades.CAMPO_ID_LOCACION_L + "," //2
                + Utilidades.CAMPO_ID_SOPORTE_L + "," //3
                + Utilidades.CAMPO_NIVEL + ","        //4
                + Utilidades.CAMPO_METRO + ")" +       //5
                "VALUES (" + campoConteo.getText().toString() + ", '"
                + txLocacion.getText().toString() + "','"
                + txSoporte.getText().toString() + "','"
                + campoNivel.getText().toString() + "','"
                + campoMetro.getText().toString() + "')";

        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(), "Ubicacion Registrada", Toast.LENGTH_SHORT).show();

        db.execSQL(insert);

        db.close();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   dialogo();
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent2 = new Intent(UbicacionActivity.this, VisualizarRegistroActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(UbicacionActivity.this, LimpiarActivity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };

    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(UbicacionActivity.this);
        dialogo.setMessage("Desea salir del sistema?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(UbicacionActivity.this, LoginActivity.class);
                startActivity(intent);
                UbicacionActivity.this.finish();//finaliza la ventana

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


    private void registrarEquipo() {
        //CONEXION
        SQLiteDatabase db = conn.getWritableDatabase();

        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        txnombreEquipo.setText(deviceName);

        //CON SENTENCIA SQL
        String insert = "INSERT INTO " + Utilidades.TABLA_LECTURAS + "("
                + Utilidades.CAMPO_ID_LOCACION_L + ")" +
                "VALUES ('" + deviceName + "')";

        db.execSQL(insert);
        db.close();
    }

    private void recepcionDatosUsuario() {
        //recibe los datos cargado en los campos de Login
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String usuario = miBundle.getString("msjUsuario");
            txtUsuario.setText(usuario);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conteo:numberPickerDialogoConteo();
                break;
            case R.id.btn_Metro:numberPickerDialogoMetro();
                break;
            case R.id.btn_nivel:numberPickerDialogoNivel();
                break;
            case R.id.btn_nroSoporte:numberPickerDialogoNroSoporte();
                break;
        }
    }

    //numberPickerDialogo de Conteo
    private void numberPickerDialogoConteo(){

        NumberPicker myNumberPickerConteo = new NumberPicker(this);
        myNumberPickerConteo.setMaxValue(10);
        myNumberPickerConteo.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoConteo.setText(""+newVal);
            }
        };
        myNumberPickerConteo.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerConteo);
        builder.setTitle("Conteo")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
    //numberPickerDialogo de Metro
      private void numberPickerDialogoMetro(){

        NumberPicker myNumberPickerMetro = new NumberPicker(this);
        myNumberPickerMetro.setMaxValue(50);
        myNumberPickerMetro.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoMetro.setText(""+newVal);
            }
        };
        myNumberPickerMetro.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerMetro);
        builder.setTitle("Metro")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    //numberPickerDialogo de Nivel
    private void numberPickerDialogoNivel(){
        NumberPicker myNumberPickerNivel = new NumberPicker(this);
        myNumberPickerNivel.setMaxValue(30);
        myNumberPickerNivel.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoNivel.setText(""+newVal);
            }
        };
        myNumberPickerNivel.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerNivel);
        builder.setTitle("Nivel")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    //numberPickerDialogo de NroSoporte
    private void numberPickerDialogoNroSoporte(){

        NumberPicker myNumberPickerNroSoporte = new NumberPicker(this);
        myNumberPickerNroSoporte.setMaxValue(150);
        myNumberPickerNroSoporte.setMinValue(1);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                campoNroSoporte.setText(""+newVal);
            }
        };
        myNumberPickerNroSoporte.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPickerNroSoporte);
        builder.setTitle("Nro.")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }


}
