package com.example.wmiltos.inveglobal_droid.entidades.tablas;

public class LetrasSoporte {

    private Integer id_letra_soporte;
    private String letra;

    public LetrasSoporte(Integer id_letra_soporte, String letra) {
        this.id_letra_soporte = id_letra_soporte;
        this.letra = letra;
    }

    public LetrasSoporte() {

    }

    public void setId_letra_soporte(Integer id_letra_soporte) {
        this.id_letra_soporte = id_letra_soporte;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Integer getId_letra_soporte() {
        return id_letra_soporte;
    }

    public String getLetra() {
        return letra;
    }
}
