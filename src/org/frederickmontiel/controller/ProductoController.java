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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.frederickmontiel.bean.Producto;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class ProductoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {
        NUEVO,
        GUARDAR,
        ELIMINAR,
        EDITAR,
        ACTUALIZAR,
        CANCELAR,
        NINGUNO
    }
    
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList < Producto > listaProducto;
    
    @FXML private TextField txtIdProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidad;
    @FXML private TableView tblProducto;
    @FXML private TableColumn colIdProducto;
    @FXML private TableColumn colNombreProducto;
    @FXML private TableColumn colCantidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    public void cargarDatos() {
        tblProducto.setItems(getProducto());
        colIdProducto.setCellValueFactory(new PropertyValueFactory < Producto, Integer > ("idProducto"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory < Producto, String > ("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory < Producto, Integer > ("cantidad"));

    }

    public ObservableList < Producto > getProducto() {
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
        return listaProducto = FXCollections.observableArrayList(lista);
    }

    public void seleccionarElemento() {
        if (tblProducto.getSelectionModel().getSelectedIndex() == -1){}else{
            txtIdProducto.setText(String.valueOf(((Producto) tblProducto.getSelectionModel().getSelectedItem()).getIdProducto()));
            txtNombreProducto.setText(((Producto) tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto());
            txtCantidad.setText(String.valueOf(((Producto) tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
        }
    }


    public void nuevo() {
        switch (tipoOperacion) {
            case NINGUNO:
                btnNuevo.setText("Guardar");
                btnCancelar.setDisable(false);
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                limpiarControles();
                activarControles();
                tipoOperacion = operaciones.GUARDAR;

                break;

            case GUARDAR:
                guardar();
                btnNuevo.setText("Nuevo");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                limpiarControles();
                desactivarControles();
                cargarDatos();
                tipoOperacion = operaciones.NINGUNO;

                break;
        }
    }
    
    public void guardar() {
        Producto registro = new Producto();
        registro.setNombreProducto(txtNombreProducto.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProducto(?,?)}");
            procedimiento.setString(1, registro.getNombreProducto());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.execute();
            listaProducto.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblProducto.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro");
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setInt(1, ((Producto) tblProducto.getSelectionModel().getSelectedItem()).getIdProducto());
                            procedimiento.execute();
                            listaProducto.remove(tblProducto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    } else if (respuesta == JOptionPane.NO_OPTION) {
                        activarControles();
                        limpiarControles();
                    } else if (respuesta == JOptionPane.CANCEL_OPTION) {
                        desactivarControles();
                        limpiarControles();
                        cargarDatos();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;

        }
    }

    public void editar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblProducto.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
            case ACTUALIZAR:
                int respuesta = JOptionPane.showConfirmDialog(null, ("¿Esta seguro de editar el registro"));
                if (respuesta == JOptionPane.YES_OPTION) {
                    actualizar();
                    btnEditar.setText("Editar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                } else if (respuesta == JOptionPane.NO_OPTION) {
                    if (tblProducto.getSelectionModel().getSelectedItem() != null) {
                        btnEditar.setText("Actualizar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = operaciones.ACTUALIZAR;
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                } else if (respuesta == JOptionPane.CANCEL_OPTION) {
                    btnEditar.setText("Editar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    limpiarControles();
                    cargarDatos();
                }
                break;
        }
    }

    public void actualizar() {
        try {
            Producto registro = (Producto) tblProducto.getSelectionModel().getSelectedItem();
            registro.setNombreProducto(txtNombreProducto.getText());
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?)}");
            procedimiento.setInt(1, registro.getIdProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {
        switch (tipoOperacion) {
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                limpiarControles();
                cargarDatos();
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnCancelar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }

    }

    public void activarControles() {
        txtIdProducto.setDisable(true);
        txtNombreProducto.setDisable(false);
        txtCantidad.setDisable(false);
        
        tblProducto.setDisable(true);
        btnCancelar.setDisable(false);
    }

    public void desactivarControles() {
        txtIdProducto.setDisable(true);
        txtNombreProducto.setDisable(true);
        txtCantidad.setDisable(true);
        
        tblProducto.setDisable(false);
        btnCancelar.setDisable(true);
    }

    public void limpiarControles() {
        txtIdProducto.setText("");
        txtNombreProducto.setText("");
        txtCantidad.setText("");
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}