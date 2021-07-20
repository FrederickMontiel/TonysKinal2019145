
package org.frederickmontiel.bean;

public class Plato {
    private int idPlato ;
    private int cantidad;
    private String nombrePlato;
    private String descripcionPlato ;
    private double precioPlato ;
    private int idTipoPlato ;

    public Plato() {
    }

    public Plato(int idPlato, int cantidad, String nombrePlato, String descripcionPlato, double precioPlato, int idTipoPlato) {
        this.idPlato = idPlato;
        this.cantidad = cantidad;
        this.nombrePlato = nombrePlato;
        this.descripcionPlato = descripcionPlato;
        this.precioPlato = precioPlato;
        this.idTipoPlato = idTipoPlato;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getDescripcionPlato() {
        return descripcionPlato;
    }

    public void setDescripcionPlato(String descripcionPlato) {
        this.descripcionPlato = descripcionPlato;
    }

    public double getPrecioPlato() {
        return precioPlato;
    }

    public void setPrecioPlato(double precioPlato) {
        this.precioPlato = precioPlato;
    }

    public int getIdTipoPlato() {
        return idTipoPlato;
    }

    public void setIdTipoPlato(int idTipoPlato) {
        this.idTipoPlato = idTipoPlato;
    }
    
     public String toString(){
        return getIdPlato() + "|" + getNombrePlato();
    }
    
}
