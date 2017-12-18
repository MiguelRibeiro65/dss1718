package main.business;

public class Utilizador {

    private String nome;
    private String email;
    private String password; 
            
    public Utilizador() {
        nome=null;
        email=null;
        password=null;
    }
    
    public Utilizador(String nome, String email,String password) {
        this.nome = nome;
        this.email = email;
        this.password = password;
    }

    public Utilizador(Utilizador a){
        nome=a.getNome();
        email=a.getEmail();
        password=a.getPassword();
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password=password;
    }
    
    public Utilizador clone() {
        return new Utilizador(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilizador u = (Utilizador) o;

        if (nome != null ? !nome.equals(u.nome) : u.nome != null) return false;
        if (email != null ? !email.equals(u.email) : u.email != null) return false;
        return true;
    }

}





/*
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

import java.util.ArrayList;
import main.business.Cadeira;
import main.business.Turno;

*/