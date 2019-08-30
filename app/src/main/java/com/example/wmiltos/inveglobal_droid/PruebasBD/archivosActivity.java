package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class archivosActivity extends AppCompatActivity implements View.OnClickListener{

    Button leermeminterna;
    Button escribirmeminterna;
    Button leersd;
    Button escribirSd;
    Button leerPrograma;
    Button escribirArchivo;
    TextView texto1;
    boolean sdDisponible=false;
    boolean sdAccesoEscritura =false;
    ConexionSQLiteHelper conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivos);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leermeminterna=(Button)findViewById(R.id.leermeminterna);
        escribirmeminterna=(Button)findViewById(R.id.escribirmeminterna);
        leersd=(Button)findViewById(R.id.leersd);
        escribirSd=(Button)findViewById(R.id.escribirsd);
        leerPrograma=(Button)findViewById(R.id.leerprograma);
        texto1=(TextView)findViewById(R.id.textView);
        escribirArchivo=(Button) findViewById(R.id.escrirArchivo);

        leermeminterna.setOnClickListener(this);
        escribirmeminterna.setOnClickListener(this);
        leersd.setOnClickListener(this);
        escribirSd.setOnClickListener(this);
        leerPrograma.setOnClickListener(this);
        escribirArchivo.setOnClickListener(this);

        String estado = Environment.getExternalStorageState();

        if(estado.equals(Environment.MEDIA_MOUNTED)){

            sdDisponible = true;
            sdAccesoEscritura = true;
        }else   if(estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){

            sdDisponible = true;
            sdAccesoEscritura = false;
        }else {

            sdDisponible = false;
            sdAccesoEscritura = false;
        }

    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    public void escribirLecturas(){
        ConexionSQLiteHelper conn = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        List<String> lLineas= new ArrayList<>();

        //Si la tabla tiene registros muestra la cantidad sino no lo muestra y pasa al sgte ventana
        try{
            conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock",null,1);
             db = conn.getWritableDatabase();
            cursor = db.rawQuery("SELECT " +Utilidades.CAMPO_ID_LOCACION_L+","
                    +Utilidades.CAMPO_NRO_CONTEO+","
                    +Utilidades.CAMPO_ID_SOPORTE_L+","
                    +Utilidades.CAMPO_NRO_SOPORTE_L+","
                    +Utilidades.CAMPO_LETRA_SOPORTE_L+","
                    +Utilidades.CAMPO_NIVEL+","
                    +Utilidades.CAMPO_METRO+","
                    +Utilidades.CAMPO_SCANNING_L+","
                    +Utilidades.CAMPO_CANTIDAD+","
                    +Utilidades.CAMPO_ID_USUARIO_L+
                    " FROM "+Utilidades.TABLA_LECTURAS, null);
             while(cursor.moveToNext())
                  lLineas.add(String.format("%s;%s",cursor.getString(0),
                                                    cursor.getString(1),
                                                    cursor.getString(2),
                                                    cursor.getString(3),
                                                    cursor.getString(4),
                                                    cursor.getString(5),
                                                    cursor.getString(6),
                                                    cursor.getString(7),
                                                    cursor.getString(8),
                                                    cursor.getString(9)
                        ));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Sin registros", Toast.LENGTH_LONG).show();
        }finally {
            if(null != db && db.isOpen())
                db.close();
            if(null != cursor)
                cursor.close();
            if(null != conn)
                conn.close();
        }
        if(!lLineas.isEmpty()) {
            OutputStreamWriter fout = null;
            try {
                fout = new OutputStreamWriter(openFileOutput("meminterna2.csv", Context.MODE_PRIVATE));
                for(String cLinea : lLineas)
                fout.write(cLinea + "\r\n");

            }catch (Exception ex){

            }finally {
                if(null != fout) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case (R.id.leermeminterna):
                try
                {
                    escribirLecturas();
                     BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("meminterna2.txt")));
                     String texto = fin.readLine();
                     texto1.setText(texto);
                     fin.close();

                }catch (Exception ex){
                    Log.e("Fichero", "Error al leer fichero en la memoria interna");
                }
                break;

           case (R.id.escribirmeminterna)://-------------------------------------------------------------------------
                try
                {
                    OutputStreamWriter fout=new OutputStreamWriter(openFileOutput("meminterna2.csv", Context.MODE_PRIVATE));
                    fout.write( "Esto es una prueba de lectura. ");

                    fout.close();

                }catch (Exception ex){
                    Log.e("Fichero", "Error al escribir fichero en la memoria interna");
                }
                break;//-----------------------------------------------------------------------------------------------------
            case (R.id.leersd):
                if(sdDisponible) {
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();//da la ruta por defecto
                        File f = new File(ruta_sd.getAbsolutePath(), "ficherosd.txt");//se puede decirle la ruta aca
                        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));

                        BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        String texto = fin.readLine();
                        texto1.setText(texto);
                        fin.close();


                    } catch (Exception ex) {
                        Log.e("Fichero", "Error al escribir fichero en la memoria interna");
                    }
                }
                break;
            case (R.id.escribirsd):
                if(sdAccesoEscritura && sdDisponible) {
                    try {
                        File ruta_sd = Environment.getExternalStorageDirectory();//da la ruta por defecto
                        File f = new File(ruta_sd.getAbsolutePath(), "ficherosd.txt");//se puede decirle la ruta aca
                        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(f));

                        fout.write("Contenido del fichero de la SD");
                        fout.close();

                    } catch (Exception ex) {
                        Log.e("Fichero", "Error al leer fichero en la memoria SD");
                    }
                }
                break;
            case (R.id.leerprograma):
                try
                {
                    InputStream fraw = getResources().openRawResource(R.raw.ficheroraw);
                    BufferedReader brind = new BufferedReader(new InputStreamReader(fraw));

                    String linea = brind.readLine();
                    texto1.setText(linea);

                    fraw.close();
                }catch (Exception ex){
                    Log.e("Fichero", "Error al leer fichero desde recurso raw");
                }
                break;
                default:
                    break;



        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
