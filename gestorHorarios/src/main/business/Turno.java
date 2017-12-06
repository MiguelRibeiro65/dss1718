/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class Turno {
    
    private String id;
    private String idUC;
    private DayOfWeek dia;
    private LocalTime inicio;
    private LocalTime fim;
    private int capacidade;
    private String docente;
    private ArrayList<String> alunos;

    public Turno(String id,String idUC,DayOfWeek dia,LocalTime inicio,LocalTime fim, String docente, int capacidade,ArrayList<String> alunos) {
        this.id = id;
        this.idUC = idUC;
        this.dia = dia;
        this.inicio = inicio;
        this.fim = fim;
        this.docente = docente;
        this.capacidade = capacidade;
        this.alunos = alunos;
    }

    public Turno(Turno t) {
        this.id = t.getID();
        this.idUC = t.getIdUC();
        this.dia = t.getDia();
        this.inicio = t.getInicio();
        this.fim = t.getFim();
        this.capacidade = t.getCapacidade();
        this.docente = t.getDocente();
        this.alunos = t.getAlunos();
    }

    public String getID() {
        return this.id;
    }

    public String getIdUC(){
        return this.idUC;
    }
    
    public DayOfWeek getDia() {
        return this.dia;
    }

    public LocalTime getInicio() {
        return this.inicio;
    }

    public LocalTime getFim() {
        return this.fim;
    }

    public String getDocente() {
        return this.docente;
    }
    
    public int getCapacidade() {
        return this.capacidade;
    }
    
    public ArrayList<String> getAlunos() {
        return this.alunos;
    }
    
    public void setID(String id) {
        this.id = id;
    }

    public void setIdUC(String id) {
        this.idUC = id;
    }
    
    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }

    public void setInicio(LocalTime inicio) {
        this.dia = dia;
    }

    public void setFim(LocalTime fim) {
        this.fim = fim;
    }

    public int coincide(ArrayList<Turno> horario){
        LocalTime i;
        LocalTime f;
        for(Turno t : horario) {
            if (inicio.isAfter(t.getInicio())) i = inicio;
            else i = t.getInicio();
            if(fim.isBefore(t.getFim())) f = fim;
            else f = t.getFim();
            if(i.isBefore(f)) return 1;
        }
        return 0;
    }

    public Turno clone() {
        return new Turno(this);
    }

    public boolean equals(Object obj){

        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Turno t = (Turno) obj;
        return t.getID().equals(id)
                && t.getDia().equals(dia)
                && t.getInicio().equals(inicio)
                && t.getFim().equals(fim);
    }

}



