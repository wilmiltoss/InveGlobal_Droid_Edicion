package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Soportes {
    private Integer id_soporte;
    private String descripcion;
    private Integer subdivisible;

    public Soportes(Integer id_soporte, String descripcion, Integer subdivisible) {
        this.id_soporte = id_soporte;
        this.descripcion = descripcion;
        this.subdivisible = subdivisible;
    }
    public Soportes() {
    }

    public Integer getId_soporte() {
        return id_soporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Integer getSubdivisible() {
        return subdivisible;
    }

    public void setId_soporte(Integer id_soporte) {
        this.id_soporte = id_soporte;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setSubdivisible(Integer subdivisible) {
        this.subdivisible = subdivisible;
    }
}
