package com.example.feriteja.projectpasmartgarbage;



public class Employee {

    private String name;
    private int age;
    private int cobaid;
    private int posisi;

    public int getPosisi() {
        return posisi;
    }

    public int getCobaid() {
        return cobaid;
    }

    public  Employee(int posisi, String name, int age){
        this.name = name;
        this.age = age;
       // this.cobaid=cobaid;
        this.posisi=posisi;
    }

    public String getName(){
        return name;
    }

    public int getAge(){
        return age;
    }
}