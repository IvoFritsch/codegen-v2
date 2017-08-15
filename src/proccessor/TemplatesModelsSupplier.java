/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.ConsolePrinter;
import database.CodegenDatabase;
import database.CodegenDatabaseController;


/**
 *
 * @author Administrador
 */
public class TemplatesModelsSupplier {
    private static String caminhoModelos;

    public static void setCaminhoModelos(String caminhoModelos) {
        ConsolePrinter.printInfo("Vai procurar os modelos na pasta " + (caminhoModelos.isEmpty() ? "base do Codegen" : caminhoModelos));
        TemplatesModelsSupplier.caminhoModelos = caminhoModelos;
    }
    
    public static TemplatesModel getModeloPorNome(String projeto, String nomeModelo){
        try{
            return TemplatesModel.fromJson(CodegenDatabaseController.getArquivoModelo(projeto, nomeModelo));
        } catch (Exception e){
            return null;
        }
    }
}
