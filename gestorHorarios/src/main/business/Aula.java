/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jose
 */
public class Aula {
    private int id;
    private String data;
    private String idTurno;
    private List<String> presencas;
    
    public Aula() {
        id=0;
        data=null;
        idTurno=null;
        presencas=null;
    }
    
    public Aula(String data, String idTurno) {
        this.data = data;
        this.idTurno = idTurno;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getData() {
        return this.data;
    }
    
    public String getIdTurno() {
        return this.idTurno;
    }
    
    public List<String> getPresencas() {
        return this.presencas;
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public void setData(String data) {
        this.data=data;
    }
    
    public void setIdTurno(String idTurno) {
        this.idTurno=idTurno;
    }
    
    public void setPresencas(List<String> presencas) {
        List<String> n = new ArrayList<>();
        for(String s:presencas) n.add(s);
        this.presencas = n;
    }
}
