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

public class RegistroLecturasActivity extends AppCompatActivity {
    EditText campoIdLocacion, campoNroConteo, campoIdSoporte,campoNroSoporte,campoIdLetraSoporte,campoNivel,campoMetro,
            campoScanning,campoCantidad,campoIdUsuario1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_lecturas);
        variables();
        recepcionDatosUbicacion();
    }

    private void variables() {
        campoIdLocacion = findViewById(R.id.campoIdlocacion);
        campoNroConteo =findViewById(R.id.campoNroConteo);
        campoIdSoporte =findViewById(R.id.campoIdSoporte);
        campoNroSoporte =findViewById(R.id.campoNroSoporte);
        campoIdLetraSoporte =findViewById(R.id.campoIdLetraSoporte);
        campoNivel=findViewById(R.id.campoNivel);
        campoMetro=findViewById(R.id.campoMetro);
        campoScanning=findViewById(R.id.campoScanning);
        campoCantidad=findViewById(R.id.campoCantidad);
        campoIdUsuario1=findViewById(R.id.campoIdUsuario);
    }

    private void registrarLecturasSQL() {

        //CONEXION
        ConexionSQLiteHelper conexion = new ConexionSQLiteHelper(this, "InveStock", null, 1);
        //ABRIR LA BD PARA EDITARLO
        SQLiteDatabase db = conexion.getWritableDatabase();
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
                "VALUES ("+campoIdLocacion.getText().toString()+", '"
                +campoNroConteo.getText().toString()+"','"
                +campoIdSoporte.getText().toString()+"','"
                +campoNroSoporte.getText().toString()+"','"
                +campoIdLetraSoporte.getText().toString()+"','"
                +campoNivel.getText().toString()+"','"
                +campoMetro.getText().toString()+"','"
                +campoScanning.getText().toString()+"','"
                +campoCantidad.getText().toString()+"','"
                +campoIdUsuario1.getText().toString()+"')";

        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(),"Lectura Registrada",Toast.LENGTH_SHORT).show();

        db.execSQL(insert);

        db.close();
    }

    public void onClickRegistrar(View view) {
        registrarLecturasSQL();
    }

    private void recepcionDatosUbicacion() {
        //recibe los datos cargado en los campos de UbicacionActivity
        Bundle miBundle2 = this.getIntent().getExtras();
        if(miBundle2 !=null){
            String conteo = miBundle2.getString("msjConteo1");
            campoNroConteo.setText("Conteo:   "+conteo);

            String metro = miBundle2.getString("msjMetro1");
            campoMetro.setText("Metro:   "+metro);

            String nivel = miBundle2.getString("msjNivel1");
            campoNivel.setText("Nivel:   "+nivel);

            String locacionDescrip = miBundle2.getString("msjLocacionDescrip1");
            String locacionId = miBundle2.getString("msjLocacionId1");
            campoIdLocacion.setText("Locacion:  "+locacionId+"-"+locacionDescrip);
            campoIdLocacion.setText(locacionId);//para enviar a StockActivity

            String SoporteDescrip = miBundle2.getString("msjDescripSoporte1");
            String SoporteId = miBundle2.getString("msjIdSoporte1");
            campoIdSoporte.setText("Soporte: "+SoporteId+"-"+SoporteDescrip);
        }
    }

    private void registrarDatosLecturaSQL() {
        //CONEXION
        ConexionSQLiteHelper conexion = new ConexionSQLiteHelper(this, "InveStock", null, 1);
        //ABRIR LA BD PARA EDITARLO
        SQLiteDatabase db = conexion.getWritableDatabase();
        //CON SENTENCIA SQL
        String insert="INSERT INTO "+Utilidades.TABLA_LECTURAS +"("
                +Utilidades.CAMPO_SCANNING_L+")"+       //1
                "VALUES ("+campoMetro.getText().toString()+"')";

        //"Id Registrado" es el mensaje que envia al insertar
        Toast.makeText(getApplicationContext(),"Lectura Registrada",Toast.LENGTH_SHORT).show();

        db.execSQL(insert);

        db.close();
    }

}
