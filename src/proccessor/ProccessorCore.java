/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.FilesSandBox;
import auxiliar.Utils;
import database.CodegenDatabaseController;
import java.io.File;

/**
 *
 * @author Ivo Fritsch
 */
public class ProccessorCore {

    private final ProccessSpecs specs;
    private TemplatesDataSupplier root;
    
    public ProccessorCore(ProccessSpecs specs) {
        this.specs = specs;
    }
    
    public void process(){
        specs.getModelos().forEach(m -> {
            root = new TemplatesDataSupplier(specs.getProjeto(), 
                                            TemplatesModel.fromJson(
                                                    CodegenDatabaseController.getArquivoModelo(
                                                            specs.getProjeto(), 
                                                            m)));
            processaTemplatesDiretorio(CodegenDatabaseController.getCaminhoTemplates(specs.getProjeto()));
        });
    }
    
    
    
    private void processaTemplatesDiretorio(String diretorio){
        diretorio = Utils.formalizaCaminho(diretorio);
        
        // Ignora o diretorio de snippets
        if(diretorio.endsWith("microSnippets")) return;
        String dirSaida;
        dirSaida = diretorio.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
        dirSaida = dirSaida.replaceAll("\\[nomeProj\\]", root.getProjNome());
        
        FilesSandBox.criaDiretorio(App.PASTA_SAIDA+dirSaida.replaceFirst(caminhoTemplates, ""));
        File arq = new File(diretorio);
        File[] listaArquivos = arq.listFiles();
        if (listaArquivos != null) {
          for (File filho : listaArquivos) {
            if(filho.isDirectory()){
                processaTemplatesDiretorio(filho.toString());
            } else if (filho.isFile()){
                processaTemplate(filho.toString(),App.PASTA_SAIDA+filho.toString());
            }
          }
        }
    }
    
    private void processaTemplate(String entrada, String saida){
        entrada = Utils.formalizaCaminho(entrada);
        saida = Utils.formalizaCaminho(saida);
        entrada = entrada.replaceFirst("templates/", "");
        saida = saida.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
        saida = saida.replaceAll("\\[nomeProj\\]", root.getProjNome());
        saida = saida.replaceAll(caminhoTemplates, "");
        TemplatesProcessor temp = new TemplatesProcessor(entrada);
        temp.put("root", root);
        temp.proccessToFile(saida);
        //System.out.println();
    }
}
