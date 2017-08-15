/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.annotations.Expose;
import java.util.Map;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenFieldConfig {
    
    @Expose
    private Map<String,String> conf;
    
    public String getValorConfig(String config){
        if(!temConfig(config)) return null;
        try{
        return conf.get(config);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    public String get(String config){
        return getValorConfig(config);
    }
    
    public boolean temConfig(String config){
        if(conf == null) return false;
        return conf.containsKey(config);
    }
    
    public boolean temConfigIgualA(String config, String valor) {
        if(conf == null) return false;
        if(!temConfig(config)) return false;
        return getValorConfig(config).equals(valor);
    }
}
