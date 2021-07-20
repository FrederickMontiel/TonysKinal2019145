package org.frederickmontiel.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import org.frederickmontiel.controller.ProgramadorController;
import org.frederickmontiel.controller.EmpleadoController;
import org.frederickmontiel.controller.EmpresasController;
import org.frederickmontiel.controller.MenuPrincipalController;
import org.frederickmontiel.controller.PlatoController;
import org.frederickmontiel.controller.PresupuestoController;
import org.frederickmontiel.controller.ProductoController;
import org.frederickmontiel.controller.Productos_Has_PlatosController;
import org.frederickmontiel.controller.ServiciosController;
import org.frederickmontiel.controller.Servicios_Has_EmpleadosController;
import org.frederickmontiel.controller.Servicios_Has_PlatosController;
import org.frederickmontiel.controller.TipoEmpleadoController;
import org.frederickmontiel.controller.TipoPlatoController;

public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String Paquete_Vista = "/org/frederickmontiel/view/";

    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal");
        escenarioPrincipal.setResizable(false);
        escenarioPrincipal.getIcons().add(new Image("/org/frederickmontiel/image/icono.png"));
        //Parent root = FXMLLoader.load(getClass().getResource("/org/frederickmontiel/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        menuPrincipal();
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void menuPrincipal() {
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 689, 429);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaProgramador() {
        try {
            ProgramadorController datosProgramador = (ProgramadorController) cambiarEscena("ProgramadorView.fxml", 734, 321);
            datosProgramador.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpresas() {
        try {
            EmpresasController empresasController = (EmpresasController) cambiarEscena("EmpresasView.fxml", 915, 401);
            empresasController.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaPresupuesto() {
        try {
            PresupuestoController presupuesto = (PresupuestoController) cambiarEscena("PresupuestoView.fxml", 401, 334);
            presupuesto.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaTipoEmpleado() {
        try {
            TipoEmpleadoController tipoEmpleado = (TipoEmpleadoController) cambiarEscena("TipoEmpleadoView.fxml", 915, 311);
            tipoEmpleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaEmpleado() {
        try {
            EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadosView.fxml", 1334, 631);
            empleado.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ventanaServicios() {
        try {
            ServiciosController servicio = (ServiciosController) cambiarEscena("ServiciosView.fxml", 1202, 563);
            servicio.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void producto() {
        try {
            ProductoController productos = (ProductoController) cambiarEscena("ProductoView.fxml", 620, 440);
            productos.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tipoPlato() {
        try {
            TipoPlatoController plato = (TipoPlatoController) cambiarEscena("TipoPlatoView.fxml", 915, 311);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void plato() {
        try {
            PlatoController plato = (PlatoController) cambiarEscena("PlatoView.fxml", 1202, 504);
            plato.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void productos_Has_Platos() {
        try {
            Productos_Has_PlatosController pro_has_pla = (Productos_Has_PlatosController) cambiarEscena("Productos_Has_PlatosView.fxml", 915, 220);
            pro_has_pla.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void servicios_Has_Platos() {
        try {
            Servicios_Has_PlatosController ser_has_pla = (Servicios_Has_PlatosController) cambiarEscena("Servicios_Has_PlatosView.fxml", 915, 220);
            ser_has_pla.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void servicios_Has_Empleados() {
        try {
            Servicios_Has_EmpleadosController ser_has_emp = (Servicios_Has_EmpleadosController) cambiarEscena("Servicios_Has_EmpleadosView.fxml", 1120, 454);
            ser_has_emp.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception {
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(Paquete_Vista + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(Paquete_Vista + fxml));
        escena = new Scene((AnchorPane) cargadorFXML.load(archivo));
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }
}