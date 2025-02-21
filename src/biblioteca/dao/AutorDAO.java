package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.model.Autor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fabricio Souza.
 */
public class AutorDAO {
    
    //Insere um item na tabela autor do banco.
    public long salva(Autor autor){
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "INSERT INTO autor(nome) VALUES (?)";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            pstm.setString(1, autor.getNome());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar autor: "+e);
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
        return autor.getCodigo();
    } 
    
    public List<Autor> pesquisar(){
        
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM autor";
        List<Autor> lista = new ArrayList<>();
        
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            while(rs.next()){
                Autor autor = new Autor();
                autor.setCodigo(rs.getLong(1));
                autor.setNome(rs.getString(2));
                lista.add(autor);
            }
        } catch (SQLException  e) {
            System.out.println("Erro ao ler banco: "+e);
        }finally{
            Conexao.fecharConexao(connection, pstm, rs);
        }

        return lista;
    }

    //Atualizar a tabela autor
    public void atualizar(Autor autor) {
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "UPDATE autor SET nome = ? WHERE codigo = ?";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
           
            pstm.setString(1, autor.getNome());
            pstm.setLong(2, autor.getCodigo());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
    }

    //Remove um item da tabela autor do banco.
    public void remover(Autor autor) {
        
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "DELETE FROM autor WHERE codigo = ?";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            pstm.setLong(1, autor.getCodigo());
            pstm.execute();
        } catch (SQLException e) {
            e.getMessage();
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
       
    }
    
    //Faz a contagem de registros no banco(Autores).
    public int nAutores() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM autor";
        try{
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        }catch(SQLException ex){
            System.out.println(ex);
        }finally{
            Conexao.fecharConexao(connection, pstm, rs);
        }
        return count;
    }
}