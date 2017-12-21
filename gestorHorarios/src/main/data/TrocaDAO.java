/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.business.Aluno;
import main.business.Troca;
import main.business.Troca;
import main.data.Connect;

/**
 *
 * @author jose
 */
public class TrocaDAO implements Map<String,Troca>{
    private Connection conn;
    
    /**
     * Apagar todos os trocas
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM troca where id>0");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um número de troca existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `id` FROM `troca` WHERE `id`=?;";
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
     * Verifica se um troca existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Troca a = (Troca) value;
        return containsKey(a.getId());
    }
    
    @Override
    public Set<Map.Entry<String,Troca>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Troca>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    /**
     * Obter um troca, dado o seu número
     * @param key
     * @return 
     */
    @Override
    public Troca get(Object key) {
        Troca al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM troca WHERE id=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) al = new Troca(rs.getInt("id"),rs.getString("turno_idTurno"),rs.getString("aluno_numero")); 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return al;
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
        throw new NullPointerException("Not implemented!");
    }
    
    /**
     * Insere um troca na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Troca put(String key, Troca value) {
        Troca al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO troca(turno_idTurno,aluno_numero)\n" +
                "VALUES (?, ?)\n");
            stm.setString(1, value.getIdTurno());
            stm.setString(2, value.getIdAluno());
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                int newId = rs.getInt(1);
                value.setId(newId);
            }
            al = value;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Connect.close(conn);
        }
        return al;
    }

    /**
     * Por um conjunto de trocas na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Troca> t) {
        for(Troca a : t.values()) {
            put(String.valueOf(a.getId()),a);
        }
    }
    
    /**
     * Remover um troca, dado o seu id
     * @param key
     * @return 
     */
    @Override
    public Troca remove(Object key) {
        Troca al = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from troca where id = ?");
            stm.setInt(1, (Integer)key);
            stm.executeUpdate();
        } catch (Exception e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        return al;
    }
    
    /**
     * Retorna o número de entradas na base de dados
     * @return 
     */
    @Override
    public int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM troca");
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
     * Obtém todos os trocas da base de dados
     * @return 
     */
    @Override
    public Collection<Troca> values() {
        Collection<Troca> col = new HashSet<Troca>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM troca");
            while (rs.next()) {
                Troca a = new Troca(rs.getInt("id"),rs.getString("turno_idTurno"),rs.getString("aluno_numero"));
                col.add(a);
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


