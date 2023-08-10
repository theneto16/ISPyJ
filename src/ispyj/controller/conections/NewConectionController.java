package ispyj.controller.conections;

import ispyj.bd.local.SQLiteConnector;
import ispyj.controller.main.MainController;
import ispyj.dto.sqlite.ConexionesDto;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ERNESTO.NAVARRO
 */
public class NewConectionController implements Initializable {

    @FXML
    private TableView<ConexionesDto> table_conections;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonEdit;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonClose;
    @FXML
    private Button buttonRefresh;

    public ConexionesDto datosConexion;
    private MainController parentController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<ConexionesDto, String> idConectionCol = new TableColumn<>("ID");
        idConectionCol.setCellValueFactory(new PropertyValueFactory<>("id_conection"));
        TableColumn<ConexionesDto, String> nombreCensoCol = new TableColumn<>("Siglas");
        nombreCensoCol.setCellValueFactory(new PropertyValueFactory<>("nombre_censo"));
        TableColumn<ConexionesDto, String> nombreCensoLarCol = new TableColumn<>("Nombre Censo");
        nombreCensoLarCol.setCellValueFactory(new PropertyValueFactory<>("nombre_censo_lago"));
        TableColumn<ConexionesDto, String> anioCol = new TableColumn<>("Año");
        anioCol.setCellValueFactory(new PropertyValueFactory<>("anio"));
        TableColumn<ConexionesDto, String> userCol = new TableColumn<>("Usuario");
        userCol.setCellValueFactory(new PropertyValueFactory<>("usuario"));
//        TableColumn<ConexionesDto, String> passCol = new TableColumn<>("Contraseña");
//        passCol.setCellValueFactory(new PropertyValueFactory<>("pass"));
        TableColumn<ConexionesDto, String> hostCol = new TableColumn<>("Host");
        hostCol.setCellValueFactory(new PropertyValueFactory<>("host"));
        TableColumn<ConexionesDto, Integer> puertoCol = new TableColumn<>("Puerto");
        puertoCol.setCellValueFactory(new PropertyValueFactory<>("puerto"));
        TableColumn<ConexionesDto, String> servCol = new TableColumn<>("Servicio");
        servCol.setCellValueFactory(new PropertyValueFactory<>("servicio"));
//        table_conections.getColumns().add(idConectionCol);
        table_conections.getColumns().add(nombreCensoCol);
        table_conections.getColumns().add(nombreCensoLarCol);
        table_conections.getColumns().add(anioCol);
        table_conections.getColumns().add(userCol);
//        table_conections.getColumns().add(passCol);
        table_conections.getColumns().add(hostCol);
        table_conections.getColumns().add(puertoCol);
        table_conections.getColumns().add(servCol);
        refreshTable(true);
    }

    @FXML
    private void handleButtonActionAdd(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ispyj/views/conections/AddConections.fxml"));
            Parent root = loader.load();
            AddConectionsController addController = loader.getController();
            addController.setParentController(this);
            addController.setConexionesData(null, 0);
            addController.setTipoProceso(0);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Agregar Nueva Conexion...");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTableSelection(MouseEvent event) {
        datosConexion = table_conections.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2) {
            if (datosConexion != null) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ispyj/views/conections/AddConections.fxml"));
                    Parent root = loader.load();
                    AddConectionsController addController = loader.getController();
                    addController.setParentController(this);
                    addController.setConexionesData(datosConexion, 1);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setTitle("Editar Nueva Conexion...");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleButtonActionEdit(ActionEvent event) {
        if (datosConexion != null) {
            try {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ispyj/views/conections/AddConections.fxml"));
                Parent root = loader.load();
                AddConectionsController addController = loader.getController();
                addController.setParentController(this);
                addController.setConexionesData(datosConexion, 1);
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("Editar Nueva Conexion...");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleButtonActionDelete(ActionEvent event) {
        if (datosConexion != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Estás seguro de que deseas eliminar el censo " + datosConexion.getNombre_censo() + ": " + datosConexion.getNombre_censo_lago());
            alert.setContentText("Esta acción no se puede deshacer.");
            ButtonType buttonTypeYes = new ButtonType("Sí");
            ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeCancel);
            // Esperar a que el usuario elija una opción
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeYes) {
                // El usuario seleccionó "Sí", eliminar el registro
                deleteConexion(datosConexion);
            }
        }

    }
    
    private void deleteConexion(ConexionesDto conexion) {
    // Aquí debes escribir el código para eliminar el registro de la base de datos SQLite.
    // Usando la conexión y el objeto ConexionesDto, ejecuta la consulta DELETE adecuada.

    // Luego, después de eliminar el registro, puedes llamar a refreshTable para actualizar el TableView:
     refreshTable(true);
}

    @FXML
    private void handleButtonActionRefresh(ActionEvent event) {
        refreshTable(false);

    }

    @FXML
    private void handleButtonActionClose(ActionEvent event) {
        Stage stage = (Stage) buttonClose.getScene().getWindow();
        stage.close();
    }

    public void refreshTable(boolean isPrimeraVez) {

        List<ConexionesDto> conexiones = new ArrayList<>();
        if (!isPrimeraVez) {
            table_conections.getItems().clear();
        }

        String query = "SELECT * FROM CONECTION";
        Connection connection = SQLiteConnector.getConnection();
        Statement statement = null;
        try {
            if (connection != null) {
                statement = connection.createStatement();
                if (statement != null) {
                    ResultSet resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        ConexionesDto vp = new ConexionesDto();
                        vp.setId_conection(resultSet.getInt("ID_CONECTION"));
                        vp.setNombre_censo(resultSet.getString("NOMBRE_CENSO"));
                        vp.setNombre_censo_lago(resultSet.getString("NOMBRE_CENSO_LARGO"));
                        vp.setAnio(resultSet.getString("ANIO"));
                        vp.setUsuario(resultSet.getString("USUARIO"));
                        vp.setPass(resultSet.getString("PASS"));
                        vp.setHost(resultSet.getString("HOST"));
                        vp.setPuerto(resultSet.getInt("PUERTO"));
                        vp.setServicio(resultSet.getString("SERVICIO"));
                        conexiones.add(vp);
                    }
                    if (conexiones.isEmpty()) {
                        table_conections.setDisable(true);
                    } else {
                        table_conections.getItems().addAll(conexiones);
                        table_conections.setDisable(false);
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

    public void setParentController(MainController controller) {
        parentController = controller;
    }

}
