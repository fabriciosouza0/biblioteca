package biblioteca.controller;
import biblioteca.model.Professor;
import biblioteca.util.FieldValidate;
import biblioteca.util.mask.MascarasFX;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Fabricio Souza, Israel Furtado.
 */
public class FXMLCadProfController implements Initializable {

    @FXML
    protected JFXTextField txtCpf;
    @FXML
    protected JFXTextField txtNome;
    @FXML
    protected JFXTextField txtTel;
    public static String cpf;
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Professor pf = new Professor();
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mascara();
    }
    
    @FXML
    public void handleButtonConfirmar(){
        if(validaEntradaDeDados()){
            pf.setCpf(txtCpf.getText());
            pf.setNome(txtNome.getText());
            pf.setTelefone(txtTel.getText());
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

    public Professor getProf() {
        return pf;
    }

    public void setProf(Professor pf, int num) {
       this.pf = pf;
       cpf = pf.getCpf();
       if(num == 1){
           this.txtCpf.setText("");
           this.txtCpf.setText("");
           this.txtNome.setText("");
       }else if(num == 2){
           this.txtCpf.setText(String.valueOf(cpf));
           this.txtNome.setText(pf.getNome());
           this.txtTel.setText(pf.getTelefone());
       }
    }
    
    private boolean validaEntradaDeDados(){
        String erroMenssage = "";
        if(txtCpf.getText() == null || txtCpf.getText().length() == 0){
            erroMenssage = "Cpf inválido";
            this.txtCpf.requestFocus();
        }else if(txtNome.getText() == null || txtNome.getText().length() == 0){
            erroMenssage = "Nome inválido";
            this.txtNome.requestFocus();
        }else if(txtTel.getText() == null || txtTel.getText().length() < 9){
            erroMenssage = "Telefone inválido";
            this.txtTel.requestFocus();
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
        MascarasFX.mascaraCPF(txtCpf);
        FieldValidate.noNumbers(txtNome);
        MascarasFX.mascaraTelefone(txtTel);
    }
    
}