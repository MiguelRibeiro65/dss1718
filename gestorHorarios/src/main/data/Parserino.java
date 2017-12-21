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
import main.business.*;
/**
 *
 * @author maria
 */
public class Parserino {
   private final static String path = "GestorHorarios//src//main//data//dados//ucsDC.json";
   private final static String pathtp = "GestorHorarios//src//main//data//dados//turnosTP.json";
   private final static String patht ="GestorHorarios//src//main//data//dados//turnosT.json";
   
   public static void main(String[] args) throws FileNotFoundException {
       getUCS();
       getTurnosTP();
       getTurnosT();
       
   }
   public static void getUCS() throws FileNotFoundException{
    
        JsonReader reader = new JsonReader(new FileReader(path));
       
        Gson gson = new Gson();
        
        Type cadeira  = new TypeToken<ArrayList<Cadeira>>(){}.getType();
        ArrayList<Cadeira> cadeiras = gson.fromJson(reader,cadeira);
        for(Cadeira a : cadeiras){
        System.out.println(a.getNome());
    }
   }
   
   public static void getTurnosTP() throws FileNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(pathtp));
       
        Gson gson = new Gson();
        Type turnostp = new TypeToken<ArrayList<Turno>>(){}.getType();
        ArrayList<Turno> tps = gson.fromJson(reader,turnostp);
        for(Turno a : tps){
        System.out.println(a.getID());
    }
   }
   
   public static void getTurnosT() throws FileNotFoundException{
        JsonReader reader = new JsonReader(new FileReader(patht));
       
        Gson gson = new Gson();
        Type turnost = new TypeToken<ArrayList<Turno>>(){}.getType();
        ArrayList<Turno> ts = gson.fromJson(reader,turnost);
        for(Turno a : ts){
        System.out.println(a.getID());
        }
    }
}
    

