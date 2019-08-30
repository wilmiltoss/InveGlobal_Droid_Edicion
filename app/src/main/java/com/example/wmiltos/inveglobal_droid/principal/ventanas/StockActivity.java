package com.example.wmiltos.inveglobal_droid.principal.ventanas;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
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

public class StockActivity extends AppCompatActivity {

    TextView txScanning, txDescripcion, txDetalle, txSector, txCantidad, txIScanning,
             txIconteo, txInroLocacion,txIsoporte, txInivel,txImetro, txInroSoporte,
             txSumaCantidad,txIdLetraSoporte,txResulSuma, nroLecturas, txUsuario, txSectorDescripcion;
    EditText campoCantidad;
    ConexionSQLiteHelper conn;
    Button btnGrabar;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        variables();
        recepcionDatosLecturas();
        consultar();
        mostrarCantidad();
        sumaRegistrosSQL();
        muestraSectorDescripcion();


        //si no tenemos el registro agregamos,osino actualizamos los campos
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (campoCantidad.getText().length() != 0) {//si el campo esta vacio, mostrar mensaje

                        SQLiteDatabase db = conn.getWritableDatabase();
                        String[] parametros = {txInroLocacion.getText().toString(),
                                txIconteo.getText().toString(),
                                txIsoporte.getText().toString(),
                                txInroSoporte.getText().toString(),
                                txInivel.getText().toString(),
                                txImetro.getText().toString(),
                                txScanning.getText().toString()};
                        fila = db.rawQuery("SELECT " + Utilidades.CAMPO_CANTIDAD +

                                        " FROM " + Utilidades.TABLA_LECTURAS + " WHERE "
                                        + Utilidades.CAMPO_ID_LOCACION_L + "=? AND "
                                        + Utilidades.CAMPO_NRO_CONTEO + "=? AND "
                                        + Utilidades.CAMPO_ID_SOPORTE_L + "=? AND "
                                        + Utilidades.CAMPO_NRO_SOPORTE_L + "=? AND "
                                        + Utilidades.CAMPO_NIVEL + "=? AND "
                                        + Utilidades.CAMPO_METRO + "=? AND "
                                        + Utilidades.CAMPO_SCANNING_L + "=?"
                                , parametros, null);
                        //si el select tira cantidad va actualizar, sino va registrar
                        if (fila.moveToFirst()) {
                            if (fila != null) {
                                actualizarDialogo();
                            }
                        } else {
                            registrarDialogo();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
                    }
            }

        });
    }

    //recepcion de los mensajes de la ventana anterior
    private void recepcionDatosLecturas() {
        recibirScannerCamara();
        Bundle miBundle = this.getIntent().getExtras();
        if(miBundle!=null){
            //1
            String locacion =miBundle.getString("msjEnroLocacion");
            txInroLocacion.setText(locacion);
            //2
            String conteo =miBundle.getString("msjEconteo");
            txIconteo.setText(conteo);
            //3
            String soporte =miBundle.getString("msjEsoporte");
            txIsoporte.setText(soporte);
            //4
            String nroSoporte =miBundle.getString("msjEnroSoporte");
            txInroSoporte.setText(nroSoporte);
            //6
            String nivel =miBundle.getString("msjEnivel");
            txInivel.setText(nivel);
            //7
            String metro =miBundle.getString("msjEmetro");
            txImetro.setText(metro);
            //8
            String scanning =miBundle.getString("msjScanning");
            txScanning.setText(scanning);
            //9
            String usuario =miBundle.getString("msjUsu");
            txUsuario.setText(usuario);
        }
    }

    private void variables() {
        //TX que muestra en la pantalla
        btnGrabar = (Button) findViewById(R.id.btn_grabar);
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        txScanning = (TextView) findViewById(R.id.tx_Scanning_Stock);
        txDescripcion = (TextView) findViewById(R.id.txtDescripcion);
        txDetalle = (TextView) findViewById(R.id.txtDetalle);
        txSector = (TextView) findViewById(R.id.txtSector);
        txCantidad = (TextView) findViewById(R.id.txtCantidad);
        txDescripcion = (TextView) findViewById(R.id.etDescripcion);
        txSumaCantidad=(TextView)findViewById(R.id.tx_sumaCantidad);
        txResulSuma=(TextView)findViewById(R.id.txResulSuma);
        nroLecturas=(TextView) findViewById(R.id.tvNroLecturas);
        txSectorDescripcion=(TextView)findViewById(R.id.txtSectorDescrip);

        //Impresion backup
        txInroLocacion = (TextView)findViewById(R.id.tvInroLocacion);    //1
        txIconteo = (TextView)findViewById(R.id.tvIconteo);              //2
        txIsoporte = (TextView)findViewById(R.id.tvIsoporte);            //3
        txInroSoporte = (TextView)findViewById(R.id.tvInroSoporte);      //4
        txIdLetraSoporte=(TextView) findViewById(R.id.tx_IdLetraSoporte);//5
        txInivel = (TextView)findViewById(R.id.tvInivel);                //6
        txImetro = (TextView)findViewById(R.id.tvImetro);                //7
        txIScanning = (TextView) findViewById(R.id.tx_Scanning_Stock);   //8
        txUsuario = (TextView)findViewById(R.id.tx_Usuario);             //9

        //campo editable
        campoCantidad = (EditText)findViewById(R.id.etCantidad);         //9
       // txScanning=(EditText)findViewById(R.id.etActualizaScanning);

    }

    //muestra los datos traidos de LecturasActivity en la pantalla StockActivity
    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {txIScanning.getText().toString()};
        String[] campos = {Utilidades.CAMPO_DESCRIPCION_SCANNING, Utilidades.CAMPO_DETALLE, Utilidades.CAMPO_ID_SECTOR};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_MAESTRO, campos, Utilidades.CAMPO_SCANNING + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            txDescripcion.setText(cursor.getString(0));//muestra los campos
            txDetalle.setText(cursor.getString(1));
            txSector.setText(cursor.getString(2));
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "El codigo no existe", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    private void limpiar() {
        txInroLocacion.setText("");
        txIconteo.setText("");
        txIsoporte.setText("");
        txInroSoporte.setText("");
        txInivel.setText("");
        txImetro.setText("");
        txIScanning.setText("");
        txIdLetraSoporte.setText("");
        txCantidad.setText("");
        txScanning.setText("");
        txDescripcion.setText("");
        txSector.setText("");
        txDetalle.setText("");
        txCantidad.setText("");
        txSumaCantidad.setText("");
        campoCantidad.setText("");
    }

    //registro SQL
    private void registrarTablaLecturaSQL() {
        try{
        //CONEXION
        SQLiteDatabase db = conn.getWritableDatabase();
        //CON SENTENCIA SQL
        String insert="INSERT INTO "+ Utilidades.TABLA_LECTURAS +"("
                +Utilidades.CAMPO_ID_LOCACION_L+","
                +Utilidades.CAMPO_NRO_CONTEO+","
                +Utilidades.CAMPO_ID_SOPORTE_L+","
                +Utilidades.CAMPO_NRO_SOPORTE_L+","
                +Utilidades.CAMPO_LETRA_SOPORTE_L+","
                +Utilidades.CAMPO_NIVEL+","
                +Utilidades.CAMPO_METRO+","
                +Utilidades.CAMPO_SCANNING_L+","
                +Utilidades.CAMPO_CANTIDAD+","
                +Utilidades.CAMPO_ID_USUARIO_L+")"+
                  "VALUES ("+txInroLocacion.getText().toString()+", '"
                            +txIconteo.getText().toString()+"','"
                            +txIsoporte.getText().toString()+"','"
                            +txInroSoporte.getText().toString()+"','"
                            +txIdLetraSoporte.getText().toString()+"','"
                            +txInivel.getText().toString()+"','"
                            +txImetro.getText().toString()+"','"
                            +txScanning.getText().toString()+"','"
                            +campoCantidad.getText().toString()+"','"
                            +txUsuario.getText().toString()+"')";

        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(),"Lectura Registrada",Toast.LENGTH_SHORT).show();
        db.execSQL(insert);
        db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al registrar lectura", Toast.LENGTH_LONG).show();
        }
    }

    //registro de un nuevo campo en la tabla Lecturas
    private void registrarDialogo() {

        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockActivity.this);
        dialogo.setMessage("Datos Guardados!").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                registrarTablaLecturaSQL();
                limpiar();
                volverAmenu();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void actualizarDialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockActivity.this);
        dialogo.setMessage("¿Se Agregar a esta cantidad a la ya antes Cargada?").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                actualizarTablaLecturaSQL3();
                limpiar();
                volverAmenu();
            }
        });

        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialogoReemplazar();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();

    }
    public void dialogoReemplazar(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockActivity.this);
        dialogo.setMessage("Esta seguro de Reemplazar la Cantidad antes Cargada?").setTitle("Datos de Lectura")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        reemplazarTablaLecturaSQL();
        volverAmenu();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                volverAmenu();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    //finaliza la pantalla StockActivity p/ desplegar LeturasActivity
    private void volverAmenu() {
        StockActivity.this.finish();
    }

    //mostrar la cantidad de existencia del codigo cargado
    public void mostrarCantidad(){
        try{
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros =  {txInroLocacion.getText().toString(),
                                txIconteo.getText().toString(),
                                txIsoporte.getText().toString(),
                                txInroSoporte.getText().toString(),
                                txInivel.getText().toString(),
                                txImetro.getText().toString(),
                                txScanning.getText().toString()};
        //Si la tabla tiene registros muestra la cantidad sino no lo muestra y pasa al sgte ventana
        try{
            Cursor cursor = db.rawQuery("SELECT "+Utilidades.CAMPO_CANTIDAD+
                    " FROM "+Utilidades.TABLA_LECTURAS+" WHERE "+Utilidades.CAMPO_ID_LOCACION_L+"=? AND "
                                                                +Utilidades.CAMPO_NRO_CONTEO+"=? AND "
                                                                +Utilidades.CAMPO_ID_SOPORTE_L+"=? AND "
                                                                +Utilidades.CAMPO_NRO_SOPORTE_L+"=? AND "
                                                                +Utilidades.CAMPO_NIVEL+"=? AND "
                                                                +Utilidades.CAMPO_METRO+"=? AND "
                                                                +Utilidades.CAMPO_SCANNING_L+"=?"
                                                                ,parametros);
            cursor.moveToFirst();
            //muestra los campos
            txSumaCantidad.setText(cursor.getString(0));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
        }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al mostrar la cantidad", Toast.LENGTH_LONG).show();
        }
    }


    public void sumaStock(){
        int aux1 = Integer.valueOf(txSumaCantidad.getText().toString());
        int aux2 = Integer.valueOf(campoCantidad.getText().toString());

        int resultado = aux1 + aux2;
        txResulSuma.setText(""+resultado);
    }

    public void botonResta(){
        try {
            int aux1 = Integer.valueOf(campoCantidad.getText().toString());
            int resultado = aux1 * (-1);
            campoCantidad.setText("" + resultado);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "El campo esta vacio", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickBtn1(View view) {
        botonResta();
    }

    private void actualizarTablaLecturaSQL3() {
        try{
        sumaStock();
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = { txInroLocacion.getText().toString(),
                txIconteo.getText().toString(),
                txIsoporte.getText().toString(),
                txInroSoporte.getText().toString(),
                txInivel.getText().toString(),
                txImetro.getText().toString(),
                txScanning.getText().toString()};

        ContentValues valores = new ContentValues();
        valores.put(Utilidades.CAMPO_CANTIDAD,txResulSuma.getText().toString());
        db.update(Utilidades.TABLA_LECTURAS,valores, Utilidades.CAMPO_ID_LOCACION_L+"=? AND "
                        +Utilidades.CAMPO_NRO_CONTEO+"=? AND "
                        +Utilidades.CAMPO_ID_SOPORTE_L+"=? AND "
                        +Utilidades.CAMPO_NRO_SOPORTE_L+"=? AND "
                        +Utilidades.CAMPO_NIVEL+"=? AND "
                        +Utilidades.CAMPO_METRO+"=? AND "
                        +Utilidades.CAMPO_SCANNING_L+"=?"
                ,parametros);
        Toast.makeText(getApplicationContext(),"Se agrego la cantidad",Toast.LENGTH_SHORT).show();
        db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al actualizar los datos", Toast.LENGTH_LONG).show();
        }

    }
    private void reemplazarTablaLecturaSQL() {
        try{
        sumaStock();
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = { txInroLocacion.getText().toString(),
                txIconteo.getText().toString(),
                txIsoporte.getText().toString(),
                txInroSoporte.getText().toString(),
                txInivel.getText().toString(),
                txImetro.getText().toString(),
                txScanning.getText().toString()};

        ContentValues valores = new ContentValues();
        valores.put(Utilidades.CAMPO_CANTIDAD,campoCantidad.getText().toString());
        db.update(Utilidades.TABLA_LECTURAS,valores, Utilidades.CAMPO_ID_LOCACION_L+"=? AND "
                        +Utilidades.CAMPO_NRO_CONTEO+"=? AND "
                        +Utilidades.CAMPO_ID_SOPORTE_L+"=? AND "
                        +Utilidades.CAMPO_NRO_SOPORTE_L+"=? AND "
                        +Utilidades.CAMPO_NIVEL+"=? AND "
                        +Utilidades.CAMPO_METRO+"=? AND "
                        +Utilidades.CAMPO_SCANNING_L+"=?"
                ,parametros);
        Toast.makeText(getApplicationContext(),"Cantidad reemplazada",Toast.LENGTH_SHORT).show();
        db.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al reemplazar los datos", Toast.LENGTH_LONG).show();
        }

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
                    Intent intent2 = new Intent(StockActivity.this, VisualizarRegistroActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent3 = new Intent(StockActivity.this, LimpiarActivity.class);
                    startActivity(intent3);
                    return true;
            }
            return false;
        }
    };


    public void dialogo(){
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(StockActivity.this);
        dialogo.setMessage("Desea salir del Stock?").setTitle("InveGlobal")
                .setIcon(R.drawable.alert_dialogo);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Intent intent = new Intent(StockActivity.this, LoginActivity.class);
                //startActivity(intent);
                StockActivity.this.finish();//finaliza la ventana
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
    //suma la cantidad agregada al codigo
    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+ Utilidades.TABLA_LECTURAS,null);
        nroLecturas.setText("Nro.Lecturas: "+c.getCount());
        db.close();
    }

    public void muestraSectorDescripcion (){

        SQLiteDatabase db = conn.getWritableDatabase();

        String[] parametros = {txSector.getText().toString()};
        String[] campos = {Utilidades.CAMPO_ID_SECTOR, Utilidades.CAMPO_DESCRIPCION_SECTOR};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_SECTORES, campos, Utilidades.CAMPO_ID_SECTOR + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            txSectorDescripcion.setText(cursor.getString(1));//muestra los campos
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en mostrar sector", Toast.LENGTH_LONG).show();
            limpiar();
        }

    }

    public void recibirScannerCamara() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String camaraScanning = miBundle.getString("msjCamaraScanning");
            txIScanning.setText(camaraScanning);
            txScanning.setText(camaraScanning);
        }
    }

}



