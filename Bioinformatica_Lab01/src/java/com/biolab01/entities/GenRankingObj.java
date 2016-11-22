/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.entities;

import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author Alejandro
 */
public class GenRankingObj {
    private int id;
    private int[] genArray;
    private Set<Integer> genSet;
    private int cantidad;
    private ArrayList<String> genNamesArray;
    
    public GenRankingObj(){
        
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the genArray
     */
    public int[] getGenArray() {
        return genArray;
    }

    /**
     * @param genArray the genArray to set
     */
    public void setGenArray(int[] genArray) {
        this.genArray = genArray;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the genSet
     */
    public Set<Integer> getGenSet() {
        return genSet;
    }

    /**
     * @param genSet the genSet to set
     */
    public void setGenSet(Set<Integer> genSet) {
        this.genSet = genSet;
    }

    /**
     * @return the genNamesArray
     */
    public ArrayList<String> getGenNamesArray() {
        return genNamesArray;
    }

    /**
     * @param genNamesArray the genNamesArray to set
     */
    public void setGenNamesArray(ArrayList<String> genNamesArray) {
        this.genNamesArray = genNamesArray;
    }
}
