package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.controller.FXMLCadLivroController;
import biblioteca.model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fabricio Souza.
 */
public class LivroDAO {
    public void salvar(Livro livro) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        String sql = "INSERT INTO livro(codigo,titulo,qtd,codigo_autor,codigo_cdd) values (?,?,?,?,?)";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setLong(1, livro.getCodigo());
            pstm.setString(2, livro.getTitulo());
            pstm.setLong(3, livro.getQtd());
            pstm.setLong(4, livro.getCodigoAutor());
            pstm.setLong(5, livro.getCodigoCDD());
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao salvar livro: " + ex);
        } finally {
            try {
                Conexao.fecharConexao(con, pstm);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public void atualizar(Livro livro) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        try {
            String sql = "UPDATE livro SET codigo = ?, titulo = ?, qtd = ?, codigo_autor = ?, codigo_cdd = ? WHERE codigo = ?";
            pstm = con.prepareStatement(sql);
            pstm.setLong(1, livro.getCodigo());
            pstm.setString(2, livro.getTitulo());
            pstm.setLong(3, livro.getQtd());
            pstm.setLong(4, livro.getCodigoAutor());
            pstm.setLong(5, livro.getCodigoCDD());
            pstm.setLong(6, FXMLCadLivroController.codigo);
            pstm.execute();
        } catch (SQLException ex) {
            System.out.println("Erro ao alterar livro: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm);
        }
    }

    public boolean remover(Livro livro) {
        boolean sucess = true;
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        String sql = "DELETE FROM livro WHERE codigo = ?";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setLong(1, livro.getCodigo());
            pstm.executeUpdate();
        } catch (SQLException ex) {
            sucess = false;
        } finally {
            Conexao.fecharConexao(con, pstm);
        }
        return sucess;
    }

    public List<Livro> pesquisar(Livro livro) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT livro.codigo, livro.titulo, livro.qtd, autor.nome, autor.codigo, cdd.descricao, cdd.codigo FROM livro "
                   + "INNER JOIN autor ON autor.codigo = livro.codigo_autor "
                   + "INNER JOIN cdd ON cdd.codigo = livro.codigo_cdd";
        try {
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();

            while (rs.next()) {
                livro = new Livro();
                livro.setCodigo(rs.getLong("codigo"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("nome"));
                livro.setQtd(rs.getLong("qtd"));
                livro.setCodigoAutor(rs.getLong("autor.codigo"));
                livro.setCDD(rs.getString("descricao"));
                livro.setCodigoCDD(rs.getLong("cdd.codigo"));
                lista.add(livro);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao ler banco: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return lista;
    }

    public List<Livro> pesquisaEspecifica(Livro livro) {
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Livro> lista = new ArrayList<>();
        String sql = "SELECT livro.codigo, livro.titulo, autor.nome, livro.qtd, cdd.descricao FROM livro "
                   + "INNER JOIN autor ON autor.codigo = livro.codigo_autor "
                   + "INNER JOIN cdd ON cdd.codigo = livro.codigo_cdd WHERE LIVRO.TITULO LIKE ?";
        try {
            pstm = con.prepareStatement(sql);
            pstm.setString(1, "%" + livro.getTitulo() + "%");
            rs = pstm.executeQuery();

            while (rs.next()) {
                livro = new Livro();
                livro.setCodigo(rs.getLong("codigo"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("nome"));
                livro.setQtd(rs.getLong("qtd"));
                livro.setCDD(rs.getString("descricao"));
                lista.add(livro);
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao ler banco: " + ex);
        } finally {
            Conexao.fecharConexao(con, pstm, rs);
        }
        return lista;
    }

    //Faz a contagem de registros no banco(Livros).
    public int nLivros() throws SQLException {
        Connection connection = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        String sql = "SELECT COUNT(codigo) AS n FROM livro";
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