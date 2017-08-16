/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import auxiliar.Utils;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrador
 */
public class ProccessLog {
    
    @Expose        // Model      Template    Mensagem
    private final Map<String,Map<String,List<String>>> mensagens = new HashMap<>();
    
    private Map<String,List<String>> logModelAtual;
    private List<String> logTemplateAtual;
    
    public void startNewModel(String nome){
        logModelAtual = new HashMap<>();
        mensagens.put(nome, logModelAtual);
    }
    
    public void startNewTemplate(String nome){
        if(logModelAtual == null) return;
        logTemplateAtual = new ArrayList<>();
        logModelAtual.put(nome, logTemplateAtual);
    }
    
    public void putMessage(String mensagem){
        if(logTemplateAtual == null) return;
        logTemplateAtual.add(mensagem);
    }
    
    public String toJson(){
        return Utils.toJsonOnlyExpose(this);
    }
}
