/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrador
 */
public class ProccessSpecs {
    
    @Expose
    private String projeto;
    
    @Expose
    private List<String> modelos;
    
    @Expose
    private List<String> templates;
    
    @Expose
    private Map<String,String> config;

    public String getProjeto() {
        return projeto;
    }

    public List<String> getModelos() {
        return modelos;
    }

    public List<String> getTemplates() {
        return templates;
    }

    public Map<String, String> getConfig() {
        return config;
    }
    
    public static ProccessSpecs fromJson(String json){
        return new Gson().fromJson(json, ProccessSpecs.class);
    }
}
