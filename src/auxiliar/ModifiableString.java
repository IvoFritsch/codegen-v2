/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auxiliar;

import java.text.Normalizer;

/**
 *
 * @author ivoaf
 */
public class ModifiableString{

    private String valor;

    public ModifiableString(String valor) {
        this.valor = valor;
    }

    public ModifiableString removeAccents() {
      return new ModifiableString(Normalizer
        .normalize(valor, Normalizer.Form.NFD)
        .replaceAll("[^\\p{ASCII}]", ""));
    }
    
    public ModifiableString removeNonAlphanumericChars() {
      return new ModifiableString(valor.replaceAll("[^A-Za-z0-9 ]", ""));
    }
    
    public ModifiableString removeRepeatedSpaces() {
      return new ModifiableString(valor.trim().replaceAll(" +", " "));
    }
    
    @Override
    public String toString() {
      return valor;
    }
}
