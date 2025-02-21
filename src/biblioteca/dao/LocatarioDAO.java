package biblioteca.dao;

import biblioteca.model.Aluno;
import biblioteca.connection.Conexao;
import biblioteca.controller.FXMLCadAlunoController;
import biblioteca.controller.FXMLCadProfController;
import biblioteca.model.Professor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fabricio Souza.
 */
public class LocatarioDAO {
    private final ProfessoresDAO pfD = new ProfessoresDAO();
    private final Professor pf = new Professor();
    private final AlunosDAO alD = new AlunosDAO();

    //Inicio da transação de professores.
    public void salvarProf(Professor pf) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;

        try {
            String sql = "INSERT INTO LOCATARIO(CPF,NOME,TELEFONE,CODIGO_PROFESSOR) VALUES (?,?,?,?)";
            pstm = con.prepareStatement(sql);
            pstm = con.prepareStatement(sql);
            pstm.setString(1, pf.getCpf());
            pstm.setString(2, pf.getNome());
            pstm.setString(3, pf.getTelefone());
            pstm.setInt(4, pfD.getLastKeyGenerated(this.pf));
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar locatario: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm);
        }

    }

    public void removerPf(Professor pf) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm;
        PreparedStatement pstmP;
        PreparedStatement pstmPl = null;
        ResultSet rs = null;

        try {
            pstm = con.prepareStatement("SELECT PROFESSOR.CODIGO FROM LOCATARIO INNER JOIN PROFESSOR ON PROFESSOR.CODIGO = LOCATARIO.CODIGO_PROFESSOR WHERE LOCATARIO.CPF = ?");
            pstm.setString(1, pf.getCpf());
            rs = pstm.executeQuery();
            while (rs.next()) {
                pf.setCodigo(rs.getInt(1));
            }
            pstm.close();
            pstmP = con.prepareStatement("DELETE FROM LOCATARIO WHERE CPF = ?");
            pstmP.setString(1, pf.getCpf());
            pstmP.execute();
            pstmP.close();
            pstmPl = con.prepareStatement("DELETE FROM PROFESSOR WHERE CODIGO = ?");
            pstmPl.setInt(1, pf.getCodigo());
            pstmPl.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao excluir professor: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstmPl, rs);
        }

    }

    public void editPf(Professor pf) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        try {
            String sql = "UPDATE LOCATARIO SET CPF = ?, NOME = ?, TELEFONE = ? WHERE CPF = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, pf.getCpf());
            pstm.setString(2, pf.getNome());
            pstm.setString(3, pf.getTelefone());
            pstm.setString(4, FXMLCadProfController.cpf);
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao alterar professor: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm);
        }
    }
    //Fim da transação de professores.

    //Inicio da transação de alunos.
    public void salvarAluno(Aluno aluno) {

        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;

        try {
            String sql = "INSERT INTO LOCATARIO(CPF,NOME,TELEFONE,CODIGO_ALUNO) VALUES (?,?,?,?)";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, aluno.getCpf());
            pstm.setString(2, aluno.getNome());
            pstm.setString(3, aluno.getTelefone());
            pstm.setInt(4, alD.getLastGeneratedKey(aluno));
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar locatario: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm);
        }

    }

    public void removerAluno(Aluno aluno) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm;
        PreparedStatement pstmA;
        PreparedStatement pstmA1 = null;
        ResultSet rs = null;

        try {
            pstm = con.prepareStatement("SELECT ALUNO.CODIGO FROM LOCATARIO INNER JOIN ALUNO ON ALUNO.CODIGO = LOCATARIO.CODIGO_ALUNO WHERE LOCATARIO.CPF = ?");
            pstm.setString(1, aluno.getCpf());
            rs = pstm.executeQuery();
            while (rs.next()) {
                aluno.setCodigo(rs.getInt(1));
            }
            pstm.close();
            pstmA = con.prepareStatement("DELETE FROM LOCATARIO WHERE CPF = ?");
            pstmA.setString(1, String.valueOf(aluno.getCpf()));
            pstmA.execute();
            pstmA.close();
            pstmA1 = con.prepareStatement("DELETE FROM ALUNO WHERE CODIGO = ?");
            pstmA1.setInt(1, aluno.getCodigo());
            pstmA1.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao excluir aluno: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstmA1, rs);
        }

    }

    public void editAluno(Aluno aluno) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm;
        PreparedStatement pstmA = null;
        PreparedStatement pstmA1;
        ResultSet rs = null;
        try {
            String sql = "UPDATE LOCATARIO SET CPF = ?, NOME = ?, TELEFONE = ? WHERE CPF = ?";
            pstm = con.prepareStatement(sql);
            pstm.setString(1, aluno.getCpf());
            pstm.setString(2, aluno.getNome());
            pstm.setString(3, aluno.getTelefone());
            pstm.setString(4, FXMLCadAlunoController.cpf);
            pstm.execute();
            try {
                sql = "SELECT ALUNO.CODIGO FROM LOCATARIO "
                        + "INNER JOIN ALUNO ON ALUNO.CODIGO = LOCATARIO.CODIGO_ALUNO "
                        + "WHERE LOCATARIO.CPF = ?";
                pstmA1 = con.prepareStatement(sql);
                pstmA1.setString(1, String.valueOf(FXMLCadAlunoController.cpf));
                pstmA1.execute();
                rs = pstmA1.executeQuery();
                while (rs.next()) {
                    aluno.setCodigo(rs.getInt(1));
                }
                pstmA1.close();
                sql = "UPDATE ALUNO SET CODIGO_TURMA = ? WHERE CODIGO = ?";
                pstmA = con.prepareStatement(sql);
                pstmA.setInt(1, FXMLCadAlunoController.turma);
                pstmA.setInt(2, aluno.getCodigo());
                pstmA.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("Erro ao alterar turma: " + ex);
            }

        } catch (SQLException ex) {
            System.out.println("Erro ao alterar aluno: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstmA, rs);
        }
    }
    //Fim da transação de alunos.

    //Faz a contagem de registros no banco(Alunos - Professores).
    public int nLocatarios() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(cpf) AS n FROM locatario";
        try {
            pstm = connection.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            Conexao.fecharConexao(connection, pstm, rs);
        }
        return count;
    }
}