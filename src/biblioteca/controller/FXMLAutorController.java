package biblioteca.controller;

import biblioteca.dao.AutorDAO;
import biblioteca.model.Autor;
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
public class FXMLAutorController implements Initializable {
    
    private double x,y;
    //Atributos para manipulação do banco de dados
    private final AutorDAO autorDAO = new AutorDAO();
    private final Autor autor = new Autor();
    private ObservableList<Autor> observableListAutor;
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();
    
    @FXML
    private StackPane stackPane;
    @FXML
    private Label lbNomeAutor;
    @FXML
    private TableView<Autor> tableViewAutor;
    @FXML
    private TableColumn<Autor, String> tableColumnNomeAutor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableViewAutor.setPlaceholder(new Label(""));
        carregarTableViewAutor();
        spinner.setMinHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxHeight(40);
        spinner.setMaxWidth(40);

        //Listener acionado diante de quaisquer alteração na seleção de itens do TableView
        tableViewAutor.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewAutor(newValue));
    } 

    //EVENTOS DE CLICK DE BOTÃO
    @FXML
    public void handleButtonInserir() throws IOException{
        //Connection con = Conexao.abrirConexao(); //Abre a conexão com o banco de dados
        //AutorDAO AutorDAO = new AutorDAO(con); //Seta a conexão
        //Autor Autor = new Autor(); //Cria objeto do tipo do modelo
        boolean buttonConfirmarClicked = showFXMLCadAutorController(autor, 1);
        if(buttonConfirmarClicked){
            autorDAO.salva(autor);
            carregarTableViewAutor();
        }       
    }
    
    @FXML
    public void handleButtonEditar() throws IOException{       
        Autor Autor = tableViewAutor.getSelectionModel().getSelectedItem();
        
        if(Autor != null){            
            boolean buttonConfirmarClicked = showFXMLCadAutorController(Autor, 2);
            if(buttonConfirmarClicked){
                autorDAO.atualizar(Autor);            
                carregarTableViewAutor();
                
            }           
        } else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos Autor's da lista", btn);
        }               
    }
    
    @FXML void handleButtonRemover(){
        Autor Autor = tableViewAutor.getSelectionModel().getSelectedItem();
        if(Autor != null){
            autorDAO.remover(Autor);
            carregarTableViewAutor();
        }else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos Autor's da lista", btn);
        }
    }

    private boolean showFXMLCadAutorController(Autor Autor, int num) throws IOException{        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadAutorController.class.getResource("/biblioteca/view/FXMLCadAutor.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if(num == 1){
            dialogStage.setTitle("Cadastro de Autor");
        }else{
            dialogStage.setTitle("Editar Autor");
        }       
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);
        
        //Setando o cliente no controller
        FXMLCadAutorController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setAutor(Autor, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    //Carregar a tabela tableViewAutor
    public void carregarTableViewAutor(){
        if(!tableViewAutor.getItems().isEmpty()){
            tableViewAutor.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    tableViewAutor.setPlaceholder(spinner);
                });
                tableColumnNomeAutor.setCellValueFactory(new PropertyValueFactory<>("nome"));
                observableListAutor = FXCollections.observableArrayList(autorDAO.pesquisar());
                tableViewAutor.setItems(observableListAutor);
                Platform.runLater(() -> {
                    tableViewAutor.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Dados selecionados na TableView
    private void selecionarItemTableViewAutor(Autor autor) { 
        if(autor != null){
            lbNomeAutor.setText(autor.getNome());
        }else{
            lbNomeAutor.setText("");
        }
    }
    
    //Métodos que controlam os eventos de arrastar/fechar/minimizar a tela.
    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        if(!stage.isMaximized()){
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
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if(!th.isAlive()) {
            stage.close();
        }else {
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.informacao(stackPane, "", "", btn);
        }
    }
    
}