/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ServerModel;

/**
 *
 * @author Ivo Fritsch
 */
public class Project {
    
    @Expose
    private final String nome;
    
    @Expose
    private final List<String> models;
    
    @Expose
    private final List<String> templates;
    
    @Expose
    private final List<String> snippets;
    
    @Expose
    private final Map<String,String> assocTipo;
    
    @Expose
    private String saidaGeracao;
    
    @Expose
    private String rodapeArquivosGerados;
    

    public Project() {
        this.nome = "";
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
    }
    
    public Project(String nome) {
        this.nome = nome;
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
    }

    public String getCaminhoSaidaGeracao(){
        String saida = getRootDir()+"";
        return saida;
    }
    
    public boolean isPseudo() {
        return nome.isEmpty();
    }
    
    public String getNome() {
        return nome;
    }

    public Map<String, String> getAssocTipo() {
        return assocTipo;
    }
    
    public String getRootDir() {
        return CodegenDatabaseController.getRootProjeto(this.nome);
    }


    public List<String> getModels() {
        return models;
    }

    public List<String> getTemplates() {
        return templates;
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
    
    public void addSnippet(String nome) {
        if(snippets.contains(nome)) return;
        snippets.add(nome);
        CodegenDatabaseController.criaArquivoSnippet(this.nome, nome);
    }

    void excluiTemplate(String nome) {
        if(!templates.contains(nome)) return;
        templates.remove(nome);
        CodegenDatabaseController.removeArquivoTemplate(this.nome, nome);
    }
     
    void excluiSnippet(String nome) {
        if(!snippets.contains(nome)) return;
        snippets.remove(nome);
        CodegenDatabaseController.removeArquivoSnippet(this.nome, nome);
    }
}
