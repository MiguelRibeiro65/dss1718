package main.business;

import java.util.ArrayList;

public class Aluno extends Utilizador {

    public String numero;
    private int estatuto;

    public Aluno() {
        super(null,null,null);
        numero=null;
        estatuto=0;
    }

    public Aluno(String numero, String nome, String email,String password,int estatuto) {
        super(nome,email,password);
        this.numero = numero;
        this.estatuto = estatuto;
    }

    public Aluno(Aluno a){
        super(a);
        numero=a.getNumero();
        estatuto=a.getEstatuto();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

        if (numero != null ? !numero.equals(aluno.numero) : aluno.numero != null) return false;
        if (estatuto != aluno.estatuto) return false;
        return true;
    }

}

