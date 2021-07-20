package org.frederickmontiel.bean;

import java.util.Date;

public class Presupuesto {
    private int idPresupuesto ;  
    private Date fechaSolicitud;
    private double  cantidadPresupuesto;
    private int idEmpresa;

    public Presupuesto() {}

    public Presupuesto(int idPresupuesto, Date fechaSolicitud, double cantidadPresupuesto, int idEmpresa) {
        this.idPresupuesto = idPresupuesto;
        this.fechaSolicitud = fechaSolicitud;
        this.cantidadPresupuesto = cantidadPresupuesto;
        this.idEmpresa = idEmpresa;
    }

    public int getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(int idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public double getCantidadPresupuesto() {
        return cantidadPresupuesto;
    }

    public void setCantidadPresupuesto(double cantidadPresupuesto) {
        this.cantidadPresupuesto = cantidadPresupuesto;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

}
