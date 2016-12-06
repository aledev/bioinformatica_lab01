/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.beans;

import com.biolab01.entities.ClusterObj;
import com.biolab01.entities.GenDictionary;
import com.biolab01.entities.GenRankingObj;
import com.biolab01.entities.SolucionObj;
import com.biolab01.utils.core.RankingProcedure;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

/**
 *
 * @author aialiagam
 */
@ManagedBean(name = "subirarchivobean", eager = true)
@SessionScoped
public class SubirArchivoBean {
    //<editor-fold defaultstate="collapsed" desc="propiedades privadas">
    private Part archivo;
    private String nombreArchivo;
    private ArrayList<SolucionObj> solucionData;
    private ArrayList<ClusterObj> clusterData;
    private ArrayList<GenDictionary> genDictionaryArray;
    private int nroClusterDetalle;
    private int cantidadGenesCalculo;
    private ClusterObj clusterDetalle;
    private String errorMessage;
    private int cantidadGenesTotal;
    private int cantidadRankingEncontrados;
    private int cantidadGenesPorRanking;
    private ArrayList<GenRankingObj> rankingData;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos accesores">
    public Part getArchivo(){
        return this.archivo;
    }

    public String getNombreArchivo(){
        return this.nombreArchivo;
    }
    
    public int getNroClusterDetalle(){
        return this.nroClusterDetalle;
    }
    
    public ClusterObj getClusterDetalle(){
        return this.clusterDetalle;
    }
    
    public int getCantidadGenesCalculo(){
        return this.cantidadGenesCalculo;
    }
    
    public ArrayList<SolucionObj> getSolucionData(){
        return this.solucionData;
    }
    
    public String getErrorMessage(){
        return this.errorMessage;
    }
    
     /**
     * @return the cantidadGenesTotal
     */
    public int getCantidadGenesTotal() {
        return cantidadGenesTotal;
    }

    /**
     * @return the cantidadRankingEncontrados
     */
    public int getCantidadRankingEncontrados() {
        return cantidadRankingEncontrados;
    }

    /**
     * @return the cantidadGenesPorRanking
     */
    public int getCantidadGenesPorRanking() {
        return cantidadGenesPorRanking;
    }

    /**
     * @return the rankingData
     */
    public ArrayList<GenRankingObj> getRankingData() {
        return rankingData;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos mutadores">
    public void setArchivo(Part archivo){
        this.archivo = archivo;
    }
    
    public void setNroClusterDetalle(int nroClusterDetalle){
        this.nroClusterDetalle = nroClusterDetalle;
    }
    
    public void setCantidadGenesCalculo(int cantidadGenesCalculo){
        this.cantidadGenesCalculo = cantidadGenesCalculo;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos publicos">
    
    //<editor-fold defaultstate="collapsed" desc="subirArchivo">
    public void subirArchivo(){
        try{
            // Limpiamos el mensaje de error
            errorMessage = "";
            // Limpiamos la variables utilizadas
            this.clusterData = null;
            this.genDictionaryArray = null;
            // Seteamos el nombre del Archivo
            this.nombreArchivo = archivo.getSubmittedFileName();
            // Leemos el archivo
            Scanner fileRead = new Scanner(archivo.getInputStream());
            // Creamos una lista con todas las soluciones existentes en el archivo
            ArrayList<SolucionObj> solucionList = new ArrayList();
            // Auxiliar para saber que línea del archivo se esta leyendo actualmente
            int currentLine = 1;
            // Auxiliar para conocer la cantidad de genes existentes en el archivo
            int cantidadGenes = 0;
            
            // Recorremos el archivo
            while(fileRead.hasNextLine()){
                // Obtenemos la línea actual del archivo
                String currentStringLine = fileRead.nextLine();
                
                // Si la línea actual es la primera, entonces es la línea del archivo
                // que contiene todas las soluciones existentes
                if(currentLine == 1){
                    // Obtenemos las soluciones
                    String[] solutionArray = currentStringLine.split(",");
                    
                    // Recorremos el arreglo de soluciones
                    for(int x = 0; x < solutionArray.length; x++){
                        // Creamos un nuevo objeto de tipo solución
                        SolucionObj solucion = new SolucionObj();
                        // Seteamos el número de la solución
                        solucion.setNroSolucion(Integer.parseInt(solutionArray[x].trim()));
                        // Seteamos la posición de la solución actual
                        solucion.setPosition(x);
                        // Agregamos el objeto a la lista de soluciones
                        solucionList.add(solucion);
                    }
                }
                // En caso contrario, vienen la lista de genes y sus respectivas posiciones de cluster
                else{ 
                    // Obtenemos el gen y las posiciones
                    String[] genClusterArray = currentStringLine.split(",");
                    // Obtenemos el nombre del gen
                    String genName = genClusterArray[0].trim();
                    // Creamos un objeto para el diccionario de genes
                    GenDictionary currentGenData = new GenDictionary(cantidadGenes + 1, genName); 
                    // Agregamos el objeto diccionario gen a la lista
                    this.addDiccionarioGenToArray(currentGenData);
                    
                    // Recorremos la lista de clusters donde esta el gen
                    for(int x = 1; x < genClusterArray.length; x++){
                        // Obtenemos el número del cluster actual
                        int clusterNum = Integer.parseInt(genClusterArray[x].trim());
                        
                        // Obtenemos la solución actual de acuerdo a la posición del archivo
                        // en este caso, correspondería al indice actual - 1
                        SolucionObj sol = findSolutionByPosition(solucionList, (x - 1));
                        // Si la solución es diferente de null, es por que existe en la lista
                        if(sol != null){
                            // Le agregamos a la solución el cluster y el gen actual
                            sol.addClusterData(clusterNum, genName, currentGenData);
                        }
                    }
                    
                    cantidadGenes++;
                }
                
                // Aumentamos el auxiliar
                currentLine++;
            }
            
            // Seteamos los datos obtenidos del archivo al objeto solucionData
            // que es el objeto que tomará la vista para mostrar el ranking
            this.solucionData = solucionList;
            
            // Mostramos la cantidad de genes en la consola
            System.out.println("Cantidad de Genes: " + cantidadGenes);
        }
        catch(Exception ex){
            // Mostramos la excepción en la consola
            System.out.println("Error al intentar leer el archivo. Detalle: " + ex.getMessage());
            errorMessage = "Error al intentar leer el archivo. Detalle: " + ex.getMessage();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="calcularRanking">
    public void calcularRanking(){
        long startTime = System.nanoTime();
        
        try{
            
           //this.solution01();
           //this.solution02();
           //this.solution03();
           //this.solution04();
           //this.solution05();
           this.solution06();
        }
        catch(Exception ex){
            // Mostramos la excepción en la consola
            System.out.println("Error al intentar realizar el calculo. Detalle: " + ex.getMessage());
        }
        
        long endTime = System.nanoTime();
        double duration = ((endTime - startTime) / 1000000);
        System.out.println("Tiempo Ejecución: " + duration + " milisegs");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="verDetalleGenes">
    public void verDetalleGenes(int posicionSolucion, int nroCluster){
        try{
            // Limpiamos el mensaje de error
            errorMessage = "";
            SolucionObj posSol = this.findSolutionByPosition(this.solucionData, posicionSolucion);
            if(posSol != null){
                ArrayList<ClusterObj> clusterData = posSol.getClusterData();
                for(ClusterObj clus : clusterData){
                    if(clus.getNroCluster() == nroCluster){
                        this.clusterDetalle = clus;
                        break;
                    }
                }
            }
            
            // Realizamos el redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("detalleLista.xhtml");
        }
        catch(Exception ex){
            // Mostramos la excepción en la consola
            System.out.println("Error al intentar leer el archivo. Detalle: " + ex.getMessage());
            errorMessage = "Error al intentar leer el archivo. Detalle: " + ex.getMessage();
        }
    }
    //</editor-fold>
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos privados">
   
    //<editor-fold defaultstate="collapsed" desc="findSolutionByPosition">   
    // Metodo para obtener una solución de acuerdo a una determinada posición
    private SolucionObj findSolutionByPosition(ArrayList<SolucionObj> arraySolucion, int position){
        SolucionObj objResult = null;
        
        for(SolucionObj sol : arraySolucion){
            if(sol.getPosition() == position){
                objResult = sol;
                break;
            }
        }
        
        return objResult;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="findSolutionByNroSolucion">   
    // Metodo para obtener una solución de acuerdo a un determinado número de solución
    private SolucionObj findSolutionByNroSolucion(ArrayList<SolucionObj> arraySolucion, int nroSolucion){
        SolucionObj objResult = null;
        
        for(SolucionObj sol : arraySolucion){
            if(sol.getNroSolucion() == nroSolucion){
                objResult = sol;
                break;
            }
        }
        
        return objResult;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addGenRankingToArray">
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addGenRankingToArray">
    private void addGenRankingToArray(ArrayList<GenRankingObj> arraySolution, Set<Integer> genArray){
        GenRankingObj objResult = null;
        
        for(GenRankingObj grk : arraySolution){
            if(grk.getGenSet().containsAll(genArray)){
                objResult = grk;
                break;
            }
        }
        
        if(objResult == null){
            objResult = new GenRankingObj();
            objResult.setId(arraySolution.size()+1);
            objResult.setGenSet(genArray);
            objResult.setCantidad(1);
            
            arraySolution.add(objResult);
        }
        else{
            objResult.setCantidad(objResult.getCantidad()+1);            
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="addDiccionarioGenToArray">
    private void addDiccionarioGenToArray(GenDictionary gen){
        if(this.genDictionaryArray == null){
            this.genDictionaryArray = new ArrayList();
            this.genDictionaryArray.add(gen);
        }
        else{
            if(!this.genDictionaryArray.contains(gen)){
                this.genDictionaryArray.add(gen);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution01">
    private void solution01(){
        RankingProcedure ranking = new RankingProcedure();
        
        ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
            ArrayList<int[]> clusterKSubSets = new ArrayList<>();
        ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
        ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

        for (int x = 0; x < totalClusters.size(); x++) {
            ArrayList<int[]> kSubSetsA = ranking.getGenDictionarySubsets(totalClusters.get(x).getDiccionarioGenes(),
                    this.cantidadGenesCalculo);

            for (int y = x + 1; y < totalClusters.size(); y++) {

                ArrayList<int[]> kSubSetsB = ranking.getGenDictionarySubsets(totalClusters.get(y).getDiccionarioGenes(),
                        this.cantidadGenesCalculo);

                for (int[] a : kSubSetsA) {
                    int aux = this.cantidadGenesCalculo;
                    int[] sortedA = a.clone();
                    Arrays.sort(sortedA);
                    String sortedAString = Arrays.toString(sortedA);
                    for (int[] b : kSubSetsB) {
                        int[] sortedB = b.clone();
                        Arrays.sort(sortedB);
                        String sortedBString = Arrays.toString(sortedB);

                        if (sortedAString.equals(sortedBString)) {
                            this.addGenRankingToArray(rankingKSubSets, sortedB);
                        }
                    }
                }
            }
        }

        int maxAux = 0;

        // Obtenemos el valor máximo
        for (GenRankingObj grk : rankingKSubSets) {
            if (maxAux == 0) {
                maxAux = grk.getCantidad();
            } else if (grk.getCantidad() > maxAux) {
                maxAux = grk.getCantidad();
            }
        }

        for (GenRankingObj grk : rankingKSubSets) {
            if (grk.getCantidad() == maxAux) {
                finalRankingKSubSets.add(grk);
            }
        }

        // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
        // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
        System.out.println("Total Clusters: " + totalClusters.size());
        System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
        System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
        System.out.println("Total Subconjuntos: " + clusterKSubSets.size());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution02">
    private void solution02(){
        RankingProcedure ranking = new RankingProcedure();
        
        ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
        ArrayList<int[]> clusterKSubSets = new ArrayList<>();
        ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
        ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

        for (int x = 0; x < totalClusters.size(); x++) {
            List<Set<Integer>> kSubSetsA = ranking.getGenDictionarySubsetsRecursive(totalClusters.get(x).getDiccionarioGenes(),
                    this.cantidadGenesCalculo);

            for (int y = x + 1; y < totalClusters.size(); y++) {
                List<Set<Integer>> kSubSetsB = ranking.getGenDictionarySubsetsRecursive(totalClusters.get(y).getDiccionarioGenes(),
                        this.cantidadGenesCalculo);

                for (Set<Integer> a : kSubSetsA) {
                    for (Set<Integer> b : kSubSetsB) {
                        if (a.containsAll(b)) {
                            this.addGenRankingToArray(rankingKSubSets, b);
                        }
                    }
                }
            }
        }

        int maxAux = 0;

        // Obtenemos el valor máximo
        for (GenRankingObj grk : rankingKSubSets) {
            if (maxAux == 0) {
                maxAux = grk.getCantidad();
            } else if (grk.getCantidad() > maxAux) {
                maxAux = grk.getCantidad();
            }
        }

        for (GenRankingObj grk : rankingKSubSets) {
            if (grk.getCantidad() == maxAux) {
                finalRankingKSubSets.add(grk);
            }
        }

        // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
        // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
        System.out.println("Total Clusters: " + totalClusters.size());
        System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
        System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
        System.out.println("Total Subconjuntos: " + clusterKSubSets.size());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution03">
    private void solution03() throws Exception{
        try {
            RankingProcedure ranking = new RankingProcedure();
            ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
            ArrayList<int[]> clusterKSubSets = new ArrayList<>();
            ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
            ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

            for (int x = 0; x < totalClusters.size(); x++) {
                System.out.println("Fecha: " + new Date().toString());
                System.out.println("Cluster " + x + " de " + totalClusters.size());
                
                ArrayList<int[]> kSubSetsA = new ArrayList<>();
                int[] subsetAuxA = ranking.getGenDictionaryIntValues(totalClusters.get(x).getDiccionarioGenes());
                boolean[] subsetUsedA = new boolean[subsetAuxA.length];
                ranking.getGenDictionarySubsetsRecursive(kSubSetsA, subsetAuxA, this.cantidadGenesCalculo, 0, 0, subsetUsedA);

                for (int y = x + 1; y < totalClusters.size(); y++) {
                    ArrayList<int[]> kSubSetsB = new ArrayList<>();
                    int[] subsetAuxB = ranking.getGenDictionaryIntValues(totalClusters.get(y).getDiccionarioGenes());
                    boolean[] subsetUsedB = new boolean[subsetAuxB.length];
                    ranking.getGenDictionarySubsetsRecursive(kSubSetsB, subsetAuxB, this.cantidadGenesCalculo, 0, 0, subsetUsedB);

                    // Obtenemos la intersección de los arreglos
                    int[] subsetAuxC = ranking.getGenDictionaryRepeatedIntValues(subsetAuxA, subsetAuxB);
                    // Verificamos que la intersección al menos contenga un valor
                    if (subsetAuxC.length > 0) {
                        // Limpiamos los subsets, y dejamos solamente los subconjuntos con los valores que contengan a la intersección
                        ArrayList<int[]> kSubSetsAI = ranking.getGenDictionaryInListIntValues(kSubSetsA, subsetAuxC);
                        ArrayList<int[]> kSubSetsBI = ranking.getGenDictionaryInListIntValues(kSubSetsB, subsetAuxC);
                        // Obtenemos el ranking final de Subconjuntos
                        // Iterativo:
                        ranking.getCommonSubSets(rankingKSubSets, kSubSetsAI, kSubSetsBI);
                        // Recursivo:
                        //ranking.getCommonSubSetsRecursive(rankingKSubSets, kSubSetsAI, kSubSetsBI, 0, 0);
                    }
                }
            }

            int maxAux = 0;

            // Obtenemos el valor máximo
            for (GenRankingObj grk : rankingKSubSets) {
                if (maxAux == 0) {
                    maxAux = grk.getCantidad();
                } else if (grk.getCantidad() > maxAux) {
                    maxAux = grk.getCantidad();
                }
            }

            for (GenRankingObj grk : rankingKSubSets) {
                if (grk.getCantidad() == maxAux) {
                    int[] genIdsAux = grk.getGenArray();
                    ArrayList<String> genNamesAux = new ArrayList<>();
                    for(int g : genIdsAux){
                        genNamesAux.add(this.genDictionaryArray.get(g - 1).getNombreGen());
                    }
                    grk.setGenNamesArray(genNamesAux);
                    
                    finalRankingKSubSets.add(grk);
                }
            }

            this.cantidadGenesCalculo = this.genDictionaryArray.size();
            this.cantidadRankingEncontrados = finalRankingKSubSets.size();
            this.cantidadGenesPorRanking = maxAux;
            this.rankingData = finalRankingKSubSets;

            // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
            // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
            System.out.println("Total Clusters: " + totalClusters.size());
            System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
            System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
            System.out.println("Total Subconjuntos: " + clusterKSubSets.size());

            // Realizamos el redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("ranking.xhtml");
        }
        catch(Exception ex){
            throw ex;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution04">
    private void solution04() throws Exception{
        try {
            RankingProcedure ranking = new RankingProcedure();
            ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
            ArrayList<int[]> clusterKSubSets = new ArrayList<>();
            ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
            ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

            for (int x = 0; x < totalClusters.size(); x++) {
                System.out.println("Fecha: " + new Date().toString());
                System.out.println("Cluster " + x + " de " + totalClusters.size());
                
                ArrayList<int[]> kSubSetsA = new ArrayList<>();
                int[] subsetAuxA = ranking.getGenDictionaryIntValues(totalClusters.get(x).getDiccionarioGenes());
                boolean[] subsetUsedA = new boolean[subsetAuxA.length];
                ranking.getGenDictionarySubsetsRecursive(kSubSetsA, subsetAuxA, this.cantidadGenesCalculo, 0, 0, subsetUsedA);

                for (int y = x + 1; y < totalClusters.size(); y++) {
                    ArrayList<int[]> kSubSetsB = new ArrayList<>();
                    int[] subsetAuxB = ranking.getGenDictionaryIntValues(totalClusters.get(y).getDiccionarioGenes());
                    boolean[] subsetUsedB = new boolean[subsetAuxB.length];
                    ranking.getGenDictionarySubsetsRecursive(kSubSetsB, subsetAuxB, this.cantidadGenesCalculo, 0, 0, subsetUsedB);

                    // Obtenemos la intersección de los arreglos
                    int[] subsetAuxC = ranking.getGenDictionaryRepeatedIntValues(subsetAuxA, subsetAuxB);
                    // Verificamos que la intersección al menos contenga un valor
                    if (subsetAuxC.length > 0) {
                        // Limpiamos los subsets, y dejamos solamente los subconjuntos con los valores que contengan a la intersección
                        ArrayList<int[]> kSubSetsAI = ranking.getGenDictionaryInListIntValues(kSubSetsA, subsetAuxC);
                        ArrayList<int[]> kSubSetsBI = ranking.getGenDictionaryInListIntValues(kSubSetsB, subsetAuxC);
                        // Obtenemos el ranking final de Subconjuntos
                        // Iterativo:
                        ranking.getCommonSubSetsIterative(rankingKSubSets, kSubSetsAI, kSubSetsBI);
                    }
                }
            }

            int maxAux = 0;

            // Obtenemos el valor máximo
            for (GenRankingObj grk : rankingKSubSets) {
                if (maxAux == 0) {
                    maxAux = grk.getCantidad();
                } else if (grk.getCantidad() > maxAux) {
                    maxAux = grk.getCantidad();
                }
            }

            for (GenRankingObj grk : rankingKSubSets) {
                if (grk.getCantidad() == maxAux) {
                    int[] genIdsAux = grk.getGenArray();
                    ArrayList<String> genNamesAux = new ArrayList<>();
                    for(int g : genIdsAux){
                        genNamesAux.add(this.genDictionaryArray.get(g - 1).getNombreGen());
                    }
                    grk.setGenNamesArray(genNamesAux);
                    
                    finalRankingKSubSets.add(grk);
                }
            }

            this.cantidadGenesCalculo = this.genDictionaryArray.size();
            this.cantidadRankingEncontrados = finalRankingKSubSets.size();
            this.cantidadGenesPorRanking = maxAux;
            this.rankingData = finalRankingKSubSets;

            // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
            // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
            System.out.println("Total Clusters: " + totalClusters.size());
            System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
            System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
            System.out.println("Total Subconjuntos: " + clusterKSubSets.size());

            // Realizamos el redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("ranking.xhtml");
        }
        catch(Exception ex){
            throw ex;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution05">
    private void solution05() throws Exception{
        try {
            RankingProcedure ranking = new RankingProcedure();
            ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
            ArrayList<int[]> clusterKSubSets = new ArrayList<>();
            ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
            ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

            for (int x = 0; x < totalClusters.size(); x++) {
                System.out.println("Fecha: " + new Date().toString());
                System.out.println("Cluster " + x + " de " + totalClusters.size());
                
                ArrayList<int[]> kSubSetsA = new ArrayList<>();
                int[] subsetAuxA = ranking.getGenDictionaryIntValues(totalClusters.get(x).getDiccionarioGenes());
                boolean[] subsetUsedA = new boolean[subsetAuxA.length];
                ranking.getGenDictionarySubsetsRecursive(kSubSetsA, subsetAuxA, this.cantidadGenesCalculo, 0, 0, subsetUsedA);

                for (int y = x + 1; y < totalClusters.size(); y++) {
                    ArrayList<int[]> kSubSetsB = new ArrayList<>();
                    int[] subsetAuxB = ranking.getGenDictionaryIntValues(totalClusters.get(y).getDiccionarioGenes());
                    boolean[] subsetUsedB = new boolean[subsetAuxB.length];
                    ranking.getGenDictionarySubsetsRecursive(kSubSetsB, subsetAuxB, this.cantidadGenesCalculo, 0, 0, subsetUsedB);

                    // Obtenemos la intersección de los arreglos
                    int[] subsetAuxC = ranking.getGenDictionaryRepeatedIntValues(subsetAuxA, subsetAuxB);
                    // Verificamos que la intersección al menos contenga un valor
                    if (subsetAuxC.length > 0) {
                        // Limpiamos los subsets, y dejamos solamente los subconjuntos con los valores que contengan a la intersección
                        ArrayList<int[]> kSubSetsAI = ranking.getGenDictionaryInListIntValues(kSubSetsA, subsetAuxC);
                        ArrayList<int[]> kSubSetsBI = ranking.getGenDictionaryInListIntValues(kSubSetsB, subsetAuxC);
                        // Obtenemos el ranking final de Subconjuntos
                        ranking.getCommonSubSetsRandom(rankingKSubSets, kSubSetsAI, kSubSetsBI);
                    }
                }
            }

            int maxAux = 0;

            // Obtenemos el valor máximo
            for (GenRankingObj grk : rankingKSubSets) {
                if (maxAux == 0) {
                    maxAux = grk.getCantidad();
                } else if (grk.getCantidad() > maxAux) {
                    maxAux = grk.getCantidad();
                }
            }

            for (GenRankingObj grk : rankingKSubSets) {
                if (grk.getCantidad() == maxAux) {
                    int[] genIdsAux = grk.getGenArray();
                    ArrayList<String> genNamesAux = new ArrayList<>();
                    for(int g : genIdsAux){
                        genNamesAux.add(this.genDictionaryArray.get(g - 1).getNombreGen());
                    }
                    grk.setGenNamesArray(genNamesAux);
                    
                    finalRankingKSubSets.add(grk);
                }
            }

            this.cantidadGenesCalculo = this.genDictionaryArray.size();
            this.cantidadRankingEncontrados = finalRankingKSubSets.size();
            this.cantidadGenesPorRanking = maxAux;
            this.rankingData = finalRankingKSubSets;

            // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
            // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
            System.out.println("Total Clusters: " + totalClusters.size());
            System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
            System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
            System.out.println("Total Subconjuntos: " + clusterKSubSets.size());

            // Realizamos el redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("ranking.xhtml");
        }
        catch(Exception ex){
            throw ex;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="solution06">
    private void solution06() throws Exception{
        try {
            RankingProcedure ranking = new RankingProcedure();
            ArrayList<ClusterObj> totalClusters = ranking.getClusterArrayList(solucionData, this.cantidadGenesCalculo);
            ArrayList<int[]> clusterKSubSets = new ArrayList<>();
            ArrayList<GenRankingObj> rankingKSubSets = new ArrayList<GenRankingObj>();
            ArrayList<GenRankingObj> finalRankingKSubSets = new ArrayList<GenRankingObj>();

            for (int x = 0; x < totalClusters.size(); x++) {
                System.out.println("Fecha: " + new Date().toString());
                System.out.println("Cluster " + x + " de " + totalClusters.size());
                
                ArrayList<int[]> kSubSetsA = new ArrayList<>();
                int[] subsetAuxA = ranking.getGenDictionaryIntValues(totalClusters.get(x).getDiccionarioGenes());
                boolean[] subsetUsedA = new boolean[subsetAuxA.length];
                ranking.getGenDictionarySubsetsRecursive(kSubSetsA, subsetAuxA, this.cantidadGenesCalculo, 0, 0, subsetUsedA);
                // Obtenemos una muestra del 15% del subconjunto A
                int sampleIndexA = (int)Math.round(kSubSetsA.size() * 0.15);
                if(sampleIndexA > 0){
                    kSubSetsA = ranking.getRandomSample(kSubSetsA, sampleIndexA);
                }

                for (int y = x + 1; y < totalClusters.size(); y++) {
                    ArrayList<int[]> kSubSetsB = new ArrayList<>();
                    int[] subsetAuxB = ranking.getGenDictionaryIntValues(totalClusters.get(y).getDiccionarioGenes());
                    boolean[] subsetUsedB = new boolean[subsetAuxB.length];
                    ranking.getGenDictionarySubsetsRecursive(kSubSetsB, subsetAuxB, this.cantidadGenesCalculo, 0, 0, subsetUsedB);
                    // Obtenemos un muestra del 15% del subconjunto B
                    int sampleIndexB = (int)Math.round(kSubSetsB.size() * 0.15);
                    if(sampleIndexB > 0){
                        kSubSetsB = ranking.getRandomSample(kSubSetsB, (int)Math.round(kSubSetsB.size() * 0.15));
                    }
                    
                    // Obtenemos la intersección de los arreglos
                    int[] subsetAuxC = ranking.getGenDictionaryRepeatedIntValues(subsetAuxA, subsetAuxB);
                    // Verificamos que la intersección al menos contenga un valor
                    if (subsetAuxC.length > 0) {
                        // Limpiamos los subsets, y dejamos solamente los subconjuntos con los valores que contengan a la intersección
                        ArrayList<int[]> kSubSetsAI = ranking.getGenDictionaryInListIntValues(kSubSetsA, subsetAuxC);
                        ArrayList<int[]> kSubSetsBI = ranking.getGenDictionaryInListIntValues(kSubSetsB, subsetAuxC);
                        // Obtenemos el ranking final de Subconjuntos
                        // Iterativo:
                        ranking.getCommonSubSets(rankingKSubSets, kSubSetsAI, kSubSetsBI);
                    }
                }
            }

            int maxAux = 0;

            // Obtenemos el valor máximo
            for (GenRankingObj grk : rankingKSubSets) {
                if (maxAux == 0) {
                    maxAux = grk.getCantidad();
                } else if (grk.getCantidad() > maxAux) {
                    maxAux = grk.getCantidad();
                }
            }

            for (GenRankingObj grk : rankingKSubSets) {
                if (grk.getCantidad() == maxAux) {
                    int[] genIdsAux = grk.getGenArray();
                    ArrayList<String> genNamesAux = new ArrayList<>();
                    for(int g : genIdsAux){
                        genNamesAux.add(this.genDictionaryArray.get(g - 1).getNombreGen());
                    }
                    grk.setGenNamesArray(genNamesAux);
                    
                    finalRankingKSubSets.add(grk);
                }
            }

            this.cantidadGenesCalculo = this.genDictionaryArray.size();
            this.cantidadRankingEncontrados = finalRankingKSubSets.size();
            this.cantidadGenesPorRanking = maxAux;
            this.rankingData = finalRankingKSubSets;

            // Obtenemos la cantidad de combinaciones que se pueden hacer con los genes
            // ArrayList<int[]> kSubSets = ranking.getGenDictionarySubsets(genDictionaryArray, this.cantidadGenesCalculo);
            System.out.println("Total Clusters: " + totalClusters.size());
            System.out.println("Cantidad Genes: " + this.genDictionaryArray.size());
            System.out.println("Ranking Subsets: " + finalRankingKSubSets.size() + ", Cantidad Max: " + maxAux);
            System.out.println("Total Subconjuntos: " + clusterKSubSets.size());

            // Realizamos el redirect
            FacesContext.getCurrentInstance().getExternalContext().redirect("ranking.xhtml");
        }
        catch(Exception ex){
            throw ex;
        }
    }
    //</editor-fold>
    
    //</editor-fold>
}
