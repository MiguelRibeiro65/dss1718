package main.business;

import java.util.ArrayList;

public class Aluno {

    public String numero;
    public String nome;
    public String email;
    private int estatuto;

    public Aluno() {
        numero=null;
        nome=null;
        email=null;
        estatuto=0;
    }

    public Aluno(String numero, String nome, String email,int estatuto) {
        this.numero = numero;
        this.nome = nome;
        this.email = email;
        this.estatuto = estatuto;
    }

    public Aluno(Aluno a){
        numero=a.getNumero();
        nome=a.getNome();
        email=a.getEmail();
        estatuto=a.getEstatuto();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEstatuto() {
        return estatuto;
    }

    public void setEstatuto(int estatuto) {
        this.estatuto = estatuto;
    }
    
    public void atribuiPraticos(ArrayList<Cadeira> ucs,int n) {
        if (horario.size()==n) return;
        if (ucs.isEmpty()) {horario.clear();return;}

        for (Cadeira u : ucs) {
            for(int i=0;i<u.sizePraticos();i++){
                if(u.getTeorica()==false) {
                    horario.add(u.getPratico(i));
                    horario.add(u.getPratico(i+1));
                    i++;
                }
                else horario.add(u.getPratico(i));
                atribuiPraticos(deleteRep(ucs,horario), n);
            }
        }
    }

    public ArrayList<Cadeira> deleteRep(ArrayList<Cadeira> ucs,ArrayList<Turno> horario) {
        ArrayList<Cadeira> aux = clonar(ucs);
        for(Cadeira u : aux) {
            u.deleteRep(horario);
        }
    }

    public void flushH() {
        this.horario.clear();
    }

    public ArrayList<Cadeira> clonar(ArrayList<Cadeira> ucs){
        ArrayList<Cadeira> aux = new ArrayList<Cadeira>();
        for(Cadeira u : ucs) {
            aux.add(u.clone());
        }
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
        if (nome != null ? !nome.equals(aluno.nome) : aluno.nome != null) return false;
        if (email != null ? !email.equals(aluno.email) : aluno.email != null) return false;
        if (estatuto != aluno.estatuto) return false;
        return true;
    }

}

