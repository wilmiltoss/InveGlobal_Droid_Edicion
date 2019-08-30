package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class Usuarios {

    private Integer id_usuario;
    private String nombre_usuario;
    private Integer nivel_acceso;

    public Usuarios(Integer id_usuario, String nombre_usuario, Integer nivel_acceso) {
        this.id_usuario = id_usuario;
        this.nombre_usuario = nombre_usuario;
        this.nivel_acceso = nivel_acceso;
    }

    public Usuarios() {

    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public Integer getNivel_acceso() {
        return nivel_acceso;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public void setNivel_acceso(Integer nivel_acceso) {
        this.nivel_acceso = nivel_acceso;
    }
}
