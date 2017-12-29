/*
 * AlunoDAO.java
 *
 * Created on 23 de Maio de 2005, 18:23
 */

package main.data;

/**
 Tabela a criar em MYSQL:
 * 
 CREATE TABLE `aluno` (
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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.business.Aluno;
import main.business.Horario;

public class AlunoDAO implements Map<String,Aluno> {
    
    private Connection conn;
    
    /**
     * Apagar todos os alunos
     */
    @Override
    public void clear () {
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            stm.executeUpdate("DELETE FROM aluno where numero>0");
        } catch (Exception e) {
            //runtime exeption!
            throw new NullPointerException(e.getMessage()); 
        } finally {
            Connect.close(conn);
        }
    }
    
    /**
     * Verifica se um número de aluno existe na base de dados
     * @param key
     * @return
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object key) throws NullPointerException {
        boolean r = false;
        try {
            conn = Connect.connect();
            String sql = "SELECT `nome` FROM `aluno` WHERE `numero`=?;";
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
     * Verifica se um aluno existe na base de dados
     * 
     * Esta implementação é provisória. Devia testar todo o objecto e não apenas a chave.
     * 
     * @param value
     * @return 
     */
    @Override
    public boolean containsValue(Object value) {
        Aluno a = (Aluno) value;
        return containsKey(a.getNumero());
    }
    
    @Override
    public Set<Map.Entry<String,Aluno>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Aluno>> entrySet() not implemented!");
    }
    
    @Override
    public boolean equals(Object o) {
        throw new NullPointerException("public boolean equals(Object o) not implemented!");
    }
    
    
    
               
    /**
     * Obter um aluno, dado o seu número
     * @param key
     * @return 
     */
    @Override
    public Aluno get(Object key) {
        Aluno al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("SELECT * FROM aluno WHERE numero=?");
            stm.setString(1,(String)key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                al = new Aluno(rs.getString("numero"),rs.getString("nome"),rs.getString("email"),rs.getString("password"),rs.getInt("estatuto"));
                   
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM aluno_has_uc WHERE aluno_numero=?");
                stm1.setString(1,(String)key);
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> ucs = new ArrayList<>();
                while(rs1.next()) ucs.add(rs1.getString("uc_acron"));
                al.setUcs(ucs);
                
                PreparedStatement stm2 = conn.prepareStatement("SELECT * FROM aluno_has_turno WHERE Aluno_numero=?");
                stm2.setString(1,(String)key);
                ResultSet rs2 = stm2.executeQuery();
                List<String> turnos = new ArrayList<>();
                while(rs2.next()) turnos.add(rs2.getString("Turno_idTurno"));
                al.setTurnos(new Horario(turnos));   
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
     * Insere um aluno na base de dados
     * @param key
     * @param value
     * @return 
     */
    @Override
    public Aluno put(String key, Aluno value) {
        Aluno al = null;
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT INTO aluno\n" +
                "VALUES (?, ?, ?, ?,?)\n" +
                "ON DUPLICATE KEY UPDATE nome=VALUES(nome),  email=VALUES(email),password=VALUES(password), estatuto=VALUES(estatuto)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, value.getNumero());
            stm.setString(2, value.getNome());
            stm.setString(3, value.getEmail());
            stm.setString(5,value.getPassword());
            stm.setInt(4, value.getEstatuto());
            stm.executeUpdate();
            
            ResultSet rs = stm.getGeneratedKeys();
            if(rs.next()) {
                String newId = rs.getString(1);
                value.setNumero(newId);
            }
            
            stm = conn.prepareStatement("INSERT IGNORE INTO aluno_has_turno\n"+"VALUES (?,?)");
            for (String id : value.getTurnos().getHorario()){
                stm.setString(1,value.getNumero());
                stm.setString(2,id);
                stm.executeUpdate();
            }
            
            stm = conn.prepareStatement("INSERT IGNORE aluno_has_uc\n"+"VALUES (?,?)");
            for (String id : value.getUcs()){
                stm.setString(1,value.getNumero());
                stm.setString(2,id);
                stm.executeUpdate();
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
     * Por um conjunto de alunos na base de dados
     * @param t 
     */
    @Override
    public void putAll(Map<? extends String,? extends Aluno> t) {
        for(Aluno a : t.values()) {
            put(a.getNumero(), a);
        }
    }
    
    /**
     * Remover um aluno, dado o seu numero
     * @param key
     * @return 
     */
    @Override
    public Aluno remove(Object key) {
        Aluno al = this.get(key);
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("delete from aluno where numero = ?");
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
            ResultSet rs = stm.executeQuery("SELECT count(*) FROM aluno");
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
     * Obtém todos os alunos da base de dados
     * @return 
     */
    @Override
    public Collection<Aluno> values() {
        Collection<Aluno> col = new HashSet<Aluno>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM aluno");
            while (rs.next()) {
                Aluno al = new Aluno(rs.getString("numero"),rs.getString("nome"),rs.getString("email"),rs.getString("password"),rs.getInt("estatuto"));
                
                PreparedStatement stm1 = conn.prepareStatement("SELECT * FROM aluno_has_uc WHERE aluno_numero=?");
                stm1.setString(1,rs.getString("numero"));
                ResultSet rs1 = stm1.executeQuery();
                ArrayList<String> ucs = new ArrayList<>();
                while(rs1.next()) ucs.add(rs1.getString("uc_acron"));
                al.setUcs(ucs);
                
                PreparedStatement stm2 = conn.prepareStatement("SELECT * FROM aluno_has_turno WHERE Aluno_numero=?");
                stm2.setString(1,rs.getString("numero"));
                ResultSet rs2 = stm2.executeQuery();
                ArrayList<String> turnos = new ArrayList<>();
                while(rs2.next()) turnos.add(rs2.getString("Turno_idTurno"));
                al.setTurnos(new Horario(turnos));   
                
                col.add(al);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            Connect.close(conn);
        }
        return col;
    }

    public void putTurnos(Aluno a)  {
        
        
        try {
            conn = Connect.connect();
            PreparedStatement stm = conn.prepareStatement("INSERT IGNORE INTO aluno_has_turno\n"+"VALUES (?,?)");
            
            for (String id : a.getTurnos().getHorario()){
                //System.out.println(id);
                stm.setString(1,a.getNumero());
                stm.setString(2,id);
                stm.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            Connect.close(conn);
        }
            
    }

    public ArrayList<String> getAlunosTurno(String turno) {
        ArrayList<String> alunos = new ArrayList<>();
        try{    
                conn = Connect.connect();
                PreparedStatement stm = conn.prepareStatement("SELECT * FROM aluno_has_turno WHERE Turno_idTurno=?");
                stm.setString(1,turno);
                ResultSet rs = stm.executeQuery();
                
                while(rs.next()) alunos.add(rs.getString("Aluno_numero"));
                
        } catch(Exception e){
        System.out.println(e.getMessage());
        } finally {
            Connect.close(conn);
        }
        
        return alunos;
    }
}
