/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.entities;

/**
 *
 * @author aialiagam
 */
public class GenDictionary {
    //<editor-fold defaultstate="collapsed" desc="propiedades privadas">
    private int numeroGen;
    private String nombreGen;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public GenDictionary(){
        
    }
    
    public GenDictionary(int numeroGen, String nombreGen){
        this.numeroGen = numeroGen;
        this.nombreGen = nombreGen;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="metodos accesores y mutadores">
    /**
     * @return the numeroGen
     */
    public int getNumeroGen() {
        return numeroGen;
    }

    /**
     * @param numeroGen the numeroGen to set
     */
    public void setNumeroGen(int numeroGen) {
        this.numeroGen = numeroGen;
    }

    /**
     * @return the nombreGen
     */
    public String getNombreGen() {
        return nombreGen;
    }

    /**
     * @param nombreGen the nombreGen to set
     */
    public void setNombreGen(String nombreGen) {
        this.nombreGen = nombreGen;
    }
    //</editor-fold>
}
