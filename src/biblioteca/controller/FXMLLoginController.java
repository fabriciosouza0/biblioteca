package biblioteca.controller;

import biblioteca.dao.AdmsDAO;
import biblioteca.model.Adm;
import biblioteca.util.DialogCreator;
import biblioteca.util.ScreenLoader;
import biblioteca.util.mask.MascarasFX;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Fabricio Souza.
 */
public class FXMLLoginController implements Initializable {
    private double x, y;
    private final AdmsDAO admsDao = new AdmsDAO();
    private final Adm adm = new Adm();
    private final ScreenLoader loader = new ScreenLoader();
    public static final Stage TELAPRINCIPAL = new Stage();
    private Scene telaPrincipalScene;
    private final JFXButton btn = new JFXButton("Ok");

    @FXML
    private StackPane stackPane;
    @FXML
    private JFXPasswordField senha;
    @FXML
    private JFXTextField login;

    private boolean encontrou;

    @FXML
    void verificar(ActionEvent event) {
        this.encontrou = false;
        try {
            admsDao.get(adm).forEach((adm) -> {
                if ((adm.getLogin().equals(this.login.getText())) && (adm.getSenha().equals(this.senha.getText()))) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                    loader.load(TELAPRINCIPAL, "/biblioteca/view/FXMLTelaPrincipal.fxml", telaPrincipalScene, true, "/biblioteca/util/style/imgs/icon.png");
                    this.encontrou = true;
                }
            });
        } catch (Exception ex) {
            System.out.println(ex);
        }
        if (!this.encontrou) {
            DialogCreator.error(stackPane, "Erro", "Usário ou senha incorretos "
                    + "\n"
                    + "Por favor verifique as informações de login e senha.", btn);
        }
    }

    @FXML
    public void sobre() {
        DialogCreator.informacao(stackPane, "Informações sobre o software", "Sistema desenvolvido pelo aluno Fabricio Souza,"
                + "\nNo intuíto de ajudar no gerenciamento da entrada e saída de livros da biblioteca.\n"
                + "\n"
                + "Data do desenvolvimento: 30/03/2018", btn);
    }

    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MascarasFX.mascaraCPF(login);
    }
}