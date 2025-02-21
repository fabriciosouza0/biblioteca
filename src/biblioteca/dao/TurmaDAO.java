package biblioteca.dao;

import biblioteca.connection.Conexao;
import biblioteca.model.Turma;
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
public class TurmaDAO {
    public List<Turma> readerTm(Turma turma){
        Connection con = Conexao.getCon();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Turma> turmaList = new ArrayList<>();
        String sql = "SELECT * FROM TURMA";
        
        try{
            pstm = con.prepareStatement(sql);
            rs = pstm.executeQuery();
            while(rs.next()){
                turma = new Turma();
                turma.setCodigo(rs.getInt("CODIGO"));
                turma.setDescricao(rs.getString("DESCRICAO"));
                turmaList.add(turma);
            }
            
        }catch(SQLException ex){
            System.out.println("Erro ao criar nova key: "+ex);
        }finally{
            Conexao.fecharConexao(con, pstm, rs);
        }
     return turmaList; 
    }
}