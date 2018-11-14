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
public class CodegenModelConfig {
    
    @Expose
    Map<String, String> defaults;
    @Expose
    Map<String, String> modelConfigs;
    
    public boolean temDefault(String defaultName){
        if(defaults == null) return false;
        return defaults.containsKey(defaultName);
    }
    
    public String getDefault(String defaultName){
        if(!temDefault(defaultName)) return "";
        return defaults.get(defaultName);
    }
    
    public boolean temConfig(String configName){
        if(modelConfigs == null) return false;
        return modelConfigs.containsKey(configName);
    }
    
    public String getConfig(String configName){
        if(!temConfig(configName)) return "";
        return modelConfigs.get(configName);
    }
    
}
