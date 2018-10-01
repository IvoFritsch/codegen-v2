/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author Pedrivo
 */
public class CliStarter {
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Inicia/Para o servidor do Haftware Codegen v2.\n"
                    + "    Uso:\n"
                    + "    start               -  Inicia o Codegen.\n"
                    + "    stop                -  Para o Codegen.\n"
                    + "    status              -  Mostra se o Codegen está rodando.");
            return;
        }
        
        if(args.length > 0){
            switch(args[0]){
                case "start":
                    startCodegen();
                    return;
                case "stop":
                    sendStop();
                    return;
                case "status":
                    if(codegenAvailabilityCheck())
                        System.out.println("O Codegen ESTÁ RODANDO.");
                    else
                        System.out.println("O Codegen NÃO ESTÁ RODANDO.");
                    return;
            }
        }
    }
    
    private static void startCodegen(){
        if(codegenAvailabilityCheck()){
            System.err.println("O Codegen já está rodando.");
            return;
        }
        String jarPath = null;
        try {
            jarPath = new File(CliStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException ex) {
            System.err.println("Não foi possível iniciar o Codegen, arquivo .jar não foi encontrado");
            return;
        }
        try {
            System.out.println("Iniciando o Codegen...");
            ProcessBuilder codegenBuilder = new ProcessBuilder("java", "-Dfile.encoding=\"UTF-8\"", "-cp", jarPath, "main.CodegenServer");
            codegenBuilder.directory(new File(jarPath).getParentFile());
            
                    
            Process manager = codegenBuilder.start();
            int timeout = 0;
            while(timeout < 15){
                Thread.sleep(333);
                if(codegenAvailabilityCheck()){
                    System.out.println("Codegen iniciou com sucesso.");
                    break;
                }
                timeout++;
            }
            if(timeout >= 15){
                if(manager.isAlive()){
                    System.err.println("O Codegen ainda não terminou de inicializar, mas ainda está tentando.");
                } else {
                    System.err.println("O Codegen não conseguiu iniciar, verifique se a porta "+CodegenServer.PORTA+" está livre.");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendStop() {
        if(!codegenAvailabilityCheck()){
            System.err.println("O Codegen não está rodando.");
            return;
        }
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL("http://localhost:"+CodegenServer.PORTA+"/api/turnoff");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
               result.append(line);
            }
            rd.close();
        } catch(Exception e){
            System.err.println("O Codegen parou com sucesso.");
        }
    }
    
    public static boolean codegenAvailabilityCheck() {
        try (Socket s = new Socket("localhost", CodegenServer.PORTA)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
    }
    
}
