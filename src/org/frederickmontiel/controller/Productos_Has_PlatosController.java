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
import org.frederickmontiel.bean.Producto;
import org.frederickmontiel.bean.Productos_Has_Platos;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class Productos_Has_PlatosController implements Initializable {
    private Principal escenarioPrincipal;
    
    private ObservableList < Productos_Has_Platos > listaPro_Has_Pla;
    private ObservableList < Producto > listaProductos;
    private ObservableList < Plato > listaPlatos;
    
    @FXML private ComboBox cmbIdPlato;
    @FXML private ComboBox cmbIdProducto;
    @FXML private TableView tblPro_Has_Pla;
    @FXML private TableColumn colIdPlato;
    @FXML private TableColumn colIdProducto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbIdProducto.setItems(getProductos());
        cmbIdPlato.setItems(getPlatos());
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void cargarDatos() {
        tblPro_Has_Pla.setItems(getProductos_Has_Platos());
        colIdPlato.setCellValueFactory(new PropertyValueFactory < Productos_Has_Platos, Integer > ("idPlato"));
        colIdProducto.setCellValueFactory(new PropertyValueFactory < Productos_Has_Platos, Integer > ("idProducto"));
    }
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    public void seleccionarElemento() {
        if (tblPro_Has_Pla.getSelectionModel().getSelectedIndex() == -1) {} else {
            cmbIdProducto.getSelectionModel().select(buscarProducto(((Productos_Has_Platos) tblPro_Has_Pla.getSelectionModel().getSelectedItem()).getIdProducto()));
            cmbIdPlato.getSelectionModel().select(buscarPlato(((Productos_Has_Platos) tblPro_Has_Pla.getSelectionModel().getSelectedItem()).getIdPlato()));
        }

    }
    private ObservableList getProductos_Has_Platos() {
        ArrayList < Productos_Has_Platos > lista = new ArrayList < Productos_Has_Platos > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProducto_Has_Platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next())
                lista.add(new Productos_Has_Platos(resultado.getInt("idPlato"),
                    resultado.getInt("IdProducto")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPro_Has_Pla = FXCollections.observableArrayList(lista);
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

    private ObservableList getProductos() {
        ArrayList < Producto > lista = new ArrayList < Producto > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Producto(resultado.getInt("idProducto"),
                    resultado.getString("nombreProducto"),
                    resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }

    public Producto buscarProducto(int idProducto) {
        Producto resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProducto(?)}");
            procedimiento.setInt(1, idProducto);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new Producto(registro.getInt("idProducto"),
                    registro.getString("nombreProducto"),
                    registro.getInt("cantidad"));
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