/*
 * Main
 * ruicouto in 14/dez/2015
 */
package main;

import main.business.GestorTurnos;
import main.presentation.MainWindow;

/**
 * Classe principal que inicializa dados (SGT) e abre janela principal
 * @author ruicouto
 */
public class Main {
    
    public static void main(String[] args) {
        GestorTurnos sgt = new GestorTurnos();
        
        MainWindow mw = new MainWindow(sgt);
        
        mw.setVisible(true);
    }
}
