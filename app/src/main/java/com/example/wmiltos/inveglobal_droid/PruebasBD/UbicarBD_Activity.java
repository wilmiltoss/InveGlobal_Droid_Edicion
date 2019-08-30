package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class UbicarBD_Activity extends Activity {

    Button copiarBD, selectArchivo;
    private int VALOR_RETORNO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicar_bd_);
        copiarBD = (Button)findViewById(R.id.copiarBD);
        selectArchivo = (Button)findViewById(R.id.seleccioneArchivo);
    }


    private void implementaDatabase() throws IOException {
    // Abre tu db local como el flujo de entrada
        String packageName = getApplicationContext().getPackageName();
        String DB_PATH = "/data/data/" + packageName + "/databases/";
    //Create the directory if it does not exist
        File directory = new File(DB_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String DB_NAME = "InveStock.sqlite"; //The name of the source sqlite file

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

    //Ventana para seleccionar archivo desde la aplicacion
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }
        if ((resultCode == RESULT_OK) && (requestCode == VALOR_RETORNO)) {
            //Procesar el resultado
            Uri uri = data.getData(); //obtener el uri content
        }
    }

    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Seleccione la ubicacion del archivo", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/storage");
        startActivityForResult(Intent.createChooser(intent, "Choose File"), VALOR_RETORNO);

    }

    public void onClick2(View view) {

        try {
            implementaDatabase();
            Toast.makeText(getApplicationContext(), "Base de datos copiados", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick3(View view) {


    }

}
