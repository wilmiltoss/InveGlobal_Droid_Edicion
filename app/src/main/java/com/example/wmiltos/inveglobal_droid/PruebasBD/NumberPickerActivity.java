package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.wmiltos.inveglobal_droid.R;

public class NumberPickerActivity extends AppCompatActivity implements View.OnClickListener {
    TextView number;
    Button numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_picker);

        number=(TextView)findViewById(R.id.tvNumberPicker);
        numberPicker = (Button) findViewById(R.id.numberPicker);
        numberPicker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        numberPickerDialogo();

    }

    private void numberPickerDialogo(){

        NumberPicker myNumberPicker = new NumberPicker(this);
        myNumberPicker.setMaxValue(30);
        myNumberPicker.setMinValue(0);
        NumberPicker.OnValueChangeListener myValChangedListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                number.setText(""+newVal);

            }
        };
        myNumberPicker.setOnValueChangedListener(myValChangedListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(myNumberPicker);
        builder.setTitle("Cantidad")
                .setIcon(R.drawable.alert_dialogo);
        //botones del alertdialogos
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });
        builder.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


}
