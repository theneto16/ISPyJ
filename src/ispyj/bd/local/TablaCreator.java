package ispyj.bd.local;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TablaCreator {

    public static void createTabla() throws SQLException {
        Connection connection = SQLiteConnector.getConnection();
        Statement statement = null;
        try {
            String query = "CREATE TABLE IF NOT EXISTS CONECTION (ID_CONECTION INTEGER PRIMARY KEY AUTOINCREMENT , NOMBRE_CENSO VARCHAR(255),"
                    + "NOMBRE_CENSO_LARGO VARCHAR(255), ANIO VARCHAR(4), USUARIO VARCHAR(255), PASS VARCHAR(255), HOST VARCHAR(255), PUERTO INTEGER, SERVICIO VARCHAR(255))";

            if (connection != null) {
                statement = connection.createStatement();
                if (statement != null) {
                    statement.execute(query);
                    System.out.println("Tabla 'CONECTION' creada exitosamente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
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
