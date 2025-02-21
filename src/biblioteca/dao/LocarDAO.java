package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.model.Locar;
import biblioteca.util.Intervalo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabricio Souza.
 */
public class LocarDAO {
    public void salvar(Locar l) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm;
        String sql = "INSERT INTO loca(CODIGO_LIVRO, CPFLOCATARIO, DATADELOCACAO, DATAPARADEVOLUCAO, ATRASADO) VALUES (?,?,?,?,?)";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setLong(1, l.getCodigoLivro());
            pstm.setString(2, l.getCodigoLocatario());
            pstm.setString(3, l.getStrDataDeLocacaoA());
            pstm.setString(4, l.getStrDataParaDevolucaoA());
            pstm.setString(5, "N");
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao locar livro: " + ex);
        } finally {
            Conexao.fecharConexao(con);
        }
    }

    public List<Locar> pesquisar(Locar l) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Locar> list = new ArrayList<>();
        String sql = "SELECT LOCA.CODIGO, ATRASADO, LIVRO.TITULO, DATADELOCACAO AS DATA, LOCATARIO.NOME, DATAPARADEVOLUCAO AS DATADEVOLUCAO "
                + "FROM LOCA "
                + "INNER JOIN LIVRO ON LIVRO.CODIGO = LOCA.CODIGO_LIVRO "
                + "INNER JOIN LOCATARIO ON LOCATARIO.CPF = LOCA.CPFLOCATARIO";
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            String updateSql;
            while (rs.next()) {
                l = getLocar(rs);
                boolean atrasado = l.getDias() <= 0;

                updateSql = "UPDATE LOCA SET ATRASADO = ? WHERE CODIGO = ?";
                pstm = con.prepareStatement(updateSql);
                pstm.setString(1, atrasado ? "Y" : "N");
                pstm.setLong(2, l.getCodigo());
                pstm.executeUpdate();

                list.add(l);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao pesquisar no banco: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return list;
    }

    public List<Locar> pesquisaEspecifica(Locar l) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Locar> list = new ArrayList<>();
        String sql = "SELECT LOCA.CODIGO, ATRASADO, LIVRO.TITULO, DATADELOCACAO AS DATA, LOCATARIO.NOME, DATAPARADEVOLUCAO AS DATADEVOLUCAO FROM loca\n" +
                "INNER JOIN locatario ON locatario.cpf = loca.cpfLocatario\n" +
                "INNER JOIN livro ON livro.codigo = loca.codigo_livro WHERE locatario.nome LIKE ?";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, "%" + l.getLocatario() + "%");
            rs = pstm.executeQuery();
            String updateSql;
            while (rs.next()) {
                l = getLocar(rs);
                boolean atrasado = l.getDias() <= 0;

                updateSql = "UPDATE LOCA SET ATRASADO = ? WHERE CODIGO = ?";
                pstm = con.prepareStatement(updateSql);
                pstm.setString(1, atrasado ? "Y" : "N");
                pstm.setLong(2, l.getCodigo());
                pstm.executeUpdate();

                list.add(l);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return list;
    }

    private Locar getLocar(ResultSet rs) throws SQLException {
        Locar l;
        l = new Locar();
        l.setCodigo(rs.getLong("CODIGO"));
        l.setLivro(rs.getString("TITULO"));
        l.setDataDeLocaCao(rs.getDate("DATA"));
        l.setLocatario(rs.getString("NOME"));
        l.setDataParaDevolucao(rs.getDate("DATADEVOLUCAO"), 0);
        l.setAtrasado(rs.getString("ATRASADO"));
        l.setDias(Intervalo.calcular(Intervalo.actualDate(), l.getStrDataParaDevolucao()));
        return l;
    }

    public void remover(Locar l) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        String sql = "DELETE FROM loca WHERE codigo = ?";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setLong(1, l.getCodigo());
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao excluir locação: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, pstm);
        }
    }

    public int nAtrasados() {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(atrasado) AS n FROM loca WHERE atrasado = 'Y'";
        int count = 0;
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        } catch (SQLException ex) {
            System.out.println("Erro ao obter número de atrasos: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return count;
    }

    public int nProfessoresComLivros() {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(locatario.codigo_professor) AS n FROM loca "
                + "INNER JOIN LOCATARIO ON LOCATARIO.CPF = LOCA.CPFLOCATARIO WHERE locatario.codigo_professor != 0";
        int count = 0;
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        } catch (SQLException ex) {
            System.out.println("Erro ao obter número de professores com livros: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return count;
    }

    public int nAlunosComLivros() {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(locatario.codigo_aluno) AS n FROM loca "
                + "INNER JOIN LOCATARIO ON LOCATARIO.CPF = LOCA.CPFLOCATARIO WHERE locatario.codigo_aluno != 0";
        int count = 0;
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            rs.next();
            count = rs.getInt("n");
        } catch (SQLException ex) {
            System.out.println("Erro ao obter número de alunos com livros: " + ex.getMessage());
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return count;
    }

}