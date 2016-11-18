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
public class SolucionObj implements Serializable{
    //<editor-fold defaultstate="collapsed" desc="propiedades privadas">
    private int position;
    private int nroSolucion;
    private ArrayList<ClusterObj> clusterData;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public SolucionObj(){
        
    }
    
    public SolucionObj(int nroSolucion, ArrayList<ClusterObj> clusterData){
        this.nroSolucion = nroSolucion;
        this.clusterData = clusterData;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos accesores">
    public int getPosition(){
        return this.position;
    }
    
    public int getNroSolucion(){
        return this.nroSolucion;
    }
    
    public ArrayList<ClusterObj> getClusterData(){
        return this.clusterData;
    }
    //</editor-fold>
   
    //<editor-fold defaultstate="collapsed" desc="metodos mutadores">
    public void setPosition(int position){
        this.position = position;
    }
    
    public void setNroSolucion(int nroSolucion){
        this.nroSolucion = nroSolucion;
    }
    
    public void setClusterData(ArrayList<ClusterObj> clusterData){
        this.clusterData = clusterData;
    }
    
    public void addClusterData(int clusterNum, String genName){
        if(this.clusterData == null){
            this.clusterData = new ArrayList();
            ClusterObj clusterObj = new ClusterObj();
            clusterObj.setNroCluster(clusterNum);
            clusterObj.addGenLista(genName);
            
            this.clusterData.add(clusterObj);
        }
        else{
            ClusterObj clusAux = this.findClusterByNum(clusterNum);
            
            if(clusAux != null){
                clusAux.addGenLista(genName);
            }
            else{
                clusAux = new ClusterObj();
                clusAux.setNroCluster(clusterNum);
                clusAux.addGenLista(genName);
                this.clusterData.add(clusAux);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos privados">
    
    // Metodo para buscar un cluster existente en la solución
    // de acuerdo a un determinado número de cluster
    private ClusterObj findClusterByNum(int clusterNum){
        ClusterObj objResult = null;
        
        for(ClusterObj clus : clusterData){
            if(clus.getNroCluster() == clusterNum){
                objResult = clus;
                break;
            }
        }
        
        return objResult;
    }
    //</editor-fold>
}
