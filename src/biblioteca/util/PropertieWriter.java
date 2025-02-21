package biblioteca.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Fabricio Souza.
 */
public class PropertieWriter {
    private final Properties pt = new Properties();
    private final File url = new File("C:\\Program Files\\Biblioteca\\Config\\Config.properties");
    private String dbUrl;
    private String user;
    private String pass;

    public PropertieWriter() {
        loadProprieties();
    }

    public void loadProprieties() {
        FileInputStream fps;
        try {
            fps = new FileInputStream(url);
            pt.load(fps);
            fps.close();
        } catch (IOException ex) {
            System.out.println("Erro ao ler arquivo de configuração: " + ex.getMessage());
        }
    }

    public String getDbUrl() {
        this.dbUrl = pt.getProperty("URL");
        return this.dbUrl;
    }

    public String getDbUser() {
        this.user = pt.getProperty("USUARIO");
        return this.user;
    }

    public String getDbPass() {
        this.pass = pt.getProperty("SENHA");
        return this.pass;
    }
}