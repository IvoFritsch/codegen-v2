/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import model.ServerModel;

/**
 *
 * @author Ivo Fritsch
 */
public class Project {
    
    @Expose
    private String nome;
    
    @Expose
    private final List<String> models;
    
    @Expose
    private final List<String> templates;
    
    @Expose
    private String saidaGeracao;
    
    @Expose
    private String rodapeArquivosGerados;
    

    public Project() {
        this.nome = "";
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
    }
    
    public Project(String nome) {
        this.nome = nome;
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
    }

    public boolean isPseudo() {
        return nome.isEmpty();
    }
    
    public String getNome() {
        return nome;
    }

    public List<String> getModels() {
        return models;
    }
    
    public void addModel(ServerModel model){
        if(models.contains(model.getNome())) return;
        models.add(model.getNome());
        CodegenDatabaseController.gravaArquivoModelo(nome, model);
    }
    
    public static Project fromJson(String json){
        return new Gson().fromJson(json, Project.class);
    }
    
    public String toJson(){
        return new Gson().toJson(this);
    }

    public void addTemplate(String nome) {
        if(templates.contains(nome)) return;
        templates.add(nome);
        CodegenDatabaseController.criaArquivoTemplate(this.nome, nome);
    }

    void excluiTemplate(String nome) {
        if(!templates.contains(nome)) return;
        templates.remove(nome);
        CodegenDatabaseController.removeArquivoTemplate(this.nome, nome);
    }
    
}
