package com.example.wmiltos.inveglobal_droid.entidades.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.wmiltos.inveglobal_droid.utilidades.Utilidades;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {


    private Context context;
    public static String DIRECTORIO = "/sdcard/";
    public static String DB_NAME = "InveStock.sqlite";
    private static int DB_VERSION = 1;

    String ruta = Environment.getExternalStorageDirectory().getPath() +  File.separator + DIRECTORIO + DB_NAME;

    File file = new File(ruta, DB_NAME);

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_USUARIO);
    }

}
