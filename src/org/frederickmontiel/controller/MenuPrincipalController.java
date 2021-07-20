package org.frederickmontiel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.frederickmontiel.system.Principal;

public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void ventanaProgramador() {
        escenarioPrincipal.ventanaProgramador();
    }

    public void ventanaEmpresas() {
        escenarioPrincipal.ventanaEmpresas();
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }

    public void ventanaEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }

    public void ventanaServicios() {
        escenarioPrincipal.ventanaServicios();
    }

    public void ventanaPro_Has_Plato() {
        escenarioPrincipal.productos_Has_Platos();
    }

    public void ventanaSer_Has_Pla() {
        escenarioPrincipal.servicios_Has_Platos();
    }

    public void ventanaSer_Has_Emp() {
        escenarioPrincipal.servicios_Has_Empleados();
    }

    public void ventanaProducto() {
        escenarioPrincipal.producto();
    }

    public void ventanaTipoPlato() {
        escenarioPrincipal.tipoPlato();
    }

    public void ventanaPlato() {
        escenarioPrincipal.plato();
    }

    public void cerrarVentana() {
        System.exit(0);
    }
}