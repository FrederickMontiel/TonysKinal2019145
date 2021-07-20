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
import org.frederickmontiel.bean.Empleado;
import org.frederickmontiel.bean.TipoEmpleado;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class EmpleadoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones {
        AGREGAR,
        GUARDAR,
        ELIMINAR,
        EDITAR,
        ACTUALIZAR,
        NINGUNO,
        CANCELAR
    };
    
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList < Empleado > listaEmpleado;
    private ObservableList < TipoEmpleado > listaTipoEmpleado;
    
    @FXML private TextField txtIdEmpleado;
    @FXML private TextField txtNumero;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtNombres;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtGradoCocinero;
    @FXML private ComboBox cmbIdTipoEmpleado;
    @FXML private TableView tblEmpleados;
    @FXML private TableColumn colIdEmpleado;
    @FXML private TableColumn colNumeroEmpleado;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colTelefono;
    @FXML private TableColumn colGradoCocinero;
    @FXML private TableColumn colIdTipoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbIdTipoEmpleado.setItems(getTipoEmpleado());
        desactivarControles();
    }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleado());
        colIdEmpleado.setCellValueFactory(new PropertyValueFactory < Empleado, Integer > ("idEmpleado"));
        colNumeroEmpleado.setCellValueFactory(new PropertyValueFactory < Empleado, Integer > ("numeroEmpleado"));
        colApellidos.setCellValueFactory(new PropertyValueFactory < Empleado, String > ("apellidosEmpleado"));
        colNombres.setCellValueFactory(new PropertyValueFactory < Empleado, String > ("nombresEmpleado"));
        colDireccion.setCellValueFactory(new PropertyValueFactory < Empleado, String > ("direccionEmpleado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory < Empleado, String > ("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory < Empleado, String > ("gradoCocinero"));
        colIdTipoEmpleado.setCellValueFactory(new PropertyValueFactory < Empleado, Integer > ("idTipoEmpleado"));
    }
    
    public void seleccionarElemento() {
        if (tblEmpleados.getSelectionModel().getSelectedIndex() == -1){}else{
            txtIdEmpleado.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getIdEmpleado()));
            txtNumero.setText(String.valueOf(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
            txtApellidos.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidosEmpleado());
            txtNombres.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado());
            txtDireccion.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado());
            txtTelefono.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto());
            txtGradoCocinero.setText(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero());
            cmbIdTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getIdTipoEmpleado()));
        }
    }

    public ObservableList < Empleado > getEmpleado() {
        ArrayList < Empleado > lista = new ArrayList < Empleado > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next())
                lista.add(new Empleado(resultado.getInt("idEmpleado"),
                    resultado.getInt("numeroEmpleado"),
                    resultado.getString("apellidosEmpleado"),
                    resultado.getString("nombresEmpleado"),
                    resultado.getString("direccionEmpleado"),
                    resultado.getString("telefonoContacto"),
                    resultado.getString("gradoCocinero"),
                    resultado.getInt("idTipoEmpleado")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList < TipoEmpleado > getTipoEmpleado() {
        ArrayList < TipoEmpleado > lista = new ArrayList < TipoEmpleado > ();
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleados}");
            ResultSet resultado = Procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new TipoEmpleado(resultado.getInt("idTipoEmpleado"),
                    resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableList(lista);
    }
    
    public TipoEmpleado buscarTipoEmpleado(int idTipoEmpleado) {
        TipoEmpleado resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, idTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next()) {
                resultado = new TipoEmpleado(registro.getInt("idTipoEmpleado"),
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
                activarControles();
                limpiarControles();
                cargarDatos();
                btnNuevo.setText("Guardar");
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar() {
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumero.getText()));
        registro.setApellidosEmpleado(txtApellidos.getText());
        registro.setNombresEmpleado(txtNombres.getText());
        registro.setDireccionEmpleado(txtDireccion.getText());
        registro.setTelefonoContacto(txtTelefono.getText());
        registro.setGradoCocinero(txtGradoCocinero.getText());
        registro.setIdTipoEmpleado(((TipoEmpleado) cmbIdTipoEmpleado.getSelectionModel().getSelectedItem()).getIdTipoEmpleado());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleado(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidosEmpleado());
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getIdTipoEmpleado());
            procedimiento.execute();
            listaEmpleado.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void eliminar() {
        switch (tipoOperacion) {
            default:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el regsitro?", "Eliminar Empleado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleado) tblEmpleados.getSelectionModel().getSelectedItem()).getIdEmpleado());
                            procedimiento.execute();
                            listaEmpleado.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                            limpiarControles();
                            cargarDatos();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    
    public void editar() {
        switch (tipoOperacion) {
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
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
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                } else if (respuesta == JOptionPane.NO_OPTION) {
                    if (tblEmpleados.getSelectionModel().getSelectedItem() != null) {
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = operaciones.ACTUALIZAR;
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    cargarDatos();
                } else if (respuesta == JOptionPane.CANCEL_OPTION) {
                    btnEditar.setText("Editar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                }
                break;
        }
    }
    
    public void actualizar() {
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpleado(?,?,?,?,?,?,?,?)}");
            Empleado registro = (Empleado) tblEmpleados.getSelectionModel().getSelectedItem();
            registro.setNumeroEmpleado(Integer.parseInt(txtNumero.getText()));
            registro.setApellidosEmpleado(txtApellidos.getText());
            registro.setNombresEmpleado(txtNombres.getText());
            registro.setDireccionEmpleado(txtDireccion.getText());
            registro.setTelefonoContacto(txtTelefono.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            procedimiento.setInt(1, registro.getIdEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidosEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getIdTipoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelar() {
        switch (tipoOperacion) {
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                limpiarControles();
                desactivarControles();
                cargarDatos();
                break;
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
        }
    }

    public void desactivarControles() {
        txtIdEmpleado.setDisable(true);
        txtNumero.setDisable(true);
        txtApellidos.setDisable(true);
        txtNombres.setDisable(true);
        txtDireccion.setDisable(true);
        txtTelefono.setDisable(true);
        txtGradoCocinero.setDisable(true);
        cmbIdTipoEmpleado.setDisable(true);
        btnReporte.setDisable(true);
        
        tblEmpleados.setDisable(false);
    }
    
    public void activarControles() {
        txtIdEmpleado.setDisable(true);
        txtNumero.setDisable(false);
        txtApellidos.setDisable(false);
        txtNombres.setDisable(false);
        txtDireccion.setDisable(false);
        txtTelefono.setDisable(false);
        txtGradoCocinero.setDisable(false);
        cmbIdTipoEmpleado.setDisable(false);
        btnReporte.setDisable(false);
        
        tblEmpleados.setDisable(true);
    }
    
    public void limpiarControles() {
        txtIdEmpleado.setText("");
        txtNumero.setText("");
        txtApellidos.setText("");
        txtNombres.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtGradoCocinero.setText("");
        cmbIdTipoEmpleado.getSelectionModel().clearAndSelect(-1);
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaTipoEmpleado() {
        escenarioPrincipal.ventanaTipoEmpleado();
    }
}