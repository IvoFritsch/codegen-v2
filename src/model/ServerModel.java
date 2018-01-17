/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import auxiliar.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo usado pelo microservidor para fins de edição e armazenamento
 * @author Ivo Fritsch
 */
public class ServerModel {
    
    @Expose
    private String nome;
    
    @Expose
    private List<ServerField> listaCampos = new ArrayList<>();
    
    @Expose
    private CodegenModelConfig config;

    public String getNome() {
        return nome;
    }

    public List<ServerField> getListaCampos() {
        return listaCampos;
    }
    
    public static ServerModel fromJson(String json){
        return new Gson().fromJson(json, ServerModel.class);
    }
    
    public String toJson(){
        return Utils.toJsonOnlyExpose(this);
    }
    
    
}
