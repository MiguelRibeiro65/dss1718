/*
 * Main
 * ruicouto in 14/dez/2015
 */
package main;

import java.io.FileNotFoundException;
import java.sql.SQLException;
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
    
    public static void main(String[] args) throws FileNotFoundException, SQLException, ClassNotFoundException {
        GestorTurnos sgt = new GestorTurnos();
        //new Parserino(sgt);
        JFrame j = new IniciarSessao(sgt);
        //MainWindow mw = new MainWindow(sgt);
       
        j.setVisible(true);
    }
}
