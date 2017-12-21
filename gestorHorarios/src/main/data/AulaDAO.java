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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import main.business.Aluno;
import main.business.Aula;
import main.business.Aula;

/**
 *
 * @author jose
 */
public class AulaDAO implements Map<String,Aula>{
    private Connection conn;
    
    /**
     * Apagar todos os aulas
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM aula where id>0");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um número de aula existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `id` FROM `aula` WHERE `id`=?;";
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
     * Verifica se um aula existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Aula a = (Aula) value;
        return containsKey(a.getID());
    }
    
    @Override
    public Set<Map.Entry<String,Aula>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aula>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    /**
     * Obter um aula, dado o seu número
     * @param key
     * @return 
     */
    @Override
    public Aula get(Object key) {
        Aula al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM aula WHERE id=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                   ArrayList<String> presencas = new ArrayList<>();
                   stm = conn.prepareStatement("SELECT * FROM Aluno_has_Aula WHERE Aula_id=?");
                   stm.setString(1,(String)key);
                   ResultSet rs1 = stm.executeQuery();
                   if (rs1.isBeforeFirst())
                   while(rs1.next()) presencas.add(rs1.getString("Aluno_numero"));
                   al = new Aula(rs.getString("data"),rs.getString("Turno_idTurno")); 
        }
                    
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
     * Insere um aula na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Aula put(String key, Aula value) {
        Aula al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO aula(data,Turno_idTurno)\n" +
                "VALUES (?, ?)\n");
            stm.setString(1, value.getData().toString());
            stm.setString(2, value.getIdTurno());
            stm.executeUpdate();
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                int newId = rs.getInt(1);
                value.setID(newId);
            }
            if(!value.getPresencas().isEmpty()){
                stm = conn.prepareStatement("INSERT INTO aluno_has_aula\n" +
                    "VALUES (?,?)\n");
                for(String id : value.getPresencas()){
                    stm.setString(1, id);
                    stm.setInt(2,value.getID());
                    stm.executeUpdate();
                }
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
     * Por um conjunto de aulas na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Aula> t) {
        for(Aula a : t.values()) {
            put(String.valueOf(a.getID()),a);
        }
    }
    
    /**
     * Remover um aula, dado o seu id
     * @param key
     * @return 
     */
    @Override
    public Aula remove(Object key) {
        Aula al = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from aula where id = ?");
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
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM aula");
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
     * Obtém todos os aulas da base de dados
     * @return 
     */
    @Override
    public Collection<Aula> values() {
        Collection<Aula> col = new HashSet<Aula>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM aula");
            while (rs.next()) {
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM Aluno_has_Aula WHERE Aula_id=?");
                stm1.setInt(1,rs.getInt("id"));
                ResultSet rs1 = stm1.executeQuery();
                Aula a = new Aula(rs.getString("data"),String.valueOf(rs.getInt("Turno_idTurno")));
                col.add(a);
                if(rs1.next()){
                ArrayList<String> presencas = new ArrayList<>();
                while(rs1.next()) presencas.add(rs1.getString("Aluno_numero"));
                a.setPresencas(presencas);
                }
                
                
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


