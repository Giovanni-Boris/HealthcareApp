package com.example.healthcareapp.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

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

}
