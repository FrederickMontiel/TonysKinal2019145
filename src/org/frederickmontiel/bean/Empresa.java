package org.frederickmontiel.bean;

public class Empresa {
    private int idEmpresa;
    private String nombreEmpresa;
    private String direccion ;
    private String telefono ;

    public Empresa() {
    }

    public Empresa(int idEmpresa, String nombreEmpresa, String direccion, String telefono) {
        this.idEmpresa = idEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
   
    
    public String toString(){
        return getIdEmpresa() + "|" + getNombreEmpresa();
    }
    
    
}
