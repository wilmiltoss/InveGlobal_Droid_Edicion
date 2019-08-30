package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;

public class VisualizarRegistroActivity extends AppCompatActivity {

    private static final String LOG_TAG = null;
    private ListView textViewPanel;
    ConexionSQLiteHelper conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_visualizar_registro);

        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        //editTextBuscar = (EditText) findViewById(R.id.editTextBuscar);
        textViewPanel = (ListView) findViewById(R.id.textViewPanel);
        llenaListView();
        validarRegistroBD("1");
    }

    public void llenaListView(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.mylistado,cargar());//myListado =cambio manual del tamaño list
        textViewPanel.setAdapter(adapter);
    }

    //valida si existe registro en la tabla Lectura algun registro
    public boolean validarRegistroBD (String campoCarga) {
        SQLiteDatabase db = conn.getWritableDatabase();

        String Query = "Select * from " +Utilidades.TABLA_LECTURAS+" where " + Utilidades.CAMPO_ID_SOPORTE_L+ " = " + campoCarga;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
             Toast.makeText(getApplicationContext(), "No se registró ningun articulo", Toast.LENGTH_LONG).show();
            return false;
        }
        cursor.close();
        //Toast.makeText(getApplicationContext(), "si hay registr", Toast.LENGTH_LONG).show();
        return true;
    }


    //consulta para mostrar los registos de la bd en el ListView
    public String []cargar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = null;

            cursor = db.rawQuery("SELECT L."+Utilidades.CAMPO_ID_LOCACION_L+",L."+
                                        Utilidades.CAMPO_SCANNING_L + ",M." +
                                        Utilidades.CAMPO_DESCRIPCION_SCANNING + ",L." +
                                         Utilidades.CAMPO_CANTIDAD
                            + " FROM " + Utilidades.TABLA_LECTURAS + " L JOIN "
                            + Utilidades.TABLA_MAESTRO + " M ON L." + Utilidades.CAMPO_SCANNING_L + "= M." + Utilidades.CAMPO_SCANNING
                    , null);
            String[] listado = new String[cursor.getCount()];//arreglo string que trae el listado
            int post = 0;
            if (cursor.moveToFirst()) {//si tenemo al menos 1 reg lo recorremos
                do {
                    String Conteo = cursor.getString(0);
                    String Scanning = cursor.getString(1);
                    String Descripcion = cursor.getString(2);
                    String Cantidad = cursor.getString(3);
                    listado[post] =Conteo +"    -"+ Scanning + "-" +Descripcion + "/ ("+Cantidad+")";
                    post++;

                } while (cursor.moveToNext());//mientras se mueve al sgte registro
            }
            return listado;
        }

    public void onClick(View view) {

        Intent miIntent = null;
        switch (view.getId()) {
            case R.id.button_volver:VisualizarRegistroActivity.this.finish();
                break;
            case R.id.button_generaArchivo:
                miIntent = new Intent(VisualizarRegistroActivity.this, GeneraArchivoActivity.class);
                break;
        }
        if (miIntent != null) {
            startActivity(miIntent);
        }
    }
}
