/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import database.CodegenDatabaseController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ServerModel;


/**
 *
 * @author Administrador
 */
public class TemplatesModelsSupplier {
    private final Map<String,TemplatesModel> modelosCarregados = new HashMap<>();
    
    public TemplatesModel getModeloPorNome(String projeto, String nomeModelo){
        try{
            if(modelosCarregados.containsKey(nomeModelo)) return modelosCarregados.get(nomeModelo);
            TemplatesModel retorno = TemplatesModel.fromJson(ServerModel.fromJson(CodegenDatabaseController.getArquivoModelo(projeto, nomeModelo).json, nomeModelo).toJson(true));
            modelosCarregados.put(nomeModelo, retorno);
            return retorno;
        } catch (Exception e){
            modelosCarregados.put(nomeModelo, null);
            return null;
        }
    }
    
    public List<TemplatesModel> getListaModelos(String projeto){
        List<TemplatesModel> retorno = new ArrayList<>();
        try{
            CodegenDatabaseController.getListaModelosProjeto(projeto).forEach(nome -> retorno.add(getModeloPorNome(projeto, nome)));
            return retorno;
        } catch (Exception e){
            return null;
        }
    }
}
