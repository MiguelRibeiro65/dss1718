/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class TurnoT extends Turno{

    public TurnoT(String id,String idUC, DayOfWeek dia, LocalTime inicio, LocalTime fim,String docente,int capac,ArrayList<String> al){
        super(id,idUC,dia,inicio,fim,docente,capac,al);
    }

}
