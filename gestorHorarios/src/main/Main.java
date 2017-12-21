/*
 * Main
 * ruicouto in 14/dez/2015
 */
package main;

import java.io.FileNotFoundException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.business.GestorTurnos;
import main.data.Parserino;
import main.presentation.IniciarSessao;
//import main.presentation.MainWindow;

/**
 * Classe principal que inicializa dados (SGT) e abre janela principal
 * @author ruicouto
 */
public class Main {
    
    public static void main(String[] args) throws FileNotFoundException {
        GestorTurnos sgt = new GestorTurnos();
       // JFrame j = new IniciarSessao(sgt);
       // MainWindow mw = new MainWindow(sgt);
       new Parserino(sgt);
        // j.setVisible(true);
    }
}
