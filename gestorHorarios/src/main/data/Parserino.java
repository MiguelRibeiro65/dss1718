/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.data;
import com.google.gson.Gson;
import java.util.*;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import main.business.*;
/**
 *
 * @author maria
 */
public class Parserino {
   private final static String path = "src//main//data//dados//ucsDC.json";
   private final static String pathtp = "src//main//data//dados//turnosTP.json";
   private final static String patht ="src//main//data//dados//turnosT.json";
   private final static String pathalunosUC ="src//main//data//dados//listaAlunos.json";
   private GestorTurnos gst;
   
   public Parserino(GestorTurnos a) throws FileNotFoundException, SQLException, ClassNotFoundException{
       gst = a;
       getUCS();
       getTurnosTP();
       getTurnosT();
       alunosParaUC();
   }
   
   public void getUCS() throws FileNotFoundException{
    
        JsonReader reader = new JsonReader(new FileReader(path));
       
        Gson gson = new Gson();
        
        Type cadeira  = new TypeToken<ArrayList<Cadeira>>(){}.getType();
        ArrayList<Cadeira> cadeiras = gson.fromJson(reader,cadeira);
        for(Cadeira a : cadeiras){
        gst.adicionarCadeira(a);
    }
   }
   
   public void getTurnosTP() throws FileNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(pathtp));
       
        Gson gson = new Gson();
        Type turnostp = new TypeToken<ArrayList<Turno>>(){}.getType();
        ArrayList<Turno> tps = gson.fromJson(reader,turnostp);
        for(Turno a : tps){
        gst.adicionarTurno(a);
    }
   }
   
   public void getTurnosT() throws FileNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(patht));
       
        Gson gson = new Gson();
        Type turnost = new TypeToken<ArrayList<Turno>>(){}.getType();
        ArrayList<Turno> ts = gson.fromJson(reader,turnost);
        for(Turno a : ts){
        gst.adicionarTurno(a);
        }
    }
   public void alunosParaUC() throws FileNotFoundException, SQLException, ClassNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(pathalunosUC));
       
        Gson gson = new Gson();
        Type al = new TypeToken<ArrayList<ListasUC>>(){}.getType();
        
        ArrayList<ListasUC> alunosUcs = gson.fromJson(reader,al);
        
        for(ListasUC a : alunosUcs){
        gst.adicionarAlunosUC(a);
        }
    }
}
    

