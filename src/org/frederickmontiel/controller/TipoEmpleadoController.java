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
import org.frederickmontiel.bean.TipoEmpleado;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class TipoEmpleadoController implements Initializable {
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
    private ObservableList < TipoEmpleado > listaTipoEmpleado;
    
    @FXML private TextField txtIdTipoEmpleado;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblTipoEmpleado;
    @FXML private TableColumn colIdTipoEmpleado;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
    }


    public void seleccionarElemento() {
        if (tblTipoEmpleado.getSelectionModel().getSelectedIndex() != -1) {
            txtIdTipoEmpleado.setText(String.valueOf(((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getIdTipoEmpleado()));
            txtDescripcion.setText((((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion()));
        }
    }
    public void cargarDatos() {
        tblTipoEmpleado.setItems(getTipoEmpleado());
        colIdTipoEmpleado.setCellValueFactory(new PropertyValueFactory < TipoEmpleado, Integer > ("idTipoEmpleado"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory < TipoEmpleado, String > ("descripcion"));
    }

    public ObservableList < TipoEmpleado > getTipoEmpleado() {
        ArrayList < TipoEmpleado > lista = new ArrayList < TipoEmpleado > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next())
                lista.add(new TipoEmpleado(resultado.getInt("idTipoEmpleado"),
                    resultado.getString("descripcion")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
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
                desactivarControles();
                cargarDatos();
                limpiarControles();
                tipoOperacion = operaciones.NINGUNO;

            break;
        }
    }

    public void guardar() {
        TipoEmpleado registro = new TipoEmpleado();
        registro.setDescripcion(txtDescripcion.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro");
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1, ((TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem()).getIdTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedIndex());
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
                if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
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
                    if (tblTipoEmpleado.getSelectionModel().getSelectedItem() != null) {
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado) tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getIdTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
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

    public void desactivarControles() {
        txtIdTipoEmpleado.setDisable(true);
        txtDescripcion.setDisable(true);
        btnCancelar.setDisable(true);
        
        tblTipoEmpleado.setDisable(false);
    }

    public void activarControles() {
        txtIdTipoEmpleado.setDisable(true);
        txtDescripcion.setDisable(false);
        btnCancelar.setDisable(false);
        
        tblTipoEmpleado.setDisable(true);
    }

    public void limpiarControles() {
        txtIdTipoEmpleado.setText("");
        txtDescripcion.setText("");
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    public void ventanaEmpleado() {
        escenarioPrincipal.ventanaEmpleado();
    }
}