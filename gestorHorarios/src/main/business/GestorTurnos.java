package main.business;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

    public void adicionarAula(String data, String turno) {
        Aula a = new Aula(data,turno);
        aulasDAO.put(null,a);
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
        List<String> horario = new ArrayList<>();
        List<String> t = new ArrayList<>();
        List<String> p = new ArrayList<>();
        int n;
        Collection<Turno> coll1 = turnosDAO.values();
        Collection<Aluno> coll2 = alunosDAO.values();
        
        
        for(Aluno a : coll2){
            horario=a.getTurnos();
            coll1.stream().filter(x -> a.getUcs().contains(x.getIdUC())).forEach((s) -> {
                
                    if (verificarTipo(s.getID())==1) t.add(s.getID());
                    else if(verificarTipo(s.getID())==2) p.add(s.getID());
                });
            //deleteRep(t,horario,coll1);
            n=nTurnos(1,t);
            atribuiTurnos(t,horario,n,coll1);
            t.clear();
            n+=nTurnos(2,p);
            deleteRep(p,horario,coll1);
//System.out.println(n);
            atribuiTurnos(p,horario,n,coll1);
            p.clear();
            
            
            
            
            a.setTurnos(horario);
            
            alunosDAO.putTurnos(a);
        }
        
    }

     

    
    public void atribuiTurnos(List<String> turnos,List<String> horario,int n,Collection<Turno> coll1) {
        if (horario.size()==n) return;
        //System.out.println(horario.size());
        System.out.println(n+"\n\n");
        String t = turnos.get(0);
        if(nAulas(t)==0||nAulas(t)==2) {
                
                horario.add(t);
                horario.add(getOutro(t));
                
                turnos.remove(t);
                turnos.remove(getOutro(t));
                if (horario.size()==n||turnos.isEmpty()) return;
                    
                if(deleteRep(turnos,horario,coll1).isEmpty()) {
                    horario.remove(t);
                    horario.remove(getOutro(t));
                    turnos.add(t);
                    turnos.add(getOutro(t));
                    atribuiTurnos(turnos,horario,n,coll1);
                    return;
                }
                
            }
                  
            else {
                horario.add(t);
                turnos.remove(t);
                if (horario.size()==n||turnos.isEmpty()) return;
                if(deleteRep(turnos,horario,coll1).isEmpty()) {horario.remove(t);turnos.add(t);atribuiTurnos(turnos,horario,n,coll1);return;}//horario.clear();return;}
            }
            turnos=deleteRep(turnos,horario,coll1);
            atribuiTurnos(turnos,horario,n,coll1);
        
    }
        
    
    
    
    private int nAulas(String turno) {
        String[] split = turno.split("-");
        for(String s:split) {if(s.equals("A")) return 2;if(s.equals("B")) return 0;}
        return 1;
    }
    private int isAorB(String turno) {
        String[] split = turno.split("-");
        for(String s:split) {if(s.equals("A")) return 1;if(s.equals("B"))return 2;}
        return 0;
    }
    public List<String> deleteRep(List<String> turnos,List<String> horario,Collection<Turno> coll1) {
        List<String> turnosAux = new ArrayList<>(turnos);
        int n=0;    
        for(String s1 : turnos) {
            Turno t1 = coll1.stream().filter(x->x.getID().equals(s1)).findAny().get();
            
                for(String s2 : horario){
                    //System.out.println(s2); 
                    Turno t2 = coll1.stream().filter(x->x.getID().equals(s2)).findAny().get();
                    
                    if(t1.coincide(t2)==1 || (t1.getIdUC().equals(t2.getIdUC())&&verificarTipo(t1.getID())==verificarTipo(t2.getID()))){
                        n=1;break;}
                    }
                
                if (n==1) {
                    turnosAux.remove(s1);
                    n=0;
                    turnosAux.remove(getOutro(s1));
                        
                        
                }
        }
        return turnosAux;
    }

    private int verificarTipo(String turnoAtual) {
        String[] split = turnoAtual.split("-");
        int n,a=0;
        n=split.length-1;
        if(temNumeros(split[n]))a=1; 
        else if(temNumeros(split[n-1])) {a=1;n-=1;}
        
        if(a==1){
            if (split[n].startsWith("T")) return 1;
            else if (split[n].startsWith("P")) return 2;
        }
        return 0;
    }
    
    private Boolean temNumeros(String arg) {
        String[] nums = {"0","1","2","3","4","5","6","7","8","9"};
        for(int n=0;n<10;n++){
            if(arg.endsWith(nums[n])) return true;
        }
        return false;
    }

    private int nTurnos(int i, List<String> turnos) {
            int n;
            int ret=0;
            String [] split;
            if(i==1){
                for(String turno : turnos) {
                    split = turno.split("-");
                    for(n=0;n<split.length;n++){
                        
                        if(split[n].equals("T1")) {
                            ret+=nAulas(turno);
                            
                        }
                    }
                }
            } else if(i==2){
                for(String turno : turnos) {
                    split = turno.split("-");
                    for(n=0;n<split.length;n++){
                        
                        if(split[n].equals("P1")) {
                            ret+=nAulas(turno);
                            
                        }
                    }
                }
            }
            return ret;
    }
    
    private String getOutro(String t) {
        String[] split = t.split("-");
        String a = split[0];
        int n;
        for(n=1;n<split.length-1;n++) a+=("-"+split[n]);
        if(split[n].equals("B")) a+="-A";
        else if (split[n].equals("A")) a+="-B";
        //System.out.println(a);
        return a;
    }
    
    
}