package com.example.feriteja.projectpasmartgarbage;


/**
 * Created by feriteja on 13/06/2017.
 */


import java.io.Serializable;


public class Category implements Serializable {

    Double dommyA;
    int kapasitas;
    Double dommyB;
    Double dommyC;
    String namaTempat;
    int id;
    int urutan;

    public Category(int urutan) {
        this.urutan = urutan;
    }

    public int getUrutan() {

        return urutan;
    }

    public void setUrutan(int urutan) {
        this.urutan = urutan;
    }

    public Category(int id, String namaTempat) {
        this.id = id;
        this.namaTempat = namaTempat;
    }

    public Category(int kapasitas, int id, String namaTempat) {
        this.kapasitas = kapasitas;
        this.id = id;
        this.namaTempat = namaTempat;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public String getNamaTempat() {
        return namaTempat;
    }

    public void setNamaTempat(String namaTempat) {
        this.namaTempat = namaTempat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}



