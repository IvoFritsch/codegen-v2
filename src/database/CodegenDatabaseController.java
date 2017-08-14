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

    public static String getArquivoModelo(String projeto, String modelo) {
        try {
            return FileUtils.readFileToString(new File("codegenDB/projects/" + projeto + "/models/" + modelo+".cgm"), "UTF-8");
        } catch (Exception e) {
        }
        return "";
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
    public static void removeArquivoTemplate(String projeto, String nome) {
        try {
            String caminho = "codegenDB/projects/" + projeto + "/templates/"+nome;
            caminho = Utils.formalizaCaminho(caminho);
            FileUtils.deleteQuietly(new File(caminho));
        } catch (Exception ex) {
        }
    }

    public static void newTemplate(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).addTemplate(specs.getNome());
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

    public static void excluiTemplate(TemplateSpecs specs) {
        getProjetoViaNome(specs.getProjeto()).excluiTemplate(specs.getNome());
        saveDb();
    }
}
