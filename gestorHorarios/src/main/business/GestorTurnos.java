package main.business;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import main.data.DocenteDAO;
import main.data.AlunoDAO;
import main.data.AulaDAO;
import main.data.CadeiraDAO;
import main.data.DirecaoCursoDAO;
import main.data.TurnoDAO;
import main.data.TrocaDAO;

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
    private TrocaDAO trocasDAO;
    private DirecaoCursoDAO direcaoCursoDAO;
   
    public GestorTurnos() {
        sessao = null; //deveria ser carregado da base de dados
        alunosDAO = new AlunoDAO();
        aulasDAO = new AulaDAO();
        cadeirasDAO = new CadeiraDAO();
        docentesDAO = new DocenteDAO();
        turnosDAO = new TurnoDAO();
        trocasDAO = new TrocaDAO();
        direcaoCursoDAO = new DirecaoCursoDAO();
    }

    public Utilizador getSessao() {
        return sessao;
    }

    public void iniciarSessao(String numero, String password) throws NumeroException{
        Utilizador u;
        try{
            if(direcaoCursoDAO.containsKey(numero)) 
                u = direcaoCursoDAO.get(numero);
            
            else if(docentesDAO.containsKey(numero)) 
                u = docentesDAO.get(numero);  
            
            else u = alunosDAO.get(numero);
            verificarPassword(u,password);
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
   
    public void adicionarAluno(String numero,String nome,String email,String password,int estatuto){
        Aluno a = new Aluno(numero,nome,email,password,estatuto);
        alunosDAO.put(a.getNumero(),a);
    }
    
    public void adicionarCadeira(String nome,String acron){
        Cadeira value = new Cadeira(nome,acron);
        cadeirasDAO.put(acron, value);
    }
    
    public void adicionarCadeira(Cadeira value){
        cadeirasDAO.put(value.getAcron(), value);
    }

    public void adicionarDocente(String numero, String nome, String email, String password) {
        Docente d = new Docente(numero,nome,email,password);
        docentesDAO.put(numero,d);
    }

    public Set<String> getTurnos() {
        return turnosDAO.keySet();
    }

    public void adicionarAula(String data, String turno) {
        Aula a = new Aula(Date.valueOf(data),turno);
        aulasDAO.put(null,a);
    }
    
    public Map<String,String> getTurnosAluno() {
        String key = sessao.getNumero();
        return turnosDAO.getA(key);
    }

    public List<String> getCadeirasAluno() {
        String key = sessao.getNumero();
        return cadeirasDAO.getA(key);
    }

    public int adicionarTroca(String t1,String t2) {
        String u1=sessao.getNumero();
        String u2 = trocasDAO.getAlunoTroca(t1,t2);
        if(u2==null) {
            trocasDAO.put("key", new Troca(0,t2,sessao.getNumero()));
            return 0;
        }
        trocasDAO.remove(u2,t1);
        turnosDAO.updateTurnoAluno(u1,t1,t2);
        turnosDAO.updateTurnoAluno(u2,t2,t1);
        return 1;
    }

    public List<String> getTurnosUC(String uc) {
        return turnosDAO.values().stream()
                                    .filter(u -> u.getIdUC().equals(uc))
                                    .map(u -> u.getID())
                                    .collect(Collectors.toList());
    }

}//.collect(Collectors.toList())