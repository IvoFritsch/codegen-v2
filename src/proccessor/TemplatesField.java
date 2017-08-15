/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

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
    private String codegenConfigAsString;
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

    public String getCodegenConfigAsString() {
        return codegenConfigAsString;
    }

    public TemplatesModel getModel() {
        return model;
    }
    
}
