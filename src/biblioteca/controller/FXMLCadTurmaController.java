package biblioteca.controller;

import java.net.URL;
import java.util.ResourceBundle;

import biblioteca.model.Cdd;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.jfoenix.controls.JFXTextField;

import biblioteca.util.FieldValidate;

public class FXMLCadTurmaController implements Initializable {
    @FXML
    protected JFXTextField txtCodigo;
    @FXML
    protected JFXTextField txtDescricao;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    Cdd cdd = new Cdd();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mascara();
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validaEntradaDeDados()) {
            cdd.setCodigo(Long.parseLong(txtCodigo.getText()));
            cdd.setDescricao(txtDescricao.getText());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
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

    public Cdd getCdd() {
        return cdd;
    }

    public void setCdd(Cdd cdd, int num) {
        this.cdd = cdd;
        if (num == 1) {
            this.txtCodigo.setText("");
            this.txtDescricao.setText("");
        } else if (num == 2) {
            this.txtCodigo.setText(String.valueOf(cdd.getCodigo()));
            this.txtDescricao.setText(cdd.getDescricao());
        }
    }

    private boolean validaEntradaDeDados() {

        String erroMenssage = "";
        if (txtCodigo.getText() == null || txtCodigo.getText().length() == 0) {
            erroMenssage = "Código inválido";
        } else if (txtDescricao.getText() == null || txtDescricao.getText().length() == 0) {
            erroMenssage = "Nome inválido";
        }
        if (erroMenssage.length() == 0) {
            return true;
        } else {
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

    public void mascara() {
        FieldValidate.noCharacter(txtCodigo, 10);
        FieldValidate.noNumbers(txtDescricao);
    }
}
