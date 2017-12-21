/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.time.LocalTime;
import java.util.ArrayList;

public class Turno {
    
    private String id;
    private String idUC;
    private String dia;
    private String inicio;
    private String fim;
    private int capacidade;
    private String docente;

    public Turno(String id,String idUC,String dia,String inicio,String fim, String docente, int capacidade) {
        this.id = id;
        this.idUC = idUC;
        this.dia = dia;
        this.inicio = inicio;
        this.fim = fim;
        this.docente = docente;
        this.capacidade = capacidade;
    }

    public Turno(Turno t) {
        this.id = t.getID();
        this.idUC = t.getIdUC();
        this.dia = t.getDia();
        this.inicio = t.getInicio();
        this.fim = t.getFim();
        this.capacidade = t.getCapacidade();
        this.docente = t.getDocente();
    }

    public String getID() {
        return this.id;
    }

    public String getIdUC(){
        return this.idUC;
    }
    
    public String getDia() {
        return this.dia;
    }

    public String getInicio() {
        return this.inicio;
    }

    public String getFim() {
        return this.fim;
    }

    public String getDocente() {
        return this.docente;
    }
    
    public int getCapacidade() {
        return this.capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }
       
    public void setID(String id) {
        this.id = id;
    }

    public void setIdUC(String id) {
        this.idUC = id;
    }
    
    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setInicio(String inicio) {
        this.dia = dia;
    }

    public void setFim(String fim) {
        this.fim = fim;
    }

    public int coincide(Turno turno){
        LocalTime i;
        LocalTime f;
        LocalTime inicio1 = LocalTime.parse(this.inicio);
        LocalTime inicio2 = LocalTime.parse(turno.getInicio());
        LocalTime fim1 = LocalTime.parse(this.fim);
        LocalTime fim2 = LocalTime.parse(turno.getFim());
        
        if (inicio1.isAfter(inicio2)) i = inicio1;
        else i = inicio2;
            if(fim1.isBefore(fim2)) f = fim1;
            else f = fim2;
            if(i.isBefore(f)) return 1;
        
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



