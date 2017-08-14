/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import database.CodegenDatabaseController;
import database.Project;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class ServerTemplatesDataSupplier {

    private final Project projeto;

    public ServerTemplatesDataSupplier(Project proj) {
        
        if(proj == null){
            proj = new Project();
        }
        this.projeto = proj;
    }

    public Project getProjeto() {
        return projeto;
    }

    public List<Project> getListaProjetos() {
        return CodegenDatabaseController.getListaProjetos();
    }

    // Processa e retorna o snippet, considerando o objeto recebido
    public String getSnippet(String nomeSnippet, Object obj){
        ServerTemplatesProcessor snip = new ServerTemplatesProcessor("microSnippets/"+nomeSnippet+".snip");
        if(!snip.pronto()){
            ConsolePrinter.printError("Não foi possível encontrar o Snippet '"+nomeSnippet+".snip' "
                    + "\n   ou ocorreu um erro no seu processamento"
                    + "\nVerifique se ele existe e se sua sintaxe está OK");
        }
        snip.put("root", this);
        snip.put("obj", obj);
        return snip.proccessAndReturn();
    }
    
    public ModifiableString modifyString(String s){
        return new ModifiableString(s);
    }
}
