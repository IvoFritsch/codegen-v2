/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.annotations.Expose;

/**
 *
 * @author Ivo Fritsch
 */
public class ServerField {    
    
    @Expose
    private String nome;
    
    @Expose
    private String tipoAsString;
        
    @Expose
    private CodegenFieldConfig config;
    
}
