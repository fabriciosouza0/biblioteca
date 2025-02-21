package biblioteca.controller;

import biblioteca.dao.LivroDAO;
import biblioteca.model.Livro;
import biblioteca.util.DialogCreator;
import biblioteca.util.FieldValidate;
import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
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
public class FXMLLivroController implements Initializable {
    private double x, y;
    private List<Livro> listLivro;
    private ObservableList<Livro> observableListLivro;
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();

    //Atributos para manipulação de banco de dados
    private final LivroDAO livroDAO = new LivroDAO();
    private final Livro livro = new Livro();

    @FXML
    private StackPane stackPane;
    @FXML
    private TableView<Livro> tableViewLivro;
    @FXML
    private TableColumn<Livro, Long> tableColumnCodigo;
    @FXML
    private TableColumn<Livro, String> tableColumnTitulo;
    @FXML
    private TableColumn<Livro, String> tableColumnNomeAutor;
    @FXML
    private TableColumn<Livro, String> tableColumnQtd;
    @FXML
    private TableColumn<Livro, Long> tableColumnCodigoAutor;
    @FXML
    private TableColumn<Livro, String> tableColumnCDD;
    @FXML
    private TableColumn<Livro, Long> tableColumnCodigoCDD;
    @FXML
    private JFXTextField tfPesquisa;
    FXMLCadLivroController cadLivroController = new FXMLCadLivroController();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewLivro(1);
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);

        //Listener acionado diante de quaisquer alteração na seleção de itens do TableView
        tableViewLivro.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewLivro(newValue));

        mask();
    }

    @FXML
    public void handleButtonInserir() throws IOException, SQLException {
        boolean buttonConfirmarClicked = showFXMLCadLivroController(livro, 1);
        if (buttonConfirmarClicked) {
            livroDAO.salvar(livro);
            carregarTableViewLivro(1);
        }
    }

    @FXML
    public void handleButtonEditar() throws IOException {
        Livro l = tableViewLivro.getSelectionModel().getSelectedItem();
        if (l != null) {
            boolean buttonConfirmarClicked = showFXMLCadLivroController(l, 2);
            if (buttonConfirmarClicked) {
                livroDAO.atualizar(l);
                carregarTableViewLivro(1);
            }
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos Autor's da lista", btn);
        }
    }

    @FXML
    public void handleButtonRemover() throws SQLException {
        Livro l = tableViewLivro.getSelectionModel().getSelectedItem();
        if (l != null) {
            if (livroDAO.remover(l)) {
                carregarTableViewLivro(1);
            } else {
                JFXButton btn = new JFXButton("Ok");
                DialogCreator.error(stackPane, "Ops!", "Parece que este livro está locado no momento."
                        + "\nPara remove-lo do estoque, espere até sua devolução.", btn);
            }
        } else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos Autor's da lista", btn);
        }
    }

    private boolean showFXMLCadLivroController(Livro livro, int num) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadLivroController.class.getResource("/biblioteca/view/FXMLCadLivro.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if (num == 1) {
            dialogStage.setTitle("Cadastro de Livro");
        } else {
            dialogStage.setTitle("Editar Livro");
        }
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);

        //Setando o cliente no controller
        FXMLCadLivroController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setLivro(livro, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    //Carregar a tabela tableViewLivro
    public void carregarTableViewLivro(int num) {
        if (!tableViewLivro.getItems().isEmpty()) {
            tableViewLivro.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    tableViewLivro.setPlaceholder(spinner);
                });
                tableColumnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                tableColumnTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
                tableColumnNomeAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
                tableColumnQtd.setCellValueFactory(new PropertyValueFactory<>("qtd"));
                tableColumnCodigoAutor.setCellValueFactory(new PropertyValueFactory<>("codigoAutor"));
                tableColumnCDD.setCellValueFactory(new PropertyValueFactory<>("CDD"));
                tableColumnCodigoCDD.setCellValueFactory(new PropertyValueFactory<>("codigoCDD"));
                if (num == 1) {
                    observableListLivro = FXCollections.observableArrayList(livroDAO.pesquisar(livro));
                } else if (num == 2) {
                    observableListLivro = FXCollections.observableArrayList(livroDAO.pesquisaEspecifica(livro));
                }

                Platform.runLater(() -> {
                    tableViewLivro.setPlaceholder(new Label("Nenhum registro encontrado"));
                });

                if (observableListLivro.isEmpty()) return null;
                tableViewLivro.setItems(observableListLivro);

                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Dados selecionados na TableView
    private void selecionarItemTableViewLivro(Livro livro) {
        if (livro != null) {

        }
    }

    @FXML
    public void pesquisar() {
        if (!th.isAlive()) {
            livro.setTitulo(tfPesquisa.getText());
            System.out.println(livro.getTitulo());
            carregarTableViewLivro(2);
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

    public void mask() {
        FieldValidate.noNumbers(tfPesquisa);
    }
}