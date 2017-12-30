package main.business;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
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
import javax.swing.JOptionPane;
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
            u.verificarPassword(password);
            sessao = u;
        }
        catch(Exception e){
            throw new NumeroException(e.getMessage());
        }
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
        Map<String,Integer> alunos = aulasDAO.verificarFaltas(a);
        if(alunos==null) return;
        for(String al:alunos.keySet()) this.adicionarTrocaFaltas(al,turno);
        
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
            Aluno a1 = (Aluno) getSessao();
            if(a1.getEstatuto()==1 && temEspaco(t2))turnosDAO.updateTurnoAluno(u1,t1,t2);
            else {
                trocasDAO.put("ss", new Troca(0,t2,sessao.getNumero()));
                return 0;
            }
        }
        else {
            trocasDAO.remove(u2,t1);
            turnosDAO.updateTurnoAluno(u1,t1,t2);
            turnosDAO.updateTurnoAluno(u2,t2,t1);
        }
        return 1;
    }
    
    public int adicionarTrocaFaltas(String u1, String t1) {
        String[] u2 = trocasDAO.getAlunoTroca(t1);
        if(u2[0]==null) return 0;
        trocasDAO.remove(u2[0],t1);
        
        turnosDAO.updateTurnoAluno(u1,t1,u2[1]);
        turnosDAO.updateTurnoAluno(u2[0],u2[1],t1);
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
        Horario horario = new Horario();
        
        for(Aluno a : coll2){
            
            coll1.stream().filter(x -> a.getUcs().contains(x.getIdUC())).forEach((s) -> {
                
                    if (horario.verificarTipo(s.getID())==1) t.add(s.getID());
                    else if(horario.verificarTipo(s.getID())==2) p.add(s.getID());
                });
            
            
            
            
            n=horario.nTurnos(1,t);
            horario.atribuiTurnos(t,n,coll1);
            t.clear();
            n+=horario.nTurnos(2,p);
            List<String> pNew = horario.deleteRep(p,coll1);

            horario.atribuiTurnos(pNew,n,coll1);
            p.clear();
            
            
            
            
            a.setTurnos(horario);
            for(String tur:horario.getHorario()){
                decrementCapacidade(tur,coll1);
                turnosDAO.decrementCapacidade(tur);
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

    public void terminarSessao() {
        this.sessao = null;
    }

    public List<Troca> getTrocasAluno(String key) {
        return trocasDAO.values().stream().filter(t->t.getIdAluno().equals(key)).collect(Collectors.toList());
    }

    public void removerTroca(Troca t) {
        trocasDAO.remove(t.getIdAluno(),t.getIdTurno());
    }

    private Boolean temEspaco(String t2) {
        if(turnosDAO.get(t2).getCapacidade()>0)return TRUE;
        else return FALSE;
    }

    public Iterable<String> getAlunosUc(String uc) {
        return alunosDAO.getAlunosUc(uc);
    }

    public void adicionarAlunoTurno(String aluno, String turno, String uc) {
        Aluno a = alunosDAO.get(aluno);
        Horario h = a.getTurnos();
        String anterior=null;
        for(String t : h.getHorario()) {
            if(h.verificarTipo(t)==h.verificarTipo(turno)&&isAorB(t)==isAorB(turno)&&finalCheck(t,uc))anterior=t;
        }
        if(turnosDAO.get(turno).getCapacidade()>0)
            turnosDAO.updateTurnoAluno(aluno,anterior,turno);
        else JOptionPane.showMessageDialog(null,"O turno encontra-se cheio"); 
    }
    
    public int isAorB(String turno) {
        if (turno==null) return 0;
        String[] split = turno.split("-");
        for(String s:split) {if(s.equals("A")) return 1;if(s.equals("B"))return 2;}
        return 0;
    }
    
    private boolean finalCheck(String t,String uc) {
        String[] split = t.split("-");
        int n = split.length;
        while(n>0){
            if(split[n].equals(uc)) return TRUE;
            n--;
        }
        return FALSE;
    }

    public void removerAlunoTurno(String aluno, String turno) {
        turnosDAO.removeAT(aluno,turno);
    }


    

    
}