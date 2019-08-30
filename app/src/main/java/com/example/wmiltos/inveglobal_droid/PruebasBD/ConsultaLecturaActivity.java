package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class ConsultaLecturaActivity extends AppCompatActivity {
    EditText campoIdLocacion,  campoScanning, campoCantidad;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_lectura);
        variables();
    }

    private void variables() {

        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock",null,1);
        campoIdLocacion = (EditText) findViewById(R.id.texIdLocacion);
        campoScanning = (EditText) findViewById(R.id.texScanning);
        campoCantidad = (EditText) findViewById(R.id.textCantidad);
    }
    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {campoIdLocacion.getText().toString()};
        String[] campos = {Utilidades.CAMPO_SCANNING_L, Utilidades.CAMPO_CANTIDAD};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_LECTURAS, campos, Utilidades.CAMPO_ID_LOCACION_L + "=?", parametros, null, null,null);
            cursor.moveToFirst();
            campoScanning.setText(cursor.getString(0));
            campoCantidad.setText(cursor.getString(1));
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "El documento no existe", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    //----------------------------------------------------------------
    private void limpiar() {
        campoIdLocacion.setText("");
        campoScanning.setText("");
        campoCantidad.setText("");
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBuscar1:
                consultar();
                break;
            case R.id.btnActualizar:
                break;
            case R.id.btnEliminar:
                break;
        }
    }
}
