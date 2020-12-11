/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.Utils;
import com.google.gson.Gson;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ivo Fritsch
 */
public class TemplatesField {
    
    private String nome;
    //private final TipoCampo tipo;
    private String tipoAsString;
    private CodegenFieldConfig config;
    private TemplatesModel model;

    public String getNome() {
        return nome;
    }

    public String getTipoAsString() {
        return tipoAsString;
    }

    public String getTipo(){
        Map<String,String> assocTipo = model.getRoot().getProjeto().getAssocTipo();
        if(!assocTipo.containsKey(tipoAsString)) {
            try {
                if(model.getRoot().getOutroModel(tipoAsString) != null){
                    return "OUTRO_MODEL";
                }
            } catch(Exception e){}
            return "DESCONHECIDO";
        }
        return assocTipo.get(tipoAsString);
    }
    
    public CodegenFieldConfig getConfig() {
        return config;
    }

    public CodegenFieldConfigEndpoint getConfigEndpoint(String config) {
        return this.config.getConfigEndpoint(config);
    }

    public TemplatesModel getModel() {
        return model;
    }

    public List<String> getListaConfigsCampo() {
        return config.getListaConfigsCampo();
    }
    
    public String getValorConfig(String config) {
        return this.config.getValorConfig(config);
    }
    
    public String getValorConfigOuDefault(String config, String def) {
        return this.config.temConfig(config) ? this.config.getValorConfig(config) : def;
    }
    
    public List<String> getValorConfigIncluiSubconfigs(String config) {
        return this.config.getValorConfigIncluiSubconfigs(config);
    }

    public boolean temConfig(String config) {
        return this.config.temConfig(config);
    }

    public boolean temAConfigIgualA(String config, String valor) {
        return this.config.temConfigIgualA(config, valor);
    }
    
    public boolean temAConfigIgualAIncluiSubconfigs(String config, String valor) {
        return this.config.temConfigIgualAIncluiSubconfigs(config, valor);
    }

    public boolean configTemSubconfig(String config, String subconfig) {
        return this.config.configTemSubconfig(config, subconfig);
    }
    
    public List<String> getSubconfigsDaConfig(String config) {
        return this.config.getSubconfigsDaConfig(config);
    }

    public String getValorSubconfigDaConfig(String config, String subconfig) {
        return this.config.getValorSubconfigDaConfig(config, subconfig);
    }

    public boolean subconfigDaConfigTemValorIgualA(String config, String subconfig, String valor) {
        return this.config.subconfigDaConfigTemValorIgualA(config, subconfig, valor);
    }
    
    public String getNomePriCharMai() {
        return Utils.priCharMai(nome);
    }
    
    void preparaEstrutura(TemplatesModel model){
        this.model = model;
        config.preparaEstrutura(this);
    }
    
    static TemplatesField fromJson(String json){
        if(json == null) return null;
        TemplatesField retorno = new Gson().fromJson(json, TemplatesField.class);
        return retorno;
    }
    
}
