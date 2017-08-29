/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proccessor;

import proccessor.CodegenFieldConfig;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import proccessor.TemplatesModel;

/**
 *
 * @author Ivo Fritsch
 */
public class CodegenFieldConfigEndpoint {
    
    @Expose
    private Map<String,String> subconfs;
    @Expose
    private String nome;
    @Expose
    private String valor;
    @Expose
    private boolean temSubConfs;
    
    private CodegenFieldConfig fieldConfig;

    public String getValor() {
        return valor;
    }
    
    public boolean temSubconfigs() {
        return temSubConfs;
    }
    
    public List<String> getSubconfigs(){
        List<String> saida = new ArrayList<>();
        if(!temSubConfs) return saida;
        subconfs.forEach((c,v)->saida.add(c));
        return saida;
    }
    
    public boolean temSubconfig(String nome){
        if(!temSubConfs) return false;
        return subconfs.containsKey(nome);
    }
    
    public String getValorSubConfig(String subconfig){
        if(!temSubconfig(subconfig)) return "";
        return subconfs.get(subconfig);
    }
    
    public boolean subconfigTemValorIgualA(String subconfig, String valor){
        if(!temSubconfig(subconfig)) return false;
        return subconfs.get(subconfig).equals(valor);
    }

    public String getNome() {
        return nome;
    }

    public CodegenFieldConfig getFieldConfig() {
        return fieldConfig;
    }
    
    void preparaEstrutura(CodegenFieldConfig fieldConfig){
         this.fieldConfig = fieldConfig;
    }
    
}
