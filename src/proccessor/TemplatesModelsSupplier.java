/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import database.CodegenDatabaseController;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author Administrador
 */
public class TemplatesModelsSupplier {
    private final Map<String,TemplatesModel> modelosCarregados = new HashMap<>();
    
    public TemplatesModel getModeloPorNome(String projeto, String nomeModelo){
        try{
            if(modelosCarregados.containsKey(nomeModelo)) return modelosCarregados.get(nomeModelo);
            TemplatesModel retorno = TemplatesModel.fromJson(CodegenDatabaseController.getArquivoModelo(projeto, nomeModelo));
            modelosCarregados.put(nomeModelo, retorno);
            return retorno;
        } catch (Exception e){
            modelosCarregados.put(nomeModelo, null);
            return null;
        }
    }
}
