/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aialiagam
 */
public class ClusterObj implements Serializable{
    //<editor-fold defaultstate="collapsed" desc="propiedades privadas">
    private int nroCluster;
    private ArrayList<String> listaGenes;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public ClusterObj(){
        
    }
    
    public ClusterObj(int nroCluster, ArrayList<String> listaGenes){
        this.nroCluster = nroCluster;
        this.listaGenes = listaGenes;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos accesores">
    public int getNroCluster(){
        return this.nroCluster;
    }
    
    public ArrayList<String> getListaGenes(){
        return this.listaGenes;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos mutadores">
    public void setNroCluster(int nroCluster){
        this.nroCluster = nroCluster;
    }
    
    public void setListaGenes(ArrayList<String> listaGenes){
        this.listaGenes = listaGenes;
    }
    
    public void addGenLista(String gen){
        if(this.listaGenes == null){
            this.listaGenes = new ArrayList();
            this.listaGenes.add(gen);
        }
        else{
            if(!this.listaGenes.contains(gen)){
                this.listaGenes.add(gen);
            }
        }
    }
    //</editor-fold>
}
