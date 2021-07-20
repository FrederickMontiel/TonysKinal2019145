
package org.frederickmontiel.bean;

public class Servicios_Has_Platos {
    private int idServicio ;
    private int idPlato;

    public Servicios_Has_Platos() {
    }

    public Servicios_Has_Platos(int idServicio, int idPlato) {
        this.idServicio = idServicio;
        this.idPlato = idPlato;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }
    
    
    
    
}
