/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.ModifiableString;
import auxiliar.ConsolePrinter;
import database.CodegenDatabaseController;

/**
 *
 * @author Administrador
 */
public class TemplatesDataSupplier {

    private final TemplatesModel model;
    private final String projeto;

    public TemplatesDataSupplier(String projeto, TemplatesModel model) {
        this.model = model;
        this.projeto = projeto;
    }

    public TemplatesModel getModel() {
        return model;
    }

    public String getProjeto() {
        return projeto;
    }
    
    // Processa e retorna o snippet, considerando o objeto recebido
    public String getSnippet(String nomeSnippet, Object obj){
        TemplatesProcessor snip = new TemplatesProcessor(projeto, CodegenDatabaseController.getCaminhoTemplates(projeto)+"microSnippets/"+nomeSnippet+".snip");
        if(!snip.pronto()){
            ConsolePrinter.printError("Não foi possível encontrar o Snippet '"+nomeSnippet+".snip' "
                    + "\n   ou ocorreu um erro no seu processamento"
                    + "\nVerifique se ele existe e se sua sintaxe está OK");
        }
        snip.put("root", this);
        snip.put("obj", obj);
        return snip.proccessAndReturn();
    }
    
    public TemplatesModel getOutroModel(String nome) throws Exception{
        TemplatesModel novoModel;
            novoModel = TemplatesModelsSupplier.getModeloPorNome(projeto, nome);
        if(novoModel == null){
            ConsolePrinter.printError("Algum template solicitou um modelo('"+nome+".java') que não foi possível parsear\n"
                    + "verifique se o arquivo existe e se está compilando corretamente\n"
                    + "Possivelmente acontecerá um erro a seguir...");
        }
        return novoModel;
    }
    
    public ModifiableString modifyString(String s){
        return new ModifiableString(s);
    }
}
