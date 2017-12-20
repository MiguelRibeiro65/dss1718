/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM Aluno_has_uc WHERE uc_acron=?");
                stm1.setString(1,rs.getString("acron"));
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> alunos = new ArrayList<>();
                while(rs1.next()) alunos.add(rs1.getString("aluno_numero"));
                c = new Cadeira(rs.getString("nome"),rs.getString("acron"));
                c.setAlunos(alunos);
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
                "ON DUPLICATE KEY UPDATE nome=VALUES(nome),  acron=VALUES(acron)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getAcron());
            stm.setString(2, value.getNome());
            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                value.setAcron(newId);
            }
            
            stm = conn.prepareStatement("INSERT INTO aluno_has_uc\n" +
                "VALUES (?, ?)\n");
            if(!value.getAlunos().isEmpty()) {
                for(String id : value.getAlunos()){
                    stm.setString(1, id);
                    stm.setString(2,value.getAcron());
                    stm.executeUpdate();
                }
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
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM Aluno_has_uc WHERE uc_acron=?");
                stm1.setString(1,rs.getString("acron"));
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> alunos = new ArrayList<>();
                while(rs1.next()) alunos.add(rs1.getString("aluno_numero"));
                c = new Cadeira(rs.getString("nome"),rs.getString("acron"));
                c.setAlunos(alunos);
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


   