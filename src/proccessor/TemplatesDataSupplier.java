/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.ModifiableString;
import database.CodegenDatabaseController;
import database.Project;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrador
 */
public class TemplatesDataSupplier {

    private final TemplatesModel model;
    private final TemplatesModelsSupplier tempModSuppl;
    private final String projeto;
    private final Map<String, String> proccessConfigs;
    private ProccessLog log;

    public TemplatesDataSupplier(String projeto, TemplatesModel model, Map<String, String> proccessConfigs) {
        this.model = model;
        this.projeto = projeto;
        this.proccessConfigs = proccessConfigs;
        this.log = new ProccessLog();
        this.model.preparaEstrutura(this);
        this.tempModSuppl = new TemplatesModelsSupplier();
    }

    public TemplatesModel getModel() {
        return model;
    }

    public void cancelaGeracao(){
        throw new CancelaGeracaoException();
    }
    
    public Project getProjeto() {
        return CodegenDatabaseController.getProjetoViaNome(projeto);
    }
    
    public String getConfig(String config){
        if(!proccessConfigs.containsKey(config)) return "";
        return proccessConfigs.get(config);
    }
    
    // Processa e retorna o snippet, considerando o objeto recebido
    public String getSnippet(String nomeSnippet, Object obj){
        TemplatesProcessor snip = new TemplatesProcessor(projeto, "microSnippets/"+nomeSnippet+".snip");
        snip.setLogger(this.log);
        snip.setIsSnippet(true);
        if(!snip.pronto()){
            //ConsolePrinter.printError("Não foi possível encontrar o Snippet '"+nomeSnippet+".snip' "
            //        + "\n   ou ocorreu um erro no seu processamento"
            //        + "\nVerifique se ele existe e se sua sintaxe está OK");
        }
        snip.put("root", this);
        snip.put("obj", obj);
        return snip.proccessAndReturn();
    }
    
    public TemplatesModel getOutroModel(String nome) throws Exception{
        TemplatesModel novoModel;
            novoModel = tempModSuppl.getModeloPorNome(projeto, nome);
        if(novoModel != null) novoModel.preparaEstrutura(this);
        return novoModel;
    }
    
    public List<TemplatesModel> getListaModelos() throws Exception {
        List<TemplatesModel> listaModelos = tempModSuppl.getListaModelos(projeto);
        listaModelos.forEach(m -> m.preparaEstrutura(this));
        return listaModelos;
    }
    
    public ModifiableString modifyString(String s){
        return new ModifiableString(s);
    }

    // Logger a ser passado para o processador dos microSnippets
    public void setLogger(ProccessLog log) {
        this.log = log;
    }
}
