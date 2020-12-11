/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.FilesSandBox;
import database.CodegenDatabaseController;
import java.util.ArrayList;
import java.util.List;
import main.VersaoInvalidaException;
import model.ServerModel;

/**
 *
 * @author Ivo Fritsch
 */
public class ProccessorCore {

    private final ProccessSpecs specs;
    private TemplatesDataSupplier root;
    private FilesSandBox fsb;
    private final String DIR_SAIDA;
    private final List<String> processOnceTemplates = new ArrayList<>();
    
    private static volatile boolean cancel = false;

    public ProccessorCore(ProccessSpecs specs) {
        this.specs = specs;
        this.DIR_SAIDA = CodegenDatabaseController.getProjetoViaNome(specs.getProjeto()).getCaminhoSaidaGeracao();
    }

    public ProccessLog process() {
        
        cancel = false;
        ProccessLog log = new ProccessLog();
        
        fsb = new FilesSandBox(this.DIR_SAIDA, specs.autoOverwrite(), specs.getProjeto());
        
        specs.getModelos().forEach(m -> {
            log.startNewModel(m);
            CodegenDatabaseController.ArquivoModel arquivoModelo = null;
            try {
              arquivoModelo = CodegenDatabaseController.getArquivoModelo(specs.getProjeto(), m);
              root = new TemplatesDataSupplier(specs.getProjeto(),
                  TemplatesModel.fromJson(
                              ServerModel.fromJson(arquivoModelo.json, m).toJson(true)
                  ).adicionaCamposPadraoProjeto(arquivoModelo.projeto),
                  specs.getConfig()
                  );
            } catch (VersaoInvalidaException e) {
              log.startNewTemplate("Erro de versão no arquivo do modelo");
              log.putError(e.getMessage());
              System.out.println("aasaa");
              return;
            }
            root.setLogger(log);
            processaTemplatesProjeto(specs.getProjeto(),log);
        });
        // Testar se log está OK antes de commitar
        if(!log.hasError()){
            fsb.commitaArquivos();
        }
        fsb.deleteSandbox();
        return log;
    }

    public static boolean mustCancel(){
        return cancel;
    }
    
    public static void cancel(){
        cancel = true;
    }
    
    private void processaTemplatesProjeto(String projeto, ProccessLog log){
        CodegenDatabaseController.getProjetoViaNome(projeto).getTemplates().forEach(t -> {
            if(mustCancel()) return;
            if(!specs.getTemplates().contains(t)) return;
            log.startNewTemplate(t);
            // Se esse template deve ser processado só uma vez e já foi, retorna
            if(processOnceTemplates.contains(t)) return;
            String dirSaida = t.replaceAll("\\[nomeModel\\]", root.getModel().getNome());
            dirSaida = dirSaida.replaceAll("\\[nomeProj\\]", root.getProjeto().getNome());
            
            TemplatesProcessor temp = new TemplatesProcessor(projeto, t, fsb, dirSaida);
            temp.setLogger(log);
            temp.put("root", root);
            temp.proccessToFile(this.DIR_SAIDA+dirSaida);
            if(!t.contains("[nomeModel]")) processOnceTemplates.add(t);
        });
    }
}
