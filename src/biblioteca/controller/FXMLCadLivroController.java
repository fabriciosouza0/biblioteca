package biblioteca.controller;

import biblioteca.dao.AutorDAO;
import biblioteca.dao.CddDAO;
import biblioteca.model.Autor;
import biblioteca.model.Cdd;
import biblioteca.model.Livro;
import biblioteca.util.FieldValidate;
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
 *
 * @author Fabricio Souza.
 */
public class FXMLCadLivroController implements Initializable {

    //Componentes do FXML.
    @FXML
    protected JFXTextField txtTitulo;
    @FXML
    protected JFXTextField txtCodigo;
    @FXML
    protected JFXComboBox<Autor> cbAutor;
    @FXML
    protected JFXComboBox<Cdd> cbCDD;
    @FXML
    protected JFXTextField txtQtd;

    //Stage atual.
    private Stage dialogStage;
    //Boolean requisitada para fazer registros.
    private boolean buttonConfirmarClicked = false;
    //Instancia do obj Livro.
    private Livro livro = new Livro();
    //Variavel requisitada pela classe 'biblioteca.LivroDAO', para efetuar o registro do livro.
    public static long codigo;

    private final AutorDAO autorDao = new AutorDAO();
    private final CddDAO cddDAO = new CddDAO();
    
    //Metodo 'initialize' da interface 'Initializable'.
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	//Metodo que retorna uma 'String' para compreenção do usuario. Localizado na linha 172.
        converteComboBox();
        //Loop prenchendo a 'JFXComboBox -> cbAutor'.
        autorDao.pesquisar().stream().forEach((a) -> {
            cbAutor.getItems().add(a);
        });
	//Loop prenchendo a 'JFXComboBox -> cbCDD'.
        cddDAO.pesquisar().stream().forEach((c) -> {
            cbCDD.getItems().add(c);
        });
        //Gera a mascara para os 'JFXTextFields';
        mascara();
    }

    //Metodo responsavel pela ação de confirmar o novo registro ou alteração de um existente.
    @FXML
    public void handleButtonConfirmar(){
        if(validaEntradaDeDados()){
            //Chamada dos metodos set's do obj 'livro'.
            livro.setTitulo(txtTitulo.getText());
            livro.setQtd(Long.parseLong(txtQtd.getText()));
            livro.setCodigo(Long.parseLong(this.txtCodigo.getText()));
            livro.setCodigoAutor(cbAutor.getValue().getCodigo());
            livro.setCodigoCDD(cbCDD.getValue().getCodigo());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    //Caso cancele a inserção ou alteração do registro, este metodo será requisitado pela classe 'biblioteca.controller.FXMLLivroController'.
    @FXML
    public void handleButtonCancelar(){        
        dialogStage.close();        
    }

    //Retorna o 'stage' atual.
    public Stage getDialogStage() {
        return dialogStage;
    }

    //Seta o 'stage' atual.
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    //Caso confirme a inserção ou alteração do registro, este metodo será requisitado pela classe 'biblioteca.controller.FXMLLivroController'.
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    //Retorna o obj 'livro'.
    public Livro getLivro() {
        return this.livro;
    }

    //Seta p obj 'livro'.
    public void setLivro(Livro livro, int num){
       this.livro = livro;
       //Num se refere a operação requisitada, caso seja 1, será um cadastro, caso seja 2 uma alteração.
       if(num == 1){
           this.txtCodigo.clear();
           this.txtTitulo.clear();
           this.cbAutor.getSelectionModel().clearSelection();
           this.cbCDD.getSelectionModel().clearSelection();
       }else if(num == 2){
           codigo = livro.getCodigo();
           this.txtCodigo.setText(String.valueOf(livro.getCodigo()));
           this.txtTitulo.setText(livro.getTitulo());
           this.txtQtd.setText(String.valueOf(livro.getQtd()));
           //Recupera o valor já cadastrado da 'JFXComboBox -> cbAutor' e seta como novo value.
           cbAutor.getItems().stream().filter((a) -> (a.getCodigo() == livro.getCodigoAutor())).forEachOrdered((a) -> {
               cbAutor.setValue(a);
           });
           //Recupera o valor já cadastrado da 'JFXComboBox -> cbCDD' e seta como novo value.
           cbCDD.getItems().stream().filter((c) -> (c.getCodigo() == livro.getCodigoCDD())).forEachOrdered((c) -> {
               cbCDD.setValue(c);
           });
       }
    }

    //Valida os dados antes de qualquer alteração.
    private boolean validaEntradaDeDados(){
        String erroMenssage = "";
        if(txtCodigo.getText() == null || txtTitulo.getText().length() == 0){
            erroMenssage = "Código inválido";
            this.txtCodigo.requestFocus();
        }else if(txtTitulo.getText() == null || txtTitulo.getText().length() == 0){
            erroMenssage = "Titulo inválido";
            this.txtTitulo.requestFocus();
        }else if(cbAutor.getValue() == null){
            erroMenssage = "Autor inválido";
            this.cbAutor.requestFocus();
        }else if(cbCDD.getValue() == null){
            erroMenssage = "CDD inválido";
            this.cbCDD.requestFocus();
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

    //Metodo que mascara os(o) 'JFXTextField's'.
    public void mascara(){
    	//Metodos estaticos da classe 'biblioteca.util.FieldValidate'.
        FieldValidate.noCharacter(txtCodigo, 13);
        FieldValidate.noCharacter(txtQtd, 4);
        FieldValidate.noNumbers(txtTitulo);
    }

    private void converteComboBox(){
        cbAutor.setConverter(new StringConverter<Autor>(){
            @Override
            public String toString(Autor object){
                if(object != null){
                    return object.getNome();
                }
                return null;
            }
            @Override
            public Autor fromString(String string){ return null; };
        });
        cbCDD.setConverter(new StringConverter<Cdd>(){
            @Override
            public String toString(Cdd object){
                if(object != null){
                    return object.getDescricao();
                }
                return null;
            }
            @Override
            public Cdd fromString(String string){ return null; };
        });
    }
    
}