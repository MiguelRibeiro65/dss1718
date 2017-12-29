package main.business;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import main.data.DocenteDAO;
import main.data.AlunoDAO;
import main.data.AulaDAO;
import main.data.CadeiraDAO;
import main.data.DirecaoCursoDAO;
import main.data.ListasUC;
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
    private Map<String,Turno> turnos;
    public GestorTurnos() {
        sessao = null; //deveria ser carregado da base de dados
        alunosDAO = new AlunoDAO();
        aulasDAO = new AulaDAO();
        cadeirasDAO = new CadeiraDAO();
        docentesDAO = new DocenteDAO();
        turnosDAO = new TurnoDAO();
        trocasDAO = new TrocaDAO();
        direcaoCursoDAO = new DirecaoCursoDAO();
        turnos = new HashMap<>();
        turnosDAO.values().forEach(x -> turnos.put(x.getID(),x));
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
    
    public void adicionarCadeira(Cadeira a){
        cadeirasDAO.put(a.getAcron(), a);
    }
    
    public void adicionarCadeira(String a, String b) {
        cadeirasDAO.put(a, new Cadeira(a,b));
    }
    
    public void adicionarDocente(String numero, String nome, String email, String password) {
        Docente d = new Docente(numero,nome,email,password);
        docentesDAO.put(numero,d);
    }

    public Set<String> getTurnos() {
        return turnosDAO.keySet();
    }

    public void adicionarAula(String data, String turno,List<String> lista) {
        Aula a = new Aula(data,turno);
        a.setPresencas(lista);
        aulasDAO.put("1",a);
        aulasDAO.verificarFaltas(a);
    }
    
    public void adicionarAlunosUC(ListasUC a) throws SQLException, ClassNotFoundException{
        cadeirasDAO.putListasUC(a);
    }
    
    public Map<String,String> getTurnosAluno() {
        String key = sessao.getNumero();
        return turnosDAO.getA(key);
    }
    
    public void adicionarTurno(Turno a){
        turnosDAO.put(a.getID(), a);
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

    public Collection<Cadeira> getCadeiras() {
        return cadeirasDAO.values();
        
    }

    public Turno getTurno(String turno) {
        return turnosDAO.get(turno);
    }

    public void removerTurno(String turno) {
        turnosDAO.remove(turno);
    }
    
    public void gerarTurnos() {
        List<String> t = new ArrayList<>();
        List<String> p = new ArrayList<>();
        int n;
        Collection<Turno> coll1 = turnosDAO.values();
        Collection<Aluno> coll2 = alunosDAO.values();
        Horario horario = new Horario(coll1);
        
        for(Aluno a : coll2){
            coll1.stream().filter(x -> a.getUcs().contains(x.getIdUC())).forEach((s) -> {
                
                    if (horario.verificarTipo(s.getID())==1) t.add(s.getID());
                    else if(horario.verificarTipo(s.getID())==2) p.add(s.getID());
                });
            //deleteRep(t,horario,coll1);
            n=horario.nTurnos(1,t);
            horario.atribuiTurnos(t,n);
            t.clear();
            n+=horario.nTurnos(2,p);
            horario.deleteRep(p);
//System.out.println(n);
            horario.atribuiTurnos(p,n);
            p.clear();
            
            
            
            
            a.setTurnos(horario.getHorario());
            for(String tur:horario.getHorario()){
                decrementCapacidade(tur,coll1);
            }
            alunosDAO.putTurnos(a);
            horario.flush();
        }
        
    }
   
    private void decrementCapacidade(String t, Collection<Turno> coll1) {
            Optional tu = coll1.stream().filter(x->x.getID().equals(t)).findFirst();
            if(tu.isPresent())
             {  Turno turno = (Turno)tu.get();
                int aux = turno.getCapacidade()-1;
                if(aux==0)coll1.remove(turno);
                else turno.setCapacidade(aux);
            }
            
    }

    public Map<String,String> getTurnosDocente() {
        return turnosDAO.getTurnosDocente(this.sessao.getNumero());
    }

    public Iterable<String> getAlunosTurno(String turno) {
        return alunosDAO.getAlunosTurno(turno);
    }
}