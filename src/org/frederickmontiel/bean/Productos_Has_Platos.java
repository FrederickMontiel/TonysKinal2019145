
package org.frederickmontiel.bean;

public class Productos_Has_Platos {
    private int idProducto ;
    private int idPlato ;

    public Productos_Has_Platos() {
    }

    public Productos_Has_Platos(int idProducto, int idPlato) {
        this.idProducto = idProducto;
        this.idPlato = idPlato;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }
    
    
    
}
