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
import java.util.List;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenDatabase {
    
    @Expose
    private List<Project> projetos;

    public CodegenDatabase() {
        projetos = new ArrayList<>();
    }

    public void addProjeto(Project projeto){
        projetos.add(projeto);
    }
    
    public List<Project> getProjetos() {
        return projetos;
    }
    
    public Project getProjetoViaNome(String nome){
        return projetos.stream().filter(p -> p.getNome().equals(nome)).findFirst().orElse(null);
    }
    
    public static CodegenDatabase fromJson(String json){
        return new Gson().fromJson(json, CodegenDatabase.class);
    }
    
    public String toJson(){
        return Utils.toJsonOnlyExpose(this);
    }
}
