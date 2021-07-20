package org.frederickmontiel.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.frederickmontiel.bean.Empleado;
import org.frederickmontiel.bean.Servicio;
import org.frederickmontiel.bean.Servicios_Has_Empleados;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class Servicios_Has_EmpleadosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList < Servicios_Has_Empleados > listaServiciosEmpleados;
    private ObservableList < Servicio > listaServicio;
    private ObservableList < Empleado > listaEmpleado;
    
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
    @FXML private ComboBox cmbIdEmpleado;
    @FXML private ComboBox cmbIdServicio;
    @FXML private TextField txtLugarEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private TableView tblServicios_Has_Empleados;
    @FXML private TableColumn colIdEmpleado;
    @FXML private TableColumn colIdServicio;
    @FXML private TableColumn colFechaEvento;
    @FXML private TableColumn colHoraEvento;
    @FXML private TableColumn colLugarEvento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private DatePicker fecha;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbIdServicio.setItems(getServicios());
        cmbIdEmpleado.setItems(getEmpleados());
        
        fecha.setValue(LocalDate.now());
    }

    public void cargarDatos() {
        tblServicios_Has_Empleados.setItems(getServicios_Has_Empleados());

        colFechaEvento.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Empleados, Date > ("fechaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Empleados, String > ("lugarEvento"));
        colHoraEvento.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Empleados, String > ("horaEvento"));
        colIdServicio.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Empleados, Integer > ("idServicio"));
        colIdEmpleado.setCellValueFactory(new PropertyValueFactory < Servicios_Has_Empleados, Integer > ("idEmpleado"));
    }

    public ObservableList < Servicios_Has_Empleados > getServicios_Has_Empleados() {
        ArrayList < Servicios_Has_Empleados > lista = new ArrayList < Servicios_Has_Empleados > ();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_Has_Empleados()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicios_Has_Empleados(
                    resultado.getDate("fechaEvento"),
                    resultado.getString("horaEvento"),
                    resultado.getString("lugarEvento"),
                    resultado.getInt("idServicio"),
                    resultado.getInt("idEmpleado")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServiciosEmpleados = FXCollections.observableList(lista);
    }
    
    public void seleccionarElemento() {
        if (tblServicios_Has_Empleados.getSelectionModel().getSelectedIndex() != -1){
            //tblServicios_Has_Empleados.setItems(getServicios_Has_Empleados());
            cmbIdServicio.getSelectionModel().select(buscarServicio((((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getIdServicio())));
            cmbIdEmpleado.getSelectionModel().select(buscarEmpleado((((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getIdEmpleado())));
            fecha.setValue(parseDatePicker(String.valueOf(((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getFechaEvento())));
            txtLugarEvento.setText((((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
            txtHoraEvento.setText(((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getHoraEvento());
        }
    }
    
    public ObservableList < Servicio > getServicios() {
        ArrayList < Servicio > lista = new ArrayList < Servicio > ();
        try {
            PreparedStatement registro = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios()}");
            ResultSet resultado = registro.executeQuery();
            while (resultado.next()) {
                lista.add(new Servicio(resultado.getInt("idServicio"),
                    resultado.getDate("fechaServicio"),
                    resultado.getString("tipoServicio"),
                    resultado.getString("horaServicio").substring(0, 5),
                    resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"),
                    resultado.getInt("idEmpresa")));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableList(lista);
    }


    public Servicio buscarServicio(int idServicio) {
        Servicio servicio = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicio(?)}");
            procedimiento.setInt(1, idServicio);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                servicio = new Servicio(resultado.getInt("idServicio"),
                    resultado.getDate("fechaServicio"),
                    resultado.getString("tipoServicio"),
                    resultado.getString("horaServicio").substring(0, 5),
                    resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"),
                    resultado.getInt("idEmpresa"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return servicio;
    }


    public ObservableList < Empleado > getEmpleados() {
        ArrayList < Empleado > lista = new ArrayList < Empleado > ();

        try {
            PreparedStatement registro = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados()}");
            ResultSet resultado = registro.executeQuery();

            while (resultado.next()) {
                lista.add(new Empleado(resultado.getInt("idEmpleado"),
                    resultado.getInt("numeroEmpleado"),
                    resultado.getString("apellidosEmpleado"),
                    resultado.getString("nombresEmpleado"),
                    resultado.getString("direccionEmpleado"),
                    resultado.getString("telefonoContacto"),
                    resultado.getString("gradoCocinero"),
                    resultado.getInt("idTipoEmpleado")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaEmpleado = FXCollections.observableList(lista);
    }


    public Empleado buscarEmpleado(int idEmpleado) {
        Empleado empleado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleado(?)}");
            procedimiento.setInt(1, idEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                empleado = new Empleado(resultado.getInt("idEmpleado"),
                    resultado.getInt("numeroEmpleado"),
                    resultado.getString("apellidosEmpleado"),
                    resultado.getString("nombresEmpleado"),
                    resultado.getString("direccionEmpleado"),
                    resultado.getString("telefonoContacto"),
                    resultado.getString("gradoCocinero"),
                    resultado.getInt("idTipoEmpleado"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return empleado;
    }

    public void nuevo() {
        switch (tipoOperacion) {
            case NINGUNO:
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                limpiarControles();
                activarControles();
                tipoOperacion = operaciones.GUARDAR;
                
                tblServicios_Has_Empleados.setDisable(true);
                break;

            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
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
        Servicios_Has_Empleados registro = new Servicios_Has_Empleados();
        registro.setIdServicio(((Servicio) cmbIdServicio.getSelectionModel().getSelectedItem()).getIdServicio());
        registro.setIdEmpleado((((Empleado) cmbIdEmpleado.getSelectionModel().getSelectedItem()).getIdEmpleado()));
        registro.setFechaEvento(parseDate(fecha.getValue()));
        registro.setLugarEvento(txtLugarEvento.getText());
        registro.setHoraEvento(txtHoraEvento.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio_has_Empleado(?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(2, registro.getHoraEvento());
            procedimiento.setString(3, registro.getLugarEvento());
            procedimiento.setInt(4, registro.getIdServicio());
            procedimiento.setInt(5, registro.getIdEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        switch (tipoOperacion) {
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnNuevo.setDisable(false);
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                break;
            default:
                if (tblServicios_Has_Empleados.getSelectionModel().getSelectedItem() != null) {
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION) {
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio_has_Empleado(?,?)}");
                            procedimiento.setInt(1, ((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getIdServicio());
                            procedimiento.setInt(2, ((Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem()).getIdServicio());
                            procedimiento.execute();
                            listaServiciosEmpleados.remove(tblServicios_Has_Empleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            desactivarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                if (tblServicios_Has_Empleados.getSelectionModel().getSelectedItem() != null) {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                    cmbIdServicio.setDisable(false);
                    cmbIdEmpleado.setDisable(false);
                    
                    tblServicios_Has_Empleados.setDisable(true);
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
                    cmbIdServicio.setDisable(false);
                    cmbIdEmpleado.setDisable(false);

                } else if (respuesta == JOptionPane.NO_OPTION) {
                    if (tblServicios_Has_Empleados.getSelectionModel().getSelectedItem() != null) {
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
                    btnReporte.setText("Reporte");
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio_has_Empleado(?,?,?,?,?)}");
            Servicios_Has_Empleados registro = (Servicios_Has_Empleados) tblServicios_Has_Empleados.getSelectionModel().getSelectedItem();
            registro.setFechaEvento(parseDate(fecha.getValue()));
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getIdServicio());
            procedimiento.setInt(2, registro.getIdEmpleado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4, registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());

            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reporte() {
        switch (tipoOperacion) {
            case ACTUALIZAR:
                this.desactivarControles();
                btnEditar.setText("Editar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                limpiarControles();
                cargarDatos();
        }
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }

    public void limpiarControles() {
        cmbIdServicio.setItems(getServicios());
        cmbIdEmpleado.setItems(getEmpleados());
        parseDate(fecha.getValue());
        txtLugarEvento.setText("");
        txtHoraEvento.setText("");
        cmbIdServicio.getSelectionModel().clearAndSelect(-1);
    }

    public void activarControles() {
        cmbIdServicio.setDisable(false);
        cmbIdEmpleado.setDisable(false);
        txtLugarEvento.setDisable(false);
        txtHoraEvento.setDisable(false);
        fecha.setDisable(false);
    }

    public void desactivarControles() {
        cmbIdServicio.setDisable(true);
        cmbIdEmpleado.setDisable(true);
        txtLugarEvento.setDisable(true);
        txtHoraEvento.setDisable(true);
        fecha.setDisable(true);
        tblServicios_Has_Empleados.setDisable(false);
    }

    public LocalDate parseDatePicker(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        
        return localDate;
    }
    
    public Date parseDate(LocalDate fecha){
        Date rs = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return rs;
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
}