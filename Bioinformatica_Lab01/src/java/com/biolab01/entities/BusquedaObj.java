/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.entities;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.Part;

/**
 *
 * @author aialiagam
 */
@ManagedBean(name = "busquedaobj", eager = true)
@RequestScoped
public class BusquedaObj {
    //<editor-fold defaultstate="collapsed" desc="propiedades privadas">
    private Part archivo;
    private String pathArchivo;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public BusquedaObj(){
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="mutadores">
    public void setArchivo(Part archivo){
        this.archivo = archivo;
    }
    
    public void setPathArchivo(String pathArchivo){
        this.pathArchivo = pathArchivo;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="accesores">
    public Part getArchivo(){
        return this.archivo;
    }
    
    public String getPathArhivo(){
        return this.pathArchivo;
    }
    //</editor-fold>
}
