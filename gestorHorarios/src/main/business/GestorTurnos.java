package main.business;

import java.util.Collection;
import java.util.Scanner;
import main.data.DocenteDAO;
import main.data.AlunoDAO;
import main.data.AulaDAO;
import main.data.CadeiraDAO;
import main.data.TurnoDAO;

/**
 * Facade para a camada de business.
 * Contém os métodos identificados nos diagramas de sequencia de implementação
 * @author ruicouto
 */
public class GestorTurnos {
    //O docente com login actualmente
    private Utilizador sessao;
    
    private AlunoDAO alunosDAO;
    private AulaDAO aulasDAO;
    private CadeiraDAO cadeirasDAO;
    private DocenteDAO docentesDAO;
   private TurnoDAO turnosDAO;
   
    public GestorTurnos() {
        sessao = null; //deveria ser carregado da base de dados
        alunosDAO = new AlunoDAO();
        aulasDAO = new AulaDAO();
        cadeirasDAO = new CadeiraDAO();
        docentesDAO = new DocenteDAO();
        turnosDAO = new TurnoDAO();
    }

    public Utilizador getSessao() {
        return sessao;
    }

    public void iniciarSessao(String numero, String password) throws NumeroException{
        Utilizador u;
        try{
            if(alunosDAO.containsKey(numero)) {
                u = alunosDAO.get(numero) ;
                verificarPassword(u,password);
            }
            else {
                u = docentesDAO.get(numero);  
                verificarPassword(u,password);
            }
            sessao = u;
        }
        catch(Exception e){
            throw new NumeroException(e.getMessage());
        }
    }
    
    public void verificarPassword(Utilizador u, String password) throws PasswordException {
        if (u.getPassword().equals(password)) return;
        else throw new PasswordException("A password está errada");
    }
   
    public void registarDocente(String numero,String password,String email,int estatuto){
        
    }
}
