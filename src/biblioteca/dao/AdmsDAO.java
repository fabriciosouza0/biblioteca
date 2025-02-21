package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.model.Adm;
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
public class AdmsDAO {
    public void salvar(Adm adm){
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        try{
            connection = Conexao.getCon();
            pstm = connection.prepareStatement("INSERT INTO adms(login, senha, nome) VALUES (?,?,?)");
            pstm.setString(1, adm.getLogin());
            pstm.setString(2, adm.getSenha());
            pstm.setString(3, adm.getNome());
            pstm.executeUpdate();
        }catch(SQLException ex){
            System.out.println("Erro ao cadastrar administrador: "+ ex);
        }finally{
            if(pstm != null){
                Conexao.fecharConexao(connection, pstm);
            }else {
                Conexao.fecharConexao(connection);
            }
        }
    }
    
    public List<Adm> get(Adm adm){
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Adm> list = new ArrayList<>();
        try{
            connection = Conexao.getCon();
            pstm = connection.prepareStatement("SELECT * FROM adms");
            rs = pstm.executeQuery();
            while(rs.next()){
                adm = new Adm();
                
                adm.setCodigo(rs.getInt("codigo"));
                adm.setLogin(rs.getString("login"));
                adm.setSenha(rs.getString("senha"));
                adm.setNome(rs.getString("nome"));
                
                list.add(adm);
            }
            
        }catch(SQLException ex){
            System.out.println("Erro ao ler banco: "+ ex);
        }finally{
            if (rs != null) {
                Conexao.fecharConexao(connection, pstm, rs);
            } else {
                Conexao.fecharConexao(connection);
            }
        }
        return list;
    }
    
    public int count() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM adms";
        try{
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
        }finally{
            if (rs != null) {
                Conexao.fecharConexao(connection, pstm, rs);
            } else {
                Conexao.fecharConexao(connection);
            }
        }
        return count;
    }
}