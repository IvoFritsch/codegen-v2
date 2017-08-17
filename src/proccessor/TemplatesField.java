/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.Utils;
import model.CodegenFieldConfig;

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

    public CodegenFieldConfig getConfig() {
        return config;
    }

    public TemplatesModel getModel() {
        return model;
    }

    public String getValorConfig(String config) {
        return this.config.getValorConfig(config);
    }

    public boolean temConfig(String config) {
        return this.config.temConfig(config);
    }

    public boolean temConfigIgualA(String config, String valor) {
        return this.config.temConfigIgualA(config, valor);
    }
    
    
    public String getNomePriCharMai() {
        return Utils.priCharMai(nome);
    }
    
}
