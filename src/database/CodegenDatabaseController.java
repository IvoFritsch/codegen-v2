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
        new File("codegenDB/").mkdirs();
        new File("codegenDB/").mkdirs();
        loadDb();
        List<String> projsRemover = new ArrayList<>();
        db.getProjetos().forEach(p -> {
            if(!new File(db.getCaminhoProjeto(p)).exists()){
                ConsolePrinter.printWarning("Projeto "+p+" não encontrado, sendo removido do banco de dados do Codegen...\n"
                        + "Essa mensagem é normal caso o Codegen foi transferido para outro computador");
                projsRemover.add(p);
            }
        });
        projsRemover.forEach(p -> db.removeProjeto(p));
        saveDb();
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
    
    
    private static void saveProj(Project proj) {
        System.out.println("Salvando: "+db.getCaminhoProjeto(proj.getNome()));
        File projFile = new File(db.getCaminhoProjeto(proj.getNome()));
        try {
            FileUtils.write(projFile, proj.toJson(), "UTF-8");
        } catch (Exception e){}
    }
    

    public static List<String> getListaModelosProjeto(String nome) {
        Project projeto = loadProjetoFromFile(db.getCaminhoProjeto(nome));
        if(projeto == null) return new ArrayList<>();
        return projeto.getModels();
    }
    
    public static Project getProjetoViaNome(String nome) {
        String caminho = db.getCaminhoProjeto(nome);
        if(caminho == null)
            return new Project();
            else
            return loadProjetoFromFile(db.getCaminhoProjeto(nome));
        
    }

    public static List<Project> getListaProjetos() {
        List<Project> saida = new ArrayList<>();
        db.getProjetos().forEach(p -> {
            saida.add(loadProjetoFromFile(db.getCaminhoProjeto(p)));
        });
        return saida;
    }

    private static Project loadProjetoFromFile(String caminho){
        File file = new File(caminho);
        if(!file.exists()) return null;
        try {
            return Project.fromJson(FileUtils.readFileToString(file, "UTF-8"));
        } catch (Exception e) {
        }
        return null;
    }
    
    private static void criaProjeto(String nome, String caminho) {
        new File("codegenDB/projects/" + nome + "/models").mkdirs();
        new File("codegenDB/projects/" + nome + "/templates").mkdirs();
    }
    
    public static void criaNovoProjetoNoDestino(String caminhoDestino, String nome){
        caminhoDestino = Utils.formalizaCaminho(caminhoDestino);
        if(!caminhoDestino.endsWith("/")) caminhoDestino += "/";
        System.out.println(caminhoDestino);
        String caminhoProjeto = caminhoDestino + "codegenProject";
        new File(caminhoProjeto + "/models").mkdirs();
        new File(caminhoProjeto + "/templates").mkdirs();
        salvaArquivoProjeto(caminhoProjeto + "/project.cgp", new Project(nome));
        db.addProjeto(nome, caminhoProjeto + "/project.cgp");
        saveDb();
    }
    
    public static void importaProjetoExistente(String caminhoArquivo){
        String nome = loadProjetoFromFile(caminhoArquivo).getNome();
        if (nome == null) return;
        new File(Utils.pegaPastaPaiArquivo(caminhoArquivo) + "/models").mkdirs();
        new File(Utils.pegaPastaPaiArquivo(caminhoArquivo) + "/templates").mkdirs();
        db.addProjeto(nome, caminhoArquivo);
        saveDb();
    }
    
    private static void salvaArquivoProjeto(String caminho, Project projeto){
        try{
            FileUtils.write(new File(caminho), projeto.toJson(), "UTF-8");
        } catch(Exception e){}
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
        Project proj = getProjetoViaNome(projeto);
        if(proj == null) return;
        proj.addModel(modelo);
        saveDb();
        saveProj(proj);
    }
    
    public static void gravaArquivoModelo(String projeto, ServerModel modelo) {
        
        System.out.println(Utils.pegaPastaPaiArquivo(Utils.formalizaCaminho(db.getCaminhoProjeto(projeto)))+"models/"+modelo.getNome()+".cgm");
        try {
            FileUtils.write(new File(Utils.pegaPastaPaiArquivo(Utils.formalizaCaminho(db.getCaminhoProjeto(projeto)))+"models/"+modelo.getNome()+".cgm"), new Gson().toJson(modelo), "UTF-8");
        } catch (Exception ex) {
        }
    }
    
    public static void criaArquivoTemplate(String projeto, String nome) {
        try {
            String cam = Utils.pegaPastaPaiArquivo(db.getCaminhoProjeto(projeto));
            System.out.println(cam);
            if (1 == 1) return;
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
