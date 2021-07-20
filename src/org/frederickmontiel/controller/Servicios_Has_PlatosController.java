package org.frederickmontiel.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.frederickmontiel.bean.Plato;
import org.frederickmontiel.bean.Servicio;
import org.frederickmontiel.bean.Servicios_Has_Platos;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class Servicios_Has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList < Servicios_Has_Platos > listaSer_Has_Pla;
    private ObservableList < Servicio > listaServicios;
    private ObservableList < Plato > listaPlatos;
    
    @FXML private ComboBox cmbIdPlato;
    @FXML private ComboBox cmbIdServicio;
    @FXML private TableView tblSer_Has_Platos;
    @FXML private TableColumn colIdPlato;
    @FXML private TableColumn colIdServicio;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbIdServicio.setItems(getServicios());
        cmbIdPlato.setItems(getPlatos());
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void cargarDatos() {
        tblSer_Has_Platos.setItems(getServicios_Has_Platos());
        colIdPlato.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Platos, Integer > ("idPlato"));
        colIdServicio.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Platos, Integer > ("idServicio"));
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void seleccionarElemento() {
        if (tblSer_Has_Platos.getSelectionModel().getSelectedIndex() == -1) {} else {
            cmbIdServicio.getSelectionModel().select(buscarServicio(((Servicios_Has_Platos) tblSer_Has_Platos.getSelectionModel().getSelectedItem()).getIdServicio()));
            cmbIdPlato.getSelectionModel().select(buscarPlato(((Servicios_Has_Platos) tblSer_Has_Platos.getSelectionModel().getSelectedItem()).getIdPlato()));
        }

    }
    
    private ObservableList getServicios_Has_Platos() {
        ArrayList < Servicios_Has_Platos > lista = new ArrayList < Servicios_Has_Platos > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_Has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next())
                lista.add(new Servicios_Has_Platos(resultado.getInt("idPlato"),
                    resultado.getInt("IdServicio")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaSer_Has_Pla = FXCollections.observableArrayList(lista);
    }

    private ObservableList getPlatos() {
        ArrayList < Plato > lista = new ArrayList < Plato > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("idPlato"),
                    resultado.getInt("cantidad"),
                    resultado.getString("nombrePlato"),
                    resultado.getString("descripcionPlato"),
                    resultado.getDouble("precioPlato"),
                    resultado.getInt("idTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista);
    }

    private ObservableList getServicios() {
        ArrayList < Servicio > lista = new ArrayList < Servicio > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("idServicio"),
                    resultado.getDate("fechaServicio"),
                    resultado.getString("tipoServicio"),
                    resultado.getString("horaServicio"),
                    resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"),
                    resultado.getInt("IdEmpresa")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicios = FXCollections.observableArrayList(lista);
    }

    public Servicio buscarServicio(int idServicio) {
        Servicio resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, idServicio);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Servicio(registro.getInt("idServicio"),
                    registro.getDate("fechaServicio"),
                    registro.getString("tipoServicio"),
                    registro.getString("horaServicio"),
                    registro.getString("lugarServicio"),
                    registro.getString("telefonoContacto"),
                    registro.getInt("IdEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }


    public Plato buscarPlato(int idPlato) {
        Plato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPlato(?)}");
            procedimiento.setInt(1, idPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Plato(registro.getInt("idPlato"),
                    registro.getInt("cantidad"),
                    registro.getString("nombrePlato"),
                    registro.getString("descripcionPlato"),
                    registro.getDouble("precioPlato"),
                    registro.getInt("idTipoPlato"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
}