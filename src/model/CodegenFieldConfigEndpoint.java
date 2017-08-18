/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.annotations.Expose;
import java.util.Map;

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

    public String getValor() {
        return valor;
    }
}
