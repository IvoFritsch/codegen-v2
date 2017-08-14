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
public class ModifiableString{

    private String valor;

    public ModifiableString(String valor) {
        this.valor = valor;
    }

    public ModifiableString substring(int i) {
        return new ModifiableString(valor.substring(i));
    }

    public ModifiableString substring(int i, int i1) {
        return new ModifiableString(valor.substring(i, i1));
    }

    public ModifiableString replace(CharSequence cs, CharSequence cs1) {
        return new ModifiableString(valor.replace(cs, cs1));
    }

    public ModifiableString toLowerCase() {
        return new ModifiableString(valor.toLowerCase());
    }

    public ModifiableString toUpperCase() {
        return new ModifiableString(valor.toUpperCase());
    }

    public ModifiableString trim() {
        return new ModifiableString(valor.trim());
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
