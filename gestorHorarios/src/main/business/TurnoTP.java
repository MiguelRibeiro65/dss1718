/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.business;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

public class TurnoTP extends Turno{

    public TurnoTP(String id,String idUC, DayOfWeek dia, LocalTime inicio, LocalTime fim,String docente,int capac){
        super(id,idUC,dia,inicio,fim,docente,capac);
    }
}
