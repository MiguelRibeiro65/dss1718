/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;


public class Horario {
    
    private List<String> horario;

    public Horario() {
        this.horario = new ArrayList<>();
    }

    public Horario(Horario turnos) {
        List<String> aux = new ArrayList<>();
        for(String t:turnos.getHorario()) aux.add(t);
        this.horario=aux;
    }
    
    public Horario(List<String> turnos) {
        this.horario=turnos;
    }
    
    public List<String> getHorario() {
        return horario;
    }

    public void setHorario(List<String> horario) {
        this.horario = horario;
    }
    
    public void atribuiTurnos(List<String> turnos,int n,Collection<Turno> coll) {
        if (horario.size()==n) return;
        String t = turnos.get(0);
        if(nAulas(t)==0||nAulas(t)==2) {
                
                horario.add(t);
                horario.add(getOutro(t));
                
                turnos.remove(t);
                turnos.remove(getOutro(t));
                if (horario.size()==n) return;
                else if(turnos.isEmpty()){
                    horario.remove(t);
                    horario.remove(getOutro(t));
                    return;
                }   
                else if(deleteRep(turnos,coll).isEmpty()) {
                    horario.remove(t);
                    horario.remove(getOutro(t));
                    atribuiTurnos(turnos,n,coll);
                    return;
                }
                
            }
      
            else {
                horario.add(t);
                turnos.remove(t);
                if (horario.size()==n||turnos.isEmpty()) return;
                else if(turnos.isEmpty()){
                    horario.remove(t);
                    return;
                }
                else if(deleteRep(turnos,coll).isEmpty()) {horario.remove(t);atribuiTurnos(turnos,n,coll);return;}//horario.clear();return;}
            }
            turnos=deleteRep(turnos,coll);
            atribuiTurnos(turnos,n,coll);
        
    }
    
    public int nTurnos(int i, List<String> turnos) {
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
    
    private int nAulas(String turno) {
        String[] split = turno.split("-");
        for(String s:split) {if(s.equals("A")) return 2;if(s.equals("B")) return 0;}
        return 1;
    }
    
    
    public List<String> deleteRep(List<String> turnos,Collection<Turno> coll) {
        
        List<String> turnosAux = new ArrayList<>(turnos);
        int n=0;
        for(String s1 : turnos) {
            Turno t1 = coll.stream().filter(x->x.getID().equals(s1)).findAny().get();
            
                for(String s2 : horario){
                    Turno t2 = coll.stream().filter(x->x.getID().equals(s2)).findAny().get();
                    
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

    public int verificarTipo(String turnoAtual) {
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

    
    
    private String getOutro(String t) {
        String[] split = t.split("-");
        String a = split[0];
        int n;
        for(n=1;n<split.length-1;n++) a+=("-"+split[n]);
        if(split[n].equals("B")) a+="-A";
        else if (split[n].equals("A")) a+="-B";
        return a;
    }

    void flush() {
        this.horario.clear();
    }

    public Boolean contains(String p) {
        if (horario.contains(p)) return TRUE;
        return FALSE;
    }

 }
