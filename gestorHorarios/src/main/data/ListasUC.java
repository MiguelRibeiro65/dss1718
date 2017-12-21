
package main.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.*;

public class ListasUC {

    private String acron;
    private ArrayList<String> alunos = null;
    
    public void ListasUC(){
        this.acron = "Ola";
        this.alunos = new ArrayList<String>();
    }
    public void ListasUC(String acron, ArrayList<String> a){
        this.acron = acron;
        this.alunos = a;
    }
    
    public void ListasUC(ListasUC a){
        this.acron = a.acron;
        this.alunos = a.alunos;
    }
    
    public String getAcron() {
        return acron;
    }

    public void setAcron(String acron) {
        this.acron = acron;
    }

    public ArrayList<String> getAlunos() {
        return alunos;
    }

    public void setAlunos(ArrayList<String> alunos) {
        this.alunos = alunos;
    }

}
