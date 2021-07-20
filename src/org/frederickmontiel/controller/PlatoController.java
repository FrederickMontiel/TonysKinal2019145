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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.frederickmontiel.bean.Plato;
import org.frederickmontiel.bean.TipoPlato;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class PlatoController implements Initializable {
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
    private ObservableList < TipoPlato > listaTipoPlato;
    private ObservableList < Plato > listaPlato;
    
    @FXML private TextField txtIdPlato;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtNombrePlato;
    @FXML private TextField txtDescripcionPlato;
    @FXML private TextField txtPrecioPlato;
    @FXML private ComboBox cmbIdTipoPlato;
    @FXML private TableView tblPlatos;
    @FXML private TableColumn colIdPlato;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colNombrePlato;
    @FXML private TableColumn colDescripcionPlato;
    @FXML private TableColumn colPrecioPlato;
    @FXML private TableColumn colIdTipoPlato;
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
        tblPlatos.setItems(getPlato());
        colIdPlato.setCellValueFactory(new PropertyValueFactory < Plato, Integer > ("idPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory < Plato, Integer > ("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory < Plato, Integer > ("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory < Plato, Integer > ("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory < Plato, Double > ("precioPlato"));
        colIdTipoPlato.setCellValueFactory(new PropertyValueFactory < Plato, Integer > ("idTipoPlato"));
    }


    public void seleccionarElemento() {
        if (tblPlatos.getSelectionModel().getSelectedIndex() != -1){
            txtIdPlato.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getIdPlato()));
            txtCantidad.setText(String.valueOf(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            txtNombrePlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtDescripcionPlato.setText(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato());
            txtPrecioPlato.setText(Double.toString(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            cmbIdTipoPlato.getSelectionModel().select(buscarTipoPlato(((Plato) tblPlatos.getSelectionModel().getSelectedItem()).getIdTipoPlato()));
        }
    }

    public ObservableList < TipoPlato > getTipoPlato() {
        ArrayList < TipoPlato > lista = new ArrayList < TipoPlato > ();
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlatos}");
            ResultSet resultado = Procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("idTipoPlato"),
                    resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList < Plato > getPlato() {
        ArrayList < Plato > lista = new ArrayList < Plato > ();
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = Procedimiento.executeQuery();
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
        return listaPlato = FXCollections.observableArrayList(lista);
    }

    public TipoPlato buscarTipoPlato(int idTipoPlato) {
        TipoPlato resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPlato(?)}");
            procedimiento.setInt(1, idTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoPlato(registro.getInt("idTipoPlato"),
                    registro.getString("descripcion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    public void nuevo() {
        switch (tipoOperacion) {
            case NINGUNO:
                btnNuevo.setText("Guardar");
                btnCancelar.setDisable(false);
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                tblPlatos.getSelectionModel().clearAndSelect(-1);
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
        Plato registro = new Plato();
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setIdTipoPlato(((TipoPlato) cmbIdTipoPlato.getSelectionModel().getSelectedItem()).getIdTipoPlato());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlato(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato());
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getIdTipoPlato());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro");
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1, ((TipoPlato) tblPlatos.getSelectionModel().getSelectedItem()).getIdTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblPlatos.getSelectionModel().getSelectedIndex());
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
                if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
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
                    if (tblPlatos.getSelectionModel().getSelectedItem() != null) {
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
                    cargarDatos();
                }
                break;
        }
    }

    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPlato(?,?,?,?,?,?)}");
            Plato registro = (Plato) tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            procedimiento.setInt(1, registro.getIdPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato());
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setDouble(6, registro.getIdTipoPlato());
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
                desactivarControles();
                limpiarControles();
                cargarDatos();
                break;
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
        txtIdPlato.setDisable(true);
        txtCantidad.setDisable(false);
        txtNombrePlato.setDisable(false);
        txtDescripcionPlato.setDisable(false);
        txtPrecioPlato.setDisable(false);
        cmbIdTipoPlato.setDisable(false);
        btnCancelar.setDisable(false);
        
        tblPlatos.setDisable(true);
    }
    
    public void desactivarControles() {
        txtIdPlato.setDisable(true);
        txtCantidad.setDisable(true);
        txtNombrePlato.setDisable(true);
        txtDescripcionPlato.setDisable(true);
        txtPrecioPlato.setDisable(true);
        cmbIdTipoPlato.setDisable(true);
        btnCancelar.setDisable(true);
        
        tblPlatos.setDisable(false);
    }
    
    public void limpiarControles() {
        txtIdPlato.setText("");
        txtCantidad.setText("");
        txtNombrePlato.setText("");
        txtDescripcionPlato.setText("");
        txtPrecioPlato.setText("");
        cmbIdTipoPlato.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
        cmbIdTipoPlato.setItems(getTipoPlato());
        desactivarControles();
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaTipoPlato() {
        escenarioPrincipal.tipoPlato();
    }
}