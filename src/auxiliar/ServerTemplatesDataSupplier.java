/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import database.CodegenDatabaseController;
import database.Project;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ServerField;
import model.ServerModel;

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
    
    public List<String> getTodasConfigsDefinidasNoProjeto(String projeto){
        List<String> listaModelosProjeto = CodegenDatabaseController.getListaModelosProjeto(projeto);
        Map<String, Integer> configsEncontradas = new HashMap<>();
        try{
        listaModelosProjeto.forEach(m -> {
            List<ServerField> listaCamposModelo = ServerModel.fromJson(CodegenDatabaseController.getArquivoModelo(projeto,m)).getListaCampos();
            listaCamposModelo.forEach(c -> {
                List<String> listaConfigsCampo = c.getConfig().getListaConfigsCampo();
                listaConfigsCampo.forEach(cfg -> {
                    int qtdOcorrencias = (Integer)Utils.defaultIfNull(configsEncontradas.get(cfg), 0);
                    configsEncontradas.put(cfg, qtdOcorrencias);
                });
            });
        });
        }catch(Exception e){
            e.printStackTrace();
        }
        List<String> retorno = new ArrayList<>(configsEncontradas.keySet());
        Collections.sort(retorno);
        return retorno;
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
