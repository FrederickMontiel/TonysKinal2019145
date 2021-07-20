package org.frederickmontiel.bean;

public class Producto {
    private int idProducto ;
    private String nombreProducto ;
    private int cantidad ;
    public Producto() {
    }

    public Producto(int idProducto, String nombreProducto, int cantidad) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
   
    
    public String toString(){
        return getIdProducto() + "|" + getNombreProducto();
    }
    
    
    
}
