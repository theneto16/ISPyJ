package ispyj.dto.sqlite;

import java.io.Serializable;

/**
 *
 * @author ERNESTO.NAVARRO
 */
public class ConexionesDto implements Serializable{
    
    private int id_conection;
    private String nombre_censo;
    private String nombre_censo_lago;
    private String anio;
    private String usuario;
    private String pass;
    private String host;
    private int puerto;
    private String servicio;

    public ConexionesDto() {
    }

    public ConexionesDto(int id_conection, String nombre_censo, String nombre_censo_lago, String anio, String usuario, String pass, String host, int puerto, String servicio) {
        this.id_conection = id_conection;
        this.nombre_censo = nombre_censo;
        this.nombre_censo_lago = nombre_censo_lago;
        this.anio = anio;
        this.usuario = usuario;
        this.pass = pass;
        this.host = host;
        this.puerto = puerto;
        this.servicio = servicio;
    }

    public int getId_conection() {
        return id_conection;
    }

    public void setId_conection(int id_conection) {
        this.id_conection = id_conection;
    }

    public String getNombre_censo() {
        return nombre_censo;
    }

    public void setNombre_censo(String nombre_censo) {
        this.nombre_censo = nombre_censo;
    }

    public String getNombre_censo_lago() {
        return nombre_censo_lago;
    }

    public void setNombre_censo_lago(String nombre_censo_lago) {
        this.nombre_censo_lago = nombre_censo_lago;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
}
