package org.frederickmontiel.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.frederickmontiel.system.Principal;

public class ProgramadorController implements Initializable {    
    private Principal escenarioPrincipal;

    @FXML private Button btRegresar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources){}
    
    public Principal getEscenarioPrincipal(){
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){ 
        escenarioPrincipal.menuPrincipal();
    }
}

