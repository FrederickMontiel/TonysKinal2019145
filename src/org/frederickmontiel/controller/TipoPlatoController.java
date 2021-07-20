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
import org.frederickmontiel.bean.TipoPlato;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;


public class TipoPlatoController implements Initializable {
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
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtIdTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colIdTipoPlato;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }

    public void seleccionarElemento() {
        if (tblTipoPlatos.getSelectionModel().getSelectedIndex() != -1){
            txtIdTipoPlato.setText(String.valueOf(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getIdTipoPlato()));
            txtDescripcion.setText(((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public void cargarDatos() {
        tblTipoPlatos.setItems(getTipoPlato());
        colIdTipoPlato.setCellValueFactory(new PropertyValueFactory < TipoPlato, Integer > ("idTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory < TipoPlato, String > ("descripcion"));
    }

    public ObservableList < TipoPlato > getTipoPlato() {
        ArrayList < TipoPlato > lista = new ArrayList < TipoPlato > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlatos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoPlato(resultado.getInt("idTipoPlato"),
                    resultado.getString("descripcion")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableList(lista);
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
        try {
            TipoPlato registro = new TipoPlato();
            registro.setDescripcion(txtDescripcion.getText());
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro");
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1, ((TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem()).getIdTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este registro por que \n es complemento de otros registro", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            limpiarControles();
                            cargarDatos();
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
                if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
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
                    if (tblTipoPlatos.getSelectionModel().getSelectedItem() != null) {
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
            TipoPlato registro = (TipoPlato) tblTipoPlatos.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoPlato(?,?)}");
            procedimiento.setInt(1, registro.getIdTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoPlato.add(registro);
            
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
                desactivarControles();
                cargarDatos();
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }

    }

    public void ventanaPlato() {
        escenarioPrincipal.plato();
    }

    public void limpiarControles() {
        txtIdTipoPlato.setText("");
        txtDescripcion.setText("");

    }

    public void desactivarControles() {
        txtIdTipoPlato.setDisable(true);
        txtDescripcion.setDisable(true);
        tblTipoPlatos.setDisable(false);
        btnCancelar.setDisable(true);
    }

    public void activarControles() {
        txtIdTipoPlato.setDisable(true);
        txtDescripcion.setDisable(false);
        tblTipoPlatos.setDisable(true);
        btnCancelar.setDisable(false);
    }
    
    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}