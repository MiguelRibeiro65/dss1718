package main.business;

public class Docente extends Utilizador{
	
    private String numero;    
    
    public Docente() {
        super(null,null,null);
        numero=null;
    }

    public Docente(String numero, String nome, String email,String password) {
        super(nome,email,password);
        this.numero = numero;
   }

    public Docente(Docente a){
        super(a);
        numero=a.getNumero();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Docente clone() {
        return new Docente(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Docente docente = (Docente) o;

        if (numero != docente.numero) return false;
        return true;
    }

}

