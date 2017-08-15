/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import model.CodegenModelConfig;

/**
 *
 * @author Ivo Fritsch
 */
public class TemplatesModel {
    
    private String nome;
    private List<TemplatesField> listaCampos = new ArrayList<>();
    
    private CodegenModelConfig config;

    public String getNome() {
        return nome;
    }

    public List<TemplatesField> getListaTodosCampos() {
        return listaCampos;
    }

    public CodegenModelConfig getConfig() {
        return config;
    }

    static TemplatesModel fromJson(String json){
        if(json == null) return null;
        return new Gson().fromJson(json, TemplatesModel.class);
    }
    
}
