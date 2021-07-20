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
import org.frederickmontiel.bean.Empresa;
import org.frederickmontiel.bean.Servicio;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.system.Principal;

public class ServiciosController implements Initializable {
    private Principal escenarioPrincipal ;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO}
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Servicio> listaServicio ;
    private ObservableList< Empresa> listaEmpresa;
    
    @FXML private DatePicker fecha;
    @FXML private TextField txtIdServicio;
    @FXML private TextField txtTipoServicio;
    @FXML private TextField txtLugarServicio ;
    @FXML private TextField txtHoraServicio ;
    @FXML private TextField txtTelefonoContacto;
    @FXML private ComboBox  cmbIdEmpresa;
    @FXML private TableView tblServicios;
    @FXML private TableColumn colIdServicio ;
    @FXML private TableColumn colTipoServicio;
    @FXML private TableColumn colFechaServicio;
    @FXML private TableColumn colHoraServicio ;
    @FXML private TableColumn colLugarServicio ;
    @FXML private TableColumn colTelefonoContacto;
    @FXML private TableColumn colCodigoEmpresa ;
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        desactivarControles();
        cmbIdEmpresa.setItems(getEmpresa());
        cmbIdEmpresa.setDisable(true);
        
        fecha.setValue(LocalDate.now());
    }
    
    public void cargarDatos(){
        tblServicios.setItems(getServicio());
        colIdServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("idServicio"));
        colFechaServicio.setCellValueFactory(new PropertyValueFactory<Servicio,Date>("fechaServicio"));
        colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("tipoServicio"));
        colHoraServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("horaServicio"));
        colLugarServicio.setCellValueFactory(new PropertyValueFactory<Servicio,String>("lugarServicio"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Servicio,String>("telefonoContacto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Servicio,Integer>("idEmpresa"));    
    }
    
    public ObservableList <Servicio> getServicio(){
        ArrayList<Servicio> lista = new ArrayList<Servicio>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while(resultado.next())
                lista.add(new Servicio(resultado.getInt("idServicio"),
                    resultado.getDate("fechaServicio"),
                    resultado.getString("tipoServicio"),
                    resultado.getString("horaServicio"),
                    resultado.getString("lugarServicio"),
                    resultado.getString("telefonoContacto"),
                    resultado.getInt("idEmpresa")));
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Empresa> getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(
                    resultado.getInt("idEmpresa"),
                    resultado.getString("nombreEmpresa"),
                    resultado.getString("direccion"),
                    resultado.getString("telefono"))
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public void seleccionarElemento(){
        txtIdServicio.setText(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getIdServicio())); 
        txtTipoServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTipoServicio());
        txtLugarServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getLugarServicio());
        fecha.setValue(parseDatePicker(String.valueOf(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getFechaServicio()))); 
        txtTelefonoContacto.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getTelefonoContacto());
        txtHoraServicio.setText(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getHoraServicio());
        cmbIdEmpresa.getSelectionModel().select(buscarEmpresa(((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getIdEmpresa()));
    }
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(
                    registro.getInt("idEmpresa"),
                    registro.getString("nombreEmpresa"),
                    registro.getString("direccion"),
                    registro.getString("telefono")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public void nuevo(){
        switch (tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(true);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.GUARDAR;
                tblServicios.setDisable(true);
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
                tblServicios.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    
    public void guardar(){
        Servicio registro = new Servicio();
        registro.setFechaServicio(parseDate(fecha.getValue()));
        registro.setTipoServicio(txtTipoServicio.getText());
        registro.setHoraServicio(txtHoraServicio.getText());
        registro.setLugarServicio(txtLugarServicio.getText());
        registro.setTelefonoContacto(txtTelefonoContacto.getText());
        registro.setIdEmpresa(((Empresa)cmbIdEmpresa.getSelectionModel().getSelectedItem()).getIdEmpresa());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicio(?,?,?,?,?,?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(2,registro.getTipoServicio());
            procedimiento.setString(3,registro.getHoraServicio());
            procedimiento.setString(4,registro.getLugarServicio());
            procedimiento.setString(5,registro.getTelefonoContacto());
            procedimiento.setInt(6,registro.getIdEmpresa());
            procedimiento.execute();
            
            listaServicio.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
       
    public void eliminar(){
        switch(tipoOperacion){
            default:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Elimar Servicio",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicio(?)}");
                            procedimiento.setInt(1, ((Servicio)tblServicios.getSelectionModel().getSelectedItem()).getIdServicio());
                            procedimiento.execute();
                            listaServicio.remove(tblServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }   
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }  
            break;
                
        }   
    }

    public void editar(){
        switch(tipoOperacion){
            case NINGUNO:
                if(tblServicios.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                }    
            break;
            case ACTUALIZAR:
                int respuesta = JOptionPane.showConfirmDialog(null,("¿Esta seguro de editar el registro"));
                
                if(respuesta == JOptionPane.YES_OPTION){
                    actualizar();  
                    btnEditar.setText("Editar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    desactivarControles();
                    limpiarControles();
                }else if(respuesta == JOptionPane.NO_OPTION){
                    if(tblServicios.getSelectionModel().getSelectedItem() != null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        activarControles();
                        tipoOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null,"Debe seleccionar un elemento");
                    }      
                }else if(respuesta == JOptionPane.CANCEL_OPTION){
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
    

    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicio(?,?,?,?,?,?,?)}");
            Servicio registro = (Servicio)tblServicios.getSelectionModel().getSelectedItem();
            registro.setFechaServicio(parseDate(fecha.getValue()));
            registro.setTipoServicio(txtTipoServicio.getText());
            registro.setHoraServicio(txtHoraServicio.getText());
            registro.setLugarServicio(txtLugarServicio.getText());
            registro.setTelefonoContacto(txtTelefonoContacto.getText());
            registro.setIdEmpresa(((Empresa)cmbIdEmpresa.getSelectionModel().getSelectedItem()).getIdEmpresa());
            procedimiento.setInt(1,registro.getIdServicio());                  
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicio().getTime()));
            procedimiento.setString(3,registro.getTipoServicio());
            procedimiento.setString(4,registro.getHoraServicio());
            procedimiento.setString(5,registro.getLugarServicio());
            procedimiento.setString(6,registro.getTelefonoContacto());
            procedimiento.setInt(7,registro.getIdEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
         
    public void cancelar(){
        switch(tipoOperacion){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO ;
                break;
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
        }
    }

    public void activarControles(){
        txtIdServicio.setDisable(true);
        txtTipoServicio.setDisable(false);
        txtLugarServicio.setDisable(false);
        txtHoraServicio.setDisable(false);
        txtTelefonoContacto.setDisable(false);
        fecha.setDisable(false);
        cmbIdEmpresa.setDisable(false);
        btnReporte.setDisable(false);
        
        tblServicios.setDisable(true);
    }
    
    public void desactivarControles(){
        txtIdServicio.setDisable(true);
        txtTipoServicio.setDisable(true);
        txtLugarServicio.setDisable(true);
        txtHoraServicio.setDisable(true);
        txtTelefonoContacto.setDisable(true);
        fecha.setDisable(true);
        cmbIdEmpresa.setDisable(true);
        btnReporte.setDisable(true);
        
        tblServicios.setDisable(false);
    }
    
    public void limpiarControles(){
        txtIdServicio.setText("");
        txtTipoServicio.setText("");
        txtLugarServicio.setText("");
        txtHoraServicio.setText("");
        txtTelefonoContacto.setText("");
        cmbIdEmpresa.getSelectionModel().clearAndSelect(-1);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();   
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
}