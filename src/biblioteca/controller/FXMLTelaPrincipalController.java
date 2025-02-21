package biblioteca.controller;

import biblioteca.dao.LivroDAO;
import biblioteca.dao.LocarDAO;
import biblioteca.dao.LocatarioDAO;
import biblioteca.model.Locar;
import biblioteca.util.ScreenLoader;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fabricio Souza.
 */
public class FXMLTelaPrincipalController implements Initializable {

    //Variáveis utilizadas para guardar as coordenadas x,y do  stage.
    private double x, y;
    //Instancia do objeto ScreenLoader.
    private final ScreenLoader loader = new ScreenLoader();
    //Chamando o cdd dão para informar a quantidade de locadores/livros.
    private final LivroDAO livroDao = new LivroDAO();
    private final LocatarioDAO locatarioDao = new LocatarioDAO();
    private final Locar locar = new Locar();
    private final LocarDAO locarDao = new LocarDAO();
    //Stages e Scenes
    public static final Stage cadCDD = new Stage();
    public static final Stage cadProf = new Stage();
    public static final Stage cadAluno = new Stage();
    public static final Stage cadAutor = new Stage();
    public static final Stage cadLivro = new Stage();
    public static final Stage cadLocar = new Stage();
    private Scene cadCDDScene;
    private Scene cadProfScene;
    private Scene cadAlunoScene;
    private Scene cadAutorScene;
    private Scene cadLivroScene;
    private Scene cadLocarScene;

    public Timer timer;

    @FXML
    private GridPane gPg1;
    @FXML
    private GridPane gPg2;
    @FXML
    private Label lTotalDeLivrosCadastrados;
    @FXML
    private Text lTotalDeMembros;
    @FXML
    private Text lTotalDeLisvrosAtrasados;
    @FXML
    private Text lTotalDeProfessoresComLivros;
    @FXML
    private Text lTotalDeAlunosComLivros;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateTime();
    }

    @FXML
    public void btnNovoCdd(ActionEvent event) throws IOException {
        //Carrego o novo stage utilizando o objeto instanciado loader.
        if (!cadCDD.isShowing()) {
            loader.loadDialog(cadCDD, "/biblioteca/view/FXMLCDD.fxml", this.cadCDDScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadCDD;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnNovoProfessor() {
        if (!cadProf.isShowing()) {
            loader.loadDialog(cadProf, "/biblioteca/view/FXMLProfessor.fxml", this.cadProfScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadProf;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnNovoAluno() {
        if (!cadAluno.isShowing()) {
            loader.loadDialog(cadAluno, "/biblioteca/view/FXMLAluno.fxml", this.cadAlunoScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadAluno;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnNovoAutor() {
        if (!cadAutor.isShowing()) {
            loader.loadDialog(cadAutor, "/biblioteca/view/FXMLAutor.fxml", this.cadAutorScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadAutor;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnLocar() {
        if (!cadLocar.isShowing()) {
            loader.loadDialog(cadLocar, "/biblioteca/view/FXMLLocar.fxml", this.cadLocarScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadLocar;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnNovoLivro() {
        if (!cadLivro.isShowing()) {
            loader.loadDialog(cadLivro, "/biblioteca/view/FXMLLivro.fxml", this.cadLivroScene, false, "/biblioteca/util/style/imgs/icon.png");
        } else {
            Stage stage = cadLivro;
            stage.requestFocus();
        }
    }

    @FXML
    public void btnNextInfo() throws SQLException {
        if (gPg1.isVisible()) {
            gPg1.setVisible(false);
            gPg2.setVisible(true);
            int livros = livroDao.nLivros();
            int membros = locatarioDao.nLocatarios();
            setTotalDeLivros(String.valueOf(livros));
            setTotalDeMembros(String.valueOf(membros));
        } else if (gPg2.isVisible()) {
            gPg2.setVisible(false);
            gPg1.setVisible(true);
        }
    }

    //Métodos que controlam os eventos de arrastar/fechar/minimizar a tela(INÍCIO).
    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!stage.isMaximized()) {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }
    }

    @FXML
    public void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    public void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void max(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (!stage.isMaximized()) {
            stage.setMaximized(true);
        } else {
            stage.setMaximized(false);
        }
    }

    //Métodos que controlam os eventos de arrastar/fechar/minimizar a tela(FIM).
    public void atualizar() throws SQLException {
        int atrasados = locarDao.nAtrasados();
        setTotalDeLivrosAtrasados(String.valueOf(atrasados));
        int professores = locarDao.nProfessoresComLivros();
        setTotalDeProfessoresComLivros(String.valueOf(professores));
        int alunos = locarDao.nAlunosComLivros();
        setTotalDeAlunosComLivros(String.valueOf(alunos));
    }

    @FXML
    private void sair() throws IOException {
        Parent login = FXMLLoader.load(getClass().getResource("/biblioteca/view/FXMLLogin.fxml"));
        Scene scene = new Scene(login);
        scene.setFill(Color.TRANSPARENT);
        FXMLLoginController.TELAPRINCIPAL.setScene(scene);
        FXMLLoginController.TELAPRINCIPAL.centerOnScreen();
    }

    //Reseta o texto dos atributos Text a cada 1.5s mostrando as alterações nas tabelas do banco.
    public void updateTime() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    atualizar();
                } catch (SQLException ex) {
                    System.out.println("Erro ao atualizar: " + ex);
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1500, 1500);
    }

    //Getters e Setters dos atributos do tipo Text.
    public void setTotalDeMembros(String total) {
        lTotalDeMembros.setText(total);
    }

    public String getTotalDeMembros() {
        return lTotalDeMembros.getText();
    }

    public void setTotalDeLivros(String total) {
        lTotalDeLivrosCadastrados.setText(total);
    }

    public String getTotalDeLivros() {
        return lTotalDeLivrosCadastrados.getText();
    }

    public void setTotalDeLivrosAtrasados(String total) {
        lTotalDeLisvrosAtrasados.setText(total);
    }

    public String getTotalDeLivrosAtrasados() {
        return lTotalDeLisvrosAtrasados.getText();
    }

    private void setTotalDeProfessoresComLivros(String total) {
        lTotalDeProfessoresComLivros.setText(total);
    }

    private void setTotalDeAlunosComLivros(String total) {
        lTotalDeAlunosComLivros.setText(total);
    }

}