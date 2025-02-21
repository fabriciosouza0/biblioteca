package biblioteca.controller;

import biblioteca.model.Autor;
import biblioteca.util.FieldValidate;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fabricio Souza, Israel Furtado.
 */
public class FXMLCadAutorController implements Initializable {
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Autor autor = new Autor();
    
    @FXML
    protected JFXTextField txtNomeAutor;
    @FXML
    protected ChoiceBox<String> choiceTurmas;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mascara();
    }
    
    @FXML
    public void handleButtonConfirmar(){
        if(validaEntradaDeDados()){
            autor.setNome(txtNomeAutor.getText());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
     @FXML
    public void handleButtonCancelar(){        
        dialogStage.close();        
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public Autor getAluno() {
        return this.autor;
    }

    public void setAutor(Autor autor, int num) {
       this.autor = autor;
       if(num == 1){
           this.txtNomeAutor.setText("");
       }else if(num == 2){
           this.txtNomeAutor.setText(autor.getNome());
       }
    }
    
    private boolean validaEntradaDeDados(){
        
        String erroMenssage = "";
        if(txtNomeAutor.getText() == null || txtNomeAutor.getText().length() == 0){
            erroMenssage = "Nome inválido";
            this.txtNomeAutor.requestFocus();
        }
        if(erroMenssage.length() == 0){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(erroMenssage);
            alert.show();
            return false;
        }
    }
    
    public void mascara(){
        FieldValidate.noNumbers(txtNomeAutor);
    }
    
}