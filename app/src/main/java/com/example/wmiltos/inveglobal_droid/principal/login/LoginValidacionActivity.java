package com.example.wmiltos.inveglobal_droid.principal.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.LimpiarActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.UbicacionActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;

public class LoginValidacionActivity extends AppCompatActivity {
    private Cursor fila;
    EditText et_PasswordV;
    TextView txParametro;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
        setContentView(R.layout.activity_login_validacion);
        et_PasswordV = (EditText) findViewById(R.id.et_usuarioV);
        txParametro = (TextView) findViewById(R.id.tvParametro2);

    }
    public void ingresar2(View view) {
        try {
            ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "InveStock.sqlite", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            String usuario = et_PasswordV.getText().toString();
            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_USUARIO + " FROM " + Utilidades.TABLA_USUARIO +
                    " WHERE " + Utilidades.CAMPO_ID_USUARIO + "='" +usuario+ "'", null);
            fila.moveToFirst();
            //si el usuario existe ingresa
            if (fila.moveToFirst()) {
                String pass = fila.getString(0);
                if (usuario.equals(pass)) {
                    dialogo();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Clave de Autorización no Válida!", Toast.LENGTH_LONG).show();
                limpiar();
                pasarAmenuPrincipal();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error de autenticación", Toast.LENGTH_LONG).show();
        }
    }

    private void limpiar() {
        et_PasswordV.setText("");
    }
    //creamos un dialogo de entrada
    private void dialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LoginValidacionActivity.this);
        dialogo.setMessage("Desea Eliminar Registro?").setTitle("Clave confirmada");
        //2 -evento click positivo
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                borrarDatosLecturaSQL();
                eliminarArchivo();
                pasarALimpiarActiviti();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pasarAmenuPrincipal();
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }

    private void pasarAmenuPrincipal() {
        Intent intent = new Intent(LoginValidacionActivity.this, UbicacionActivity.class);
        startActivity(intent);
        LoginValidacionActivity.this.finish();//finaliza la ventana anterior
    }

    private void pasarALimpiarActiviti() {
        Intent intent = new Intent(LoginValidacionActivity.this, LimpiarActivity.class);
        startActivity(intent);
        LoginValidacionActivity.this.finish();//finaliza la ventana anterior
    }


    private void borrarDatosLecturaSQL() {
        try {
            SQLiteDatabase db = conn.getReadableDatabase();
            String[] parametros = {txParametro.getText().toString()};
            //SENTENCIA SQL
            db.delete(Utilidades.TABLA_LECTURAS, Utilidades.CAMPO_ID_LOCACION_L + ">?", parametros);
            Toast.makeText(getApplicationContext(), "Lectura Eliminadas correctamente", Toast.LENGTH_SHORT).show();
            db.close();
        }catch (Exception e){

            Toast.makeText(getApplicationContext(), "Error al eliminar los datos", Toast.LENGTH_SHORT).show();
        }
    }

   //elimina segun la ubicacion del archivo
    private void eliminarArchivo() {
        File archivo, archivoSdcard = null;

        String direccionExterna = "sdcard/lectura.csv";
        String direccionSdcard = "sdcard/sdcard/lectura.csv";
        String direccionInterna = "/data/data/com.example.wmiltos.inveglobal_droid/files/lectura.txt";
        try{
            archivo = new File(direccionExterna);
            archivoSdcard = new File(direccionSdcard);
            //eliminamos ambos archivos de las dos ubicaciones
            boolean estatus = archivo.delete();
            boolean estatus2 = archivoSdcard.delete();
            if (!estatus && estatus2) {
                Toast.makeText(getApplicationContext(), "No se encotro documento", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "Documento eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }

}
