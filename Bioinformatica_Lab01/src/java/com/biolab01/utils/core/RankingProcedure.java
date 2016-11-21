/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.utils.core;

import com.biolab01.entities.ClusterObj;
import com.biolab01.entities.GenDictionary;
import com.biolab01.entities.SolucionObj;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author aialiagam
 */
public class RankingProcedure {
    //<editor-fold defaultstate="collapsed" desc="constructores">
    public RankingProcedure(){
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos publicos">
    public ArrayList<int[]> getGenDictionarySubsets(ArrayList<GenDictionary> genLista, int k){
        int[] superSet = new int[genLista.size()];
        
        for(int x = 0; x < genLista.size(); x++){
            superSet[x] = genLista.get(x).getNumeroGen();
        }
        
        ArrayList<int[]> subsets = new ArrayList<>();
        int[] s = new int[k]; // here we'll keep indices pointing to elements in input array
        if (k <= superSet.length) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++);  
            subsets.add(getSubset(superSet, s));
            for(;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == superSet.length - k + i; i--); 
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1; 
                    }
                    subsets.add(getSubset(superSet, s));
                }
            }
        }
        
        return subsets;
    }
    
    public ArrayList<ClusterObj> getClusterArrayList(ArrayList<SolucionObj> solArray, int kGenes){
        ArrayList<ClusterObj> resultObj = new ArrayList<>();
        
        for(int x = 0; x < solArray.size(); x++){
            for(int y = 0; y < solArray.get(x).getClusterData().size(); y++){
                ClusterObj currentClusterAux = solArray.get(x).getClusterData().get(y);
                
                if(currentClusterAux.getListaGenes().size() >= kGenes){
                    resultObj.add(currentClusterAux);
                }
            }
        }
        
        return resultObj;
    }
    
    public List<Set<Integer>> getGenDictionarySubsetsRecursive(ArrayList<GenDictionary> genLista, int k){
        List<Integer> superSet = new ArrayList<>();
        
        for(int x = 0; x < genLista.size(); x++){
            superSet.add(genLista.get(x).getNumeroGen());
        }
        
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        
        return res;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos privados">
    private void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        int x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }
    
     private int[] getSubset(int[] input, int[] subset) {
        int[] result = new int[subset.length]; 
        for (int i = 0; i < subset.length; i++) 
            result[i] = input[subset[i]];
        return result;
    }
    //</editor-fold>
}
