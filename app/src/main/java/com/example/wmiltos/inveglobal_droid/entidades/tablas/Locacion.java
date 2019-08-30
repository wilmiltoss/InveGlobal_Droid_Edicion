package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Locacion {

    private Integer id_locacion;
    private String descripcion;

    public Locacion(Integer id_locacion, String descripcion) {
        this.id_locacion = id_locacion;
        this.descripcion = descripcion;
    }

    public Locacion() {

    }

    public Integer getId_locacion() {
        return id_locacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId_locacion(Integer id_locacion) {
        this.id_locacion = id_locacion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
