package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.principal.login.LoginActivity;

public class PermisoActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_DOCUMENTOS=1;
    private final int REQUEST_PERMISSION_CAMARA=2;
    Button abrirDocumentos,abrirCamara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiso);

        abrirDocumentos = (Button) findViewById(R.id.buttonDoc);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            abrirDocumentos.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },REQUEST_PERMISSION_DOCUMENTOS);
        }

        abrirCamara = (Button) findViewById(R.id.buttonCamara);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            abrirCamara.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.CAMERA },REQUEST_PERMISSION_CAMARA);
        }
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_DOCUMENTOS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PermisoActivity.this, "Permiso concedido!", Toast.LENGTH_SHORT).show();
                    abrirDocumentos.setEnabled(true);
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PermisoActivity.this, "Permiso concedido de Camara!", Toast.LENGTH_SHORT).show();
                        abrirCamara.setEnabled(true);

                    } else {
                        Toast.makeText(PermisoActivity.this, "Permiso denegado!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case REQUEST_PERMISSION_CAMARA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(PermisoActivity.this, "Permiso concedido de Camara!", Toast.LENGTH_SHORT).show();
                    abrirCamara.setEnabled(true);

                } else {
                    Toast.makeText(PermisoActivity.this, "Permiso denegado!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
