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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import main.business.Aluno;
import main.business.Cadeira;
import main.business.TurnoTP;

/**
 *
 * @author jose
 */
public class CadeiraDAO implements Map<String,Cadeira> {

    private Connection conn;
    
    /**
     * Apagar todos as cadeiras
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE * FROM uc");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um id de uma cadeira existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `nome` FROM `uc` WHERE `acron`=?;";
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
     * Verifica se uma cadeira existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Cadeira c = (Cadeira) value;
        return containsKey(c.getAcron());
    }
    
    @Override
    public Set<Map.Entry<String,Cadeira>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aluno>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    /**
     * Obter uma cadeira, dado o seu id
     * @param key
     * @return 
     */
    
    @Override
    public Cadeira get(Object key) {
        Cadeira c = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM uc WHERE acron=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) { 
                c = new Cadeira(rs.getString("nome"),rs.getString("acron"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return c;
    }
    
        /**
     * Obter os ucs de um aluno dado o seu id
     * @param key
     * @return 
     */
    
    public List<String> getA(Object key) {
        List<String> ucs = new ArrayList<>(); 
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM uc\nINNER JOIN aluno_has_uc\nON uc.acron=aluno_has_uc.uc_acron");
            ResultSet rs = stm.executeQuery();
            while(rs.next()) {
                ucs.add(rs.getString("uc_acron"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return ucs;
    }
    
    
    @Override
    public int hashCode() {
        return this.conn.hashCode();
    }
    
    /**
     * Verifica se existem cadeiras na base de dados
     * @return 
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public Set<String> keySet() {
        throw new NullPointerException("Not implemented!");
    }
    public ListasUC putListasUC(ListasUC a) throws SQLException, ClassNotFoundException{
            ListasUC c = null;
            ArrayList<String> alunos = new ArrayList<String>();
            alunos = a.getAlunos();
            
            for(String aluno : alunos){
            
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO aluno_has_uc\n" +
                "VALUES (?, ?)\n" +
                "ON DUPLICATE KEY UPDATE aluno_numero=VALUES(aluno_numero) uc_acron=VALUES(uc_acron)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, aluno);
            stm.setString(2, a.getAcron());
            stm.executeUpdate();
            
            
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                a.setAcron(newId);
            }
            }
            c = a;
            
        return c;
    }
    /**
     * Insere uma cadeira na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Cadeira put(String key, Cadeira value) {
        Cadeira c = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO uc\n" +
                "VALUES (?, ?)\n" +
                "ON DUPLICATE KEY UPDATE nome=VALUES(nome)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getAcron());
            stm.setString(2, value.getNome());
            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                value.setAcron(newId);
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
     * Por um conjunto de cadeiras na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Cadeira> t) {
        for(Cadeira c : t.values()) {
            put(c.getAcron(), c);
        }
    }
    
    /**
     * Remover uma cadeira, dado o seu id
     * @param key
     * @return 
     */
    @Override
    public Cadeira remove(Object key) {
        Cadeira c = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from uc where acron = ?");
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
     * Retorna o número de cadeiras na base de dados
     * @return 
     */
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM uc");
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
     * Obtém todas as cadeiras da base de dados
     * @return 
     */
    @Override
    public Collection<Cadeira> values() {
        Collection<Cadeira> col = new HashSet<Cadeira>();
        Cadeira c;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM uc");
            while (rs.next()) {
                c = new Cadeira(rs.getString("nome"),rs.getString("acron"));
                col.add(c);
                
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


   