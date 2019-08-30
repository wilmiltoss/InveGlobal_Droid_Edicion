package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wmiltos.inveglobal_droid.R;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.LecturasActivity;
import com.example.wmiltos.inveglobal_droid.principal.ventanas.StockActivity;

public class ResScannerActivity extends AppCompatActivity {

    TextView recibo;
    Button enviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_scanner);

        recibo = (TextView) findViewById(R.id.txReciboDeLaCamara);
        enviar = (Button) findViewById(R.id.btnEnviarCodigo);


        recibirScannerCamara();
    }

    public void recibirScannerCamara() {
        Bundle miBundle = this.getIntent().getExtras();
        if (miBundle != null) {
            String camaraScanning = miBundle.getString("msjCamaraScanning");
            recibo.setText(camaraScanning);
        }
    }

    public void Enviar(View view) {
        Intent miIntent = null;
        String msjCamaraScanning2 = recibo.getText().toString();
        miIntent = new Intent(ResScannerActivity.this, StockActivity.class);
        //envio de los mensajes a la otra pantalla
        Bundle miBundle = new Bundle();
        miBundle.putString("msjCamaraScanning2", recibo.getText().toString());
        miIntent.putExtras(miBundle);
        startActivity(miIntent);
    }
}
