package biblioteca.controller;

import biblioteca.dao.LocarDAO;
import biblioteca.model.Locar;
import biblioteca.util.DialogCreator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
public class FXMLLocarController implements Initializable {
    private final Locar locar = new Locar();
    private final LocarDAO locarDao = new LocarDAO();
    private double x,y;
    private Thread th;
    private final JFXSpinner spinner = new JFXSpinner();
    
    @FXML
    private TableColumn<Locar, Date> clDataD;
    @FXML
    private TableColumn<Locar, Integer> clDias;
    @FXML
    private TableColumn<Locar, String> clLivro;
    @FXML
    private TableColumn<Locar, Date> clDataL;
    @FXML
    private TableColumn<Locar, Long> clCodigo;
    @FXML
    private TableView<Locar> tbLocacao;
    @FXML
    private StackPane stackPane;
    @FXML
    private TableColumn<Locar, String> clLocatario;
    @FXML
    private JFXTextField tfPesquisa;
    
    private ObservableList<Locar> obsLocar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbLocacao.setPlaceholder(new Label(""));
        carregarTableView(1);
        spinner.setMinHeight(40);
        spinner.setMaxHeight(40);
        spinner.setMinWidth(40);
        spinner.setMaxWidth(40);
        
        tbLocacao.setOnMouseClicked((MouseEvent event) -> {
            if(event.getClickCount() == 2){
                inf(tbLocacao.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    void handleButtonInserir(ActionEvent event) throws IOException {
        boolean isButtonConfirmClicked = showFXMLCadLocarController(locar, 1);
        if(isButtonConfirmClicked){
            locarDao.salvar(locar);
            carregarTableView(1);
        }
    }

    @FXML
    void handleButtonEditar(ActionEvent event) {
        
    }

    @FXML
    void handleButtonRemover(ActionEvent event) {
        Locar l = tbLocacao.getSelectionModel().getSelectedItem();
        if(l != null){
            locarDao.remover(l);
            carregarTableView(1);
        }else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.error(stackPane, "Erro", "Por favor selecione um dos locatarios da lista", btn);
        }
    }
    
    private boolean showFXMLCadLocarController(Locar loc, int num) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLCadAlunoController.class.getResource("/biblioteca/view/FXMLCadLocar.fxml"));
        AnchorPane page = (AnchorPane) loader.load();      
        //Criando um Estágio de dialogStage
        Stage dialogStage = new Stage();
        if(num == 1){
            dialogStage.setTitle("Locação de livro");
        }else{
            dialogStage.setTitle("Verificação de registro");
        }
        Scene cena = new Scene(page);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(cena);
        
        //Setando o cliente no controller
        FXMLCadLocarController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setLocar(loc, num);
        //Mostra o dialog e espera até que o usuário feche
        dialogStage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        return controller.isButtonConfirmarClicked();
    }

    @FXML
    void draged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
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
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if(!th.isAlive()){
            stage.close();
        }else{
            JFXButton btn = new JFXButton("Ok");
            DialogCreator.informacao(stackPane, "Informação", "Por favor aguarde o fim da operação.", btn);
        }
    }
    
    @FXML
    public void pesquisar(){
        if(!th.isAlive()){
            locar.setLocatario(tfPesquisa.getText());
            carregarTableView(2);
        }
    }
    
    public void carregarTableView(int num){
        if(!tbLocacao.getItems().isEmpty()){
            tbLocacao.getItems().clear();
        }
        Task<Void> task = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> {
                    tbLocacao.setPlaceholder(spinner);
                });
                clCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                clDataD.setCellValueFactory(new PropertyValueFactory<>("strDataParaDevolucao"));
                clDataL.setCellValueFactory(new PropertyValueFactory<>("strDataDeLocaCao"));
                clDias.setCellValueFactory(new PropertyValueFactory<>("dias"));
                clLivro.setCellValueFactory(new PropertyValueFactory<>("livro"));
                clLocatario.setCellValueFactory(new PropertyValueFactory<>("locatario"));
                if(num == 1){
                    obsLocar = FXCollections.observableArrayList(locarDao.pesquisar(locar));
                }else if(num == 2){
                    obsLocar = FXCollections.observableArrayList(locarDao.pesquisaEspecifica(locar));
                }
                if(!obsLocar.isEmpty()) tbLocacao.setItems(obsLocar);
                Platform.runLater(() -> {
                    tbLocacao.setPlaceholder(new Label("Nenhum registro encontrado"));
                });
                return null;
            }
        };
        th = new Thread(task);
        th.start();
    }
    
    private void inf(Locar locar){
        JFXButton btn = new JFXButton("Ok");
        DialogCreator.informacao(stackPane, "Descrição geral",
                "LIVRO: " + locar.getLivro() +"\n"
                +"\nLOCATARIO: " + locar.getLocatario() +"\n"
                +"\nDATA DE LOCAÇÃO: " + locar.getStrDataDeLocaCao() +"\n"
                +"\nDIAS PARA DEVOLUÇÃO: " + locar.getDias(), btn);
    }
    
}