/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;


import main.business.Turno;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;


public class Cadeira {
    
    private String id;
    private String nome;
    private String acron;

    public Cadeira() {
        id=null;
        nome=null;
        acron=null;
    }

    public Cadeira(String id,String nome,String acron) {
        this.id = id;
        this.nome = nome;
        this.acron = acron;
    }

    public Cadeira(Cadeira uc) {
        this.id = uc.getID();
        this.nome = uc.getNome();
        this.acron = uc.getAcron();
    }

    public String getID() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getAcron() {
        return this.acron;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setAcron(String acron) {
        this.acron = acron;
    }
    
    public int sizePraticos() {
        return (int)this.turnos.stream().filter(t -> t instanceof TurnoTP).count();
    }

    public Turno getPratico(int i) {
        return this.turnos.stream().filter(t -> t instanceof TurnoTP).collect(Collectors.toList()).get(i);
    }

    public void deleteRep(ArrayList<Turno> horario) {
       turnos = (ArrayList<Turno>)turnos.stream().filter(t -> t.coincide(horario)==0).collect(Collectors.toList());
    }

    public Cadeira clone() {
        return new Cadeira(this);
    }

    public boolean equals(Object obj){
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Cadeira u = (Cadeira) obj;
        if (!Objects.equals(this.turnos, u.turnos)) 
            return false;

        return u.getID().equals(id)
                && u.getNome().equals(nome)
                && u.getAcron().equals(acron);
    }
    
}
