/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

/**
 *
 * @author ivoaf
 */
public class ProjectSpecs {
    
    @Expose
    private String nome;
    
    @Expose
    private String caminho;

    public String getNome() {
        return nome;
    }

    public String getCaminho() {
        return caminho;
    }
    
    public static ProjectSpecs fromJson(String json){
        return new Gson().fromJson(json, ProjectSpecs.class);
    }
}
