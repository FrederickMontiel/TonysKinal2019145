package org.frederickmontiel.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.frederickmontiel.bean.Empresa;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.report.GenerarReporte;
import org.frederickmontiel.system.Principal;

public class EmpresasController implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones {
        AGREGAR,
        GUARDAR,
        ELIMINAR,
        EDITAR,
        ACTUALIZAR,
        CANCELAR,
        NINGUNO
    };
    
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList < Empresa > listaEmpresa;

    @FXML private TextField txtIdEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    @FXML private TableView tblEmpresas;
    @FXML private TableColumn colIdEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;

    public void cargarDatos() {
        tblEmpresas.setItems(getEmpresa());
        colIdEmpresa.setCellValueFactory(new PropertyValueFactory < Empresa, Integer > ("idEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory < Empresa, String > ("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory < Empresa, String > ("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory < Empresa, String > ("telefono"));
    }

    public void seleccionarElemento() {
        if (tblEmpresas.getSelectionModel().getSelectedIndex() != -1) {
            txtIdEmpresa.setText(String.valueOf(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getIdEmpresa()));
            txtNombreEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
            txtDireccionEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
            txtTelefonoEmpresa.setText(((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
        }
    }

    public ObservableList < Empresa > getEmpresa() {
        ArrayList < Empresa > lista = new ArrayList < Empresa > ();

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();

            while (resultado.next())
                lista.add(new Empresa(resultado.getInt("idEmpresa"),
                    resultado.getString("nombreEmpresa"),
                    resultado.getString("direccion"),
                    resultado.getString("telefono")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }

    public void nuevo() {
        switch (tipoOperacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);

                tblEmpresas.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                tblEmpresas.setDisable(false);

                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }

    public void guardar() {
        Empresa registro = new Empresa();
        registro.setNombreEmpresa(txtNombreEmpresa.getText());
        registro.setDireccion(txtDireccionEmpresa.getText());
        registro.setTelefono(txtTelefonoEmpresa.getText());

        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall(("{call sp_AgregarEmpresa(?,?,?)}"));
            procedimiento.setString(1, registro.getNombreEmpresa());
            procedimiento.setString(2, registro.getDireccion());
            procedimiento.setString(3, registro.getTelefono());
            procedimiento.execute();
            listaEmpresa.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Empresa buscarEmpresa(int idEmpresa) {
        Empresa resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, idEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while (registro.next())
                resultado = new Empresa(
                    registro.getInt("idEmpresa"),
                    registro.getString("nombreEmpresa"),
                    registro.getString("direccion"),
                    registro.getString("telefono"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    public void eliminar() {
        switch (tipoOperacion) {
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Empresa", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresa(?)}");
                            procedimiento.setInt(1, ((Empresa) tblEmpresas.getSelectionModel().getSelectedItem()).getIdEmpresa());
                            procedimiento.execute();
                            listaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "No se puede eliminar este registro por que \n es complemento de otros registro", "Advertencia", JOptionPane.WARNING_MESSAGE);
                            limpiarControles();
                            cargarDatos();
                        }
                    } else {
                        limpiarControles();
                        cargarDatos();
                        desactivarControles();
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
                if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    txtNombreEmpresa.setDisable(false);
                    txtDireccionEmpresa.setDisable(false);
                    txtTelefonoEmpresa.setDisable(false);
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
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();

                    txtIdEmpresa.setDisable(true);
                    txtNombreEmpresa.setDisable(true);
                    txtDireccionEmpresa.setDisable(true);
                    txtTelefonoEmpresa.setDisable(true);
                    desactivarControles();
                } else if (respuesta == JOptionPane.NO_OPTION) {
                    if (tblEmpresas.getSelectionModel().getSelectedItem() != null) {
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = operaciones.ACTUALIZAR;
                    } else {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                } else if (respuesta == JOptionPane.CANCEL_OPTION) {
                    btnEditar.setText("Editar");
                    btnReporte.setText("Repote");
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
            Empresa registro = (Empresa) tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarEmpresa(?,?,?,?)}");
            procedimiento.setInt(1, registro.getIdEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccion());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reporte() {
        switch (tipoOperacion) {
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnEditar.setDisable(false);
                btnReporte.setText("Reporte");
                btnReporte.setDisable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                imprimirReporte();
            break;
        }
    }

    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("idEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresa.jasper", "Reporte de Empresas", parametros);
    }


    public void desactivarControles() {
        txtIdEmpresa.setDisable(true);
        txtNombreEmpresa.setDisable(true);
        txtDireccionEmpresa.setDisable(true);
        txtTelefonoEmpresa.setDisable(true);
        
        tblEmpresas.setDisable(false);
    }

    public void activarControles() {
        txtIdEmpresa.setDisable(true);
        txtNombreEmpresa.setDisable(false);
        txtDireccionEmpresa.setDisable(false);
        txtTelefonoEmpresa.setDisable(false);
        
        tblEmpresas.setDisable(true);
    }

    public void limpiarControles() {
        txtIdEmpresa.setText("");
        txtNombreEmpresa.setText("");
        txtDireccionEmpresa.setText("");
        txtTelefonoEmpresa.setText("");
    }

    public void ventanaPresupuesto() {
        escenarioPrincipal.ventanaPresupuesto();
    }

    public void ventanaServicios() {
        escenarioPrincipal.ventanaServicios();
    }

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

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}