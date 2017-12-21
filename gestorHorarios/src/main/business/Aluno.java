package main.business;

import java.util.ArrayList;

public class Aluno extends Utilizador {

    private int estatuto;
    private ArrayList<String> ucs;
    private ArrayList<String> turnos;
    
    public Aluno() {
        super(null,null,null,null);
        estatuto=0;
        ucs=null;
        turnos=null;
    }

    public Aluno(String numero, String nome, String email,String password,int estatuto) {
        super(numero,nome,email,password);
        this.estatuto = estatuto;
        this.ucs = new ArrayList<String>();
        this.turnos = new ArrayList<String>();
    }

    public ArrayList<String> getUcs() {
        return ucs;
    }

    public void setUcs(ArrayList<String> ucs) {
        this.ucs = ucs;
    }

    public ArrayList<String> getTurnos() {
        return turnos;
    }

    public void setTurnos(ArrayList<String> turnos) {
        this.turnos = turnos;
    }

    public Aluno(Aluno a){
        super(a);
        estatuto=a.getEstatuto();
    }

    public int getEstatuto() {
        return estatuto;
    }

    public void setEstatuto(int estatuto) {
        this.estatuto = estatuto;
    }
    
    
    public Aluno clone() {
        return new Aluno(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Aluno aluno = (Aluno) o;

        //if (numero != null ? !numero.equals(aluno.numero) : aluno.numero != null) return false;
        if (estatuto != aluno.estatuto) return false;
        return true;
    }

}

