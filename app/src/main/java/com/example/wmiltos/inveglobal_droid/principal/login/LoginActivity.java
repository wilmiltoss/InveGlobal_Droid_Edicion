package com.example.wmiltos.inveglobal_droid.principal.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.PruebasBD.PermisoActivity;
import com.example.wmiltos.inveglobal_droid.PruebasBD.PruebaTabsActivity;
import com.example.wmiltos.inveglobal_droid.PruebasBD.ScannerActivity;
import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.entidades.conexion.ConexionSQLiteHelper;
import com.example.wmiltos.inveglobal_droid.principal.subVentanas.VisualizarRegistroActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.UbicacionActivity;
import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LoginActivity extends AppCompatActivity {

    //----------------------------------------------------------

    private Cursor fila;
    EditText et_Password, et_usuario2, validacionTablaLectura;
    private int VALOR_RETORNO = 1;

    private final int REQUEST_PERMISSION_DOCUMENTOS_CAMARA=1;
    Button abrirDocumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        consultaTablaLectura();//1

        //Icono en el action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);



        //para los permisos de lectura y escritura de documentos y camara
        abrirDocumentos = (Button) findViewById(R.id.button);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE+ Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            abrirDocumentos.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA },REQUEST_PERMISSION_DOCUMENTOS_CAMARA);
        }

        et_Password = (EditText) findViewById(R.id.et_usuario);
        et_usuario2 = (EditText) findViewById(R.id.et_usuario2);

        Button btnSalir = (Button) findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();//finaliza la ventana
            }
        });

    }
    //dialogo de solicitud de permiso
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_DOCUMENTOS_CAMARA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(LoginActivity.this, "Permiso concedido!", Toast.LENGTH_SHORT).show();
                    abrirDocumentos.setEnabled(true);
                    } else {
                        Toast.makeText(LoginActivity.this, "Permiso denegado!", Toast.LENGTH_SHORT).show();
                    }
                }
    }

    //login usuario
    public void ingresar2(View view) {
        //copia siempre la BD al ingresar de Dowload al app db
         copiarBaseDatos();
        try {
            ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "InveStock.sqlite", null, 1);
            SQLiteDatabase db = admin.getWritableDatabase();
            String usuario = et_Password.getText().toString();
            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_ID_USUARIO + " FROM " + Utilidades.TABLA_USUARIO +
                    " WHERE " + Utilidades.CAMPO_ID_USUARIO + "='" +usuario+ "'", null);
            fila.moveToFirst();
            et_usuario2.setText(fila.getString(0));//muestra los campos

            //si el usuario existe ingresa
            if (fila.moveToFirst()) {
                String pass = fila.getString(0);
                if (usuario.equals(pass)) {
                    dialogo();
                }
            } else {
               Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_LONG).show();
                limpiar();
            }
            //en el primer login, cuando da error al ingreso, copia el 1er maestro
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error al ingreso - Intente nuevamente", Toast.LENGTH_LONG).show();
            //copiarBaseDatos();
        }
    }

    private void limpiar() {
        et_Password.setText("");
    }
    //creamos un dialogo de entrada
    private void dialogo() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
        dialogo.setMessage("Ingresar al sistema").setTitle("InveGlobal")
                .setIcon(R.drawable.alerta);
        //2 -evento click positivo
        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //copiarPegarArchivo();
                //copiarPegarBd();//copia la bd a download
                limpiar();
                envioUsuario();
                //pasarAmenuPrincipal();
            }
        });
        dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //si queremos hacer algo sino dejar en blanco
            }
        });
        //4-crear alertDialogo
        android.support.v7.app.AlertDialog alertDialog = dialogo.create();
        alertDialog.show();
    }



    public void envioUsuario(){
        Intent miIntent = null;
        String msjUsuario = et_usuario2.getText().toString();
        miIntent = new Intent(LoginActivity.this, UbicacionActivity.class);
        Toast.makeText(getApplicationContext(),"Bienvenido al  InveGlobal",Toast.LENGTH_LONG).show();
        Bundle miBundle = new Bundle();
        miBundle.putString("msjUsuario", et_usuario2.getText().toString());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
        LoginActivity.this.finish();//finaliza la ventana anterior

    }


    //consultamos para validar la carga del maestro
    public void consultaTablaLectura (){
        try {
            validacionTablaLectura = (EditText) findViewById(R.id.et_validaTablaLectura);

            ConexionSQLiteHelper admin = new ConexionSQLiteHelper(this, "InveStock.sqlite", null, 1);
            SQLiteDatabase db = admin.getReadableDatabase();

            fila = db.rawQuery("SELECT " + Utilidades.CAMPO_SCANNING_L + " FROM " + Utilidades.TABLA_LECTURAS, null);
            fila.moveToFirst();
            validacionTablaLectura.setText(fila.getString(0));
        }catch (Exception e){
            Log.e("Error", "Error al consultar tabla Lectura: ");
        }
    }

    //metodo para copiar y pegar archivos
    public void copiarDirectorio(File sourceLocation , File targetLocation) {
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

                //Copa bits de inputStream to outputStream.
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

    public void copiarBaseDatos(){//2
        try {

            // si solo encuentra el maestro dentro de la carpeta Download va copiar el archivo al sdcard
                copiarPegarArchivo();           //copia y pega la bd de download a sdcard
                System.out.println("Maestro copiado!!");
                eliminarBdDownload (); //posteriormente lo elimina de la carpeta download

        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "Error al obtener Maestro ", Toast.LENGTH_SHORT).show();
        }
    }


    //copia la bd del directorio raiz al directorio database de la aplicacion
    public void copiarPegarArchivo (){
        File comprobarArchivo = new File("sdcard/Download/InveStock.sqlite");
        if (comprobarArchivo.exists()) {
        File origen = new File("sdcard/Download/InveStock.sqlite");//copia de la direccion
        String packageName = getApplicationContext().getPackageName();
        String DB_PATH = "/data/data/" + packageName + "/databases/";//y pega la bd en el directorio de la aplicacion
        //Crea el directorio si no existe
            File directorio = new File(DB_PATH);
            if (!directorio.exists()) {
            directorio.mkdirs();
            }
            File destino = new File(DB_PATH + "InveStock.sqlite");
            copiarDirectorio(origen, destino);

        Toast.makeText(LoginActivity.this, "Maestro nuevo cargado!", Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(LoginActivity.this, "No se encotro Maestro en el sdcard", Toast.LENGTH_SHORT).show();

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


    public void eliminarBdDownload ()
    {
        File BdDownload  = null;

        String direccionExterna = "sdcard/Download/InveStock.sqlite";
        try{
            BdDownload = new File(direccionExterna);
            //eliminamos ambos archivos de las dos ubicaciones
            boolean estatus = BdDownload.delete();
            if (!estatus) {
              //  Toast.makeText(getApplicationContext(), "No se encotro documento", Toast.LENGTH_SHORT).show();
            }else{
              //  Toast.makeText(getApplicationContext(), "BD eliminado exitosamente de download", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }



    //-------------------------- metodos a modo pruebas-----------------------------------
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
    public void onClickMenu(View view) {
        Intent miIntent = null;
        switch (view.getId()) {
            // case R.id.btn_adjuntarBD:
            // break;
            //case R.id.btn_registrarse:copiarBaseDatos();
            //miIntent = new Intent(LoginActivity.this, PruebaTabsActivity.class);
            //  break;
        }
        if (miIntent != null) {
            startActivity(miIntent);
        }
    }
    //dialogo insertar el maestro
    private void mensajeDialogoCargaMaestro() {
        //1 -creamos dialogo
        android.support.v7.app.AlertDialog.Builder dialogo = new android.support.v7.app.AlertDialog.Builder(LoginActivity.this);
        dialogo.setMessage("Desea insertar un nuevo Maestro?").setTitle("Base de Datos")
                .setIcon(R.drawable.alerta);
        //2 -evento click ok
        dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // copiarBaseDatos();
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


    //Adjuntar base de datos en la aplicacion InveGlobal_________________________
    private void implementaDatabase() throws IOException {
        // Abre tu db local es la ruta de entrada
        String packageName = getApplicationContext().getPackageName();
        String DB_PATH = "/data/data/" + packageName + "/databases/";

        // Crear el directorio si no existe.
        File directory = new File(DB_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String DB_NAME = "InveStock.sqlite"; // El nombre del archivo sqlite de origen
        InputStream myInput = getAssets().open("InveStock.sqlite");

        // Ruta a la db vacía recién creada
        String outFileName = DB_PATH + DB_NAME;

        // Abrir la base de datos vacía como el flujo de salida
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    private void pasarAmenuPrincipal() {
        Intent intent = new Intent(LoginActivity.this, UbicacionActivity.class);
        Toast.makeText(getApplicationContext(),"Bienvenido al  InveGlobal",Toast.LENGTH_LONG).show();
        startActivity(intent);
        LoginActivity.this.finish();//finaliza la ventana anterior
    }



}