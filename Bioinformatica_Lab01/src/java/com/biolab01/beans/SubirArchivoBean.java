/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.beans;

import com.biolab01.entities.ClusterObj;
import com.biolab01.entities.SolucionObj;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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
    private int nroClusterDetalle;
    private ClusterObj clusterDetalle;
    private String errorMessage;
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
    
    public ArrayList<SolucionObj> getSolucionData(){
        return this.solucionData;
    }
    
    public String getErrorMessage(){
        return this.errorMessage;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos mutadores">
    public void setArchivo(Part archivo){
        this.archivo = archivo;
    }
    
    public void setNroClusterDetalle(int nroClusterDetalle){
        this.nroClusterDetalle = nroClusterDetalle;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="metodos publicos">
    
    //<editor-fold defaultstate="collapsed" desc="realizarRanking">
    public void realizarRanking(){
        try{
            // Limpiamos el mensaje de error
            errorMessage = "";
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
                            sol.addClusterData(clusterNum, genName);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect("ranking.xhtml");
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
}
