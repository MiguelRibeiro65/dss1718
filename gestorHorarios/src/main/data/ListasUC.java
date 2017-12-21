
package main.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.*;

public class ListasUC {

    private String acron;
    private String aluno;

    
    public void ListasUC(){
        this.acron = "Ola";
        this.aluno = "Mundo";
    }
    public void ListasUC(String acron, String a){
        this.acron = acron;
        this.aluno = a;
    }
    
    public void ListasUC(ListasUC a){
        this.acron = a.acron;
        this.aluno = a.aluno;
    }
    
    public String getAcron() {
        return acron;
    }

    public void setAcron(String acron) {
        this.acron = acron;
    }

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String alunos) {
        this.aluno = alunos;
    }

}
