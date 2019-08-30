package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

public class ConsultaActivity extends AppCompatActivity {

    EditText campoId, campoNombre, campoNivel;
    ConexionSQLiteHelper conn;
    Button btnBuscar3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        variables();

    }

    private void variables(){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock",null,1);
        campoId = (EditText) findViewById(R.id.texDocumento);
        campoNombre = (EditText) findViewById(R.id.texNombre);
        campoNivel = (EditText) findViewById(R.id.textNivelAcceso);
        btnBuscar3 =(Button)findViewById(R.id.btnBuscar1);

    }

   //----------------------------------------------------------------
    private void consultar() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {campoId.getText().toString()};
        String[] campos = {Utilidades.CAMPO_NOMBRE_USUARIO, Utilidades.CAMPO_NIVEL_ACCESO};

        try {
            Cursor cursor = db.query(Utilidades.TABLA_USUARIO, campos, Utilidades.CAMPO_ID_USUARIO + "=?", parametros, null, null,null);
            cursor.moveToFirst();
            campoNombre.setText(cursor.getString(0));
            campoNivel.setText(cursor.getString(1));
            cursor.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "El documento no existe", Toast.LENGTH_LONG).show();
            limpiar();
        }
    }

    private void eliminarUsuario() {
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] parametros = {campoId.getText().toString()};

          db.delete(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_ID_USUARIO+ "=?",parametros );

            Toast.makeText(getApplicationContext(), "Se elimino correctamente", Toast.LENGTH_LONG).show();
         db.close();

    }

    //----------------------------------------------------------------
    private void limpiar() {
        campoId.setText("");
        campoNombre.setText("");
        campoNivel.setText("");
    }

    public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnBuscar1:consultar();
                    consultar();
                    break;
                case R.id.btnActualizar:comprobarBotonVacio();
                    break;
                case R.id.btnEliminar:eliminarUsuario();
                    break;
            }
    }

    private void comprobarBotonVacio(){
        try {
            String s1 = campoId.getText().toString();
            btnBuscar3.setEnabled(!s1.trim().isEmpty());
           /* if (campoId.length() > 0) {
                btnBuscar3.setEnabled(true);
            } else {
                btnBuscar3.setEnabled(false);
            }*/
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al comprobar campo vacio", Toast.LENGTH_LONG).show();
        }
    }


}
