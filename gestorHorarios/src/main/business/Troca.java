/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.util.Objects;

/**
 *
 * @author Piromaniaco
 */
public class Troca {
    private int id;
    private String idTurno;
    private String idAluno;
    
    public Troca() {
        id=0;
        idTurno=null;
        idAluno=null;
    }
    
    public Troca(int id,String idTurno,String idAluno) {
        this.id = id;
        this.idTurno = idTurno;
        this.idAluno = idAluno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getIdAluno() {
        return idAluno;
    }

    public void setIdAluno(String idAluno) {
        this.idAluno = idAluno;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.idTurno);
        hash = 37 * hash + Objects.hashCode(this.idAluno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Troca other = (Troca) obj;
        if (!Objects.equals(this.idTurno, other.idTurno)) {
            return false;
        }
        if (!Objects.equals(this.idAluno, other.idAluno)) {
            return false;
        }
        return true;
    }
    
    
}
