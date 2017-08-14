/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;


/**
 *
 * @author Administrador
 */
public class ModelsSupplier {
    private static String caminhoModelos;

    public static void setCaminhoModelos(String caminhoModelos) {
        ConsolePrinter.printInfo("Vai procurar os modelos na pasta " + (caminhoModelos.isEmpty() ? "base do Codegen" : caminhoModelos));
        ModelsSupplier.caminhoModelos = caminhoModelos;
    }
    
    public static TemplatesModel getModeloPorNome(String nomeModelo){
        try{
            return new TemplatesModel(caminhoModelos+nomeModelo+".java");
        } catch (Exception e){
            return null;
        }
    }
}
