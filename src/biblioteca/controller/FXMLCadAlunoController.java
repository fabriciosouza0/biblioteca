package biblioteca.controller;

import biblioteca.dao.TurmaDAO;
import biblioteca.model.Aluno;
import biblioteca.model.Turma;
import biblioteca.util.FieldValidate;
import biblioteca.util.mask.MascarasFX;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Fabricio Souza.
 */
public class FXMLCadAlunoController implements Initializable {
    @FXML
    protected JFXTextField txtCpf;
    @FXML
    protected JFXTextField txtNome;
    @FXML
    protected JFXTextField txtTel;
    @FXML
    protected JFXComboBox<Turma> cbTurmas;
    public static String cpf;
    public static int turma;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Aluno aluno = new Aluno();
    private final TurmaDAO tDao = new TurmaDAO();
    private final Turma tm = new Turma();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mascara();
        cbTurmas.setPromptText("Turma");
        converteComboBox();
        tDao.readerTm(tm).forEach(cbTurmas.getItems()::add);
    }

    @FXML
    public void handleButtonConfirmar() {
        if (validaEntradaDeDados()) {
            aluno.setCpf(txtCpf.getText());
            aluno.setNome(txtNome.getText());
            aluno.setTelefone(txtTel.getText());
            turma = cbTurmas.getValue().getCodigo();
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

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno al, int num) {
        this.aluno = al;
        cpf = al.getCpf();
        if (num == 1) {
            this.txtCpf.setText("");
            this.txtCpf.setText("");
            this.txtNome.setText("");
        } else if (num == 2) {
            converteComboBox();
            turma = al.getTurma();
            this.txtCpf.setText(String.valueOf(cpf));
            this.txtNome.setText(al.getNome());
            this.txtTel.setText(al.getTelefone());
            cbTurmas.getItems().stream().filter((t) -> (al.getTurma() == t.getCodigo())).forEachOrdered((t) -> {
                cbTurmas.setValue(t);
            });
        }
    }

    private boolean validaEntradaDeDados() {
        String erroMenssage = "";
        if (txtCpf.getText() == null || txtCpf.getText().length() == 0) {
            erroMenssage = "Cpf inválido";
            this.txtCpf.requestFocus();
        } else if (txtNome.getText() == null || txtNome.getText().length() == 0) {
            erroMenssage = "Nome inválido";
            this.txtNome.requestFocus();
        } else if (txtTel.getText() == null || txtTel.getText().length() < 9) {
            erroMenssage = "Telefone inválido";
            this.txtTel.requestFocus();
        } else if (cbTurmas.getValue() == null) {
            erroMenssage = "Turma inválida";
            this.cbTurmas.requestFocus();
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
        MascarasFX.mascaraCPF(txtCpf);
        FieldValidate.noNumbers(txtNome);
        MascarasFX.mascaraTelefone(txtTel);
    }

    private void converteComboBox() {
        cbTurmas.setConverter(new StringConverter<Turma>() {
            @Override
            public String toString(Turma object) {
                if (object != null) {
                    return object.getDescricao();
                }
                return null;
            }

            @Override
            public Turma fromString(String string) {
                return null;
            }
        });
    }
}