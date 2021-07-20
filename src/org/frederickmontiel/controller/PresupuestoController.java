package org.frederickmontiel.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import org.frederickmontiel.bean.Presupuesto;
import org.frederickmontiel.db.Conexion;
import org.frederickmontiel.report.GenerarReporte;
import org.frederickmontiel.system.Principal;

public class PresupuestoController  implements Initializable {

    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoOperacion = operaciones.NINGUNO;
    private ObservableList<Empresa> listaEmpresa;
    private ObservableList<Presupuesto> listaPresupuesto;

    @FXML private DatePicker fecha;
    @FXML private TextField txtIdPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private ComboBox cmbIdEmpresa;
    @FXML private TableView tblPresupuesto;
    @FXML private TableColumn colIdPresupuesto;
    @FXML private TableColumn colFecSolPresupuesto;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colEmpresaPresupuesto;
    @FXML private Button btnNuevo; 
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
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
        cmbIdEmpresa.setItems(getEmpresa());
        
        fecha.setValue(LocalDate.now());
    }

    public void cargarDatos(){
       tblPresupuesto.setItems(getPresupuesto());
       colIdPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("idPresupuesto"));
       colFecSolPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date>("fechaSolicitud"));
       colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Double>("cantidadPresupuesto"));
       colEmpresaPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("idEmpresa"));    
    }
    
    public void seleccionarElemento(){
        if(tblPresupuesto.getSelectionModel().getSelectedIndex() != -1){   
            txtIdPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getIdPresupuesto()));
            try {
                fecha.setValue(parseDatePicker(String.valueOf(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud())));
            }catch(Exception e){
                e.printStackTrace();
            }
            txtCantidadPresupuesto.setText(Double.toString(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
            cmbIdEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getIdEmpresa()));
        }
    }
    
    public ObservableList<Empresa>getEmpresa(){
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
            ResultSet resultado = procedimiento.executeQuery() ;
            while(resultado.next())
                lista.add(new Empresa(resultado.getInt("idEmpresa"),
                resultado.getString("nombreEmpresa"),
                resultado.getString("direccion"),
                resultado.getString("telefono")));
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return listaEmpresa = FXCollections.observableArrayList(lista);
    } 
           
     public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresa(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("idEmpresa"),
                                        registro.getString("nombreEmpresa"),
                                        registro.getString("direccion"),
                                        registro.getString("telefono"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Presupuesto>getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado =procedimiento.executeQuery();
                while (resultado.next())
                                lista.add(new Presupuesto(resultado.getInt("idPresupuesto"),
                                resultado.getDate("fechaSolicitud"),
                                resultado.getDouble("cantidadPresupuesto"),
                                resultado.getInt("idEmpresa")));
        }catch(Exception e){
            e.printStackTrace();
        }
        return  listaPresupuesto = FXCollections.observableArrayList(lista);
    }

    public void nuevo(){
        switch (tipoOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setDisable(false);
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoOperacion = operaciones.GUARDAR;
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
        
           
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolicitud(parseDate(this.fecha.getValue()));
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setIdEmpresa(((Empresa)cmbIdEmpresa.getSelectionModel().getSelectedItem()).getIdEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?)}");
                                    procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                                    procedimiento.setDouble(2, registro.getCantidadPresupuesto());
                                    procedimiento.setInt(3, registro.getIdEmpresa());
                                    procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    public void eliminar(){
        switch (tipoOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnEliminar.setText("Eliminar");
                btnNuevo.setText("Nuevo");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoOperacion = operaciones.NINGUNO;
            break;
            default:
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Está seguro de eliminar el registro?","Elimar Presupuesto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1, ((Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem()).getIdPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblPresupuesto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    } else{
                        limpiarControles();
                        cargarDatos();
                        desactivarControles();
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
                if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
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
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                }else if(respuesta == JOptionPane.NO_OPTION){
                    if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
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
                    btnReporte.setText("Repote");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                }
            break;
        }
    }
     
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarPresupuesto(?,?,?,?)}");
            Presupuesto registro = (Presupuesto)tblPresupuesto.getSelectionModel().getSelectedItem();
            registro.setFechaSolicitud(parseDate(fecha.getValue()));
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setIdEmpresa(((Empresa)cmbIdEmpresa.getSelectionModel().getSelectedItem()).getIdEmpresa());
                                 procedimiento.setInt(1,registro.getIdPresupuesto());                  
                                 procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
                                 procedimiento.setDouble(3,registro.getCantidadPresupuesto());
                                 procedimiento.setInt(4,registro.getIdEmpresa());
            procedimiento.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
     public void reporte(){
        switch(tipoOperacion){
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
     
     
    public void imprimirReporte(){
        if(tblPresupuesto.getSelectionModel().getSelectedItem() != null){
            Map parametros = new HashMap();
            int idEmpresa_i = Integer.valueOf(((Empresa)cmbIdEmpresa.getSelectionModel().getSelectedItem()).getIdEmpresa());
            parametros.put("idEmpresa_i",idEmpresa_i);
            GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }  
        
    }
     
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();   
    }
    
    public void activarControles(){
        txtIdPresupuesto.setDisable(true);
        txtCantidadPresupuesto.setDisable(false);
        fecha.setDisable(false);
        cmbIdEmpresa.setDisable(false);
        
        tblPresupuesto.setDisable(true);
    }
    
    public void desactivarControles(){
        txtIdPresupuesto.setDisable(true);
        txtCantidadPresupuesto.setDisable(true);
        fecha.setDisable(true);
        cmbIdEmpresa.setDisable(true);
        
        tblPresupuesto.setDisable(false);
    }
 
    public void limpiarControles(){
        txtIdPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
        cmbIdEmpresa.getSelectionModel().clearAndSelect(-1);
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
