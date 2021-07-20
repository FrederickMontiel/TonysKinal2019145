package org.frederickmontiel.bean;

import java.util.Date;

public class Servicios_Has_Empleados {
private Date fechaEvento ;
private String horaEvento ;
private String lugarEvento ;
private int idServicio;
private int idEmpleado;

    public Servicios_Has_Empleados() {
    }

    public Servicios_Has_Empleados(Date fechaEvento, String horaEvento, String lugarEvento, int idEmpleado, int idServicio) {
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
        this.idServicio = idServicio;
        this.idEmpleado = idEmpleado;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    
}
