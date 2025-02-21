package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.model.Professor;
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
public class ProfessoresDAO {
    // Método que gera um novo codigo para professor
    public int getLastKeyGenerated(Professor professor){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            String sql = "INSERT INTO PROFESSOR VALUES (?)";
            pstm = con.prepareStatement((sql), PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, 0);
            pstm.execute();
            
            rs = pstm.getGeneratedKeys();
            while(rs.next()){
                professor.setCodigo(rs.getInt(1));
            }
            
        }catch(SQLException ex){
            System.out.println("Erro ao gerar nova key: "+ex);
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        return professor.getCodigo();
    }
    
    public List<Professor> readerPf(Professor professor){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Professor> pfList = new ArrayList<>();
        String sql ="SELECT LOCATARIO.CPF, LOCATARIO.NOME, LOCATARIO.TELEFONE FROM LOCATARIO "+
                    "INNER JOIN PROFESSOR ON LOCATARIO.CODIGO_PROFESSOR = PROFESSOR.CODIGO";
        try{
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while(rs.next()){
                professor = new Professor();
                professor.setCpf(rs.getString("CPF"));
                professor.setNome(rs.getString("NOME"));
                professor.setTelefone(rs.getString("TELEFONE"));
                pfList.add(professor);
            }
        }catch(SQLException ex){
            System.out.println("Erro ao ler banco: "+ex);
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        return pfList;
    }
    
    public List<Professor> pesquisaEspecifica(Professor professor){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Professor> pfList = new ArrayList<>();
        String sql ="SELECT LOCATARIO.CPF, LOCATARIO.NOME, LOCATARIO.TELEFONE FROM LOCATARIO "+
                    "INNER JOIN PROFESSOR ON LOCATARIO.CODIGO_PROFESSOR = PROFESSOR.CODIGO WHERE LOCATARIO.NOME LIKE ?";
        try{
            pstm = con.prepareStatement(sql);
            pstm.setString(1, "%"+professor.getNome()+"%");
            rs = pstm.executeQuery();
            while(rs.next()){
                professor = new Professor();
                professor.setCpf(rs.getString("CPF"));
                professor.setNome(rs.getString("NOME"));
                professor.setTelefone(rs.getString("TELEFONE"));
                pfList.add(professor);
            }
        }catch(SQLException ex){
            System.out.println("Erro ao ler banco: "+ex);
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        return pfList;
    }
    
    public int nProfessores() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM professor";
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