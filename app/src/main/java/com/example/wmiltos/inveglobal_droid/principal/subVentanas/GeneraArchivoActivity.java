package com.example.wmiltos.inveglobal_droid.principal.subVentanas;

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

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import java.util.List;

public class GeneraArchivoActivity extends AppCompatActivity {

    private static final String LOG_TAG = null;
    Button generarArchivo;
    ConexionSQLiteHelper conn;
    TextView cantidadRegistros, nombreEquipo;
    boolean sdDisponible = false;
    boolean sdAccesoEscritura = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genera_archivo);

        cantidadRegistros = (TextView) findViewById(R.id.tvCampoCantidadRegistrosG);
        generarArchivo = (Button) findViewById(R.id.btn_generaArchivo);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "InveStock.sqlite", null, 1);
        nombreEquipo = (TextView) findViewById(R.id.text_nombreEquipo);
        sumaRegistrosSQL();
        //muestra el nombre del equipo
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        nombreEquipo.setText("Nombre del Equipo: " + deviceName);
        comprobarTarjetaSD();

    }

    //valida si existe registro en la tabla Lectura para generar archivo
    public boolean validarRegistroBD (String campoCarga) {
        SQLiteDatabase db = conn.getWritableDatabase();

        String Query = "Select * from " +Utilidades.TABLA_LECTURAS+" where " + Utilidades.CAMPO_ID_SOPORTE_L+ " = " + campoCarga;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0)
        {
            cursor.close();
           // Toast.makeText(getApplicationContext(), "No se registró ningun articulo", Toast.LENGTH_LONG).show();
            return false;
        }
            cursor.close();
        //Toast.makeText(getApplicationContext(), "si hay registr", Toast.LENGTH_LONG).show();
        return true;
    }



    //Genera archivo csv de tabla Lectura SQL si existe registro, sino vuelve al menu
    public void generarArchivo(View view) {
        try
        {
            if((validarRegistroBD("1"))){//valida si existe registro en la tabla

            //Copia los datos de lectura a un nuevo directorio "InveGlobal"
            File origen = new File("sdcard/Download/lectura.csv");//de origen
            File destino = new File("sdcard/Inve_Back/lectura.csv");//copia en destino
            copiarDirectorio(origen, destino);
            //copiarPegarBd ();

            escribirLecturas2();//escribe de la bd al archivo csv
            Toast.makeText(getApplicationContext(), "Archivo Generado!", Toast.LENGTH_LONG).show();

            pasarAmenuPrincipal();

            }else{
                Toast.makeText(getApplicationContext(), "No Hay Datos de Lecturas", Toast.LENGTH_LONG).show();
                pasarAmenuPrincipal();
            }

        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al leer fichero en la memoria interna", Toast.LENGTH_LONG).show();
        }
    }

    private void pasarAmenuPrincipal() {
        GeneraArchivoActivity.this.finish();//finaliza la ventana anterior
    }
    //genera el archivo csv
    public void escribirLecturas2(){
        ConexionSQLiteHelper conn = null;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        List<String> lLineas= new ArrayList<>();
        String deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        try{
            conn= new ConexionSQLiteHelper(getApplicationContext(),"InveStock.sqlite",null,1);
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
                //muestra los campos por columnas
                lLineas.add(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9)));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al generar registros", Toast.LENGTH_LONG).show();
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
            File ruta_sd, ruta_sd1, ruta_sd2, ruta_sd3, ruta_sd4 = null;
            String ruta_sd5 = null;
            String []ruta_sd6 = null;
            File file = null;
            String pathDir = null;
            String pathDB = null;

            try {

                //guarda el archivo en la memoria externa  de la aplicacion
                ruta_sd3 = new File (Environment.getExternalStorageDirectory()+"/Download");//guarda en la carpeta /sdcard/
                //guarda en la memoria externa de la aplicacion(sdcard)
                pathDir = ruta_sd3+ "";
                //_____________________________________________________________________________
                   //almacenamiento externo **Dar permiso en el aplicacion del dispositivo
                file = new File(pathDir, "lectura.csv");//se puede decirle la ruta aca
                fout = new OutputStreamWriter(new FileOutputStream(file));

                //proceso de lectura_________________________________________________________________
                fout.write("Nombre del Equipo: "+deviceName+"\r\n");//nombre del equipo
                for(String cLinea : lLineas)
                    fout.write(cLinea+"\r\n");//salto de lineas

            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Error al generar archivo", Toast.LENGTH_LONG).show();
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


    public void sumaRegistrosSQL(){
        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor c = db.rawQuery("select * from "+Utilidades.TABLA_LECTURAS,null);
        cantidadRegistros.setText("Cantidad de Lecturas:   "+c.getCount());
        db.close();
    }

    private void comprobarTarjetaSD (){

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

    public void copiarDirectorio(File sourceLocation , File targetLocation) {
        //si no existe el directorio la crea
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                Log.e("Error", "No puede crear directorio: " + targetLocation.getAbsolutePath());
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copiarDirectorio(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                Log.e("Error", "No puede crear directorio: " + directory.getAbsolutePath());
            }

            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);


                //Copiar bits de inputStream a outputStream.
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

            }catch(IOException ioe){
                Log.e("Error", "Error " + ioe.getMessage());
            }
        }

    }

    public void copiarDirectorioBD(File sourceLocation , File targetLocation) {
        //si no existe el directorio la crea
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists() && !targetLocation.mkdirs()) {
                Log.e("Error", "No puede crear directorio: " + targetLocation.getAbsolutePath());
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copiarDirectorio(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            File directory = targetLocation.getParentFile();
            if (directory != null && !directory.exists() && !directory.mkdirs()) {
                Log.e("Error", "No puede crear directorio: " + directory.getAbsolutePath());
            }

            try {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);


                //Copiar bits de inputStream a outputStream.
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();

            }catch(IOException ioe){
                Log.e("Error", "Error " + ioe.getMessage());
            }
        }

    }
    //-----------------------pruebas--------------------------------
    public void copiarPegarBd (){
        try {
            String packageName = getApplicationContext().getPackageName();
            File origen = new File("/data/data/" + packageName + "/databases/InveStock.sqlite");//copia del directorio de la aplicacion
            String DB_PATH = "sdcard/Download/";//y pega la direccion bd
            File destino = new File(DB_PATH + "investock.sqlite");
            copiarDirectorioBD(origen, destino);
            //Toast.makeText(getApplicationContext(), " Bd se copio a download", Toast.LENGTH_LONG).show();
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(), "Error al copiar Bd a download", Toast.LENGTH_LONG).show();
        }
    }

}
