package main.business;

public class Docente {
	
    private int id;    
    private String nome;
    private String email;
    
    public Docente() {
        id=0;
        nome=null;
        email=null;
    }

    public Docente(int id, String nome, String email,int estatuto) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        
    }

    public Docente(Docente a){
        id=a.getID();
        nome=a.getNome();
        email=a.getEmail();
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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
    
    public Docente clone() {
        return new Docente(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Docente docente = (Docente) o;

        if (id != docente.id) return false;
        if (nome != null ? !nome.equals(docente.nome) : docente.nome != null) return false;
        if (email != null ? !email.equals(docente.email) : docente.email != null) return false;
        return true;
    }

}

