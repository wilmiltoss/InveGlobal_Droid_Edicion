package com.example.wmiltos.inveglobal_droid.PruebasBD;

import android.support.design.widget.Snackbar;
import android.view.View;

class Utils {
    public static void showSanckBar(View view, String message){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
