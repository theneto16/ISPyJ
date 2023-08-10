package ispyj.controller.conections;

import ispyj.bd.cloud.OracleConnector;
import ispyj.bd.local.SQLiteConnector;
import ispyj.dto.sqlite.ConexionesDto;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author ERNESTO.NAVARRO
 */
public class AddConectionsController implements Initializable {

    @FXML
    private Button buttonProbar;
    @FXML
    private Button buttonAgregar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private TextField siglas;
    @FXML
    private TextField anio;
    @FXML
    private TextField nombreCenso;
    @FXML
    private TextField usuario;
    @FXML
    private PasswordField pass;
    @FXML
    private TextField passShow;
    @FXML
    private TextField puerto;
    @FXML
    private TextField host;
    @FXML
    private TextField servicio;
    @FXML
    private CheckBox show;

    private int tipoProceso;
    private NewConectionController parentController;
    private ConexionesDto conexionesEdit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        anio.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() < 5) {
                return change;
            }
            return null;
        }));

        puerto.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() < 5) {
                return change;
            }
            return null;
        }));
    }

    public void setConexionesData(ConexionesDto conexionesEdit, int tipoProceso) {
        this.conexionesEdit = conexionesEdit;
        this.tipoProceso = tipoProceso;

        // Ahora puedes realizar las operaciones que dependen de conexionesEdit
        if (conexionesEdit != null) {
            if (tipoProceso == 1) {
                buttonAgregar.setText("Editar");
                siglas.setText(conexionesEdit.getNombre_censo());
                anio.setText(conexionesEdit.getAnio());
                nombreCenso.setText(conexionesEdit.getNombre_censo_lago());
                usuario.setText(conexionesEdit.getUsuario());
                pass.setText(conexionesEdit.getPass());
                puerto.setText(Integer.toString(conexionesEdit.getPuerto()));
                host.setText(conexionesEdit.getHost());
                servicio.setText(conexionesEdit.getServicio());
            }
        }
    }

    @FXML
    private void handleButtonActionProbar(ActionEvent event) {
        List<String> validacion = validations();
        if (validacion.isEmpty()) {
            OracleConnector oc = new OracleConnector();
            boolean isCorrecto = false;
            String user = usuario.getText();
            String cont = pass.getText();
            String url = "jdbc:oracle:thin:@//" + host.getText() + ":" + puerto.getText() + "/" + servicio.getText();
            isCorrecto = oc.crearConexionPrueba(cont, user, url);
            if (isCorrecto) {
                showAlertGood("Conexion exitosa.");
            } else {
                showAlertError("Error al momento de intentar conectarse, verifique los datos e intentelo de nuevo.");
            }
        } else {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Faltan campos obligatorios por llenar: ");
            for (int i = 0; i < validacion.size(); i++) {
                if (i < validacion.size() - 1) {
                    mensaje.append(validacion.get(i) + ", ");
                } else {
                    mensaje.append(validacion.get(i));
                }
            }
            showAlertError(mensaje.toString());
        }
    }

    private void showAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertGood(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conexion Correcta");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleShowPass(ActionEvent event) {
        boolean showPassword = show.isSelected();
        if (showPassword) {
            passShow.setText(pass.getText());
            pass.setVisible(false);
            passShow.setVisible(true);
            show.setText("Ocultar");
        } else {
            pass.setText(passShow.getText());
            passShow.setVisible(false);
            pass.setVisible(true);
            show.setText("Mostrar");
        }

    }

    @FXML
    private void handleButtonActionAgregar(ActionEvent event) {
        List<String> validacion = validations();
        PreparedStatement pstmt = null;
        if (validacion.isEmpty()) {
            OracleConnector oc = new OracleConnector();
            boolean isCorrecto = false;
            String user = usuario.getText();
            String cont = pass.getText();
            String url = "jdbc:oracle:thin:@//" + host.getText() + ":" + puerto.getText() + "/" + servicio.getText();
            isCorrecto = oc.crearConexionPrueba(cont, user, url);
            if (isCorrecto) {
                if (tipoProceso == 0) {
                    String insert = "INSERT INTO CONECTION (NOMBRE_CENSO, NOMBRE_CENSO_LARGO, ANIO, USUARIO, PASS, HOST, PUERTO, SERVICIO) VALUES (?,?,?,?,?,?,?,?)";
                    Connection connection = SQLiteConnector.getConnection();
                    try {
                        if (connection != null) {
                            pstmt = connection.prepareStatement(insert);
                            pstmt.setString(1, siglas.getText());
                            pstmt.setString(2, nombreCenso.getText());
                            pstmt.setString(3, anio.getText());
                            pstmt.setString(4, usuario.getText());
                            pstmt.setString(5, pass.getText());
                            pstmt.setString(6, host.getText());
                            pstmt.setString(7, puerto.getText());
                            pstmt.setString(8, servicio.getText());
                            pstmt.executeUpdate();
                            parentController.refreshTable(false); // Actualiza el TableView en el controlador padre
                            Stage stage = (Stage) buttonAgregar.getScene().getWindow();
                            stage.close();
                        }
                    } catch (SQLException e) {
                        showAlertError("Error al ejecutar el insert: " + e.getMessage());
                        System.out.println("Error al ejecutar el insert: " + e.getMessage());
                        e.printStackTrace();
                    } finally {

                        if (pstmt != null) {
                            try {
                                pstmt.close();
                            } catch (SQLException ex) {
                                showAlertError("Ocurrio un error en la generacion de la consulta " + ex);
                                System.out.println("Ocurrio un error en la generacion de la consulta " + ex);
                            }
                        }
                    }
                } else {
                    if (conexionesEdit != null) {
                        String update = "UPDATE CONECTION SET NOMBRE_CENSO=?, NOMBRE_CENSO_LARGO=?, ANIO=?, USUARIO=?, PASS=?, HOST=?, PUERTO=?, SERVICIO=? WHERE ID_CONECTION = ?";
                        Connection connection = SQLiteConnector.getConnection();
                        try {
                            if (connection != null) {
                                pstmt = connection.prepareStatement(update);
                                pstmt.setString(1, siglas.getText());
                                pstmt.setString(2, nombreCenso.getText());
                                pstmt.setString(3, anio.getText());
                                pstmt.setString(4, usuario.getText());
                                pstmt.setString(5, pass.getText());
                                pstmt.setString(6, host.getText());
                                pstmt.setString(7, puerto.getText());
                                pstmt.setString(8, servicio.getText());
                                pstmt.setInt(9, conexionesEdit.getId_conection());
                                int rowsAffect = pstmt.executeUpdate();
                                if (rowsAffect > 0) {
                                    parentController.refreshTable(false); // Actualiza el TableView en el controlador padre
                                    Stage stage = (Stage) buttonAgregar.getScene().getWindow();
                                    stage.close();
                                } else {
                                    showAlertError("Ocurrio un error en la actualizacion de datos. ");
                                }

                            }
                        } catch (SQLException e) {
                            showAlertError("Ocurrio un error en la actualizacion de datos. " + e.toString());
                            e.printStackTrace();
                        } finally {

                            if (pstmt != null) {
                                try {
                                    pstmt.close();
                                } catch (SQLException ex) {
                                    showAlertError("Ocurrio un error en la actualizacion de datos. " + ex.toString());
                                }
                            }
                        }
                    }

                }

            } else {
                showAlertError("Error al momento de intentar conectarse, verifique los datos e intentelo de nuevo.");
            }
        } else {
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Faltan campos obligatorios por llenar: ");
            for (int i = 0; i < validacion.size(); i++) {
                if (i < validacion.size() - 1) {
                    mensaje.append(validacion.get(i) + ", ");
                } else {
                    mensaje.append(validacion.get(i));
                }
            }
            showAlertError(mensaje.toString());
        }
    }

    @FXML
    private void handleButtonActionCancelar(ActionEvent event) {
        Stage stage = (Stage) buttonCancelar.getScene().getWindow();
        stage.close();
    }

    public List<String> validations() {
        List<String> faltantes = new ArrayList<String>();
        if (siglas.getText().isEmpty()) {
            faltantes.add("Siglas");
        }
        if (anio.getText().isEmpty()) {
            faltantes.add("Año");
        }
        if (nombreCenso.getText().isEmpty()) {
            faltantes.add("Nombre Censo");
        }
        if (usuario.getText().isEmpty()) {
            faltantes.add("Usuario");
        }
        if (pass.getText().isEmpty()) {
            faltantes.add("Constraseña");
        }
        if (puerto.getText().isEmpty()) {
            faltantes.add("Puerto");
        }
        if (host.getText().isEmpty()) {
            faltantes.add("Host");
        }
        if (servicio.getText().isEmpty()) {
            faltantes.add("Servicio");
        }
        return faltantes;
    }

    public void setParentController(NewConectionController controller) {
        parentController = controller;
    }

    public void setTipoProceso(int tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

}
