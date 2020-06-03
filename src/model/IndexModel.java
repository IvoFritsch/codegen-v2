/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.gson.annotations.Expose;
import java.util.List;

/**
 *
 * @author pedro
 */
public class IndexModel {
    
    @Expose
    public String nome;
    
    @Expose
    public List<IndexField> campos;

    public String getNome() {
        return nome;
    }

    public List<IndexField> getCampos() {
        return campos;
    }
    
    public class IndexField {
        
        @Expose
        public String nome;

        public String getNome() {
            return nome;
        }
        
    }
          
}
