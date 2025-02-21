package biblioteca.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Fabricio Souza.
 */
public class Conexao {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1/BIBLIOTECA";
    private static final String USUARIO = "root";
    private static final String SENHA = "root";

    public static Connection getCon() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Erro ao se conectar com o banco: " + ex.getMessage());
        }
        return con;
    }

    public static void fecharConexao(Connection con) {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    public static void fecharConexao(Connection con, PreparedStatement stmt) {
        fecharConexao(con);
        try {
            if (!stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    public static void fecharConexao(Connection con, PreparedStatement stmt, ResultSet rs) {
        fecharConexao(con, stmt);
        try {
            if (!rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }
}