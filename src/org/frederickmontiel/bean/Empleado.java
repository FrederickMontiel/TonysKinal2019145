
package org.frederickmontiel.bean;

public class Empleado {
    private int idEmpleado ;
    private int numeroEmpleado;
    private String apellidosEmpleado ;
    private String nombresEmpleado;
    private String direccionEmpleado ;
    private String telefonoContacto ;
    private String gradoCocinero;
    private int idTipoEmpleado ;

    public Empleado() {
    }

    public Empleado(int idEmpleado, int numeroEmpleado, String apellidosEmpleado, String nombresEmpleado, String direccionEmpleado, String telefonoContacto, String gradoCocinero, int idTipoEmpleado) {
        this.idEmpleado = idEmpleado;
        this.numeroEmpleado = numeroEmpleado;
        this.apellidosEmpleado = apellidosEmpleado;
        this.nombresEmpleado = nombresEmpleado;
        this.direccionEmpleado = direccionEmpleado;
        this.telefonoContacto = telefonoContacto;
        this.gradoCocinero = gradoCocinero;
        this.idTipoEmpleado = idTipoEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(int numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getApellidosEmpleado() {
        return apellidosEmpleado;
    }

    public void setApellidosEmpleado(String apellidosEmpleado) {
        this.apellidosEmpleado = apellidosEmpleado;
    }

    public String getNombresEmpleado() {
        return nombresEmpleado;
    }

    public void setNombresEmpleado(String nombresEmpleado) {
        this.nombresEmpleado = nombresEmpleado;
    }

    public String getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(String direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getGradoCocinero() {
        return gradoCocinero;
    }

    public void setGradoCocinero(String gradoCocinero) {
        this.gradoCocinero = gradoCocinero;
    }

    public int getIdTipoEmpleado() {
        return idTipoEmpleado;
    }

    public void setIdTipoEmpleado(int idTipoEmpleado) {
        this.idTipoEmpleado = idTipoEmpleado;
    }
    
      public String toString(){
        return getIdEmpleado() + "|" + getNombresEmpleado();
    }
   
}

