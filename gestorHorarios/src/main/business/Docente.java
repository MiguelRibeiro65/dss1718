package main.business;

public class Docente extends Utilizador{ 
    
    public Docente() {
        super(null,null,null,null);
    }

    public Docente(String numero, String nome, String email,String password) {
        super(numero,nome,email,password);
   }

    public Docente(Docente a){
        super(a);
    }

    public Docente clone() {
        return new Docente(this);
    }

    @Override
    public boolean equals(Object o) {
        return equals((Utilizador)o);
    }

}

