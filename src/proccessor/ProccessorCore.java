/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.FilesSandBox;
import auxiliar.Utils;
import com.google.gson.Gson;
import database.CodegenDatabaseController;
import java.io.File;

/**
 *
 * @author Ivo Fritsch
 */
public class ProccessorCore {

    private final ProccessSpecs specs;
    private TemplatesDataSupplier root;
    private FilesSandBox fsb;
    

    public ProccessorCore(ProccessSpecs specs) {
        this.specs = specs;
    }

    public ProccessLog process() {
        
        ProccessLog log = new ProccessLog();
        
        
        fsb = new FilesSandBox("saida_codegen/");
        
        specs.getModelos().forEach(m -> {
            log.startNewModel(m);
            root = new TemplatesDataSupplier(specs.getProjeto(),
                    TemplatesModel.fromJson(
                            CodegenDatabaseController.getArquivoModelo(
                                    specs.getProjeto(),
                                    m)),
                                    specs.getConfig()
                    );
            processaTemplatesProjeto(specs.getProjeto(),log);
        });
        fsb.commitaArquivos();
        return log;
    }

    private void processaTemplatesProjeto(String projeto, ProccessLog log){
        CodegenDatabaseController.getProjetoViaNome(projeto).getTemplates().forEach(t -> {
            log.startNewTemplate(t);
            
            String dirSaida = t.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
            dirSaida = dirSaida.replaceAll("\\[nomeProj\\]", root.getProjeto());
            
            TemplatesProcessor temp = new TemplatesProcessor(projeto,t, fsb);
            temp.setLogger(log);
            temp.put("root", root);
            temp.proccessToFile("saida_codegen/"+dirSaida);
        });
    }
    
    private void processaTemplatesDiretorio(String diretorio) {
        diretorio = Utils.formalizaCaminho(diretorio);
        System.out.println("Processando diretorio "+diretorio);

        // Ignora o diretorio de snippets
        if (diretorio.endsWith("microSnippets")) {
            return;
        }
        String dirSaida;
        dirSaida = diretorio.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
        dirSaida = dirSaida.replaceAll("\\[nomeProj\\]", root.getProjeto());

        fsb.criaDiretorio("saida_codegen/" + dirSaida.replaceFirst(CodegenDatabaseController.getCaminhoTemplates(specs.getProjeto()), ""));
        File arq = new File(diretorio);
        File[] listaArquivos = arq.listFiles();
        if (listaArquivos != null) {
            for (File filho : listaArquivos) {
                if (filho.isDirectory()) {
                    processaTemplatesDiretorio(filho.toString());
                } else if (filho.isFile()) {
                    processaTemplate(filho.toString(), "saida_codegen/" + filho.toString());
                }
            }
        }
    }

    private void processaTemplate(String entrada, String saida) {
        entrada = Utils.formalizaCaminho(entrada);
        saida = Utils.formalizaCaminho(saida);
        System.out.println(entrada+ "  "+ saida);
        
        entrada = entrada.replaceFirst("templates/", "");
        saida = saida.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
        saida = saida.replaceAll("\\[nomeProj\\]", root.getProjeto());
        saida = saida.replaceAll(CodegenDatabaseController.getCaminhoTemplates(specs.getProjeto()), "");
        TemplatesProcessor temp = new TemplatesProcessor(specs.getProjeto(), entrada, fsb);
        temp.init();
        temp.put("root", root);
        temp.proccessToFile(saida);
        //System.out.println();
        
    }
}
