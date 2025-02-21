package biblioteca.dao;

import biblioteca.model.Aluno;
import biblioteca.connection.Conexao;
import biblioteca.controller.FXMLCadAlunoController;
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
public class AlunosDAO {
    //Pega a ultima chave primaria gerada pelo banco.
    public int getLastGeneratedKey(Aluno aluno){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try{
            String sql = "INSERT INTO ALUNO(CODIGO, CODIGO_TURMA) VALUES (?,?)";
            pstm = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, 0);
            pstm.setInt(2, FXMLCadAlunoController.turma);
            pstm.execute();
            rs = pstm.getGeneratedKeys();
            while(rs.next()){
                aluno.setCodigo(rs.getInt(1));
            }
        }catch(SQLException ex){
            System.out.println("Erro ao executar SQL: "+ex.getMessage());
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        return aluno.getCodigo();
    }
    
    //Gera uma lista com o conteudo vindo do banco de dados.
    public List<Aluno> getStudents(Aluno aluno){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Aluno> alList = new ArrayList<>();
        String sql ="SELECT LOCATARIO.CPF, LOCATARIO.NOME, LOCATARIO.TELEFONE, TURMA.DESCRICAO, TURMA.CODIGO FROM LOCATARIO "+
                    "INNER JOIN ALUNO ON LOCATARIO.CODIGO_ALUNO = ALUNO.CODIGO "+
                    "INNER JOIN TURMA ON TURMA.CODIGO = ALUNO.CODIGO_TURMA";
        try{
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while(rs.next()){
                aluno = new Aluno();
                aluno.setCpf(rs.getString("CPF"));
                aluno.setNome(rs.getString("NOME"));
                aluno.setTelefone(rs.getString("TELEFONE"));
                aluno.setTurmaDesc(rs.getString("TURMA.DESCRICAO"));
                aluno.setTurma(rs.getInt("TURMA.CODIGO"));
                alList.add(aluno);
            }
        }catch(SQLException ex){
            System.out.println("Erro ao executar SQL: "+ex.getMessage());
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        
        return alList;
    }
    
    public List<Aluno> findStudent(Aluno aluno){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Aluno> alList = new ArrayList<>();
        String sql ="SELECT LOCATARIO.CPF, LOCATARIO.NOME, LOCATARIO.TELEFONE, TURMA.DESCRICAO, TURMA.CODIGO FROM LOCATARIO "+
                    "INNER JOIN ALUNO ON LOCATARIO.CODIGO_ALUNO = ALUNO.CODIGO "+
                    "INNER JOIN TURMA ON TURMA.CODIGO = ALUNO.CODIGO_TURMA WHERE LOCATARIO.NOME LIKE ?";
        try{
            pstm = con.prepareStatement(sql);
            pstm.setString(1, "%"+aluno.getNome()+"%");
            rs = pstm.executeQuery();
            while(rs.next()){
                aluno = new Aluno();
                aluno.setCpf(rs.getString("CPF"));
                aluno.setNome(rs.getString("NOME"));
                aluno.setTelefone(rs.getString("TELEFONE"));
                aluno.setTurmaDesc(rs.getString("TURMA.DESCRICAO"));
                aluno.setTurma(rs.getInt("TURMA.CODIGO"));
                alList.add(aluno);
            }
        }catch(SQLException ex){
            System.out.println("Erro ao executar SQL: "+ex.getMessage());
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
        return alList;
    }
    
    public int studentCount() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM aluno";
        try{
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        }catch(SQLException ex){
            System.out.println("Erro ao executar SQL: "+ex.getMessage());
        }finally{
            Conexao.fecharConexao(connection, pstm, rs);
        }
        return count;
    }
    
}