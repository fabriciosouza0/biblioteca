package biblioteca.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fabricio Souza.
 */
public class Log {
    private static final File URL = new File("C:\\Program Files\\Biblioteca\\Logs.txt");
    private static String user;
    private static Date data;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static FileWriter fw;
    private static PrintWriter pw;
    
    public static void generateUserLog(String user){
        data = new Date();
        String strData = SDF.format(data.getTime());
        try {
            fw = new FileWriter(URL);
            pw = new PrintWriter(fw);
            pw.println("#--------------------Login--------------------#");
            pw.println("# Usuário: "+user);
            pw.println("# Data de acesso: "+strData);
            pw.println("#--------------------Login--------------------#");
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                if(fw != null)
                    fw.close();
                if(pw != null)
                    pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    public static void generateLocaLog(String user, Date dataL, Date dataD, String locatario){
        data = new Date();
        String strData = SDF.format(data.getTime());
        try {
            fw = new FileWriter(URL);
            pw = new PrintWriter(fw);
            pw.println("#--------------------Locação de livro--------------------#");
            pw.println("# Responsavel: "+user);
            pw.println("# Data de locação: "+strData);
            pw.println("# Locatario: "+strData);
            pw.println("# Data para devolução: "+strData);
            pw.println("#--------------------Locação de livro--------------------#");
        } catch (IOException ex) {
            ex.printStackTrace();
        }finally{
            try {
                if(fw != null)
                    fw.close();
                if(pw != null)
                    pw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            } 
        }
    }

    public String getUser() {
        return user;
    }

    public void setUser(String usuario) {
        user = usuario;
    }
}