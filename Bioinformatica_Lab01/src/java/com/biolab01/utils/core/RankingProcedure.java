/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.utils.core;

import com.biolab01.entities.ClusterObj;
import com.biolab01.entities.GenDictionary;
import com.biolab01.entities.GenRankingObj;
import com.biolab01.entities.SolucionObj;
import java.util.ArrayList;
import java.util.Arrays;
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
    
    public void getGenDictionarySubsetsRecursive(ArrayList<int[]> result, int[] A, int k, int start, int currLen, boolean[] used){
        if (currLen == k) {
            int[] subSetAux = new int[k];
            int ssai = 0;
            for (int i = 0; i < A.length; i++) {
                if (used[i] == true) {
                    subSetAux[ssai] = A[i];
                    ssai++;
                }
            }
            result.add(subSetAux);
            return;
        }
        if (start == A.length) {
            return;
        }
        // For every index we have two options,
        // 1.. Either we select it, means put true in used[] and make currLen+1
        used[start] = true;
        getGenDictionarySubsetsRecursive(result, A, k, start + 1, currLen + 1, used);
        // 2.. OR we dont select it, means put false in used[] and dont increase
        // currLen
        used[start] = false;
        getGenDictionarySubsetsRecursive(result, A, k, start + 1, currLen, used);
    }
    
    public int[] getGenDictionaryIntValues(ArrayList<GenDictionary> genLista){
        int[] subset = new int[genLista.size()];
        
        for(int x = 0; x < genLista.size(); x++){
            subset[x] = genLista.get(x).getNumeroGen();
        }
        
        return subset;
    }
    
    public int[] getGenDictionaryRepeatedIntValues(int[] a, int[] b){
        ArrayList<Integer> repeatedAux = new ArrayList<Integer>();
        // Ordenamos los arreglos
        Arrays.sort(a);
        Arrays.sort(b);
        // Recorremos los arreglos
        for(int x = 0; x < a.length; x++){
            for(int y = 0; y < b.length; y++){
                if(a[x] == b[y] && !repeatedAux.contains(b[y])){
                    repeatedAux.add(b[y]);
                }
            }
        }
        
        int[] subset = new int[repeatedAux.size()];
        
        for(int x = 0; x < repeatedAux.size(); x++){
            subset[x] = repeatedAux.get(x);
        }
        
        return subset;
    }
    
    public ArrayList<int[]> getGenDictionaryInListIntValues(ArrayList<int[]> kSubSets, int[] fList){
        ArrayList<int[]> objResult = new ArrayList<>();
        // Ordenamos los arreglos
        Arrays.sort(fList);
        // Recorremos los arreglos
        for(int x = 0; x < kSubSets.size(); x++){
            boolean isOK = true;
            
            for(int y = 0; y < fList.length; y++){
                if(Arrays.binarySearch(kSubSets.get(x), fList[y]) == -1){
                    isOK = false;
                    break;
                }
            }
            
            if(isOK){
                Arrays.sort(kSubSets.get(x));
                objResult.add(kSubSets.get(x));
            }
        }
        
        return objResult;
    }
    
    public void getCommonSubSets(ArrayList<GenRankingObj> arraySolution, ArrayList<int[]> ksubsetA, ArrayList<int[]> ksubsetB){
        // Parcializamos la cantidad de datos para la recursión
        /*int init = 0;
        int grow = 1000;
        boolean isCompleted = false;
        
        for (int[] a : ksubsetA) {
            init = 0;
            isCompleted = false;
            List<int[]> ksubsetBAux = null;
            
            while(!isCompleted){
                ksubsetBAux = new ArrayList<>();
                
                if(init >= ksubsetB.size()){
                    int diff = init - ksubsetB.size() + 1;
                    ksubsetBAux = ksubsetB.subList(init - diff, ksubsetB.size() - 1);
                    isCompleted = true;
                }
                else{
                    if(((init + grow) - 1) > ksubsetB.size()){
                        ksubsetBAux = ksubsetB.subList(init, ksubsetB.size() - 1);
                        isCompleted = true;
                    }
                    else{
                        ksubsetBAux = ksubsetB.subList(init, (init + grow) - 1);
                        init = init + grow;
                    }
                }
                
                getCommonSubSetRecursiveComparison(arraySolution, a, ksubsetBAux, 0);
            }
        }*/
        
        int init = 0;
        int grow = 10;
        boolean isCompleted = false;
        List<int[]> ksubsetAAux = null;
        
        while(!isCompleted){
            ksubsetAAux = new ArrayList<>();

            if(init >= ksubsetA.size()){
                int diff = init - ksubsetA.size() + 1;
                ksubsetAAux = ksubsetA.subList(init - diff, ksubsetA.size() - 1);
                isCompleted = true;
            }
            else{
                if(((init + grow) - 1) > ksubsetA.size()){
                    ksubsetAAux = ksubsetA.subList(init, ksubsetA.size() - 1);
                    isCompleted = true;
                }
                else{
                    ksubsetAAux = ksubsetA.subList(init, (init + grow) - 1);
                    init = init + grow;
                }
            }

            this.getCommonSubSetsARecursive(arraySolution, ksubsetAAux, ksubsetB, 0);
        }
        
        // Metodo Recursivo
        //this.getCommonSubSetsARecursive(arraySolution, ksubsetA, ksubsetB, 0);
    }
    
    public void getCommonSubSetsRecursive(ArrayList<GenRankingObj> arraySolution, ArrayList<int[]> ksubsetA, ArrayList<int[]> ksubsetB, int[] ksubSetIdxA, int[] ksubSetIdxB, int idxA, int idxB){
        if(idxA == ksubsetA.size())
            return;
        if(idxB == ksubsetB.size()){
            idxA++;
            idxB = 0;
        }
        else{
            if(ksubSetIdxA == null && ksubSetIdxB == null){
                ksubSetIdxA = ksubsetA.get(idxA);
                ksubSetIdxB = ksubsetB.get(idxB);
                
                getCommonSubSetsRecursive(arraySolution, ksubsetA, ksubsetB, ksubSetIdxA, ksubSetIdxB, idxA, idxB);
            }
            else{
                if(this.equalsHelper(ksubSetIdxA, ksubSetIdxB, 0)){
                    this.addGenRankingToArray(arraySolution, ksubSetIdxB);
                }
                idxB++;
            }
        }    
        
        getCommonSubSetsRecursive(arraySolution, ksubsetA, ksubsetB, ksubSetIdxA, ksubSetIdxB, idxA, idxB);
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
     
     private void addGenRankingToArray(ArrayList<GenRankingObj> arraySolution, int[] genArray){
        GenRankingObj objResult = null;
        
        for(GenRankingObj grk : arraySolution){
            int[] sortedA = grk.getGenArray().clone();
            Arrays.sort(sortedA);
            String sortedAString = Arrays.toString(sortedA);
            
            if(sortedAString.equals(Arrays.toString(genArray))){
                objResult = grk;
                break;
            }
        }
        
        if(objResult == null){
            objResult = new GenRankingObj();
            objResult.setId(arraySolution.size()+1);
            objResult.setGenArray(genArray);
            objResult.setCantidad(1);
            
            arraySolution.add(objResult);
        }
        else{
            objResult.setCantidad(objResult.getCantidad()+1);            
        }
    }
     
    private boolean equalsHelper(int[] first, int[] second, int indx) {
        if(indx == first.length)
            return true;
        if(first[indx] != second[indx])
            return false;
        return equalsHelper(first, second, indx + 1);
    }
    
    private void getCommonSubSetsARecursive(ArrayList<GenRankingObj> arraySolution, List<int[]> ksubsetA, ArrayList<int[]> ksubsetB, int idx){
        if (idx == ksubsetA.size()) {
            return;
        } 
        else {
            // Obtenemos el subset actual del conjunto "A"
            int[] ksubsetIdxA = ksubsetA.get(idx);
            // Parcializamos la cantidad de datos para la recursión
            int init = 0;
            int grow = 1000;
            boolean isCompleted = false;
            List<int[]> ksubsetBAux = null;
            
            while (!isCompleted) {
                ksubsetBAux = new ArrayList<>();

                if (init >= ksubsetB.size()) {
                    int diff = init - ksubsetB.size() + 1;
                    ksubsetBAux = ksubsetB.subList(init - diff, ksubsetB.size() - 1);
                    isCompleted = true;
                } else if (((init + grow) - 1) > ksubsetB.size()) {
                    ksubsetBAux = ksubsetB.subList(init, ksubsetB.size() - 1);
                    isCompleted = true;
                } else {
                    ksubsetBAux = ksubsetB.subList(init, (init + grow) - 1);
                    init = init + grow;
                }

                getCommonSubSetRecursiveComparison(arraySolution, ksubsetIdxA, ksubsetBAux, 0);
            }

            idx++;
            getCommonSubSetsARecursive(arraySolution, ksubsetA, ksubsetB, idx);
        }
    }
    
    private void getCommonSubSetRecursiveComparison(ArrayList<GenRankingObj> arraySolution, int[] subsetA, ArrayList<int[]> ksubsetB, int idx){
        if(idx == ksubsetB.size())
            return;
        else{
            int[] ksubsetIdxB = ksubsetB.get(idx);
            if(this.equalsHelper(subsetA, ksubsetIdxB, 0)){
                this.addGenRankingToArray(arraySolution, ksubsetIdxB);
            }
            idx++;
            getCommonSubSetRecursiveComparison(arraySolution, subsetA, ksubsetB, idx);
        }
    }
    
    private void getCommonSubSetRecursiveComparison(ArrayList<GenRankingObj> arraySolution, int[] subsetA, List<int[]> ksubsetB, int idx){
        if(idx == ksubsetB.size())
            return;
        else{
            int[] ksubsetIdxB = ksubsetB.get(idx);
            //if(this.equalsHelper(subsetA, ksubsetIdxB, 0)){
            if(Arrays.equals(subsetA, ksubsetIdxB)){
                this.addGenRankingToArray(arraySolution, ksubsetIdxB);
            }
            idx++;
            getCommonSubSetRecursiveComparison(arraySolution, subsetA, ksubsetB, idx);
        }
    }
    //</editor-fold>
}
