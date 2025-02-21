package biblioteca;

import biblioteca.controller.FXMLLoginController;
import biblioteca.controller.FXMLTelaPrincipalController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Fabricio Souza.
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage stage)throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/biblioteca/view/FXMLLogin.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.centerOnScreen();
        stage.getIcons().add(new Image("/biblioteca/util/style/imgs/icon.png"));
        stage.show();
        
        FXMLLoginController.TELAPRINCIPAL.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadCDD.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadCDD.initModality(Modality.APPLICATION_MODAL);
        FXMLTelaPrincipalController.cadProf.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadProf.initModality(Modality.APPLICATION_MODAL);
        FXMLTelaPrincipalController.cadAluno.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadAluno.initModality(Modality.APPLICATION_MODAL);
        FXMLTelaPrincipalController.cadAutor.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadAutor.initModality(Modality.APPLICATION_MODAL);
        FXMLTelaPrincipalController.cadLivro.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadLivro.initModality(Modality.APPLICATION_MODAL);
        FXMLTelaPrincipalController.cadLocar.initStyle(StageStyle.TRANSPARENT);
        FXMLTelaPrincipalController.cadLocar.initModality(Modality.APPLICATION_MODAL);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop(){
        System.exit(0);
    }
    
}