/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package ispyj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ispyj.bd.local.TablaCreator;

/**
 *
 * @author ERNESTO.NAVARRO
 */
public class ISPyJ extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        TablaCreator.createTabla(); // Crear la tabla
        Parent root = FXMLLoader.load(getClass().getResource("/ispyj/views/main/Splash.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
