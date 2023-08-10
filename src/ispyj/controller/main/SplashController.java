package ispyj.controller.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ERNESTO.NAVARRO
 */
public class SplashController implements Initializable {

    @FXML
    private Parent root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Simula una carga o espera de la pantalla de inicio
        new Thread(() -> {
            try {
                Thread.sleep(3000); // 3 segundos de espera
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            // Cambiar a la pantalla de inicio de sesión
            javafx.application.Platform.runLater(() -> {
                Stage ventana = (Stage) root.getScene().getWindow();
                ventana.hide();
                Stage ventanaApp = new Stage();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ispyj/views/main/Main.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    ventanaApp.setResizable(false);
                    ventanaApp.setTitle("Conectar a Base de Datos");
                    ventanaApp.setScene(scene);
//                    ventanaApp.initStyle(StageStyle.TRANSPARENT);
                    ventanaApp.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

}
