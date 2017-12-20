/*
 * DocenteDAO.java
 *
 * Created on 23 de Maio de 2005, 18:23
 */

package main.data;

/**
 Tabela a criar em MYSQL:
 * 
 CREATE TABLE `docente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

* 
 * @author jfc
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import main.business.Docente;

public class DocenteDAO implements Map<String,Docente> {
    
    private Connection conn;
    
    /**
     * Apagar todos os docentes
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM docente where numero>0");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um número de docente existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `nome` FROM `docente` WHERE `numero`=?;";
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
     * Verifica se um docente existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Docente a = (Docente) value;
        return containsKey(a.getNumero());
    }
    
    @Override
    public Set<Map.Entry<String,Docente>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Docente>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    /**
     * Obter um docente, dado o seu número
     * @param key
     * @return 
     */
    @Override
    public Docente get(Object key) {
        Docente al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM docente WHERE numero=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) 
                   al = new Docente(rs.getString("numero"),rs.getString("nome"),rs.getString("email"),rs.getString("password"));
            
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
     * Insere um docente na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Docente put(String key, Docente value) {
        Docente al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO docente\n" +
                "VALUES (?, ?, ?, ?)\n" +
                "ON DUPLICATE KEY UPDATE nome=VALUES(nome),email=VALUES(email),password=VALUES(password)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getNumero());
            stm.setString(2, value.getNome());
            stm.setString(3, value.getEmail());
            stm.setString(4,value.getPassword());
            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                value.setNumero(newId);
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
     * Por um conjunto de docentes na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Docente> t) {
        for(Docente a : t.values()) {
            put(a.getNumero(), a);
        }
    }
    
    /**
     * Remover um docente, dado o seu numero
     * @param key
     * @return 
     */
    @Override
    public Docente remove(Object key) {
        Docente al = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from docente where numero = ?");
            stm.setString(1, (String)key);
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
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM docente");
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
     * Obtém todos os docentes da base de dados
     * @return 
     */
    @Override
    public Collection<Docente> values() {
        Collection<Docente> col = new HashSet<Docente>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM docente");
            while (rs.next()) {
                col.add(new Docente(rs.getString("numero"),rs.getString("nome"),rs.getString("email"),rs.getString("password")));
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

