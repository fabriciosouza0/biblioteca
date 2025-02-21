package biblioteca.util;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 *
 *@author Fabricio Souza.
 */
public class DialogCreator {
    public static void error(StackPane owner, String cabecario, String texto, JFXButton buttonClose){
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(cabecario));
        layout.setBody(new Text(texto));
        JFXDialog erro = new JFXDialog(owner, layout, JFXDialog.DialogTransition.CENTER);
        buttonClose.setOnAction((ActionEvent event) -> {
            erro.close();
        });
        layout.setActions(buttonClose);
        erro.show();
    }
    
    public static void informacao(StackPane owner, String cabecario, String texto, JFXButton buttonClose){
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(cabecario));
        layout.setBody(new Text(texto));
        JFXDialog information = new JFXDialog(owner, layout, JFXDialog.DialogTransition.CENTER);
        buttonClose.setOnAction((ActionEvent event) -> {
            information.close();
        });
        layout.setActions(buttonClose);
        information.show();
    }
    
}