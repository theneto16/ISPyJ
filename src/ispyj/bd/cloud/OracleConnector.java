package ispyj.bd.cloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ERNESTO.NAVARRO
 */
public class OracleConnector {

    public OracleConnector() {
    }

    public boolean crearConexionPrueba(String pass, String user, String url) {
        boolean retorno = false;
        Connection conexion = null;
        System.out.println("Intentando conexión a la bd...");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conexion = DriverManager.getConnection(url,
                    user, pass);
            retorno = true;
        } catch (SQLException e) {
            System.out.println("No se pudo conectar la base de datos" + e);
        } catch (Exception e) {
            System.out.println("No hay driver JDBC");
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException ex) {
                    System.out.println("Ocurrio un error en la generacion de la consulta " + ex);
                }
                conexion = null;
            }
        }
        return retorno;
    }
    
    

}
