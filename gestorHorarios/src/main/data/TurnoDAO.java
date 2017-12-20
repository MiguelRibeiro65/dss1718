/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.business.Turno;
import main.business.Turno;
import main.business.TurnoT;
import main.business.TurnoTP;

/**
 *
 * @author jose
 */
public class TurnoDAO implements Map<String,Turno>{
private Connection conn;
    
    /**
     * Apagar todos os turnos
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM turno where idTurno>0");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um id de turno existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `idTurno` FROM `turno` WHERE `idTurno`=?;";
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, key.toString());
            ResultSet rs = stm.executeQuery();
            r = rs.next();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return r;
    }
    
    /**
     * Verifica se um turno existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Turno c = (Turno) value;
        return containsKey(c.getID());
    }
    
    @Override
    public Set<Map.Entry<String,Turno>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aluno>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    /**
     * Obter um turno, dado o seu id
     * @param key
     * @return 
     */
    
    @Override
    public Turno get(Object key) {
        Turno c = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM turno WHERE idTurno=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM Aluno_has_turno WHERE Turno_idTurno=?");
                stm1.setString(1,rs.getString("idTurno"));
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> alunos = new ArrayList<>();
                while(rs1.next()) alunos.add(rs1.getString("Aluno_numero"));
                                
                if(rs.getInt("tipo")==1)
                    c = new TurnoTP(rs.getString("idTurno"),rs.getString("UC_idUC"),DayOfWeek.valueOf(rs.getString("dia")),LocalTime.parse(rs.getString("inicio")),LocalTime.parse(rs.getString("fim")),rs.getString("docente"),rs.getInt("capacidade"),alunos);
                else c = new TurnoT(rs.getString("idTurno"),rs.getString("UC_idUC"),DayOfWeek.valueOf(rs.getString("dia")),LocalTime.parse(rs.getString("inicio")),LocalTime.parse(rs.getString("fim")),rs.getString("docente"),rs.getInt("capacidade"),alunos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return c;
    }
    
    @Override
    public int hashCode() {
        return this.conn.hashCode();
    }
    
    /**
     * Verifica se existem entradas
     * @return 
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public Set<String> keySet() {
        Set<String> set = null;
        try {
            conn = Connect.connect();
            set = new HashSet<>();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM turno;");
            ResultSet rs = stm.executeQuery();
            while(rs.next()){
                set.add(rs.getString("idTurno"));
            }   
        }
        catch(SQLException e){
            System.out.printf(e.getMessage());
        } catch (ClassNotFoundException ex) { 
        Logger.getLogger(TurnoDAO.class.getName()).log(Level.SEVERE, null, ex);
    } 
        finally{
            try{
                Connect.close(conn);
            }
            catch(Exception e){
                System.out.printf(e.getMessage());
            }
        }
        return set;
    }
            
    
    
    /**
     * Insere um aluno na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Turno put(String key, Turno value) {
        Turno c = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO turno\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE dia=VALUES(dia),  inicio=VALUES(inicio), fim=VALUES(fim), docente=VALUES(docente)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getID());
            stm.setString(2, value.getIdUC());
            stm.setString(3, value.getDia().toString());
            stm.setString(4, value.getInicio().toString());
            stm.setString(5, value.getFim().toString());
            stm.setString(6, value.getDocente());
            stm.setInt(7, value.getCapacidade());
            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                value.setID(newId);
            }
            
            stm = conn.prepareStatement("INSERT INTO aluno_has_turno\n" +
                "VALUES (?, ?)\n");
            for(String id : value.getAlunos()){
                stm.setString(1, id);
                stm.setInt(2,Integer.valueOf(value.getID()));
                stm.executeUpdate();
            }
            
            c = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return c;
    }

    /**
     * Por um conjunto de turnos na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Turno> t) {
        for(Turno c : t.values()) {
            put(c.getID(), c);
        }
    }
    
    /**
     * Remover um turno, dado o seu id
     * @param key
     * @return 
     */
    @Override
    public Turno remove(Object key) {
        Turno c = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from turno where idTurno = ?");
            stm.setString(1, (String)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return c;
    }
    
    /**
     * Retorna o número de turnos na base de dados
     * @return 
     */
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM turno");
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {throw new NullPointerException(e.getMessage());}
        finally {
            Connect.close(conn);
        }
        return i;
    }
    
    /**
     * Obtém todos os turnos da base de dados
     * @return 
     */
    @Override
    public Collection<Turno> values() {
        Collection<Turno> col = new HashSet<Turno>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM turno");
            while (rs.next()) {
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM Aluno_has_turno WHERE Turno_idTurno=?");
                stm1.setString(1,rs.getString("idTurno"));
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> alunos = new ArrayList<>();
                while(rs1.next()) alunos.add(rs1.getString("Aluno_numero"));
                
                
                if(rs.getInt("tipo")==1)
                    col.add(new TurnoTP(rs.getString("idTurno"),rs.getString("UC_idUC"),DayOfWeek.valueOf(rs.getString("dia")),LocalTime.parse(rs.getString("inicio")),LocalTime.parse(rs.getString("fim")),rs.getString("docente"),rs.getInt("capacidade"),alunos));
                else col.add(new TurnoT(rs.getString("idTurno"),rs.getString("UC_idUC"),DayOfWeek.valueOf(rs.getString("dia")),LocalTime.parse(rs.getString("inicio")),LocalTime.parse(rs.getString("fim")),rs.getString("docente"),rs.getInt("capacidade"),alunos));
           }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return col;
    }
    
}


   