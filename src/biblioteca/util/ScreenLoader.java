package biblioteca.util;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Fabricio Souza.
 */
public class ScreenLoader {
    public void load(Stage stage, String url, Scene scene, boolean resizable) {
        try {
            Parent screen = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(url)));
            scene = new Scene(screen);
        } catch (IOException ex) {
            System.out.println("Tela não encontrada: " + ex);
        }
        stage.setScene(scene);
        stage.resizableProperty().set(resizable);
        stage.centerOnScreen();
        stage.show();
    }

    public void load(Stage stage, String url, Scene scene, boolean resizable, String icon_url) {
        try {
            Parent screen = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(url)));
            scene = new Scene(screen);
        } catch (IOException ex) {
            System.out.println("Tela não encontrada: " + ex);
        }
        stage.setScene(scene);
        stage.resizableProperty().set(resizable);
        stage.getIcons().add(new Image(icon_url));
        stage.centerOnScreen();
        stage.show();
    }

    public void loadDialog(Stage stage, String url, Scene scene, boolean resizable, String icon_url) {
        try {
            Parent screen = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(url)));
            scene = new Scene(screen);
        } catch (IOException ex) {
            System.out.println("Tela não encontrada: " + ex);
        }
        stage.setScene(scene);
        stage.resizableProperty().set(resizable);
        stage.getIcons().add(new Image(icon_url));
        stage.centerOnScreen();
        stage.show();
    }
}