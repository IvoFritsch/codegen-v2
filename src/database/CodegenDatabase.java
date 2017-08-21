/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import auxiliar.Utils;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenDatabase {
    
    private List<Project> projetos;
    
    @Expose
    private Map<String, String> projetos1;

    public CodegenDatabase() {
        projetos = new ArrayList<>();
        projetos1 = new HashMap<>();
    }

    public void addProjeto(String nome, String caminhoArquivo){
        projetos1.put(nome, caminhoArquivo);
    }
    
    public List<String> getProjetos() {
        List<String> saida = new ArrayList<>();
        projetos1.forEach((n,c) -> saida.add(n));
        return saida;
    }
    
    public String getCaminhoProjeto(String nome){
        if(!projetos1.containsKey(nome)) return "";
        return projetos1.get(nome);
    }
    
    public String getProjetoViaNome(String nome){
        return projetos1.getOrDefault(nome, null);
    }
    
    public static CodegenDatabase fromJson(String json){
        return new Gson().fromJson(json, CodegenDatabase.class);
    }
    
    public String toJson(){
        return Utils.toJsonOnlyExpose(this);
    }
}
