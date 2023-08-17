package ispyj.controller.main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ispyj.bd.local.SQLiteConnector;
import ispyj.controller.conections.NewConectionController;
import ispyj.interfaces.ConectionChangeListenerMain;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author ERNESTO.NAVARRO
 */
public class MainController implements Initializable, ConectionChangeListenerMain {

    @FXML
    private Button buttonConnection;
    @FXML
    private Button buttonNewConection;
    @FXML
    private Button buttonOut;
    @FXML
    private Button buttonRefresh;
    @FXML
    private ComboBox comboConection;
    @FXML
    private Label label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshCombo(true);
    }

    @Override
    public void onConectionChange() {
        refreshCombo(false);
    }

    @FXML
    private void handleButtonActionConnection(ActionEvent event) {
    }

    @FXML
    private void handleButtonActionNewConnection(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ispyj/views/conections/NewConection.fxml"));
            Parent root = loader.load();
            NewConectionController newConectionController = loader.getController();
            newConectionController.setConectionChangeListener(this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Conexiones");
            stage.setOnCloseRequest((WindowEvent evento) -> {
                refreshCombo(false);
            });
//            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void handleRefreshButton(ActionEvent event) {
        refreshCombo(false);
    }

    @FXML
    private void handleButtonActionOut(ActionEvent event) {
        Platform.exit();
    }

    public void refreshCombo(boolean isPrimeraVez) {
        if (!isPrimeraVez) {
            comboConection.getItems().clear();
        }
        String query = "SELECT * FROM CONECTION";
        Connection connection = SQLiteConnector.getConnection();
        Statement statement = null;
        try {

            if (connection != null) {
                statement = connection.createStatement();
                if (statement != null) {
                    ResultSet resultSet = statement.executeQuery(query);
                    ObservableList<String> items = FXCollections.observableArrayList();

                    while (resultSet.next()) {
                        String resultado = resultSet.getString("NOMBRE_CENSO");
                        if (!resultSet.wasNull()) { // Verificar si el valor es nulo
                            items.add(resultado);
                        }
                    }

                    if (items.isEmpty()) {
                        comboConection.setDisable(true);
                    } else {
                        comboConection.setItems(items);
                        comboConection.setDisable(false);
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Ocurrio un error en la generacion de la consulta " + ex);
                }
            }
        }
    }

}
