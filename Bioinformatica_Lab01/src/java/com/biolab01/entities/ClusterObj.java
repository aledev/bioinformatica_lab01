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
    private int nroSolucion;
    private int nroCluster;
    private ArrayList<String> listaGenes;
    private ArrayList<GenDictionary> diccionarioGenes;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public ClusterObj(){
        
    }
    
    public ClusterObj(int nroSolucion, int nroCluster, ArrayList<String> listaGenes, ArrayList<GenDictionary> diccionarioGenes){
        this.nroSolucion = nroSolucion;
        this.nroCluster = nroCluster;
        this.listaGenes = listaGenes;
        this.diccionarioGenes = diccionarioGenes;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos accesores">
    public int getNroSolucion(){
        return this.nroSolucion;
    }
    
    public int getNroCluster(){
        return this.nroCluster;
    }
    
    /**
     * @return the diccionarioGenes
     */
    public ArrayList<GenDictionary> getDiccionarioGenes() {
        return diccionarioGenes;
    }
    
    public ArrayList<String> getListaGenes(){
        return this.listaGenes;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos mutadores">
    public void setNroSolucion(int nroSolucion){
        this.nroSolucion = nroSolucion;
    }
    
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
    
     /**
     * @param diccionarioGenes the diccionarioGenes to set
     */
    public void setDiccionarioGenes(ArrayList<GenDictionary> diccionarioGenes) {
        this.diccionarioGenes = diccionarioGenes;
    }
    
     public void addDiccionarioGenLista(GenDictionary gen){
        if(this.diccionarioGenes == null){
            this.diccionarioGenes = new ArrayList();
            this.diccionarioGenes.add(gen);
        }
        else{
            if(!this.diccionarioGenes.contains(gen)){
                this.diccionarioGenes.add(gen);
            }
        }
    }
    //</editor-fold>
}
