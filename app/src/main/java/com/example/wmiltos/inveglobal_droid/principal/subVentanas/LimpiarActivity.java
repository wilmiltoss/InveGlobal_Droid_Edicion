package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginValidacionActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class LimpiarActivity extends AppCompatActivity {

    Button limpiarDatos;
    TextView cantidadRegistros, parameters;
    ConexionSQLiteHelper conn;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpiar);
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        cantidadRegistros=(TextView) findViewById(R.id.tvCampoCantidadRegistros);
        limpiarDatos = (Button)findViewById(R.id.btn_limpiar);
        parameters=(TextView) findViewById(R.id.tvParametro);
        sumaRegistrosSQL();
    }

    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor c = db.rawQuery("select * from "+Utilidades.TABLA_LECTURAS,null);
        cantidadRegistros.setText("Cantidad de Lecturas:   "+c.getCount());
        db.close();
    }

    private void mensajeDialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LimpiarActivity.this);
        dialogo.setMessage("Esta Seguro de borrar los datos?").setTitle("Datos de lecturas")
                .setIcon(R.drawable.alerta);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pasarAmenuLogin();
            }
        });
        dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LimpiarActivity.this.finish();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    public void onClick2(View view) {
        switch (view.getId()){
            case R.id.btn_volverL:LimpiarActivity.this.finish();
                break;
            case R.id.btn_limpiar:mensajeDialogo();
                break;
        }
    }

    private void pasarAmenuLogin() {
        Intent intent = new Intent(LimpiarActivity.this, LoginValidacionActivity.class);
        startActivity(intent);
        LimpiarActivity.this.finish();
    }

}
