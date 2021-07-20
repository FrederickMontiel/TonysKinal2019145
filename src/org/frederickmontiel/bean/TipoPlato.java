
package org.frederickmontiel.bean;

public class TipoPlato {
    private int idTipoPlato;
    private String descripcion;

    public TipoPlato() {
    }

    public TipoPlato(int idTipoPlato, String descripcion) {
        this.idTipoPlato = idTipoPlato;
        this.descripcion = descripcion;
    }

    public int getIdTipoPlato() {
        return idTipoPlato;
    }

    public void setIdTipoPlato(int idTipoPlato) {
        this.idTipoPlato = idTipoPlato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String toString(){
        return getIdTipoPlato() + "|" + getDescripcion();
    }
}
