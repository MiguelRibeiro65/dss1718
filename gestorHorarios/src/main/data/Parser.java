/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.data;
import java.lang.Object;
import com.google.gson.Gson;
import main.business.Cadeira;
/**
 *
 * @author maria
 */
public class Parser {
   
        
    private static Cadeira daUC(){
        String jsonLine = "{'acron' :'BI-DW','id':'H505N43','nome':'BI-UC2-Data Warehousing','diasem:sex'}";
        
        
        Cadeira a = new Cadeira(gson.fromJson(jsonLine,Cadeira.class));
        System.out.println(a.toString());
        return a;
    }
}
    
