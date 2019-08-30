package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class RegistroActivity extends AppCompatActivity {

    EditText campoIdUsuario,campoNombreUsuario,campoNivelAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        campoIdUsuario = findViewById(R.id.campoIdUsuario);
        campoNombreUsuario = findViewById(R.id.campoNombreUsuario);
        campoNivelAcceso = findViewById(R.id.campoNivelAcceso);
    }

    public void onClickRegistrar(View view) {
        registrarUsuariosSQL();
    }

    private void registrarUsuariosSQL() {

        //CONEXION
        ConexionSQLiteHelper conexion = new ConexionSQLiteHelper(this, "InveStock", null, 1);
        //ABRIR LA BD PARA EDITARLO
        SQLiteDatabase db = conexion.getWritableDatabase();
        //CON SENTENCIA SQL
        String insert="INSERT INTO "+Utilidades.TABLA_USUARIO +"("
                +Utilidades.CAMPO_ID_USUARIO+","
                +Utilidades.CAMPO_NOMBRE_USUARIO+","
                +Utilidades.CAMPO_NIVEL_ACCESO+")"+
                "VALUES ("+campoIdUsuario.getText().toString()+", '"
                +campoNombreUsuario.getText().toString()+"','"
                +campoNivelAcceso.getText().toString()+"')";


        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(),"Id Registrado",Toast.LENGTH_SHORT).show();

        db.execSQL(insert);

        db.close();
    }



}
