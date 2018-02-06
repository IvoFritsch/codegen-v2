/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenFieldConfig {
    
    @Expose
    private Map<String,CodegenFieldConfigEndpoint> conf;
    
    private TemplatesField campo;
    
    public String getValorConfig(String config){
        if(!temConfig(config)) return "";
        CodegenFieldConfigEndpoint endpoint = conf.get(config);
        if(endpoint == null) return campo.getModel().getConfig().getDefault(config);
        return endpoint.getValor();
    }
    
    public String get(String config){
        return getValorConfig(config);
    }
    
    public CodegenFieldConfigEndpoint getConfigEndpoint(String config){
        if(!temConfig(config)) return null;
        return conf.get(config);
    }
    
    public boolean temConfig(String config){
        if(campo.getModel().getConfig().temDefault(config)) return true;
        if(conf == null) return false;
        return conf.containsKey(config);
    }
    
    public boolean temConfigIgualA(String config, String valor) {
        if(conf == null) return false;
        if(!temConfig(config)) return false;
        return getValorConfig(config).equals(valor);
    }
    
    public List<String> getSubconfigsDaConfig(String config){
        if(!temConfig(config)) return new ArrayList<>();
        return conf.get(config).getSubconfigs();
    }
    
        public boolean configTemSubconfig(String config, String subconfig){
        if(!temConfig(config)) return false;
        return conf.get(config).temSubconfig(subconfig);
    }
    
    public String getValorSubconfigDaConfig(String config, String subconfig){
        if(!temConfig(config)) return "";
        return conf.get(config).getValorSubConfig(subconfig);
    }
    
    public boolean subconfigDaConfigTemValorIgualA(String config, String subconfig, String valor){
        if(!temConfig(config)) return false;
        return conf.get(config).subconfigTemValorIgualA(subconfig, valor);
    }
    
    public List<String> getListaConfigsCampo(){
        List<String> saida = new ArrayList<>();
        if(conf == null) return saida;
        conf.forEach((c,v)->saida.add(c));
        return saida;
    }

    public TemplatesField getCampo() {
        return campo;
    }
    
    void preparaEstrutura(TemplatesField campo){
        this.campo = campo;
        if(conf != null) 
            conf.forEach((c,ce) -> ce.preparaEstrutura(this));
    }
    
}
