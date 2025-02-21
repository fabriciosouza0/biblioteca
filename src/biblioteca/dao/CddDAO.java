package biblioteca.dao;

import biblioteca.model.Cdd;
import biblioteca.connection.Conexao;
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
public class CddDAO{
    public String salva(Cdd cdd) {
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "INSERT INTO CDD (CODIGO, DESCRICAO) VALUES (?, ?)";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            pstm.setLong(1, cdd.getCodigo());
            pstm.setString(2, cdd.getDescricao());
            pstm.executeUpdate();
            return "CDD inserido com sucesso";
        } catch (SQLException e) {
            return "Erro ao cadastrar o CDD";
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
    } 
    
    public List<Cdd> pesquisar() {
        Connection connection = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM CDD";
        List<Cdd> lista = new ArrayList<>();
        
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
                while(rs.next()){
                    Cdd cdd = new Cdd();
                    cdd.setCodigo(rs.getLong(1));
                    cdd.setDescricao(rs.getString(2));
                    lista.add(cdd);
                }         
        } catch (SQLException  e) {
            System.out.println("Erro ao ler banco: "+e);
        }finally{
            Conexao.fecharConexao(connection, pstm, rs);
        }

        return lista;
    }

    //Atualizar a tabela CDD
    public void atualizar(Cdd cdd) {
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "UPDATE CDD SET descricao = ? WHERE codigo = ?";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
           
            pstm.setString(1, cdd.getDescricao());
            pstm.setLong(2, cdd.getCodigo());
            pstm.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
    }

    public void remover(Cdd cdd) {
        
        Connection connection = null;
        PreparedStatement pstm = null;
        String sql = "DELETE FROM CDD WHERE codigo = ?";
        try {
            connection = Conexao.getCon();
            pstm = connection.prepareStatement(sql);
            pstm.setLong(1, cdd.getCodigo());
            pstm.execute();
        } catch (SQLException e) {
            e.getMessage();
        }finally{
            Conexao.fecharConexao(connection, pstm);
        }
       
    }
    
    //Faz a contagem de registros no banco(CDD's).
    public int nCdds() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM cdd";
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