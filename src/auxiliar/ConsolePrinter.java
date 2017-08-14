/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

/**
 *
 * @author ivoaf
 */
public class ConsolePrinter {
    
    private static int qtdErros = 0;
    
    public static void printMessage(String message){
        flush();
          System.out.printf("                        "+message+"\n");
    }
    
    public static void printInfo(String message){
        flush();
        System.out.printf("[Info        ]:         "+message.replaceAll("\n", "\n                        ")+"\n");
    }
    
    public static void printError(String message){
        qtdErros++;
        flush();
        System.err.printf("[Erro        ]:         "+message.replaceAll("\n", "\n                        ")+"\n");
    }
        
    public static void printWarning(String message){
        flush();
        System.err.printf("[!!!Alerta!!!]:         "+message.replaceAll("\n", "\n                        ")+"\n");
    }
    
    private static void flush(){
        System.out.flush();
        System.err.flush();
    }

    public static int getQtdErros() {
        return qtdErros;
    }
    
    
     
}
