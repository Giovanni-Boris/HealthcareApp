package com.example.healthcareapp.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Map;

@Entity(tableName = "register")
public class Register implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String hora;
    private double glucemia;
    private String insulina;
    private String carbohidrato;
    private String medicamento;
    private String actividad;
    private String tension;
    private String peso;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getGlucemia() {
        return glucemia;
    }

    public void setGlucemia(double glucemia) {
        this.glucemia = glucemia;
    }

    public String getInsulina() {
        return insulina;
    }

    public void setInsulina(String insulina) {
        this.insulina = insulina;
    }

    public String getCarbohidrato() {
        return carbohidrato;
    }

    public void setCarbohidrato(String carbohidrato) {
        this.carbohidrato = carbohidrato;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getTension() {
        return tension;
    }

    public void setTension(String tension) {
        this.tension = tension;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public static Register fromMap(Map<String, Object> map) {
        Register register = new Register();

        // La clave del mapa es la fecha y hora
        register.setHora(map.get("hora").toString());
        register.setGlucemia(Double.parseDouble(map.get("glucemia").toString()));
        register.setInsulina(map.get("insulina").toString());
        register.setCarbohidrato(map.get("carbohidrato").toString());
        register.setMedicamento(map.get("medicamento").toString());
        register.setActividad(map.get("actividad").toString());
        register.setTension(map.get("tension").toString());
        register.setPeso(map.get("peso").toString());

        return register;
    }

}
