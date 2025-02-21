package biblioteca.controller;

import biblioteca.model.Aluno;
import biblioteca.dao.AlunosDAO;
import biblioteca.dao.LocatarioDAO;
import biblioteca.util.DialogCreator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Fabricio Souza.
 */
public class FXMLAlunoController implements Initializable {

    private double x, y; // Stage x, y positions
    private final LocatarioDAO locatarioDao = new LocatarioDAO();
    private final Aluno aluno = new Aluno();
    private final AlunosDAO alunoDao = new AlunosDAO();
    private ObservableList<Aluno> obAfList;
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();

    @FXML
    private Label lbDescricao;
    @FXML
    private Label lbTel;
    @FXML
    private TableView<Aluno> tableViewAluno;
    @FXML
    private TableColumn<Aluno, Long> tableColumnAlunoCpf;
    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoTelefone;
    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoNome;
    @FXML
    private TableColumn<Aluno, String> tableColumnAlunoTurma;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label lbCodigo;
    @FXML
    private Label lbTurma;

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
        if (!th.isAlive()) {
            stage.close();
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.informacao(stackPane, "Informação", "Por favor aguarde o fim da operação.", btn);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableViewAluno.setPlaceholder(new Label(""));
        carregarTableViewAluno();
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);

        tableViewAluno.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProf(newValue));
    }

    //EVENTOS DE CLICK DE BOTÃO
    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        boolean buttonConfirmarClicked = showFXMLCadAlunoController(aluno, 1);
        if (buttonConfirmarClicked) {
            locatarioDao.salvarAluno(aluno);
            carregarTableViewAluno();
        }
    }

    @FXML
    public void handleButtonEditar() throws IOException {
        Aluno aluno = tableViewAluno.getSelectionModel().getSelectedItem();

        if (aluno != null) {
            boolean buttonConfirmarClicked = showFXMLCadAlunoController(aluno, 2);
            if (buttonConfirmarClicked) {
                locatarioDao.editAluno(aluno);
                carregarTableViewAluno();
            }
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos alunos da lista", btn);
        }
    }

    @FXML
    void handleButtonRemover() throws SQLException {
        Aluno aluno = tableViewAluno.getSelectionModel().getSelectedItem();
        if (aluno != null) {
            locatarioDao.removerAluno(aluno);
            carregarTableViewAluno();
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos alunos da lista", btn);
        }

    }

    private boolean showFXMLCadAlunoController(Aluno aluno, int num) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadAlunoController.class.getResource("/biblioteca/view/FXMLCadAluno.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if (num == 1) {
            dialogStage.setTitle("Cadastro de alunos");
        } else {
            dialogStage.setTitle("Editar cadastro do aluno");
        }
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);

        //Setando o cliente no controller
        FXMLCadAlunoController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAluno(aluno, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    //Carregar a tabela tableViewCdd
    public void carregarTableViewAluno() {
        if (!tableViewAluno.getItems().isEmpty()) {
            tableViewAluno.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    tableViewAluno.setPlaceholder(spinner);
                });
                tableColumnAlunoCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
                tableColumnAlunoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
                tableColumnAlunoTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
                tableColumnAlunoTurma.setCellValueFactory(new PropertyValueFactory<>("turmaDesc"));
                obAfList = FXCollections.observableArrayList(alunoDao.getStudents(aluno));
                Platform.runLater(() -> {
                    tableViewAluno.setPlaceholder(new Label("Nenhum registro encontrado"));
                });

                if (obAfList.isEmpty()) return null;
                tableViewAluno.setItems(obAfList);
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Dados selecionados na TableView
    private void selecionarItemTableViewProf(Aluno aluno) {
        if (aluno != null) {
            lbCodigo.setText(String.valueOf(aluno.getCpf()));
            lbDescricao.setText(aluno.getNome());
            lbTel.setText(aluno.getTelefone());
            lbTurma.setText(aluno.getTurmaDesc());
        } else {
            lbCodigo.setText("");
            lbDescricao.setText("");
            lbTel.setText("");
            lbTurma.setText("");
        }
    }
}