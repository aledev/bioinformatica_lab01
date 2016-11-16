/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biolab01.utils.similarity;

import static com.biolab01.utils.similarity.CharacterVectorUtils.*;

/**
 * Calculate the Jaccard Similarity of two strings.
 * @author Richard C (Berico Technologies)
 */
public class JaccardSimilarity implements ISimilarityCalculator{

    @Override
    public double calculate(String stringOne, String stringTwo) {
        return (double) intersect(stringOne, stringTwo).size() /
                (double) union(stringOne, stringTwo).size();
    }
    
}	
	
	
