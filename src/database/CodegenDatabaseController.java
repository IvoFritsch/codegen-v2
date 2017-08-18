/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import auxiliar.ConsolePrinter;
import auxiliar.Utils;
import com.google.gson.Gson;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import model.ServerModel;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenDatabaseController {

    private static CodegenDatabase db;
    private static final String DB_FILE = "codegenDB/database.cgd";
    
    public static void init() throws Exception{
        ConsolePrinter.printInfo("Inicializando CodegenDatabase...");
        new File("codegenDB/projects/").mkdirs();
        new File("codegenDB/projects/").mkdirs();
        loadDb();
        
        try {
            FileUtils.write(new File("codegenDB/NUNCA EDITAR ESSE DIRETÓRIO MANUALMENTE.txt"), 
                    "Sempre utilizar o Microservidor do Codegen para fazer edições", "UTF-8");
        } catch (Exception e){}
    }
    
    private static void loadDb() throws Exception{
        File db = new File(DB_FILE);
        if(!db.exists()){
            CodegenDatabaseController.db = new CodegenDatabase();
            saveDb();
            return;
        }
        CodegenDatabaseController.db = CodegenDatabase.fromJson(FileUtils.readFileToString(db, "UTF-8"));
    }
    
    private static void saveDb() {
        File db = new File(DB_FILE);
        try {
            FileUtils.write(db, CodegenDatabaseController.db.toJson(), "UTF-8");
        } catch (Exception e){}
    }

    public static List<String> getListaModelosProjeto(String nome) {
        Project projeto = db.getProjetoViaNome(nome);
        if(projeto == null) return new ArrayList<>();
        return projeto.getModels();
    }
    
    public static Project getProjetoViaNome(String nome) {
        return db.getProjetoViaNome(nome);
        
    }

    public static List<Project> getListaProjetos() {
        return db.getProjetos();
    }

    private static void criaProjeto(String nome) {
        new File("codegenDB/projects/" + nome + "/models").mkdirs();
        new File("codegenDB/projects/" + nome + "/templates").mkdirs();
    }
    
    
    public static void adicionaProjeto(Project projeto) {
        if(db.getProjetoViaNome(projeto.getNome()) != null) return;
        
        db.addProjeto(projeto);
        criaProjeto(projeto.getNome());
        saveDb();
    }

    public static void removeFileAndParentsIfEmpty(Path path, String basePath)
        throws IOException {
    if(path == null || path.endsWith(basePath)) return;

    if (Files.isRegularFile(path)) {
        Files.deleteIfExists(path);
    } else if(Files.isDirectory(path)) {
        try {
            Files.delete(path);
        } catch(DirectoryNotEmptyException e) {
            return;
        }
    }
    removeFileAndParentsIfEmpty(path.getParent(), basePath);
}
    
    
    public static String getArquivoModelo(String projeto, String modelo) {
        File file = new File("codegenDB/projects/" + projeto + "/models/" + modelo+".cgm");
        if(!file.exists()) return null;
        try {
            return FileUtils.readFileToString(file, "UTF-8");
        } catch (Exception e) {
        }
        return null;
    }
    
    public static void addModel(String projeto, ServerModel modelo){
        Project proj = db.getProjetoViaNome(projeto);
        if(proj == null) return;
        proj.addModel(modelo);
        saveDb();
    }
    
    public static void gravaArquivoModelo(String projeto, ServerModel modelo) {
        try {
            FileUtils.write(new File("codegenDB/projects/" + projeto + "/models/"+modelo.getNome()+".cgm"), new Gson().toJson(modelo), "UTF-8");
        } catch (Exception ex) {
        }
    }
    
    public static void criaArquivoTemplate(String projeto, String nome) {
        try {
            String caminho = "codegenDB/projects/" + projeto + "/templates/"+nome;
            caminho = Utils.formalizaCaminho(caminho);
            FileUtils.write(new File(caminho), "", "UTF-8");
        } catch (Exception ex) {
        }
    }
    
    public static void criaArquivoSnippet(String projeto, String nome) {
        try {
            String caminho = "codegenDB/projects/" + projeto + "/templates/microSnippets/"+nome+".snip";
            caminho = Utils.formalizaCaminho(caminho);
            FileUtils.write(new File(caminho), "", "UTF-8");
        } catch (Exception ex) {
        }
    }
    
    public static void removeArquivoTemplate(String projeto, String nome) {
        String caminho = "codegenDB/projects/" + projeto + "/templates/"+nome;
        try {
            caminho = Utils.formalizaCaminho(caminho);
            FileUtils.deleteQuietly(new File(caminho));
        } catch (Exception ex) {
        }
        try{
        removeFileAndParentsIfEmpty(new File(Utils.pegaPastaPaiArquivo(caminho)).toPath(), "codegenDB/projects/" + projeto + "/templates/");
        } catch(Exception e){}
    }

    public static void removeArquivoSnippet(String projeto, String nome) {
        String caminho = "codegenDB/projects/" + projeto + "/templates/microSnippets/"+nome+".snip";
        try {
            caminho = Utils.formalizaCaminho(caminho);
            FileUtils.deleteQuietly(new File(caminho));
        } catch (Exception ex) {
        }
        try{
        removeFileAndParentsIfEmpty(new File(Utils.pegaPastaPaiArquivo(caminho)).toPath(), "codegenDB/projects/" + projeto + "/templates/microSnippets/");
        } catch(Exception e){}
    }
    
    public static void newTemplate(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).addTemplate(specs.getNome());
        saveDb();
    }
    public static void newSnippet(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).addSnippet(specs.getNome());
        saveDb();
    }

    public static void openTemplate(TemplateSpecs specs) {
        Desktop dt = Desktop.getDesktop();
        try {
            String caminho = "codegenDB/projects/" + specs.getProjeto() + "/templates/"+specs.getNome();
            caminho = Utils.formalizaCaminho(caminho);
            dt.open(new File(caminho));
        } catch (Exception ex) {}
    }

    public static void openSnippet(TemplateSpecs specs) {
        Desktop dt = Desktop.getDesktop();
        try {
            String caminho = "codegenDB/projects/" + specs.getProjeto() + "/templates/microSnippets/"+specs.getNome()+".snip";
            caminho = Utils.formalizaCaminho(caminho);
            dt.open(new File(caminho));
        } catch (Exception ex) {}
    }

    public static void excluiTemplate(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).excluiTemplate(specs.getNome());
        saveDb();
    }
    public static void excluiSnippet(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).excluiSnippet(specs.getNome());
        saveDb();
    }
    
    public static String getCaminhoTemplates(String projeto){
        return "codegenDB/projects/" + projeto + "/templates/";
    }
}
