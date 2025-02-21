package biblioteca.controller;


import biblioteca.dao.CddDAO;
import biblioteca.model.Cdd;
import biblioteca.util.DialogCreator;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;
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
 * FXML Controller class
 *
 * @author Fabricio Souza.
 */
public class FXMLCDDController implements Initializable {
    private double x, y;
    private ObservableList<Cdd> observableListCdd;
    //Atributos para manipulação do banco de dados
    private final CddDAO cddDAO = new CddDAO();
    private final Cdd cdd = new Cdd();
    private final JFXSpinner spinner = new JFXSpinner();

    @FXML
    private StackPane stackPane;
    @FXML
    private Label lbCodigo;
    @FXML
    private Label lbDescricao;
    @FXML
    private TableView<Cdd> tableViewCdd;
    @FXML
    private TableColumn<Cdd, Integer> tableColumnCddCodigo;
    @FXML
    private TableColumn<Cdd, String> tableColumnCddDescricao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewCdd();
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);

        //Listener acionado diante de quaisquer alteração na seleção de itens do TableView
        tableViewCdd.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selecionarItemTableViewCdd(newValue));
    }

    //EVENTOS DE CLICK DE BOTÃO
    @FXML
    public void handleButtonInserir() throws IOException {
        boolean buttonConfirmarClicked = showFXMLCadCddController(cdd, 1);
        if (buttonConfirmarClicked) {
            cddDAO.salva(cdd);
            carregarTableViewCdd();
        }
    }

    @FXML
    public void handleButtonEditar() throws IOException {
        Cdd cdd = tableViewCdd.getSelectionModel().getSelectedItem();

        if (cdd != null) {
            boolean buttonConfirmarClicked = showFXMLCadCddController(cdd, 2);
            if (buttonConfirmarClicked) {
                cddDAO.atualizar(cdd);
                carregarTableViewCdd();
            }
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos CDD's da lista", btn);
        }
    }

    @FXML
    void handleButtonRemover() {
        Cdd cdd = tableViewCdd.getSelectionModel().getSelectedItem();
        if (cdd != null) {
            cddDAO.remover(cdd);
            carregarTableViewCdd();
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos CDD's da lista", btn);
        }

    }

    private boolean showFXMLCadCddController(Cdd cdd, int num) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadCddController.class.getResource("/biblioteca/view/FXMLCadCdd.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if (num == 1) {
            dialogStage.setTitle("Cadastro de CDD");
        } else {
            dialogStage.setTitle("Editar CDD");
        }
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);

        //Setando o cliente no controller
        FXMLCadCddController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCdd(cdd, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    //Carregar a tabela tableViewCdd
    public void carregarTableViewCdd() {
        if (!tableViewCdd.getItems().isEmpty()) {
            tableViewCdd.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    tableViewCdd.setPlaceholder(spinner);
                });
                tableColumnCddCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                tableColumnCddDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
                observableListCdd = FXCollections.observableArrayList(cddDAO.pesquisar());
                tableViewCdd.setItems(observableListCdd);
                Platform.runLater(() -> {
                    tableViewCdd.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        //Objetos para efeitos visuais (Pacote de codigos localizado em: biblioteca.util.DialogCreator)
        Thread th = new Thread(task);
        th.start();
    }

    //Dados selecionados na TableView
    private void selecionarItemTableViewCdd(Cdd cdd) {
        if (cdd != null) {
            lbCodigo.setText(String.valueOf(cdd.getCodigo()));
            lbDescricao.setText(cdd.getDescricao());
        } else {
            lbCodigo.setText("");
            lbDescricao.setText("");
        }
    }

    //Métodos que controlam os eventos de arrastar/fechar/minimizar a tela.
    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (!stage.isMaximized()) {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }
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
}