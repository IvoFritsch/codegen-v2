/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this snippet file, choose Tools | Templates
 * and open the snippet in the editor.
 */
package database;

import auxiliar.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ServerModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

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
    private Map<String, String> generatedFilesChecksum;
    
    @Expose
    private final List<String> snippets;
    
    @Expose
    private final Map<String,String> assocTipo;
    
    @Expose
    private String saidaGeracao;
    
    @Expose
    private String rodapeArquivosGerados;
    
    @Expose
    private String projectRootDir;

    public Project() {
        this.nome = "";
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
        this.generatedFilesChecksum = new HashMap<>();
    }
    
    public Project(String nome) {
        this.nome = nome;
        this.models = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
        this.generatedFilesChecksum = new HashMap<>();
        this.projectRootDir = getRootDir()+"";
        System.out.println(getRootDir());
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

    public void setNome(String nome) {
        this.nome = nome;
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
    
    public String getFileChecksum(String file){
        return generatedFilesChecksum.get(file);
    }
    
    public String setFileChecksum(String file, String checksum){
        return generatedFilesChecksum.put(file, checksum);
    }
    
    public void addModel(ServerModel model){
        if(models.contains(model.getNome())) return;
        models.add(model.getNome());
        CodegenDatabaseController.gravaArquivoModelo(nome, model);
    }
    
    public void deleteModel(String model){
        if (!models.remove(model)) return;
        CodegenDatabaseController.deleteModelFile(nome, model);
    }
    
    public static Project fromJson(String json){
        Project retorno = new Gson().fromJson(json, Project.class);
        retorno.projectRootDir = retorno.getRootDir();
        if(retorno.generatedFilesChecksum == null) retorno.generatedFilesChecksum = new HashMap<>();
        return retorno;
    }
    
    public String toJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
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
    
    void updateModelsFromDir(String dir){
        new File(dir+"models").mkdirs();
        FileUtils.listFiles(new File(dir+"models"), 
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).forEach(f -> {
                    if(!models.contains(Utils.pegaNomeArquivo(Utils.formalizaCaminho(f.toString()))))
                        models.add(Utils.pegaNomeArquivo(Utils.formalizaCaminho(f.toString())));
                });
        List<String> remover = new ArrayList<>();
        models.forEach(m -> {
            if(!new File(dir+"models/"+m+".cgm").exists())
                remover.add(m);
        });
        remover.forEach(r -> models.remove(r));
    }
    
    void updateTemplatesFromDir(String dir){
        FileUtils.listFiles(new File(dir+"templates"), 
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).forEach(f -> {
                    String template = Utils.formalizaCaminho(f.toString()).replace(dir+"templates/", "");
                    if(!templates.contains(template))
                        templates.add(template);
                });
        List<String> remover = new ArrayList<>();
        templates.forEach(t -> {
            if(t.startsWith("microSnippets") || !new File(dir+"templates/"+t).exists() || new File(dir+"templates/"+t).isDirectory())
                remover.add(t);
        });
        remover.forEach(r -> templates.remove(r));
        
    }
    
    void updateSnippetsFromDir(String dir){
        new File(dir+"templates/microSnippets").mkdirs();
        FileUtils.listFiles(new File(dir+"templates/microSnippets"), 
                TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE).forEach(f -> {
                    String snippet = Utils.formalizaCaminho(f.toString()).replace(dir+"templates/microSnippets/", "");
                    if(!snippets.contains(snippet))
                        snippets.add(snippet);
                });
        List<String> remover = new ArrayList<>();
        snippets.forEach(t -> {
            if(!new File(dir+"templates/microSnippets/"+t).exists() || new File(dir+"templates/microSnippets/"+t).isDirectory())
                remover.add(t);
        });
        remover.forEach(r -> snippets.remove(r));
        
    }
    
    public void save(){
        CodegenDatabaseController.saveProj(this);
    }
    
    
}
