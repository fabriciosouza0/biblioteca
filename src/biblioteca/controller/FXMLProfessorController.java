package biblioteca.controller;

import biblioteca.dao.LocatarioDAO;
import biblioteca.dao.ProfessoresDAO;
import biblioteca.model.Professor;
import biblioteca.util.DialogCreator;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
 *
 * @author Fabricio Souza.
 */
public class FXMLProfessorController implements Initializable {
    
    private double x,y;
    private final LocatarioDAO locatarioDao = new LocatarioDAO();
    private final Professor professor = new Professor();
    private final ProfessoresDAO professorDao = new ProfessoresDAO();
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();
    
    @FXML
    private Label lbDescricao;
    @FXML
    private Label lbTel;
    @FXML
    private TableView<Professor> tableViewProf;
    @FXML
    private TableColumn<Professor, String> tableColumnProfCpf;
    @FXML
    private TableColumn<Professor, String> tableColumnProfTelefone;
    @FXML
    private TableColumn<Professor, String> tableColumnProfNome;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label lbCodigo;
    
    private ObservableList<Professor> obPfList;

    
    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        if(!stage.isMaximized()){
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
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarTableViewProf();
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);

        tableViewProf.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProf(newValue));
    }
    
    //EVENTOS DE CLICK DE BOTÃO
    @FXML
    public void handleButtonInserir() throws IOException, SQLException{
        //Connection con = Conexao.abrirConexao(); //Abre a conexão com o banco de dados
        //CddDAO cddDAO = new CddDAO(con); //Seta a conexão
        //Cdd cdd = new Cdd(); //Cria objeto do tipo do modelo
        
        boolean buttonConfirmarClicked = showFXMLCadProfController(professor, 1);
        if(buttonConfirmarClicked){
            locatarioDao.salvarProf(professor);
            carregarTableViewProf();
        }       
    }
    
    @FXML
    public void handleButtonEditar() throws IOException{       
        Professor professor = tableViewProf.getSelectionModel().getSelectedItem();
        if(professor != null){            
            boolean buttonConfirmarClicked = showFXMLCadProfController(professor, 2);
            if(buttonConfirmarClicked){   
                locatarioDao.editPf(professor);
                carregarTableViewProf();
            }           
        } else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos professores da lista", btn);
        }               
    }
    
    @FXML
    void handleButtonRemover() throws SQLException{
        Professor professor = tableViewProf.getSelectionModel().getSelectedItem();
        if(professor != null){
            locatarioDao.removerPf(professor);
            carregarTableViewProf();
        }else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos professores da lista", btn);
        }
    }

    private boolean showFXMLCadProfController(Professor professor, int num) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadProfController.class.getResource("/biblioteca/view/FXMLCadProf.fxml"));
        AnchorPane page = (AnchorPane) loader.load();      
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if(num == 1){
            dialogStage.setTitle("Cadastro de Professores");
        }else{
            dialogStage.setTitle("Editar cadastro do professor");
        }       
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);
        
        //Setando o cliente no controller
        FXMLCadProfController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProf(professor, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    //Carregar a tabela tableViewCdd
    public void carregarTableViewProf(){
        if(!tableViewProf.getItems().isEmpty()){
            tableViewProf.getItems().clear();
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() ->{
                    tableViewProf.setPlaceholder(spinner);
                });
                tableColumnProfCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
                tableColumnProfNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
                tableColumnProfTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
                obPfList = FXCollections.observableArrayList(professorDao.readerPf(professor));
                tableViewProf.setItems(obPfList);
                Platform.runLater(() ->{
                    tableViewProf.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }

    //Dados selecionados na TableView
    private void selecionarItemTableViewProf(Professor professor) {
        if(professor != null){
            lbCodigo.setText(String.valueOf(professor.getCpf()));
            lbDescricao.setText(professor.getNome());
            lbTel.setText(professor.getTelefone());
        }else{
            lbCodigo.setText("");
            lbDescricao.setText("");
            lbTel.setText("");
        }
    }
    
}