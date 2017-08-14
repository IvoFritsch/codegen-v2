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
 * @author Administrador
 */
public class TemplateSpecs {
    
    @Expose
    private String projeto;
    
    @Expose
    private String nome;
    
    @Expose
    private String nomeAntigo;

    public String getNome() {
        return nome;
    }

    public String getProjeto() {
        return projeto;
    }

    public String getNomeAntigo() {
        return nomeAntigo;
    }
    
    public static TemplateSpecs fromJson(String json){
        return new Gson().fromJson(json, TemplateSpecs.class);
    }
}
