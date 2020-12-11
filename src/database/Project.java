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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import main.VersaoInvalidaException;
import model.ServerField;
import model.ServerModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 *
 * @author Ivo Fritsch
 */
public final class Project {
  
    private final static int VERSAO_ATUAL = 1;
    
    @Expose
    private String nome;
    
    @Expose
    private Map<String, String> generatedFilesChecksum;
    
    @Expose
    private final Map<String,String> assocTipo;
    
    @Expose
    private String caminhoSaidaGeracao;
    
    @Expose
    private List<ServerField> camposPadrao = new ArrayList<>();
    
    @Expose
    private int versao;
    
    private final List<String> templates;
    
    class ModelLista {
        boolean importado;
        String importadoDe;
        String nome;

        public ModelLista(String nome, String importadoDe) {
            this.importado = true;
            this.importadoDe = importadoDe;
            this.nome = nome;
        }

        public ModelLista(String nome) {
            this.nome = nome;
            importado = false;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 13 * hash + Objects.hashCode(this.nome);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ModelLista other = (ModelLista) obj;
            return Objects.equals(this.nome, other.nome);
        }
    }
    private final List<ModelLista> allModels;
    
    private final List<String> snippets;

    public List<ServerField> getCamposPadrao() {
      return camposPadrao;
    }

    public Project() {
        this.nome = "";
        this.allModels = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
        this.generatedFilesChecksum = new HashMap<>();
        this.versao = VERSAO_ATUAL;
    }
    
    public Project(String nome) {
        this.nome = nome;
        this.allModels = new ArrayList<>();
        this.templates = new ArrayList<>();
        this.snippets = new ArrayList<>();
        this.assocTipo = new HashMap<>();
        this.generatedFilesChecksum = new HashMap<>();
        this.versao = VERSAO_ATUAL;
    }

    public String getCaminhoSaidaGeracao(){
        String concat = caminhoSaidaGeracao;
        if(concat == null) concat = "";
        String saida = getRootDir() + concat;
        return saida;
    }

    public String getCaminhoSaidaGeracaoRelativo(){
        return caminhoSaidaGeracao != null ? caminhoSaidaGeracao : "";
    }
    
    public void setCaminhoSaidaGeracao(String caminhoSaidaGeracao) {
        if(caminhoSaidaGeracao == null || caminhoSaidaGeracao.isEmpty()) {
            this.caminhoSaidaGeracao = "";
            return;
        }
        caminhoSaidaGeracao = caminhoSaidaGeracao.replace('\\', '/');
        if(caminhoSaidaGeracao.startsWith("/")) {
           caminhoSaidaGeracao = caminhoSaidaGeracao.replaceFirst("/", "");
        }
        if(caminhoSaidaGeracao.isEmpty()) {
            this.caminhoSaidaGeracao = "";
            return;
        }
        if(!caminhoSaidaGeracao.endsWith("/")){
           caminhoSaidaGeracao += "/";
        }
        this.caminhoSaidaGeracao = caminhoSaidaGeracao;
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

    public boolean modeloPertenceAEsseProjeto(String model){
        ModelLista r = allModels.stream().filter(m -> m.equals(new ModelLista(model))).findFirst().orElse(null);
        return r != null && !r.importado;
    }
    public String modeloImportadoDoProjeto(String model){
        ModelLista r = allModels.stream().filter(m -> m.equals(new ModelLista(model))).findFirst().orElse(null);
        return r.importadoDe;
    }
    
    public List<String> getModels() {
        return allModels.stream().map(m -> m.nome).collect(Collectors.toList());
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
        if(allModels.contains(new ModelLista(model.getNome()))) return;
        allModels.add(new ModelLista(model.getNome()));
        CodegenDatabaseController.gravaArquivoModelo(nome, model);
    }
    
    public void deleteModel(String model){
        if (!allModels.remove(new ModelLista(model))) return;
        CodegenDatabaseController.deleteModelFile(nome, model);
    }
    
    public static Project fromJson(String json){
        try{
            Project retorno = new Gson().fromJson(json, Project.class);
            if(retorno.generatedFilesChecksum == null) retorno.generatedFilesChecksum = new HashMap<>();
            if(retorno.versao > VERSAO_ATUAL){
              throw new VersaoInvalidaException("projeto(project.cgp)", VERSAO_ATUAL, retorno.versao);
            }
            retorno.versao = VERSAO_ATUAL;
            return retorno;
        } catch(VersaoInvalidaException e) {
          throw e;
        } catch(Exception e){
            System.out.println(e.getMessage() + json);
            throw e;
        }
    }
    
    public String toJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
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
                    if(".import".equals(f.getName())) return;
                    ModelLista inserir = new ModelLista(Utils.pegaNomeArquivo(Utils.formalizaCaminho(f.toString())));
                    if(!allModels.contains(inserir)){
                        allModels.add(inserir);
                    }
                });
        List<ModelLista> remover = new ArrayList<>();
        allModels.forEach(m -> {
            if(!new File(dir+"models/"+m.nome+".cgm").exists())
                remover.add(m);
        });
        remover.forEach(r -> allModels.remove(r));
        File imports = new File(dir+"models/.import");
        if(imports.exists()){
            try {
                FileUtils.readLines(imports, "UTF-8").forEach(p -> {
                    CodegenDatabaseController.getListaModelosProjeto(p).forEach(mi -> {
                        if(!allModels.contains(new ModelLista(mi))){
                            allModels.add(new ModelLista(mi, p));
                        }
                    });
                });
            } catch (IOException ex) {
            }
        }
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
